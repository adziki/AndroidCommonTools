package com.meyouhealth.myhandroid.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.meyouhealth.myhandroid.R;

public class FontHelper {

    private Context mContext;

    public FontHelper(Context context) {
        mContext = context;
    }

    public Typeface loadTypeface(String fileName) {
        if(!TextUtils.isEmpty(fileName)) {
            try {
                Typeface specifiedFont = Typeface.createFromAsset(mContext.getAssets(),
                        mContext.getString(R.string.font_path_prefix) + fileName);
                if(specifiedFont != null) {
                    return specifiedFont;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //absolute default
        String defaultFontFileName = mContext.getString(R.string.myh_default_font);
        if(TextUtils.isEmpty(defaultFontFileName)) {
            defaultFontFileName = mContext.getString(R.string.font_helvetica_new);
        }
        return Typeface.createFromAsset(mContext.getAssets(),
                mContext.getString(R.string.font_path_prefix) + defaultFontFileName);
    }
}
