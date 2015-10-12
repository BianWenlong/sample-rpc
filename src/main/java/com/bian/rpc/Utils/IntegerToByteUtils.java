package com.bian.rpc.Utils;
public class IntegerToByteUtils {

	public static byte[] intToBytes(int value, byte[] bytes, int offset) {
		bytes[offset + 3] = (byte) ((value >> 24) & 0xFF);
		bytes[offset + 2] = (byte) ((value >> 16) & 0xFF);
		bytes[offset + 1] = (byte) ((value >> 8) & 0xFF);
		bytes[offset] = (byte) (value & 0xFF);
		return bytes;
	}

	public static int bytesToInt(byte[] src, int offset) {
		int value;
		value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8)
				| ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
		return value;
	}
}
