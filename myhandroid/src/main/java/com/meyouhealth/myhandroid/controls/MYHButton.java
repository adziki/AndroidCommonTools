package com.meyouhealth.myhandroid.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.meyouhealth.myhandroid.util.MYHFontHelper;

public class MYHButton extends Button {

    public MYHButton(Context context) {
        super(context);
        init(null);
    }

    public MYHButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MYHButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        MYHFontHelper.applyFont(attrs, getContext(), this);
    }
}
