package com.example.tarek.inventoreyapp.views;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.database.table.ProductContract.ProductEntry;
import com.example.tarek.inventoreyapp.presenter.ProductDbQuery;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CatalogActivity extends AppCompatActivity {

    @BindView(R.id.label_count)
    TextView labelCount;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private static final String TAG = CatalogActivity.class.getSimpleName();
    private static final String DELETED_MSG = " rows are been Deleted ";
    private static final String NOT_DELETED_MSG = " Db is empty and there is no row to delete ";
    private static final String SEPARATOR = " - ";
    private static final String NEW_LINE = "\n";
    private static final int NO_ROWS = 0 ;
    private static final int QUANTITY_VALUE = 50 ;
    private static final int PRICE_VALUE = 50 ;


    private ProductDbQuery productDbQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        ButterKnife.bind(this);
        setUI();

        productDbQuery = new ProductDbQuery(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayData(productDbQuery.queryData());
    }

    /**
     * to get query and get all data from it's cursor then display them
     */
    private void displayData(Cursor cursor) {

        int indexProductId = cursor.getColumnIndex(ProductEntry._ID);
        int indexProductName = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int indexProductPrice = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int indexQuantity = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int indexSupplierName = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
        int indexSupplierPhone = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE);

        try {
            labelCount.setText(String.format(getString(R.string.count_msg), cursor.getCount()));
            labelCount.append(ProductEntry._ID + SEPARATOR +
                    ProductEntry.COLUMN_PRODUCT_NAME + SEPARATOR +
                    ProductEntry.COLUMN_PRODUCT_PRICE + SEPARATOR +
                    ProductEntry.COLUMN_PRODUCT_QUANTITY + SEPARATOR +
                    ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + SEPARATOR +
                    ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE + NEW_LINE);

            while (cursor.moveToNext()) {
                int index = cursor.getInt(indexProductId);
                //long newRowId = cursor.getPosition();
                String productName = cursor.getString(indexProductName).trim();
                float productPrice = Float.parseFloat((cursor.getString(indexProductPrice)));
                int quantity = cursor.getInt(indexQuantity);
                String supplierName = cursor.getString(indexSupplierName).trim();
                String supplierPhone = cursor.getString(indexSupplierPhone).trim();

                labelCount.append((NEW_LINE + index + SEPARATOR +
                        productName + SEPARATOR +
                        productPrice + SEPARATOR +
                        quantity + SEPARATOR +
                        supplierName + SEPARATOR +
                        supplierPhone));
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calatlog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Cursor cursor = productDbQuery.queryData();
        switch (id) {
            case R.id.action_insert_dummy_data:
                productDbQuery.insertDummyData();
                break;
            case R.id.action_display_ordered_by_name:
                cursor = productDbQuery.queryDataOrderedBy(ProductEntry.COLUMN_PRODUCT_NAME);
                break;
            case R.id.action_display_select_price_more_than:
                cursor = productDbQuery.queryDataFromWhereOrderByName(ProductEntry.COLUMN_PRODUCT_PRICE, PRICE_VALUE);
                break;
            case R.id.action_display_select_quantity_more_than:
                cursor = productDbQuery.queryDataFromWhereOrderByName(ProductEntry.COLUMN_PRODUCT_QUANTITY, QUANTITY_VALUE);
                break;
            case R.id.action_delete_all_data:
                int deletedRows = productDbQuery.deleteAll();
                Toast.makeText(this, deletedRows + DELETED_MSG, Toast.LENGTH_LONG).show();
                break;
            case R.id.action_delete_last_row:
                if (productDbQuery.getCountRows() > NO_ROWS) {
                    productDbQuery.deleteFromWhere(productDbQuery.getCountRows());
                } else {
                    Toast.makeText(this, NOT_DELETED_MSG, Toast.LENGTH_LONG).show();
                }
                break;
        }

        displayData(cursor);

        return super.onOptionsItemSelected(item);
    }

    public void setUI() {

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openEditorActivity = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(openEditorActivity);
            }
        });
    }
}
