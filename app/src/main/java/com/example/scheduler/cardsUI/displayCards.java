package com.example.scheduler.cardsUI;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

public class displayCards extends WearableActivity {

	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_cards);

		mTextView = (TextView) findViewById(R.id.text);

		// Enables Always-on
		setAmbientEnabled();
	}
}