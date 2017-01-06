package com.ab.umphoto;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 照相机帮助类
 *
 */
public class CrameUtils {

	public File saveFile;
	/**
	 * 调用相机
	 */
	public final static int CAMERA = 0x0002;
	/**
	 * 切图
	 */
	public final static int CROP = 0x0003;
	/**
	 * 调用相册
	 */
	public final static int ALBUM = 0x0004;

	/**
	 * 照相机拍照
	 */
	public void camera(Activity activity) {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			String filePth = FileUtils.setFileName();
			File file = new File(FileUtils.getPhotoPath(), filePth + ".jpg");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			this.saveFile = file;
			startForResultActivity(activity, intent, CrameUtils.CAMERA);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 相册
	 */
	public void album(Activity activity) {
		// 调用android的图库
		Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startForResultActivity(activity, i, CrameUtils.ALBUM);
	}

	public String fromAlbumGetFilePath(Activity context, Uri uri) {
		// ContentResolver resolver = context.getContentResolver();
		Uri originalUri = uri; // 获得图片的uri
		// bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); //
		// 显得到bitmap图片
		String[] proj = { MediaStore.Images.Media.DATA };
		// 好像是android多媒体数据库的封装接口，具体的看Android文档
		Cursor cursor = context.managedQuery(originalUri, proj, null, null, null);
		// 按我个人理解 这个是获得用户选择的图片的索引值
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		// 将光标移至开头 ，这个很重要，不小心很容易引起越界
		cursor.moveToFirst();
		// 最后根据索引值获取图片路径
		String path = cursor.getString(column_index);
		return path;
	}

	/**
	 * 截取图片
	 * @param activity
	 * @param uri
	 * @param outputX
	 *            输出大小
	 * @param outputY
	 *            输出大小
	 * @param requestCode
	 */
	public void cropImage(Activity activity, Uri uri, int outputX, int outputY, int scaleX, int scaleY, int requestCode) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", scaleX);
		intent.putExtra("aspectY", scaleY);
		// 裁剪后输出图片的尺寸大小 像素
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		// 图片格式
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		// intent.putExtra("noFaceDetection", false);
		intent.putExtra("return-data", false);
		// intent.putExtra("scale", true);
		String filePth = FileUtils.setFileName();
		File file = new File(FileUtils.getPhotoPath(), filePth + ".jpg");
		file.delete();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		this.saveFile = file;
		startForResultActivity(activity, intent, requestCode);
	}

	/**
	 * 返回activity
	 */
	private void startForResultActivity(Activity activity, Intent intent, int reqest) {
		activity.startActivityForResult(intent, reqest);
	}

	/** 保存方法 path路径存在 */
	public static void saveBitmap(String path, Bitmap mBitmap) {
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 98, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 读取图片的旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	public static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 * 
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}



}
