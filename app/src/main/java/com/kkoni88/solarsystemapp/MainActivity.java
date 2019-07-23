package com.kkoni88.solarsystemapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView statusTV = (TextView) findViewById(R.id.status);
        String apiKey = getResources().getString(R.string.solar_api_key);
        String siteDetailsUrl = String.format("https://monitoringapi.solaredge.com/site/1204115/details?api_key=%s", apiKey);



        Runnable runnable = () -> {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, siteDetailsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            statusTV.setText("Response is: " + response.substring(0, 500));
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    statusTV.setText("That didn't work!");
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        };

        runnable.run();

    }
}
