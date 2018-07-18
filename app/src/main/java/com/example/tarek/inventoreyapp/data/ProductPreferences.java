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
import com.example.tarek.inventoreyapp.utils.ConstantsUtils;
import com.example.tarek.inventoreyapp.utils.ProductUtils;


public class ProductPreferences extends PreferenceFragmentCompat
        implements OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener, ConstantsUtils {

    private final ProductUtils productUtils = new ProductUtils();
    private String SORT_OR_SEARCH_KEY;
    private String SORT_OR_SEARCH_DEFAULT_VALUE;
    private String SEARCH_KEY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SORT_OR_SEARCH_KEY = getString(R.string.sort_or_search_key);
        SORT_OR_SEARCH_DEFAULT_VALUE = getString(R.string.value_order_by);
        SEARCH_KEY = getString(R.string.search_key);

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_settings_layout);

        // to change summary of all preferences
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();
        for (int i = ZERO; i < count; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            if (preference instanceof PreferenceCategory) {
                PreferenceCategory preferenceCategory = (PreferenceCategory) preference;
                int countCategory = preferenceCategory.getPreferenceCount();
                for (int k = ZERO; k < countCategory; k++) {
                    Preference preference1 = preferenceCategory.getPreference(k);
                    if (!(preference1 instanceof CheckBoxPreference)) {
                        String key = preference.getKey();
                        String value = sharedPreferences.getString(key, EMPTY_STRING);
                        setPreferenceSummary(preference, value);
                    }
                }
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
                setPreferenceSummary(preference, sharedPreferences.getString(preference.getKey(), EMPTY_STRING));
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String errorMsgForNumbers = getString(R.string.error_input_number);
        final String errorMsgForText = getString(R.string.error_text);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        String sortOrSearchValue = sharedPreferences.getString(SORT_OR_SEARCH_KEY, SORT_OR_SEARCH_DEFAULT_VALUE);
        if (SEARCH_KEY.equals(preference.getKey())) {
            String stringInputValue = (String) newValue;
            String[] result;
            if (null != sortOrSearchValue && sortOrSearchValue.contains(MINUS)) {
                //user want to search for int numbers
                try {
                    result = productUtils.checkEnteredNumbers(stringInputValue);
                    if (TRUE.equals(result[ZERO])) {
                        Toast.makeText(getContext(), errorMsgForNumbers, Toast.LENGTH_LONG).show();
                        return false;
                    } else {
                        sharedPreferences.edit().putString(SEARCH_KEY, result[ONE]).apply();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), errorMsgForNumbers, Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                result = productUtils.checkEnteredTextToSearch(stringInputValue);
                if (TRUE.equals(result[ZERO])) {
                    Toast.makeText(getContext(), errorMsgForText, Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    sharedPreferences.edit().putString(SEARCH_KEY, result[ONE]).apply();
                }
            }
        }
        return true;
    }

    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            if (index >= ZERO) {
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
