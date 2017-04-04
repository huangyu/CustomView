package com.huangyu.customviewlibrary.refreshandload_view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huangyu.customviewlibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyu on 2017-3-27.
 */
public class RefreshAndLoadViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public List<String> mDataList;

    public RefreshAndLoadViewAdapter() {
        this.mDataList = new ArrayList<>();
    }

    public void addData(int groupSize, int groupCount) {
        if (mDataList == null) {
            return;
        }

        int start = groupSize * (groupCount - 1);
        for (int i = start; i < groupSize * groupCount; i++) {
            mDataList.add("item" + i);
        }
    }

    public void clearData() {
        if (mDataList == null) {
            return;
        }

        mDataList.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_normal, parent, false);
            return new NormalViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_foot, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            ((NormalViewHolder) holder).mTvTitle.setText(mDataList.get(position));
        } else if (holder instanceof FootViewHolder) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size() + 1;
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTitle;

        NormalViewHolder(View view) {
            super(view);
            mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

        ProgressBar mProgressBar;

        FootViewHolder(View view) {
            super(view);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        }
    }

}
