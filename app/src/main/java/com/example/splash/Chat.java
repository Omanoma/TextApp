package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

public class Chat extends AppCompatActivity {
    TextView username;
    ShapeableImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        icon = findViewById(R.id.icons);
        username = findViewById(R.id.usernameChat);
        Bundle b = getIntent().getExtras();
         icon.setImageResource((Integer) b.get("Image"));
         username.setText(b.getString("User"));

    }

}