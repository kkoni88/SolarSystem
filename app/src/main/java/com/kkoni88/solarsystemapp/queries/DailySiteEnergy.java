package com.kkoni88.solarsystemapp.queries;

import android.app.Activity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kkoni88.solarsystemapp.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class DailySiteEnergy extends AbstractQueryAsyncTask {
    public DailySiteEnergy(Activity activity) {
        super(activity);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
        Date dayBefore = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        LocalDate yesterday = LocalDate.now();
        yesterday.minusDays(1);

        Date date = new Date();
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        // DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("yyyy-mm/dd% 'at' hh:mm a z");

        //  LocalDate localDate = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(localDateTime.toString()));

        String url = urlBuilder
                .addPathSegment("energyDetails")
                .addQueryParameter("startTime", SolarHelper.getZonedDate(ZonedDateTime.now()))
//                .addQueryParameter("startTime", SolarHelper.getZonedDate(ZonedDateTime.now().minusDays(1)))
                .addQueryParameter("endTime", SolarHelper.getZonedDateTime(ZonedDateTime.now()))
//                .addQueryParameter("endTime", SolarHelper.getZonedDateTime(ZonedDateTime.now().minusDays(1)))
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
                    queryResult = generatedPower;
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

        final TextView statusTV = activity.findViewById(R.id.status);
        float queryResultFloat = Float.parseFloat(queryResult) / 1000;
        BigDecimal bigDecimal = new BigDecimal(queryResultFloat).setScale(2, RoundingMode.HALF_UP);
        statusTV.setText(String.format("A ma megtermelt áram: %s kWh", bigDecimal.doubleValue()));
    }
}
