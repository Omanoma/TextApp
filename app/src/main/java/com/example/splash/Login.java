package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText username;
    EditText password;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);

    }
}