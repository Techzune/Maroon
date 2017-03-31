package com.operontech.maroon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ActivityStart extends AppCompatActivity {

	Button buttonStudent;
	Button buttonNonStudent;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		buttonStudent = (Button) findViewById(R.id.start_button_student);
		buttonNonStudent = (Button) findViewById(R.id.start_button_nonstudent);

		buttonNonStudent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				final Intent intent = new Intent(ActivityStart.this, ActivityStartLast.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

	}
}
