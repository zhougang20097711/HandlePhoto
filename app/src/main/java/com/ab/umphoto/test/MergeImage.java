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
public class MergeImage extends Activity {
	private ImageView imageview;
	private TextView message;
	private RelativeLayout zoomImage01;
	private RelativeLayout zoomImage02;
	private RelativeLayout zoomImage03;
	private Bitmap bitmap;
	private Bitmap wbitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mergeimage);
		init();
	}


	private void init() {
		imageview = (ImageView) findViewById(R.id.imageview);
		message = (TextView) findViewById(R.id.message);
		zoomImage01 = (RelativeLayout) findViewById(R.id.zoomImage01);
		zoomImage02 = (RelativeLayout) findViewById(R.id.zoomImage02);
		zoomImage03 = (RelativeLayout) findViewById(R.id.zoomImage03);
		message.setText("");
		imageview.setImageResource(R.mipmap.prd_bx_13);
		bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.prd_bx_13);
		wbitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.choice_gy_1);
		zoomImage01.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				message.setText("mergeImage(bitmap,watermark,0, 0)");
				imageview.setImageBitmap(UMPhotoUtils.mergeImage(bitmap, wbitmap, 0, 0));
			}
		});
		zoomImage02.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int w = bitmap.getWidth() - wbitmap.getWidth();
				int h = bitmap.getHeight() - wbitmap.getHeight();
				message.setText("mergeImage(bitmap,watermark,"+w+","+h+" 0)");
				imageview.setImageBitmap(UMPhotoUtils.mergeImage(bitmap, wbitmap, w, h));
			}
		});
		zoomImage03.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int w = bitmap.getWidth() - wbitmap.getWidth();
				int h = bitmap.getHeight() - wbitmap.getHeight();
				message.setText("mergeImage(bitmap,watermark,"+w/2+","+h/2+" 0)");
				imageview.setImageBitmap(UMPhotoUtils.mergeImage(bitmap, wbitmap, w/2, h/2));
			}
		});
	}
}
