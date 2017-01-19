package com.ab.umphotolib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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

import com.ab.umphotolib.filter.GaussianBlurFilter;
import com.ab.umphotolib.filter.ImagePixelsArrayHandle;
import com.ab.umphotolib.filter.ImageSketchFilter;
import com.ab.umphotolib.filter.ImageTypeFilter;
import com.ab.umphotolib.filter.SoftGlowFilter;
import com.ab.umphotolib.filter.ColorMatrixFilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * UMPhoto 图片处理类
 * Matrix: 3 x 3矩阵
 * API-- Translate(平移变换)  Scale(缩放变换)  Rotate(旋转变换)  Skew(错切变换)
 * 三种操作 set-post-pre
 *作用-图像的复合变化 缩放，移动，旋转，透视，扭曲，倒影
 *
 * ColorMatrix:5 x 4矩阵  RGBA
 * --API提供饱和度、色相、亮度修改，
 * --通过直接设置5 x 4矩阵实现各种滤镜效果
 * 作用-实现图像各种滤镜效果
 * Created by AB051788 on 2017/1/3.
 */
public class UMPhotoUtils {
	/**
	 * 保存图片到本地
	 * @param mBitmap  图片
	 * @param filepath 绝对路径
	 * @throws IOException 抛出异常
	 */
	public static void saveImage(Bitmap mBitmap, String filepath) throws IOException {
		saveImage(mBitmap, filepath, 100, Bitmap.CompressFormat.JPEG);
	}

	/**
	 * 保存图片-可设置图片质量和类型
	 * @param mBitmap   图片
	 * @param filePath  绝对路径
	 * @param quality   图片质量
	 * @param photoType 图片类型 Bitmap.CompressFormat.JPEG  Bitmap.CompressFormat.PNG  Bitmap.CompressFormat.WEBP
	 * @throws IOException  抛出异常
	 */
	public static void saveImage(Bitmap mBitmap, String filePath, int quality, Bitmap.CompressFormat photoType) throws IOException {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream out = new FileOutputStream(file);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		mBitmap.compress(photoType, quality, stream);
		byte[] bytes = stream.toByteArray();
		out.write(bytes);
		out.close();
	}

	/**
	 *获取本地图片
	 * @param filePath 图片路径
	 * @return 获取到的图片
	 * @throws FileNotFoundException 抛出异常
	 */
	public static Bitmap getBitmapByPath(String filePath) throws FileNotFoundException {
		return getBitmapByPath(filePath, null);
	}

	/**
	 *获取本地图片
	 * @param filePath  图片路径
	 * @param opts  信息
	 * @return  获取到的图片
	 * @throws FileNotFoundException 抛出异常
	 */
	public static Bitmap getBitmapByPath(String filePath, BitmapFactory.Options opts) throws FileNotFoundException {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis, null, opts);
		} catch (FileNotFoundException e) {
			throw e;
//			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			throw e;
//			e.printStackTrace();
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
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		return bitmapDrawable.getBitmap();
	}

	/**
	 * Bitmap 转 Drawable
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	/**
	 * byte[] 转 bitmap
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
	 * @param bm
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	/**
	 * 获取类型 - 文件
	 * @param file 目标文件
	 * @return 类型的字符串 "PNG"  "JPEG"  "GIF" "BMP"  null
	 */
	public static String getImageType(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(file);  //获取文件流
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
	 * 获取类型 - 流
	 * @param input 流
	 * @return 类型的字符串 "PNG"  "JPEG"  "GIF" "BMP"  null
	 */
	public static String getImageType(InputStream input) {
		return ImageTypeFilter.getImageType(input);
	}


	/**
	 * 图片等比缩放  按照宽或高等比缩放
	 * 目标高或宽为0，则以另一方为标准
	 * @param bitmap  图片
	 * @param w   缩放宽度 可为0
	 * @param h   缩放高度 可为0
	 * @return  缩放后的图片
	 */
	public static Bitmap zoomImage(Bitmap bitmap, int w, int h) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		float zoomScale = 1.0f;       //缩放比例
		if(w == 0 && h == 0){
			return bitmap;           //目标宽高都为0 则不缩放
		}else if(h == 0){
			zoomScale = scaleWidht;           //高为0 以宽为准
		}else if(w == 0){
			zoomScale = scaleHeight;       //宽为0 以高为准
		}else if(w / h > width / height){   //以缩放比例小的一方为准
			zoomScale = scaleHeight;      //以高为准
		}else{
			zoomScale = scaleWidht;         // 以宽为准
		}
		return zoomImage(bitmap, zoomScale, zoomScale);
	}

	/**
	 * 图片缩放  设置等比缩放比例
	 * @param bitmap  图片
	 * @param scale  缩放比例
	 * @return  缩放后图片
	 */
	public static Bitmap zoomImage(Bitmap bitmap, float scale) {
		return zoomImage(bitmap, scale, scale);
	}

	/**
	 * 图片缩放  分别设置宽高缩放比例
	 * @param bitmap  图片
	 * @param scaleWidht  宽缩放比例
	 * @param scaleHeight  高缩放比例
	 * @return  缩放后图片
	 */
	public static Bitmap zoomImage(Bitmap bitmap, float scaleWidht, float scaleHeight) {
		Bitmap newbmp = null;
		if (bitmap != null) {
			Matrix matrix = new Matrix();    //创建矩阵
			matrix.postScale(scaleWidht, scaleHeight); //设置宽高缩放比例
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		}
		return newbmp;
	}

	/**
	 * 压缩图片 以图片质量为代价 可用于图片上传
	 * @param bitmap  图片
	 * @param sizeKB  压缩到指定大小以下（KB）
	 * @return  返回压缩后的图片
	 */
	public static Bitmap compressImage(Bitmap bitmap,int sizeKB){
		byte[] data = compressImageToStream(bitmap,sizeKB).toByteArray();
		return bytesToBimap(data);
	}

	/**
	 *  压缩图片 以图片质量为代价 根据具体上传需要定制
	 * @param bitmap  图片
	 * @param sizeKB  压缩到指定大小以下 （KB）
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
			baos.reset();   // 重置baos即清空baos
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;   // 每次都减少10
		}
		return baos;
	}
	/**
	 * 图片旋转
	 * @param bitmap  图片
	 * @param degree  旋转角度
	 * @return 旋转后图片
	 */
	public static Bitmap rotateImageByDegree(Bitmap bitmap, int degree) {
		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);  //旋转
		Bitmap tempBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);
		return tempBmp;
	}

	/**
	 * 剪裁出最大正方形 具体剪裁功能放拍照相册模块
	 * @param bitmap  图片
	 * @return  裁剪后图片
	 */
	public static Bitmap cropRectImage(Bitmap bitmap) {
		int w = bitmap.getWidth(); // 得到图片的宽，高
		int h = bitmap.getHeight();
		int cropWidth = w >= h ? h : w;// 裁切后所取的正方形区域边长
		//Bitmap.createBitmap() 可用于对图片一定区域的裁剪  具体场合参数具体设置
		return Bitmap.createBitmap(bitmap, (w - cropWidth) / 2, (h - cropWidth) / 2, cropWidth, cropWidth, null, false);
	}

	/**
	 * 水印文字 图片下方水平居中  可根据具体场景定制方法
	 * @param bitmap  图片
	 * @param message 水印文字
	 * @return  有水印文字图片
	 */
	public static Bitmap waterImageBottomCenter(Bitmap bitmap, String message) {
		Paint paint = new Paint();
		paint.setColor(Color.LTGRAY);  //设置画笔颜色
		paint.setTextSize(30);       //设置字体大写
		paint.setTextAlign(Paint.Align.CENTER);  //设置居中
		if (bitmap == null) {
			return null;
		}
		int x = bitmap.getWidth() / 2;  //计算水印文字坐标
		int y = bitmap.getHeight() - 15;
		return waterImage(bitmap, message, x, y, paint);
	}

	/**
	 * 水印文字  方法
	 * @param bitmap  原图
	 * @param message 文字
	 * @param x       文字x坐标
	 * @param y       文字y坐标
	 * @param paint   画笔可设置字体大小，颜色，对齐方式等相关配置信息
	 *                   可参考waterImageBottomCenter的配置
	 * @return  水印文字图片
	 */
	public static Bitmap waterImage(Bitmap bitmap, String message, int x, int y, Paint paint) {
		if (bitmap == null) {
			return null;
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newb);   //以原图大小创建画布
		canvas.drawBitmap(bitmap, 0, 0, null);  // 在 0，0坐标开始画入src Bitmap
		canvas.drawText(message, x, y, paint);   //绘水印文字
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return newb;
	}

	/**
	 * 水印图片 贴图  涂鸦
	 * @param src       源图
	 * @param watermark 水印图
	 * @param left      水印图左边距离
	 * @param top       顶距离
	 * @return  水印图片
	 */
	public static Bitmap mergeImage(Bitmap src, Bitmap watermark, float left, float top) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);    // 在 0，0坐标开始画入src Bitmap
		cv.drawBitmap(watermark, left, top, null); // 在指定坐标画入水印图片
		cv.save(Canvas.ALL_SAVE_FLAG);  // 保存
		cv.restore();// 存储
		return newb;
	}
	/**
	 * 图片合成 用的四个方向
	 */
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int TOP = 2;
	public static final int BOTTOM = 3;
	/**
	 * 两个图片的合成
	 * @param first     第一张图
	 * @param second    第二张图
	 * @param direction 拼接方向 UMPhotoUtils.LEFT RIGHT TOP BOTTOM
	 * @return 合成后的图片
	 */
	public static Bitmap mixImage(Bitmap first, Bitmap second, int direction) {
		if (first == null) {
			return null;
		}
		if (second == null) {
			return first;
		}
		//获取各自宽高
		int fw = first.getWidth();
		int fh = first.getHeight();
		int sw = second.getWidth();
		int sh = second.getHeight();
		Bitmap newBitmap = null;
		//根据两图片的宽高和方向 创建一个足够放下两图片的画布，在相应的位置画入两图片
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
	 * 同方向多图片合成
	 * @param direction 拼接方向 UMPhotoUtils.LEFT RIGHT TOP BOTTOM
	 * @param bitmaps 图片
	 * @return 合成后图片
	 */
	public static Bitmap mixImage(int direction, Bitmap... bitmaps) {
		if (bitmaps.length <= 0) {
			return null;
		}
		if (bitmaps.length == 1) {
			return bitmaps[0];
		}
		Bitmap newBitmap = bitmaps[0];
		for (int i = 1; i < bitmaps.length; i++) {
			//循环两两合成图片
			newBitmap = mixImage(newBitmap, bitmaps[i], direction);
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
		Canvas canvas = new Canvas(output); //创建一个原图大写的画布
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		//设置目标图片区域
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		// PorterDuff.Mode.SRC_IN 只在源图像和目标图像相交的地方绘制目标图像
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 获得带倒影的图片
	 * @param bitmap 图片
	 * @return 倒影图片
	 */
	public static Bitmap reflectionImage(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		//创建矩阵
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
       //倒影位图
		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);
        //合成的位图
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Bitmap.Config.ARGB_8888);
		//创建合成位图大小的画布
		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);//画入原图
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		//LinearGradient线性渲染
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
		paint.setShader(shader); //设置渲染器
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
		return bitmapWithReflection;
	}

	/**
	 *修改图片 色相 亮度 饱和度
	 * @param mBitmap  原图
	 * @param hue  [0-255] 色相
	 * @param lum  [0-255] 亮度
	 * @param saturation  [0-255] 饱和度
	 * @return 效果图
	 */
	public static Bitmap toneBitmap(Bitmap mBitmap,int hue,int lum,int saturation) {
		ColorMatrixFilter layer = new ColorMatrixFilter(mBitmap);
		layer.setHue(hue);//色相
		layer.setLum(lum); //亮度
		layer.setSaturation(saturation);//饱和度
		return layer.getBitMap();
	}

	/**
	 * 修改饱和度
	 * @param mBitmap 原图
	 * @param saturation [0-255]
	 * @return 效果图
	 */
	public static Bitmap toneBitmapSaturation(Bitmap mBitmap,int saturation){
		ColorMatrixFilter layer = new ColorMatrixFilter(mBitmap);
		layer.setSaturation(saturation);
		return layer.getBitMap();
	}

	/**
	 * 修改色相
	 * @param mBitmap 原图
	 * @param hue  [0-255]
	 * @return 效果图
	 */
	public static Bitmap toneBitmapHue(Bitmap mBitmap,int hue){
		ColorMatrixFilter layer = new ColorMatrixFilter(mBitmap);
		layer.setHue(hue);
		return layer.getBitMap();
	}

	/**
	 * 修改亮度
	 * @param mBitmap 原图
	 * @param lum  [0-255]
	 * @return 效果图
	 */
	public static Bitmap toneBitmapLum(Bitmap mBitmap,int lum){
		ColorMatrixFilter layer = new ColorMatrixFilter(mBitmap);
		layer.setLum(lum);
		return layer.getBitMap();
	}

	/**
	 * 黑白图片 灰度图片 效果
	 * @param bitmap 传入的图片
	 * @return 效果图
	 */
	public static Bitmap grayImage(Bitmap bitmap) {
		//修改图片饱和度
		return toneBitmapSaturation(bitmap,0);
	}

	/**
	 * 怀旧效果
	 * @param bitmap 原图
	 * @return 效果图
	 */
	public static Bitmap oldImage(Bitmap bitmap) {
		//初始化像素矩阵集合
		ImagePixelsArrayHandle pixelsArrayHandle = new ImagePixelsArrayHandle(bitmap);
		int width = pixelsArrayHandle.getWidth();
		int height = pixelsArrayHandle.getHeight();
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		for (int i = 0; i < height; i++) {
			for (int k = 0; k < width; k++) {
				//获取RGB旧值
				pixR = pixelsArrayHandle.getRComponent(k,i);
				pixG = pixelsArrayHandle.getGComponent(k,i);
				pixB = pixelsArrayHandle.getBComponent(k,i);
				//计算RGB新值
				newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
				newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
				newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
				int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
				pixelsArrayHandle.setPixelColor(k,i,newColor);//改变每个像素点的颜色值
			}
		}
		return pixelsArrayHandle.getDstBitmap();
	}


	/**
	 * 图片锐化（拉普拉斯变换）
	 * @param bmp 原图
	 * @return 效果图
	 */
	public static Bitmap sharpenImage(Bitmap bmp) {
		// 拉普拉斯矩阵
		int[] laplacian = new int[]{-1, -1, -1, -1, 9, -1, -1, -1, -1};
		//初始化像素点矩阵
		ImagePixelsArrayHandle pixelsArrayHandle = new ImagePixelsArrayHandle(bmp);
		int width = pixelsArrayHandle.getWidth();
		int height = pixelsArrayHandle.getHeight();
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int idx = 0;
		float alpha = 0.3F;
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						//获取RGB旧值
						pixR = pixelsArrayHandle.getRComponent(k+m,i+n);
						pixG = pixelsArrayHandle.getGComponent(k+m,i+n);
						pixB = pixelsArrayHandle.getBComponent(k+m,i+n);
						//计算RGB新值
						newR = newR + (int) (pixR * laplacian[idx] * alpha);
						newG = newG + (int) (pixG * laplacian[idx] * alpha);
						newB = newB + (int) (pixB * laplacian[idx] * alpha);
						idx++;
					}
				}
				//改变每个像素点的值
				pixelsArrayHandle.setPixelColor(k,i,Color.argb(255, Math.min(255, Math.max(0, newR)), Math.min(255, Math.max(0, newG)), Math.min(255, Math.max(0, newB))));
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		return pixelsArrayHandle.getDstBitmap();
	}

	/**
	 * 高斯模糊 -柔化效果
	 * @param bmp 图片
	 * @param sigma [0, 40] 模糊整的
	 * @return 效果图
	 */
	public static Bitmap blurImage(Bitmap bmp,float sigma) {
		GaussianBlurFilter gaussianBlurFilter = new GaussianBlurFilter(bmp);
		gaussianBlurFilter.Sigma =sigma;
		return gaussianBlurFilter.imageProcess().getDstBitmap();
	}

	/**
	 * 美白效果
	 * @param bitmap 原图
	 * @return 效果图
	 */
	public static Bitmap skinWhiteImage(Bitmap bitmap) {
		return new SoftGlowFilter(bitmap,10,0.1f,0.1f).imageProcess().getDstBitmap();
	}

	/**
	 * 素描
	 * @param bmp 原图
	 * @return 返回处理后的素描
	 */
	public static Bitmap sketchImage(Bitmap bmp) {
		return ImageSketchFilter.sketchImage(bmp);
	}

	/**
	 * 通过改变颜色矩阵获取各种效果
	 * @param bitmap 原图
	 * @param array  矩阵
	 * 泛黄效果
	 * array1 = {1, 0, 0, 0, 100, 0, 1, 0, 0, 100, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};
	//灰度效果：
	float[] array2 = {0.33F, 0.59F, 0.11F, 0, 0, 0.33F, 0.59F, 0.11F, 0, 0, 0.33F, 0.59F, 0.11F, 0, 0, 0, 0, 0, 1, 0};
	//图像反转：
	float[] array3 = {-1, 0, 0, 1, 1, 0, -1, 0, 1, 1, 0, 0, -1, 1, 1, 0, 0, 0, 1, 0};
	//怀旧效果：
	float[] array4 = {0.393F, 0.769F, 0.189F, 0, 0, 0.349F, 0.686F, 0.168F, 0, 0, 0.272F, 0.534F, 0.131F, 0, 0, 0, 0, 0, 1, 0};
	//去色效果：
	float[] array5 = {1.5F, 1.5F, 1.5F, 0, -1, 1.5F, 1.5F, 1.5F, 0, -1, 1.5F, 1.5F, 1.5F, 0, -1, 0, 0, 0, 1, 0};
	//高饱和度：
	float[] array6 = {1.438F, -0.122F, -0.016F, 0, -0.03F, -0.062F, 1.378F, -0.016F, 0, 0.05F, -0.062F, -0.122F, 1.483F, 0, -0.02F, 0, 0, 0, 1, 0};
	 * @return
	 */
	public static Bitmap filterBitmapByColorMatrix(Bitmap bitmap,float[] array){
		ColorMatrixFilter filter = new ColorMatrixFilter(bitmap);
		return filter.changeBitmapByArray(array);
	}


	/**
	 *马赛克图片
	 * @param bitmap 原图
	 * @param targetRect 位置
	 * @param blockSize 区域
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
