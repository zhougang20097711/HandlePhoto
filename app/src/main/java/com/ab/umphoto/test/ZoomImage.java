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
 * Created by AB051788 on 2017/1/5.
 */
public class ZoomImage extends Activity {
	private ImageView imageview;
	private TextView message;
	private RelativeLayout zoomImage01;
	private RelativeLayout zoomImage02;
	private RelativeLayout zoomImage03;
	private Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zoomimage);
		init();
	}


	private void init(){
		imageview = (ImageView) findViewById(R.id.imageview);
		message = (TextView) findViewById(R.id.message);
		zoomImage01 = (RelativeLayout) findViewById(R.id.zoomImage01);
		zoomImage02 = (RelativeLayout) findViewById(R.id.zoomImage02);
		zoomImage03 = (RelativeLayout) findViewById(R.id.zoomImage03);
		message.setText("");
		imageview.setImageResource(R.mipmap.prd_bx_13);
		bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.prd_bx_13);
		zoomImage01.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imageview.setImageBitmap(UMPhotoUtils.zoomImage(bitmap,bitmap.getWidth()-100,bitmap.getHeight()-50));
			}
		});
		zoomImage02.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imageview.setImageBitmap(UMPhotoUtils.zoomImage(bitmap,1.5f));
			}
		});
		zoomImage03.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imageview.setImageBitmap(UMPhotoUtils.zoomImage(bitmap,1.5f,1.8f));
			}
		});
	}
}
