package com.kkoni88.solarsystemapp.queries;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kkoni88.solarsystemapp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SiteEnvironmentalBenefits extends AbstractQueryAsyncTask {
    Float co2Saved;
    Float so2Saved;
    Float noxSaved;
    Float treesPlanted;
    Float lightBulbs;

    public SiteEnvironmentalBenefits(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        String url = urlBuilder
                .addPathSegment("envBenefits")
                .build().toString();


        RequestQueue queue = Volley.newRequestQueue(activity);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                (response) -> {
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

                    co2Saved = Float.parseFloat(jsonObject.get("envBenefits").getAsJsonObject().get("gasEmissionSaved")
                            .getAsJsonObject().get("co2").getAsString());
                    so2Saved = Float.parseFloat(jsonObject.get("envBenefits").getAsJsonObject().get("gasEmissionSaved")
                            .getAsJsonObject().get("so2").getAsString());
                    noxSaved = Float.parseFloat(jsonObject.get("envBenefits").getAsJsonObject().get("gasEmissionSaved")
                            .getAsJsonObject().get("nox").getAsString());
                    treesPlanted = Float.parseFloat(jsonObject.get("envBenefits").getAsJsonObject().get("treesPlanted")
                            .getAsString());
                    lightBulbs = Float.parseFloat(jsonObject.get("envBenefits").getAsJsonObject().get("lightBulbs")
                            .getAsString());

                    BigDecimal bigDecimal = new BigDecimal(co2Saved).setScale(2, RoundingMode.HALF_UP);
                    co2Saved = bigDecimal.floatValue();

                    bigDecimal = new BigDecimal(so2Saved).setScale(2, RoundingMode.HALF_UP);
                    so2Saved = bigDecimal.floatValue();

                    bigDecimal = new BigDecimal(noxSaved).setScale(2, RoundingMode.HALF_UP);
                    noxSaved = bigDecimal.floatValue();

                    bigDecimal = new BigDecimal(treesPlanted).setScale(2, RoundingMode.HALF_UP);
                    treesPlanted = bigDecimal.floatValue();

                    bigDecimal = new BigDecimal(lightBulbs).setScale(2, RoundingMode.HALF_UP);
                    lightBulbs = bigDecimal.floatValue();

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


        final View envBenefitsImageView = activity.findViewById(R.id.envBenefitsImageView);
        activity.runOnUiThread(()->{
            envBenefitsImageView.setOnClickListener((View v)-> {
                String envBenefits = String.format("Környezeti hatások %n %s kg CO2, %s kg SO2, %s kg NOx, %n " +
                "%s fa ültetve, %s égő energiája megspórolva", co2Saved, so2Saved, noxSaved, treesPlanted, lightBulbs);

                Toast.makeText(activity,envBenefits,Toast.LENGTH_LONG).show();
            });
        });


        return null;
    }
}
