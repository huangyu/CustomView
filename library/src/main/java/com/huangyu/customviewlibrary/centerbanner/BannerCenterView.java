package com.huangyu.customviewlibrary.centerbanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.huangyu.customviewlibrary.R;
import com.huangyu.customviewlibrary.Utils;
import com.huangyu.customviewlibrary.banner.BannerViewCreator;

import java.util.List;

/**
 * Created by huangyu on 2017-5-22.
 */
public class BannerCenterView<T> extends RelativeLayout {

    private int mWidth;
    private int mHeight;

    private ViewPager mViewPager;
    private CenterBannerPagerAdapter<T> mAdapter;

    public BannerCenterView(Context context) {
        super(context);
        init();
    }

    public BannerCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttribute(context, attrs);
        init();
    }

    private void init() {
        this.setClipChildren(false);
        this.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        initViewPager();
    }

    private void initViewPager() {
        mViewPager = new ViewPager(getContext());
        mViewPager.setClipChildren(false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (mWidth * Utils.getScreenDensity()), (int) (mHeight * Utils.getScreenDensity()));
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mViewPager.setLayoutParams(layoutParams);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPager.setPageTransformer(true, new ScalePageTransformer());
        this.addView(mViewPager);
    }

    public void setView(List<T> dataList, BannerViewCreator<T> creator) {
        mAdapter = new CenterBannerPagerAdapter<>(dataList, creator);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mAdapter.getCount());
    }

    private void getAttribute(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CenterBannerView);
        try {
            mWidth = a.getInteger(R.styleable.CenterBannerView_viewWidth, 200);
            mHeight = a.getInteger(R.styleable.CenterBannerView_viewHeight, 200);
        } finally {
            a.recycle();
        }
    }

}
