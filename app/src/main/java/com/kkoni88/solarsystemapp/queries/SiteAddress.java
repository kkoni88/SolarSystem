package com.kkoni88.solarsystemapp.queries;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SiteAddress extends AbstractQueryAsyncTask {

    QueryResultCallback queryResultCallback;

    @Override
    protected Void doInBackground(Void... voids) {

        String url = urlBuilder
                .addPathSegment("details")
                .build().toString();

        RequestQueue queue = Volley.newRequestQueue(activity);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                (response) -> {
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

                    String country = jsonObject.get("details").getAsJsonObject()
                            .get("location").getAsJsonObject().get("country").getAsString();
                    String address = jsonObject.get("details").getAsJsonObject()
                            .get("location").getAsJsonObject().get("address").getAsString();
                    String city = jsonObject.get("details").getAsJsonObject()
                            .get("location").getAsJsonObject().get("city").getAsString();
                    String zip = jsonObject.get("details").getAsJsonObject()
                            .get("location").getAsJsonObject().get("zip").getAsString();
                    queryResult = String.format("%s %s %s %s", country, city, zip, address);
                    countDownLatch.countDown();

                }, (error) -> {
            queryResult = error.getLocalizedMessage();
            countDownLatch.countDown();

        });
        queue.add(stringRequest);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        queryResultCallback.onResultReceived(queryResult);
    }


    public SiteAddress(Activity activity, QueryResultCallback queryResultCallback) {
        super(activity);
        this.queryResultCallback = queryResultCallback;
    }
}
