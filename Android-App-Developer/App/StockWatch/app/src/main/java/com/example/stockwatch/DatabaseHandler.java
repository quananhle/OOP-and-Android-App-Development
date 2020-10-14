package com.example.stockwatch;

import android.database.sqlite.SQLiteOpenHelper;

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

}
