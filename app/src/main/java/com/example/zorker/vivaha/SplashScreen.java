package com.example.zorker.vivaha;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    private ImageView iv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        iv_logo = (ImageView) findViewById(R.id.iv_splash_screen);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.logo_animation);
        iv_logo.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
             startActivity(new Intent(SplashScreen.this,MainActivity.class));
             finish();
            }
        },3000);

    }
}
