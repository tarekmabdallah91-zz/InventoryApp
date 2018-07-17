package com.example.tarek.inventoreyapp.preferences;


import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.utils.ProductUtility;


public class ProductPreferencesSettings extends PreferenceFragmentCompat
        implements OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    static final String TAG = ProductPreferencesSettings.class.getSimpleName();
    String SORT_OR_SEARCH_KEY;
    String SORT_OR_SEARCH_DEFAULT_VALUE;
    String ORDER_KEY;
    String ORDER_DEFAULT_VALUE;
    String SEARCH_KEY;
    String sortOrSearchValue;
    String orderByValue;

    ProductUtility productUtility = new ProductUtility();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SORT_OR_SEARCH_KEY = getString(R.string.sort_or_search_key);
        SORT_OR_SEARCH_DEFAULT_VALUE = getString(R.string.value_order_by);
        ORDER_KEY = getString(R.string.order_key);
        ORDER_DEFAULT_VALUE = getString(R.string.value_order_by_default);
        SEARCH_KEY = getString(R.string.search_key);

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_settings_layout);

        //TODO : to solve this method to set summary when activity starts
        // to change summary of all preferences
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            if (!(preference instanceof CheckBoxPreference)) {
                String key = preference.getKey();
                String value = sharedPreferences.getString(key, productUtility.EMPTY_TEXT);
                Log.d(TAG, i + " - " + value);
                setPreferenceSummary(preference, value);
            }
        }

        // to validate the input values immediately setOnPreferenceChangeListener for the EditText
        Preference preference = findPreference(getString(R.string.search_key));
        preference.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preferenceForSorting = findPreference(ORDER_KEY);
        Preference preferenceForSearching = findPreference(SEARCH_KEY);

        if (SORT_OR_SEARCH_KEY.equals(key)) {
            // get the selected value to decide which the second choice to complete the query correctly
            sortOrSearchValue = sharedPreferences.getString(SORT_OR_SEARCH_KEY, SORT_OR_SEARCH_DEFAULT_VALUE);
            if (sortOrSearchValue.equals(SORT_OR_SEARCH_DEFAULT_VALUE)) {
                // user choose sorting ORDER_BY something
                preferenceForSorting.setEnabled(true);
                preferenceForSearching.setEnabled(false);
            } else {
                // user choose Searching for something (name,category,code)
                preferenceForSearching.setEnabled(true);
                preferenceForSorting.setEnabled(false);
            }
        } else if (ORDER_KEY.equals(key)) {
            // get the selected  ORDER_BY value to complete the query correctly
            orderByValue = sharedPreferences.getString(ORDER_KEY, ORDER_DEFAULT_VALUE);
            if (orderByValue.contains(productUtility.THAN_KEYWORD)) {
                // if the user's choice was contains "than" keyword something like moreThan/lessThan
                // enable the search edit text
                preferenceForSearching.setEnabled(true);
            } else { // else still disabled to avoid invalid inputs
                preferenceForSearching.setEnabled(false);
            }
        }

        // to change the summary immediately
        Preference preference = findPreference(key);
        if (null != preference) {
            if (!(preference instanceof CheckBoxPreference)) {
                setPreferenceSummary(preference, sharedPreferences.getString(preference.getKey(), ""));
            }
        }

        //Log.d(TAG , "sortOrSearch " + sortOrSearch + " , " + "orderBy " + orderBy + " , " + "search for " + searchFor);

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String errorMsgForNumbers = "you should enter correct numbers only";
        final String errorMsgForText = "error in input text ";
        if (SEARCH_KEY.equals(preference.getKey())) {
            String stringInputValue = (String) newValue;
            String[] result;
            if (null != orderByValue && orderByValue.contains(productUtility.THAN_KEYWORD)) {
                //user want to search for int numbers
                try {
                    result = productUtility.checkEnteredNumbers(stringInputValue);
                    if (productUtility.TRUE.equals(result[ProductUtility.ZERO])) {
                        Toast.makeText(getContext(), errorMsgForNumbers, Toast.LENGTH_LONG).show();
                        return false;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), errorMsgForNumbers, Toast.LENGTH_LONG).show();
                    return false;
                }


            } else {
                result = productUtility.checkEnteredTextToSearch(stringInputValue);
                if (productUtility.TRUE.equals(result[ProductUtility.ZERO])) {
                    Toast.makeText(getContext(), errorMsgForText, Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }

        return true;
    }

    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            if (index >= 0) {
                listPreference.setSummary(listPreference.getEntries()[index]);
            }
        } else if (preference instanceof EditTextPreference) {
            preference.setSummary(value);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
