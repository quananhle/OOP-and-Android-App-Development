package com.example.stockwatch;

import androidx.annotation.NonNull;
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
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stockwatch.database.DatabaseHandler;
import com.example.stockwatch.helper.Stock;
import com.example.stockwatch.helper.StockAdapter;
import com.example.stockwatch.helper.StockLoaderRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            showMessage(R.drawable.error, "NETWORK ERROR", "No Internet Connection Found!");
        }
    }
    @Override
    public void onClick(View v){
        int pos = recyclerView.getChildLayoutPosition(v);
        Stock s = stockList.get(pos);
        final String URL = webURL + s.getSymbol();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URL));
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
        builder.setTitle("\t\t\t\t\t\t\t\t\t\tPROCEED TO WEB BROWSER?");
        builder.setMessage("\t\t\t\t\t\t\t\t\t\tYOU WILL BE REDIRECTED TO ANOTHER APP");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public boolean onLongClick(View v){
        //get the position of item that is long clicked
        final int pos = recyclerView.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.addStock:
                Toast.makeText(this, "NEW STOCK", Toast.LENGTH_SHORT).show();
                addButtonSelected();
                return true;
            default:
                return super.onOptionsItemSelected(item)
        }
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
                        showMessage(R.drawable.warning, "WARNING", "Missing Stock Symbol");
                    }
                    else{
                        databaseHandler.findStock(params);
                    }
                }
                else {
                    showMessage(R.drawable.warning, "WARNING", "Search Failed!");
                }
                break;
        }
        Collections.sort(stockList);
        stockAdapter.notifyDataSetChanged();
    }
    //========================HELPER•METHODS===================================\\
    public boolean addButtonSelected(){
        if (!isConnected()){
            showMessage(R.drawable.error, "NETWORK ERROR", "No Internet Connection Found");
        }
        else {
            final MainActivity mainActivity = this;
            final EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            editText.setGravity(Gravity.CENTER_HORIZONTAL);

            final String symbol = editText.getText().toString();
            //check if user has entered a stock symbol into the field
            if (symbol.trim().isEmpty()){
                Toast.makeText(this, "INPUT MISSING", Toast.LENGTH_SHORT).show();
                showMessage(R.drawable.warning, "STOCK SYMBOL MISSING", "You Have Not Entered A Valid Stock Symbol");
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(editText);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showOptionsDialog(symbol);
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //do nothing
                }
            });
            builder.setTitle("Stock Selection");
            builder.setMessage("Please enter a Stock Symbol");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return true;
    }
//    public void showOptionsDialog(String userInput){
//        final List<Stock> items = new ArrayList<>();
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
//                (this, android.R.layout.select_dialog_singlechoice);
//        //check if stock is already in the watchlist
//        for (int i=0; i < stockList.size(); ++i){
//            if (stockList.get(i).getSymbol().equals(userInput)){
//                showMessage(R.drawable.warning,
//                        "DUPLICATE STOCK!",
//                        "Stock Symbol" + userInput + " is already displayed!");
//            }
//        }
//        //search all stocks that matches user input
//        for(int i=0; i<stockList.size(); ++i){
//            if (stockList.get(i).getSymbol().contains(userInput)){
//                items.add(stockList.get(i));
//            }
//        }
//        for(int i=0; i<items.size(); ++i){
//            arrayAdapter.add(items.get(i).getCompany());
//        }
//        if (items.size()>0){
//            final CharSequence[] listCompany = new CharSequence[items.size()];
//            for (int i=0; i < items.size(); ++i){
//                listCompany[i] = items.get(i) + " - " + StockMap
//            }
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setNegativeButton("NEVERMIND", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.setTitle("Make a Selection");
//        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Stock stock = new Stock(items.get(which).getCompany(), items.get(which).getSymbol());
//                databaseHandler.
//            }
//        });
//    }
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
    private void showMessage(int iconId, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(iconId);
        builder.setTitle(title);
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
//    public void showFindResults(Stock stock){
//        //if no stock found
//        if (stock == null){
//            showMessage(R.drawable.error, "STOCK NOT FOUND", "No Stocks Matched Search Criteria");
//            return;
//        }
//        //dialog with a layout
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        final View view = layoutInflater.inflate(R.layout.stock_entry, null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("FIND RESULTS: ");
//        builder.setIcon(R.drawable.search);
//        ((TextView) view.findViewById(R.id.company)).setText(stock.getCompany());
//        ((TextView) view.findViewById(R.id.symbol)).setText(stock.getSymbol());
//        builder.setView(view);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
    /*
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
        builder.setTitle("\t\t\t\t\t\t\t\t\t\tPROCEED TO WEB BROWSER?");
        builder.setMessage("\t\t\t\t\t\t\t\t\t\tYOU WILL BE REDIRECTED TO DIFERENT APP");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
     */
    public boolean isConnected(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (connectivityManager == null) {
            return false;
        }
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else {
            return false;
        }
    }
}

