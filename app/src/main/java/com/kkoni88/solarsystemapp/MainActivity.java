package com.kkoni88.solarsystemapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kkoni88.solarsystemapp.queries.DailySiteEnergy;
import com.kkoni88.solarsystemapp.queries.SiteEnvironmentalBenefits;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DailySiteEnergy(this).execute();
        new SiteEnvironmentalBenefits(this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
