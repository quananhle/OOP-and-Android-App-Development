package com.example.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHandler";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StockWatchDB";
    private static final String TABLE_NAME = "StockTable";

    private static final String COMPANY = "CompanyName";
    private static final String SYMBOL = "Symbol";
    private static final String PRICE = "Price";
    private static final String CHANGE = "PriceChange";
    private static final String PERCENT_CHANGE = "PercentChange";

    private static final String SQL_CREATE_TABLE = "CREATE_TABLE " + TABLE_NAME + " (" +
            COMPANY + " TEXT " +
            SYMBOL + " TEXT " +
            PRICE + " TEXT " +
            CHANGE + " DOUBLE " +
            PERCENT_CHANGE + " DOUBLE)";

    private SQLiteDatabase database;

    DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
        Log.d(TAG, "DatabaseHandler: DONE");
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d(TAG, "onCreate = Making new DB");
        db.execSQL(SQL_CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){


    }
    ArrayList<Stock> loadStocks(){
        //load stocks and return a watch list of loaded stocks
        Log.d(TAG, "loadStocks: START");
        ArrayList<Stock> watchList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{COMPANY, SYMBOL, PRICE, CHANGE, PERCENT_CHANGE},
                null,
                null,
                null,
                null,
                null);
        if (cursor != null){
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); ++i){
                String company = cursor.getString(0);
                String symbol = cursor.getString(1);
                Double price = cursor.getDouble(2);
                Double change = cursor.getDouble(3);
                Double percentChange = cursor.getDouble(4);
                Stock s = new Stock(company, symbol, price, change, percentChange);
                watchList.add(s);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d(TAG, "loadWatchList: DONE");
        return watchList;
    }
    public void addStock(Stock stock){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPANY, stock.getCompany());
        contentValues.put(SYMBOL, stock.getSymbol());
        contentValues.put(PRICE, stock.getCurrentPrice());
        contentValues.put(CHANGE, stock.getTodayPriceChange());
        contentValues.put(PERCENT_CHANGE, stock.getTodayPercentChange());

        long key = database.insert(TABLE_NAME, null, contentValues);
        Log.d(TAG, "addStock: " + key);
    }
    public void updateList(Stock stock){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPANY, stock.getCompany());
        contentValues.put(SYMBOL, stock.getSymbol());
        contentValues.put(PRICE, stock.getCurrentPrice());
        contentValues.put(CHANGE, stock.getTodayPriceChange());
        contentValues.put(PERCENT_CHANGE, stock.getTodayPercentChange());

        long numRows = database.update(TABLE_NAME, contentValues, COMPANY + " = ?",
                new String[]{stock.getSymbol()});
        Log.d(TAG, "updateWatchList: " + numRows);
    }
    public void deleteStock(String name){
        Log.d(TAG, "deleteStock: " + name);
        int cnt = database.delete(TABLE_NAME, COMPANY + " ?", new String[]{name});
        Log.d(TAG, "deleteStock: " + cnt);
    }
    public void dumpDbToLog(){
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor != null){
            cursor.moveToFirst();

            Log.d(TAG, "dumpDatabaseToLog: v");
            for (int i=0; i < cursor.getCount(); ++i){
                String company = cursor.getString(0);
                String symbol = cursor.getString(1);
                Double price = cursor.getDouble(2);
                Double priceChange = cursor.getDouble(3);
                Double percentChange = cursor.getDouble(4);
                Log.d(TAG, "dumpDatabaseToLog: " +
                        String.format("%s %-18s", COMPANY + ": ", company) +
                        String.format("%s %-18s", SYMBOL + ": ", symbol) +
                        String.format("%s %-18s", PRICE + ": ", price) +
                        String.format("%s %-18s", CHANGE + ": ", priceChange) +
                        String.format("%s %-18s", PERCENT_CHANGE + ": ", percentChange));
                cursor.moveToNext();
            }
            cursor.close();
        }
    }
    public void shutDown(){
        database.close();
    }
    public void findStock(HashMap<String, String> params){
        Log.d(TAG, "findBook: ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : params.keySet()){
            stringBuilder.append(key).append(" = '").append(params.get(key)).append("' AND ");
        }
        String clause = stringBuilder.substring(0, stringBuilder.lastIndexOf("AND"));
    }
}
