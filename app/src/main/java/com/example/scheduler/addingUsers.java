package com.example.scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class addingUsers extends AppCompatActivity {


    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        addButton = (Button)findViewById(R.id.add_friends);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addingUsers.this, ThirdActivity.class);
                startActivity(intent);
            }
        });


    }
}
