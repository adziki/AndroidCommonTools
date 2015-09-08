package com.meyouhealth.myhandroid.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.meyouhealth.myhandroid.R;

public class MYHFontHelper {

    public synchronized static void applyFont(AttributeSet attrs, Context context, TextView textView) {
        TypedArray attributeArray = null;
        String fontName = context.getString(R.string.myh_default_font);
        if (attrs != null) {
            attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MYHTextView);
            String font = attributeArray.getString(R.styleable.MYHTextView_font);
            if(!TextUtils.isEmpty(font)) {
                fontName = font;
            }
        }
        if (!TextUtils.isEmpty(fontName)) {
            FontHelper fontHelper = new FontHelper(context);
            textView.setTypeface(fontHelper.loadTypeface(fontName));
        }
        if (attributeArray != null) {
            attributeArray.recycle();
        }
    }
}
