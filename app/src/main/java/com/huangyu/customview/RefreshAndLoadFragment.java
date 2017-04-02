package com.huangyu.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangyu.customviewlibrary.refreshandload_view.RecyclerViewAdapter;
import com.huangyu.customviewlibrary.refreshandload_view.RefreshAndLoadListener;
import com.huangyu.customviewlibrary.refreshandload_view.RefreshAndLoadView;

/**
 * Created by huangyu on 2017-3-31.
 */
public class RefreshAndLoadFragment extends Fragment {

    private int mGroupCount = 0;
    private int mGroupSize = 10;

    private RefreshAndLoadView refreshAndLoadView;
    private RecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refreshandload, container, false);
        adapter = new RecyclerViewAdapter();
        refreshAndLoadView = (RefreshAndLoadView) view.findViewById(R.id.refresh_and_load_view);
        refreshAndLoadView.setLayoutManager(new LinearLayoutManager(getActivity())).setAdapter(adapter).setRefreshAndLoadListener(new RefreshAndLoadListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoad() {
                load();
            }
        }).startRefresh();
        return view;
    }

    private void loadData(final boolean isRefresh) {
        refreshAndLoadView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adapter != null) {
                    if (isRefresh) {
                        adapter.clearData();
                    }
                    adapter.addData(mGroupSize, mGroupCount);
                    adapter.notifyDataSetChanged();
                }
                refreshAndLoadView.setComplete();
            }
        }, 2000);
    }

    private void refresh() {
        mGroupCount = 1;
        loadData(true);
    }

    private void load() {
        mGroupCount++;
        loadData(false);
    }

}
