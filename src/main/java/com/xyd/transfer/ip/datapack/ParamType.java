package com.xyd.transfer.ip.datapack;

public enum ParamType {
	resourceCode(1, "resourceCode"),
	physicalAddress(2, "physicalAddress");
	
	private int code;
	private String name;
	
	ParamType(int code, String name){
		this.code = code;
		this.name = name;
	}
}
