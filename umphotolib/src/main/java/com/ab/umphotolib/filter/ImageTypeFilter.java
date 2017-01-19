package com.ab.umphotolib.filter;

import java.io.IOException;
import java.io.InputStream;

/**
 * 图片类型的判断
 * Created by AB051788 on 2017/1/4.
 */
public class ImageTypeFilter {
	public static final String PNG = "PNG";
	public static final String JPEG = "JPEG";
	public static final String GIF = "GIF";
	public static final String BMP = "BMP";
	/**
	 * 获取图片的类型信息
	 * @param in 输入流
	 * @return  返回类型的字符串 "PNG"  "JPEG"  "GIF" "BMP"  null
	 * @see #getImageType(byte[])
	 */
	public static String getImageType(InputStream in) {
		if (in == null) {
			return null;
		}
		try {
			byte[] bytes = new byte[8];
			in.read(bytes);    //获取前8字节判断
			return getImageType(bytes);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 根据字节数组判断图片类型信息
	 *
	 * @param bytes
	 *            文件开始的2~8字节数组
	 * @return  返回类型的字符串 "PNG"  "JPEG"  "GIF" "BMP"  null
	 */
	public static String getImageType(byte[] bytes) {
		if (isJPEG(bytes)) {
			return JPEG;
		}
		if (isGIF(bytes)) {
			return GIF;
		}
		if (isPNG(bytes)) {
			return PNG;
		}
		if (isBMP(bytes)) {
			return BMP;
		}
		return null;
	}

	/**
	 *判断是否为JPEG
	 * @param b 数组
	 * @return true false
	 */
	private static boolean isJPEG(byte[] b) {
		if (b.length < 2) {
			return false;
		}
		return (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
	}

	/**
	 *判断是否为GIF
	 * @param b 数组
	 * @return true false
	 */
	private static boolean isGIF(byte[] b) {
		if (b.length < 6) {
			return false;
		}
		return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'
				&& (b[4] == '7' || b[4] == '9') && b[5] == 'a';
	}

	/**
	 *判断是否为PNG
	 * @param b 数组
	 * @return true false
	 */
	private static boolean isPNG(byte[] b) {
		if (b.length < 8) {
			return false;
		}
		return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78
				&& b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10
				&& b[6] == (byte) 26 && b[7] == (byte) 10);
	}

	/**
	 *判断是否为BMP
	 * @param b 数组
	 * @return true false
	 */
	private static boolean isBMP(byte[] b) {
		if (b.length < 2) {
			return false;
		}
		return (b[0] == 0x42) && (b[1] == 0x4d);
	}
}
