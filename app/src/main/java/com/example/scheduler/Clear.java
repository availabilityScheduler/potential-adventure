package com.example.scheduler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class Clear extends ThirdActivity implements View.OnClickListener {
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        setTitle("RadioGroupActivity");
        mRadioGroup = (RadioGroup) findViewById(R.id.clearRG);
        Button clearButton = (Button) findViewById(R.id.clear);
        clearButton.setOnClickListener(this);
    }

    //supposed to clear all buttons on-click

    public void onClick(View v) {
        mRadioGroup.clearCheck();
    }
}