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
public class MixImage extends Activity {
	private ImageView imageview;
	private TextView message;
	private RelativeLayout zoomImage01;
	private RelativeLayout zoomImage02;
	private RelativeLayout zoomImage03;
	private RelativeLayout zoomImage04;
	private RelativeLayout zoomImage05;
	private Bitmap bitmap;
	private Bitmap wbitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_miximage);
		init();
	}


	private void init() {
		imageview = (ImageView) findViewById(R.id.imageview);
		message = (TextView) findViewById(R.id.message);
		zoomImage01 = (RelativeLayout) findViewById(R.id.zoomImage01);
		zoomImage02 = (RelativeLayout) findViewById(R.id.zoomImage02);
		zoomImage03 = (RelativeLayout) findViewById(R.id.zoomImage03);
		zoomImage04 = (RelativeLayout) findViewById(R.id.zoomImage04);
		zoomImage05 = (RelativeLayout) findViewById(R.id.zoomImage05);
		message.setText("");
		imageview.setImageResource(R.mipmap.prd_bx_13);
		bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.prd_bx_13);
		wbitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_01);
		zoomImage01.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				message.setText("上");
				imageview.setImageBitmap(UMPhotoUtils.mixImage(bitmap, wbitmap, UMPhotoUtils.TOP));
			}
		});
		zoomImage02.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				message.setText("下");
				imageview.setImageBitmap(UMPhotoUtils.mixImage(bitmap, wbitmap, UMPhotoUtils.BOTTOM));
			}
		});
		zoomImage03.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				message.setText("左");
				imageview.setImageBitmap(UMPhotoUtils.mixImage(bitmap, wbitmap, UMPhotoUtils.LEFT));
			}
		});
		zoomImage04.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				message.setText("右");
				imageview.setImageBitmap(UMPhotoUtils.mixImage(bitmap, wbitmap, UMPhotoUtils.RIGHT));
			}
		});
		zoomImage05.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				message.setText("多张（同向）");
				imageview.setImageBitmap(UMPhotoUtils.mixImage( UMPhotoUtils.RIGHT,bitmap, wbitmap,wbitmap,wbitmap));
			}
		});
	}
}
