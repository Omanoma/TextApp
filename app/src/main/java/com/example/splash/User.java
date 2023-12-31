package com.example.splash;


import static io.grpc.okhttp.internal.Platform.logger;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    String username;
    String password;
    String email;
    LocalDate date;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String image;
    FirebaseStorage storage;

    String userID;
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        image = "gs://textapp-75211.appspot.com/face1.png";
        CreatedDate();
    }
    public User(){
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }
    public User(String username, String password){
        this.username = username;
        this.password = password;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        db.collection("userInfo").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    userID = task.getResult().get("UserID").toString();
                    image = task.getResult().get("Url").toString();
                }
                catch(Exception e){
                    userID = "NULL2";

                }
            }
        });
    }
    private void CreatedDate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date =  LocalDate.now();
        }
    }

    public Map<String,Object> EmailToHash(){
        Map<String,Object> a = new HashMap<>();
        a.put("Email",email);
        return a;
    }
    public boolean SamePass(String a){
        logger.info(a+" "+password);
        logger.info(password.compareTo(a)+"  OZIOMS");
        return password.compareTo(a) == 0;
    }
    public CompletableFuture<Boolean> validateUserOrPass(){
        CompletableFuture<Boolean> futureResult = new CompletableFuture<>();
        db.collection("userInfo").document(username).get().addOnCompleteListener(task -> {
            System.out.println(task.getResult().get("Email"));
            if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                if (doc.exists() && doc.contains("Email")) {
                    LOGIN(doc.getString("Email")).thenAccept(user -> {
                        futureResult.complete(user);
                    });
                }
            }
        });
        return futureResult;
    }
    private CompletableFuture<Boolean> LOGIN(String email1) {
        CompletableFuture<Boolean> futureResult = new CompletableFuture<>();
        if (TextUtils.isEmpty(email1) || TextUtils.isEmpty(password)) {
            System.out.println(email1 + "  Ozioma");
            futureResult.complete(false);
            return futureResult;
        }
        auth.signInWithEmailAndPassword(email1, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    futureResult.complete(true);
                }
            }
        });
        return futureResult;
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
                    taskCompletionSource.setException(Objects.requireNonNull(task.getException()));
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
                    taskCompletionSource.setException(Objects.requireNonNull(task.getException()));
                }
            }
        });
        return taskCompletionSource.getTask();
    }
    public void addToDatabase(){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser a = auth.getCurrentUser();
                    assert a != null;
                    logger.info(a.getEmail());
                    Map<String,Object> c = new HashMap<>();
                    c.put("UserName",username);
                    c.put("Password",password);
                    c.put("Email",email);
                    c.put("UserID",a.getUid());
                    c.put("Date",date.toString());
                    c.put("Url","gs://textapp-75211.appspot.com/face1.png");
                    db.collection("userInfo").document(username).set(c);
                    db.collection("EmailList").document(email).set(EmailToHash());
                    userID = a.getUid();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();
                    a.updateProfile(profileChangeRequest);
                    a.sendEmailVerification();
                }
            }
        });
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
    public String getCurrentUser(){
        return auth.getCurrentUser().getUid();
    }
    public CompletableFuture<List<User>> getAllContact() {
        CompletableFuture<List<User>> futureResult = new CompletableFuture<>();

        db.collection("userInfo").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> users = new ArrayList<>();

                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String username = doc.getString("UserName");
                    String password = doc.getString("Password");
                    users.add(new User(username, password));
                }
                futureResult.complete(users);
            } else {
                futureResult.completeExceptionally(task.getException());
            }
        });

        return futureResult;
    }
    enum label{
        PASSWORD,
        USERNAME,
        EMAIL
    }
}
