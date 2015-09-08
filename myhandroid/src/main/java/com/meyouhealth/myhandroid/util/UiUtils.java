package com.meyouhealth.myhandroid.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UiUtils {
    public static final int KEYBOARD_HEIGHT = 150;  //good static size for comparing if keyboard visible or not

    public static void hideSoftKeyboard(Activity activity) {
        if(activity == null || activity.getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                .getWindowToken(), 0);
    }

    public static boolean isSoftKeyboardShowing(Activity activity, View activityRootView) {

        Rect r = new Rect();
        activityRootView.getWindowVisibleDisplayFrame(r);
        int screenHeight = activityRootView.getRootView().getHeight();
        int keypadHeight = screenHeight - r.bottom;
        int actionBarHeight = 0;

        if (activity instanceof AppCompatActivity) {
            actionBarHeight = ((AppCompatActivity)activity).getSupportActionBar().getHeight();
        } else if (activity.getActionBar() != null) {
            actionBarHeight = activity.getActionBar().getHeight();
        }

        if (keypadHeight - actionBarHeight > KEYBOARD_HEIGHT) {
            return true;
        }

        return false;
    }

    public static float dpFromPx(Context context, float px)
    {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int pxFromDp(Context context, float dp)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
