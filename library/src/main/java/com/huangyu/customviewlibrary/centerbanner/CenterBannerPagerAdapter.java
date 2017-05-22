package com.huangyu.customviewlibrary.centerbanner;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.huangyu.customviewlibrary.banner.BannerViewCreator;

import java.util.List;

/**
 * Created by huangyu on 2017-5-22.
 */
public class CenterBannerPagerAdapter<T> extends PagerAdapter {

    private List<T> mDataList;
    private SparseArray<View> mViews;
    private BannerViewCreator<T> mCreater;

    public CenterBannerPagerAdapter(List<T> dataList, BannerViewCreator<T> creator) {
        this.mDataList = dataList;
        this.mViews = new SparseArray<>();
        this.mCreater = creator;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mViews.get(position);

        if (view == null) {
            view = mCreater.createView(position);
            mViews.put(position, view);
        }

        T data = mDataList.get(position);

        mCreater.loadData(data, position, view);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

}
