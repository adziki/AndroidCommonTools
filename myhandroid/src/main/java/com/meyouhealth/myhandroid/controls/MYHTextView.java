package com.meyouhealth.myhandroid.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.meyouhealth.myhandroid.util.MYHFontHelper;

public class MYHTextView extends TextView {

    public MYHTextView(Context context) {
        super(context);
        init(null);
    }

    public MYHTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MYHTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        MYHFontHelper.applyFont(attrs, getContext(), this);
    }
}
