package com.xyd.resource.model;


/*/ 
 本级	01
应急广播适配器	02
传输覆盖播出设备	03
终端	04
*/
public enum Subcategory {
	NONE("00", "未知"),
	SYSTEM("01", "系统"),
	ADAPTER("02", "适配器"),
	TRANSMISSION_COVERAGE_BROADCAST_EQUIPMENT("03", "传输覆盖播出设备"),
	RECEIVING_TERMINAL("04", "终端"),
	XYD("61", "接入设备");

	private String code;
	private String label;
	private Subcategory(String code, String label){
		this.code = code;
		this.label = label;
	}
	
	public String getCode(){
		return code;
	}

	public String getLabel() {
		return label;
	}

	public static Subcategory valueForCode(String code){
		Subcategory[] scs = values();
		for(Subcategory sc : scs){
			if(sc.getCode().equals(code)){
				return sc;
			}
		}
		return NONE;
	}
	
	public static Subcategory valueForLabel(String label){
		Subcategory[] scs = values();
		for(Subcategory sc : scs){
			if(sc.getLabel().equals(label)){
				return sc;
			}
		}
		return NONE;
	}
}
