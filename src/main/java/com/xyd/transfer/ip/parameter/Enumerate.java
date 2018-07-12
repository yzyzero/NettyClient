package com.xyd.transfer.ip.parameter;

/*
默认音量	1	0x01
本地地址	12	0x02
回传地址	14	0x03
终端资源编码	9	0x04
物理地址编码	可变	0x05
工作状态	1	0x06
故障代码	1	0x07
设备类型	2	0x08
硬件版本号	4	0x09
软件版本号	4	0x0A
信号状态	2	0x0B
有线频率（锁定频率）	5	0x0C
FM频点扫描列表	可变	0x0D
FM当前频点	6	0x0E
FM维持指令模式	3	0x0F
 */
public enum Enumerate {
	NONE(0), 
	VOLUME(1),
	LOCAL_ADDRESS(2), 
	ECHO_ADDRESS(3), 
	RESOURCE_CODE(4), 
	PHYSICAL_ADDRESS(5), 
	STATUS(6), 
	FAULT_CODE(7),
	TYPE(8),
	HARDWARE_VERSION(9),
	SOFTWARE_VERSION(10),
	SIGNAL_STATE(11),
	TS_FREQUENCY(12),
	FM_FREQUENCY_LIST(13),
	FM_FREQUENCY(14),
	FM_COMMAND_MODE(15);
	
	private int code;
	
	private Enumerate(int code) {
		this.code = code;
	}
	
	public IPParameter createInstance() {
		switch (code) {
		case 1: return new PByte(this);
		case 2: return new IPInfo(this);
		case 3: return new PServerAddress(this);
		case 4: return new BCDCode(this, 12);
		case 5: return new PhysicalAddress(this);
		case 6: return new PByte(this);
		case 7: return new PByte(this);
		case 8: return new PShort(this);
		case 9: return new PInteger(this);
		case 10: return new PInteger(this);
		case 11: return new PShort(this);
		case 12: return new TSFrequency(this);
		case 13: return new FMFrequencyList(this);
		case 14: return new FMFrequency(this);
		case 15: return new FMCommandMode(this);
		default: return new PBytes(this);
		}
	}
	
	public int getCode() {
		return code;
	}

	public static byte[] ALL = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
	public static byte[] IP = {1,2,3,4,5,6,7,8,9,10};
	public static byte[] TS = {1,2,3,4,5,6,8,9,10,11,12};
	public static byte[] FM = {1,2,3,4,5,6,8,9,10,11,13,14,15};
	public static Enumerate valueOf(int code) {
		switch (code) {  
        case 1:  
            return VOLUME;
        case 2:  
            return LOCAL_ADDRESS;  
        case 3:  
            return ECHO_ADDRESS;  
        case 4:  
            return RESOURCE_CODE; 
        case 5:  
            return PHYSICAL_ADDRESS;
        case 6:  
            return STATUS; 
        case 7:  
            return FAULT_CODE;
        case 8:  
            return TYPE;  
        case 9:  
            return HARDWARE_VERSION;  
        case 10:  
            return SOFTWARE_VERSION;  
        case 11:  
            return SIGNAL_STATE; 
        case 12:  
            return TS_FREQUENCY;
        case 13:  
            return FM_FREQUENCY_LIST; 
        case 14:  
            return FM_FREQUENCY;
        case 15:  
            return FM_COMMAND_MODE;
        default:  
            return NONE;  
        }
    }
}