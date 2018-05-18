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

package com.example.tarek.inventoreyapp.database.contentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.database.contract.ProductContract;
import com.example.tarek.inventoreyapp.database.contract.ProductContract.ProductEntry;
import com.example.tarek.inventoreyapp.database.dbHelper.ProductDbHelper;
import com.example.tarek.inventoreyapp.utils.ProductUtility;


public class ProductProvider extends ContentProvider {

    private static final String LOG_TAG = ProductProvider.class.getSimpleName();


    private static final int PRODUCT = 100; // code for table path uri
    private static final int PRODUCT_ID = 101; // code for table's rows uri
    // to create 2 uris od table and rows and add them to MATCHER object
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PRODUCT_PATH, PRODUCT);

        MATCHER.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PRODUCT_PATH + "/#", PRODUCT_ID);
    }

    private ProductDbHelper dbHelper; // object link between database and contentProvider

    @Override
    public boolean onCreate() {
        dbHelper = new ProductDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // get readable database to retrieve data to display it

        int match = MATCHER.match(uri); // to know which uri the MATCHER is carrying ?
        Cursor cursor; // cursor which will contains the database / row as the MATCHER gets table or row uri
        switch (match) {
            case PRODUCT:
                cursor = db.query(
                        ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null, sortOrder);
                break;
            case PRODUCT_ID:
                cursor = db.query(
                        ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(LOG_TAG + getContext().getString(R.string.error_displaying_data) + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = MATCHER.match(uri);

        if (match == PRODUCT) {
            return ProductEntry.PRODUCT_LIST_TYPE;
        } else { // if match == PRODUCT_ID
            return ProductEntry.PRODUCT_ITEM_TYPE;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = MATCHER.match(uri);

        if (match == PRODUCT_ID) {
            throw new IllegalArgumentException(LOG_TAG + getContext().getString(R.string.error_inserting_data) + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return insertData(uri, values);
    }

    /**
     * to insert the data to the table
     *
     * @param uri    of the table
     * @param values which well be inserted
     * @return newRowUri
     */
    private Uri insertData(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // get the writable db to insert the data
        int newRowId = (int) db.insert(ProductEntry.TABLE_NAME, null, values);
        if (newRowId == ProductUtility.INVALID) { // if not inserted throw this error
            throw new IllegalArgumentException(LOG_TAG + getContext().getString(R.string.error_inserting_data) + uri);
        }
        // else return new uri with the id of inserted data
        return Uri.withAppendedPath(uri, String.valueOf(newRowId));
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = MATCHER.match(uri);
        int deletedRows;
        switch (match) {
            case PRODUCT:
                deletedRows = db.delete(ProductEntry.TABLE_NAME, null, null); // to delete all rows but still increment according to last id
                break;
            case PRODUCT_ID:
                deletedRows = db.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.error_deleting_data) + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = MATCHER.match(uri);

        if (match == PRODUCT) {
            // because update only working if take row id not table names
            throw new IllegalArgumentException(LOG_TAG + getContext().getString(R.string.error_updating_data) + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectRows = db.update(ProductEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return affectRows;
    }

}
