package com.huangyu.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huangyu.customviewlibrary.banner.BannerView;
import com.huangyu.customviewlibrary.banner.BannerViewCreator;
import com.huangyu.customviewlibrary.canvas_view.CanvasView;
import com.huangyu.customviewlibrary.labels_view.LabelsView;
import com.huangyu.customviewlibrary.refreshandload_view.RefreshAndLoadListener;
import com.huangyu.customviewlibrary.refreshandload_view.RefreshAndLoadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyu on 2017-3-31.
 */
public class PageFragment extends Fragment {

    public static final String ARGS_PAGE = "args_page";
    private int mPage;

    private int mGroupCount = 0;
    private int mGroupSize = 10;

    private RefreshAndLoadView refreshAndLoadView;
    private RecyclerViewAdapter adapter;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        switch (mPage) {
            case 0:
                view = inflater.inflate(R.layout.fragment_banner, container, false);
                List<String> dataList = new ArrayList<>();
                dataList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490263950073&di=c245e6da88195cd1df4b414edab5dcde&imgtype=0&src=http%3A%2F%2Fimg.sootuu.com%2Fvector%2F2007-07-01%2F066%2F1%2F664.gif");
                dataList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490263893185&di=98a25c57a2c7a6270453133c41317fc2&imgtype=0&src=http%3A%2F%2Fimg.sootuu.com%2Fvector%2F200801%2F097%2F655.jpg");
                dataList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490263897676&di=20d6f9a3c5e82190259c5e53bacf290c&imgtype=0&src=http%3A%2F%2Fimg.sootuu.com%2Fvector%2F200801%2F097%2F656.jpg");
                dataList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490263901894&di=2da0e48167e07e093d04ff840dcde11a&imgtype=0&src=http%3A%2F%2Fimg.sootuu.com%2Fvector%2F200801%2F097%2F657.jpg");
                dataList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490263960022&di=303777ced56f439c568073b05da064a6&imgtype=0&src=http%3A%2F%2Fimg.sootuu.com%2Fvector%2F200801%2F097%2F658.jpg");
                BannerView<String> bannerView = (BannerView<String>) view.findViewById(R.id.banner_view);
                bannerView.setView(dataList, new BannerViewCreator<String>() {
                    @Override
                    public View createView() {
                        ImageView imageView = new ImageView(getActivity());
                        return imageView;
                    }

                    @Override
                    public void loadData(String data, int position, View view) {
                        Glide.with(getActivity()).load(data).into((ImageView) view);
                    }
                });
                break;
            case 1:
                view = inflater.inflate(R.layout.fragment_canvas, container, false);
                CanvasView canvasView = (CanvasView) view.findViewById(R.id.canvas);
                canvasView.setParentView(((MainActivity) getActivity()).viewPager);
                break;
            case 2:
                view = inflater.inflate(R.layout.fragment_labels, container, false);
                LabelsView labelsView = (LabelsView) view.findViewById(R.id.labels_view);
                List<String> dataList2 = new ArrayList<>();
                dataList2.add("Android");
                dataList2.add("iOS");
                dataList2.add("Windows");
                dataList2.add("Mac");
                dataList2.add("Linux");
                dataList2.add("Android");
                dataList2.add("iOS");
                dataList2.add("Windows");
                dataList2.add("Mac");
                dataList2.add("Linux");
                dataList2.add("Android");
                dataList2.add("iOS");
                dataList2.add("Windows");
                dataList2.add("Mac");
                dataList2.add("Linux");
                dataList2.add("Android");
                dataList2.add("iOS");
                dataList2.add("Windows");
                dataList2.add("Mac");
                dataList2.add("Linux");
                dataList2.add("Android");
                dataList2.add("iOS");
                dataList2.add("Windows");
                dataList2.add("Mac");
                dataList2.add("Linux");
                labelsView.setView(dataList2);
                break;
            case 3:
                view = inflater.inflate(R.layout.fragment_refreshandload, container, false);
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
                break;
        }
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
