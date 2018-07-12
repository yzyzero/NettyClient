package com.xyd.transfer.ip;

public enum Error {
	SUCCEED(0, "成功"),
	ERROR10(10, "未知通信协议类别错误"),
	ERROR11(11, "请求超时，对方无应答"),
	ERROR12(12, "协议版本不匹配"),
	ERROR13(13, "数据包解析错误"),
	ERROR14(14, "缺少必选参数"),
	ERROR15(15, "CRC 校验错误"),
	ERROR30(30, "未知系统类别错误"),
	ERROR31(31, "系统忙（电话或短信插拨通道被全部占用等情况）"),
	ERROR32(32, "没找到存储卡"),
	ERROR33(33, "读取文件失败"),
	ERROR34(34, "写入文件失败"),
	ERROR35(35, "UKey 未插入"),
	ERROR36(36, "UKey 非法（内容无法解析，或为非法拷贝Ukey）"),
	ERROR50(50, "未知数据验证类别错误"),
	ERROR51(51, "用户名密码错误（用于插播设备验证）"),
	ERROR52(52, "数字证书非法"),
	ERROR53(53, "输入超时（登录成功后长时间未操作）"),
	ERROR54(54, "参数非法（输入参数超出范围）"),
	ERROR55(55, "功能不支持（未实现指定功能）"),
	ERROR56(56, "短信格式非法（短信内容无法解析）"),
	ERROR57(57, "号码无效（电话或短信号码在系统中未配置）"),
	ERROR58(58, "内容非法（短信内容包含反动内容等）"),
	ERROR59(59, "资源编码无效（系统中找不到指定资源编码信息）70 未知终端类别错误"),
	ERROR71(71, "终端无效（系统中找不到指定终端信息）"),
	ERROR72(72, "设备离线"),
	ERROR73(73, "终端忙（业务所涉及所有终端被高优先级任务占用）");
	
	private byte code;
	private String description;
	Error(int code, String description){
		this.code = (byte) code;
		this.description = description;
	}
	public byte getCode() {
		return code;
	}
	public void setCode(byte code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public static Error valueOf(int code) {
		switch (code) {
		case 0: return SUCCEED;
		case 10: return ERROR10;
		case 11: return ERROR11;
		case 12: return ERROR12;
		case 13: return ERROR13;
		case 14: return ERROR14;
		case 15: return ERROR15;
		case 30: return ERROR30;
		case 31: return ERROR31;
		case 32: return ERROR32;
		case 33: return ERROR33;
		case 34: return ERROR34;
		case 35: return ERROR35;
		case 36: return ERROR36;
		case 50: return ERROR50;
		case 51: return ERROR51;
		case 52: return ERROR52;
		case 53: return ERROR53;
		case 54: return ERROR54;
		case 55: return ERROR55;
		case 56: return ERROR56;
		case 57: return ERROR57;
		case 58: return ERROR58;
		case 59: return ERROR59;
		case 71: return ERROR71;
		case 72: return ERROR72;
		case 73: return ERROR73;
		default: return null;
		}
	}
}
