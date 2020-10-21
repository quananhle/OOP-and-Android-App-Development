package com.example.stockwatch.database;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.stockwatch.MainActivity;
import com.example.stockwatch.helper.Stock;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StockLoader extends AsyncTask<String, String, String> {
    private static final String REQUEST_METHOD = "GET";
    private MainActivity mainActivity;
    public StockLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    @Override
    protected void onPreExecute() {}
    @Override
    protected void onPostExecute(String s) {
        ArrayList<Stock> stockList = parseJSON(s);
        if (stockList != null)
            mainActivity.updateData(stockList);
    }
    @Override
    protected String doInBackground(String... params) {
        Uri dataUri = Uri.parse(params[0]);
        String urlToUse = dataUri.toString();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(REQUEST_METHOD);
            InputStream inputStream = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (Exception e) {
            return null;
        }
        return stringBuilder.toString();
    }
    private ArrayList<Stock> parseJSON(String s) {
        ArrayList<Stock> stockList = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(s);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jArray.get(i);
                String name = jsonObject.getString("name");
                String symbol = jsonObject.getString("symbol");
                double price = 0.00;
                double change = 0.00;
                double percentage = 0.00;
                stockList.add(new Stock(name, symbol, price, change, percentage));
            }
            return stockList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
