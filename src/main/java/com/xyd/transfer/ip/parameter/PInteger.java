package com.xyd.transfer.ip.parameter;

import io.netty.buffer.ByteBuf;

public class PInteger extends IPParameter {
	private int value;
	
	protected PInteger(Enumerate type) {
		super(type, 4);
	}

	@Override
	public void parse(ByteBuf frame) throws IPTransmitException {
		value = frame.readInt();
	}
	
	@Override
	public int writeBytes(ByteBuf frame) throws Exception {
		frame.writeByte(getCode()).writeByte(len).writeInt(value);
		return 6;
	}

	@Override
	public void setValue(String str) throws Exception {
		value = Integer.parseInt(str);
	}
	
	@Override
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	public int getInt() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
