package com.example.stockwatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.stockwatch.database.DatabaseHandler;
import com.example.stockwatch.helper.Stock;
import com.example.stockwatch.helper.StockAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Quan Le
 */

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener{
    private final List<Stock> stockList = new ArrayList<>();
    private RecyclerView recyclerView;
    private static final String TAG = "MainActivity";
    private StockAdapter stockAdapter;
    private SwipeRefreshLayout swipeRefresh;

    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    private static final int FIND_CODE = 3;

    private DatabaseHandler databaseHandler;
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
        ArrayList<Stock> watchList = databaseHandler.loadStocks();
        stockList.addAll(watchList);
        Collections.sort(stockList);
        stockAdapter.notifyDataSetChanged();
        //Load the data
//        StockLoaderRunnable stockLoaderRunnable = new StockLoaderRunnable(this);
//        new Thread(stockLoaderRunnable).start();
    }
    @Override
    public void onClick(View v){
        int pos = recyclerView.getChildLayoutPosition(v);
        Stock s = stockList.get(pos);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Stock.class.getName(), s);
        startActivityForResult(intent, UPDATE_CODE);
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
        builder.setTitle("\t\t\t\t\t\t\t\t\t\tDELETE STOCK");
        builder.setMessage("\t\t\t\t\t\t\t\tARE YOU SURE YOU WANT TO \n \t\t\tDELETE STOCK "
                + stockList.get(pos).getCompany()
                + "(" + stockList.get(pos).getSymbol() + ")" + "?");
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
        }
    }
    //========================HELPER•METHOD===================================\\
    public void doAdd(View v){
        Intent intent = new Intent(this, dialog.class);
        startActivityForResult(int, ADD_CODE);
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
        new Thread(new ThreadedClass(this)).start();
        stockAdapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
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
}