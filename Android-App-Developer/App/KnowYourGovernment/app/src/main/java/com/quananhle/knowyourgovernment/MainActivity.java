package com.quananhle.knowyourgovernment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.quananhle.knowyourgovernment.helper.OfficialsAdapter;
import com.quananhle.knowyourgovernment.helper.OfficialsList;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<OfficialsList> officialsList;
    private OfficialsAdapter officialsAdapter;
    private static MainActivity mainActivity;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    
}