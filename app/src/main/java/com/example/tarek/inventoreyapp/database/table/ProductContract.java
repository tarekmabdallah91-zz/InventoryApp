package com.example.tarek.inventoreyapp.database.table;

import android.provider.BaseColumns;

public class ProductContract {

    public ProductContract(){}

    public static class ProductEntry implements BaseColumns {

        public static final String TABLE_NAME ="Products";

        public static final String _ID =BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME ="Name";
        public static final String COLUMN_PRODUCT_PRICE ="Price";
        public static final String COLUMN_PRODUCT_QUANTITY ="Quantity";
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME ="SuppName";
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE ="SuppPhone";



    }

}
