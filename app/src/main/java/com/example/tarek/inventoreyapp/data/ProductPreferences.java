package com.example.tarek.inventoreyapp.data;


import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.utils.ProductUtils;


public class ProductPreferences extends PreferenceFragmentCompat
        implements OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    static final String TAG = ProductPreferences.class.getSimpleName();
    String SORT_OR_SEARCH_KEY;
    String SORT_OR_SEARCH_DEFAULT_VALUE;
    String ORDER_KEY;
    String ORDER_DEFAULT_VALUE;
    String SEARCH_KEY;
    String sortOrSearchValue;
    String orderByValue;

    ProductUtils productUtils = new ProductUtils();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SORT_OR_SEARCH_KEY = getString(R.string.sort_or_search_key);
        SORT_OR_SEARCH_DEFAULT_VALUE = getString(R.string.value_order_by);
        ORDER_KEY = getString(R.string.order_key);
        ORDER_DEFAULT_VALUE = getString(R.string.value_order_by);
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
        for (int i = productUtils.ZERO; i < count; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            if (!(preference instanceof CheckBoxPreference)) {
                String key = preference.getKey();
                String value = sharedPreferences.getString(key, productUtils.EMPTY_STRING);
                //Log.d(TAG, i + " - " + value);
                setPreferenceSummary(preference, value);
            }
        }

        // to validate the input values immediately setOnPreferenceChangeListener for the EditText
        Preference preference = findPreference(getString(R.string.search_key));
        preference.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // to change the summary immediately
        Preference preference = findPreference(key);
        if (null != preference) {
            if (!(preference instanceof CheckBoxPreference)) {
                setPreferenceSummary(preference, sharedPreferences.getString(preference.getKey(), productUtils.EMPTY_STRING));
            }
        }
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String errorMsgForNumbers = getString(R.string.error_input_number);
        final String errorMsgForText = getString(R.string.error_text);
        sortOrSearchValue = getPreferenceScreen().getSharedPreferences()
                .getString(SORT_OR_SEARCH_KEY, SORT_OR_SEARCH_DEFAULT_VALUE);
        if (SEARCH_KEY.equals(preference.getKey())) {
            String stringInputValue = (String) newValue;
            String[] result;
            if (null != orderByValue && orderByValue.contains(productUtils.MINUS)) {
                //user want to search for int numbers
                try {
                    result = productUtils.checkEnteredNumbers(stringInputValue);
                    if (productUtils.TRUE.equals(result[productUtils.ZERO])) {
                        Toast.makeText(getContext(), errorMsgForNumbers, Toast.LENGTH_LONG).show();
                        return false;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), errorMsgForNumbers, Toast.LENGTH_LONG).show();
                    return false;
                }


            } else {
                result = productUtils.checkEnteredTextToSearch(stringInputValue);
                if (productUtils.TRUE.equals(result[productUtils.ZERO])) {
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
            if (index >= productUtils.ZERO) {
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
