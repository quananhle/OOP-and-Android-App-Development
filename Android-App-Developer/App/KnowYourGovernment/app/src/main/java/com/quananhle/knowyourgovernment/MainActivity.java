package com.quananhle.knowyourgovernment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.quananhle.knowyourgovernment.helper.OfficialsAdapter;
import com.quananhle.knowyourgovernment.helper.Officials;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Officials> officialsList;
    private OfficialsAdapter officialsAdapter;
    private static MainActivity mainActivity;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //check if connected to the network at start
        if (!isConnected()){
            AlertDialog.Builder connectionFailed = new AlertDialog.Builder(this);
            connectionFailed.setTitle("NETWORK CONNECTION FAILED");
            connectionFailed.setMessage("Not connected to the Internet. Please check the network!");
            connectionFailed.show();
        }
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
}