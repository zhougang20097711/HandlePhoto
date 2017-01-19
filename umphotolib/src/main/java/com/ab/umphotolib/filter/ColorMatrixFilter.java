package com.ab.umphotolib.filter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * 颜色矩阵：
 * *ColorMatrix:5 x 4矩阵  RGBA
 * --API提供饱和度、色相、亮度修改，
 * --通过直接设置5 x 4矩阵实现各种滤镜效果
 * 作用-实现图像各种滤镜效果
 * Created by AB051788 on 2017/1/9.
 */
public class ColorMatrixFilter {
	private static final int MIDDLE_VALUE = 127; //中间值
	private Bitmap bitmap;
	private ColorMatrix mAllMatrix;
	private ColorMatrix mLightnessMatrix;
	private ColorMatrix mSaturationMatrix;
	private ColorMatrix mHueMatrix;

	private float hue = -1.0f;
	private float lum = -1.0f;
	private float saturation = -1.0f;

	public ColorMatrixFilter(Bitmap bitmap) {
		this.bitmap = bitmap;
		this.mAllMatrix = new ColorMatrix();
	}

	/**
	 * 通过矩阵  实现各种各样的滤镜效果
	 * @param array  颜色矩阵
	 * @return  滤镜图片
	 */
	public Bitmap changeBitmapByArray(float[] array){
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.set(array);    //设置颜色矩阵
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(bitmap, 0, 0, paint);
		return output;
	}
	/**
	 * 设置色相值  [0-255]
	 */
	public void setHue(int hue) {
		this.hue  = (hue-MIDDLE_VALUE) * 1.0F / MIDDLE_VALUE*180;
	}

	/**
	 * 设置饱和度值 [0-255]
	 */
	public void setSaturation(int saturation) {
		this.saturation = saturation * 1.0F / MIDDLE_VALUE;
	}

	/**
	 * 设置亮度值 [0-255]
	 */
	public void setLum(int lum) {
		this.lum = lum  * 1.0F / MIDDLE_VALUE;
	}

	/**
	 * 改变色相饱和度亮度后的效果图   API提供的方法
	 * @return 效果图
	 */
	public Bitmap getBitMap() {
		Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		// 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
		Canvas canvas = new Canvas(bmp); // 得到画笔对象
		Paint paint = new Paint(); // 新建paint
		paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理
		mAllMatrix.reset();


		//色相调节
		ColorMatrix hueMatrix = new ColorMatrix();
		if(hue>=0){
			hueMatrix.setRotate(0, hue);
			hueMatrix.setRotate(1, hue);
			hueMatrix.setRotate(2, hue);
		}


		//饱和度调节
		ColorMatrix saturationColorMatrix = new ColorMatrix();
		if(saturation>=0){
			saturationColorMatrix.setSaturation(saturation);
		}

		//亮度调节
		ColorMatrix lumMatrix = new ColorMatrix();
		if(lum>=0){
			lumMatrix.setScale(lum, lum, lum, 1);
		}
		mAllMatrix.reset();
		mAllMatrix.postConcat(hueMatrix);
		mAllMatrix.postConcat(saturationColorMatrix);
		mAllMatrix.postConcat(lumMatrix);
		paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));// 设置颜色变换效果
		canvas.drawBitmap(bitmap, 0, 0, paint); // 将颜色变化后的图片输出到新创建的位图区
		// 返回新的位图，也即调色处理后的图片
		return bmp;
	}


}
