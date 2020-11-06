package com.quananhle.knowyourgovernment.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.quananhle.knowyourgovernment.MainActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Locator {
    private static final String TAG = "Locator";
    private MainActivity owner;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private final int REQUEST_CODE = 5;
    //default constructor
    public Locator(MainActivity mainActivity) {
        this.owner = mainActivity;
        if (checkPermission()){
            Log.d("Setup Locator", "Step 1");
            setUpLocationManager();
            determineLocation();
        }
    }
    public boolean checkPermission(){
        if (ContextCompat.checkSelfPermission(owner, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(owner, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return false;
        }
        return true;
    }
    public void setUpLocationManager(){
        if (locationManager != null){
            return;
        }
        if (!checkPermission()){
            return;
        }
        locationManager = (LocationManager) owner.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Toast.makeText(owner, "Update from " + location.getProvider(), Toast.LENGTH_SHORT).show();
                Log.d("Get position", "" + location.getLatitude() + location.getLongitude());
                owner.setLocation(location.getLatitude(), location.getLongitude());
                owner.warningClose();
            }
        }
    }

}
