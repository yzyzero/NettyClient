package com.xyd.transfer.ip.parameter;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.netty.buffer.ByteBuf;

public class PServerAddress extends IPParameter {
	private byte mold;  // 1-IP  2-域名
	private String value;
	private int port;

	protected PServerAddress(Enumerate type) {
		super(type, 0);
	}

	public byte getMold() {
		return mold;
	}

	public String getValue() {
		return value;
	}

	public int getPort() {
		return port;
	}

	public void setValue(String ip, int port) {
		mold = 1;
		this.value = ip;
		this.port = port;
	}

	public void setUrl(String url) {
		mold = 2;
		this.value = url;
	}
	
	@Override
	public void setValue(String value) {
		String[] aval =  value.split("\\|");
		mold = (byte) Integer.parseInt(aval[0]);
		if(mold == 1) {
			String[] bval = aval[1].split(":");
			this.value = bval[0];
			this.port = Integer.parseInt(bval[1]);
		} else {
			this.value = aval[1];
		}
	}

	@Override
	public void parse(ByteBuf frame) throws IPTransmitException {
		mold = frame.readByte();
		if(mold==1) {
			byte[] b = new byte[4];
			frame.readBytes(b);
			try { 
				value = InetAddress.getByAddress(b).toString().substring(1); 
			} catch (UnknownHostException e) {
				throw new IPTransmitException(13, e.getMessage());
			}
			
			port = frame.readShort();
		} else {
			byte[] b = new byte[len-1];
			frame.readBytes(b);
			value = new String(b);
		}
	}
	
	@Override
	public int writeBytes(ByteBuf frame) throws Exception {
		if(mold == 1) {
			len = 7;
			frame.writeByte(getCode()).writeByte(len).writeByte(mold)
				.writeBytes(InetAddress.getByName(value).getAddress())
				.writeShort(port);
		} else {
			byte[] bval = value.getBytes();
			len = bval.length + 1;
			frame.writeByte(getCode()).writeByte(len).writeByte(mold).writeBytes(bval);
		}
		
		return len + 2;
	}

	@Override
	public String toString() {
		if(mold==1) {
			return String.format("%d|%s:%d", mold, value, port);
		} else {
			return String.format("%d|%s", mold, value);
		}
	}
}
