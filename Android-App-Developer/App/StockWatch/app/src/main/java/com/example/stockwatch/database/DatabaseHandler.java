package com.example.stockwatch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.stockwatch.MainActivity;
import com.example.stockwatch.helper.Stock;

import java.util.ArrayList;

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

    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COMPANY + " TEXT not null, " +
            SYMBOL + " TEXT not null unique, " +
            PRICE + " TEXT not null, " +
            CHANGE + " TEXT not null, " +
            PERCENT_CHANGE + " TEXT not null)";

    private SQLiteDatabase database;

    public DatabaseHandler(Context context){
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    public ArrayList<Stock> loadStocks(){
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
                String price = cursor.getString(2);
                String change = cursor.getString(3);
                String percentChange = cursor.getString(4);
                Stock s = new Stock(company, symbol, Double.parseDouble(price),
                        Double.parseDouble(change), Double.parseDouble(percentChange));
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
    public void deleteStock(String symbol){
        Log.d(TAG, "deleteStock: Deleting Stock " + symbol);
        int cnt = database.delete(TABLE_NAME, SYMBOL + " = ?", new String[]{symbol});
        Log.d(TAG, "deleteStock: " + cnt);
    }
    public void shutDown(){
        database.close();
    }
//    public void findStock(HashMap<String, String> params){
//        Log.d(TAG, "findBook: ");
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String key : params.keySet()){
//            stringBuilder.append(key).append(" = '").append(params.get(key)).append("' AND ");
//        }
//        String clause = stringBuilder.substring(0, stringBuilder.lastIndexOf("AND"));
//        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
//                clause, null);
//        if (cursor != null){
//            cursor.moveToFirst();
//            if (cursor.getCount() > 0){
//                String company = cursor.getString(0);
//                String symbol = cursor.getString(1);
//                String currPrice = cursor.getString(2);
//                String priceChange = cursor.getString(3);
//                String percentChange = cursor.getString(4);
//                Stock s = new Stock(company, symbol, Double.parseDouble(currPrice),
//                        Double.parseDouble(priceChange), Double.parseDouble(percentChange));
//                mainActivity.showFindResults(s);
//            }
//            else {
//                mainActivity.showFindResults(null);
//            }
//            cursor.close();
//        }
//    }
}
