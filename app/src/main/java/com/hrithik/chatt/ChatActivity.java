package com.hrithik.chatt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {

    private TextView user_name;

    private String name, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        name = getIntent().getStringExtra("name");
        uid = getIntent().getStringExtra("uid");

        user_name = findViewById(R.id.name);
        user_name.setText(name);

    }
}