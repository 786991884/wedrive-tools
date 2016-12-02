package com.simulation.terminal.util;

import java.util.ArrayList;
import java.util.List;

public class PacketProcessing {
	public static byte[] escapeByte = null;
	public static byte[][] toEscapeByte = null;
	static {
		toEscapeByte = new byte[2][2];
		toEscapeByte[0][0] = 0x7D;
		toEscapeByte[0][1] = 0x01;
		toEscapeByte[1][0] = 0x7D;
		toEscapeByte[1][1] = 0x02;
		escapeByte = new byte[2];
		escapeByte[0] = 0x7D;
		escapeByte[1] = 0x7E;
	}
	
	/**
	 * 数据分包 <br>
	 * 如果最小包长为0,则不做验证
	 * 
	 * @param bytes
	 *            {@link Byte}[] 源数据
	 * @param pkBegin
	 *            {@link Byte} 包头
	 * @param pkEnd
	 *            {@link Byte} 包尾
	 * @param leastPkLength
	 *            {@link Integer} 最小包长
	 * @return {@link List}<{@link Byte}[]>
	 */
	public static List<byte[]> subpackage(byte[] bytes, byte pkBegin,
			byte pkEnd, int leastPkLength) {
		List<byte[]> result = new ArrayList<byte[]>();
		int index = -1;
		for (int i = 0, length = bytes.length; i < length; i++) {
			if (index == -1) {// 寻找包头
				if (pkBegin == bytes[i]) {
					index = i;
				}
			} else {
				if (pkEnd == bytes[i]) {// 寻找包尾
					if (leastPkLength > 0) {// 是否做最小包长验证
						if (i - index + 1 < leastPkLength) {
							index = i;
						} else {
							result.add(ArraysUtils.subarrays(bytes, index, i
									- index + 1));
							index = -1;
						}
					} else {
						result.add(ArraysUtils.subarrays(bytes, index, i
								- index + 1));
						index = -1;
					}
				}
			}
		}
		if (index != -1)
			result.add(ArraysUtils
					.subarrays(bytes, index, bytes.length - index));
		return result;
	}
	
	/**
	 * 数据检验
	 * 
	 * @param data
	 *            {@link Byte}[] 源数据
	 * @param begin
	 *            {@link Integer} 开始字节
	 * @param end
	 *            {@link Integer} 结束字节
	 * @return {@link Byte}
	 */
	public static byte checkPackage(byte[] data, int begin, int end) {
		byte crc = 0x00;
		for (int i = begin ; i <= end; i++) {
			crc ^= data[i];
		}
		return crc;
	}
	
	/**
	 * 数据转义
	 * 
	 * @param data
	 *            {@link Byte} 源数据
	 * @param from
	 *            {@link Byte}[] 需要转义的字节
	 * @param to
	 *            {@link Byte}[][] 转义后的字节
	 * @return
	 */
	public static byte[] escape(byte[] data, byte[] from, byte[][] to) {
		// 统计转义次数
		int count = 0;
		for (int i = 0, length = data.length; i < length; i++) {
			for (byte b : from) {
				if (data[i] == b) {
					count++;
				}
			}
		}
		int index = 0;
		byte[] result = new byte[count + data.length];
		for (int i = 0, i_length = data.length; i < i_length; i++) {
			boolean isEscape = false;// 标识当前字节是否转义
			for (int j = 0, j_length = from.length; j < j_length; j++) {
				if (data[i] == from[j]) {
					result[index++] = to[j][0];
					result[index++] = to[j][1];
					isEscape = true;
				}
			}
			if (!isEscape) {
				result[index++] = data[i];
			}
		}
		return result;
	}

	/**
	 * 数据转义还原
	 * 
	 * @param data
	 *            {@link Byte} 源数据
	 * @param from
	 *            {@link Byte}[] 需要转义还原的字节
	 * @param to
	 *            {@link Byte}[][] 转义还原后的字节
	 * @return
	 */
	public static byte[] unEscape(byte[] data, byte[][] from, byte[] to) {
		int count = 0;// 累计还原次数
		byte[] temp_result = new byte[data.length];
		int index = 0;
		for (int i = 0, i_length = data.length; i < i_length; i++) {
			boolean isUnEscape = false;
			for (int j = 0, j_length = from.length; j < j_length; j++) {
				if (data[i] == from[j][0]) {
					if (i + 1 < i_length) {
						if (data[i + 1] == from[j][1]) {
							temp_result[index++] = to[j];
							i++;
							count++;
							isUnEscape = true;
						}
					}
				}
			}
			if (!isUnEscape) {
				temp_result[index++] = data[i];
			}
		}
		// 如果没有转义还原,直接返回,否则去除最后空节字数据
		if (count == 0) {
			return temp_result;
		}

		byte[] result = new byte[data.length - count];
		System.arraycopy(temp_result, 0, result, 0, result.length);
		return result;
	}
}
