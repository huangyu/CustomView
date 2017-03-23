package com.huangyu.customview.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 描述：
 * 作者：huangyu
 * 版本：V1.0
 * 创建时间：2017-3-23
 * 最后修改时间：2017-3-23
 */
class BannerPagerAdapter<T> extends PagerAdapter {

    private Context mContext;
    private List<T> mDataList;
    private SparseArray<View> mViews;

    BannerPagerAdapter(Context context, List<T> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
        this.mViews = new SparseArray<>();
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
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);

        if (view == null) {
            view = new ImageView(mContext);
            mViews.put(position, view);
        }

        T data = mDataList.get(position);
        Glide.with(mContext).load(data).into((ImageView) view);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

}
