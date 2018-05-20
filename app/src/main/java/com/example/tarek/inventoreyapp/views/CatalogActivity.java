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

package com.example.tarek.inventoreyapp.views;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.adpater.ProductCursorAdapter;
import com.example.tarek.inventoreyapp.database.contract.ProductContract.ProductEntry;
import com.example.tarek.inventoreyapp.presenter.ProductDbQuery;
import com.example.tarek.inventoreyapp.utils.ProductUtility;
import com.example.tarek.inventoreyapp.utils.ScreenSizeUtility;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    // --Commented out by Inspection (18/05/2018 01:37 ص):private static final String TAG = CatalogActivity.class.getSimpleName();

    @BindView(R.id.warn_image)
    ImageView warnImage;
    @BindView(R.id.btn_show_data)
    Button showData;
    @BindView(R.id.input_number)
    EditText inputNumber;
    @BindView(R.id.input_name)
    EditText inputText;
    @BindView(R.id.count_rows_text_view)
    TextView countRowsTextView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.empty_text_view)
    TextView emptyTextView;
    @BindView(R.id.list)
    ListView listView;
    @BindView(R.id.spinner_ordered_by)
    Spinner spinnerOrderBy;
    @BindView(R.id.spinner_order_search)
    Spinner spinnerOrderOrSearch;
    @BindString(R.string.invalid_input_user_catalog_activity)
    String invalidInput;
    @BindString(R.string.error_text)
    String invalidName;
    @BindString(R.string.empty_text_msg)
    String emptyTextMsg;
    @BindString(R.string.catalog_label)
    String catalogLabel;
    @BindString(R.string.put_value_text_msg)
    String putValueMsg;
    @BindString(R.string.no_data_for_input_value_msg)
    String noDataForInputValue;
    @BindString(R.string.spinner_label_order_by)
    String spinnerLabelOrderBy;
    @BindString(R.string.spinner_label_order_by_default)
    String labelOrderByDefault;
    @BindString(R.string.spinner_label_id)
    String labelId;
    @BindString(R.string.action_delete_all_items)
    String deleteAllItems;
    @BindString(R.string.all_items_deleted)
    String allItemsDeletedMsg;
    @BindString(R.string.action_insert_dummy_item)
    String insertDummyItem;
    @BindString(R.string.discard)
    String discardMsg;
    @BindString(R.string.delete_all_items_dialog_msg)
    String deleteAllItemsMsg;
    @BindInt(R.integer.price_max_length)
    int priceMaxLength;
    @BindInt(R.integer.text_min_length)
    int textMinLength;
    @BindInt(R.integer.text_max_length)
    int textMaxLength;
    private String mode;

    private int lastId; // max size of db - to get last id in the table

    private ScreenSizeUtility screenSizeUtility;
    private ProductDbQuery productDbQuery;
    private ProductUtility productUtility;
    private CursorAdapter cursorAdapter;
    private boolean defaultLoaderStateChanged = false;
    private String selection;
    private String moreOrLessSign;
    private String sortOrder;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calatlog, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        ButterKnife.bind(this);

        productUtility = new ProductUtility();
        productDbQuery = new ProductDbQuery(this);
        screenSizeUtility = new ScreenSizeUtility(this);
        setUI();

        getLoaderManager().initLoader(ProductUtility.ZERO, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_insert_dummy_data:
                setMode(insertDummyItem);
                break;
            case R.id.action_delete_all_data:
                setMode(deleteAllItems);
                break;
        }
        restartLoaderOnQueryBundleChange(null);

        return super.onOptionsItemSelected(item);
    }

    /**
     * to set all views
     */
    private void setUI() {
        setListView();
        setSpinnerOrderBy();
        setSpinnerOrderOrSearch();
        setFab();
        setInputFieldVisibility(View.GONE); // to hide it tell user want to enter some values to show certain queries
        setViewsMode(productUtility.DEFAULT_MODE); // as default mode
    }

    /**
     * to show discard msg if the user clicked back,up or save  buttons
     */
    private void showDiscardMsg(String msg, String discard, String doSomeThing) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton(discard, getDialogInterfaceOnClickListener(discard));
        builder.setNegativeButton(doSomeThing, getDialogInterfaceOnClickListener(doSomeThing));

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private DialogInterface.OnClickListener getDialogInterfaceOnClickListener(String usage) {
        if (usage.equals(discardMsg)) {
            return new DialogInterface.OnClickListener() {
                // user want to edit
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            };
        } else if (usage.equals(deleteAllItems)) { // delete all items (used in CatalogActivity)
            return new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int deletedRows = productDbQuery.deleteAll();
                    Toast.makeText(getBaseContext(), deletedRows + allItemsDeletedMsg, Toast.LENGTH_LONG).show();
                }
            };
        } else { // discard case - user want to exit
            return new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            };
        }
    }

    /**
     * to set the spinnerOrderBy and it's cursorAdapter
     */
    private void setSpinnerOrderBy() {
        final ArrayAdapter<CharSequence> adapterSpinnerOrderBy =
                ArrayAdapter.createFromResource(this, R.array.default_order_by, android.R.layout.simple_spinner_item);

        adapterSpinnerOrderBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderBy.setAdapter(adapterSpinnerOrderBy);

        spinnerOrderBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String itemName = (String) adapterSpinnerOrderBy.getItem(position);
                String[] parts;
                if (mode.equals(spinnerLabelOrderBy) || mode.equals(moreOrLessSign)) {// mode order by
                    if (itemName.contains(productUtility.MINUS)) { // like : price/quantity - more / less than
                        parts = itemName.split(productUtility.SPLITTER_REGEX_MINUS);
                        selection = parts[ProductUtility.ZERO];
                        if (parts[ProductUtility.ONE].contains(productUtility.MORE_THAN)) {
                            sortOrder = selection + productUtility.ASC;
                            moreOrLessSign = productUtility.MORE_THAN_SIGN;
                        } else if (parts[ProductUtility.ONE].contains(productUtility.LESS_THAN)) {
                            sortOrder = selection + productUtility.DESC;
                            moreOrLessSign = productUtility.LESS_THAN_SIGN;
                        }
                        setViewsMode(moreOrLessSign);
                        selection = selection + moreOrLessSign;// to add sign to selection whatever was it's case
                        setEmptyTextView(putValueMsg);
                        defaultLoaderStateChanged = true;
                    } else { // like : id , name , quantity ,  price/quantity ASC/DESC
                        if (itemName.equals(labelOrderByDefault)) {
                            // just to prevent restarting
                            // the loader after changing the mode 1st time while the activity still just started
                            defaultLoaderStateChanged = false;
                            sortOrder = null;
                        } else {
                            defaultLoaderStateChanged = true;
                            sortOrder = itemName;
                            setViewsMode(spinnerLabelOrderBy);
                        }
                        if (defaultLoaderStateChanged) {

                            //  must be here to restart the loader if user clicked on Id Item
                            //  and load the default cursor then get last ID while defaultLoaderStateChanged = false;
                            //  and here is the right place for this lines
                            if (itemName.equals(labelId)) {
                                defaultLoaderStateChanged = false;
                                sortOrder = null;
                            }
                            restartLoaderOnQueryBundleChange(productUtility.getBundle(null, null, sortOrder));
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * to set the setSpinnerOrderOrSearch and it's cursorAdapter
     */
    private void setSpinnerOrderOrSearch() {
        final ArrayAdapter<CharSequence> adapter_spinner_order_search =
                ArrayAdapter.createFromResource(this, R.array.order_search, android.R.layout.simple_spinner_item);

        adapter_spinner_order_search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderOrSearch.setAdapter(adapter_spinner_order_search);

        spinnerOrderOrSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String itemName = (String) adapter_spinner_order_search.getItem(position);
                setViewsMode(itemName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * to set cursor = null and msg for the text view
     *
     * @param msg to be set to the emptyTextView
     */
    private void setEmptyTextView(String msg) {
        cursorAdapter.swapCursor(null);
        warnImage.setImageResource(R.drawable.icons8_error_96_2);
        emptyTextView.setText(msg);
    }

    /**
     * to hide/show views according to the case of mode
     *
     * @param mode as switch between cases
     */
    private void setViewsMode(String mode) {
        setMode(mode);
        if (mode.equals(productUtility.DEFAULT_MODE) || mode.equals(spinnerLabelOrderBy)) {
            spinnerOrderBy.setVisibility(View.VISIBLE);
            inputNumber.setVisibility(View.GONE);
            inputText.setVisibility(View.GONE);
            showData.setVisibility(View.GONE);
        } else if (mode.equals(moreOrLessSign)) {
            spinnerOrderBy.setVisibility(View.VISIBLE);
            inputNumber.setVisibility(View.VISIBLE);
            inputText.setVisibility(View.GONE);
            showData.setVisibility(View.VISIBLE);
            setEmptyTextView(noDataForInputValue);
        } else { // search for name/category/code
            spinnerOrderBy.setVisibility(View.GONE);
            inputNumber.setVisibility(View.GONE);
            inputText.setVisibility(View.VISIBLE);
            showData.setVisibility(View.VISIBLE);
            setEmptyTextView(noDataForInputValue);
        }
    }

    private void setMode(String mode) {
        this.mode = mode;
    }

    @OnClick(R.id.btn_show_data)
    void onClickShowData() {
        String[] result;
        String[] selectionArgs;
        String errorMsg;
        if (mode.equals(moreOrLessSign)) {// get input number from user
            result = productUtility.checkEnteredNumbers(productUtility.getTextEditText(inputNumber));
            selectionArgs = new String[]{result[ProductUtility.ONE]};
            errorMsg = String.format(invalidInput, priceMaxLength);
        } else { // get input text from user
            result = productUtility.checkEnteredTextToSearch(productUtility.getTextEditText(inputText));
            /* EDIT selection here because SQL SELECTION must be expression contains something like "=?"
              and in this case there is no any expression make the statement is correctly read  , so to avoid this error *
              Caused by: android.database.sqlite.SQLiteException: near "LIKE": syntax error (code 1): ,while compiling: SELECT * FROM Products WHERE Name LIKE
              add the input value to the selection directly to be as : WHERE name LIKE '%(inputValue)%'
              and must make selection args = null to avoid any code interfaces  */
            String columnName = mode.split(productUtility.SPLITTER_REGEX_COLUMN_NAME,
                    productUtility.TEN)[ProductUtility.ONE];
            selection = columnName + productUtility.LIKE + result[ProductUtility.ONE];
            selectionArgs = null;
            sortOrder = columnName; // to be ordered by the column name
            errorMsg = String.format(invalidName, ProductEntry.COLUMN_PRODUCT_NAME, textMinLength, textMaxLength);
        }
        String flag = result[ProductUtility.ZERO];
        if (flag.equals(productUtility.TRUE)) {
            productUtility.showToastMsg(getBaseContext(), errorMsg);
        } else {
            Bundle args = productUtility.getBundle(
                    selection,
                    selectionArgs,
                    sortOrder);
            restartLoaderOnQueryBundleChange(args);
        }
    }

    /**
     * to set fab and toolbar
     */
    private void setFab() {
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openEditorActivity = new Intent(CatalogActivity.this, EditorActivity.class);
                openEditorActivity.putExtra(productUtility.LAST_ID_KEY, lastId);
                startActivity(openEditorActivity);
            }
        });
    }

    /**
     * to set list view and it's cursorAdapter
     */
    private void setListView() {
        cursorAdapter = new ProductCursorAdapter(this, null);
        int paddingSidesValue = ProductUtility.ZERO;
        int paddingBottomValue = screenSizeUtility.getHeight() / productUtility.FOUR;

        listView.setEmptyView(emptyTextView);
        listView.setPadding(paddingSidesValue, paddingSidesValue, paddingSidesValue, paddingBottomValue);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editProduct = new Intent(CatalogActivity.this, EditorActivity.class);
                editProduct.setData(ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id));
                startActivity(editProduct);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader;
        String[] projection = productDbQuery.projection();
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        if (args == null) {
            if (mode.equals(insertDummyItem)) {
                productDbQuery.insertData(null); // to insert dummy values
            } else if (mode.equals(deleteAllItems)) {
                showDiscardMsg(deleteAllItemsMsg, discardMsg, deleteAllItems);
            }
        } else {
            selection = args.getString(productUtility.BUNDLE_KEY_SELECTION);
            selectionArgs = args.getStringArray(productUtility.BUNDLE_KEY_SELECTION_ARGS);
            sortOrder = args.getString(productUtility.BUNDLE_KEY_SORT_ORDER);

        }
        cursorLoader = new CursorLoader(this, ProductEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() > ProductUtility.ZERO) {
            cursorAdapter.swapCursor(data);
            if (!defaultLoaderStateChanged) {// to get last id just on the default case when loads all data only
                if (data.moveToLast()) {
                    lastId = data.getInt(data.getColumnIndex(ProductEntry._ID));
                }
            }
            countRowsTextView.setText(String.format(catalogLabel, data.getCount()));
            warnImage.setImageResource(ProductUtility.ZERO);
            emptyTextView.setVisibility(View.GONE);
            return;
        }

        cursorAdapter.swapCursor(null);
        listView.setEmptyView(emptyTextView);
        countRowsTextView.setText(String.format(catalogLabel, data.getCount()));
        warnImage.setImageResource(R.drawable.icons8_empty_box_64);
        emptyTextView.setText(emptyTextMsg);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    /**
     * to set Loader Manager with variable Bundle args EASILY in spinnerOrderBy's cases
     *
     * @param args it's value is used to change Loader<Cursor> which return from onCreateLoader
     *             to change the displayed values on the screen depending on the query parameters
     */
    private void restartLoaderOnQueryBundleChange(Bundle args) {
        getLoaderManager().restartLoader(ProductUtility.ZERO, args, this);
    }

    /**
     * to set EditText as false as default state till user choose  price/quantity  more/less than value
     * <p>
     * when the focus changed and not be on the edit text
     * it take the input value and check it then return (it's Integer value as String
     * to be passed to selectionArgs String[] as 1st element) if it wasn't null
     * or showing Toast msg to warn him to enter valid number
     */
    private void setInputFieldVisibility(int visibility) {
        inputNumber.setVisibility(visibility); // as default state till user choose  price/quantity  more/less than value
        inputText.setVisibility(visibility);
        showData.setVisibility(visibility);
    }
}
