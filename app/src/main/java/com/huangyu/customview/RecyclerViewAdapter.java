package com.huangyu.customview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyu on 2017-3-27.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public List<String> mDataList;

    public RecyclerViewAdapter() {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTvTitle.setText(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTitle;

        ViewHolder(View view) {
            super(view);
            mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        }
    }

}
