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
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.database.contract.ProductContract.ProductEntry;
import com.example.tarek.inventoreyapp.presenter.ProductDbQuery;
import com.example.tarek.inventoreyapp.utils.ImageUtility;
import com.example.tarek.inventoreyapp.utils.ProductUtility;

import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.error_msg_name)
    TextView productNameMsg;
    @BindView(R.id.error_msg_code)
    TextView productCodeMsg;
    @BindView(R.id.error_msg_category)
    TextView productCategoryMsg;
    @BindView(R.id.error_msg_price)
    TextView productPriceMsg;
    @BindView(R.id.error_msg_quantity)
    TextView productQuantityMsg;
    @BindView(R.id.error_msg_supplier_name)
    TextView productSupplierNameMsg;
    @BindView(R.id.error_msg_supplier_phone)
    TextView productSupplierPhoneMsg;

    @BindView(R.id.input_name)
    EditText productName;
    @BindView(R.id.input_code)
    EditText productCode;
    @BindView(R.id.input_category)
    EditText productCategory;
    @BindView(R.id.input_price)
    EditText productPrice;
    @BindView(R.id.input_quantity)
    EditText productQuantity;
    @BindView(R.id.input_supplier_name)
    EditText productSupplierName;
    @BindView(R.id.input_supplier_phone)
    EditText productSupplierPhone;
    @BindView(R.id.input_description)
    EditText productDescription;
    @BindView(R.id.image1)
    ImageView productImage1;
    @BindView(R.id.image2)
    ImageView productImage2;
    @BindView(R.id.image3)
    ImageView productImage3;

    @BindString(R.string.details_product)
    String detailsProduct;
    @BindString(R.string.update_product)
    String updateProduct;
    @BindString(R.string.add_product)
    String addProduct;
    @BindString(R.string.action_delete_last_row)
    String deleteProduct;
    @BindString(R.string.unsaved_changes_dialog_msg)
    String unsavedChangesMsg;
    @BindString(R.string.save_changes_dialog_msg)
    String saveChangesMsg;
    @BindString(R.string.discard)
    String discardMsg;
    @BindString(R.string.keep_editing)
    String keepMsg;
    @BindString(R.string.save_changes)
    String saveMsg;
    @BindString(R.string.error_text)
    String errorText;
    @BindString(R.string.error_price)
    String errorPrice;
    @BindString(R.string.error_quantity)
    String errorQuantity;
    @BindString(R.string.error_supp_phone)
    String errorSuppPhone;

    @BindDimen(R.dimen.item_image_size)
    int IMAGE_SIZE;
    @BindInt(R.integer.text_min_length)
    int textMinLength;
    @BindInt(R.integer.text_max_length)
    int textMaxLength;


    private ProductDbQuery productDbQuery;
    private Bitmap bitmapImage1, bitmapImage2, bitmapImage3;
    private int updatingId;
    private boolean productTouched;
    private MenuItem saveOrEdit;
    private MenuItem deleteThisItem;
    private MenuItem fillWithDummyData;
    private String mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);

        setOnTouchListenerForEditTextViews();
        initiateValues();
    }

    /**
     * to initiate variables
     */
    private void initiateValues() {
        bitmapImage1 = null;
        bitmapImage2 = null;
        bitmapImage3 = null;
        productDbQuery = new ProductDbQuery(this);
    }

    /**
     * to set OnTouchListener For EditTextViews
     */
    private void setOnTouchListenerForEditTextViews() {
        productName.setOnTouchListener(changeTagToTrueIfClicked());
        productCode.setOnTouchListener(changeTagToTrueIfClicked());
        productCategory.setOnTouchListener(changeTagToTrueIfClicked());
        productPrice.setOnTouchListener(changeTagToTrueIfClicked());
        productQuantity.setOnTouchListener(changeTagToTrueIfClicked());
        productSupplierName.setOnTouchListener(changeTagToTrueIfClicked());
        productSupplierPhone.setOnTouchListener(changeTagToTrueIfClicked());
        productDescription.setOnTouchListener(changeTagToTrueIfClicked());
        productImage1.setOnTouchListener(changeTagToTrueIfClicked());
        productImage2.setOnTouchListener(changeTagToTrueIfClicked());
        productImage3.setOnTouchListener(changeTagToTrueIfClicked());
    }

    /**
     * set view's tag to 0 as false or to 1 as true if the object was been clicked
     *
     * @return true
     */
    private View.OnTouchListener changeTagToTrueIfClicked() {

        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                productTouched = true;
                return false;
            }
        };
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

    /**
     * to ask user if he sure to leave the page (if he update some values)
     * and go back if there isn't any changes
     */
    @Override
    public void onBackPressed() {
        if (!productTouched) { // productTouched = false (no changes) then go back
            super.onBackPressed();
            return;
        } // else productTouched = true; then ask the user

        showDiscardMsg(unsavedChangesMsg, discardMsg, keepMsg);
    }

    private DialogInterface.OnClickListener getDialogInterfaceOnClickListener(String usage) {
        if (usage.equals(saveMsg)) {
            return new DialogInterface.OnClickListener() {
                // user want to save changes and update the product
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int done = productDbQuery.update(getEnteredData(),
                            ProductEntry._ID + ProductUtility.SIGN_ID, updatingId);
                    if (done != ProductUtility.INVALID) {
                        restartLoaderOnQueryBundleChange(); // to update in background thread
                    }
                }
            };
        } else if (usage.equals(keepMsg)) {
            return new DialogInterface.OnClickListener() {
                // user want to edit
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
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
     * to get incoming intents if not null
     */
    private void getComingIntents() {
        Intent comingIntent = getIntent();
        // display mode
        if (comingIntent.getData() != null) {
            Uri uri = comingIntent.getData();
            updatingId = (int) ContentUris.parseId(uri);
            if (mode == null) { // as initial mode and to not be changed again during using
                setMode(detailsProduct);
                // initiate loader here because of the order of initiating method here is :
                // onCreate then >> onPrepareOptionsMenu and I want to initiate the loader after getting the
                // intent and setting the mode
                getLoaderManager().initLoader(ProductUtility.ZERO, null, this);
            }
        } else { // insert mode // if there isn't extra with this Key name it will set lastId = ZERO
            int lastId = comingIntent.getIntExtra(CatalogActivity.getLastIdKey(), ProductUtility.INVALID);
            if (lastId >= ProductUtility.ZERO) {
                // to get lastId from intent then show count+1 with the title
                updatingId = ++lastId;
                setMode(addProduct);
            }
        }
    }

    /**
     * to set title and menu items as mode is  display/edit/add ?
     *
     * @param title          resource int for action bar title
     * @param iconResourceId resource int for icon
     * @param enableEditing  edit texts or not ? and setVisibility of fillWithDummyData
     */
    private void setViewsAsMode(String title, int iconResourceId, boolean enableEditing, boolean showItemDummy) {
        mode = title;
        setTitle(mode + updatingId);
        saveOrEdit.setIcon(iconResourceId);
        enableViews(enableEditing);
        fillWithDummyData.setVisible(showItemDummy);
    }

    /**
     * to change mode as coming intents
     *
     * @param modeResourceId resource id for current mode
     */
    private void setMode(String modeResourceId) {
        if (modeResourceId.equals(detailsProduct)) {
            setViewsAsMode(detailsProduct, R.drawable.icons8_edit_96, false, false);
        } else if (modeResourceId.equals(updateProduct)) {
            setViewsAsMode(updateProduct, R.drawable.icons8_checkmark_96_2, true, false);
            productImage1.setVisibility(View.VISIBLE);
            productImage2.setVisibility(View.VISIBLE);
            productImage3.setVisibility(View.VISIBLE);
        } else { // add new product
            setViewsAsMode(addProduct, R.drawable.icons8_checkmark_96_2, true, true);
            deleteThisItem.setVisible(false);
        }
    }

    /**
     * to make editing disabled
     */
    private void enableViews(boolean enable) {
        productName.setEnabled(enable);
        productCode.setEnabled(enable);
        productCategory.setEnabled(enable);
        productPrice.setEnabled(enable);
        productQuantity.setEnabled(enable);
        productSupplierName.setEnabled(enable);
        productSupplierPhone.setEnabled(enable);
        productDescription.setEnabled(enable);
        productImage1.setEnabled(enable);
        productImage2.setEnabled(enable);
        productImage3.setEnabled(enable);
    }

    /**
     * to change mode of the activity depending on user's click on CatalogActivity
     * if the user clicked on an item will be converted to "show details" mode & pencil to edit the product if needed
     * if the user clicked on the pencil it will be change to "correct sign" to be able to save edits "Update product" mode
     * or if the user clicked on fab , he will be converted to "Add product" mode and the ability to save new product
     *
     * @param menu .
     * @return .
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        saveOrEdit = menu.findItem(R.id.action_save);
        deleteThisItem = menu.findItem(R.id.action_delete_this_item);
        fillWithDummyData = menu.findItem(R.id.action_insert_dummy_data);
        getComingIntents();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save: // R.id.action_save:
                ContentValues values = getEnteredData();
                if (mode.equals(detailsProduct)) {
                    // if the mode was details mode then change it to updating mode
                    setMode(updateProduct);
                } else if (mode.equals(updateProduct)) {
                    if (ProductUtility.noNullValues(values)) {
                        // to update ... where id = new String[]{String.valueOf(ContentUris.parseId(uri))}
                        if (productTouched) { // productTouched = false (no changes) then go back
                            showDiscardMsg(saveChangesMsg, saveMsg, keepMsg);
                        } else {
                            finish();
                        }
                    } else { // as if user choose keep then keep in this page and don't move to CatalogActivity
                        showWarnsMsgToUser(values);
                    }
                } else if (mode.equals(addProduct)) {
                    if (ProductUtility.noNullValues(values)) {
                        restartLoaderOnQueryBundleChange();  // to add item in background thread
                    } else {
                        showWarnsMsgToUser(values);
                    }
                }
                break;
            case R.id.action_insert_dummy_data:
                fillFieldsWithDummyValues();
                break;
            case R.id.action_delete_this_item:
                setMode(deleteProduct);
                restartLoaderOnQueryBundleChange();  // to delete item in background thread
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * to generate dummy values and set them to the empty Edit Text fields
     */
    private void fillFieldsWithDummyValues() {
        String[] dummyValues = ProductUtility.getDummyStringValues();
        String text;
        String[] result = ProductUtility.checkIfFieldIsNull(productName, ProductUtility.TEXT);
        if (result[ProductUtility.ZERO].equals(ProductUtility.TRUE)) {
            text = result[ProductUtility.TWO] + dummyValues[ProductUtility.ZERO];
            productName.setText(text);
        }
        result = ProductUtility.checkIfFieldIsNull(productCode, ProductUtility.TEXT);
        if (result[ProductUtility.ZERO].equals(ProductUtility.TRUE)) {
            text = result[ProductUtility.TWO] + dummyValues[ProductUtility.ONE];
            productCode.setText(text);
        }
        result = ProductUtility.checkIfFieldIsNull(productCategory, ProductUtility.TEXT);
        if (result[ProductUtility.ZERO].equals(ProductUtility.TRUE)) {
            text = result[ProductUtility.TWO] + dummyValues[ProductUtility.TWO];
            productCategory.setText(text);
        }
        result = ProductUtility.checkIfFieldIsNull(productPrice, ProductUtility.NUMERIC);
        if (result[ProductUtility.ZERO].equals(ProductUtility.TRUE)) {
            text = result[ProductUtility.TWO] + dummyValues[ProductUtility.THREE];
            productPrice.setText(text);
        }
        result = ProductUtility.checkIfFieldIsNull(productQuantity, ProductUtility.NUMERIC);
        if (result[ProductUtility.ZERO].equals(ProductUtility.TRUE)) {
            text = result[ProductUtility.TWO] + dummyValues[ProductUtility.FOUR];
            productQuantity.setText(text);
        }
        result = ProductUtility.checkIfFieldIsNull(productSupplierName, ProductUtility.TEXT);
        if (result[ProductUtility.ZERO].equals(ProductUtility.TRUE)) {
            text = result[ProductUtility.TWO] + dummyValues[ProductUtility.FIVE];
            productSupplierName.setText(text);
        }
        result = ProductUtility.checkIfFieldIsNull(productSupplierPhone, ProductUtility.PHONE);
        if (result[ProductUtility.ZERO].equals(ProductUtility.TRUE)) {
            text = result[ProductUtility.TWO] + dummyValues[ProductUtility.SIX];
            productSupplierPhone.setText(text);
        }
        result = ProductUtility.checkIfFieldIsNull(productDescription, ProductUtility.LONG_TEXT);
        text = result[ProductUtility.TWO] + dummyValues[ProductUtility.SEVEN];
        productDescription.setText(text);
    }

    /**
     * to fetch data from Product's object then set them in Content value object
     * if any value doesn't match regex and size it will be set null
     * then will be ignored by db check and warn the user to check it again
     *
     * @return ContentValue contains values wanted to be inserted
     */
    private ContentValues getEnteredData() {
        ContentValues values = new ContentValues();

        String[] result = ProductUtility.checkIfFieldIsNull(productName, ProductUtility.TEXT);
        if (result[ProductUtility.ZERO].equals(ProductUtility.FALSE)) {
            values.put(ProductEntry.COLUMN_PRODUCT_NAME, result[ProductUtility.ONE]);
        }
        result = ProductUtility.checkIfFieldIsNull(productCode, ProductUtility.TEXT);
        if (result[ProductUtility.ZERO].equals(ProductUtility.FALSE)) {
            values.put(ProductEntry.COLUMN_PRODUCT_CODE, result[ProductUtility.ONE]);
        }
        result = ProductUtility.checkIfFieldIsNull(productCategory, ProductUtility.TEXT);
        if (result[ProductUtility.ZERO].equals(ProductUtility.FALSE)) {
            values.put(ProductEntry.COLUMN_PRODUCT_CATEGORY, result[ProductUtility.ONE]);
        }
        result = ProductUtility.checkIfFieldIsNull(productPrice, ProductUtility.NUMERIC);
        if (result[ProductUtility.ZERO].equals(ProductUtility.FALSE)) {
            values.put(ProductEntry.COLUMN_PRODUCT_PRICE, Integer.parseInt(result[ProductUtility.ONE]));
        }
        result = ProductUtility.checkIfFieldIsNull(productQuantity, ProductUtility.NUMERIC);
        if (result[ProductUtility.ZERO].equals(ProductUtility.FALSE)) {
            values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, Integer.parseInt(result[ProductUtility.ONE]));
        }
        result = ProductUtility.checkIfFieldIsNull(productSupplierName, ProductUtility.TEXT);
        if (result[ProductUtility.ZERO].equals(ProductUtility.FALSE)) {
            values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, result[ProductUtility.ONE]);
        }
        result = ProductUtility.checkIfFieldIsNull(productSupplierPhone, ProductUtility.PHONE);
        if (result[ProductUtility.ZERO].equals(ProductUtility.FALSE)) {
            values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, result[ProductUtility.ONE]);
        }
        result = ProductUtility.checkIfFieldIsNull(productDescription, ProductUtility.LONG_TEXT);
        values.put(ProductEntry.COLUMN_PRODUCT_DESCRIPTION, result[ProductUtility.ONE]);// could be null

        byte[] blobImage; // it can be sorted as null in the database
        blobImage = ImageUtility.bitmapToBytes(bitmapImage1);
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE1, blobImage);

        blobImage = ImageUtility.bitmapToBytes(bitmapImage2);
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE2, blobImage);

        blobImage = ImageUtility.bitmapToBytes(bitmapImage3);
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE3, blobImage);

        return values;
    }

    private void showWarnsMsgToUser(ContentValues values) {

        if (!values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME))
            productNameMsg.setText(String.format(errorText
                    , ProductEntry.COLUMN_PRODUCT_NAME, textMinLength, textMaxLength));

        if (!values.containsKey(ProductEntry.COLUMN_PRODUCT_CATEGORY))
            productCategoryMsg.setText(String.format(errorText,
                    ProductEntry.COLUMN_PRODUCT_CATEGORY, textMinLength, textMaxLength));

        if (!values.containsKey(ProductEntry.COLUMN_PRODUCT_CODE))
            productCodeMsg.setText(String.format(errorText,
                    ProductEntry.COLUMN_PRODUCT_CODE, textMinLength, textMaxLength));

        if (!values.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE))
            productPriceMsg.setText(errorPrice);

        if (!values.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY))
            productQuantityMsg.setText(errorQuantity);

        if (!values.containsKey(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME))
            productSupplierNameMsg.setText((String.format(errorText,
                    ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, textMinLength, textMaxLength)));

        if (!values.containsKey(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE))
            productSupplierPhoneMsg.setText(errorSuppPhone);
    }

    @OnClick(R.id.image1)
    void onClickImage1() {
        intentToPickImageFromGallery(ProductUtility.ONE);
    }

    @OnClick(R.id.image2)
    void onClickImage2() {
        intentToPickImageFromGallery(ProductUtility.TWO);
    }

    @OnClick(R.id.image3)
    void onClickImage3() {
        intentToPickImageFromGallery(ProductUtility.THREE);
    }

    /**
     * intent to get image from the gallery for the @param clickedImage
     */
    private void intentToPickImageFromGallery(int clickedImage) {
        Intent pickImageFromGallery = new Intent(Intent.ACTION_PICK); //  , Uri.parse("images/*")
        pickImageFromGallery.setType(ProductUtility.INTENT_TYPE_IMAGE);
        startActivityForResult(pickImageFromGallery, clickedImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // I used the requested code as an assigned value to each image previously as Image1 = 1 etc
        if (requestCode == ProductUtility.ONE ||
                requestCode == ProductUtility.TWO ||
                requestCode == ProductUtility.THREE) {

            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Bitmap imageBitmap = ImageUtility.uriToBitmap(this, uri, IMAGE_SIZE);
                setClickedImageView(requestCode, imageBitmap);
            }
        }

    }

    /**
     * to set bitmap for clicked image view
     *
     * @param clickedImage code 1,2 or 3
     * @param imageBitmap  the value will be set to the image
     */
    private void setClickedImageView(int clickedImage, Bitmap imageBitmap) {
        ImageView imageView;
        switch (clickedImage) {
            case 2:
                imageView = productImage2;
                bitmapImage2 = imageBitmap;
                break;
            case 3:
                imageView = productImage3;
                bitmapImage3 = imageBitmap;
                break;
            case 1:
            default:
                imageView = productImage1;
                bitmapImage1 = imageBitmap;
        }
        imageView.setImageBitmap(imageBitmap);
    }

    /**
     * to get String from cursor
     *
     * @param cursor     that contains the data
     * @param columnName to get it's index then get it's value
     * @return string value of the column name
     */
    private String getStringFromCursor(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    /**
     * to get String from cursor
     *
     * @param cursor     that contains the data
     * @param columnName to get it's index then get it's value
     * @return string value of the column name
     */
    private byte[] getBlobFromCursor(Cursor cursor, String columnName) {
        return cursor.getBlob(cursor.getColumnIndex(columnName));
    }

    /**
     * to show imageViews set by the retrieved Blob value from the cursor if not null else GONE
     *
     * @param updatingRow cursor contains the data of the clicked item
     */
    private void showImagesIfNotNull(Cursor updatingRow) {
        byte[] imageBytes = getBlobFromCursor(updatingRow, ProductEntry.COLUMN_PRODUCT_IMAGE1);
        int imageResourceId = R.drawable.click_to_add_image;
        if (imageBytes != null) {
            bitmapImage1 = ImageUtility.byteArrayToBitmap(imageBytes);
            productImage1.setImageBitmap(bitmapImage1);
            productImage1.setVisibility(View.VISIBLE);
        } else {
            productImage1.setImageResource(imageResourceId);
            productImage1.setVisibility(View.INVISIBLE);
        }
        imageBytes = getBlobFromCursor(updatingRow, ProductEntry.COLUMN_PRODUCT_IMAGE2);
        if (imageBytes != null) {
            bitmapImage2 = ImageUtility.byteArrayToBitmap(imageBytes);
            productImage2.setImageBitmap(bitmapImage2);
            productImage2.setVisibility(View.VISIBLE);
        } else {
            productImage2.setImageResource(imageResourceId);
            productImage2.setVisibility(View.INVISIBLE);
        }
        imageBytes = getBlobFromCursor(updatingRow, ProductEntry.COLUMN_PRODUCT_IMAGE3);
        if (imageBytes != null) {
            bitmapImage3 = ImageUtility.byteArrayToBitmap(imageBytes);
            productImage3.setImageBitmap(bitmapImage3);
            productImage3.setVisibility(View.VISIBLE);
        } else {
            productImage3.setImageResource(imageResourceId);
            productImage3.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * loader used here to display details of wanted product by id
     * or to update data or to insert data in background thread
     * to avoid loading if mode was insert new product
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        int done = ProductUtility.INVALID;
        // if (mode.equals(detailsProduct)) {
        // moved this lines to the end of this method to be a general case loaded for any case
        // because if choose "return null" it force the loader to repeat loading twice
        // which not needed here , all needed to load only once for each case
        // return new CursorLoader( // display current id only this,ProductEntry.CONTENT_URI,
        //  null, ProductEntry._ID + ProductUtility.SIGN_ID, new String[]{String.valueOf(updatingId)}, null);
        // end of this comment  } else
        if (mode.equals(updateProduct)) { // mode = update product
            done = productDbQuery.update(getEnteredData(),
                    ProductEntry._ID + ProductUtility.SIGN_ID, updatingId);

        } else if (mode.equals(addProduct)) { // mode = add product
            if (productDbQuery.insertData(getEnteredData())) done = ProductUtility.VALID;
        } else if (mode.equals(deleteProduct)) { // case delete
            done = productDbQuery.deleteById(updatingId);
        }
        if (done != ProductUtility.INVALID) {
            finish();
        }
        return new CursorLoader( // display current id only
                this,
                ProductEntry.CONTENT_URI,
                null,
                ProductEntry._ID + ProductUtility.SIGN_ID,
                new String[]{String.valueOf(updatingId)},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // load the clicked product's data if it was available (if the mode is details product )
        if (mode.equals(detailsProduct)) {
            Cursor updatingRow = productDbQuery.getRowById(updatingId);

            if (updatingRow.moveToFirst()) { // to avoid index -1 in the cursor

                productName.setText(getStringFromCursor(
                        updatingRow, ProductEntry.COLUMN_PRODUCT_NAME));
                productCode.setText(getStringFromCursor(
                        updatingRow, ProductEntry.COLUMN_PRODUCT_CODE));
                productCategory.setText(getStringFromCursor(
                        updatingRow, ProductEntry.COLUMN_PRODUCT_CATEGORY));
                productPrice.setText(getStringFromCursor(
                        updatingRow, ProductEntry.COLUMN_PRODUCT_PRICE));
                productQuantity.setText(getStringFromCursor(
                        updatingRow, ProductEntry.COLUMN_PRODUCT_QUANTITY));
                productSupplierName.setText(getStringFromCursor(
                        updatingRow, ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME));
                productDescription.setText(getStringFromCursor(
                        updatingRow, ProductEntry.COLUMN_PRODUCT_DESCRIPTION));
                productSupplierPhone.setText(getStringFromCursor(
                        updatingRow, ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE));
                showImagesIfNotNull(updatingRow); // to show images if not null

            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    /**
     * to set Loader Manager with variable Bundle args EASILY in spinnerOrderBy's cases
     * args it's value is used to change Loader<Cursor> which return from onCreateLoader
     * to change the displayed values on the screen depending on the query parameters
     * but here not needed so it always = null
     */
    private void restartLoaderOnQueryBundleChange() {
        getLoaderManager().restartLoader(ProductUtility.ZERO, null, this);
    }
}
