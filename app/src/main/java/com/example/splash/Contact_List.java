package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Contact_List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        User a = new User();
        RecyclerView r = findViewById(R.id.recycle);
        CompletableFuture<List<User>> futureUserList = a.getAllContact();
        futureUserList.thenAccept(users -> {
            List<User> user = users;
            CardList_Adapter c = new CardList_Adapter((Context) this, user);
            r.setAdapter(c);
            r.setLayoutManager(new LinearLayoutManager(this));

            System.out.println(user + " kill 2");
        });

        //System.out.println(a.user.size() + " OZIOMASS");


    }
}