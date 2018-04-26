package com.example.tarek.inventoreyapp.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.tarek.inventoreyapp.database.dbHelper.ProductDbHelper;
import com.example.tarek.inventoreyapp.database.table.ProductContract;
import com.example.tarek.inventoreyapp.database.table.ProductContract.ProductEntry;
import com.example.tarek.inventoreyapp.database.product.Product;
import com.example.tarek.inventoreyapp.R;

import java.util.Random;

public class ProductDbQuery {

    private static final String ORDER_BY = "SELECT * FROM Products ORDER BY %s ";
    private static final String SELECT_FROM_WHERE = "SELECT * FROM Products WHERE %s >= %d ORDER BY %s";
    private static final String DELETE_FROM_WHERE = "DELETE FROM Products WHERE %s = %d ";
    private static final String DUMMY_PRODUCT_NAME = "product";
    private static final String DUMMY_SUPPLIER_NAME = "name";
    private static final String DUMMY_SUPPLIER_PHONE = "01234";
    private static final float DUMMY_PRODUCT_PRICE = 0.99f;
    private static final int LIMIT_RANDOM_VALUE = 100;
    private static int countRows ;

    private final Context context;
    private final ProductDbHelper dbHelper;

    private static final int NO_ROWS = -1 ;

    public ProductDbQuery(Context context){
        this.context = context;
        dbHelper = new ProductDbHelper(context);
    }

    public void insertData(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = getProductValues (product);

        long newRowId = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == NO_ROWS) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(
                    context,
                    context.getResources().getString(R.string.error_with_saving_product),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(context,
                    context.getResources().getString(R.string.product_saved_with_row)+ newRowId,
                    Toast.LENGTH_SHORT).show();
        }
        countRows =  (int) newRowId;
    }

    /**
     * to insert dummy data to our database for testing it
     */
    public void insertDummyData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Random random = new Random();
        int randomValue = random.nextInt(LIMIT_RANDOM_VALUE); // to change dummy data values

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, DUMMY_PRODUCT_NAME + randomValue);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, DUMMY_PRODUCT_PRICE + randomValue);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, randomValue);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, DUMMY_SUPPLIER_NAME + randomValue);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, DUMMY_SUPPLIER_PHONE + randomValue);

        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);

        countRows =  (int) newRowId;
    }

    /**
     * as default method to set query
     * @return cursor contains Table name and columns not adjust by any column
     */
    public Cursor queryData(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
                ProductEntry.TABLE_NAME,
                columnsNames(),
                null,
                null,
                null,
                null,
                null);
    }

    /**
     * method to set query with ordered by choice and limit
     * @return cursor contains Table name and columns not adjust by any column
     */
    public Cursor queryDataFromWhereOrderByName(String columnName , int value){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(String.format(SELECT_FROM_WHERE,columnName,value , ProductEntry.COLUMN_PRODUCT_NAME ), null);
    }

    /**
     * method to delete all data
     * @return number of deleted rows
     */
    public int deleteAll( ){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.delete(ProductEntry.TABLE_NAME , ProductEntry._ID , null);
    }

    /**
     * method to delete by selected value
     */
    public void deleteFromWhere(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL(String.format(DELETE_FROM_WHERE , ProductEntry._ID ,id));
    }

    /**
     * method to set query with ordered by
     * @return cursor contains Table name and columns not adjust by any column
     */
    public Cursor queryDataOrderedBy(String columnName ){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(String.format(ORDER_BY ,columnName ), null);
    }

    /**
     * get columns names from Contract Class
     * @return columns names in String[] to be passed to cursor
     */
    private static String[] columnsNames (){
        return new String[] {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE,};
    }

    /**
     * to fetch data from Product's object then set them in Content value object
     * @param product to extract it's data
     * @return ContentValue contains values wanted to be inserted
     */
    private ContentValues getProductValues (Product product){
        String productName = product.getProductName();
        float productPrice = product.getProductPrice();
        int productQuantity = product.getProductQuantity();
        String supplierName = product.getSupplierName();
        String supplierPhone = product.getSupplierPhone();

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, supplierPhone);

        return values;
    }

    public int getCountRows() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        countRows = (int) db.getMaximumSize();
        return countRows;
    }
}
