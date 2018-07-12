package com.xyd.transfer.ip.parameter;

import io.netty.buffer.ByteBuf;

public class PShort extends IPParameter {
	private short value;
	
	protected PShort(Enumerate type) {
		super(type, 2);
	}


	@Override
	public void parse(ByteBuf frame) throws IPTransmitException {
		value = frame.readShort();
	}
	
	@Override
	public int writeBytes(ByteBuf frame) throws Exception {
		frame.writeByte(getCode()).writeByte(len).writeShort(value);
		return 4;
	}

	@Override
	public String toString() {
		return Short.toString(value);
	}

	public void setValue(short value) {
		this.value = value;
	}
	
	@Override
	public void setValue(String str) throws Exception {
		value = Short.parseShort(str);
	}
	
	@Override
	public int getInt() {
		return value;
	}
}
