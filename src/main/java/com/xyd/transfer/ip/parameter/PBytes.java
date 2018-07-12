package com.xyd.transfer.ip.parameter;

import org.apache.commons.codec.binary.Hex;

import io.netty.buffer.ByteBuf;

public class PBytes extends IPParameter {
	private byte[] value;
	
	protected PBytes(Enumerate type) {
		super(type, 0);
	}

	@Override
	public void parse(ByteBuf frame) {
		value = new byte[len];
		frame.readBytes(value);
	}
	
	@Override
	public int writeBytes(ByteBuf frame) throws Exception {
		if(value == null || value.length != len)
			throw new Exception("数据长度与定义的长度不符");
		
		frame.writeByte(getCode()).writeByte(len).writeBytes(value);
		return len + 2;
	}

	@Override
	public void setValue(String value) throws Exception {
		byte[] bs = Hex.decodeHex(value);
		if(len == 0) {
			len = bs.length;
		} else if(len != bs.length) {
			throw new Exception("数据长度与定义的长度不符");
		}
		this.value = bs;
	}

	@Override
	public String toString() {
		return Hex.encodeHexString(value);
	}
}
