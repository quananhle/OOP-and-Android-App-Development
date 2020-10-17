package com.example.stockwatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockwatch.database.DatabaseHandler;
import com.example.stockwatch.helper.Stock;
import com.example.stockwatch.helper.StockAdapter;
import com.example.stockwatch.helper.StockLoaderRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Collections.*;
import java.util.HashMap;
import java.util.List;

/**
 * @author Quan Le
 */

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener{
    private List<Stock> stockList = new ArrayList<>();
    private RecyclerView recyclerView;
    private static final String TAG = "MainActivity";
    private StockAdapter stockAdapter;
    private SwipeRefreshLayout swipeRefresh;

    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    private static final int FIND_CODE = 3;

    private DatabaseHandler databaseHandler;

    private static final String webURL = "http://www.marketwatch.com/investing/stock/";

    //********************************************************//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: ");
                doRefresh();
            }
        });
        stockAdapter = new StockAdapter(stockList, this);
        recyclerView.setAdapter(stockAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseHandler = new DatabaseHandler(this);

        databaseHandler.dumpDbToLog();

        //load the data
        StockLoaderRunnable stockLoaderRunnable = new StockLoaderRunnable(this);
        new Thread(stockLoaderRunnable).start();

        ArrayList<Stock> watchList = databaseHandler.loadStocks();
        stockList.addAll(watchList);
        Collections.sort(stockList);
        stockAdapter.notifyDataSetChanged();

        //check the network
        if (!isConnected()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("NETWORK ERROR");
            builder.setMessage("NO INTERNET CONNECTIONS FOUND!");
            builder.show();
        }
    }
    @Override
    public void onClick(View v){
        int pos = recyclerView.getChildLayoutPosition(v);
        Stock s = stockList.get(pos);
        final String URL = webURL + s.getSymbol();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URL));
                stockAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
                ;
            }
        });
//        builder.setTitle("\t\t\t\t\t\t\t\t\t\tPROCEED TO WEB BROWSER?");
        builder.setMessage("\t\t\t\t\t\t\t\t\t\tPROCEED TO WEB BROWSER?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public boolean onLongClick(View v){
        //get the position of item that is long clicked
        final int pos = recyclerView.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHandler.deleteStock(stockList.get(pos).getSymbol());
                        stockList.remove(pos);
                        stockAdapter.notifyDataSetChanged();
                    }
                });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
                ;
            }
        });
        builder.setTitle("\t\t\t\t\t\t\t\t\t\tREMOVE STOCK FROM WATCHLIST");
        builder.setMessage("\t\t\t\t\t\t\t\tARE YOU SURE YOU WANT TO \n \t\t\tREMOVE STOCK "
                + stockList.get(pos).getCompany()
                + "(" + stockList.get(pos).getSymbol().toUpperCase() + ")" + " FROM YOUR WATCHLIST?");
        AlertDialog dialog = builder.create();
        dialog.show();
        Toast.makeText(this, "DELETE", Toast.LENGTH_SHORT).show();
        return true;
    }
    @Override
    public void onBackPressed(){

    }
    @Override
    public void onResume(){
        databaseHandler.dumpDbToLog();
        ArrayList<Stock> watchList = databaseHandler.loadStocks();
        stockList.clear();
        stockList.addAll(watchList);
        Log.d(TAG, "onResume: " + watchList);
        stockAdapter.notifyDataSetChanged();
        super.onResume();
    }
    public void onDestroy(){
        databaseHandler.shutDown();
        super.onDestroy();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        if (requestCode != ADD_CODE && requestCode != UPDATE_CODE){
            Log.d(TAG, "onActivityResult: Unknown Request Code: " + requestCode);
            return;
        }
        if (resultCode != RESULT_OK){
            Toast.makeText(this, "ERROR ADDING NEW STOCK", Toast.LENGTH_SHORT).show();
            return;
        }
        Stock stock = null;
        if (data.hasExtra("COMPANY")){
            stock = (Stock) data.getSerializableExtra("COMPANY");
        }
        if (stock == null){
            Toast.makeText(this, "COMPANY DATA NOT FOUND", Toast.LENGTH_SHORT).show();
            return;
        }
        //Make database request
        switch (requestCode){
            case ADD_CODE:
                databaseHandler.addStock(stock);
                stockList.add(stock);
                break;
            case UPDATE_CODE:
                databaseHandler.updateList(stock);
                stockList.add(stock);
                break;
            case FIND_CODE:
                HashMap<String, String> params;
                if (data.hasExtra("FIND")){
                    params = (HashMap<String, String>) data.getSerializableExtra("FIND");
                    if (params == null || params.isEmpty()){
                        showWarning("MISSING STOCK SYMBOL");
                    }
                    else{
                        databaseHandler.findStock(params);
                    }
                }
                else {
                    showWarning("SEARCH FAILED!");
                }
                break;
        }
        Collections.sort(stockList);
//        Collections.sort(stockList);
        stockAdapter.notifyDataSetChanged();
    }
    //========================HELPER•METHODS===================================\\
    public void doAdd(Stock stock){
        stockList.add(stock);
        databaseHandler.addStock(stock);
        stockAdapter.notifyDataSetChanged();
    }

    public void updateStockData(ArrayList<Stock> stockList){
        this.stockList.addAll(stockList);
        this.stockAdapter.notifyDataSetChanged();
    }
    public void downloadFailed(){
        this.stockList.clear();
        this.stockAdapter.notifyDataSetChanged();
    }
    public void doRefresh(){
        Log.d(TAG, "onRefresh: ");

    }
    public void doneRefresh(){
        swipeRefresh.setRefreshing(false);
    }
    private void showWarning(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.warning);
        builder.setMessage(message);
        builder.setTitle("SEARCH FAILED");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showFindResults(Stock stock){
        //if no stock found
        if (stock == null){
            showWarning("NO STOCKS MATCHED SEARCH CRITERIA");
            return;
        }
        //dialog with a layout
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View view = layoutInflater.inflate(R.layout.stock_entry, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("FIND RESULTS: ");
        builder.setIcon(R.drawable.search);
        ((TextView) view.findViewById(R.id.company)).setText(stock.getCompany());
        ((TextView) view.findViewById(R.id.symbol)).setText(stock.getSymbol());
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void readStock(View v){
        String symbol = ((TextView) findViewById(R.id.symbol)).getText().toString();
        if (symbol.trim().isEmpty()){
            return;
        }
        final String URL = webURL + symbol;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(URL));
                stockAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
                ;
            }
        });
//        builder.setTitle("\t\t\t\t\t\t\t\t\t\tPROCEED TO WEB BROWSER?");
        builder.setMessage("\t\t\t\t\t\t\t\t\t\tPROCEED TO WEB BROWSER?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void updateData(ArrayList<Stock> watchList){
        stockList.addAll(watchList);
        stockAdapter.notifyDataSetChanged();
    }
    public boolean isConnected(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else {
            return false;
        }
    }
}