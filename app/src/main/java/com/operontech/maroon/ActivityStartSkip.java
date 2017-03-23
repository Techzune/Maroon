package com.operontech.maroon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ActivityStartSkip extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_skip);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
