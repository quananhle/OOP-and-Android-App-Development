package com.example.stockwatch.database;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.example.stockwatch.MainActivity;
import com.example.stockwatch.helper.Stock;

import java.util.ArrayList;

public class StockUpdate extends AsyncTask<ArrayList<Stock>, Integer, ArrayList<Stock>> {

    MainActivity context;

    StockUpdate (MainActivity context){
        this.context = context;
    }

    @SuppressLint("WrongThread")
    @Override
    protected ArrayList<Stock> doInBackground(ArrayList<Stock>... arrayLists) {
        new StockData(context).execute(new ArrayList<Stock>());
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Stock> stocks) {
        super.onPostExecute(stocks);
    }
}
