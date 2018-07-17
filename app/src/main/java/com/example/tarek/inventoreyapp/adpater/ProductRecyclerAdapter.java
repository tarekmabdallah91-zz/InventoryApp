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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.data.ProductContract.ProductEntry;
import com.example.tarek.inventoreyapp.utils.ConstantsUtils;
import com.example.tarek.inventoreyapp.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder>
        implements ConstantsUtils {

    private final ProductItemOnClickListener productItemOnClickListener;
    // --Commented out by Inspection (17/07/2018 02:20 ص):private final String TAG = ProductRecyclerAdapter.class.getSimpleName();
    private Context context;
    private Cursor cursorProducts;

    public ProductRecyclerAdapter(ProductItemOnClickListener productItemOnClickListener) {
        this.productItemOnClickListener = productItemOnClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View root = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ProductViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (!cursorProducts.isClosed() && null != cursorProducts) {
            if (cursorProducts.moveToPosition(position))
                bindViewsWithCursorValues(holder, cursorProducts);
        }
    }

    @Override
    public int getItemCount() {
        if (null == cursorProducts) return ZERO;

        return cursorProducts.getCount();
    }

    /**
     * to check if quantity more than 0 or not if 0 set text "out of stock" in red background color
     *
     * @param context  to get resources
     * @param quantity to check
     * @param textView to set it's text and color
     */
    private void setQuantityTextView(Context context, TextView textView, int quantity) {
        if (quantity == ZERO) {
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
        if (price == ZERO) {
            textView.setText(context.getString(R.string.free_item));
            textView.setBackgroundColor(context.getResources().getColor(R.color.free_item_color));
        } else {
            String productPrice = String.valueOf(price + DUMMY_PRODUCT_PRICE) + DOLLAR_SIGN;
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
            imageView.setImageBitmap(ImageUtils.byteArrayToBitmap(imageBytes));
        }
    }

    private void setSyncedImage(ImageView imageView, int value) {
        if (value == ONE) {
            imageView.setImageResource(R.drawable.ic_cloud_on);
            imageView.setColorFilter(context.getResources().getColor(R.color.icons_color));
        } else if (value == ZERO) {
            imageView.setImageResource(R.drawable.ic_cloud_off);
            imageView.setColorFilter(ZERO);
        }
    }

    private void setFavouredImage(ImageView imageView, int value) {
        if (value == ONE) {
            imageView.setImageResource(R.drawable.ic_heart_on);
            imageView.setColorFilter(context.getResources().getColor(R.color.icons_color));
        } else if (value == ZERO) {
            imageView.setImageResource(R.drawable.ic_heart_off);
            imageView.setColorFilter(ZERO);
        }
    }

    private void bindViewsWithCursorValues(ProductViewHolder itemViewHolder, Cursor cursor) {
        int indexProductId = cursor.getColumnIndex(ProductEntry._ID);
        int indexProductName = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int indexProductCode = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_CODE);
        int indexProductCategory = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_CATEGORY);
        int indexProductPrice = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int indexQuantity = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int indexImage1 = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE1);
        int indexSynced = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SYNCED);
        int indexFavoured = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_FAVORED);

        int index = cursor.getInt(indexProductId);
        String productName = cursor.getString(indexProductName).trim();
        String productCode = cursor.getString(indexProductCode).trim();
        String productCategory = cursor.getString(indexProductCategory).trim();
        int productPrice = Integer.parseInt(cursor.getString(indexProductPrice));
        int quantity = cursor.getInt(indexQuantity);
        byte[] imageBytes = cursor.getBlob(indexImage1);
        int syncedBinaryValue = cursor.getInt(indexSynced);
        int favouredBinaryValue = cursor.getInt(indexFavoured);

        itemViewHolder.productId.setText(String.valueOf(index));
        itemViewHolder.productName.setText(productName);
        itemViewHolder.productCode.setText(productCode);
        itemViewHolder.productCategory.setText(productCategory);
        setPriceTextView(context, itemViewHolder.productPrice, productPrice);
        setQuantityTextView(context, itemViewHolder.productQuantity, quantity);
        setItemImage(itemViewHolder.productImage1, imageBytes);
        setSyncedImage(itemViewHolder.productSyncedIcon, syncedBinaryValue);
        setFavouredImage(itemViewHolder.productFavouredIcon, favouredBinaryValue);

        itemViewHolder.itemView.setTag(index);
    }

    public void swapCursor(Cursor cursor) {
        this.cursorProducts = cursor;
        notifyDataSetChanged();
    }

    public interface ProductItemOnClickListener {
        void onClick(int id);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_id)
        TextView productId;
        @BindView(R.id.item_name)
        TextView productName;
        @BindView(R.id.item_code)
        TextView productCode;
        @BindView(R.id.item_category)
        TextView productCategory;
        @BindView(R.id.item_price)
        TextView productPrice;
        @BindView(R.id.item_quantity)
        TextView productQuantity;
        @BindView(R.id.item_image)
        ImageView productImage1;
        @BindView(R.id.item_synced_icon)
        ImageView productSyncedIcon;
        @BindView(R.id.item_favoured_icon)
        ImageView productFavouredIcon;

        private ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = (int) v.getTag();
            //Log.d(TAG , " view id = " +id);
            productItemOnClickListener.onClick(id);
        }
    }
}
