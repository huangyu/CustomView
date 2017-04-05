package com.huangyu.customviewlibrary;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by huangyu on 2017-4-5.
 */
public class Utils {

    private Utils() {
    }

    /**
     * 获取屏幕密度
     *
     * @return
     */
    public static float getScreenDensity() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.density;
    }

}
