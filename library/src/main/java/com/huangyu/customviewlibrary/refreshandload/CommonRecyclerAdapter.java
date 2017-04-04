package com.huangyu.customviewlibrary.refreshandload;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyu on 2017/4/4.
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewHolder> {

    protected static final int TYPE_NORMAL = 0;
    protected static final int TYPE_HEADER = 1;
    protected static final int TYPE_FOOTER = 2;

    protected List<T> mDataList;

    protected OnItemClickListener mOnItemClick;
    protected OnItemLongClickListener mOnItemLongClick;

    public CommonRecyclerAdapter() {
        mDataList = new ArrayList<>();
    }

    public void setData(List<T> list) {
        mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(T data) {
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addItem(T data, int position) {
        mDataList.add(position, data);
        notifyItemInserted(position);
    }

    public void removeItem(int positon) {
        mDataList.remove(positon);
        notifyItemRemoved(positon);
    }

    public void clearData() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClickListener onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    public void setOnItemLongClick(OnItemLongClickListener onItemLongClick) {
        this.mOnItemLongClick = onItemLongClick;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        CommonRecyclerViewHolder holder;

        if (viewType == TYPE_HEADER) {
            holder = CommonRecyclerViewHolder.getViewHolder(viewGroup, getHeadLayoutResource());
        } else if (viewType == TYPE_FOOTER) {
            holder = CommonRecyclerViewHolder.getViewHolder(viewGroup, getFootLayoutResource());
        } else {
            holder = CommonRecyclerViewHolder.getViewHolder(viewGroup, getLayoutResource());
        }

        if (holder == null) {
            return null;
        }

        if (mOnItemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonRecyclerViewHolder holder = (CommonRecyclerViewHolder) v.getTag();
                    mOnItemClick.onItemClick(v, holder.getLayoutPosition());
                }
            });
        }
        if (mOnItemLongClick != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CommonRecyclerViewHolder holder = (CommonRecyclerViewHolder) v.getTag();
                    mOnItemLongClick.onItemLongClick(v, holder.getLayoutPosition());
                    return false;
                }
            });
        }

        holder.itemView.setTag(holder);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        if (holder != null && position < mDataList.size()) {
            convert(holder, mDataList.get(position), position);
        }
    }

    public abstract void convert(CommonRecyclerViewHolder holder, T data, int position);

    public abstract int getLayoutResource();

    public int getHeadLayoutResource() {
        return 0;
    }

    public int getFootLayoutResource() {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

}
