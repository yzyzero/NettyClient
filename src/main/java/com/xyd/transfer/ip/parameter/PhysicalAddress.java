package com.xyd.transfer.ip.parameter;

import io.netty.buffer.ByteBuf;

public class PhysicalAddress extends BCDCode {

	protected PhysicalAddress(Enumerate type) {
		super(type, 0);
	}

	@Override
	public int writeBytes(ByteBuf frame) throws Exception {
		len  = value.length + 1;
		frame.writeByte(getCode()).writeByte(len).writeByte(value.length).writeBytes(value);
		return len + 2;
	}

	@Override
	public void parse(ByteBuf frame) throws IPTransmitException {
		len = frame.readByte();
		value = new byte[len];
		frame.readBytes(value);
	}
}
