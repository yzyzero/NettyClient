package com.xyd;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class MyHex {
	public static void main(String[] args) {
		try {
			byte [] bb = Hex.decodeHex("069951010402000801");
			System.out.println(bb.length);
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
