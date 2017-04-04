package com.huangyu.customviewlibrary.refreshandload;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huangyu on 2017/4/4.
 */
public class CommonRecyclerViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mView;

    private CommonRecyclerViewHolder(View itemView) {
        super(itemView);
        this.mView = new SparseArray<>();
    }

    public static CommonRecyclerViewHolder getViewHolder(ViewGroup parent, int layoutId) {
        if(layoutId == 0) {
            return null;
        }
        return new CommonRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    public <T extends View> T getView(int viewId) {
        View view = mView.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mView.put(viewId, view);
        }
        return (T) view;
    }

}
