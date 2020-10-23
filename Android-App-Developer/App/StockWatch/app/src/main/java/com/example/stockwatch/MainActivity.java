package com.example.stockwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stockwatch.database.DatabaseHandler;
import com.example.stockwatch.helper.Stock;
import com.example.stockwatch.helper.StockAdapter;
import com.example.stockwatch.database.StockData;
import com.example.stockwatch.database.StockLoader;
import com.example.stockwatch.helper.StockLoaderRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author Quan Le
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String webURL = "http://www.marketwatch.com/investing/stock/";

    private ArrayList<Stock> stockList = new ArrayList<Stock>();

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private StockAdapter stockAdapter;
    private DatabaseHandler databaseHandler;

    private static final int ADD_CODE = 1;
    private static final int UPDATE_CODE = 2;
    private static final int FIND_CODE = 3;

    private static MainActivity ma;

    public ArrayList<Stock> companyList = new ArrayList<>();

    private static final String TAG = "onOpen";

    final int WARNING_ICON = 1;
    final int ERROR_ICON = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        stockAdapter = new StockAdapter(stockList, this);
        recyclerView.setAdapter(stockAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        databaseHandler = new DatabaseHandler(this);
        if(!isConnected()){
            AlertDialog.Builder noNet = new AlertDialog.Builder(this);
            noNet.setTitle("Not connected to network");
            noNet.show();
        }

        databaseHandler.dumpDbToLog();

        //load the data
        StockLoaderRunnable stockLoaderRunnable = new StockLoaderRunnable(this);
        new Thread(stockLoaderRunnable).start();

        ArrayList<Stock> list = databaseHandler.loadStocks();
        stockList.addAll(list);
        Collections.sort(stockList);
        stockAdapter.notifyDataSetChanged();
        new StockData(this).execute(stockList);;
    }

    @Override
    public void onResume() {
        ArrayList<Stock> watchList = databaseHandler.loadStocks();
        new StockLoader(this).execute("https://api.iextrading.com/1.0/ref-data/symbols");
        Log.d(TAG, "onResume: " + watchList);
        stockAdapter.notifyDataSetChanged();
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_stock_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.addStock:
                Toast.makeText(this, "NEW STOCK", Toast.LENGTH_SHORT).show();
                addButtonSelected();
            default:
                return false;
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
                        showMessage(WARNING_ICON, "WARNING", "Missing Stock Symbol");
                    }
//                    else{
//                        databaseHandler.findStock(params);
//                    }
                }
                else {
                    showMessage(WARNING_ICON, "WARNING", "Search Failed!");
                }
                break;
        }
        Collections.sort(stockList);
        stockAdapter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View view) {
        if(!isConnected()){
            showMessage(WARNING_ICON, "NETWORK ERROR",
                    "No network connection found. Failed to redirect to web browser");
        }
        else{
            int pos = recyclerView.getChildLayoutPosition(view);
            Stock s = stockList.get(pos);
            final String URL = webURL + s.getSymbol();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(URL));
                    stockAdapter.notifyDataSetChanged();
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //do nothing
                    ;
                }
            });
            builder.setTitle("PROCEED TO WEB BROWSER?");
            builder.setMessage("You will be redirected to another app if proceed");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    @Override
    public boolean onLongClick(View view) {
        //get the position of item that is long clicked
        final int pos = recyclerView.getChildLayoutPosition(view);
        int position = pos;
        int size = stockList.size();
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
        builder.setTitle("REMOVE STOCK FROM WATCHLIST");
        builder.setMessage("Are you sure you want to remove \n "
                +
                stockList.get(pos).getCompany() + " (" + stockList.get(pos).getSymbol().toUpperCase() + ")"
                +
                " \n from your watchlist?");
        AlertDialog dialog = builder.create();
        dialog.setIcon(android.R.drawable.ic_menu_delete);
        dialog.show();
        Toast.makeText(this, "REMOVE " + stockList.get(position).getSymbol() + "?",
                Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onDestroy() {
        databaseHandler.shutDown();
        super.onDestroy();
    }
    //========================HELPER•METHODS===================================\\
    public void downloadFailed(){
        this.stockList.clear();
        this.stockAdapter.notifyDataSetChanged();
    }
    public void updateStockData(ArrayList<Stock> stockList){
        this.stockList.addAll(stockList);
        this.stockAdapter.notifyDataSetChanged();
    }
    public void updateData(ArrayList<Stock> cList){
        if (!companyList.isEmpty()){
            companyList.clear();
        }
        companyList.addAll(cList);
    }
    private void showMessage(int icon, String title, String message){
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        if (icon == WARNING_ICON){
            dialog.setIcon(R.drawable.warning);
        }
        else if (icon == ERROR_ICON){
            dialog.setIcon(R.drawable.error);
        }
        else {
            dialog.setIcon(null);
        }
        dialog.show();
    }
    public void addNewStock(Stock stock){
        databaseHandler.addStock(stock);
        ArrayList<Stock> list = databaseHandler.loadStocks();
        stockList.clear();
        stockList.addAll(list);
        new StockData(this).execute(stockList);
    }
    public ArrayList<Stock> searchingStock(String symbol){
        ArrayList<Stock> stocks = new ArrayList<>();
        if(symbol.length() <= 0){
            return stocks;
        }
        if(!symbol.isEmpty()) {
            for (int i = 0; i < stockList.size(); i++) {
                if (stockList.get(i).getSymbol().equals(symbol)) {
                    showMessage(ERROR_ICON,"Duplicate Stock",
                            "Stock Symbol " + symbol + " is already displayed.");
                }
            }
        }
        for(int i =0; i < companyList.size(); i++){
            if(companyList.get(i).getSymbol().contains(symbol.toUpperCase())){
                stocks.add(companyList.get(i));
            }
        }
        return stocks;
    }
    public void showOptionsDialog(final ArrayList<Stock> companyList, String symbol){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Make A Selection");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_singlechoice);
        for(int i =0; i < companyList.size(); i++){
            arrayAdapter.add(companyList.get(i).getCompany());
        }

        builderSingle.setNegativeButton("NEVERMIND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Stock stock =  new Stock(
                        companyList.get(which).getCompany(),
                        companyList.get(which).getSymbol()
                );
                addNewStock(stock);
                Collections.sort(stockList);
            }
        });
        if(companyList.size() > 0){
            builderSingle.show();
        }
        else if (symbol.trim().isEmpty()) {
            Toast.makeText(this, "INPUT MISSING!", Toast.LENGTH_SHORT).show();
            showMessage(WARNING_ICON, "STOCK SYMBOL MISSING", "You Have Not Entered A Valid Stock Symbol");
        }
        else {
            Toast.makeText(this, "STOCK SEARCHING FAILED!", Toast.LENGTH_SHORT).show();
            showMessage(ERROR_ICON, "SYMBOL NOT FOUND", "Check the stock symbol again");
        }
    }
    public boolean isConnected(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
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
    public boolean addButtonSelected(){
        if (isConnected()) {
            final MainActivity main = this;
            final EditText editText = new EditText(this);

            AlertDialog.Builder adb = new AlertDialog.Builder(this);

            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            editText.setGravity(Gravity.CENTER_HORIZONTAL);

            adb.setView(editText);
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String symbol = editText.getText().toString();
                    ArrayList<Stock> result = searchingStock(symbol);
                    showOptionsDialog(result, symbol);
                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
            adb.setTitle("Stock Selection");
            adb.setMessage("Enter a Stock Symbol:");
            AlertDialog dialog = adb.create();
            dialog.setIcon(R.drawable.create);
            dialog.show();
        } else{
            showMessage(ERROR_ICON, "NETWORK ERROR",
                    "Stocks Cannot Be Added Without A Network Connection");
        }
        return true;
    }
    public void doRefresh(){
        if(isConnected()){
            for(int i=0; i < stockList.size(); i++){
                new StockLoader(this).execute(new String[] {stockList.get(i).getSymbol(), stockList.get(i).getCompany()});
            }
            Toast.makeText(this, "WATCHLIST UPDATED!", Toast.LENGTH_SHORT).show();
        }
        //otherwise, prompt user that we do not have a connection
        else{
            showMessage(ERROR_ICON, "NETWORK ERROR",
                    "Stocks Cannot Be Updated Without A Network Connection");
        }
        stockAdapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }
    public void doneRefresh(){
        swipeRefresh.setRefreshing(false);
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
//    @SuppressLint("SetJavaScriptEnabled")
//    public void readStock(View v){
//        String symbol = ((TextView) findViewById(R.id.symbol)).getText().toString();
//        if (symbol.trim().isEmpty()){
//            return;
//        }
//        final String URL = webURL + symbol;
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(URL));
//                stockAdapter.notifyDataSetChanged();
//            }
//        });
//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //do nothing
//                ;
//            }
//        });
//        builder.setTitle("\t\t\t\t\t\t\t\t\t\tPROCEED TO WEB BROWSER?");
//        builder.setMessage("\t\t\t\t\t\t\t\t\t\tYOU WILL BE REDIRECTED TO DIFERENT APP");
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
}