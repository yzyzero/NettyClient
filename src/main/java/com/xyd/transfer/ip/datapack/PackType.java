package com.xyd.transfer.ip.datapack;

public enum PackType {
	NONE, REQUEST, RESPONSE;

    public static PackType valueOf(int value) { 
        switch (value) {  
        case 1:  
            return REQUEST;  
        case 2:  
            return RESPONSE;  
        default:  
            return NONE;  
        }  
    }
}
