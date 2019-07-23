package com.kkoni88.solarsystemapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView statusTV = findViewById(R.id.status);

        String url = new HttpUrl.Builder()
                .scheme(getResources().getString(R.string.solar_monitoring_api_scheme))
                .host(getResources().getString(R.string.solar_monitoring_api_host))
                .addPathSegments(getResources().getString(R.string.solar_monitoring_api_path_site))
                .addPathSegment("energyDetails")
                .addQueryParameter("api_key", getResources().getString(R.string.solar_api_key))
                .addQueryParameter("startTime", "2019-07-23 00:00:00")
                .addQueryParameter("endTime", "2019-07-24 00:00:00")
                .build().toString();



        Runnable runnable = () -> {
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    (response) -> {
                        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                        String generatedPower =
                                jsonObject.get("energyDetails").getAsJsonObject().get("meters").getAsJsonArray().get(0).getAsJsonObject().get("values").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();

                        statusTV.setText(String.format("A mai megtermelt áram: %s Wh", generatedPower));

                    }, (error) -> {
                    statusTV.setText("That didn't work!");

            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        };

        runnable.run();

    }
}
