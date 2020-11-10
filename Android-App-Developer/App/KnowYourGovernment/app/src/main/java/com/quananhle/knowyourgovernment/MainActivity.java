package com.quananhle.knowyourgovernment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.quananhle.knowyourgovernment.about.AboutActivity;
import com.quananhle.knowyourgovernment.details.OfficialActivity;
import com.quananhle.knowyourgovernment.helper.Locator;
import com.quananhle.knowyourgovernment.helper.OfficialAdapter;
import com.quananhle.knowyourgovernment.helper.Officials;
import com.quananhle.knowyourgovernment.helper.AsyncOfficial;
import com.quananhle.knowyourgovernment.thread.OfficialLoaderRunnable;

/**
 * @author Quan Le
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private List<Officials> officialsList = new ArrayList<>();
    private OfficialAdapter officialAdapter;
    private MainActivity mainActivity = this;
    private ConnectivityManager connectivityManager;
    private TextView locationView;
    private Locator locator;
    private EditText editText;

    final int WARNING_ICON = 1;
    final int ERROR_ICON = 2;
    final int REQUEST_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        officialAdapter = new OfficialAdapter(officialsList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationView = findViewById(R.id.location);

        //check if connected to the network at start
        if (!isConnected()) {
            showMessage(ERROR_ICON,
                    "NO NETWORK CONNECTION",
                    "Data cannot be accessed/loaded without an Internet connection");
        }
        locator = new Locator(this);
        locator.shutDown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.search_button:
                Log.d(TAG, "onOptionsItemSelected: search clicked");
                Toast.makeText(this, "NEW LOCATION", Toast.LENGTH_SHORT).show();
                if (isConnected()) {
                    searchButtonPressed();
                } else {
                    showMessage(ERROR_ICON, "NO NETWORK CONNECTION",
                            "Data cannot be accessed/loaded without an Internet connection");
                }
                break;
            case R.id.about_button:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(this, "Unknown Option", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("location", locationView.getText().toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable("official", officialsList.get(position));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            Log.d(TAG, "onRequestPermissionResult: Permission length" + permissions.length);
            for (int i = 0; i < permissions.length; ++i) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        locator.setUpLocationManager();
                        locator.determineLocation();
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    } else {
                        Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting");
    }

    //====================== *** HELPER•METHODS *** ======================//
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (connectivityManager == null) {
            return false;
        } else if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void showMessage(int icon, String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        if (icon == WARNING_ICON) {
            dialog.setIcon(R.drawable.warning);
        } else if (icon == ERROR_ICON) {
            dialog.setIcon(R.drawable.error);
        } else {
            dialog.setIcon(null);
        }
        dialog.show();
    }

    //=====* OfficialAdapter *====//
    public void setOfficialsList(Object[] list) {
        if (list == null) {
            locationView.setText("No Data For Location");
            officialsList.clear();
        } else {
            locationView.setText(list[0].toString());
            officialsList.clear();
            ArrayList<Officials> officialsArrayList = (ArrayList<Officials>) list[1];
            for (int i = 0; i < officialsArrayList.size(); ++i) {
                officialsList.add(officialsArrayList.get(i));
            }
        }
        officialAdapter.notifyDataSetChanged();
    }

    //=====* Menu onOptionsItemSelected *====//
    public void searchButtonPressed() {
        if (!isConnected()) {
            showMessage(ERROR_ICON,
                    "NO NETWORK CONNECTION",
                    "Data cannot be accessed/loaded without an Internet connection");
        }
        final EditText editText = new EditText(this);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        adb.setView(editText);

        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String location = editText.getText().toString();
//                doRunnable(location);
                 new AsyncOfficial(mainActivity).execute(location);
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: searching cancelled");
            }
        });
        adb.setMessage("Enter a City, State, or Zip Code:");
        AlertDialog dialog = adb.create();
        dialog.show();
    }

    public void setLocation(double latitude, double longtitude) {
        Log.d(TAG, "doAddress: Lat " + latitude + ", Lon " + longtitude);
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            Log.d(TAG, "doAddress: Retrieving address");
            addressList = geocoder.getFromLocation(latitude, longtitude, 1);
//            OfficialLoaderRunnable officialLoaderRunnable = new OfficialLoaderRunnable(this,
//                    addressList.get(0).getPostalCode());
            Log.d(TAG, "doAddress: " + addressList.get(0).getPostalCode());
//            officialLoaderRunnable.run();
            new AsyncOfficial(mainActivity).execute(addressList.get(0).getPostalCode());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Log.d(TAG, "doAddress: " + ioe.getMessage());
            Toast.makeText(this, "Address Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    //=====* OfficialLoaderRunnable *====//
    public void doRunnable(String location){
        if (isConnected()){
            if (editText.getText().toString().isEmpty()){
                Toast.makeText(this, "Location is missing", Toast.LENGTH_SHORT).show();
                return;
            }
            //Load the data
            OfficialLoaderRunnable officialLoaderRunnable = new OfficialLoaderRunnable(this, location);
            new Thread(officialLoaderRunnable).start();
        }
    }
    public void downloadFailed() {
        officialsList.clear();
    }

    public void updateList(ArrayList<Officials> officialsArrayList) {
        officialsList.addAll(officialsArrayList);
    }

    public void setLocationView(String location) {
        locationView.setText(location);
    }
}

