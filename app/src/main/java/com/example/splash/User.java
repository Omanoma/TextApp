package com.example.splash;


import static io.grpc.okhttp.internal.Platform.logger;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Parcelable {
    String username;
    String password;
    String email;
    ArrayList<Map<String,String>> friendsList;
    LocalDate date;

    FirebaseFirestore db;
    FirebaseAuth auth;
    String image1;
    int image;
    FirebaseStorage storage;

    String userID;
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        IMAGE a = new IMAGE();
        image = a.RandomImage();
        image1 = "gs://textapp-75211.appspot.com/face1.png";
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
        IMAGE a = new IMAGE();
        image = a.RandomImage();
        db.collection("userInfo").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    userID = task.getResult().get("UserID").toString();
                    friendsList = (ArrayList<Map<String, String>>) task.getResult().get("FriendLists");

                }
                catch(Exception e){
                    userID = "NULL2";

                }
            }
        });
        System.out.println(friendsList);
    }

    protected User(Parcel in) {
        username = in.readString();
        password = in.readString();
        email = in.readString();
        image1 = in.readString();
        image = in.readInt();
        userID = in.readString();

        // Read friendsList
        int friendsListSize = in.readInt();
        this.friendsList = new ArrayList<>(friendsListSize);
        for (int j = 0; j < friendsListSize; j++) {
            Bundle friendBundle = in.readBundle(getClass().getClassLoader());
            Map<String, String> friendMap = new HashMap<>();
            for (String key : friendBundle.keySet()) {
                friendMap.put(key, friendBundle.getString(key));
            }
            this.friendsList.add(friendMap);
        }
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
    public int getImage() {
        return image;
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
                    users.add(new User(username,"12332"));
                }
                futureResult.complete(users);
            } else {
                futureResult.completeExceptionally(task.getException());
            }
        });

        return futureResult;
    }

    public CompletableFuture<List<User>>  getFriendList() {
        CompletableFuture<List<User>> usersList = new CompletableFuture<>();
        List<User> users = new ArrayList<>();
        for(int i = 0; i<friendsList.size();i++){
            users.add(new User(friendsList.get(i).get("UserName"),""));
        }
        usersList.complete(users);
        return usersList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(email);
        parcel.writeString(image1);
        parcel.writeInt(image);
        parcel.writeString(userID);

        // Write friendsList size
        if (friendsList != null) {
            parcel.writeInt(friendsList.size());
            for (Map<String, String> friend : friendsList) {
                Bundle friendBundle = new Bundle();
                for (Map.Entry<String, String> entry : friend.entrySet()) {
                    friendBundle.putString(entry.getKey(), entry.getValue());
                }
                parcel.writeBundle(friendBundle);
            }
        } else {
            parcel.writeInt(0); // friendsList is null or empty
        }
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
    int image4 = R.mipmap.face3;
    int [] a = {image1,image2,image4};
    public int RandomImage(){
        Random rd = new Random();
        int g = rd.nextInt(a.length);
        return a[g];
    }
}
