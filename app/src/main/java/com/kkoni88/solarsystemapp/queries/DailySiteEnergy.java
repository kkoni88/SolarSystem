package com.kkoni88.solarsystemapp.queries;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kkoni88.solarsystemapp.R;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import okhttp3.HttpUrl;

public class DailySiteEnergy extends AsyncTask<String, Void, String> {
    private Activity activity;

    String result = "";
    CountDownLatch countDownLatch = new CountDownLatch(1);

    public DailySiteEnergy(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {

//            LocalDateTime localDateTime = LocalDateTime.now().minusDays(7);
//            Date weekBefore = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());
        Date date = new Date();

        String url = new HttpUrl.Builder()
                .scheme(activity.getResources().getString(R.string.solar_monitoring_api_scheme))
                .host(activity.getResources().getString(R.string.solar_monitoring_api_host))
                .addPathSegments(activity.getResources().getString(R.string.solar_monitoring_api_path_site))
                .addPathSegment("energyDetails")
                .addQueryParameter("api_key", activity.getResources().getString(R.string.solar_api_key))
                .addQueryParameter("startTime", SolarHelper.getDateAndTime(date, "00:00"))
                .addQueryParameter("endTime", SolarHelper.getDateAndTime(date, "23:59"))
                .addQueryParameter("timeUnit", "DAY")
                .build().toString();


        RequestQueue queue = Volley.newRequestQueue(activity);

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

        final TextView statusTV = activity.findViewById(R.id.status);
        statusTV.setText(String.format("A ma megtermelt áram: %s Wh", result));
    }
}
