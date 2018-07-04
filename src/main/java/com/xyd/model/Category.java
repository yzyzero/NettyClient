package com.xyd.model;
/**
 * 应急广播平台资源分类
 * @author hhq
 * @create 2016-7-15
 */
public enum Category {
	ALL("00","所有类型"),
	/**
	 * 应急广播平台
	 */
	EMERGENCY_BROADCAST_PLATFORM("01","应急广播平台"),
	/**
	 * 电台/电视台
	 */
	RADIO_TELEVISION("02","电台/电视台"),
	/**
	 * 传输覆盖台站
	 */
	TRANSMISSION_COVERAGE_STATIONS("03","传输覆盖台站"),
	/**
	 * 传输覆盖适配设备
	 */
	TRANSMISSION_COVER_ADAPTER_DEVICE("04","传输覆盖适配设备"),
	/**
	 * 传输覆盖播出设备
	 */
	TRANSMISSION_COVERAGE_BROADCAST_EQUIPMENT("05","传输覆盖播出设备"),
	/**
	 * 接收终端
	 */
	RECEIVING_TERMINAL("06","接收终端"),
	/**
	 * 应急广播监管系统
	 */
	EMERGENCY_BROADCAST_REGULATORY_SYSTEM("07","应急广播监管系统"),
	/**
	 * 应急广播前端采集设备
	 */
	FRONTEND_ACQUISITION_EQUIPMENT("61","应急广播前端采集设备");
	
	private String code;
	private String label;
	private Category(String code, String label){
		this.code = code;
		this.label = label;
	}
	
	public String getCode(){
		return code;
	}

	public String getLabel() {
		return label;
	}
	
	public static Category valueOf(int code){
		Category[] pcs = values();
		if(0 < code && code < pcs.length){
			return Category.values()[code];
		} else {
			return ALL;
		}
	}

	
	public static Category valueForCode(String code){
		switch (code) {
		case "01": return EMERGENCY_BROADCAST_PLATFORM;
		case "02": return RADIO_TELEVISION;
		case "03": return TRANSMISSION_COVERAGE_STATIONS;
		case "04": return TRANSMISSION_COVER_ADAPTER_DEVICE;
		case "05": return TRANSMISSION_COVERAGE_BROADCAST_EQUIPMENT;
		case "06": return RECEIVING_TERMINAL;
		case "07": return EMERGENCY_BROADCAST_REGULATORY_SYSTEM;
		default: return ALL;
		}
	}
}
