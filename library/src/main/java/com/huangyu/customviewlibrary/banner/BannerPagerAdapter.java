package com.huangyu.customviewlibrary.banner;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by huangyu on 2017-3-23.
 */
class BannerPagerAdapter<T> extends PagerAdapter {

    private List<T> mDataList;
    private SparseArray<View> mViews; // mViews.size() = mDataList.size() + 2
    private BannerViewCreator<T> mCreater;

    BannerPagerAdapter(List<T> dataList, BannerViewCreator<T> creator) {
        this.mDataList = dataList;
        this.mViews = new SparseArray<>();
        this.mCreater = creator;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size() + 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);

        if (view == null) {
            view = mCreater.createView();
            mViews.put(position, view);
        }

        T data = mDataList.get(getRealPosition(position));

        mCreater.loadData(data, position, view);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    /**
     * 此处用于获取真实的序号，如下对应所示：
     * a,b,c,e,f  mViews
     * 2,0,1,2,0  mDataList
     *
     * @param position
     * @return
     */
    private int getRealPosition(int position) {
        if (position == 0) {
            return mDataList.size() - 1;
        } else if (position == getCount() - 1) {
            return 0;
        } else {
            return position - 1;
        }
    }

}
