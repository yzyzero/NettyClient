package com.xyd.transfer.ip.parameter;

import org.apache.commons.codec.binary.Hex;

import io.netty.buffer.ByteBuf;

public class BCDCode extends IPParameter {
	byte[] value;

	protected BCDCode(Enumerate type, int len) {
		super(type, len);
	}

	@Override
	public int writeBytes(ByteBuf frame) throws Exception {
		frame.writeByte(getCode()).writeByte(len).writeBytes(value);
		return len + 2;
	}

	@Override
	public void parse(ByteBuf frame) throws IPTransmitException {
		value = new byte[len];
		frame.readBytes(value);
	}

	@Override
	public void setValue(String str) throws Exception {
		if(str == null || str.length() != len*2-1) {
			throw new Exception("资源编码格式错误");
		}
		value = Hex.decodeHex('F' + str);
	}

	@Override
	public String toString() {
		return Hex.encodeHexString(value);
	}
}
