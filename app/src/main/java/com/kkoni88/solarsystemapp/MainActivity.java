package com.kkoni88.solarsystemapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kkoni88.solarsystemapp.queries.DailySiteEnergy;
import com.kkoni88.solarsystemapp.queries.SiteAddress;
import com.kkoni88.solarsystemapp.queries.SiteEnvironmentalBenefits;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String TAG = "MainActivity";

    //members for google maps
    private GoogleMap gmap;
    private MapView mapView;
    private ImageView mapViewImageView;
    private String siteAddress;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DailySiteEnergy(this).execute();
        new SiteEnvironmentalBenefits(this).execute();
        new SiteAddress(this, (Object siteAddress) -> {
            String siteAddressStr = (String) siteAddress;
            LatLng siteLng = getLocationFromAddress(this, siteAddressStr);

            Bitmap sunBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sun);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(sunBitmap);

            gmap.addMarker(new MarkerOptions().position(siteLng)
                    .title(getString(R.string.google_maps_location_marker))
                    .icon(bitmapDescriptor)
            );
            gmap.moveCamera(CameraUpdateFactory.newLatLng(siteLng));
        }).execute();


        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);


        mapViewImageView = findViewById(R.id.mapImageView);

        runOnUiThread(() -> {
            mapViewImageView.setOnClickListener((View v) -> {
                mapView.setVisibility(View.VISIBLE == mapView.getVisibility() ? View.GONE : View.VISIBLE);
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    private LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng latLng = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            latLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return latLng;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(16);
    }
}
