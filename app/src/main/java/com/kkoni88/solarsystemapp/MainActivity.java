package com.kkoni88.solarsystemapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new DailyWhAT().execute();
    }


    private class DailyWhAT extends AsyncTask<String, Void, String> {
        String result = "";
        CountDownLatch countDownLatch = new CountDownLatch(1);

        @Override
        protected String doInBackground(String... strings) {

//            LocalDateTime localDateTime = LocalDateTime.now().minusDays(7);
//            Date weekBefore = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());
            Date date = new Date();

            String url = new HttpUrl.Builder()
                    .scheme(getResources().getString(R.string.solar_monitoring_api_scheme))
                    .host(getResources().getString(R.string.solar_monitoring_api_host))
                    .addPathSegments(getResources().getString(R.string.solar_monitoring_api_path_site))
                    .addPathSegment("energyDetails")
                    .addQueryParameter("api_key", getResources().getString(R.string.solar_api_key))
                    .addQueryParameter("startTime", SolarHelper.getDateAndTime(date, "00:00"))
                    .addQueryParameter("endTime", SolarHelper.getDateAndTime(date, "23:59"))
                    .addQueryParameter("timeUnit", "DAY")
                    .build().toString();


            RequestQueue queue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    (response) -> {
                        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                        String generatedPower =
                                jsonObject.get("energyDetails").getAsJsonObject()
                                        .get("meters").getAsJsonArray()
                                        .get(0).getAsJsonObject()
                                        .get("values").getAsJsonArray()
                                        .get(0).getAsJsonObject()
                                        .get("value").getAsString();
                        result = generatedPower;
                        countDownLatch.countDown();

                    }, (error) -> {
                result = error.getLocalizedMessage();
                countDownLatch.countDown();

            });
            queue.add(stringRequest);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            final TextView statusTV = findViewById(R.id.status);
            statusTV.setText(String.format("A ma megtermelt áram: %s Wh", result));
        }
    }

}
