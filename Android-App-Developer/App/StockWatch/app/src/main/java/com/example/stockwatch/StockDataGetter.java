package com.example.stockwatch;

import android.util.Log;

import java.util.Date;

public class StockDataGetter implements Runnable{
    private static final String TAG = "DataGetter";
    public MainActivity mainActivity;
    private String data;

    public StockDataGetter(MainActivity mainActivity, String data) {
        this.mainActivity = mainActivity;
        this.data = data;
    }

    @Override
    public void run(){
        for (int i = 0; i < 5; ++i){
            Log.d(TAG, "run: Pretending" + i);
            try{
                Thread.sleep(1000);
            }
            catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.receiveData(new Date().toString());
            }
        });
        Log.d(TAG, "run: DONE");
    }
    
}
