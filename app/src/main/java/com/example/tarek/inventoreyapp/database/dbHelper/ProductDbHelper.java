/*
Copyright 2018 tarekmabdallah91@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.example.tarek.inventoreyapp.database.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tarek.inventoreyapp.database.contract.ProductContract.ProductEntry;


public class ProductDbHelper extends SQLiteOpenHelper {

    private static final String Db_NAME = "shelter.db";
    private static final int Db_VERSION = 1;

    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String AUTOINCREMENT = " AUTOINCREMENT";
    private static final String TEXT = " TEXT";
    private static final String INTEGER = " INTEGER";
    private static final String BLOB = " BLOB";
    private static final String NOT_NULL = " NOT NULL";
    private static final String DEFAULT = " DEFAULT ";
    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ")";
    private static final String COMA = ",";
    private static final String SEMI_COLUMN = ";";
    private static final int ZERO = 0;
    public ProductDbHelper(Context context) {
        super(context, Db_NAME, null, Db_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_PRODUCT =
                CREATE_TABLE + ProductEntry.TABLE_NAME + OPEN_BRACKET +
                        ProductEntry._ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMA +
                        ProductEntry.COLUMN_PRODUCT_NAME + TEXT + NOT_NULL + COMA +
                        ProductEntry.COLUMN_PRODUCT_CODE + TEXT + NOT_NULL + COMA +
                        ProductEntry.COLUMN_PRODUCT_CATEGORY + TEXT + NOT_NULL + COMA +
                        ProductEntry.COLUMN_PRODUCT_PRICE + INTEGER + NOT_NULL + COMA +
                        ProductEntry.COLUMN_PRODUCT_QUANTITY + INTEGER + NOT_NULL + DEFAULT + ZERO + COMA +
                        ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + TEXT + NOT_NULL + COMA +
                        ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE + TEXT + NOT_NULL + COMA +
                        ProductEntry.COLUMN_PRODUCT_DESCRIPTION + TEXT + COMA +
                        ProductEntry.COLUMN_PRODUCT_IMAGE1 + BLOB + COMA +
                        ProductEntry.COLUMN_PRODUCT_IMAGE2 + BLOB + COMA +
                        ProductEntry.COLUMN_PRODUCT_IMAGE3 + BLOB + CLOSE_BRACKET + SEMI_COLUMN;

        db.execSQL(CREATE_TABLE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
