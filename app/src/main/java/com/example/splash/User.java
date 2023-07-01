package com.example.splash;

import android.os.Build;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class User {
    String username;
    String password;
    String email;
    LocalDate date;
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        CreatedDate();
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
}
