package com.ab.umphoto.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.umphoto.FileUtils;
import com.ab.umphoto.MyApplication;
import com.ab.umphoto.R;
import com.ab.umphotolib.UMPhotoUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by AB051788 on 2017/1/5.
 */
public class SaveImage extends Activity {
	private ImageView imageview;
	private TextView message;
	private RelativeLayout saveimage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saveimage);
		init();
	}


	private void init(){
		imageview = (ImageView) findViewById(R.id.imageview);
		message = (TextView) findViewById(R.id.message);
		saveimage = (RelativeLayout) findViewById(R.id.saveimage);
		message.setText("将本图片保存在本地");
		imageview.setImageResource(R.mipmap.prd_bx_13);

		saveimage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.prd_bx_13);
				String filename = FileUtils.setFileName();
				File file = new File(FileUtils.getPhotoPath(), filename + ".jpg");
				try {
					UMPhotoUtils.saveImage(bitmap, file.getAbsolutePath(), 100, Bitmap.CompressFormat.JPEG);
				} catch (IOException e) {
					e.printStackTrace();
				}
				MyApplication application = (MyApplication) getApplication();
				application.setSavePhotopath(file.getAbsolutePath());
				message.setText(file.getAbsolutePath());
			}
		});
	}
}
