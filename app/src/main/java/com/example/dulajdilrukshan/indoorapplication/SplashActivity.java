package com.example.dulajdilrukshan.indoorapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences =
                        getSharedPreferences(AppConstants.PREF_SEESION_DATA, MODE_PRIVATE);
                boolean isLoggedIn = false;
                if (preferences != null){
                    isLoggedIn = preferences.getBoolean(AppConstants.PREF_SEESION_DATA_KEY_IS_LOGGED_IN, false);
                }

                Intent intent = null;
                if(isLoggedIn){
                    intent = new Intent(SplashActivity.this, HomeActivity.class);

                }else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }


                startActivity(intent);
            }
        }, 4000);

    }
}
