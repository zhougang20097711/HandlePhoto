package com.ab.umphotolib.filter;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * 像素矩阵的颜色值控制
 * 通过修改像素矩阵中对应位置的颜色值
 * pixelsArray
 * Created by AB051788 on 2017/1/10.
 */
public class ImagePixelsArrayHandle {
	private Bitmap srcBitmap; //原图
	private Bitmap dstBitmap;  //目标图

	private int width;  //宽
	private int height;  //高

	protected int[] colorArray;  //像素矩阵

	/**
	 * 构造方法
	 * @param bmp
	 */
	public ImagePixelsArrayHandle(Bitmap bmp) {
		srcBitmap = bmp;
		width = bmp.getWidth();
		height = bmp.getHeight();
		dstBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		initColorArray();
	}

	/**
	 *复制一个
	 * @return
	 */
	public ImagePixelsArrayHandle clone() {
		return new ImagePixelsArrayHandle(this.srcBitmap);
	}

	/**
	 * 初始化像素矩阵
	 */
	private void initColorArray() {
		colorArray = new int[width * height];
		srcBitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
	}

	/**
	 * 获取对应坐标颜色值
	 * @param x
	 * @param y
	 * @return
	 */
	public int getPixelColor(int x, int y) {
		return colorArray[y * srcBitmap.getWidth() + x];
	}

	/**
	 *获取对应坐标颜色值
	 * @param x
	 * @param y
	 * @param offset 补偿值
	 * @return
	 */
	public int getPixelColor(int x, int y, int offset) {
		return colorArray[y * srcBitmap.getWidth() + x + offset];
	}

	/**
	 * 获取对应坐标R
	 * @param x
	 * @param y
	 * @return
	 */
	public int getRComponent(int x, int y) {
		return Color.red(colorArray[y * srcBitmap.getWidth() + x]);
	}

	/**
	 * 获取对应坐标R
	 * @param x
	 * @param y
	 * @param offset 补偿值
	 * @return
	 */
	public int getRComponent(int x, int y, int offset) {
		return Color.red(colorArray[y * srcBitmap.getWidth() + x + offset]);
	}

	/**
	 * 获取对应坐标G
	 * @param x
	 * @param y
	 * @return
	 */
	public int getGComponent(int x, int y) {
		return Color.green(colorArray[y * srcBitmap.getWidth() + x]);
	}

	/**
	 * 获取对应坐标G
	 * @param x
	 * @param y
	 * @param offset 补偿
	 * @return
	 */
	public int getGComponent(int x, int y, int offset) {
		return Color.green(colorArray[y * srcBitmap.getWidth() + x + offset]);
	}

	/**
	 * 对应坐标B
	 * @param x
	 * @param y
	 * @return
	 */
	public int getBComponent(int x, int y) {
		return Color.blue(colorArray[y * srcBitmap.getWidth() + x]);
	}

	/**
	 * 对应坐标B
	 * @param x
	 * @param y
	 * @param offset
	 * @return
	 */
	public int getBComponent(int x, int y, int offset) {
		return Color.blue(colorArray[y * srcBitmap.getWidth() + x + offset]);
	}

	/**
	 * 设置对应坐标RGB
	 * @param x
	 * @param y
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setPixelColor(int x, int y, int r, int g, int b) {
		int rgbcolor = (255 << 24) + (r << 16) + (g << 8) + b;
		colorArray[((y * srcBitmap.getWidth() + x))] = rgbcolor;
	}

	/**
	 * 设置对应坐标RGB
	 * @param x
	 * @param y
	 * @param rgbcolor
	 */
	public void setPixelColor(int x, int y, int rgbcolor) {
		colorArray[((y * srcBitmap.getWidth() + x))] = rgbcolor;
	}

	/**
	 * 获取当前矩阵
	 * @return
	 */
	public int[] getColorArray() {
		return colorArray;
	}

	/**
	 * 获取改变结果的图片
	 * @return
	 */
	public Bitmap getDstBitmap() {
		dstBitmap.setPixels(colorArray, 0, width, 0, 0, width, height);
		return dstBitmap;
	}

	/**
	 * 设置颜色值安全范围
	 * @param a
	 * @return
	 */
	public int safeColor(int a) {
		if (a < 0)
			return 0;
		else if (a > 255)
			return 255;
		else
			return a;
	}

	/**
	 * 获取宽
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 获取高
	 * @return
	 */
	public int getHeight() {
		return height;
	}

}
