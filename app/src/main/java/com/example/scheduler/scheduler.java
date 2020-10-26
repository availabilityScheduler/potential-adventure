package com.example.scheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

//Just a set up
public class scheduler extends AppCompatActivity {

    private RadioButton mon6am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        mon6am = (RadioButton)findViewById(R.id.mon6am);

    }
}