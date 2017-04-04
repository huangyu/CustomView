package com.huangyu.customviewlibrary.refreshandload;

import android.widget.TextView;

import com.huangyu.customviewlibrary.R;

/**
 * Created by huangyu on 2017-3-27.
 */
public class RefreshAndLoadViewAdapter extends CommonRecyclerAdapter<String> {

    public RefreshAndLoadViewAdapter() {
        super();
    }

    @Override
    public void convert(CommonRecyclerViewHolder holder, String data, int position) {
        int itemType = getItemViewType(position);
        if(itemType == TYPE_HEADER || itemType == TYPE_NORMAL) {
            ((TextView) holder.getView(R.id.tv_title)).setText(data);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.list_item_normal;
    }

    @Override
    public int getHeadLayoutResource() {
        return R.layout.list_item_normal;
    }

    @Override
    public int getFootLayoutResource() {
        return R.layout.list_item_foot;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size() + 1;
    }

}
