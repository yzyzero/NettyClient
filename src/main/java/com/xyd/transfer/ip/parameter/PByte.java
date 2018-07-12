package com.xyd.transfer.ip.parameter;

import io.netty.buffer.ByteBuf;

public class PByte extends IPParameter {
	private byte value;
	
	protected PByte(Enumerate type) {
		super(type, 1);
	}

	public void setValue(int value) {
		this.value = (byte) value;
	}
	
	@Override
	public int getInt() {
		return value;
	}
	
	@Override
	public void parse(ByteBuf frame) {
		value = frame.readByte();
	}
	
	@Override
	public int writeBytes(ByteBuf frame) throws Exception {
		frame.writeByte(getCode()).writeByte(len).writeByte(value);
		return 3;
	}
	
	@Override
	public void setValue(String str) {
		value = Byte.parseByte(str);
	}

	@Override
	public String toString() {
		return Byte.toString(value);
	}
}
