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
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tarek.inventoreyapp.data.ProductContract.ProductEntry;

import java.util.Random;

public class ProductUtils implements ConstantsUtils {

    private final Random random = new Random();

    public ProductUtils() {

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
        dummyValues[EIGHT] = String.valueOf(ONE);
        dummyValues[NINE] = String.valueOf(ZERO);
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
        values.put(ProductEntry.COLUMN_PRODUCT_SYNCED, dummyValues[EIGHT]);
        values.put(ProductEntry.COLUMN_PRODUCT_FAVORED, dummyValues[NINE]);
        //Log.d(TAG , values.toString());
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
     * @return true if there is white space or false if not
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
            try {
                int numberButNotMatched = (int) Math.floor(Integer.parseInt(number));
                if (numberButNotMatched >= ZERO) {
                    invalid = FALSE; // invalid value
                    checkedValue = String.valueOf(numberButNotMatched);
                }
            } catch (NumberFormatException e) {
                checkedValue = String.valueOf(INVALID);
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
    private String getTextEditText(EditText editText) {
        return editText.getText().toString().trim();
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

