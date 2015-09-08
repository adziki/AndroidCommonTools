package com.meyouhealth.myhandroid.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.meyouhealth.myhandroid.util.MYHFontHelper;

public class MYHEditText extends EditText {

    public MYHEditText(Context context) {
        super(context);
        init(null);
    }

    public MYHEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MYHEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        MYHFontHelper.applyFont(attrs, getContext(), this);
    }
}
