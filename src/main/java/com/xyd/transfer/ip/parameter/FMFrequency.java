package com.xyd.transfer.ip.parameter;

import io.netty.buffer.ByteBuf;

public class FMFrequency extends IPParameter {
	private float command;			// 指令频点(MHz)
	private float program;			// 节目频点(MHz)

	protected FMFrequency(Enumerate type) {
		super(type, 6);
	}

	public float getCommand() {
		return command;
	}

	public float getProgram() {
		return program;
	}

	@Override
	public int writeBytes(ByteBuf frame) throws Exception {
		frame.writeByte(getCode()).writeByte(len)
			.writeShort((short)command).writeByte(Math.round((command%1)*100))
			.writeShort((short)program).writeByte(Math.round((program%1)*100));
		return len + 2;
	}

	@Override
	public void parse(ByteBuf frame) throws IPTransmitException {
		command = frame.readShort() + frame.readByte()/100;
		program = frame.readShort() + frame.readByte()/100;
	}

	@Override
	public void setValue(String str) throws Exception {
		String[] astr = str.split("/");
		if(astr.length == 2) {
			command = Float.parseFloat(astr[0]);
			program = Float.parseFloat(astr[1]);
		} else {
			throw new Exception("FM 当前频率参数格式错误");
		}
	}

	@Override
	public String toString() {
		return String.format("%.2f/%.2f", command, program);
	}
}
