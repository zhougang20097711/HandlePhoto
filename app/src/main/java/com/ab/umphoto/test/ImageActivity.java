package com.ab.umphoto.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.umphoto.R;
import com.ab.umphotolib.UMPhotoUtils;

/**
 * Created by AB051788 on 2017/1/9.
 */
public class ImageActivity extends Activity {
	private ImageView imageview01;
	private ImageView imageview02;
	private TextView title;
	private RelativeLayout saveimage;
	private Bitmap bitmap;
	private int position;

	private Bitmap result;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 10000:
					if(result !=null){
						imageview02.setImageBitmap(result);
						imageview02.invalidate();
					}
					break;
				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		init();
	}


	private void init() {
		imageview01 = (ImageView) findViewById(R.id.imageview01);
		imageview02 = (ImageView) findViewById(R.id.imageview02);
		title = (TextView) findViewById(R.id.title);
		saveimage = (RelativeLayout) findViewById(R.id.saveimage);
		position = getIntent().getIntExtra("position", 1000);
		if(position == 1007){
			imageview01.setImageResource(R.mipmap.prd_bx_15);
		}else{
			imageview01.setImageResource(R.mipmap.prd_bx_13);
		}
		switch (position){
			case 1000:
				title.setText("cropImage()");
				break;
			case 1001:
				title.setText("rotateImage(45)");
				break;
			case 1002:
				title.setText("waterImage()");
				break;
			case 1003:
				title.setText("toneImage()");
				break;
			case 1004:
				title.setText("oldImage()");
				break;
			case 1005:
				title.setText("sharpenImage()");
				break;
			case 1006:
				title.setText("blurImage()");
				break;
			case 1007:
				title.setText("skinImage()");
				break;
			case 1008:
				title.setText("sketchImage()");
				break;
			case 1009:
				title.setText("compressImage()");
				break;
			case 1010:
				title.setText("mosaicImage()");
				break;
			default:
				break;
		}
if(position == 1007){
	bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.prd_bx_15);
}else{
	bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.prd_bx_13);
}

		saveimage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						switch (position){
							case 1000:
								result = UMPhotoUtils.cropRectImage(bitmap);
								break;
							case 1001:
								result = UMPhotoUtils.rotateImageByDegree(bitmap,45);
								break;
							case 1002:
								result = UMPhotoUtils.waterImageBottomCenter(bitmap,"Hello,World");
								break;
							case 1003:
								result = UMPhotoUtils.toneBitmapHue(bitmap,200);
								break;
							case 1004:
								result = UMPhotoUtils.oldImage(bitmap);
								break;
							case 1005:
								result = UMPhotoUtils.sharpenImage(bitmap);
								break;
							case 1006:
								result = UMPhotoUtils.blurImage(bitmap,5);
								break;
							case 1007:
								result = UMPhotoUtils.skinWhiteImage(bitmap);
								break;
							case 1008:
								result = UMPhotoUtils.sketchImage(bitmap);
								break;
							case 1009:
								result = UMPhotoUtils.compressImage(bitmap,2);
								break;
							case 1010:
								result = UMPhotoUtils.mosaicImage(bitmap,new Rect(300,100,400,200),10);
								break;
							default:
								break;
						}
						mHandler.sendEmptyMessage(10000);
					}
				}).start();

			}
		});
	}
}
