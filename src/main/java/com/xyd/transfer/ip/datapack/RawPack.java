package com.xyd.transfer.ip.datapack;

import io.netty.buffer.ByteBuf;

public class RawPack extends BasePack {
	
	private ByteBuf buf;
	private PackType type;
	private OperationType operation;
	
	RawPack(PackType type, int sessionID, String source, String[] targets, OperationType operation, ByteBuf operationBuf) {
		this.setSessionID(sessionID);
		this.setSource(source);
		this.setTargets(targets);
		this.type = type;
		this.buf = operationBuf;
		this.operation = operation;
	}

	@Override
	public OperationType getOperation() {
		return operation;
	}

	public ByteBuf getBuf() {
		return buf;
	}

	public PackType getType() {
		return type;
	}
}
