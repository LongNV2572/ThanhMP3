package com.example.baseproduct.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class Utils {
    public static void hideKeyboard(Context context, View view) {
        if (context == null || view == null) return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
