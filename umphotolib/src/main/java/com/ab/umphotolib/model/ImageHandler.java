package com.ab.umphotolib.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.nio.IntBuffer;

/**
 * Created by AB051788 on 2017/1/10.
 */
public class ImageHandler {

	//original bitmap image
	public Bitmap bitmap;
	public Bitmap result;

	//format of image (jpg/png)
	private String formatName;
	//dimensions of image
	private int width, height;
	// RGB Array Color
	protected int[] colorArray;

	public ImageHandler(Bitmap bitmap) {
		this.bitmap = bitmap;
		formatName = "jpg";
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		updateColorArray();
	}

	/**
	 * Method to reset the image to a solid color
	 *
	 * @param color - color to rest the entire image to
	 */
	public void clearImage(int color) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				setPixelColor(x, y, color);
			}
		}
	}

	public ImageHandler clone(){
		return new ImageHandler(this.bitmap);
	}
	/**
	 * Set color array for image - called on initialisation
	 */
	private void updateColorArray() {
		colorArray = new int[width * height];
		bitmap.getPixels(colorArray, 0, width, 0, 0, width, height);
		int r, g, b;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int index = y * width + x;
				r = (colorArray[index] >> 16) & 0xff;
				g = (colorArray[index] >> 8) & 0xff;
				b = colorArray[index] & 0xff;
				colorArray[index] = 0xff000000 | (b << 16) | (g << 8) | r;//androidϵͳ��windowϵͳ��rgb�洢��ʽ�෴
			}
		}
	}


	/**
	 * Method to set the color of a specific pixel
	 *
	 * @param x
	 * @param y
	 * @param color
	 */
	public void setPixelColor(int x, int y, int color) {
		colorArray[((y * bitmap.getWidth() + x))] = color;
		//image.setPixel(x, y, color);
		//destImage.setPixel(x, y, colorArray[((y*image.getWidth()+x))]);
	}

	/**
	 * Get the color for a specified pixel
	 *
	 * @param x
	 * @param y
	 * @return color
	 */
	public int getPixelColor(int x, int y) {
		return colorArray[y * width + x];
	}

	/**
	 * Set the color of a specified pixel from an RGB combo
	 *
	 * @param x
	 * @param y
	 * @param c0
	 * @param c1
	 * @param c2
	 */
	public void setPixelColor(int x, int y, int c0, int c1, int c2) {
		int rgbcolor = (255 << 24) + (c0 << 16) + (c1 << 8) + c2;
		colorArray[((y * bitmap.getWidth() + x))] = rgbcolor;
		//int array = ((y*image.getWidth()+x));

		//vbb.order(ByteOrder.nativeOrder());
		//vertexBuffer = vbb.asFloatBuffer();
		//vertexBuffer.put(vertices);
		//vertexBuffer.position(0);

		//image.setPixel(x, y, colorArray[((y*image.getWidth()+x))]);
	}

	public void copyPixelsFromBuffer() {
		IntBuffer vbb = IntBuffer.wrap(colorArray);
		//vbb.put(colorArray);
		result.copyPixelsFromBuffer(vbb);
		vbb.clear();
		//vbb = null;
	}

	/**
	 * Method to get the RED color for the specified
	 * pixel
	 *
	 * @param x
	 * @param y
	 * @return color of R
	 */
	public int getRComponent(int x, int y) {
		return (getColorArray()[((y * width + x))] & 0x00FF0000) >>> 16;
	}


	/**
	 * Method to get the GREEN color for the specified
	 * pixel
	 *
	 * @param x
	 * @param y
	 * @return color of G
	 */
	public int getGComponent(int x, int y) {
		return (getColorArray()[((y * width + x))] & 0x0000FF00) >>> 8;
	}


	/**
	 * Method to get the BLUE color for the specified
	 * pixel
	 *
	 * @param x
	 * @param y
	 * @return color of B
	 */
	public int getBComponent(int x, int y) {
		return (getColorArray()[((y * width + x))] & 0x000000FF);
	}


	/**
	 * Method to rotate an image by the specified number of degrees
	 *
	 * @param rotateDegrees
	 */
	public void rotate(int rotateDegrees) {
		Matrix mtx = new Matrix();
		mtx.postRotate(rotateDegrees);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, mtx, true);
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		updateColorArray();
	}


	/**
	 * @return the image
	 */
	public Bitmap getImage() {
		//return image;
		return result;
	}


	/**
	 * @param image the image to set
	 */
	public void setImage(Bitmap image) {
		this.bitmap = image;
	}


	/**
	 * @return the formatName
	 */
	public String getFormatName() {
		return formatName;
	}


	/**
	 * @param formatName the formatName to set
	 */
	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}


	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}


	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}


	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}


	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}


	/**
	 * @return the colorArray
	 */
	public int[] getColorArray() {
		return colorArray;
	}


	/**
	 * @param colorArray the colorArray to set
	 */
	public void setColorArray(int[] colorArray) {
		this.colorArray = colorArray;
	}


	public static int SAFECOLOR(int a) {
		if (a < 0) return 0;
		else if (a > 255) return 255;
		else return a;
	}


}
