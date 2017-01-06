package com.ab.umphoto;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AB051788 on 2017/1/4.
 */
public class FileUtils {
	/**
	 * @return
	 */
	public static final String getPhotoPath() {
		File file = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

	/**
	 * 设置文件的名称
	 *
	 * @return fileName
	 */
	public static String setFileName() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssMS");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}
}
