package com.ab.umphotolib.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by AB051788 on 2017/1/9.
 */
public class ToneLayer {
	private static final int MIDDLE_VALUE = 127;
	private Bitmap bitmap;
	private ColorMatrix mAllMatrix;
	private ColorMatrix mLightnessMatrix;
	private ColorMatrix mSaturationMatrix;
	private ColorMatrix mHueMatrix;

	public ToneLayer(Bitmap bitmap) {
		this.bitmap = bitmap;
		this.mAllMatrix = new ColorMatrix();
	}

	/**
	 * 设置色相值  0-255
	 */
	public void setHue(int hue) {
		float mHueValue = hue * 1.0F / MIDDLE_VALUE;
		if (mHueMatrix == null) {
			mHueMatrix = new ColorMatrix();
		}
		mHueMatrix.reset();
		mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1); // 红、绿、蓝三分量按相同的比例,最后一个参数1表示透明度不做变化，
	}

	/**
	 * 设置饱和度值 0-255
	 */
	public void setSaturation(int saturation) {
		float mSaturationValue = saturation * 1.0F / MIDDLE_VALUE;
		if (mSaturationMatrix == null) {
			mSaturationMatrix = new ColorMatrix();
		}
		mSaturationMatrix.reset();
		mSaturationMatrix.setSaturation(mSaturationValue);
	}

	/**
	 * 设置亮度值 0-255
	 */
	public void setLum(int lum) {
		float mLumValue = (lum - MIDDLE_VALUE) * 1.0F / MIDDLE_VALUE * 180;
		if (mLightnessMatrix == null) {
			mLightnessMatrix = new ColorMatrix();
		}
		mLightnessMatrix.reset();
		mLightnessMatrix.setRotate(0, mLumValue); // 控制让红色区在色轮上旋转的角度
		mLightnessMatrix.setRotate(1, mLumValue); // 控制让绿红色区在色轮上旋转的角度
		mLightnessMatrix.setRotate(2, mLumValue); // 控制让蓝色区在色轮上旋转的角度
	}

	public Bitmap getBitMap() {
		Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		// 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
		Canvas canvas = new Canvas(bmp); // 得到画笔对象
		Paint paint = new Paint(); // 新建paint
		paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理
		mAllMatrix.reset();
		if (mHueMatrix != null) {
			mAllMatrix.postConcat(mHueMatrix);
		}
		if (mSaturationMatrix != null) {
			mAllMatrix.postConcat(mSaturationMatrix);
		}
		if (mLightnessMatrix != null) {
			mAllMatrix.postConcat(mLightnessMatrix);
		}
		paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));// 设置颜色变换效果
		canvas.drawBitmap(bitmap, 0, 0, paint); // 将颜色变化后的图片输出到新创建的位图区
		// 返回新的位图，也即调色处理后的图片
		return bmp;
	}
}
