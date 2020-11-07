package com.quananhle.knowyourgovernment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.quananhle.knowyourgovernment.details.OfficialActivity;
import com.quananhle.knowyourgovernment.helper.Locator;
import com.quananhle.knowyourgovernment.helper.OfficialAdapter;
import com.quananhle.knowyourgovernment.helper.Officials;
import com.quananhle.knowyourgovernment.thread.OfficialLoaderRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Quan Le
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private List<Officials> officialsList = new ArrayList<>();
    private OfficialAdapter officialAdapter;
    private static MainActivity mainActivity;
    private ConnectivityManager connectivityManager;
    private TextView locationView;
    private Locator locator;

    final int WARNING_ICON = 1;
    final int ERROR_ICON = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        officialAdapter = new OfficialAdapter(officialsList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //check if connected to the network at start
        if (!isConnected()){
            showMessage(ERROR_ICON,
                    "NO NETWORK CONNECTION",
                    "Data cannot be accessed/loaded without an Internet connection");
        }

        // Load the data
        OfficialLoaderRunnable officialLoaderRunnable = new OfficialLoaderRunnable(this, "60608");
        new Thread(officialLoaderRunnable).start();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem){
        Intent intent;
        switch (menuItem.getItemId()){
            case R.id.search_button:
                return true;
            case R.id.about_button:
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
    @Override
    public void onClick(View view){
        int position = recyclerView.getChildAdapterPosition(view);
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("location", locationView.getText().toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable("official", officialsList.get(position));
        startActivity(intent);
    }







    //====================== *** HELPER•METHODS *** ======================//
    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (connectivityManager == null){
            return false;
        }
        else if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else{
            return false;
        }
    }

    public void setLocation(double latitude, double longtitude) {
        Log.d(TAG, "doAddress: Lat " + latitude + ", Lon " + longtitude);
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(this , Locale.getDefault());
        try {
            Log.d(TAG, "doAddress: Retrieving address");
            addressList = geocoder.getFromLocation(latitude, longtitude, 1);
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        if (addressList == null){
            Log.d("Address", "Not Found!");
        }
        else {
            locationView.setText(addressList.get(0).getAddressLine(1));
        }
//        OfficialLoaderRunnable officialLoaderRunnable = new OfficialLoaderRunnable(this);
//        officialLoaderRunnable.run();
    }

    public void showMessage(int icon, String title, String message){
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        if (icon == WARNING_ICON){
            dialog.setIcon(R.drawable.warning);
        }
        else if (icon == ERROR_ICON){
            dialog.setIcon(R.drawable.error);
        }
        else {
            dialog.setIcon(null);
        }
        dialog.show();
    }
    //=====* OfficialLoaderRunnable *====//
//    public void doRunnable(View v){
//        if ()
//    }
    public void downloadFailed() {
        officialsList.clear();
    }
    public void updateList(ArrayList<Officials> officialsArrayList) {
        officialsList.addAll(officialsArrayList);
    }
    public void setLocationView(String location){
        locationView.setText(location);
    }
}