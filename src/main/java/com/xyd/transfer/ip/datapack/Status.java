package com.xyd.transfer.ip.datapack;

/**
 * 0: 离线
 * 1：空闲，终端在线，但未进行任何广播操作；
 * 2：工作，终端进行广播中；
 * 3：故障，终端处于故障状态；
 * 4：未注册，终端未通过平台注册。
 */
public enum Status {
	NONE, IDLE, BROADCAST, FAILURE, OFFLINE, UNREGISTERED;

    public static Status valueOf(int value) { 
        switch (value) {
        case 1:  
            return IDLE; 
        case 2:  
            return BROADCAST;
        case 3:  
            return FAILURE;  
        case 4:  
            return OFFLINE; 
        case 5:  
            return UNREGISTERED;   
        default:  
            return NONE;
        }  
    }
}
