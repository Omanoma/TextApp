package com.example.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;
import java.util.Map;

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
    private void add(){
        Map<String, Object> users = new HashMap<>();
        User a = new User("one","two","three");
        db.collection("userInfo").document(a.username).set(a.TurnToHash());
    }

    public void checkValidate(View v){
        String user = username.getText().toString();
        String pass = password.getText().toString();
         db.collection("userInfo").document(user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if(task.isSuccessful()){
                     if(task.getResult().get("Password").equals(pass)){
                         Toast.makeText(Login.this, "Correct UserName OR Password", Toast.LENGTH_LONG).show();
                     }
                     else{
                         Toast.makeText(Login.this, "Incorrect UserName OR Password", Toast.LENGTH_LONG).show();

                     }

                 }
                 else{
                     Toast.makeText(Login.this, "Incorrect UserName OR Password", Toast.LENGTH_LONG).show();
                 }
             }
         });
    }

}