package com.xyd.transfer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Hex;

import com.xyd.model.Category;
import com.xyd.transfer.ip.datapack.OperationType;
import com.xyd.transfer.ip.datapack.PackType;
import com.xyd.transfer.ip.datapack.ParamType;
import com.xyd.transfer.ip.datapack.RawPack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PackDecoder extends ByteToMessageDecoder {
	private static enum ReadStatus {HEAD_SIGN, HEAD, BODY};
	private static short HEAD_SIGN = (short) 0xFEFD;

	private ReadStatus readStatus = ReadStatus.HEAD_SIGN;
	private int length = 2;
	private int sessionID;
	private PackType type;
	private int authSign;
	
	private boolean firstPack = true;
	private final Map<Category, OperationManager<?>> m_EventHandlers;
//	private Map<ParamType,String> params;
	
	private String id;

	public PackDecoder(Map<Category, OperationManager<?>> handlers, String physicalAddress) {
		this.m_EventHandlers = handlers;
		this.id = physicalAddress;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		while(in.isReadable(length)) {
			if(readStatus.equals(ReadStatus.HEAD_SIGN)) {
	    		if(HEAD_SIGN == in.readShort()) {
	    			readStatus = ReadStatus.HEAD;
	    			length = 10;
	    		}
			} else if(readStatus.equals(ReadStatus.HEAD)) {
		    	in.readShort(); // 协议版本号
		    	sessionID = in.readInt();
		    	type = PackType.valueOf(in.readByte());
		    	authSign =  in.readByte();
		    	length = in.readShort();//跳过包长度
		    	
    			readStatus = ReadStatus.BODY;
    			length = length -12;
			}else if(readStatus.equals(ReadStatus.BODY)) {

		    	byte bResId[] = new byte[9];
		    	in.readBytes(bResId);
		    	String source = Hex.encodeHexString(bResId);

		    	// 判断是否第一个数据包;如果是第一个数据包则创建根据设备类型创建指令接收设备
		    	if(firstPack) {
		    		if(source != null && source.length() == 18) { 
		    			Category category = Category.valueForCode(source.substring(0, 2));
		    			if(category.equals(Category.ALL)) {
							System.out.println("资源编码的设备类型未定义：" + source);
							throw new Exception();
		    			} else {
			    			OperationManager<?> handler = m_EventHandlers.get(category);
			    			if(handler != null) {			    				
					    		ctx.channel().pipeline().addLast(handler.getProcessor((SocketChannel) ctx.channel(), source, id));
			    			} else { // 不做任何处理 返回异常，强制断开连接
					    		ctx.channel().pipeline().addLast(new OperationProcessor() {
									@Override
									protected void operate(RawPack pack) throws Exception {
										System.out.println("不接受设备“" + category.getLabel() + "”的连接" + category.name());
										throw new Exception();
									}
	
									@Override
									protected void fireOffline() {}
								});
			    			}
		    			}
		    		} else {
		    			throw new Exception("源地址错误");
		    		}
		    		
		    		firstPack = false;
		    	}
		    	
		    	int targetLen = in.readShort();
		    	String targets[] = new String[targetLen];
		    	for(int i = 0; i < targetLen; i++) {
		        	in.readBytes(bResId);
		        	targets[i] = Hex.encodeHexString(bResId);
		    	}
		    	
		    	OperationType operation = OperationType.valueOf(in.readByte());
		    	int operContentLen = in.readShort(); //业务数据长度值
		    	ByteBuf operContent = in.readBytes(operContentLen);
		    	
		    	RawPack pack =  new RawPack(type, sessionID, source, targets, operation, operContent);
		    	
		    	if(authSign == 1) {
		    		int signContentLen = in.readShort();
		    		int signatureTime = in.readInt();
		    		byte bSignNum[] = new byte[6];
		    		in.readBytes(bSignNum);
		    		byte signature[] = new byte[signContentLen -10];
		    		in.readBytes(signature);
		    		
		    		pack.setSignatureTime(signatureTime);
		    		pack.setSignatureNumber(Hex.encodeHexString(bSignNum));
		    		pack.setSignature(signature);
		    	}
		    	
		    	in.skipBytes(4);  // 跳过CRC32
		    	
		    	if(pack != null) {
		    		out.add(pack);
		    	}
		    	
    			readStatus = ReadStatus.HEAD_SIGN;
    			length = 2;
			}
		}
	}
}
