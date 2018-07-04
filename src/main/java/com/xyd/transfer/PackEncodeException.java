package com.xyd.transfer;

public class PackEncodeException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public PackEncodeException(String msg) {
		super(msg);
	}

	public PackEncodeException(Exception e) {
		super(e);
	}

}
