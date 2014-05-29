package com.mslibs.utils;

import java.security.MessageDigest;

public class MD5 {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"a", "b", "c", "d", "e", "f" };

	private static String byteArrayToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bytes) {
			sb.append(byteToHexString(b));
		}
		return sb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 将字符串加密成MD5字符串
	 * 
	 * @param origin
	 *            需要加密的字符串
	 * @return 加密后的字符串
	 */
	public static String MD5Encode(String origin) {
		String ret = null;
		try {
			ret = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			ret = byteArrayToHexString(md.digest(ret.getBytes()));
		} catch (Exception e) {
		}
		return ret;
	}

}