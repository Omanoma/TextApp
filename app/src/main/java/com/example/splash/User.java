package com.example.splash;

import static io.grpc.okhttp.internal.Platform.logger;

import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
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
    int image;
    ArrayList<User> user;
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        IMAGE a = new IMAGE();
        image = a.RandomImage();
        CreatedDate();
    }
    public User(){
        getAllContact();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }
    public User(String username,String password){
        this.username = username;
        this.password = password;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        IMAGE a = new IMAGE();
        image = a.RandomImage();
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
        logger.info(password.compareTo(a)+"  OZIOMS");
        return password.compareTo(a) == 0;
    }
    public CompletableFuture<Boolean> validateUserOrPass(){
        CompletableFuture<Boolean> futureResult = new CompletableFuture<>();
        System.out.println(password);
        db.collection("userInfo").document(username).get().addOnCompleteListener(task -> {
            System.out.println(username);
            System.out.println(password);
            System.out.println(task.getResult().get("Password") +" OZIOMA");
            if(task.isSuccessful() && password.compareTo((String)(task.getResult().get("Password"))) == 0){
                System.out.println(task.getResult().get("Password") + "The pepe");
                futureResult.complete(true);

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
        db.collection("userInfo").document(username).set(TurnToHash());
        db.collection("EmailList").document(email).set(EmailToHash());
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser a = auth.getCurrentUser();
                    assert a != null;
                    logger.info(a.getEmail());
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

    public int getImage() {
        return image;
    }
    public void getAllContact(){
        db.collection("userInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc : task.getResult()){
                    String a = doc.getData().get("UserName").toString();
                    String b = doc.getData().get("Password").toString();
                    user.add(new User(a,b));

                }
            }
        });
    }


    enum label{
        PASSWORD,
        USERNAME,
        EMAIL
    }
}



 class IMAGE{
    int image1 = R.mipmap.face1;
    int image2 = R.mipmap.face2;
    int image3 = R.mipmap.face4;
    int image4 = R.mipmap.face3;
    int [] a = {image1,image2,image3,image4};
    public int RandomImage(){
        Random rd = new Random();
        int g = rd.nextInt(a.length);
        return a[g];
    }
}