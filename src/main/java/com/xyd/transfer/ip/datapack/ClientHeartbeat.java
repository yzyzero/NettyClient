package com.xyd.transfer.ip.datapack;

import org.apache.commons.codec.binary.Hex;

import io.netty.buffer.ByteBuf;

public class ClientHeartbeat extends SendPack {
	
	/*----------------------------------- 5.3.1 终端心跳 ---------------------------------------------
	序号	语法		长度（字节）	编码规则
	1	终端工作状态		1	终端当前所处工作状态。1：空闲，终端在线，但未进行任何广播操作；2：工作，终端进行广播中；3：故障，终端处于故障状态。
	2	首次注册标识		1	终端通电启动后第一次注册标识。1：首次注册；2：非首次注册。
	3	物理地址编码长度	1	物理地址编码的长度。
	4	物理地址编码	物理地址编码长度	终端唯一标识，出厂时生成，固定不变。
	--------------------------------------------------------------------------------------------*/
	private Status status;
	private byte first;
	private String physicalAddress;
	
	public ClientHeartbeat(RawPack pack) {
		this(pack.getSessionID(), pack.getSource(), pack.getTargets(), pack.getBuf());
	}
	
	public ClientHeartbeat(int sessionID, String source, String[] targets, ByteBuf frame) {
		this.setSessionID(sessionID);
		this.setSource(source);
		this.setTargets(targets);
		
		this.status = Status.valueOf(frame.readByte());
		this.first = frame.readByte();
		
		int pLen = frame.readByte();
		byte bPhyAddress[] = new byte[pLen];
		frame.readBytes(bPhyAddress);
		physicalAddress = Hex.encodeHexString(bPhyAddress);
	}
	
	public ClientHeartbeat(int sessionID, String source, String[] targets, Status status, byte first, String physicalAddress) {
		this.setSessionID(sessionID);
		this.setSource(source);
		this.setTargets(targets);
		
		this.status = status;
		this.first = first;
		this.physicalAddress = physicalAddress;
	}

	@Override
	protected PackType getType() {
		return PackType.REQUEST;
	}

	@Override
	protected int generateBuf(ByteBuf buf) throws Exception {
		int contentLen = 1;
		buf.writeByte(status.ordinal());
		buf.writeByte(first);
		byte [] bb = Hex.decodeHex(physicalAddress);
		buf.writeByte(bb.length);
		buf.writeBytes(bb);
		contentLen = 3+bb.length;
		return contentLen;
	}

	@Override
	public OperationType getOperation() {
		return OperationType.HEARTBEAT;
	}
}
