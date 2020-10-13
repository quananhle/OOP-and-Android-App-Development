package com.example.stockwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnLongClickListener, View.OnLongClickListener{
    private final List<Companies> companiesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private static final String TAG = "MainActivity";
    private StockAdapter stockAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        stockAdapter = new StockAdapter(companiesList, this);
    }
    public void execRunnable(View v){
        String stockData = editText.getText().toString();
        StockDataGetter dataGetter = new StockDataGetter(this, stockData);
        new Thread(dataGetter).start();
        Log.d(TAG, "run: Pretending");
    }
    public void getStockData(View v){
        String stockData = editText.getText().toString();
        new Thread(new StockDataGetter(this, stockData)).start();
    }
    public void receiveStockData(final String s){
        Log.d(TAG, "run: Pretending");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editText.setText(s);
            }
        });
    }
}