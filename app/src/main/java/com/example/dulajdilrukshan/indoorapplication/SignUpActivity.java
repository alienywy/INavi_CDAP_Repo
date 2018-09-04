package com.example.dulajdilrukshan.indoorapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SignUpActivity extends AppCompatActivity {


    StringRequest MyStringRequest;
    RequestQueue MyRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        MyRequestQueue = Volley.newRequestQueue(this);

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
        final EditText name = (EditText) findViewById(R.id.txtname);
        final EditText username = (EditText) findViewById(R.id.txtsingupusername);
        final EditText password1 = (EditText) findViewById(R.id.txtpassword1);
        final EditText emailValidate = (EditText) findViewById(R.id.txtsignupemail);


        final String email = emailValidate.getText().toString().trim();
        final String pname = name.getText().toString();
        final String puername = username.getText().toString();
        final String pass1 = password1.getText().toString().trim();

        final String url = "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/insertuser";


        MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                 Server Sends a Respornse as "1" if data is Success

                    if (response.equals("1")) {


                        Toast.makeText(getApplicationContext(), "Registration Completed", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getApplicationContext(), "Failed To Register", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    String err = e.getMessage();
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

                FormData.put("name", pname);
                FormData.put("username", puername);
                FormData.put("email", email);
                FormData.put("password", pass1);

                return FormData;

            }
        };
        MyRequestQueue.add(MyStringRequest);

    }

    // password to MD5
//    public static String getMd5Hash(String input) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] messageDigest = md.digest(input.getBytes());
//            BigInteger number = new BigInteger(1, messageDigest);
//            String md5 = number.toString(16);
//
//            while (md5.length() < 32)
//                md5 = "0" + md5;
//
//            return md5;
//        } catch (NoSuchAlgorithmException e) {
////            Log.e("MD5", e.getLocalizedMessage());
//            return null;
//        }
//    }


    public void register(View view) {


        final EditText name = (EditText) findViewById(R.id.txtname);
        final EditText username = (EditText) findViewById(R.id.txtsingupusername);
        final EditText password1 = (EditText) findViewById(R.id.txtpassword1);
        final EditText password2 = (EditText) findViewById(R.id.txtpassword2);
        final EditText emailValidate = (EditText) findViewById(R.id.txtsignupemail);


        String email = emailValidate.getText().toString().trim();
        String pname = name.getText().toString();
        String puername = username.getText().toString();
        String pass1 = password1.getText().toString().trim();
        String pass2 = password2.getText().toString().trim();
        if (pname.isEmpty()) {
            name.setError("Name Cannot be Empty");
        }
        if (puername.isEmpty()) {
            username.setError("Username Cannot be Empty");
        }
        if (email.isEmpty()) {
            emailValidate.setError("Email Cannot be Empty");

        } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            emailValidate.setError("Invalid Email Address");
        }
        if (pass1.isEmpty()) {
            password1.setError("Password Cannot be Empty");
        }
        if (pass1.length() < 4) {
            password1.setError("Password Must be 4 characters");
        }
        if (!pass2.equals(pass1)) {
            password2.setError("Passwords do not Match");
        }
        if (!isOnline(this)) {

            Toast.makeText(getApplicationContext(), "Connection to Server Failed !.Check Internet Connection", Toast.LENGTH_LONG).show();
        }
        if (pass1.equals(pass2) && !email.isEmpty() && !pname.isEmpty()
                && !puername.isEmpty() && !pass1.isEmpty()
                && !pass2.isEmpty()) {
            try {
//                Push Data to Server

                pushData();

            } catch (Exception e) {
                String err = e.getMessage();
                Toast.makeText(getApplicationContext(), err, Toast.LENGTH_SHORT).show();

            }


            name.setText("");
            username.setText("");
            password1.setText("");
            password2.setText("");
            emailValidate.setText("");

            Intent signUp = new Intent(this, LoginActivity.class);

//          Sending data for other activities. You can call pname by using the key "isLogged"
            signUp.putExtra("isLogged", pname);

            startActivity(signUp);
        }


    }


}
