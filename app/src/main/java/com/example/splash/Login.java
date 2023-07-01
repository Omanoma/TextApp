package com.example.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText username;
    EditText password;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        //username = findViewById(R.id.login_username);
        //password = findViewById(R.id.login_password);
        add();

    }
    private void add(){
        Map<String, Object> users = new HashMap<>();
        User a = new User("one","two","three");
        db.collection("userInfo").add(a.TurntoHash());
    }
    private void checkValidate(){
         db.collection("userInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
             @Override
             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                 for(QueryDocumentSnapshot doc : task.getResult()){
                     User user = (User) doc.get("User");
                 }
             }
         });
    }
}