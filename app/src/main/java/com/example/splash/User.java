package com.example.splash;

import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class User {
    String username;
    String password;
    String email;
    LocalDate date;
    FirebaseFirestore db;
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        CreatedDate();
    }
    public User(String username,String password){
        this.username = username;
        this.password = password;
    }
    private void CreatedDate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date =  LocalDate.now();;
        }
    }
    public Map<String,Object> TurnToHash(){
        Map<String,Object> a = new HashMap<>();
        a.put("UserName",username);
        a.put("Password",password);
        a.put("Email",email);
        a.put("Date",date.toString());
        return a;
    }
    public Map<String,Object> EmailToHash(){
        Map<String,Object> a = new HashMap<>();
        a.put("Email",email);
        return a;
    }
    public boolean SamePass(String a){
        if(password.equals(a))return true;
        return false;
    }
    public boolean validateUserOrPass(){
        final boolean [] a = {false};
        db.collection("userInfo").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(SamePass(task.getResult().get("Password").toString())){
                        a[0] = true;
                    }
                }
            }
        });
        return a[0];
    }

    public boolean checkEmail(){
        final boolean[] a = {true};
        db.collection("EmailList").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    a[0] = false;
                }
            }
        });
        return a[0];
    }
    public boolean checkUser(){
        final boolean[] a = {true};
        db.collection("userInfo").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    a[0] = false;
                }
            }
        });
        return a[0];
    }
    public void addToDatabase(){
        db.collection("userInfo").document(username).set(TurnToHash());
        db.collection("EmailList").document(username).set(EmailToHash());
    }

}