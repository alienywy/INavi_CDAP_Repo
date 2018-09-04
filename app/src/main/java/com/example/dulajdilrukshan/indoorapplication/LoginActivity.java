package com.example.dulajdilrukshan.indoorapplication;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    StringRequest MyStringRequest;
    RequestQueue MyRequestQueue;
    String url;
    boolean session = false;

  //  private SharedPreferences mPreferences;
  //  private SharedPreferences.Editor mEditor;

    Globals sharedData = Globals.getInstance();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyRequestQueue = Volley.newRequestQueue(this);

        url = "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/login";
        //mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //mEditor = mPreferences.edit();


    }
//    Checking for internet connection Avaialable

    public boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected())
            return true;
        else
            return false;
    }


    public void pushData() {
        final EditText username = (EditText) findViewById(R.id.txtloginusername);
        final EditText password = (EditText) findViewById(R.id.txtloginpassword);

        final String pusername = username.getText().toString().trim();
        final String pass1 = password.getText().toString().trim();


        final String url = "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/login";


        MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                 Server Sends a Response as "true" if data is Success

                    if (response.equals("1")) {
                        session = true;
                        enter();

                        sharedData.setValue(pusername);

                       // Toast.makeText(getApplicationContext(), sharedData.getValue() + " from Global class ", Toast.LENGTH_LONG).show();

                    } else if (!session) {

                        Toast.makeText(getApplicationContext(), "Incorrect Credentials", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    String err = e.getMessage();
                    Toast.makeText(getApplicationContext(), err, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> FormData = new HashMap<String, String>();

                FormData.put("username", pusername);
                FormData.put("password", pass1);

                return FormData;

            }
        };
        MyRequestQueue.add(MyStringRequest);


    }

    public void login(View view) {

        EditText username = (EditText) findViewById(R.id.txtloginusername);
        EditText password = (EditText) findViewById(R.id.txtloginpassword);

        String pusername = username.getText().toString().trim();
        final String pass1 = password.getText().toString().trim();

        if (pusername.isEmpty()) {
            username.setError("Username cannot be empty");
            return;
        }
        if (pass1.isEmpty()) {
            password.setError("Password Cannot be Empty");
            return;
        }

        if (!isOnline(this)) {

            Toast.makeText(getApplicationContext(), "Connection to Server Failed !.Check Internet Connection", Toast.LENGTH_LONG).show();
        }
        if (!pusername.isEmpty() && !pass1.isEmpty()) {

            pushData();
            //mEditor.putString(getString(R.string.pusername), pusername);
           // mEditor.commit();

        }


    }
    public void enter(){
        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);
    }


    public void register(View view) {
        Intent reg = new Intent(this, SignUpActivity.class);
        startActivity(reg);
    }
}
