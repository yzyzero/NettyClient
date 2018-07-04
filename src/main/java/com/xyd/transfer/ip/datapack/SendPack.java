package com.xyd.transfer.ip.datapack;

import org.apache.commons.codec.binary.Hex;

import com.xyd.transfer.ip.PackEncodeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.tass.yingjgb.YingJGBCALLDLL;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;

public abstract class SendPack extends BasePack {
	private static final String ZERO_MASK = "000000000000000000";
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	public ByteBuf toBuffer() throws PackEncodeException {
		ByteBuf buf = Unpooled.buffer(54);
		try {
			int packLen = 30 + getTargetNumber()*9;
			
			// 消息头 12
			buf.writeShort(0xFEFD);
			buf.writeShort(getProtocolVersion());
			buf.writeInt(getSessionID());
			buf.writeByte(getType().ordinal());
			buf.writeByte(0);
			buf.writeShort(packLen);
			
			// 消息体 12 + 2 + n*9 + X
			buf.writeBytes(Hex.decodeHex(getSource()));
			buf.writeShort(getTargetNumber());
			for (String target : getTargets()) {
				if(target.length() == 18) {
					buf.writeBytes(Hex.decodeHex(target));
				} else {
					String t = "0000" + target;
					t = t + ZERO_MASK.substring(0, 18 - t.length());
					
					buf.writeBytes(Hex.decodeHex(t));
				}
			}
			buf.writeByte(getOperation().getCode());
			buf.writeShort(0);
			int operaLen = generateBuf(buf);
			buf.setShort(packLen - 6, operaLen);
			packLen += operaLen;
			
			// TODO 计算数字签名
			byte[] hbBuf = new byte[packLen];
			System.arraycopy(buf.array(), 0, hbBuf, 0, packLen);
			
			try {
				byte[] bSignature = YingJGBCALLDLL.Base64Decode(YingJGBCALLDLL.platformCalculateSignature(1, hbBuf));
				byte[] signNumber = new byte[6];
				byte [] signature = new byte[bSignature.length -10];

				setSignatureTime((int) System.currentTimeMillis());
				
				System.arraycopy(bSignature, 4, signNumber, 0, 6);
				setSignatureNumber(YingJGBCALLDLL.byteToHexString(signNumber));
				
				System.arraycopy(bSignature, 10, signature, 0, signature.length);
				setSignature(signature);
			} catch (Exception e) {
				//e.printStackTrace();
				logger.info("签名错误");
				//System.out.println("签名错误");
			}
			
			// 验证数据
			if(useSignature()) {
				int signLen = 10 + getSignatureLength();
				buf.setByte(10, 1);
				packLen += signLen + 2;
				buf.writeShort(signLen);
				buf.writeInt(getSignatureTime());
				buf.writeBytes(Hex.decodeHex(getSignatureNumber()));
				buf.writeBytes(getSignature());
			}
			buf.setShort(10, packLen);
			CRC32.generateCRC(buf);
			
			// TODO 检测数据 合法性
			if(buf.readableBytes() != packLen)
				throw new PackEncodeException("数据长度与实际buffer区数据长度不一致");
			
			return buf;
		} catch (Exception e) {
			ReferenceCountUtil.release(buf);
			throw new PackEncodeException(e);
		}
	}
	
	protected abstract PackType getType();
	protected abstract int generateBuf(ByteBuf buf) throws Exception;
	
	public int getSessionID() {
		int sessionID = super.getSessionID();
		if(sessionID < 1) sessionID = GenerateSeesionCode();
		return sessionID;
	}
	
	private static Integer g_SeesionID = 1;
	private static synchronized int GenerateSeesionCode() {
		if (g_SeesionID < 0xFFFFFFFE)
			g_SeesionID++;
		else
			g_SeesionID = 1;

		return g_SeesionID;
	}
}
