package com.meyouhealth.myhandroid.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

public class AnimationUtils {

    private int ANIMATION_DURATION = 500;//in milliseconds

    private Context mContext;

    public AnimationUtils(Context c) {
        mContext = c;
    }

    public AnimationUtils() {
        // intentionally left blank
    }

    public void expand(final View v) {

        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(ANIMATION_DURATION);

        v.startAnimation(a);
    }

    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(ANIMATION_DURATION);

        v.startAnimation(a);
    }

    public interface PopUpCallback {
        void onDoneSlidingFromTop(View view, int endMargin);
        void onDoneSlidingDownOut();
    }

    private static final long POP_UP_HOVER_TIME_MILLIS = 1500;

    public void popUpFull(View view, float startMargin) {

        popUpSlideFromTop(view, startMargin, new PopUpCallback() {
            @Override
            public void onDoneSlidingFromTop(View view, int endMargin) {
                popUpSlideDownOut(view, endMargin, POP_UP_HOVER_TIME_MILLIS, this);
            }

            @Override
            public void onDoneSlidingDownOut() {
                // intentionally left blank
            }
        });
    }

    private static final int SLIDE_DOWN_END_Y_POSITION_DP = 95;

    public void popUpSlideFromTop(final View view, final float startMargin,
                                  final PopUpCallback callback) {
        if (mContext == null) return;

        int endPosition = UiUtils.pxFromDp(mContext, SLIDE_DOWN_END_Y_POSITION_DP);
        final float endMargin = endPosition + (-startMargin);
        final int startMarginInt = (int)startMargin;

        Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                applyDownwardTransformation(view, startMarginInt, (int)endMargin, interpolatedTime);
            }
        };
        a.setZAdjustment(Animation.ZORDER_TOP);
        a.setDuration(ANIMATION_DURATION);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //intentionally left blank
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callback != null) {
                    int topMargin = getTopMargin(view);
                    callback.onDoneSlidingFromTop(view, topMargin);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //intentionally left blank
            }
        });
        a.setDuration(1000);
        view.setAnimation(a);
        a.start();
    }

    private int getTopMargin(View view) {
        try {
            ViewGroup.MarginLayoutParams margins =
                    (ViewGroup.MarginLayoutParams)view.getLayoutParams();
            if (margins != null) {
                return margins.topMargin;
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private void applyDownwardTransformation(View view, int start, int end, float interpolatedTime) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                view.getLayoutParams();
        params.setMargins(0, start + (int)(interpolatedTime * end), 0, 0);
        view.setLayoutParams(params);
    }

    @TargetApi(11)
    private int getViewHeightBelowThirteen() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getHeight();
    }

    @TargetApi(13)
    private int getViewHeight() {
        Point size = new Point();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(size);
        return size.y;
    }

    @TargetApi(13)
    public void popUpSlideDownOut(final View view, final int startingMargin, long delay,
                                  final PopUpCallback callback) {

        int height;
        if (Build.VERSION.SDK_INT >= 13) {
            height = getViewHeight();
        } else {
            height = getViewHeightBelowThirteen();
        }

        if (height < 1) {
            //Crashlytics.log(Log.ERROR, "AnimUtils:popUpSlideDownOut", "Could not get screen height.");
            view.setVisibility(View.INVISIBLE);
            return;
        }

        final int heightToUse = height;

        Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                applyDownwardTransformation(view, startingMargin, heightToUse, interpolatedTime);
            }
        };
        a.setZAdjustment(Animation.ZORDER_TOP);
        a.setDuration(ANIMATION_DURATION);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //intentionally left blank
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                if (callback != null) {
                    callback.onDoneSlidingDownOut();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //intentionally left blank
            }
        });

        a.setDuration(1000);
        a.setStartOffset(delay);
        view.setAnimation(a);
        a.start();
    }
}
