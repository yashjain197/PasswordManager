package com.passwordmanager.passwordmanager.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.passwordmanager.passwordmanager.LoginOTPScreen.LoginOTPActivity;
import com.passwordmanager.passwordmanager.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashScreen.this, LoginOTPActivity.class);
                startActivity(i);
                finish();
            }
        },2500);
    }
}