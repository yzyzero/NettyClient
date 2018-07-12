package com.xyd.transfer.ip.parameter;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import io.netty.buffer.ByteBuf;

public class FMFrequencyList extends IPParameter {
	private Map<Integer, Frequency> freqs = new TreeMap<>(); 	// 指令频点列表(MHz)

	public FMFrequencyList(Enumerate type) {
		super(type, 0);
	}

	@Override
	public int writeBytes(ByteBuf frame) throws Exception {
		len = freqs.size() * 5 + 1;
		frame.writeByte(getCode()).writeByte(len).writeByte(freqs.size());
		Iterator<Integer> it = freqs.keySet().iterator();
		while(it.hasNext()) {
			int key = it.next();
			Frequency freq = freqs.get(key);
			frame.writeByte(key);
			frame.writeByte(freq.priority);
			frame.writeShort((short)freq.value);
			frame.writeByte(Math.round((freq.value%1)*100));
		}
		
		return len + 2;
	}

	@Override
	public void parse(ByteBuf frame) throws IPTransmitException {
		int n = frame.readByte();
		freqs.clear();
		for(int i = 0; i < n; i++) {
			int no = frame.readByte();
			Frequency freq = new Frequency();
			freq.priority = frame.readByte();
			freq.value = frame.readShort() + frame.readByte()/100;
			freqs.put(no, freq);
		}
	}

	@Override
	public void setValue(String str) throws Exception {
		freqs = JSON.parseObject(str, new TypeReference<TreeMap<Integer, Frequency>>(){});
	}

	@Override
	public String toString() {
		return JSON.toJSONString(freqs);
	}
	
	public static class Frequency {
		public int priority;
		private float value;
	}
}
