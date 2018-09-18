package com.example.dulajdilrukshan.indoorapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.Path;

public class HomeActivity extends AppCompatActivity {

    Globals sharedData = Globals.getInstance();
    TextView txtusername ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        Intent i= new Intent(this, ScheduledService.class);
        startService(i);

    }


    public void logout(View view) {
        Intent logout = new Intent(this, LoginActivity.class);
        startActivity(logout);
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
    }

    public void navigate(View view) {


      //  SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = mPreferences.edit();
       // String name = mPreferences.getString(getString(R.string.pusername), "");

       // Toast.makeText(getApplicationContext(),name, Toast.LENGTH_SHORT).show();


        Intent path = new Intent(this, PathActivity.class);
        startActivity(path);
    }


    public void LocateFriend(View view) {
        startActivity(new Intent(HomeActivity.this,LocateFriend.class));
    }
}
