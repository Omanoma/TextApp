package com.example.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Sign_up extends AppCompatActivity {
    EditText email;
    EditText pass;
    EditText user;
    EditText repass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.signUpEmail);
        pass = findViewById(R.id.signUpPassword);
        user = findViewById(R.id.signUpUsername);
        repass = findViewById(R.id.signUpRePassword);
    }
    public void ValidateOrAdd(View v){
        String e = email.getText().toString();
        String p = pass.getText().toString();
        String u = user.getText().toString();
        String rp = repass.getText().toString();
        User a = new User(u,p,e);
        if(!a.checkUser().isSuccessful()) Toast.makeText(Sign_up.this,"User Not correct",Toast.LENGTH_LONG).show();
        else if(!a.checkEmail().isSuccessful())  Toast.makeText(Sign_up.this,"Email Not correct",Toast.LENGTH_LONG).show();
        else if(a.SamePass(rp) == false) Toast.makeText(Sign_up.this,"Password Not correct",Toast.LENGTH_LONG).show();
        else{
            a.addToDatabase();
            Toast.makeText(Sign_up.this,"Password correct",Toast.LENGTH_LONG).show();
            changeContactList();
        }

    }
    public void changeLogin(View v){
        Intent in = new Intent(this,Login.class);
        finish();
        startActivity(in);
    }
    public void changeContactList(){
        Intent in = new Intent(this, Contact_List.class);
        finish();
        startActivity(in);
    }
    public void changeTAndC(View v){
        setContentView(R.layout.terms_and_conditions);
    }


}
