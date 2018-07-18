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
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.adpater.ProductRecyclerAdapter;
import com.example.tarek.inventoreyapp.data.ProductContract.ProductEntry;
import com.example.tarek.inventoreyapp.sync.ProductCursorLoader;
import com.example.tarek.inventoreyapp.utils.ConstantsUtils;
import com.example.tarek.inventoreyapp.utils.ProductUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemFragment extends Fragment implements ConstantsUtils,
        SharedPreferences.OnSharedPreferenceChangeListener, ProductRecyclerAdapter.ProductItemOnClickListener {

    @BindView(R.id.frg_recycler_view)
    RecyclerView frg_recycler_view;
    @BindView(R.id.frg_tv_count_msg)
    TextView frg_tv_count_msg;
    @BindView(R.id.frg_tv_error_msg)
    TextView frg_tv_error_msg;
    @BindView(R.id.frg_iv_error_image)
    ImageView frg_iv_error_image;
    @BindView(R.id.frg_fab)
    FloatingActionButton fab;
    @BindString(R.string.empty_text_msg)
    String noData_msg;
    @BindString(R.string.count_msg)
    String countMsg;
    @BindString(R.string.sort_or_search_key)
    String SORT_OR_SEARCH_KEY;
    @BindString(R.string.value_order_by)
    String SORT_OR_SEARCH_DEFAULT_VALUE;
    @BindString(R.string.order_key)
    String ORDER_KEY;
    @BindString(R.string.value_order_by_id)
    String ORDER_DEFAULT_VALUE;
    @BindString(R.string.search_key)
    String SEARCH_KEY;
    @BindString(R.string.invalid_input_user_catalog_activity)
    String invalidInput;
    @BindString(R.string.empty_text_msg)
    String emptyTextMsg;
    @BindString(R.string.put_value_text_msg)
    String putValueMsg;
    @BindString(R.string.no_data_for_input_value_msg)
    String noDataForInputValue;
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
    private String sortOrSearchValue;
    private String orderByValue;
    private String inputText;

    private int fragmentItemPosition;
    private final Boolean BOOLEAN_TRUE = true;
    private final Boolean BOOLEAN_FALSE = false;

    private ProductRecyclerAdapter productRecyclerAdapter;
    private Context context;
    private ContentResolver contentResolver;
    private Cursor cursor;
    private Bundle bundle;

    @Override // run before onCreateView - onActivityCreated - onStart ..
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(BOOLEAN_TRUE); // to support OptionsMenu
        // run before onCreateView - onActivityCreated - onStart ..
    }

    @Nullable
    @Override // run after onCreate and before onActivityCreated - onStart ..
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = getLayoutInflater().inflate(R.layout.fragment_item, container, BOOLEAN_FALSE);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
        contentResolver = null != context ? context.getContentResolver() : null;

        if (null != savedInstanceState) {
            bundle = savedInstanceState.getBundle(BUNDLE);
        } else {
            bundle = new Bundle();
            bundle.putInt(FRAGMENT_ITEM_POSITION, fragmentItemPosition);
        }
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(BUNDLE, bundle);
    }

    private void runCode() {
        setRecyclerAdapter();
        getSharedPreferenceWhenStartActivity();
        getAppropriateCursor(bundle, QUERY);
        setRecyclerView();
    }
    @Override
    public void onResume() {
        super.onResume();
        runCode();
    }

    private void getSharedPreferenceWhenStartActivity() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sortOrSearchValue = sharedPreferences.getString(SORT_OR_SEARCH_KEY, SORT_OR_SEARCH_DEFAULT_VALUE);
        bundle.putString(SORT_OR_SEARCH_PREFERENCE_KEY, sortOrSearchValue);
        // get the selected  ORDER_BY value to complete the query correctly
        orderByValue = sharedPreferences.getString(ORDER_KEY, ORDER_DEFAULT_VALUE);
        bundle.putString(ORDER_BY_PREFERENCE_KEY, orderByValue);
        // get the selected  ORDER_BY value to complete the query correctly
        inputText = sharedPreferences.getString(SEARCH_KEY, EMPTY_STRING);
        bundle.putString(SEARCHED_INPUT_TEXT_PREFERENCE_KEY, inputText);
    }

    private void getAppropriateCursor(Bundle bundle, String mode) {
        bundle.putString(MODE, mode);
        cursor = new ProductCursorLoader(context, contentResolver, bundle).loadInBackground();

        if (null == cursor) {
            showData(BOOLEAN_FALSE, noData_msg);
            frg_tv_count_msg.setVisibility(View.GONE);
        } else if (cursor.getCount() <= ZERO) {
            showData(BOOLEAN_FALSE, noDataForInputValue);
        } else {
            productRecyclerAdapter.swapCursor(cursor);
            String msg = String.format(countMsg, cursor.getCount());
            showData(BOOLEAN_TRUE, msg);
        }
    }

    private void setRecyclerAdapter() {
        productRecyclerAdapter = new ProductRecyclerAdapter(this);
        productRecyclerAdapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        frg_recycler_view.setHasFixedSize(BOOLEAN_TRUE);
        frg_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        frg_recycler_view.setAdapter(productRecyclerAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ZERO, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return BOOLEAN_FALSE;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();

                getAppropriateCursor(bundle, id + DELETE_ITEM);
            }
        }).attachToRecyclerView(frg_recycler_view);

    }

    @OnClick(R.id.frg_fab)
    void onClickFab() {
        startActivity(new Intent(context, EditorActivity.class));
    }

    public void setFragmentItemPosition(int position) {
        this.fragmentItemPosition = position;
    }

    private void showData(boolean value, String text) {
        // showed text in count text view
        String msg = sortOrSearchValue + WHITE_SPACE;
        if (SORT_OR_SEARCH_DEFAULT_VALUE.equals(sortOrSearchValue)) { //  sortOrSearchValue = order by
            msg += orderByValue;
        } else {
            msg += EQUAL + WHITE_SPACE + inputText;
        }
        if (value) {
            msg = text + WHITE_SPACE + msg;
            frg_iv_error_image.setVisibility(View.GONE);
            frg_tv_error_msg.setVisibility(View.GONE);
            frg_recycler_view.setVisibility(View.VISIBLE);
            frg_tv_count_msg.setTextColor(context.getResources().getColor(R.color.default_text_color));
        } else {
            frg_recycler_view.setVisibility(View.GONE);
            frg_tv_error_msg.setText(text);
            frg_iv_error_image.setVisibility(View.VISIBLE);
            frg_tv_error_msg.setVisibility(View.VISIBLE);
            frg_tv_count_msg.setTextColor(context.getResources().getColor(R.color.error_msg_text_color));
        }

        frg_tv_count_msg.setText(msg);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_all_data:
                showDiscardMsg(deleteAllItemsMsg, discardMsg, deleteAllItems);
                break;
            case R.id.action_insert_dummy_data:
                getAppropriateCursor(bundle, INSERT_DUMMY_ITEM);
                break;
            case R.id.action_settings:
                startActivity(new Intent(context, SettingActivity.class));
                break;
            case R.id.action_reload_data:
                reloadData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * to show discard msg if the user clicked back,up or save  buttons
     */
    private void showDiscardMsg(String msg, String discard, String doSomeThing) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                    getAppropriateCursor(bundle, DELETE_ALL_DATA);
                }
            };
        } else { // discard case - user want to exit
            return new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //finish();
                }
            };
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (SORT_OR_SEARCH_KEY.equals(key)) {
            // get the selected value to decide which the second choice to complete the query correctly
            sortOrSearchValue = sharedPreferences.getString(SORT_OR_SEARCH_KEY, SORT_OR_SEARCH_DEFAULT_VALUE);
            bundle.putString(SORT_OR_SEARCH_PREFERENCE_KEY, sortOrSearchValue);
        } else if (ORDER_KEY.equals(key)) {
            // get the selected  ORDER_BY value to complete the query correctly
            orderByValue = sharedPreferences.getString(ORDER_KEY, ORDER_DEFAULT_VALUE);
            bundle.putString(ORDER_BY_PREFERENCE_KEY, orderByValue);
        } else if (SEARCH_KEY.equals(key)) {
            // get the selected  ORDER_BY value to complete the query correctly
            inputText = sharedPreferences.getString(SEARCH_KEY, ProductUtils.EMPTY_STRING);
            bundle.putString(SEARCHED_INPUT_TEXT_PREFERENCE_KEY, inputText);
        }
    }

    @Override
    public void onClick(int id) {
        startActivity(new Intent(context, EditorActivity.class).
                setData(ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id)));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
                closeCursor();
                setAdapterByNull();
            } else {
                // it's VERY IMPORTANT TO reload data here after set adapter null to get all changes add/delete items immediately
                // may be there is another best solution but I tried every thing I know till reach to try this one.
                getAppropriateCursor(bundle, QUERY);
            }
        }
    }

    private void reloadData() {
        setAdapterByNull(); // to empty the current recycler
        getAppropriateCursor(bundle, QUERY); // then reload it again
    }

    private void closeCursor() {
        if (null != cursor) cursor.close();
    }

    private void setAdapterByNull() {
        productRecyclerAdapter.swapCursor(null);
    }

    @Override
    public void onStop() {
        super.onStop();
        closeCursor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(this);
    }

}
