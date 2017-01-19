package com.ab.umphotolib.filter;

import android.graphics.Bitmap;

/**
 * 美白效果器
 * Created by AB051788 on 2017/1/12.
 */
public class SoftGlowFilter {
	BrightContrastFilter contrastFx;

	GaussianBlurFilter gaussianBlurFx;
	private ImagePixelsArrayHandle image = null;


	public SoftGlowFilter(Bitmap bmp) {
		image = new ImagePixelsArrayHandle(bmp);
	}

	/**
	 *对位图进行模糊 对比度 亮度的调整
	 * @param bmp
	 * @param nSigma  [0, 40] 模糊值
	 * @param nBrightness  [0,1] 亮度
	 * @param nContrast  [-1,1]对比度
	 */
	public SoftGlowFilter(Bitmap bmp, int nSigma, float nBrightness, float nContrast) {
		gaussianBlurFx = new GaussianBlurFilter(bmp); //高斯模糊过滤器
		gaussianBlurFx.Sigma = nSigma;
		image = gaussianBlurFx.imageProcess();
		contrastFx = new BrightContrastFilter(image);  //亮度对比度过滤器
		contrastFx.BrightnessFactor = nBrightness;
		contrastFx.ContrastFactor = nContrast;

		image = contrastFx.imageProcess();
	}

	/**
	 *改变对应像素点值
	 * @return
	 */
	public ImagePixelsArrayHandle imageProcess() {
		int width = image.getWidth();
		int height = image.getHeight();
		ImagePixelsArrayHandle clone = image.clone();
		int old_r, old_g, old_b, r, g, b;
		for (int x = 0; x < (width - 1); x++) {
			for (int y = 0; y < (height - 1); y++) {
				//获取旧的RGB值
				old_r = clone.getRComponent(x, y);
				old_g = clone.getGComponent(x, y);
				old_b = clone.getBComponent(x, y);
				//计算新的RGB值
				r = 255 - (255 - old_r) * (255 - image.getRComponent(x, y)) / 255;
				g = 255 - (255 - old_g) * (255 - image.getGComponent(x, y)) / 255;
				b = 255 - (255 - old_b) * (255 - image.getBComponent(x, y)) / 255;
				image.setPixelColor(x, y, r, g, b);//设置像素点新值
			}
		}
		return image;
	}

}
