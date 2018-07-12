package com.xyd.transfer.ip.parameter;

public class IPTransmitException extends Exception {
	private static final long serialVersionUID = 1L;
	private int code = 0;
	
	public IPTransmitException(int code, String message) {
		super(message);
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}
