package com.ab.umphotolib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

import com.ab.umphotolib.model.ImageType;

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
	public static final int TOP = 3;
	public static final int BOTTOM = 4;
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
	 * 水印图片 贴图  涂鸦
	 * @param src 源图
	 * @param watermark 水印图
	 * @param left  水印图左边距离
	 * @param top  顶距离
	 * @return
	 */
	public static Bitmap mergeImage(Bitmap src, Bitmap watermark,float left, float top) {
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
	 *同方向多图片合成
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
	 *两个图片的合成
	 * @param first 第一张图
	 * @param second 第二张图
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

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, Shader.TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}





}
