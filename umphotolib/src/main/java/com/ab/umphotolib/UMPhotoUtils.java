package com.ab.umphotolib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.ab.umphotolib.model.ImageSketch;
import com.ab.umphotolib.model.ImageType;
import com.ab.umphotolib.model.Imageskin;
import com.ab.umphotolib.model.ToneLayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by AB051788 on 2017/1/3.
 */
public class UMPhotoUtils {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int TOP = 2;
	public static final int BOTTOM = 3;

	/**
	 * 保存图片
	 *
	 * @param mBitmap  图片
	 * @param filepath 路径
	 * @throws IOException
	 */
	public static void saveImage(Bitmap mBitmap, String filepath) throws IOException {
		saveImage(mBitmap, filepath, 100, Bitmap.CompressFormat.JPEG);
	}

	/**
	 * 保存图片
	 *
	 * @param mBitmap   图片
	 * @param filePath  绝对路径
	 * @param quality   图片质量
	 * @param photoType 图片类型 Bitmap.CompressFormat.JPEG  Bitmap.CompressFormat.PNG  Bitmap.CompressFormat.WEBP
	 * @throws IOException
	 */
	public static void saveImage(Bitmap mBitmap, String filePath, int quality, Bitmap.CompressFormat photoType) throws IOException {
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}
		FileOutputStream out = new FileOutputStream(f);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		mBitmap.compress(photoType, quality, stream);
		byte[] bytes = stream.toByteArray();
		out.write(bytes);
		out.close();
	}

	/**
	 * 获取bitmap
	 *
	 * @param filePath 图片路径
	 * @return
	 */
	public static Bitmap getBitmapByPath(String filePath) {
		return getBitmapByPath(filePath, null);
	}

	public static Bitmap getBitmapByPath(String filePath, BitmapFactory.Options opts) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis, null, opts);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	/**
	 * Drawable 转 Bitmap
	 *
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmapByBD(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		return bitmapDrawable.getBitmap();
	}

	/**
	 * Bitmap 转 Drawable
	 *
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawableByBD(Bitmap bitmap) {
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	/**
	 * byte[] 转 bitmap
	 *
	 * @param b
	 * @return
	 */
	public static Bitmap bytesToBimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * bitmap 转 byte[]
	 *
	 * @param bm
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}


	/**
	 * 获取图片类型
	 *
	 * @param file
	 * @return PNG JEPG GIF BMP
	 */
	public static String getImageType(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			String type = getImageType(in);
			return type;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * @param in 流
	 * @return PNG JEPG GIF BMP
	 */
	public static String getImageType(InputStream in) {
		return ImageType.getImageType(in);
	}


	/**
	 * 放大缩小图片
	 *
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bitmap, int w, int h) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
//		float scaleWidht = ((float) w / width); //待确定缩放规则
//		float scaleHeight = ((float) h / height);
		float zoomScale = 1.0f;
		if (w / h > width / height) {
			zoomScale = ((float) h) / height;//以高为准
		} else {
			zoomScale = ((float) w) / width;//以宽为准
		}
		return zoomImage(bitmap, zoomScale, zoomScale);
	}

	/**
	 * 图片缩放
	 *
	 * @param bitmap
	 * @param scale
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bitmap, float scale) {
		return zoomImage(bitmap, scale, scale);
	}

	/**
	 * 图片缩放
	 *
	 * @param bitmap
	 * @param scaleWidht
	 * @param scaleHeight
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bitmap, float scaleWidht, float scaleHeight) {
		Bitmap newbmp = null;
		if (bitmap != null) {
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidht, scaleHeight);
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		}
		return newbmp;
	}

	/**
	 * 压缩图片 以图片质量为代价 可用于图片上传
	 * @param bitmap
	 * @param sizeKB  压缩到指定大小以下
	 * @return  返回压缩后的图片
	 */
	public static Bitmap compressImage(Bitmap bitmap,int sizeKB){
		byte[] data = compressImageToStream(bitmap,sizeKB).toByteArray();
		return bytesToBimap(data);
	}

	/**
	 *  压缩图片 以图片质量为代价 根据具体上传需要定制
	 * @param bitmap
	 * @param sizeKB  压缩到指定大小以下
	 * @return  返回压缩后的数据流
	 */
	public static  ByteArrayOutputStream  compressImageToStream(Bitmap bitmap,int sizeKB){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > sizeKB) {
			if (options <= 0) {
				break;
			}
			baos.reset();// 重置baos即清空baos
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		return baos;
	}
	/**
	 * 图片旋转
	 *
	 * @param bitmap
	 * @param degree
	 * @return
	 */
	public static Bitmap rotateImageByDegree(Bitmap bitmap, int degree) {
		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		Bitmap tempBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);
		return tempBmp;
	}

	/**
	 * 剪裁出最大正方形 具体剪裁功能放拍照相册模块
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap cropRectImage(Bitmap bitmap) {
		int w = bitmap.getWidth(); // 得到图片的宽，高
		int h = bitmap.getHeight();
		int cropWidth = w >= h ? h : w;// 裁切后所取的正方形区域边长
		return Bitmap.createBitmap(bitmap, (w - cropWidth) / 2, (h - cropWidth) / 2, cropWidth, cropWidth, null, false);
	}

	/**
	 * 水印文字 方法
	 *
	 * @param bitmap  原图
	 * @param message 文字
	 * @param x       文字开始x坐标
	 * @param y       文字y坐标
	 * @param paint   画笔可设置字体大小，颜色，居中方式等
	 * @return
	 */
	public static Bitmap waterImage(Bitmap bitmap, String message, int x, int y, Paint paint) {
		if (bitmap == null) {
			return null;
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newb);
		canvas.drawBitmap(bitmap, 0, 0, null);// 在 0，0坐标开始画入src
		canvas.drawText(message, x, y, paint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return newb;
	}

	/**
	 * 水印文字 图片下方水平居中  可根据具体场景定制方法
	 *
	 * @param bitmap  图片
	 * @param message 文字
	 * @return
	 */
	public static Bitmap waterImageBottomCenter(Bitmap bitmap, String message) {
		Paint paint = new Paint();
		paint.setColor(Color.LTGRAY);
		paint.setTextSize(30);
		paint.setTextAlign(Paint.Align.CENTER);
		if (bitmap == null) {
			return null;
		}
		int x = bitmap.getWidth() / 2;
		int y = bitmap.getHeight() - 15;
		return waterImage(bitmap, message, x, y, paint);
	}

	/**
	 * 水印图片 贴图  涂鸦
	 *
	 * @param src       源图
	 * @param watermark 水印图
	 * @param left      水印图左边距离
	 * @param top       顶距离
	 * @return
	 */
	public static Bitmap mergeImage(Bitmap src, Bitmap watermark, float left, float top) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
//		int ww = watermark.getWidth();
//		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		cv.drawBitmap(watermark, left, top, null);// 在src的右下角画入水印
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
//		watermark.recycle();  //需不需要recycle
//		watermark = null;
		return newb;
	}

	/**
	 * 同方向多图片合成
	 *
	 * @param direction 拼接方向 UMPhotoUtils.LEFT RIGHT TOP BOTTOM
	 * @param bitmaps
	 * @return
	 */
	public static Bitmap mixImage(int direction, Bitmap... bitmaps) {
		if (bitmaps.length <= 0) {
			return null;
		}
		if (bitmaps.length == 1) {
			return bitmaps[0];
		}
		Bitmap newBitmap = bitmaps[0];
		// newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
		for (int i = 1; i < bitmaps.length; i++) {
			newBitmap = mixImage(newBitmap, bitmaps[i], direction);
		}
		return newBitmap;
	}

	/**
	 * 两个图片的合成
	 *
	 * @param first     第一张图
	 * @param second    第二张图
	 * @param direction 拼接方向 UMPhotoUtils.LEFT RIGHT TOP BOTTOM
	 * @return
	 */
	public static Bitmap mixImage(Bitmap first, Bitmap second, int direction) {
		if (first == null) {
			return null;
		}
		if (second == null) {
			return first;
		}
		int fw = first.getWidth();
		int fh = first.getHeight();
		int sw = second.getWidth();
		int sh = second.getHeight();
		Bitmap newBitmap = null;
		if (direction == LEFT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, sw, 0, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == RIGHT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, fw, 0, null);
		} else if (direction == TOP) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, sh, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == BOTTOM) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, 0, fh, null);
		}
		return newBitmap;
	}

	/**
	 * 把图片变成圆角
	 *
	 * @param bitmap 需要修改的图片
	 * @param pixels 圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap roundCornerImage(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}


	/**
	 * 黑白图片 灰度图片
	 *
	 * @param bitmap 传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap grayImage(Bitmap bitmap) {
		int width, height;
		height = bitmap.getHeight();
		width = bitmap.getWidth();
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bitmap, 0, 0, paint);
		return bmpGrayscale;
	}


	/**
	 * 获得带倒影的图片方法
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap reflectionImage(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
		return bitmapWithReflection;
	}

	/**
	 * 修改图片 色相 亮度 饱和度
	 * @param mBitmap
	 * @return
	 */
	public static Bitmap toneBitmap(Bitmap mBitmap,int hue,int lum,int saturation) {
		ToneLayer layer = new ToneLayer(mBitmap);
		layer.setHue(hue);//色相
		layer.setLum(lum); //亮度
		layer.setSaturation(saturation);//饱和度
		return layer.getBitMap();
	}

	/**
	 * 修改饱和度
	 * @param mBitmap
	 * @param saturation
	 * @return
	 */
	public static Bitmap toneBitmapSaturation(Bitmap mBitmap,int saturation){
		ToneLayer layer = new ToneLayer(mBitmap);
		layer.setSaturation(saturation);
		return layer.getBitMap();
	}

	/**
	 * 修改色相
	 * @param mBitmap
	 * @param hue
	 * @return
	 */
	public static Bitmap toneBitmapHue(Bitmap mBitmap,int hue){
		ToneLayer layer = new ToneLayer(mBitmap);
		layer.setHue(hue);
		return layer.getBitMap();
	}

	/**
	 * 修改亮度
	 * @param mBitmap
	 * @param lum
	 * @return
	 */
	public static Bitmap toneBitmapLum(Bitmap mBitmap,int lum){
		ToneLayer layer = new ToneLayer(mBitmap);
		layer.setLum(lum);
		return layer.getBitMap();
	}
	/**
	 * 怀旧效果
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap oldImage(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int pixColor = 0;
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 0; i < height; i++) {
			for (int k = 0; k < width; k++) {
				pixColor = pixels[width * i + k];
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
				newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
				newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
				int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
				pixels[width * i + k] = newColor;
			}
		}
		bm.setPixels(pixels, 0, width, 0, 0, width, height);
		return bm;
	}


	/**
	 * 图片锐化（拉普拉斯变换）
	 *
	 * @param bmp
	 * @return
	 */
	public static Bitmap sharpenImage(Bitmap bmp) {
		// 拉普拉斯矩阵
		int[] laplacian = new int[]{-1, -1, -1, -1, 9, -1, -1, -1, -1};

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int idx = 0;
		float alpha = 0.3F;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + n) * width + k + m];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + (int) (pixR * laplacian[idx] * alpha);
						newG = newG + (int) (pixG * laplacian[idx] * alpha);
						newB = newB + (int) (pixB * laplacian[idx] * alpha);
						idx++;
					}
				}

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		return bitmap;
	}


	/**
	 * 高斯模糊 -柔化效果
	 *
	 * @param bmp
	 * @return
	 */
	public static Bitmap blurImage(Bitmap bmp) {
		// 高斯矩阵
		int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int delta = 16; // 值越小图片会越亮，越大则越暗

		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + (int) (pixR * gauss[idx]);
						newG = newG + (int) (pixG * gauss[idx]);
						newB = newB + (int) (pixB * gauss[idx]);
						idx++;
					}
				}

				newR /= delta;
				newG /= delta;
				newB /= delta;

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 美颜 没成功
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap skinImage(Bitmap bitmap) {
		Imageskin skin = new Imageskin(bitmap);
		return skin.imageProcess().bitmap;
	}

	/**
	 * 素描
	 * @param bmp 原图
	 * @return 返回处理后的素描
	 */
	public static Bitmap sketchImage(Bitmap bmp) {
		return ImageSketch.sketchImage(bmp);
	}


	/**
	 *马赛克图片
	 * @param bitmap
	 * @param targetRect
	 * @param blockSize
	 * @return
	 * @throws Exception
	 */
	public static Bitmap mosaicImage(Bitmap bitmap, Rect targetRect,
									int blockSize) throws OutOfMemoryError {
		if (bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0
				|| bitmap.isRecycled()) {
			throw new RuntimeException("bad bitmap to add mosaic");
		}
		if (blockSize < 4) {
			blockSize = 4;//最小区域
		}
		if (targetRect == null) {
			targetRect = new Rect();
		}
		int bw = bitmap.getWidth();
		int bh = bitmap.getHeight();
		if (targetRect.isEmpty()) {
			targetRect.set(0, 0, bw, bh);
		}
		//
		int rectW = targetRect.width();
		int rectH = targetRect.height();
		int[] bitmapPxs = new int[bw * bh];
		// fetch bitmap pxs
		bitmap.getPixels(bitmapPxs, 0, bw, 0, 0, bw, bh);
		//
		int rowCount = (int) Math.ceil((float) rectH / blockSize);
		int columnCount = (int) Math.ceil((float) rectW / blockSize);
		int maxX = bw;
		int maxY = bh;
		for (int r = 0; r < rowCount; r++) { // row loop
			for (int c = 0; c < columnCount; c++) {// column loop
				int startX = targetRect.left + c * blockSize + 1;
				int startY = targetRect.top + r * blockSize + 1;
				dimBlock(bitmapPxs, startX, startY, blockSize, maxX, maxY);
			}
		}
		return Bitmap.createBitmap(bitmapPxs, bw, bh, Bitmap.Config.ARGB_8888);
	}

	/**
	 * 从块内取样，并放大，从而达到马赛克的模糊效果
	 *
	 * @param pxs
	 * @param startX
	 * @param startY
	 * @param blockSize
	 * @param maxX
	 * @param maxY
	 */
	private static void dimBlock(int[] pxs, int startX, int startY,
								 int blockSize, int maxX, int maxY) {
		int stopX = startX + blockSize - 1;
		int stopY = startY + blockSize - 1;
		if (stopX > maxX) {
			stopX = maxX;
		}
		if (stopY > maxY) {
			stopY = maxY;
		}
		//
		int sampleColorX = startX + blockSize / 2;
		int sampleColorY = startY + blockSize / 2;
		//
		if (sampleColorX > maxX) {
			sampleColorX = maxX;
		}
		if (sampleColorY > maxY) {
			sampleColorY = maxY;
		}
		int colorLinePosition = (sampleColorY - 1) * maxX;
		int sampleColor = pxs[colorLinePosition + sampleColorX - 1];// 像素从1开始，但是数组层0开始
		for (int y = startY; y <= stopY; y++) {
			int p = (y - 1) * maxX;
			for (int x = startX; x <= stopX; x++) {
				// 像素从1开始，但是数组层0开始
				pxs[p + x - 1] = sampleColor;
			}
		}
	}

}
