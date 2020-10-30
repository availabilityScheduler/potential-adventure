package com.example.scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

//Just a set up
public class scheduler extends AppCompatActivity {

     RadioButton mon6am;
     Button saveSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.content_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mon6am = (RadioButton)findViewById(R.id.mon6am);
        saveSchedule = (Button)findViewById(R.id.saveSchedule);

        saveSchedule.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("hehe");
            }
        });
    }
}