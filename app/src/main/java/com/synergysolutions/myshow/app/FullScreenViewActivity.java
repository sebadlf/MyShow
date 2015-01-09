package com.synergysolutions.myshow.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.synergysolutions.myshow.app.helper.Utils;

public class FullScreenViewActivity extends Activity{

	private Utils utils;
	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_screen_activity);

		viewPager = (ViewPager) findViewById(R.id.pager);

		utils = new Utils(getApplicationContext());

		Intent i = getIntent();
		int position = i.getIntExtra("position", 0);

		adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
				utils.getFilePaths());

		viewPager.setAdapter(adapter);

		// displaying selected image first
		viewPager.setCurrentItem(position);
	}
}
