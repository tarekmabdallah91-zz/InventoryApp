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

    public static final String DEFAULT_MODE = "default mode";
    public static final String BUNDLE_KEY_SELECTION = "selection";
    public static final String BUNDLE_KEY_SELECTION_ARGS = "selectionArgs";
    public static final String BUNDLE_KEY_SORT_ORDER = "sortOrder";
    public static final String SIGN_ID = " =?";
    public static final String DOLLAR_SIGN = " $";
    public static final String MORE_THAN_SIGN = " >=?";
    public static final String LESS_THAN_SIGN = " <=?";
    public static final String MORE_THAN = "more than";
    public static final String LESS_THAN = "less than";
    public static final String DESC = " DESC";
    public static final String ASC = " ASC";
    public static final String TEXT = "TEXT";
    public static final String NUMERIC = "NUMERIC";
    public static final String LONG_TEXT = "LONG_TEXT";
    public static final String PHONE = "PHONE";
    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";
    public static final String LIKE = " LIKE ";
    public static final String SPLITTER_REGEX_COLUMN_NAME = "(for )";
    public static final String SPLITTER_REGEX_MINUS = "(-)";
    public static final String MINUS = "-";
    public static final String INTENT_TYPE_IMAGE = "image/*";
    public static final float DUMMY_PRODUCT_PRICE = 0.99f;
    public static final int LIMIT_RANDOM_VALUE = 1000;
    public static final int NOT_NULLABLE_COLUMNS_COUNT = 7;
    public static final int INVALID = -1;
    public static final int VALID = 1;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int TEN = 10;
    private static final String DUMMY_PRODUCT_NAME = "product";
    private static final String DUMMY_PRODUCT_CODE = "code";
    private static final String DUMMY_SUPPLIER_NAME = "name";
    private static final String DUMMY_SUPPLIER_PHONE = "01234";
    private static final String DUMMY_PRODUCT_DESCRIPTION = "no description found";
    private static final String DUMMY_PRODUCT_CATEGORY = "category";
    private static final String EMPTY_STRING = "";
    private static final String PHONE_NUMBER_REGEX = "^[0-9]*$";
    private static final String TEXT_REGEX = "^[A-Za-z][A-Za-z0-9]*(?:_[A-Za-z0-9]+)*$";
    private static final String SEARCH_TEXT_1ST_SINGLE_QUOTE = "'%";
    private static final String SEARCH_TEXT_2ND_SINGLE_QUOTE = "%'";
    private static final int LIMIT_DESCRIPTION_LONG = 1000;
    private static final int COLUMNS_COUNT = 8;
    private static final Random random = new Random();


    /**
     * to generate dummy values and set them to Edit Text fields
     */
    public static String[] getDummyStringValues() {
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
    public static ContentValues getDummyContentValues() {
        String[] dummyValues = ProductUtility.getDummyStringValues();

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, dummyValues[ProductUtility.ZERO]);
        values.put(ProductEntry.COLUMN_PRODUCT_CODE, dummyValues[ProductUtility.ONE]);
        values.put(ProductEntry.COLUMN_PRODUCT_CATEGORY, dummyValues[ProductUtility.TWO]);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, dummyValues[ProductUtility.THREE]);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, dummyValues[ProductUtility.FOUR]);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, dummyValues[ProductUtility.FIVE]);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, dummyValues[ProductUtility.SIX]);
        values.put(ProductEntry.COLUMN_PRODUCT_DESCRIPTION, dummyValues[ProductUtility.SEVEN]);
        return values;
    }


    /**
     * to show toast Msg
     *
     * @param context for toast context
     * @param msg     to be shown
     */
    public static void showToastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static int getRandomValue() {
        return random.nextInt(LIMIT_RANDOM_VALUE);
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
    public static String[] checkEnteredTextToSearch(String name) {
        String invalid = TRUE; // invalid value
        String nameButNotMatched = EMPTY_STRING;
        String checkedValue = EMPTY_STRING;
        if (!TextUtils.isEmpty(name)) {
            if (name.matches(TEXT_REGEX)) {
                invalid = FALSE;
                checkedValue = ProductUtility.SEARCH_TEXT_1ST_SINGLE_QUOTE + name +
                        ProductUtility.SEARCH_TEXT_2ND_SINGLE_QUOTE;
            } else nameButNotMatched = name;
        }
        return new String[]{invalid, checkedValue, nameButNotMatched};
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
    private static String[] checkEnteredText(String name) {
        String invalid = TRUE; // invalid value
        String nameButNotMatched = EMPTY_STRING;
        String checkedValue = EMPTY_STRING;
        if (!TextUtils.isEmpty(name)) {
            if (name.length() >= SIX) {
                if (name.matches(TEXT_REGEX)) {
                    invalid = FALSE;
                    checkedValue = name;
                } else nameButNotMatched = name;
            }
        }
        return new String[]{invalid, checkedValue, nameButNotMatched};
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
    private static String[] checkEnteredLongText(String longText) {
        String invalid = TRUE; // invalid value
        String nameButNotMatched = EMPTY_STRING;
        String checkedValue = EMPTY_STRING;
        if (!TextUtils.isEmpty(longText)) {
            if (longText.length() < LIMIT_DESCRIPTION_LONG) {
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
    public static String[] checkEnteredNumbers(String number) {
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
    private static String[] checkEnteredPhoneNumber(String phone) {
        String invalid = TRUE; // not valid value as default case
        String phoneButNotMatched = EMPTY_STRING;
        String checkedValue = EMPTY_STRING;
        if (!TextUtils.isEmpty(phone)) {
            if (phone.matches(PHONE_NUMBER_REGEX)) {
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
    public static String[] checkIfFieldIsNull(EditText editText, String type) {
        String[] flagAndValues;
        switch (type) {
            case TEXT: // name , code .. etc
                flagAndValues = ProductUtility.checkEnteredText(ProductUtility.getTextEditText(editText));
                break;
            case NUMERIC: // price , quantity
                flagAndValues = ProductUtility.checkEnteredNumbers(ProductUtility.getTextEditText(editText));
                break;
            case PHONE: // phone number
                flagAndValues = ProductUtility.checkEnteredPhoneNumber(ProductUtility.getTextEditText(editText));
                break;
            case LONG_TEXT:// long text (description)
            default:
                flagAndValues = ProductUtility.checkEnteredLongText(ProductUtility.getTextEditText(editText));
        }
        return flagAndValues;
    }

    /**
     * to get String from edit texts
     *
     * @param editText to get it's text
     * @return it's String value
     */
    public static String getTextEditText(EditText editText) {
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
    public static Bundle getBundle(String selection, String[] selectionArgs, String sortOrder) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_SELECTION, selection);
        bundle.putStringArray(BUNDLE_KEY_SELECTION_ARGS, selectionArgs);
        bundle.putString(BUNDLE_KEY_SORT_ORDER, sortOrder);
        return bundle;
    }

    public static boolean noNullValues(ContentValues values) {
        // if values didn't have 6 values for all columns , check which one didn't found
        return (values.containsKey(ProductEntry.COLUMN_PRODUCT_CATEGORY)) &&
                (values.containsKey(ProductEntry.COLUMN_PRODUCT_CODE)) &&
                (values.containsKey(ProductEntry.COLUMN_PRODUCT_CATEGORY)) &&
                (values.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE)) &&
                (values.containsKey(ProductEntry.COLUMN_PRODUCT_QUANTITY)) &&
                (values.containsKey(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME)) &&
                (values.containsKey(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE));
    }

}

