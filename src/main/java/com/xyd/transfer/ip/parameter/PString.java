package com.xyd.transfer.ip.parameter;

import io.netty.buffer.ByteBuf;

public class PString extends IPParameter {
	private String value;
	
	public PString(Enumerate type, int len) {
		super(type, len);
	}

	@Override
	public void parse(ByteBuf frame) throws IPTransmitException {
		byte[] b = new byte[len];
		frame.readBytes(b);
		value = new String(b);
	}
	
	@Override
	public int writeBytes(ByteBuf frame) throws Exception {
		byte[] b = value.getBytes();
		len = b.length;
		frame.writeByte(getCode()).writeByte(len).writeBytes(b);
		return len + 2;
	}

	@Override
	public void setValue(String value) throws Exception {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
