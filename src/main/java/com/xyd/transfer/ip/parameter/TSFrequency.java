package com.xyd.transfer.ip.parameter;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xyd.resource.model.QAM;

import io.netty.buffer.ByteBuf;

public class TSFrequency extends IPParameter {
	private Map<Integer, Frequency> freqs = new TreeMap<>();
	
	public TSFrequency(Enumerate type) {
		super(type, 0);
	}

	@Override
	public int writeBytes(ByteBuf frame) throws Exception {
		len = freqs.size() * 10 + 1;
		frame.writeByte(getCode()).writeByte(len);
		Iterator<Integer> it  = freqs.keySet().iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			Frequency value = freqs.get(key);
			frame.writeByte(key).writeInt(value.freq).writeInt(value.symbolRate).writeByte(value.qam.ordinal());
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
			freq.freq = frame.readInt();
			freq.symbolRate = frame.readInt();
			freq.qam = QAM.Definition.values()[frame.readByte()];
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
		public int freq;			//主频频率(KHZ)
		public int symbolRate;		//符号率(KBPS)
		public QAM.Definition qam;
	}
}
