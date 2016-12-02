package com.simulation.terminal.util;

import java.io.UnsupportedEncodingException;

public final class Convert {
	private final static byte[] hex = "0123456789ABCDEF".getBytes();

	/**
	 * 字节数组转换到十六进制字符串
	 * 
	 * @param bytes
	 *            {@link Byte}字节数组
	 * @return {@link String} 十六进制字符串
	 */
	public static String bytesToHexString(byte[] bytes) {
		byte[] buff = new byte[2 * bytes.length];
		for (int i = 0, length = bytes.length; i < length; i++) {
			buff[2 * i] = hex[(bytes[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[bytes[i] & 0x0f];
		}
		return new String(buff);
	}

	/**
	 * 十六进制字符串转换到字节数组
	 * 
	 * @param hexstr
	 *            {@link String} 十六进制字符串
	 * @return {@link Byte}[]字节数组
	 */
	public static byte[] hexStringToBytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0, length = b.length; i < length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);
			b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return b;
	}

	private static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}

	/**
	 * 十进制转十六进制
	 * 
	 * @param number
	 *            int 十进制
	 * @param x
	 *            int 位数
	 * @return
	 */
	public static String decimalToHexadecimal(long number, int x) {
		String hex = Long.toHexString(number).toUpperCase();
		return preFillZero(hex, x);
	}

	/**
	 * 前面填充0
	 * 
	 * @param text
	 *            {@link Object} 需要补0的对象
	 * @param length
	 *            {@link Integer} 补0后的长度
	 * @return {@link String}
	 */
	public static String preFillZero(Object text, int length) {
		StringBuilder builder = new StringBuilder(length);
		if (text == null) {
			for (int i = 0; i < length; i++)
				builder.append("0");
		} else {
			for (int i = String.valueOf(text).length(); i < length; i++) {
				builder.append("0");
			}
			builder.append(text);
		}
		return builder.toString();
	}
	
	
	/**
	 * 后面填充0
	 * 
	 * @param text
	 *            {@link Object} 需要补0的对象
	 * @param length
	 *            {@link Integer} 补0后的长度
	 * @return {@link String}
	 */
	public static String sufFillZero(Object text, int length) {
		StringBuilder builder = new StringBuilder(length);
		if (text == null) {
			for (int i = 0; i < length; i++)
				builder.append("0");
		} else {
			builder.append(text);
			for (int i = String.valueOf(text).length(); i < length; i++) {
				builder.append("0");
			}
		}
		return builder.toString();
	}
	
	/**
	 * 整形转字节
	 * 
	 * @param number
	 * @param size
	 * 
	 * @return
	 */
	public static byte[] longTobytes(long number, int size) {
		byte[] b = new byte[size];
		for (int i = 0; i < size; i++) {
			b[i] = (byte) (number >> 8 * (size - i - 1) & 0xFF);
		}
		return b;
	}

	/**
	 * 字节转整形<br>
	 * 最大支持4字节
	 * 
	 * @param bytes
	 * @param size
	 * @return
	 */
	public static long byte2Long(byte[] bytes, int size) {
		long intValue = 0;
		for (int i = 0; i < bytes.length; i++) {
			intValue += (bytes[i] & 0xFF) << (8 * (size - i - 1));
		}
		return intValue;
	}

	public static int byte2Int(byte[] bytes, int size) {
		int intValue = 0;
		for (int i = 0; i < bytes.length; i++) {
			intValue += (bytes[i] & 0xFF) << (8 * (size - i - 1));
		}
		return intValue;
	}

	/**
	 * 整形转字节
	 * 
	 * @param number
	 * @param size
	 * 
	 * @return
	 */
	public static byte[] intTobytes(int number, int size) {
		byte[] b = new byte[size];
		for (int i = 0; i < size; i++) {
			b[i] = (byte) (number >> 8 * (size - i - 1) & 0xFF);
		}
		return b;
	}
	
	public static String mixStr2Hex(String str) {
		byte[] bytes = null;
		try {
			bytes = str.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			sb.append("0123456789ABCDEF".charAt((bytes[i] & 0xf0) >> 4));
			sb.append("0123456789ABCDEF".charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}
	public static void main(String[] args) {
		try {
			System.err.println(new String(Convert.hexStringToBytes("24263131383131324D554C4C494E4B2A4936312E3133322E3232312E352A422A5032313030302A53312A"),"GBK"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
