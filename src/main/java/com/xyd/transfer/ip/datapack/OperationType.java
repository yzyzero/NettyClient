package com.xyd.transfer.ip.datapack;

public enum OperationType {
	NONE(0),
	BROADCAST_OPEN(0x1),
	BROADCAST_CLOSE(0x2),
	BROADCAST_ACCESS_OPEN(0x40),
	BROADCAST_ACCESS_CLOSE(0x41),
	HEARTBEAT(0x10),
	UPGRADING(0xA1),
	TERMINAL_STATUS_QUERY(0x11),
	TERMINAL_CONFIG(0x12),
	TERMINAL_FAILURE_RECOVERY(0x13),
	TERMINAL_TASK_SWITCH(0x14),
	TERMINAL_BROADCAST_REPORT(0x15),
	TERMINAL_BROADCAST_QUERY(0x20),
	TERMINAL_AUTHENTICATION(0x30);
	
	private byte code; 
	OperationType(int code){
		this.code = (byte) code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static OperationType valueOf(int code) {
		switch (code) {
		case 0x1: return BROADCAST_OPEN;
		case 0x2: return BROADCAST_CLOSE;
		case 0x40: return BROADCAST_ACCESS_OPEN;
		case 0x41: return BROADCAST_ACCESS_CLOSE;
		case 0x10: return HEARTBEAT;
		case 0x11: return TERMINAL_STATUS_QUERY;
		case 0x12: return TERMINAL_CONFIG;
		case 0x13: return TERMINAL_FAILURE_RECOVERY;
		case 0x14: return TERMINAL_TASK_SWITCH;
		case 0x15: return TERMINAL_BROADCAST_REPORT;
		case 0x20: return TERMINAL_BROADCAST_QUERY;
		case 0x30: return TERMINAL_AUTHENTICATION;
		case 0xA1: return UPGRADING;
		default:
			return NONE;
		}
	}
}
