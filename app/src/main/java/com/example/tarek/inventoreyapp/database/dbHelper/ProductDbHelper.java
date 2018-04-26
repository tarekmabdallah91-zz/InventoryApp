package com.example.tarek.inventoreyapp.database.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tarek.inventoreyapp.database.table.ProductContract.ProductEntry;

public class ProductDbHelper extends SQLiteOpenHelper {

    private static final String Db_NAME = "shelter.db";
    private static final int Db_VERSION = 1;

    public ProductDbHelper(Context context) {
        super(context, Db_NAME, null, Db_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_PRODUCT =
                "CREATE TABLE " + ProductEntry.TABLE_NAME + " ( " +
                        ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL," +
                        ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL," +
                        ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0," +
                        ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " TEXT NOT NULL," +
                        ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE + " TEXT NOT NULL);";
        Log.e("query" , CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
