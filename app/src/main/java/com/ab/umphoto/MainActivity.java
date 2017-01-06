package com.ab.umphoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.ab.umphoto.test.GetImage;
import com.ab.umphoto.test.GrayImage;
import com.ab.umphoto.test.MergeImage;
import com.ab.umphoto.test.MixImage;
import com.ab.umphoto.test.ReflectionImage;
import com.ab.umphoto.test.RoundCornerImage;
import com.ab.umphoto.test.SaveImage;
import com.ab.umphoto.test.ZoomImage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	private RelativeLayout saveimage;
	private RelativeLayout getimage;
	private RelativeLayout zoomScale;
	private RelativeLayout mergeImage;
	private RelativeLayout mixImage;
	private RelativeLayout roundCornerImage;
	private RelativeLayout grayImage;
	private RelativeLayout reflectionImage;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_main);
		initId();
	}


	private void initId() {
		saveimage = (RelativeLayout) findViewById(R.id.saveimage);
		getimage = (RelativeLayout) findViewById(R.id.getimage);
		zoomScale = (RelativeLayout) findViewById(R.id.zoomScale);
		mergeImage = (RelativeLayout) findViewById(R.id.mergeImage);
		mixImage = (RelativeLayout) findViewById(R.id.mixImage);
		roundCornerImage = (RelativeLayout) findViewById(R.id.roundCornerImage);
		grayImage = (RelativeLayout) findViewById(R.id.grayImage);
		reflectionImage = (RelativeLayout) findViewById(R.id.reflectionImage);

		saveimage.setOnClickListener(this);
		getimage.setOnClickListener(this);
		zoomScale.setOnClickListener(this);
		mergeImage.setOnClickListener(this);
		mixImage.setOnClickListener(this);
		roundCornerImage.setOnClickListener(this);
		grayImage.setOnClickListener(this);
		reflectionImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
			case R.id.saveimage:
				intent.setClass(MainActivity.this, SaveImage.class);
				break;
			case R.id.getimage:
				intent.setClass(MainActivity.this, GetImage.class);
				break;
			case R.id.zoomScale:
				intent.setClass(MainActivity.this, ZoomImage.class);
				break;
			case R.id.mergeImage:
				intent.setClass(MainActivity.this, MergeImage.class);
				break;
			case R.id.mixImage:
				intent.setClass(MainActivity.this, MixImage.class);
				break;
			case R.id.roundCornerImage:
				intent.setClass(MainActivity.this, RoundCornerImage.class);
				break;
			case R.id.grayImage:
				intent.setClass(MainActivity.this, GrayImage.class);
				break;
			case R.id.reflectionImage:
				intent.setClass(MainActivity.this, ReflectionImage.class);
				break;
			default:
				break;
		}

		startActivity(intent);
	}
}
