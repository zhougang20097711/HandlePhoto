package com.ab.umphoto.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.umphoto.R;
import com.ab.umphotolib.UMPhotoUtils;

/**
 * Created by AB051788 on 2017/1/6.
 */
public class GrayImage extends Activity {

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
		imageview.setImageResource(R.mipmap.prd_bx_13);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("grayImage()");
		final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.prd_bx_13);
		saveimage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imageview.setImageBitmap(UMPhotoUtils.grayImage(bitmap));
			}
		});
	}
}
