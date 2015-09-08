package com.meyouhealth.myhandroid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class MYHBaseActivity extends AppCompatActivity {

    // extending classes can override this function with their page name
    protected String getTrackPath() {
        return getClass().getSimpleName();
    }

    protected AtomicBoolean mIsRunning = new AtomicBoolean(false);
    private Context mContext;

    @Override
    protected void onPause() {
        super.onPause();
        mIsRunning.set(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContext = this;
        mIsRunning.set(true);
    }

    public synchronized boolean isRunning() {
        return mIsRunning.get();
    }
}
