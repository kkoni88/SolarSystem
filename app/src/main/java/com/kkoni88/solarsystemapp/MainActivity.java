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
import com.kkoni88.solarsystemapp.queries.DailySiteEnergy;
import com.kkoni88.solarsystemapp.queries.SiteEnvironmentalBenefits;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DailySiteEnergy(this).execute();
//        new SiteEnvironmentalBenefits(this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
