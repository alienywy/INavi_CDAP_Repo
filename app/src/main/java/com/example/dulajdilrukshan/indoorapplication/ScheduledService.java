package com.example.dulajdilrukshan.indoorapplication;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Oshan Fernando on 7/30/2018.
 */

public class ScheduledService extends Service {

    Globals sharedData = Globals.getInstance();

    String username = sharedData.getValue();

    StringRequest getRequest;

    boolean isAlertShown;

    double exp1, exp2, exp3, distance1, distance2, distance3, rssi1, rssi2, rssi3;
    String bssid1, bssid2, bssid3;

    double val = 1.8;



    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        handler.post(getResponceAfterInterval);

        isAlertShown  = false;

    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        handler.removeCallbacks(getResponceAfterInterval);
    }


    private final Handler handler = new Handler();
    private Runnable getResponceAfterInterval = new Runnable() {

        public void run() {

            final RequestQueue queue = Volley.newRequestQueue(ScheduledService.this);

             try {
                String url = "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/isLocationRequested/"+username;

                getRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                final int id;

                                try {

                                    JSONArray jsonArr = new JSONArray(response);

                                     id = Integer.parseInt((String)jsonArr.get(0));
                                    String sender =  (String)jsonArr.get(1);


                                    AlertDialog.Builder builder = new AlertDialog.Builder(ScheduledService.this);

                                    builder.setTitle("Location Request");
                                    builder.setMessage("Your friend "+sender+ " is requesting your location, would you like to allow ?");

                                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                                            Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
                                                @Override
                                                public int compare(ScanResult lhs, ScanResult rhs) {
                                                    return (lhs.level < rhs.level ? 1 : (lhs.level == rhs.level ? 0 : -1));
                                                }
                                            };

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
                                                        while (i < results.size())
                                                        {
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


                                                        String url = "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/updateLocationRequest";

                                                        getRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {

                                                                try {

                                                                    if(response.equals("1")){
                                                                        Toast.makeText(ScheduledService.this,"Successfully updated",Toast.LENGTH_LONG).show();
                                                                    }

                                                                } catch (Exception e) {
                                                                    //Toast.makeText(ScheduledService.this,e.getMessage(),Toast.LENGTH_LONG).show();

                                                                }

                                                            }
                                                        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {

                                                            }
                                                        }) {
                                                            protected Map<String, String> getParams() {
                                                                Map<String, String> FormData = new HashMap<String, String>();

                                                                FormData.put("bssid1", bssid1);
                                                                FormData.put("bssid2", bssid2);
                                                                FormData.put("bssid3", bssid3);
                                                                FormData.put("r1", distance1 + "");
                                                                FormData.put("r2", distance2 + "");
                                                                FormData.put("r3", distance3 + "");
                                                                FormData.put("isAllowed","allow");
                                                                FormData.put("reqId",""+id);

//
                                                                return FormData;
                                                            }
                                                        };

                                                        queue.add(getRequest);
                                                    } else {
                                                        Toast.makeText(ScheduledService.this, "Not Enough Access Points", Toast.LENGTH_SHORT).show();
                                                    }


                                                } catch (Exception e) {

                                                }

                                            }

                                        }

                                    });

                                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            getRequest = new StringRequest(Request.Method.POST,
                                                    "http://ec2-18-191-196-123.us-east-2.compute.amazonaws.com:8081/updateLocationRequest",
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {


                                                            try {


                                                                if(response.equals("1")){
                                                                    Toast.makeText(ScheduledService.this,"Successfully updated",Toast.LENGTH_LONG).show();
                                                                }

                                                            } catch (Exception e) {
                                                                Toast.makeText(ScheduledService.this,e.getMessage(),Toast.LENGTH_LONG).show();

                                                            }

                                                        }
                                                    }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(ScheduledService.this,error.getMessage(),Toast.LENGTH_LONG).show();

                                                }
                                            }) {
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> FormData = new HashMap<String, String>();


                                                    FormData.put("isAllowed","reject");
                                                    FormData.put("reqId",""+id);

//
                                                    return FormData;
                                                }
                                            };

                                            queue.add(getRequest);
                                        }
                                    });

                                    AlertDialog alert = builder.create();
                                    alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

                                    if(!isAlertShown) {
                                        alert.show();
                                        isAlertShown = true;
                                    }
                                    //handler.removeCallbacks(getResponceAfterInterval);

                                    //Code Block to Show Confirmation dialog box with positive button
                                    Toast.makeText(ScheduledService.this, id + " "+sender+" Wishes to know your location",Toast.LENGTH_LONG).show();



                                }
                                catch (JSONException e) {
                                    //Toast.makeText(ScheduledService.this,"Exception "+ e.getMessage(),Toast.LENGTH_LONG).show();


                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ScheduledService.this,error.toString(),Toast.LENGTH_LONG).show();
                            }
                        });


                // add it to the RequestQueue
                queue.add(getRequest);

            }
            catch (Exception e){
                Toast.makeText(ScheduledService.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }

            handler.postDelayed(this, 5000);

        }
    };


//    <service android:name=".ScheduledService" android:enabled="true"/>
//    Intent i= new Intent(context, ScheduledService.class);
//    context.startService(i);
}

