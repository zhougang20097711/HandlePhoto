package com.ab.umphoto;

import android.app.Application;

/**
 * Created by AB051788 on 2017/1/4.
 */
public class MyApplication extends Application {
	private String savePhotopath;
	@Override
	public void onCreate() {
		super.onCreate();
	}

	public String getSavePhotopath() {
		return savePhotopath;
	}

	public void setSavePhotopath(String savePhotopath) {
		this.savePhotopath = savePhotopath;
	}
}
