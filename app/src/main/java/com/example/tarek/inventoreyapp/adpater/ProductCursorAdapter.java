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

package com.example.tarek.inventoreyapp.adpater;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.database.contract.ProductContract.ProductEntry;
import com.example.tarek.inventoreyapp.utils.ImageUtility;
import com.example.tarek.inventoreyapp.utils.ProductUtility;

public class ProductCursorAdapter extends CursorAdapter {

    private final ProductUtility productUtility;
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        productUtility = new ProductUtility();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View rowView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder();
        itemViewHolder.productId = rowView.findViewById(R.id.item_id);
        itemViewHolder.productName = rowView.findViewById(R.id.item_name);
        itemViewHolder.productCode = rowView.findViewById(R.id.item_code);
        itemViewHolder.productCategory = rowView.findViewById(R.id.item_category);
        itemViewHolder.productPrice = rowView.findViewById(R.id.item_price);
        itemViewHolder.productQuantity = rowView.findViewById(R.id.item_quantity);
        itemViewHolder.productImage1 = rowView.findViewById(R.id.item_image);

        rowView.setTag(itemViewHolder);
        return rowView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) view.getTag();

        int indexProductId = cursor.getColumnIndex(ProductEntry._ID);
        int indexProductName = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int indexProductCode = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_CODE);
        int indexProductCategory = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_CATEGORY);
        int indexProductPrice = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int indexQuantity = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int indexImage1 = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE1);

        int index = cursor.getInt(indexProductId);
        String productName = cursor.getString(indexProductName).trim();
        String productCode = cursor.getString(indexProductCode).trim();
        String productCategory = cursor.getString(indexProductCategory).trim();
        int productPrice = Integer.parseInt(cursor.getString(indexProductPrice));
        int quantity = cursor.getInt(indexQuantity);
        byte[] imageBytes = cursor.getBlob(indexImage1);

        itemViewHolder.productId.setText(String.valueOf(index));
        itemViewHolder.productName.setText(productName);
        itemViewHolder.productCode.setText(productCode);
        itemViewHolder.productCategory.setText(productCategory);
        setPriceTextView(context, itemViewHolder.productPrice, productPrice);
        setQuantityTextView(context, itemViewHolder.productQuantity, quantity);
        setItemImage(itemViewHolder.productImage1, imageBytes);
    }

    /**
     * to check if quantity more than 0 or not if 0 set text "out of stock" in red background color
     *
     * @param context  to get resources
     * @param quantity to check
     * @param textView to set it's text and color
     */
    private void setQuantityTextView(Context context, TextView textView, int quantity) {
        if (quantity == ProductUtility.ZERO) {
            textView.setText(context.getString(R.string.out_of_stock));
            textView.setBackgroundColor(context.getResources().getColor(R.color.out_off_stock_color));
        } else {
            textView.setText(String.valueOf(quantity));
        }
    }

    /**
     * to check if quantity more than 0 or not if 0 set text "free" in green background color
     *
     * @param context  to get resources
     * @param price    to check
     * @param textView to set it's text and color
     */
    private void setPriceTextView(Context context, TextView textView, int price) {
        if (price == ProductUtility.ZERO) {
            textView.setText(context.getString(R.string.free_item));
            textView.setBackgroundColor(context.getResources().getColor(R.color.free_item_color));
        } else {
            String productPrice = String.valueOf(price + productUtility.DUMMY_PRODUCT_PRICE) + productUtility.DOLLAR_SIGN;
            textView.setText(productPrice);
        }
    }

    /**
     * to set image item
     *
     * @param imageView  to set it's src
     * @param imageBytes if not null set to the image view as bitmap
     */
    private void setItemImage(ImageView imageView, byte[] imageBytes) {
        if (imageBytes == null) { // as default icon
            imageView.setImageResource(R.drawable.icons8_warehouse_64);
        } else {
            imageView.setImageBitmap(ImageUtility.byteArrayToBitmap(imageBytes));
        }
    }

    static class ItemViewHolder {
        TextView productId;
        TextView productName;
        TextView productCode;
        TextView productCategory;
        TextView productPrice;
        TextView productQuantity;
        ImageView productImage1;
    }
}
