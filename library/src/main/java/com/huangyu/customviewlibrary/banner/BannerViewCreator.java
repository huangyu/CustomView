package com.huangyu.customviewlibrary.banner;

import android.view.View;

/**
 * Created by huangyu on 2017/3/25.
 */

public interface BannerViewCreator<T> {
    View createView(int position);
    void loadData(T data, int position, View view);
}
