package com.huangyu.customview.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.List;

/**
 * 描述：
 * 作者：huangyu
 * 版本：V1.0
 * 创建时间：2017-3-23
 * 最后修改时间：2017-3-23
 */
public class BannerView<T> extends FrameLayout {

    private Context mContext;

    private ViewPager mViewPager;
    private BannerPagerAdapter<T> mAdapter;

    public BannerView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        mViewPager = new ViewPager(mContext);
        this.addView(mViewPager);
    }

    public void setView(List<T> dataList) {
        mAdapter = new BannerPagerAdapter<>(mContext, dataList);
        mViewPager.setAdapter(mAdapter);
    }

}
