package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;


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
        Bundle bundle = new Bundle();
        CompletableFuture<Boolean> h = (a.validateUserOrPass());
        AtomicReference<String> statement = new AtomicReference<>("");
        h.thenAccept(isValid -> {
            if (isValid) {
                bundle.putParcelable("Username", a);
                statement.set("User validation successful.");
                Toast.makeText(Login.this, statement.get(),Toast.LENGTH_LONG).show();//changeContactList();
                System.out.println("User validation successful.");
                changeContactList(bundle);
            } else {
                statement.set("User validation failed.");
                System.out.println("User validation failed.");
                Toast.makeText(Login.this, statement.get(),Toast.LENGTH_LONG).show();//changeContactList();

            }
        });

    }
    public void changeSignUp(View v){
        Intent in = new Intent(this,Sign_up.class);
        finish();
        startActivity(in);
    }
    public void changeContactList(Bundle bun){
        Intent in = new Intent(this, Contact_List.class);
        in.putExtras(bun);
        finish();
        startActivity(in);
    }
    public void changeForgetPasswords(View v){
        Intent in = new Intent(this,forgetPasswords.class);
        startActivity(in);
    }

}