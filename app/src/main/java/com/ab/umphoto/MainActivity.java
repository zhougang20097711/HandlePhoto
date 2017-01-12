package com.ab.umphoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.ab.umphoto.test.FilterBitmap;
import com.ab.umphoto.test.GetImage;
import com.ab.umphoto.test.GrayImage;
import com.ab.umphoto.test.ImageActivity;
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
	private RelativeLayout cropImage;
	private RelativeLayout rotateImage;
	private RelativeLayout waterImage;
	private RelativeLayout toneImage;
	private RelativeLayout oldImage;
	private RelativeLayout sharpenImage;
	private RelativeLayout blurImage;
	private RelativeLayout skinImage;
	private RelativeLayout sketchImage;
	private RelativeLayout compressImage;
	private RelativeLayout mosaicImage;
	private RelativeLayout filterBitmapByColorMatrix;


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
		cropImage = (RelativeLayout) findViewById(R.id.cropImage);
		rotateImage = (RelativeLayout) findViewById(R.id.rotateImage);
		waterImage = (RelativeLayout) findViewById(R.id.waterImage);
		toneImage = (RelativeLayout) findViewById(R.id.toneImage);
		oldImage = (RelativeLayout) findViewById(R.id.oldImage);
		sharpenImage = (RelativeLayout) findViewById(R.id.sharpenImage);
		blurImage = (RelativeLayout) findViewById(R.id.blurImage);
		skinImage = (RelativeLayout) findViewById(R.id.skinImage);
		sketchImage = (RelativeLayout) findViewById(R.id.sketchImage);
		compressImage = (RelativeLayout) findViewById(R.id.compressImage);
		mosaicImage = (RelativeLayout) findViewById(R.id.mosaicImage);
		filterBitmapByColorMatrix = (RelativeLayout) findViewById(R.id.filterBitmapByColorMatrix);

		saveimage.setOnClickListener(this);
		getimage.setOnClickListener(this);
		zoomScale.setOnClickListener(this);
		mergeImage.setOnClickListener(this);
		mixImage.setOnClickListener(this);
		roundCornerImage.setOnClickListener(this);
		grayImage.setOnClickListener(this);
		reflectionImage.setOnClickListener(this);
		cropImage.setOnClickListener(this);
		rotateImage.setOnClickListener(this);
		waterImage.setOnClickListener(this);
		toneImage.setOnClickListener(this);
		oldImage.setOnClickListener(this);
		sharpenImage.setOnClickListener(this);
		blurImage.setOnClickListener(this);
		skinImage.setOnClickListener(this);
		sketchImage.setOnClickListener(this);
		compressImage.setOnClickListener(this);
		mosaicImage.setOnClickListener(this);
		filterBitmapByColorMatrix.setOnClickListener(this);
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
			case R.id.cropImage:
				intent.setClass(MainActivity.this, ImageActivity.class);
				intent.putExtra("position",1000);
				break;
			case R.id.rotateImage:
				intent.setClass(MainActivity.this, ImageActivity.class);
				intent.putExtra("position",1001);
				break;
			case R.id.waterImage:
				intent.setClass(MainActivity.this, ImageActivity.class);
				intent.putExtra("position",1002);
				break;
			case R.id.toneImage:
				intent.setClass(MainActivity.this, ImageActivity.class);
				intent.putExtra("position",1003);
				break;
			case R.id.oldImage:
				intent.setClass(MainActivity.this, ImageActivity.class);
				intent.putExtra("position",1004);
				break;
			case R.id.sharpenImage:
				intent.setClass(MainActivity.this, ImageActivity.class);
				intent.putExtra("position",1005);
				break;
			case R.id.blurImage:
				intent.setClass(MainActivity.this, ImageActivity.class);
				intent.putExtra("position",1006);
				break;
			case R.id.skinImage:
				intent.setClass(MainActivity.this, ImageActivity.class);
				intent.putExtra("position",1007);
				break;
			case R.id.sketchImage:
				intent.setClass(MainActivity.this, ImageActivity.class);
				intent.putExtra("position",1008);
				break;
			case R.id.compressImage:
				intent.setClass(MainActivity.this, ImageActivity.class);
				intent.putExtra("position",1009);
				break;
			case R.id.mosaicImage:
				intent.setClass(MainActivity.this, ImageActivity.class);
				intent.putExtra("position",1010);
				break;
			case R.id.filterBitmapByColorMatrix:
				intent.setClass(MainActivity.this, FilterBitmap.class);
				break;
			default:
				break;
		}
		startActivity(intent);
	}
}
