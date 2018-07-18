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

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.data.ProductContract.ProductEntry;
import com.example.tarek.inventoreyapp.presenter.ProductDbQuery;
import com.example.tarek.inventoreyapp.utils.ConstantsUtils;

public class ProductCursorLoader extends AsyncTaskLoader<Cursor> implements ConstantsUtils {

    private static final String TAG = ProductCursorLoader.class.getSimpleName();
    private final ContentResolver contentResolver;
    private final ProductDbQuery productDbQuery;

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final Bundle bundle;

    public ProductCursorLoader(Context context, ContentResolver contentResolver, Bundle bundle) {
        super(context);
        this.contentResolver = contentResolver;
        this.bundle = bundle;
        this.context = context;
        productDbQuery = new ProductDbQuery(context);
    }

    @Nullable
    @Override
    public Cursor loadInBackground() {
        int fragmentItemPosition = bundle.getInt(FRAGMENT_ITEM_POSITION);
        String sortOrSearchValue = bundle.getString(SORT_OR_SEARCH_PREFERENCE_KEY);
        String orderByValue = bundle.getString(ORDER_BY_PREFERENCE_KEY);
        String inputText = bundle.getString(SEARCHED_INPUT_TEXT_PREFERENCE_KEY);
        String mode = bundle.getString(MODE);
        String[] projection = productDbQuery.projection();
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = orderByValue;

        final int ZERO = 0;
        final int ONE = 1;
        final int TWO = 2;
        final int THREE = 3;
        try {
            switch (fragmentItemPosition) {
                case ZERO:
                    selection = null;
                    selectionArgs = null;
                    break;
                case ONE:
                    selection = ProductEntry.COLUMN_PRODUCT_SYNCED + SIGN_ID;
                    selectionArgs = new String[]{String.valueOf(ONE)};
                    break;
                case TWO:
                    selection = ProductEntry.COLUMN_PRODUCT_SYNCED + SIGN_ID;
                    selectionArgs = new String[]{String.valueOf(ZERO)};
                    break;
                case THREE:
                default:
                    selection = ProductEntry.COLUMN_PRODUCT_FAVORED + SIGN_ID;
                    selectionArgs = new String[]{String.valueOf(ONE)};
            }

            if (QUERY.equals(mode)) {
                String args1stElement;
                String args2ndElement;
                String[] parts;
                String selection1;
                String selection2;
                if (null != sortOrSearchValue) {
                    if (!sortOrSearchValue.equals(context.getString(R.string.value_order_by))) {
                        switch (fragmentItemPosition) {
                            case ZERO:
                                selection1 = null; // must be empty
                                args1stElement = null;
                                break;
                            case ONE:
                                selection1 = ProductEntry.COLUMN_PRODUCT_SYNCED + SIGN_ID;
                                args1stElement = String.valueOf(ONE);
                                break;
                            case TWO:
                                selection1 = ProductEntry.COLUMN_PRODUCT_SYNCED + SIGN_ID;
                                args1stElement = String.valueOf(ZERO);
                                break;
                            case THREE:
                            default:
                                selection1 = ProductEntry.COLUMN_PRODUCT_FAVORED + SIGN_ID;
                                args1stElement = String.valueOf(ONE);
                        }
                        if (sortOrSearchValue.contains(MINUS)) { //user search for numbers  price/quantity - more/less than
                            parts = sortOrSearchValue.split(SPLITTER_REGEX_MINUS); // column name - more/less than
                            selection2 = parts[ZERO]; // column name
                            if (parts[ONE].contains(MORE_THAN)) {
                                sortOrder = selection2 + ASC;
                                selection2 += MORE_THAN_SIGN; // column name >=?
                            } else if (parts[ONE].contains(LESS_THAN)) {
                                sortOrder = selection2 + DESC;
                                selection2 += LESS_THAN_SIGN; // column name <=?
                            }
                            args2ndElement = String.valueOf(Integer.parseInt(inputText)); // input is integer not string
                        } else { // user search for texts
                            String columnName = sortOrSearchValue.split(SPLITTER_REGEX_COLUMN_NAME, TEN)[ONE];
                            selection2 = columnName + LIKE + inputText;
                            sortOrder = columnName; // to be ordered by the column name
                            args2ndElement = null;
                        }

                        if (null == selection1) selection = selection2;
                        else selection = selection1 + AND + selection2;

                        if (null == args1stElement && null != args2ndElement) {
                            selectionArgs = new String[]{args2ndElement};
                        } else if (null == args2ndElement && null != args1stElement) {
                            selectionArgs = new String[]{args1stElement};
                        } else if (null != args1stElement) {
                            selectionArgs = new String[]{args1stElement, args2ndElement};
                        } else {
                            selectionArgs = null;
                        }
                    }
                }
            } else if (INSERT_DUMMY_ITEM.equals(mode)) {
                productDbQuery.insertData(null);
            } else if (DELETE_ALL_DATA.equals(mode)) {
                contentResolver.delete(ProductEntry.CONTENT_URI, selection, selectionArgs);
            } else if (null != mode && mode.contains(DELETE_ITEM)) {
                int id = Integer.parseInt(mode.replaceAll(
                        REGEX_TO_GET_INTEGER_ONLY_FROM_STRING, EMPTY_STRING));
                productDbQuery.deleteById(id);
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return contentResolver.query(ProductEntry.CONTENT_URI,
                projection, selection, selectionArgs, sortOrder);
    }
}
