package com.xyd.transfer.ip.parameter;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.netty.buffer.ByteBuf;

public class IPInfo extends IPParameter {
	private String address = "";
	private String netmask = "";
	private String gateway = "";

	protected IPInfo(Enumerate type) {
		super(type, 12);
	}
	
	public String getAddress() {
		return address;
	}

	public String getNetmask() {
		return netmask;
	}

	public String getGateway() {
		return gateway;
	}
	
	public void setValue(String address, String netmask, String gateway) {
		this.address = address;
		this.netmask = netmask;
		this.gateway = gateway;
	}

	@Override
	public void setValue(String value) {
		String[] s = value.split("/");
		if(s.length == 3) {
			address = s[0];
			netmask = s[1];
			gateway = s[2];
		}
	}
	
	@Override
	public void parse(ByteBuf frame) {
		byte[] b = new byte[4];
		frame.readBytes(b);
		try { address = InetAddress.getByAddress(b).toString().substring(1); } catch (UnknownHostException e) {}
		frame.readBytes(b);
		try { netmask = InetAddress.getByAddress(b).toString().substring(1); } catch (UnknownHostException e) {}
		frame.readBytes(b);
		try { gateway = InetAddress.getByAddress(b).toString().substring(1); } catch (UnknownHostException e) {}
	}
	
	@Override
	public int writeBytes(ByteBuf frame) throws Exception {
		frame.writeByte(getCode()).writeByte(len)
			.writeBytes(InetAddress.getByName(address).getAddress())
			.writeBytes(InetAddress.getByName(netmask).getAddress())
			.writeBytes(InetAddress.getByName(gateway).getAddress());
		
		return len + 2;
	}
	
	@Override
	public String toString() {
		return String.format("%s/%s/%s", address, netmask, gateway);
	}
}
