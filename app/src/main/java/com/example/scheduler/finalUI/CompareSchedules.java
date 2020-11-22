package com.example.scheduler.finalUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.scheduler.R;
import com.example.scheduler.mainActivities.ThirdActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CompareSchedules extends AppCompatActivity {

    private FloatingActionButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_schedules);


        //For now
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompareSchedules.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(CompareSchedules.this, ThirdActivity.class);
        startActivity(intent);
    }

}