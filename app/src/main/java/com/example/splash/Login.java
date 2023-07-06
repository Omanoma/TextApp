package com.example.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;




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

    public void checkValidate(View v){
        String user = username.getText().toString();
        String pass = password.getText().toString();
        User a = new User(user,pass);
        String statement = (a.validateUserOrPass())? "Correct Username & Password" : "Incorrect Username & Password";
        Toast.makeText(Login.this,statement,Toast.LENGTH_LONG);
    }
    public void changeSignUp(View v){
        Intent in = new Intent(this,Sign_up.class);
        startActivity(in);
    }

}