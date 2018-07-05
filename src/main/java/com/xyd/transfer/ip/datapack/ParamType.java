package com.xyd.transfer.ip.datapack;

public enum ParamType {
	resourceCode(1, "resourceCode"),
	physicalAddress(2, "physicalAddress"),
	source(3, "source"),
	targets(4, "targets");
	
	private int code;
	private String name;
	
	ParamType(int code, String name){
		this.setCode(code);
		this.setName(name);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
