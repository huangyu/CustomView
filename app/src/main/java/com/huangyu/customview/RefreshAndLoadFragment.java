package com.huangyu.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huangyu.customviewlibrary.refreshandload.CommonRecyclerAdapter;
import com.huangyu.customviewlibrary.refreshandload.RefreshAndLoadViewAdapter;
import com.huangyu.customviewlibrary.refreshandload.RefreshAndLoadListener;
import com.huangyu.customviewlibrary.refreshandload.RefreshAndLoadView;

/**
 * Created by huangyu on 2017-3-31.
 */
public class RefreshAndLoadFragment extends Fragment {

    private int mGroupCount = 0;
    private int mGroupSize = 10;

    private RefreshAndLoadView refreshAndLoadView;
    private RefreshAndLoadViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refreshandload, container, false);
        adapter = new RefreshAndLoadViewAdapter();
        adapter.setOnItemClick(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), "click item " + position, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemLongClick(new CommonRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getContext(), "long click item " + position, Toast.LENGTH_SHORT).show();
            }
        });
        refreshAndLoadView = (RefreshAndLoadView) view.findViewById(R.id.refresh_and_load_view);
        refreshAndLoadView.setLayoutManager(new LinearLayoutManager(getContext())).setAdapter(adapter).setRefreshAndLoadListener(new RefreshAndLoadListener() {
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
                    addData(mGroupSize, mGroupCount);
                }
                refreshAndLoadView.setComplete();
            }
        }, 2000);
    }

    private void addData(int groupSize, int groupCount) {
        int start = groupSize * (groupCount - 1);
        for (int i = start; i < groupSize * groupCount; i++) {
            adapter.addItem("item" + i);
        }
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
