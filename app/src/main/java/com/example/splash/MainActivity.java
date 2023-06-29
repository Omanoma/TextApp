package com.example.splash;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView ImageView;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        ImageView = findViewById(R.id.face1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        rAnimation();
        handler.postDelayed(() -> setContentView(R.layout.splash2),3000);

    }
    private void rAnimation(){
        Animation rototeAnimation = AnimationUtils.loadAnimation(this,R.anim.face);
        ImageView.setAnimation(rototeAnimation);
    }
}
