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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    String username;
    String password;
    String email;
    LocalDate date;
    FirebaseFirestore db;
    int image;
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
            date =  LocalDate.now();
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
        return password.compareTo(a) == 0 && Regex(label.PASSWORD);
    }
    public Task<Boolean> validateUserOrPass(){
        final TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("userInfo").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                       if(SamePass(task.getResult().get("Password").toString())){
                           taskCompletionSource.setResult(true);
                    }
                }
            }
        });
        return taskCompletionSource.getTask();
    }

    public Task<Boolean> checkEmail() {
        final TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();
        if(!Regex(label.EMAIL)){
            taskCompletionSource.setResult(false);
            return taskCompletionSource.getTask();
        }
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
        if(!Regex(label.USERNAME)){
            taskCompletionSource.setResult(false);
            return taskCompletionSource.getTask();
        }
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
    public boolean Regex(@NonNull label a){
        String pattern = "";
        Matcher m = null;
        if(a.equals(label.EMAIL)) {
            pattern = "^[a-zA-Z0-9]+@[a-zA-Z]+/.[a-z]+$";
            Pattern p = Pattern.compile(pattern);
            m = p.matcher(email);
        }
        else if(a.equals(label.PASSWORD)){
            pattern ="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{6,}$";
            Pattern p = Pattern.compile(pattern);
            m = p.matcher(password);
        }
        else if(a.equals(label.USERNAME)){
            pattern ="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{6,}$";
            Pattern p = Pattern.compile(pattern);
            m = p.matcher(username);
        }
        return m.matches();
    }

    public int getImage() {
        return image;
    }


    enum label{
        PASSWORD,
        USERNAME,
        EMAIL
    }

}