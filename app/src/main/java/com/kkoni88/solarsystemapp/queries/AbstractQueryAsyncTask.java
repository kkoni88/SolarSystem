package com.kkoni88.solarsystemapp.queries;

import android.app.Activity;
import android.os.AsyncTask;

import com.kkoni88.solarsystemapp.R;

import java.util.concurrent.CountDownLatch;

import okhttp3.HttpUrl;

public class AbstractQueryAsyncTask extends AsyncTask<Void, Void, Void> {
    HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
    Activity activity;
    CountDownLatch countDownLatch = new CountDownLatch(1);
    String queryResult = "";

    public AbstractQueryAsyncTask(Activity activity) {
        this.activity = activity;
        urlBuilder.scheme(activity.getResources().getString(R.string.solar_monitoring_api_scheme))
                .host(activity.getResources().getString(R.string.solar_monitoring_api_host))
                .addQueryParameter("api_key", activity.getResources().getString(R.string.solar_api_key))
                .addPathSegments(activity.getResources().getString(R.string.solar_monitoring_api_path_site));

    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }
}
