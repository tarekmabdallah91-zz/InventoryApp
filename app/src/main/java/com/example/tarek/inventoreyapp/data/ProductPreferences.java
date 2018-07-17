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

package com.example.tarek.inventoreyapp.data;


import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import com.example.tarek.inventoreyapp.R;
import com.example.tarek.inventoreyapp.utils.ProductUtils;


public class ProductPreferences extends PreferenceFragmentCompat
        implements OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    private final ProductUtils productUtils = new ProductUtils();
    // --Commented out by Inspection (17/07/2018 02:20 ص):private static final String TAG = ProductPreferences.class.getSimpleName();
    private String SORT_OR_SEARCH_KEY;
    private String SORT_OR_SEARCH_DEFAULT_VALUE;
    private String SEARCH_KEY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        SORT_OR_SEARCH_KEY = getString(R.string.sort_or_search_key);
        SORT_OR_SEARCH_DEFAULT_VALUE = getString(R.string.value_sort_or_search_default_value);
        SEARCH_KEY = getString(R.string.search_key);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_settings_layout);

        // to change summary of all preferences
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        // to get 1st preference category
        int count = preferenceScreen.getPreferenceCount();
        for (int k = productUtils.ZERO; k < count; k++) {
            Preference preference = preferenceScreen.getPreference(k);
            if (preference instanceof PreferenceCategory) {
                PreferenceCategory category = (PreferenceCategory) preference;
                int countCategoryItems = category.getPreferenceCount();
                for (int i = productUtils.ZERO; i < countCategoryItems; i++) {
                    Preference preferenceFromCategory = category.getPreference(i);
                    if (!(preferenceFromCategory instanceof CheckBoxPreference)) {
                        String key = preferenceFromCategory.getKey();
                        String value = sharedPreferences.getString(key, productUtils.EMPTY_STRING);
                        setPreferenceSummary(preferenceFromCategory, value);
                    }
                }
            }
        }
        // to validate the input values immediately setOnPreferenceChangeListener for the EditText
        Preference preferenceForSearching = findPreference(SEARCH_KEY);
        preferenceForSearching.setOnPreferenceChangeListener(this);
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
        // TODO : to solve this method - is doesn't respond !!!
        final String errorMsgForNumbers = getString(R.string.error_input_number);
        final String errorMsgForText = getString(R.string.error_text);
        final String sortOrSearchValue = getPreferenceScreen().getSharedPreferences().
                getString(SORT_OR_SEARCH_KEY, SORT_OR_SEARCH_DEFAULT_VALUE);

        if (SEARCH_KEY.equals(preference.getKey())) {
            //Log.d(TAG , " onPreferenceChange - sortOrSearchValue: " +sortOrSearchValue);
            String stringInputValue = (String) newValue;
            String[] result;
            if (sortOrSearchValue.contains(productUtils.MINUS)) { // we expect integer input
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

            } else if (sortOrSearchValue.contains(ProductUtils.SEARCH)) {
                result = productUtils.checkEnteredTextToSearch(stringInputValue);
                if (productUtils.TRUE.equals(result[ProductUtils.ZERO])) {
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
