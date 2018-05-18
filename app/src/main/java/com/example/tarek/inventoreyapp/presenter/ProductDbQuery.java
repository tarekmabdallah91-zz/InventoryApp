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

package com.example.tarek.inventoreyapp.presenter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.database.contract.ProductContract.ProductEntry;
import com.example.tarek.inventoreyapp.utils.ProductUtility;

// class link between database resolverContent and views activities

public class ProductDbQuery {

    private final Context context;
    // --Commented out by Inspection (18/05/2018 01:37 ص):private static final String LOG_TAG = ProductDbQuery.class.getSimpleName();

    public ProductDbQuery(Context context) {
        this.context = context;
    }

    /**
     * method to set query to the content resolver
     *
     * @return cursor contains wanted row id
     */
    public Cursor getRowById(int id) {
        return context.getContentResolver().query(
                ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id)
                , null, //select * from table where _ID =? id
                ProductEntry._ID + ProductUtility.SIGN_ID, new String[]{String.valueOf(id)}, null);
    }

    /**
     * method to set query with ordered by
     *
     * @return cursor contains Table name and columns projection() with selection =? args ordered by sortOrder
     */
    private Cursor queryDataByColumnName(String[] projection) {
        Uri uri;
        uri = ProductEntry.CONTENT_URI;
        return context.getContentResolver().query(uri, projection, null, null, null);
    }

    /**
     * when user update product if category or code match one int db then show toast msg to tell him that
     *
     * @param values to check it's category and code
     * @return -1 if repeated or 1 is not repeated
     */
    private int ifCategoryOrCodeRepeatedShowToast(ContentValues values, int id) {
        // if current id != matched id AND matched id != -1 , means that the category/code repeated
        int matchedId = categoryOrCodeRepeated(values, ProductEntry.COLUMN_PRODUCT_CODE);
        if (matchedId != ProductUtility.INVALID && matchedId != id) {
            ProductUtility.showToastMsg(context,
                    context.getString(R.string.repeated_input_value, ProductEntry.COLUMN_PRODUCT_CODE, matchedId));
            return ProductUtility.INVALID;
        }
        matchedId = categoryOrCodeRepeated(values, ProductEntry.COLUMN_PRODUCT_CATEGORY);
        if (matchedId != ProductUtility.INVALID && matchedId != id) {
            ProductUtility.showToastMsg(context,
                    context.getString(R.string.repeated_input_value, ProductEntry.COLUMN_PRODUCT_CATEGORY, matchedId));
            return ProductUtility.INVALID;
        }
        return ProductUtility.VALID;
    }

    /**
     * check category and code contained in @param values are found or not
     *
     * @param values     to check it's code/category
     * @param columnName to be dynamic for code/category column
     * @return true if repeated or false if  not repeated
     */
    private int categoryOrCodeRepeated(ContentValues values, String columnName) {
        return (DatabaseContainsText(
                queryDataByColumnName(new String[]{ProductEntry._ID, columnName}), // cursor of _ID & product category/code
                columnName, // to get it's String value from the cursor
                values.getAsString(columnName))); // current String value by key @param columnName
        // returns -1 if not found or matched id if found
    }

    /**
     * to get unrepeated dummyValue for code / category
     *
     * @param type       code/category
     * @param columnName code/category
     * @return unrepeated dummyValue for code / category
     */
    private String getUniqueCategoryOrCode(String columnName, String type) {
        String text = type + ProductUtility.getRandomValue();// like code120
        int count = ProductUtility.ZERO; // for iterations
        while (count < ProductUtility.LIMIT_RANDOM_VALUE && DatabaseContainsText(
                queryDataByColumnName(new String[]{columnName}),
                columnName, text) == ProductUtility.INVALID) { // get all text in this column to be compared with the random text
            text = type + ProductUtility.getRandomValue();
            count++;
        }
        if (count == ProductUtility.LIMIT_RANDOM_VALUE) {
            text = ProductUtility.SIGN_ID; // as Invalid data to br rejected in the next step
        }
        return text;
    }

    /**
     * to get dummy values and check if category and code are unique or not
     * if one of them or both are repeated then will ask to got a unique one
     * if the program can't got a random unique value then it will be set as null
     *
     * @return values may be have data for all columns or not so there is will be another check for it's size
     */
    private ContentValues getNewDummyValues() {
        /*  ==========================================( these lines  just for dummy data )==========+==============================
         *  if user want to insert dummy data to database directly means that values = null so generate a dummy values
         *  category and code of products cannot be repeated they must be unique values
         *  check if the random value is matched to category/code in database then repeat the iteration till ProductUtility.LIMIT_RANDOM_VALUE
         *  if it still repeat the category / code so set the text with  SIGN_ID as just a placeholder data
         *  it text = SIGN_ID show toast msg tell the user that cannot insert this product because it is repeated ,
         *  return ProductUtility.INVALID
         */
        ContentValues values = ProductUtility.getDummyContentValues();  // to insert dummy values
        String text;
        if (categoryOrCodeRepeated(values, ProductEntry.COLUMN_PRODUCT_CODE) != ProductUtility.INVALID) { // check code repeated or not
            text = getUniqueCategoryOrCode(ProductEntry.COLUMN_PRODUCT_CODE, values.getAsString(ProductEntry.COLUMN_PRODUCT_CODE));
            if (!text.equals(ProductUtility.SIGN_ID))
                values.put(ProductEntry.COLUMN_PRODUCT_CODE, text);
        }
        if (categoryOrCodeRepeated(values, ProductEntry.COLUMN_PRODUCT_CATEGORY) != ProductUtility.INVALID) { // check category repeated or not
            text = getUniqueCategoryOrCode(ProductEntry.COLUMN_PRODUCT_CATEGORY, values.getAsString(ProductEntry.COLUMN_PRODUCT_CATEGORY));
            if (!text.equals(ProductUtility.SIGN_ID))
                values.put(ProductEntry.COLUMN_PRODUCT_CATEGORY, text);
        }
        return values;
    }

    /**
     * to insert data to the content resolver
     */
    public boolean insertData(ContentValues values) {
        if (values == null) values = getNewDummyValues();

        if (values.size() < ProductUtility.NOT_NULLABLE_COLUMNS_COUNT) return false;

        if (ifCategoryOrCodeRepeatedShowToast(values, ProductUtility.INVALID) == ProductUtility.INVALID)
            return false;

        Uri newRowUri = context.getContentResolver().insert(ProductEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowUri == null) {
            // If the row ID is -1, then there was an error with insertion.
            ProductUtility.showToastMsg(context,
                    context.getResources().getString(R.string.error_with_saving_product));
            return false;
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            ProductUtility.showToastMsg(context,
                    context.getResources().getString(R.string.product_saved_with_row) + ContentUris.parseId(newRowUri));
            return true;
        }
    }

    public int update(ContentValues values, String selection, int id) {
        // if the user changed category / code to one which already in the database return ProductUtility.INVALID;
        if (ifCategoryOrCodeRepeatedShowToast(values, id) == ProductUtility.INVALID) {
            return ProductUtility.INVALID;
        }
        Uri uri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
        return context.getContentResolver().update(uri, values, selection, new String[]{String.valueOf(id)});
    }

    /**
     * method to delete all data
     *
     * @return number of deleted rows
     */
    public int deleteAll() {
        return context.getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);
    }

    /**
     * method to delete  row by id
     */
    public int deleteById(int id) {
        return context.getContentResolver().delete(ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id) // uri of id path
                , ProductEntry._ID + ProductUtility.SIGN_ID, new String[]{String.valueOf(id)});

    }

    /**
     * the columns names wanted to retrieve from db
     * @return String[] contains required columns names
     */
    public String[] projection() {
        return new String[]{ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_CODE,
                ProductEntry.COLUMN_PRODUCT_CATEGORY,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_IMAGE1};
    }

    /**
     * to get all String values from the db (@param cursor)
     * @param columnName to query data depending ot it
     * @param text       wanted to be check if already in the db or not
     * @param cursor     db query to get all values from it
     * @return "id matched" if found ProductName / ProductCode in the db or -1 if not found it .
     */
    private int DatabaseContainsText(Cursor cursor, String columnName, String text) {
        while (cursor.moveToNext()) {
            // if there is matched value then get it's id
            if (text.equals(cursor.getString(cursor.getColumnIndex(columnName)))) {
                return cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
            }
        }
        return ProductUtility.INVALID;
    }
}
