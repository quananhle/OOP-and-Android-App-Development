package com.example.stockwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
    }
    public void execRunnable(View v){
        String stockData = editText.getText().toString();
        StockDataGetter dataGetter = new StockDataGetter(this, stockData);
        new Thread(dataGetter).start();
        Log.d(TAG, "run: Pretending");
    }
    public void getStockData(View v){
        String stockData = editText.getText().toString();
        StockDataGetter stockDataGetter = new StockDataGetter(this, stockData);
        new Thread(stockDataGetter).start();
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