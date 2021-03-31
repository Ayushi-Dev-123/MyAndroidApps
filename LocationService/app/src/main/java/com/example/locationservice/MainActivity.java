package com.example.locationservice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.locationservice.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.location.LocationManager.*;

public class MainActivity extends AppCompatActivity implements LocationListener {

    ActivityMainBinding binding;
    LocationManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        getPermission();
        checkProviderIsEnableOrNot();
        manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        try {
            manager.requestLocationUpdates(NETWORK_PROVIDER, 100, 5, this);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void  checkProviderIsEnableOrNot(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        try{
            gpsEnabled = lm.isProviderEnabled(GPS_PROVIDER);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            networkEnabled = lm.isProviderEnabled(NETWORK_PROVIDER);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(!gpsEnabled && !networkEnabled){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Enabled Location")
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent in = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(in);
                        }
                    }).setNegativeButton("Cancel", null)
                      .setCancelable(false)
                      .show();
        }
    }
    private void getPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED)
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PermissionChecker.PERMISSION_GRANTED){
               ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},111);
            }
    }
    @Override
    public void onLocationChanged(Location location){
       double latitude = location.getLatitude();
       double longitude = location.getLongitude();
       try {
           Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
           List<Address> list = geocoder.getFromLocation(latitude,longitude,1);
           String country = list.get(0).getCountryName();
           String state = list.get(0).getAdminArea();
           String city = list.get(0).getLocality();
           String pincode = list.get(0).getPostalCode();
           String completeAddress = list.get(0).getAddressLine(0);

           String address = "Country : " + country +
                            "\nState : " + state +
                            "\nCity : " + city +
                            "\nPinCode : " + pincode +
                            "\nComplete Address : " + completeAddress;
           binding.tv.setText(address);
           Log.e("Tag", "Address : " + address);
       }
       catch (IOException e) {
           e.printStackTrace();
       }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}