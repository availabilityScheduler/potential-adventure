package com.example.scheduler.mainActivities;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.scheduler.R;

public class UpdateNotification extends AppCompatActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updates_notification);
		TextView textView=findViewById(R.id.saved_schedules);
		String message = getIntent().getStringExtra("Matched!");
		textView.setText(message);
	}
}