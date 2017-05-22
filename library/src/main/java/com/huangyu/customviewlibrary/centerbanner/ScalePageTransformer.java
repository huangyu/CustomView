package com.huangyu.customviewlibrary.centerbanner;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by huangyu on 2017-5-22.
 */
public class ScalePageTransformer implements ViewPager.PageTransformer {

    static final float MAX_SCALE = 1.3f;
    static final float MIN_SCALE = 0.6f;

    @Override
    public void transformPage(View page, float position) {
        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }

        float tempScale = position < 0 ? 1 + position : 1 - position;

        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        float scaleValue = MIN_SCALE + tempScale * slope;
        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.getParent().requestLayout();
        }
    }

}
