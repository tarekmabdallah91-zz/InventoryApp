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
package com.example.tarek.inventoreyapp.sync;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.tarek.inventoreyapp.data.ProductContract.ProductEntry;
import com.example.tarek.inventoreyapp.presenter.ProductDbQuery;
import com.example.tarek.inventoreyapp.utils.ConstantsUtils;

public class ProductCursorLoader extends AsyncTaskLoader<Cursor> implements ConstantsUtils {

    private static final String TAG = ProductCursorLoader.class.getSimpleName();
    private final ContentResolver contentResolver;
    private final ProductDbQuery productDbQuery;

    private final Bundle bundle;

    public ProductCursorLoader(Context context, ContentResolver contentResolver, Bundle bundle) {
        super(context);
        this.contentResolver = contentResolver;
        this.bundle = bundle;
        productDbQuery = new ProductDbQuery(context);
    }

    @Nullable
    @Override
    public Cursor loadInBackground() {
        int fragmentItemPosition = bundle.getInt(FRAGMENT_ITEM_POSITION);
        String sortOrSearchValue = bundle.getString(SORT_OR_SEARCH_PREFERENCE_KEY); // the 1st choice from the user
        String orderByValue = bundle.getString(ORDER_BY_PREFERENCE_KEY); // if he choose to sort item, get the key to sort them
        String inputText = bundle.getString(SEARCHED_INPUT_TEXT_PREFERENCE_KEY); // if he want to search for text or numbers
        String mode = bundle.getString(MODE); // keyword to differ between the db operations CURD
        String[] projection = productDbQuery.projection(); // column names , if we want all columns it can be null instead of typing all columns names
        String selection = null; // WHERE selection
        String[] selectionArgs = null; // WHERE selection =? / <=? / >=? selectionArgs

        final int ZERO = 0;
        final int ONE = 1;
        final int TWO = 2;
        final int THREE = 3;
        try {
            String args1stElement;
            String args2ndElement = null;
            String[] parts;
            String selection2;
            if (null != sortOrSearchValue) {
                switch (fragmentItemPosition) {
                    case ZERO:
                        selection = null; // must be empty
                        args1stElement = null;
                        break;
                    case ONE:
                        selection = ProductEntry.COLUMN_PRODUCT_SYNCED + SIGN_ID;
                        args1stElement = String.valueOf(ONE);
                        break;
                    case TWO:
                        selection = ProductEntry.COLUMN_PRODUCT_SYNCED + SIGN_ID;
                        args1stElement = String.valueOf(ZERO);
                        break;
                    case THREE:
                    default:
                        selection = ProductEntry.COLUMN_PRODUCT_FAVORED + SIGN_ID;
                        args1stElement = String.valueOf(ONE);
                }
                if (!ORDER_BY.equals(sortOrSearchValue)) {
                    if (sortOrSearchValue.contains(MINUS)) { //user search for numbers  price/quantity - more/less than
                        parts = sortOrSearchValue.split(SPLITTER_REGEX_MINUS); // column name - more/less than
                        selection2 = parts[ZERO]; // column name
                        if (parts[ONE].contains(MORE_THAN)) {
                            selection2 += MORE_THAN_SIGN; // column name >=?
                        } else if (parts[ONE].contains(LESS_THAN)) {
                            selection2 += LESS_THAN_SIGN; // column name <=?
                        }
                        if (null == selection && null != selection2) selection = selection2;
                        else if (null != selection)
                            selection = selection + selection2; //&& null != selection2
                        //an other case will be by default as: else if (null != selection && null == selection2) selection = selection;
                        args2ndElement = String.valueOf(Integer.parseInt(inputText)); // input is integer not string
                    } else { // user search for texts
                        String columnName = sortOrSearchValue.split(SPLITTER_REGEX_COLUMN_NAME, TEN)[ONE];
                        selection2 = columnName + LIKE + SEARCH_TEXT_1ST_SINGLE_QUOTE + inputText + SEARCH_TEXT_2ND_SINGLE_QUOTE;
                        args2ndElement = null;
                        if (null == selection) selection = selection2;
                        else selection = selection + AND /*" AND*/ + selection2;
                    }
                }

                if (null == args1stElement && null != args2ndElement) {
                    selectionArgs = new String[]{args2ndElement};
                } else if (null != args1stElement && null == args2ndElement) {
                    selectionArgs = new String[]{args1stElement};
                } else if (null != args1stElement) { //&& null != args2ndElement) {
                    selectionArgs = new String[]{args1stElement, args2ndElement};
                } else { // each of them is null
                    selectionArgs = null;
                }
            }
            //if (QUERY.equals(mode)) is the default case as the cursor is reloading whatever any value of the mode
            if (INSERT_DUMMY_ITEM.equals(mode)) {
                productDbQuery.insertData(null);
            } else if (DELETE_ALL_DATA.equals(mode)) {
                // TODO - It should delete only the items in the current fragment but it delete all items from db
                // although the selection and selectionArgs is the same as the shown data !!
                contentResolver.delete(ProductEntry.CONTENT_URI, selection, selectionArgs);
            } else if (null != mode && mode.contains(DELETE_ITEM)) {
                int id = Integer.parseInt(mode.replaceAll(
                        REGEX_TO_GET_INTEGER_ONLY_FROM_STRING, EMPTY_STRING));
                productDbQuery.deleteById(id);
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString() + e.getMessage() + e.getCause() + e.getLocalizedMessage());
            return null;
        }
        return contentResolver.query(ProductEntry.CONTENT_URI,
                projection, selection, selectionArgs, orderByValue);
    }
}
