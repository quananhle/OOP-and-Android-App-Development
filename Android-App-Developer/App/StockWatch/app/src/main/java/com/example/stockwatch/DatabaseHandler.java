package com.example.stockwatch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    }
}
