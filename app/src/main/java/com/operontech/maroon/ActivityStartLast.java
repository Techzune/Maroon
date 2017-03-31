package com.operontech.maroon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ActivityStartLast extends AppCompatActivity {

	Button finishButton;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_last);

		finishButton = (Button) findViewById(R.id.startLast_button_begin);
		finishButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				final Intent newIntent = new Intent(ActivityStartLast.this, ActivityMain.class);
				newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(newIntent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});
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
