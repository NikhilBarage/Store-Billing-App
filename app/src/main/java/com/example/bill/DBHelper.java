package com.example.bill;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;


    // Table name and column names1
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY = "quantity";

    // Create table SQL query
    private static final String PRODUCTS_CREATE = "create table "
            + TABLE_PRODUCTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_PRICE
            + " real not null, " + COLUMN_QUANTITY
            + " integer not null);";


    // Table name and column names2
    public static final String TABLE_ORDERS = "orders";

    // Columns
    public static final String COLUMN_ORDESID = "orderId";
    public static final String COLUMN_ORDESNAME = "orderN";
    public static final String COLUMN_ORDESPRICE = "orderP";
    public static final String COLUMN_ORDESQUANTITY = "orderQ";
    public static final String COLUMN_ORDESDATE = "orderD";

    // Create table query
    private static final String ORDER_CREATE =
            "create table " + TABLE_ORDERS + " (" +
                    COLUMN_ORDESID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDESNAME + " TEXT NOT NULL, " +
                    COLUMN_ORDESPRICE + " REAL NOT NULL, " +
                    COLUMN_ORDESQUANTITY + " REAL NOT NULL, " +
                    COLUMN_ORDESDATE + " TEXT NOT NULL);";


    // Table name and column names1
    public static final String TABLE_ADMIN = "login";
    public static final String COLUMN_LOG_ID = "_iid";
    public static final String COLUMN_LOG_NAME = "username";
    public static final String COLUMN_LOG_PASS = "password";


    // Create table query
    private static final String LOG_CREATE =
            "create table " + TABLE_ADMIN + " (" +
                    COLUMN_LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LOG_NAME + " TEXT NOT NULL, " +
                    COLUMN_LOG_PASS + " TEXT NOT NULL);";


    // Create table SQL query
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(PRODUCTS_CREATE);
        database.execSQL(ORDER_CREATE);
        database.execSQL(LOG_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        onCreate(db);
    }

}