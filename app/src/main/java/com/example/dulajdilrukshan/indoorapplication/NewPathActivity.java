package com.example.dulajdilrukshan.indoorapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class NewPathActivity extends AppCompatActivity  implements View.OnClickListener {

    ImageView drawingImageView;
    Button bt;

    //Variables for linLayTop-----------------------------------------------------------------------------------------------------------
    EditText fromtxt;
    EditText totxt;

    //For Request Permission------------------------------------------------------------------------------------------------------------
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final String TAG = "MainActivity";

    //For Rating------------------------------------------------------------------------------------------------------------------------
    ImageButton shop1;
    ImageButton shop2;
    ImageButton shop3;
    TextView viewRate;

    Dialog MyDialog;
    Button rate, close;
    private PopupWindow mypopup;
    private ConstraintLayout popupconstraint;
    Double rating;

    //for voice-------------------------------------------------------------------------------------------------------------------------
    TextToSpeech toSpeech;
    EditText editText;
    Button btnspeak;
    String text;
    int result;

    //For RSS localization---------------------------------------------------------------------------------------------------------------
    com.example.dulajdilrukshan.indoorapplication.Paths paths = new com.example.dulajdilrukshan.indoorapplication.Paths();

    double d;
    double temp;
    double txpower = 32;
    double val = 1.8;

    RequestQueue MyRequestQueue;
    private Timer mytimer;
    WifiManager wifiManager;

    float x, y;
    double exp1, exp2, exp3, distance1, distance2, distance3, rssi1, rssi2, rssi3;
    String bssid1, bssid2, bssid3;
    StringRequest MyStringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_path);
//-----------------------test--------------------------------
        bt = findViewById(R.id.btnCanvas);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawRect(drawCanvas());
            }
        });
//-----------------------/test--------------------------------



        checkAndRequestPermissions();

        MyRequestQueue = Volley.newRequestQueue(this);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mytimer = new Timer();
        mytimer.schedule(new TimerTask(){
            @Override
            public void run() {
                TimerMethod();
            }
        }, 0, 2000);


        //Shop Rating----------------------------------------------------------------------------------------------------------------------
        shop1 = (ImageButton) findViewById(R.id.shop1);
        shop2 = (ImageButton) findViewById(R.id.shop2);
        shop3 = (ImageButton) findViewById(R.id.shop3);


        shop1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                getRating("taco%20bell");
            }
        });

        shop2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                getRating("coffee%20bean");


            }
        });
        shop3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                getRating("tea%20avenue");
            }
        });


        //For voice-------------------------------------------------------------------------------------------------------------------------

        editText = (EditText) findViewById(R.id.txtvoice);
        btnspeak = (Button) findViewById(R.id.btnvoice);

        btnspeak.setOnClickListener(this);
        toSpeech = new TextToSpeech(NewPathActivity.this, new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    result = toSpeech.setLanguage(Locale.US);
                } else {
                    Toast.makeText(getApplicationContext(), "Feature not supported in your device", Toast.LENGTH_SHORT).show();
                }
            }
        });


        String url = "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/gettemp";

        MyStringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        double temperature = Double.parseDouble(response);

                            /*
                           RSSI loss [ dBm ] = 0.1996 × ( T [°C]  – 25[°C])
                           where T is the temperature in the range of  25 °C ≤  T ≤ 65 °C

                             */
                        txpower = txpower + (0.1996 * (temperature - 25));

                        Toast.makeText(getApplicationContext(), temperature + "", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });




    }

    public Canvas drawCanvas() {

        drawingImageView = this.findViewById(R.id.floorPlan);
        int screenWidth,screenHeight;

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int screenWidth = displayMetrics.widthPixels;
//        int screenHeight = displayMetrics.heightPixels;

        LinearLayout layout = this.findViewById(R.id.linLayBot);

        screenHeight = layout.getHeight();
        screenWidth = layout.getWidth();


        Bitmap bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawingImageView.setImageBitmap(bitmap);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        return canvas;
    }

    public void drawRect(Canvas c){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

        c.drawRect(0,0,c.getWidth(),c.getHeight(),paint);
    }

    //Request Access Permissions Device Location----------------------------------------------------------------------------------------------------

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showDialogOK("Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    //Methods in linLayTop-------------------------------------------------------------------------------------------------------------------------
    public void search(View view) {
        totxt = (EditText) findViewById(R.id.direction_header_to_text);
        fromtxt = (EditText) findViewById(R.id.direction_header_from_text);
        Intent intent = new Intent(this, PathActivity.class);
        // intent.putExtra("from",fromtxt.getText().toString());

        intent.putExtra("to", totxt.getText().toString());
        intent.putExtra("from", fromtxt.getText().toString());
        startActivity(intent);
        finish();
    }

    public  void  goback(View view){
        Intent goback = new Intent(this,HomeActivity.class);
        startActivity(goback);
    }

    //Voice implementation--------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toSpeech != null) {
            toSpeech.stop();
            toSpeech.shutdown();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnvoice:
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(getApplicationContext(), "Feature not supported in your device", Toast.LENGTH_SHORT).show();
                } else {
                    text = editText.getText().toString();
                    toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
                break;
//            case R.id.bstop:
//                if (toSpeech != null) {
//                    toSpeech.stop();
//                }
//                break;
        }
    }

    //RSS localization methods-------------------------------------------------------------------------------------------------------------------------
    Comparator<ScanResult> comparator = new Comparator<ScanResult>(){
        @Override
        public int compare(ScanResult lhs, ScanResult rhs) {
            return (lhs.level < rhs.level ? 1 : (lhs.level == rhs.level ? 0 : -1));
        }
    };


    private void TimerMethod() {
        this.runOnUiThread(measureRSSI);
    }

    private Runnable measureRSSI = new Runnable(){

        public void run() {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.startScan();
                List<ScanResult> results = wifiManager.getScanResults();
                try {
                    // Sorting the results list
                    Collections.sort(results, comparator);
                    int i = 0;
                    if (results.size() >= 3) {
                        // distance = 10 ^ ((27.55 - (20 * log10(frequency)) + signalLevel)/20)
                        // AP 1
                        while (i < results.size()) {
                            // AP 1
                            if (results.get(i).BSSID.equalsIgnoreCase("80:3f:5d:36:98:e5")) {
                                rssi1 = results.get(i).level;
                                bssid1 = results.get(i).BSSID;
                                exp1 = (27.55 - (20 * Math.log10(2412)) + Math.abs(rssi1)) / (10 * val);
                                distance1 = Math.pow(10.0, exp1);
                            } else if (results.get(i).BSSID.equalsIgnoreCase("98:e7:f5:d1:0c:28")) {
                                // AP 2
                                rssi2 = results.get(i).level;
                                bssid2 = results.get(i).BSSID;
                                exp2 = (27.55 - (20 * Math.log10(2412)) + Math.abs(rssi2)) / (10 * val);
                                distance2 = Math.pow(10.0, exp2);
                            } else if (results.get(i).BSSID.equalsIgnoreCase("80:3f:5d:36:98:b8")) {
                                // AP 3
                                rssi3 = results.get(i).level;
                                bssid3 = results.get(i).BSSID;
                                exp3 = (27.55 - (20 * Math.log10(2412)) + Math.abs(rssi3)) / (10 * val);
                                distance3 = Math.pow(10.0, exp3);
                            }
                            // Traversing to the next element
                            i++;
                        }

                        String url = "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/getlocation";

                        MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    // Convert meters into pixels
                                    x = ((float) jsonObject.getDouble("x")) * 25;
                                    y = ((float) jsonObject.getDouble("y")) * 25;
                                    showLocation(x, y);
                                    Toast.makeText(NewPathActivity.this, jsonObject.getDouble("x")
                                            + " " + jsonObject.getDouble("y"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener(){ //Create an error listener to handle errors appropriately.
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }){
                            protected Map<String, String> getParams() {
                                Map<String, String> FormData = new HashMap<String, String>();
                                FormData.put("bssid1", bssid1);
                                FormData.put("bssid2", bssid2);
                                FormData.put("bssid3", bssid3);
                                FormData.put("r1", distance1 + "");
                                FormData.put("r2", distance2 + "");
                                FormData.put("r3", distance3 + "");//
                                return FormData;
                            }
                        };
                        MyRequestQueue.add(MyStringRequest);
                    } else {
                        Toast.makeText(NewPathActivity.this, "Not Enough Access Points", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    String err = e.getMessage();
                }
            }

        }
    };

    public void showLocation(float x, float y) {
        paths.showLocation(drawCanvas(), x, y);
    }

    //Shop Rating-------------------------------------------------------------------------------------------------------------------------------------------
    public void getRating(final String shopName) {


        final String setRatingUrl = "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/getrating/" + shopName;

        MyStringRequest = new StringRequest(Request.Method.GET, setRatingUrl, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {

                    rating = Double.parseDouble(response);

                    if (shopName == "taco%20bell") {

                        tacobellDialog(rating);
                    }
                    if (shopName == "coffee%20bean") {

                        coffeeBeanDialog(rating);
                    }
                    if (shopName == "tea%20avenue") {

                        teaAveneueDialog(rating);
                    }
                    if (shopName.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "No shop Ratings", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    String err = e.getMessage();
                    Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){


        };
        MyRequestQueue.add(MyStringRequest);

    }


    public void tacobellDialog(double rating) {
        MyDialog = new Dialog(NewPathActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.shop);
        MyDialog.setTitle("My Custom Dialog");

        viewRate = MyDialog.findViewById(R.id.viewRate);
        viewRate.setText(String.valueOf(rating));


        rate = (Button) MyDialog.findViewById(R.id.hello);
        close = (Button) MyDialog.findViewById(R.id.close);

        rate.setEnabled(true);
        close.setEnabled(true);


        rate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                String url = "taco%20bell";
                String name = "taco bell";
                Intent i = new Intent(NewPathActivity.this, ShopRate.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image", R.drawable.taco_bell);
                i.putExtras(bundle);
                i.putExtra("keyUrl", url);
                i.putExtra("keyName", name);


                startActivity(i);

            }
        });
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();
    }

    public void coffeeBeanDialog(double rating) {
        MyDialog = new Dialog(NewPathActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.shop);
        MyDialog.setTitle("My Custom Dialog");

        viewRate = MyDialog.findViewById(R.id.viewRate);
        viewRate.setText(String.valueOf(rating));

        rate = (Button) MyDialog.findViewById(R.id.hello);
        close = (Button) MyDialog.findViewById(R.id.close);

        rate.setEnabled(true);
        close.setEnabled(true);


        rate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                String url = "coffee%20bean";
                String name = "coffee bean";
                Intent i = new Intent(NewPathActivity.this, ShopRate.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image", R.drawable.coffee_bean_shop);
                i.putExtras(bundle);
                i.putExtra("keyUrl", url);
                i.putExtra("keyName", name);

                startActivity(i);

            }
        });
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();
    }

    public void teaAveneueDialog(double rating) {
        MyDialog = new Dialog(NewPathActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.shop);
        MyDialog.setTitle("My Custom Dialog");

        viewRate = MyDialog.findViewById(R.id.viewRate);
        viewRate.setText(String.valueOf(rating));

        rate = (Button) MyDialog.findViewById(R.id.hello);
        close = (Button) MyDialog.findViewById(R.id.close);

        rate.setEnabled(true);
        close.setEnabled(true);


        rate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String url = "tea%20avenue";
                String name = "tea avenue";
                Intent i = new Intent(NewPathActivity.this, ShopRate.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image", R.drawable.tea_avenue_shop);
                i.putExtras(bundle);
                i.putExtra("keyUrl", url);
                i.putExtra("keyName", name);

                startActivity(i);

            }
        });
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();
    }
}

