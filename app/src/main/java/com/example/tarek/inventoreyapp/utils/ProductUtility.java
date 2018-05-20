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

package com.example.tarek.inventoreyapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tarek.inventoreyapp.database.contract.ProductContract.ProductEntry;

import java.util.Random;

public class ProductUtility {

    private static final String DUMMY_SUPPLIER_PHONE = "0100111100";
    private static final String WHITE_SPACE_REGEX = ".*\\s.*";
    private static final int LIMIT_DESCRIPTION_LENGTH = 1000;
    public final String LAST_ID_KEY = "last id";
    public final String DEFAULT_MODE = "default mode";
    public final String BUNDLE_KEY_SELECTION = "selection";
    public final String BUNDLE_KEY_SELECTION_ARGS = "selectionArgs";
    public final String BUNDLE_KEY_SORT_ORDER = "sortOrder";
    public final String SIGN_ID = " =?";
    public final String DOLLAR_SIGN = " $";
    public final String MORE_THAN_SIGN = " >=?";
    public final String LESS_THAN_SIGN = " <=?";
    public final String MORE_THAN = "more than";
    public final String LESS_THAN = "less than";
    public final String DESC = " DESC";
    public final String ASC = " ASC";
    public final String TEXT = "TEXT";
    public final String NUMERIC = "NUMERIC";
    public final String LONG_TEXT = "LONG_TEXT";
    public final String PHONE = "PHONE";
    public final String TRUE = "TRUE";
    public final String FALSE = "FALSE";
    public final String LIKE = " LIKE ";
    public final String SPLITTER_REGEX_COLUMN_NAME = "(for )";
    public final String SPLITTER_REGEX_MINUS = "(-)";
    public final String MINUS = "-";
    public final String INTENT_TYPE_IMAGE = "image/*";
    public final float DUMMY_PRODUCT_PRICE = 0.99f;
    public final int LIMIT_RANDOM_VALUE = 100;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public final int NOT_NULLABLE_COLUMNS_COUNT = 7;
    public final int INVALID = -1;
    public final int VALID = 1;
    public final int THREE = 3;
    public final int FOUR = 4;
    public final int FIVE = 5;
    private static final String DUMMY_PRODUCT_NAME = "product";
    private static final String DUMMY_PRODUCT_CODE = "code";
    private static final String DUMMY_SUPPLIER_NAME = "name";
    public final int SIX = 6;
    private static final String DUMMY_PRODUCT_DESCRIPTION = "no description found";
    private static final String DUMMY_PRODUCT_CATEGORY = "category";
    private static final String EMPTY_STRING = "";
    private static final String TEXT_REGEX = "^[A-Za-z][A-Za-z0-9]*(?:_[A-Za-z0-9]+)*$";
    public final int SEVEN = 7;
    private static final String SEARCH_TEXT_1ST_SINGLE_QUOTE = "'%";
    private static final String SEARCH_TEXT_2ND_SINGLE_QUOTE = "%'";
    public final int TEN = 10;
    private static final int COLUMNS_COUNT = 8;
    private final Random random = new Random();

    public ProductUtility() {

    }

    /**
     * to generate dummy values and set them to Edit Text fields
     */
    public String[] getDummyStringValues() {
        // to change dummy data values according to  the random value
        String[] dummyValues = new String[COLUMNS_COUNT];
        dummyValues[ZERO] = DUMMY_PRODUCT_NAME + getRandomValue();
        dummyValues[ONE] = DUMMY_PRODUCT_CODE + getRandomValue();
        dummyValues[TWO] = DUMMY_PRODUCT_CATEGORY + getRandomValue();
        dummyValues[THREE] = String.valueOf(getRandomValue());
        dummyValues[FOUR] = String.valueOf(getRandomValue());
        dummyValues[FIVE] = DUMMY_SUPPLIER_NAME + getRandomValue();
        dummyValues[SIX] = DUMMY_SUPPLIER_PHONE + getRandomValue();
        dummyValues[SEVEN] = DUMMY_PRODUCT_DESCRIPTION;
        return dummyValues;
    }

    /**
     * to generate dummy values for testing purpose
     * used when want to insert dummy content values to db directly
     */
    public ContentValues getDummyContentValues() {
        String[] dummyValues = getDummyStringValues();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, dummyValues[ZERO]);
        values.put(ProductEntry.COLUMN_PRODUCT_CODE, dummyValues[ONE]);
        values.put(ProductEntry.COLUMN_PRODUCT_CATEGORY, dummyValues[TWO]);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, dummyValues[THREE]);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, dummyValues[FOUR]);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, dummyValues[FIVE]);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, dummyValues[SIX]);
        values.put(ProductEntry.COLUMN_PRODUCT_DESCRIPTION, dummyValues[SEVEN]);
        return values;
    }


    /**
     * to show toast Msg
     *
     * @param context for toast context
     * @param msg     to be shown
     */
    public void showToastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public int getRandomValue() {
        return random.nextInt(LIMIT_RANDOM_VALUE);
    }

    /**
     * to check if text has white space or not
     *
     * @param text to be checked
     * @return true if there is whte space or false if not
     */
    private boolean textHasWhiteSpace(String text) {
        return text.matches(WHITE_SPACE_REGEX);
    }

    /**
     * to check user input value if empty or doesn't match the regex
     *
     * @param name input value
     * @return string [] 3 elements
     * 1st element is the flag if it was false means that input data is valid
     * 2nd element is the valid value or null if it was invalid
     * 3rd element is the input value even it was invalid
     * regex from link
     * https://stackoverflow.com/questions/2821419/regular-expression-starting-and-ending-with-a-letter-accepting-only-letters
     */
    public String[] checkEnteredTextToSearch(String name) {
        String invalid = TRUE; // invalid value
        String nameButNotMatched = EMPTY_STRING;
        String checkedValue = EMPTY_STRING;
        if (!TextUtils.isEmpty(name) && !textHasWhiteSpace(name)) {
            if (name.matches(TEXT_REGEX)) {
                invalid = FALSE;
                checkedValue = SEARCH_TEXT_1ST_SINGLE_QUOTE + name +
                        SEARCH_TEXT_2ND_SINGLE_QUOTE;
            } else nameButNotMatched = name;
        }
        return new String[]{invalid, checkedValue, nameButNotMatched};
    }

    /**
     * to check user input value if empty or doesn't match the regex or has white space
     *
     * @param name input value
     * @return string [] 3 elements
     * 1st element is the flag if it was false means that input data is valid
     * 2nd element is the valid value or null if it was invalid
     * 3rd element is the input value even it was invalid
     * regex from link
     * https://stackoverflow.com/questions/2821419/regular-expression-starting-and-ending-with-a-letter-accepting-only-letters
     */
    private String[] checkEnteredText(String name) {
        String flag = TRUE; // invalid value
        String nameButNotMatched = EMPTY_STRING;
        String checkedValue = EMPTY_STRING;
        if (!TextUtils.isEmpty(name) && !textHasWhiteSpace(name)) {
            if (name.length() >= THREE) {
                if (name.matches(TEXT_REGEX)) {
                    flag = FALSE;
                    checkedValue = name;
                } else nameButNotMatched = name;
            }
        }
        return new String[]{flag, checkedValue, nameButNotMatched};
    }

    /**
     * to check user input value if empty or doesn't match the regex
     *
     * @param longText input value
     * @return string [] 3 elements
     * 1st element is the flag if it was false means that input data is valid
     * 2nd element is the valid value or null if it was invalid
     * 3rd element is the input value even it was invalid
     */
    private String[] checkEnteredLongText(String longText) {
        String invalid = TRUE; // invalid value
        String nameButNotMatched = EMPTY_STRING;
        String checkedValue = EMPTY_STRING;
        if (!TextUtils.isEmpty(longText)) {
            if (longText.length() < LIMIT_DESCRIPTION_LENGTH) {
                invalid = FALSE;
                checkedValue = longText;
            } else nameButNotMatched = longText;
        }
        return new String[]{invalid, checkedValue, nameButNotMatched};
    }

    /**
     * to check user input value if empty or doesn't match the regex
     *
     * @param number input value
     * @return string [] 3 elements
     * 1st element is the flag if it was false means that input data is valid
     * 2nd element is the valid value or null if it was invalid
     * 3rd element is the input value even it was invalid
     */
    public String[] checkEnteredNumbers(String number) {
        String invalid = TRUE; // invalid value
        String checkedValue = EMPTY_STRING;
        if (!TextUtils.isEmpty(number)) {
            int numberButNotMatched = (int) Math.floor(Integer.parseInt(number));
            if (numberButNotMatched >= ZERO) {
                invalid = FALSE; // invalid value
                checkedValue = String.valueOf(numberButNotMatched);
            }
        }//String value of the number it will be converted to int in next steps
        return new String[]{invalid, checkedValue, number};
    }

    /**
     * to check user input value if empty or doesn't match the regex
     *
     * @param phone input value
     * @return string [] 3 elements
     * 1st element is the flag if it was false means that input data is valid
     * 2nd element is the valid value or null if it was invalid
     * 3rd element is the input value even it was invalid
     */
    private String[] checkEnteredPhoneNumber(String phone) {
        String invalid = TRUE; // not valid value as default case
        String phoneButNotMatched = EMPTY_STRING;
        String checkedValue = EMPTY_STRING;
        if (!TextUtils.isEmpty(phone)) {
            if (phone.length() >= TEN) {
                invalid = FALSE;
                checkedValue = phone;
            } else phoneButNotMatched = phone;
        }
        return new String[]{invalid, checkedValue, phoneButNotMatched};
    }

    /**
     * check if @param editText is empty or null if so return true;
     *
     * @param editText to get it's entered text
     * @param type     for switch statement to differ between TEXT or NUMERIC
     * @return string [] 3 elements
     * 1st element is the flag if it was false means that input data is valid
     * 2nd element is the valid value or null if it was invalid
     * 3rd element is the input value even it was invalid
     */
    public String[] checkIfFieldIsNull(EditText editText, String type) {
        String[] flagAndValues;
        switch (type) {
            case TEXT: // name , code .. etc
                flagAndValues = checkEnteredText(getTextEditText(editText));
                break;
            case NUMERIC: // price , quantity
                flagAndValues = checkEnteredNumbers(getTextEditText(editText));
                break;
            case PHONE: // phone number
                flagAndValues = checkEnteredPhoneNumber(getTextEditText(editText));
                break;
            case LONG_TEXT:// long text (description)
            default:
                flagAndValues = checkEnteredLongText(getTextEditText(editText));
        }
        return flagAndValues;
    }

    /**
     * to get String from edit texts
     *
     * @param editText to get it's text
     * @return it's String value
     */
    public String getTextEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * to set bundle for loader args in @link MainActivity
     *
     * @param selection     where selection
     * @param selectionArgs where selection = args
     * @param sortOrder     order by
     * @return bundle ready for the required  query
     */
    public Bundle getBundle(String selection, String[] selectionArgs, String sortOrder) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_SELECTION, selection);
        bundle.putStringArray(BUNDLE_KEY_SELECTION_ARGS, selectionArgs);
        bundle.putString(BUNDLE_KEY_SORT_ORDER, sortOrder);
        return bundle;
    }

    public boolean noNullValues(ContentValues values) {
        // if values didn't have 6 values for all columns , check which one didn't found
        return (values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)) &&
                (values.containsKey(ProductEntry.COLUMN_PRODUCT_CODE)) &&
                (values.containsKey(ProductEntry.COLUMN_PRODUCT_CATEGORY)) &&
                (values.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE)) &&
                (values.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY)) &&
                (values.containsKey(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME)) &&
                (values.containsKey(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE));
    }

}

