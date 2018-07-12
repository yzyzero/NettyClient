package com.xyd.transfer.ip.parameter;

import io.netty.buffer.ByteBuf;

public class FMCommandMode extends IPParameter {
	private boolean use = false;
	private short period = 0; // 单位：秒

	protected FMCommandMode(Enumerate type) {
		super(type, 3);
	}

	@Override
	public int writeBytes(ByteBuf frame) throws Exception {
		frame.writeByte(getCode()).writeByte(len).writeByte(use?1:0).writeShort(period);
		return len + 2;
	}

	@Override
	public void parse(ByteBuf frame) throws IPTransmitException {
		use = frame.readByte() != 0;
		period = frame.readShort();
	}

	@Override
	public void setValue(String str) throws Exception {
		String[] astr = str.split("/");
		use = Boolean.parseBoolean(astr[0]);
		period = Short.parseShort(astr[1]);
	}

	@Override
	public String toString() {
		return String.format("%b/%d", use, period);
	}
}
