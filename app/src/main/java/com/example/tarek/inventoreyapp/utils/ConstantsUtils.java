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

public interface ConstantsUtils {
    String DUMMY_SUPPLIER_PHONE = "0100111100";
    String WHITE_SPACE_REGEX = ".*\\s.*";
    String SPLITTER_REGEX_MINUS = "(-)";
    String MINUS = "-";
    String PLUS = "+";
    String EQUAL = "=";
    String EMPTY_STRING = "";
    String WHITE_SPACE = " ";
    String COMMA = ", ";
    String NEW_LINE_STRING = "\n";
    String TEXT_REGEX = "^[A-Za-z0-9][A-Za-z0-9]*(?:_[A-Za-z0-9]+)*$";
    String SPLITTER_REGEX_COLUMN_NAME = "(for )";
    String REGEX_TO_GET_INTEGER_ONLY_FROM_STRING = "[\\D]";

    String MORE_THAN = "more than";
    String LESS_THAN = "less than";
    String TEXT = "TEXT";
    String NUMERIC = "NUMERIC";
    String LONG_TEXT = "LONG_TEXT";
    String PHONE = "PHONE";
    String TRUE = "TRUE";
    String FALSE = "FALSE";
    String LIKE = " LIKE ";
    String AND = " AND ";

    String SEARCH_TEXT_1ST_SINGLE_QUOTE = "'%";
    String SEARCH_TEXT_2ND_SINGLE_QUOTE = "%'";

    String MODE = "mode";
    String QUERY = "query";
    String DELETE_ITEM = " delete item";
    String DELETE_ALL_DATA = "delete all data";
    String INSERT_DUMMY_ITEM = "insert dummy item";
    String FRAGMENT_ITEM_POSITION = "fragmentItemPosition";
    String SORT_OR_SEARCH_PREFERENCE_KEY = "sortOrSearchValue";
    String ORDER_BY_PREFERENCE_KEY = "orderByValue";
    String ORDER_BY = "order by";
    String SEARCHED_INPUT_TEXT_PREFERENCE_KEY = "searchedInputText";
    String SIGN_ID = " =?";
    String DOLLAR_SIGN = " $";
    String MORE_THAN_SIGN = " >=?";
    String LESS_THAN_SIGN = " <=?";
    String INTENT_TYPE_IMAGE = "image/*";
    String INTENT_TYPE_TEXT = "text/plain";
    String INTENT_TYPE_TEL = "tel:";

    String DUMMY_PRODUCT_NAME = "product";
    String DUMMY_PRODUCT_CODE = "code";
    String DUMMY_SUPPLIER_NAME = "name";
    String DUMMY_PRODUCT_DESCRIPTION = "no description found";
    String DUMMY_PRODUCT_CATEGORY = "category";
    String BUNDLE = " bundle: ";

    float DUMMY_PRODUCT_PRICE = 0.99f;
    int LIMIT_DESCRIPTION_LENGTH = 1000;
    int LIMIT_RANDOM_VALUE = 100;

    int INVALID = -1;
    int ZERO = 0;
    int ONE = 1;
    int TWO = 2;
    int THREE = 3;
    int FOUR = 4;
    int FIVE = 5;
    int SIX = 6;
    int SEVEN = 7;
    int EIGHT = 8;
    int NINE = 9;
    int TEN = 10;

    int VALID = ONE;
    int NULLABLE_COLUMNS_COUNT = NINE;
    int COLUMNS_COUNT = TEN;
}
