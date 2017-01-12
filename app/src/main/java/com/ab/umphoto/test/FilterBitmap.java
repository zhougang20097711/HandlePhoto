package com.ab.umphoto.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ab.umphoto.R;
import com.ab.umphotolib.UMPhotoUtils;

/**
 * Created by AB051788 on 2017/1/12.
 */
public class FilterBitmap extends Activity implements View.OnClickListener {
	private ImageView imageview01;
	private ImageView imageview02;
	private RelativeLayout zoomImage01;
	private RelativeLayout zoomImage02;
	private RelativeLayout zoomImage03;
	private RelativeLayout zoomImage04;
	private RelativeLayout zoomImage05;
	private RelativeLayout zoomImage06;
	private Bitmap bitmap;
	private Bitmap result;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 10000:
					if (result != null) {
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
		setContentView(R.layout.activity_filter);
		init();
	}


	private void init() {
		imageview01 = (ImageView) findViewById(R.id.imageview01);
		imageview02 = (ImageView) findViewById(R.id.imageview02);
		zoomImage01 = (RelativeLayout) findViewById(R.id.zoomImage01);
		zoomImage02 = (RelativeLayout) findViewById(R.id.zoomImage02);
		zoomImage03 = (RelativeLayout) findViewById(R.id.zoomImage03);
		zoomImage04 = (RelativeLayout) findViewById(R.id.zoomImage04);
		zoomImage05 = (RelativeLayout) findViewById(R.id.zoomImage05);
		zoomImage06 = (RelativeLayout) findViewById(R.id.zoomImage06);
		imageview01.setImageResource(R.mipmap.prd_bx_13);
		bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.prd_bx_13);
		zoomImage01.setOnClickListener(this);
		zoomImage02.setOnClickListener(this);
		zoomImage03.setOnClickListener(this);
		zoomImage04.setOnClickListener(this);
		zoomImage05.setOnClickListener(this);
		zoomImage06.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.zoomImage01:
				getBitMap(1);
				break;
			case R.id.zoomImage02:
				getBitMap(2);
				break;
			case R.id.zoomImage03:
				getBitMap(3);
				break;
			case R.id.zoomImage04:
				getBitMap(4);
				break;
			case R.id.zoomImage05:
				getBitMap(5);
				break;
			case R.id.zoomImage06:
				getBitMap(6);
				break;
		}
	}

	public void getBitMap(final int pp) {
		new Thread(new Runnable() {
			public void run() {
				switch (pp) {
					case 1:
						float[] array1 = {1, 0, 0, 0, 100, 0, 1, 0, 0, 100, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0};
						result = UMPhotoUtils.filterBitmapByColorMatrix(bitmap, array1);
						break;
					case 2:
						//灰度效果：
						float[] array2 = {0.33F, 0.59F, 0.11F, 0, 0, 0.33F, 0.59F, 0.11F, 0, 0, 0.33F, 0.59F, 0.11F, 0, 0, 0, 0, 0, 1, 0};
						result = UMPhotoUtils.filterBitmapByColorMatrix(bitmap, array2);
						break;
					case 3:
						//图像反转：
						float[] array3 = {-1, 0, 0, 1, 1, 0, -1, 0, 1, 1, 0, 0, -1, 1, 1, 0, 0, 0, 1, 0};
						result = UMPhotoUtils.filterBitmapByColorMatrix(bitmap, array3);
						break;
					case 4:
						//怀旧效果：
						float[] array4 = {0.393F, 0.769F, 0.189F, 0, 0, 0.349F, 0.686F, 0.168F, 0, 0, 0.272F, 0.534F, 0.131F, 0, 0, 0, 0, 0, 1, 0};
						result = UMPhotoUtils.filterBitmapByColorMatrix(bitmap, array4);
						break;
					case 5:
						//去色效果：
						float[] array5 = {1.5F, 1.5F, 1.5F, 0, -1, 1.5F, 1.5F, 1.5F, 0, -1, 1.5F, 1.5F, 1.5F, 0, -1, 0, 0, 0, 1, 0};
						result = UMPhotoUtils.filterBitmapByColorMatrix(bitmap, array5);
						break;
					case 6:
						//高饱和度：
						float[] array6 = {1.438F, -0.122F, -0.016F, 0, -0.03F, -0.062F, 1.378F, -0.016F, 0, 0.05F, -0.062F, -0.122F, 1.483F, 0, -0.02F, 0, 0, 0, 1, 0};
						result = UMPhotoUtils.filterBitmapByColorMatrix(bitmap, array6);
						break;

				}
				mHandler.sendEmptyMessage(10000);
			}
		}).start();
	}
}
