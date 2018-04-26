package com.example.tarek.inventoreyapp.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.presenter.ProductDbQuery;
import com.example.tarek.inventoreyapp.database.product.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorActivity extends AppCompatActivity {

    @BindView(R.id.product_name)
    EditText productName ;
    @BindView(R.id.product_price)
    EditText productPrice ;
    @BindView(R.id.quantity)
    EditText quantity ;
    @BindView(R.id.supplier_name)
    EditText supplierName ;
    @BindView(R.id.supplier_phone)
    EditText supplierPhone ;

    private ProductDbQuery productDbQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);

        productDbQuery = new ProductDbQuery(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save){
            productDbQuery.insertData(getEnteredData());
        }else if (id == R.id.action_insert_dummy_data){
            productDbQuery.insertDummyData();
        }
        finish(); // to go to parent activity and display data by onStart() method ;
        return super.onOptionsItemSelected(item);
    }

    private Product getEnteredData(){
        String productName = this.productName.getText().toString().trim();
        float productPrice = Float.parseFloat(this.productPrice.getText().toString().trim());
        int productQuantity = Integer.parseInt(this.quantity.getText().toString().trim());
        String supplierName = this.supplierName.getText().toString().trim();
        String supplierPhone = this.supplierPhone.getText().toString().trim();

        return  new Product(productName , productPrice , productQuantity , supplierName , supplierPhone);
    }
}
