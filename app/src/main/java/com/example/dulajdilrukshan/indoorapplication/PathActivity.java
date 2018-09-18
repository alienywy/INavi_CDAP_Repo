package com.example.dulajdilrukshan.indoorapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class PathActivity extends AppCompatActivity{
    ImageView drawingImageView, creatpath, floorplan;
    TextView error;
    Button navi;
    int index = 0;

    com.example.dulajdilrukshan.indoorapplication.Paths paths = new com.example.dulajdilrukshan.indoorapplication.Paths();
    public float locationarrays[][], connectionarray[][];
    private Timer mytimer;
    WifiManager wifiManager;
    AutoCompleteTextView autoCompleteTextViewfrom;
    AutoCompleteTextView autoCompleteTextViewto;
    LinearLayout Auditorium, Multimedia, MSCRoom, LectureHall1, Library, DCCNLab, LiftLobby, CommonRoom, WashRooms, StaffRoom;
    LinearLayout Con1, Con2, Con4, Main, Con5, Con6, Con7, Con8, Con9, Con10;

    double d;
    double temp;
    double txpower = 32;
    double val = 1.8;


    RequestQueue MyRequestQueue;

    float x, y;
    double exp1, exp2, exp3, distance1, distance2, distance3, rssi1, rssi2, rssi3;
    String bssid1, bssid2, bssid3;
    StringRequest MyStringRequest;


    //For Request Permission
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final String TAG = "MainActivity";


    ImageButton shop1;
    ImageButton shop2;
    ImageButton shop3;
    TextView viewRate;

    Dialog MyDialog;
    Button rate, close;
    private PopupWindow mypopup;
    private ConstraintLayout popupconstraint;
    Double rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        com.example.dulajdilrukshan.indoorapplication.CreatePaths createPaths = new com.example.dulajdilrukshan.indoorapplication.CreatePaths();
        Auditorium = findViewById(R.id.auditorium);
        Multimedia = findViewById(R.id.multimedia);
        MSCRoom = findViewById(R.id.mscroom);
        LectureHall1 = findViewById(R.id.hallone);
        Library = findViewById(R.id.library);
        DCCNLab = findViewById(R.id.dccn);
        LiftLobby = findViewById(R.id.lift);
        CommonRoom = findViewById(R.id.commonroom);
        WashRooms = findViewById(R.id.washroom);
        StaffRoom = findViewById(R.id.staffroom);
        Con2 = findViewById(R.id.con2);
        Con4 = findViewById(R.id.con4);
        Con5 = findViewById(R.id.con5);
        Main = findViewById(R.id.maincon);
        Con6 = findViewById(R.id.con6);
        Con7 = findViewById(R.id.con7);
        Con8 = findViewById(R.id.con8);
        Con9 = findViewById(R.id.con9);
        Con10 = findViewById(R.id.con10);
        Con1 = findViewById(R.id.con1);
        autoCompleteTextViewfrom = (AutoCompleteTextView) findViewById(R.id.direction_header_from_text);
        autoCompleteTextViewto = (AutoCompleteTextView) findViewById(R.id.direction_header_to_text);

        Resources resources = getResources();

        String[] locations = resources.getStringArray(R.array.locations_array);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, locations);

        autoCompleteTextViewfrom.setAdapter(adapter);
        autoCompleteTextViewto.setAdapter(adapter);
        navi = findViewById(R.id.button3);

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


        shop1 = (ImageButton) findViewById(R.id.shop1);
        shop2 = (ImageButton) findViewById(R.id.shop2);
        shop3 = (ImageButton) findViewById(R.id.shop3);
//        viewRate = (TextView) findViewById(R.id.viewrate);


        shop1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                getRating("taco%20bell");


                tacobellDialog();
            }
        });

        shop2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                coffeeBeanDialog();
            }
        });
        shop3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                teaAveneueDialog();
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

    // Overriding compare method to sort by rss level
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

                                    Toast.makeText(PathActivity.this, jsonObject.getDouble("x")
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
                                FormData.put("r3", distance3 + "");
//
                                return FormData;
                            }
                        };

                        MyRequestQueue.add(MyStringRequest);
                    } else {
                        Toast.makeText(PathActivity.this, "Not Enough Access Points", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    String err = e.getMessage();
                }

            }

        }
    };


    public Canvas showlocation() {

        drawingImageView = (ImageView) this.findViewById(R.id.DrawingImageView);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;


        Bitmap bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawingImageView.setImageBitmap(bitmap);
        return canvas;
    }

    public Canvas createpaths() {

        creatpath = (ImageView) this.findViewById(R.id.pathImageView);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;


        Bitmap bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        creatpath.setImageBitmap(bitmap);
        return canvas;
    }


    public void showLocation(float x, float y) {

        paths.showLocation(showlocation(), x, y);
    }


//Gowtham-ShowPaths


    public void navigation(View view) {

//      // createPaths.showpaths(createpaths(),"Auditorium","Multimedia");
        CreatePaths cp = new CreatePaths();
        EditText txtsub = (EditText) findViewById(R.id.txtval);
//        //int index=0;
//
        showpaths();
//        cp.createpath(autoCompleteTextViewfrom.getText().toString(),autoCompleteTextViewto.getText().toString());
        cp.createpath("Auditorium", "Library");
        cp.drawingpaths(createpaths(), locationarrays, connectionarray);

//        txtsub.setText(locationarrays[0][0]+" "+locationarrays[0][1]);
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);


//
    }


    public void submit(View view) {
        EditText text = findViewById(R.id.txtval);
        float density = getResources().getDisplayMetrics().density;
        text.setText("" + density);


    }

    public void showpaths() {
        EditText txtsub = (EditText) findViewById(R.id.txtval);
        CreatePaths cp = new CreatePaths();
        float auditoriumX, multimediaX, mscroomX, libraryX, lecthall1X, dccnX, liftX, commonX, staffX, washX,
                auditoriumY, multimediaY, mscroomY, libraryY, lecthall1Y, dccnY, liftY, commonY, staffY, washY;
        float con1X, con2X, con4X, con5X, con6X, con7X, con8X, con9X, con10X, mainX,
                con1Y, con2Y, con4Y, con5Y, con6Y, con7Y, con8Y, con9Y, con10Y, mainY;
        int auditoriumlocationarray[] = new int[2];
        int multimedialocationarray[] = new int[2];
        int mscroomlocationarray[] = new int[2];
        int librarylocationarray[] = new int[2];
        int lecturehall1locationarray[] = new int[2];
        int dccnlablocationarray[] = new int[2];
        int liftlocationarray[] = new int[2];
        int commonroomlocationarray[] = new int[2];
        int staffroomlocationarray[] = new int[2];
        int washroomlocationarray[] = new int[2];

        int con1array[] = new int[2];
        int con2array[] = new int[2];
        int con4array[] = new int[2];
        int con5array[] = new int[2];
        int con6array[] = new int[2];
        int con7array[] = new int[2];
        int con8array[] = new int[2];
        int con9array[] = new int[2];
        int con10array[] = new int[2];
        int conMainarray[] = new int[2];


        Auditorium.getLocationOnScreen(auditoriumlocationarray);
        Multimedia.getLocationOnScreen(multimedialocationarray);
        MSCRoom.getLocationOnScreen(mscroomlocationarray);
        Library.getLocationOnScreen(librarylocationarray);
        LectureHall1.getLocationOnScreen(lecturehall1locationarray);
        DCCNLab.getLocationOnScreen(dccnlablocationarray);
        LiftLobby.getLocationOnScreen(liftlocationarray);
        CommonRoom.getLocationOnScreen(commonroomlocationarray);
        StaffRoom.getLocationOnScreen(staffroomlocationarray);
        WashRooms.getLocationOnScreen(washroomlocationarray);

        Con1.getLocationOnScreen(con1array);
        Con2.getLocationOnScreen(con2array);
        Con4.getLocationOnScreen(con4array);
        Con5.getLocationOnScreen(con5array);
        Con6.getLocationOnScreen(con6array);
        Con7.getLocationOnScreen(con7array);
        Con8.getLocationOnScreen(con8array);
        Con9.getLocationOnScreen(con9array);
        Con10.getLocationOnScreen(con10array);
        Main.getLocationOnScreen(conMainarray);

        //X coordinates
        auditoriumX = auditoriumlocationarray[0];
        multimediaX = multimedialocationarray[0];
        mscroomX = mscroomlocationarray[0];
        libraryX = librarylocationarray[0];
        lecthall1X = lecturehall1locationarray[0];
        dccnX = dccnlablocationarray[0];
        liftX = liftlocationarray[0];
        commonX = commonroomlocationarray[0] + 50;
        staffX = staffroomlocationarray[0] + 50;
        washX = washroomlocationarray[0] + 50;
        con1X = con1array[0];
        con2X = con2array[0];
        con4X = con4array[0];
        con5X = con5array[0];
        con6X = con6array[0] + 50;
        con7X = con7array[0] + 50;
        con8X = con8array[0] + 50;
        con9X = con9array[0] + 50;
        con10X = con10array[0] + 50;
        mainX = conMainarray[0];


        //Y Coordinates
        auditoriumY = auditoriumlocationarray[1] - 65;
        multimediaY = multimedialocationarray[1] - 50;
        mscroomY = mscroomlocationarray[1] - 50;
        libraryY = librarylocationarray[1] - 65;
        lecthall1Y = lecturehall1locationarray[1] - 50;
        dccnY = dccnlablocationarray[1] - 60;
        liftY = liftlocationarray[1] - 50;
        commonY = commonroomlocationarray[1] - 50;
        staffY = staffroomlocationarray[1];
        washY = washroomlocationarray[1] - 50;

        con1Y = con1array[1] - 50;
        con2Y = con2array[1] - 50;
        con4Y = con4array[1] - 65;
        con5Y = con5array[1] - 50;
        con6Y = con6array[1] - 50;
        con7Y = con7array[1] - 50;
        con8Y = con8array[1];
        con9Y = con9array[1] - 50;
        con10Y = con10array[1] - 50;
        mainY = conMainarray[1] - 50;


        locationarrays = new float[][]
                {
                        {auditoriumX, auditoriumY}, {multimediaX, multimediaY}, {mscroomX, mscroomY},
                        {libraryX, libraryY}, {lecthall1X, lecthall1Y}, {dccnX, dccnY}, {liftX, liftY},
                        {commonX, commonY}, {staffX, staffY}, {washX, washY}

                };

        connectionarray = new float[][]
                {
                        {con1X, con1Y}, {con2X, con2Y}, {con4X, con4Y}, {con5X, con5Y},
                        {con6X, con6Y}, {con7X, con7Y}, {con8X, con8Y}, {con9X, con9Y},
                        {con10X, con10Y}, {mainX, mainY}
                };


    }


    // SamWickramarachchi - Request Access Permissions Device Location

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





    public void getRating(final String shopName) {

        viewRate = (TextView) findViewById(R.id.viewrate);

        final String setRatingUrl = "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/getrating/" + shopName;

        MyStringRequest = new StringRequest(Request.Method.GET, setRatingUrl, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {

                    rating = Double.parseDouble(response);


                    Toast.makeText(getApplicationContext(), " rating "+shopName, Toast.LENGTH_LONG).show();


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


    public void tacobellDialog() {
        MyDialog = new Dialog(PathActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.shop);
        MyDialog.setTitle("My Custom Dialog");



        rate = (Button) MyDialog.findViewById(R.id.hello);
        close = (Button) MyDialog.findViewById(R.id.close);

        rate.setEnabled(true);
        close.setEnabled(true);

//        Toast.makeText(getApplicationContext(),url, Toast.LENGTH_LONG).show();

        rate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                String url = "taco%20bell";
                String name = "taco bell";
                Intent i = new Intent(PathActivity.this, ShopRate.class);
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

    public void coffeeBeanDialog() {
        MyDialog = new Dialog(PathActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.shop);
        MyDialog.setTitle("My Custom Dialog");

        rate = (Button) MyDialog.findViewById(R.id.hello);
        close = (Button) MyDialog.findViewById(R.id.close);

        rate.setEnabled(true);
        close.setEnabled(true);


        rate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                String url = "coffee%20bean";
                String name = "coffee bean";
                Intent i = new Intent(PathActivity.this, ShopRate.class);
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

    public void teaAveneueDialog() {
        MyDialog = new Dialog(PathActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.shop);
        MyDialog.setTitle("My Custom Dialog");

        rate = (Button) MyDialog.findViewById(R.id.hello);
        close = (Button) MyDialog.findViewById(R.id.close);

        rate.setEnabled(true);
        close.setEnabled(true);


        rate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                String url = "tea%20avenue";
                String name = "tea avenue";
                Intent i = new Intent(PathActivity.this, ShopRate.class);
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

