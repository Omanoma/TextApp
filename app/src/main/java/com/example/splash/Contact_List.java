package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class Contact_List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        User a = new User();
        RecyclerView r = findViewById(R.id.recycle);
        CardList_Adapter c = new CardList_Adapter(this,a.user);
        r.setAdapter(c);
        r.setLayoutManager(new LinearLayoutManager(this));

    }
}