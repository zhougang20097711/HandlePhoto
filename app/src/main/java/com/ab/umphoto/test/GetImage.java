package com.ab.umphoto.test;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.umphoto.MyApplication;
import com.ab.umphoto.R;
import com.ab.umphotolib.UMPhotoUtils;

import java.io.FileNotFoundException;

/**
 * Created by AB051788 on 2017/1/5.
 */
public class GetImage extends Activity {
	private ImageView imageview;
	private TextView message;
	private RelativeLayout saveimage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saveimage);
		init();
	}


	private void init() {
		imageview = (ImageView) findViewById(R.id.imageview);
		message = (TextView) findViewById(R.id.message);
		saveimage = (RelativeLayout) findViewById(R.id.saveimage);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText("getImage()");
		MyApplication application = (MyApplication) getApplication();
		final String path = application.getSavePhotopath();
		if (TextUtils.isEmpty(path)) {
			message.setText("先saveImage(),才获取图片");
		} else {
			message.setText("获取图片-" + path);
		}
		saveimage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(path)) {
					return;
				}
				try {
					imageview.setImageBitmap(UMPhotoUtils.getBitmapByPath(path));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
