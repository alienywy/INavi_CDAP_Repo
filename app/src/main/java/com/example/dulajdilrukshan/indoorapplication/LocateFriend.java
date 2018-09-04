package com.example.dulajdilrukshan.indoorapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LocateFriend extends AppCompatActivity {

    String myName;
    RequestQueue queue;
    StringRequest getRequest;
    Handler handler;
    ProgressDialog nDialog;
    int reqId;

    Globals sharedData = Globals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_friend);

        queue = Volley.newRequestQueue(this);

    }
    public void test(View view) {


        final TextView friend= (TextView)findViewById(R.id.txtfriendname);

        //SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //String myName = mPreferences.getString(getString(R.string.pusername), "");

        myName = sharedData.getValue();
        final String friendName = friend.getText().toString();


        try {
            String url = "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/sendLocationRequest/"+myName+"/"+friendName;



            getRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            reqId = Integer.parseInt(response);

                            nDialog = new ProgressDialog(LocateFriend.this);
                            nDialog.setMessage("Requesting for "+friendName+"'s location");
                            nDialog.setTitle("Location Request");
                            nDialog.setIndeterminate(false);
                            nDialog.setCancelable(true);
                            nDialog.show();

                            handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    nDialog.dismiss();
                                    getRequest = new StringRequest(Request.Method.GET,
                                            "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/isLocationRequestAccepted/"+reqId,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    try {
                                                        JSONObject locations = new JSONObject(response);

                                                        float x = (float)locations.getDouble("x");
                                                        float y = (float)locations.getDouble("y");

                                                        showFriendLocation(imagepaths(),x*25,y*25);


                                                    } catch (Exception e) {
                                                        AlertDialog alertDialog = new AlertDialog.Builder(LocateFriend.this).create();
                                                        alertDialog.setTitle("Request Denied");
                                                        alertDialog.setMessage(friendName+" is either offline or Location is not shared");
                                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                        startActivity(new Intent(LocateFriend.this,HomeActivity.class));
                                                                    }
                                                                });
                                                        alertDialog.show();

                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    AlertDialog alertDialog = new AlertDialog.Builder(LocateFriend.this).create();
                                                    alertDialog.setTitle("Request Denied");
                                                    alertDialog.setMessage("Current Location of "+friendName+" is not shared");
                                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                    startActivity(new Intent(LocateFriend.this,HomeActivity.class));
                                                                }
                                                            });
                                                    alertDialog.show();
                                                }
                                            });

                                    queue.add(getRequest);
                                }
                            }, 10000);



                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"2"+error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });


            // add it to the RequestQueue
            queue.add(getRequest);

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }

    public Canvas imagepaths() {

        ImageView FriendLocationMap = (ImageView) this.findViewById(R.id.FriendLocationMap);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;


        Bitmap bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        FriendLocationMap.setImageBitmap(bitmap);
        return canvas;
    }

    public void showFriendLocation(Canvas canvas,float x,float y)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        paint.setShadowLayer(15, 0, 0, Color.BLUE);

        canvas.drawCircle(x, y, 10, paint);
    }

    public void testMethod(View view){
        showFriendLocation(imagepaths(),100,500);
    }
}

