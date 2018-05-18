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

package com.example.tarek.inventoreyapp.database.contract;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ProductContract {

    public static final String CONTENT_AUTHORITY = "com.example.tarek.inventoreyapp.database.contentProvider";
    public static final String PRODUCT_PATH = "Products";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class ProductEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PRODUCT_PATH);

        public static final String PRODUCT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PRODUCT_PATH;
        public static final String PRODUCT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PRODUCT_PATH;

        public static final String TABLE_NAME ="Products";

        public static final String _ID =BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME ="Name";
        public static final String COLUMN_PRODUCT_CODE = "Code";
        public static final String COLUMN_PRODUCT_CATEGORY = "Category";
        public static final String COLUMN_PRODUCT_PRICE ="Price";
        public static final String COLUMN_PRODUCT_QUANTITY ="Quantity";
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME ="SuppName";
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE ="SuppPhone";
        public static final String COLUMN_PRODUCT_DESCRIPTION = "Description";
        public static final String COLUMN_PRODUCT_IMAGE1 = "Image1";
        public static final String COLUMN_PRODUCT_IMAGE2 = "Image2";
        public static final String COLUMN_PRODUCT_IMAGE3 = "Image3";

    }

}
