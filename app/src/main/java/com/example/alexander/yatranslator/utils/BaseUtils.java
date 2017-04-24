package com.example.alexander.yatranslator.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

public class BaseUtils {
    public static void showMessage(View view, int resourceString) {
        if(view != null) {
            Log.d("[Debug]", "showMessage " + resourceString);
            Snackbar.make(view, resourceString, Snackbar.LENGTH_LONG).show();
        }
        Log.d("[Debug]", "showMessage null");
    }

    public static boolean canTranslate(String text) {
        text = text.trim();
        return text != null && !text.isEmpty();
    }

    public static boolean canInsert(String text, List<String> translations) {
        return canTranslate(text)
                && translations != null
                && !translations.isEmpty()
                && translations.get(0) != text;
    }

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
