package com.awandigital.sikas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.awandigital.sikas.db.SessionManager;

public class SplashScreen extends AppCompatActivity {
    SessionManager sessionManager;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sessionManager = new SessionManager(this);

        if (sessionManager.isFirstLook() && sessionManager.isLoggedIn() ) {
            intent = new Intent(this, Home.class);
        } else if (sessionManager.isFirstLook() ) {
            intent = new Intent(this, Login.class);
        } else {
            intent = new Intent(this, WelcomePage.class);
        }
        startActivity(intent);
        finish();
    }
}