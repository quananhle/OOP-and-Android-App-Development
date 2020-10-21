package com.example.stockwatch.database;

import android.os.AsyncTask;

import com.example.stockwatch.MainActivity;
import com.example.stockwatch.helper.Stock;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StockData extends AsyncTask<ArrayList<Stock>, Void, ArrayList<Stock>> {
    MainActivity mainActivity;
    private static final String REQUEST_METHOD = "GET";
    private final String API_KEY = "pk_86b3f91f7dde4df9841dc6a9a7d61168";

    public StockData ( MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override
    protected ArrayList<Stock> doInBackground(ArrayList<Stock>... arrayLists) {
        String jSon = "";
        try {
            for (int i = 0; i < arrayLists[0].size(); i++) {
                URL url = new URL("https://cloud.iexapis.com/stable/stock/" +
                        arrayLists[0].get(i).getSymbol() + "/quote?token=" + API_KEY);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(REQUEST_METHOD);
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    jSon = line;
                }
                try {
                    JSONObject jsonObject = new JSONObject(jSon);
                    double price = jsonObject.getDouble("latestPrice");
                    double change = jsonObject.getDouble("change");
                    double percentage = jsonObject.getDouble("changePercent");
                    arrayLists[0].get(i).setCurrentPrice(price);
                    arrayLists[0].get(i).setTodayPriceChange(change);
                    arrayLists[0].get(i).setTodayPercentChange(percentage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return arrayLists[0];
    }
    @Override
    protected void onPostExecute(ArrayList<Stock> stocks) {
        mainActivity.onResume();
        super.onPostExecute(stocks);
    }
}
