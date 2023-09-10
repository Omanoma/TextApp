package com.example.splash;


//import static io.grpc.okhttp.internal.Platform.logger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView ImageView;
    Handler handler;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(R.mipmap.face1 + " OZIOMA FACE1");
        System.out.println(R.mipmap.face2 + " OZIOMA FACE2");
        System.out.println(R.mipmap.face3 + " OZIOMA FACE3");
        System.out.println(R.mipmap.face4 + " OZIOMA FACE4");
        handler = new Handler();
        intent = new Intent(this,Login.class);
        ImageView = findViewById(R.id.face1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        rAnimation();
        handler.postDelayed(() -> setContentView(R.layout.splash2),3000);
        handler.postDelayed(this::changeActivity,3500);

    }
    private void rAnimation(){
        Animation rototeAnimation = AnimationUtils.loadAnimation(this,R.anim.face);
        ImageView.setAnimation(rototeAnimation);
    }
    private void changeActivity(){
        finish();
        startActivity(intent);
    }
}
