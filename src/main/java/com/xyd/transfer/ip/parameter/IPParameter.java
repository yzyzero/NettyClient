package com.xyd.transfer.ip.parameter;

import io.netty.buffer.ByteBuf;

public abstract class IPParameter {
	protected final Enumerate type;
	protected int len;
	public IPParameter(Enumerate type, int len) {
		this.type = type;
		this.len = len;
	}

	protected byte[] getBytes() throws RuntimeException {
		throw new RuntimeException("不支持该方法");
	};

	public Enumerate getType() {
		return type;
	}

	public int getCode() {
		return type.getCode();
	}
	
	public int getInt() throws RuntimeException {
		throw new RuntimeException("不支持该方法");
	}
	
	public void readBuffer(ByteBuf frame) throws IPTransmitException {
		byte bLen = frame.readByte();
		if(len ==0) {
			len = bLen;
		} else if(bLen != len) {
			throw new IPTransmitException(13, "错误的IP数据包");
		}
		
		parse(frame);
	}
	
	public abstract int writeBytes(ByteBuf frame) throws Exception;
	public abstract void parse(ByteBuf frame) throws IPTransmitException;
	public abstract void setValue(String str) throws Exception;
}
