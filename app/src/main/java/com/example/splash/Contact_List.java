package com.example.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Contact_List extends AppCompatActivity {
    BottomNavigationView menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        User a = (User) getIntent().getParcelableExtra("Username");
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new Message(a)).commit();
        menu = findViewById(R.id.menu);


        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.message) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Message(a)).commit();
                    return true;
                } else if (itemId == R.id.contact) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Message(a)).commit();
                    return true;
                } else if (itemId == R.id.setting) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Settings()).commit();
                    return true;
                }
                return false;

                }
        });
    }


}