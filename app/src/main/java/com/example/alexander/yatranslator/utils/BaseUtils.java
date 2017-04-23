package com.example.alexander.yatranslator.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class BaseUtils {
//    public static void hideKeyboard(Activity activity, View view) {
//        if (view == null)
//            return;
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
//    }
public static void hideKeyboard(Context ctx) {
    InputMethodManager inputManager = (InputMethodManager) ctx
            .getSystemService(Context.INPUT_METHOD_SERVICE);

    View v = ((Activity) ctx).getCurrentFocus();
    if (v == null)
        return;

    Log.d("[Debug]", "View found!");

    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
}
}
