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

import java.util.concurrent.CountDownLatch;

import okhttp3.HttpUrl;

public class SiteEnvironmentalBenefits extends AsyncTask<Void, Void, Void> {
    private Activity activity;

    String result = "";
    CountDownLatch countDownLatch = new CountDownLatch(1);

    String co2Saved;
    String so2Saved;
    String noxSaved;
    String treesPlanted;
    String lightBulbs;

    public SiteEnvironmentalBenefits(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        String url = new HttpUrl.Builder()
                .scheme(activity.getResources().getString(R.string.solar_monitoring_api_scheme))
                .host(activity.getResources().getString(R.string.solar_monitoring_api_host))
                .addPathSegments(activity.getResources().getString(R.string.solar_monitoring_api_path_site))
                .addPathSegment("envBenefits")
                .addQueryParameter("api_key", activity.getResources().getString(R.string.solar_api_key))
                .build().toString();


        RequestQueue queue = Volley.newRequestQueue(activity);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                (response) -> {
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

                    co2Saved = jsonObject.get("envBenefits").getAsJsonObject().get("gasEmissionSaved")
                            .getAsJsonObject().get("co2").getAsString();
                    so2Saved = jsonObject.get("envBenefits").getAsJsonObject().get("gasEmissionSaved")
                            .getAsJsonObject().get("so2").getAsString();
                    noxSaved = jsonObject.get("envBenefits").getAsJsonObject().get("gasEmissionSaved")
                            .getAsJsonObject().get("nox").getAsString();
                    treesPlanted = jsonObject.get("envBenefits").getAsJsonObject().get("treesPlanted")
                            .getAsString();
                    lightBulbs = jsonObject.get("envBenefits").getAsJsonObject().get("lightBulbs")
                            .getAsString();

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
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        final TextView envBenefits = activity.findViewById(R.id.envBenefits);
        envBenefits.setText(String.format("Környezeti hatások %n %s kg CO2, %s kg SO2, %s kg NOx, %n " +
                "%s fa ültetve, %s égő energiája megspórolva", co2Saved, so2Saved, noxSaved, treesPlanted, lightBulbs));
    }
}
