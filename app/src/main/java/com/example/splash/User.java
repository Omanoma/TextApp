package com.example.splash;

import static io.grpc.okhttp.internal.Platform.logger;

import android.os.Build;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
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
    boolean ab;
    FirebaseFirestore db;
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        db = FirebaseFirestore.getInstance();
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
        logger.info(a+" "+password);
        System.out.println(password.compareTo(a) == 0);
        System.out.println(password.compareTo(a));
        return password.compareTo(a) == 0;
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

    public Task<Boolean> checkEmail() {
        final TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("EmailList").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    boolean exists = task.getResult().exists();
                    taskCompletionSource.setResult(!exists);
                } else {
                    taskCompletionSource.setException(task.getException());
                }
            }
        });
        return taskCompletionSource.getTask();
    }

    public Task<Boolean> checkUser(){
        final TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("userInfo").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    boolean exists = task.getResult().exists();
                    taskCompletionSource.setResult(!exists);
                } else {
                    taskCompletionSource.setException(task.getException());
                }
            }
        });
        return taskCompletionSource.getTask();
    }
    public void addToDatabase(){
        db.collection("userInfo").document(username).set(TurnToHash());
        db.collection("EmailList").document(email).set(EmailToHash());
    }

}