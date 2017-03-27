package com.huangyu.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.huangyu.customviewlibrary.refreshandload_view.RefreshAndLoadListener;
import com.huangyu.customviewlibrary.refreshandload_view.RefreshAndLoadView;

public class MainActivity extends AppCompatActivity {

    private int mGroupCount = 0;
    private int mGroupSize = 10;

    private RefreshAndLoadView refreshAndLoadView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new RecyclerViewAdapter();
        refreshAndLoadView = (RefreshAndLoadView) findViewById(R.id.refresh_and_load_view);
        refreshAndLoadView.setLayoutManager(new LinearLayoutManager(this)).setAdapter(adapter).setRefreshAndLoadListener(new RefreshAndLoadListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoad() {
                load();
            }
        }).startRefresh();

//        List<String> mDataList = new ArrayList<>();
//        mDataList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490263950073&di=c245e6da88195cd1df4b414edab5dcde&imgtype=0&src=http%3A%2F%2Fimg.sootuu.com%2Fvector%2F2007-07-01%2F066%2F1%2F664.gif");
//        mDataList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490263893185&di=98a25c57a2c7a6270453133c41317fc2&imgtype=0&src=http%3A%2F%2Fimg.sootuu.com%2Fvector%2F200801%2F097%2F655.jpg");
//        mDataList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490263897676&di=20d6f9a3c5e82190259c5e53bacf290c&imgtype=0&src=http%3A%2F%2Fimg.sootuu.com%2Fvector%2F200801%2F097%2F656.jpg");
//        mDataList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490263901894&di=2da0e48167e07e093d04ff840dcde11a&imgtype=0&src=http%3A%2F%2Fimg.sootuu.com%2Fvector%2F200801%2F097%2F657.jpg");
//        mDataList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490263960022&di=303777ced56f439c568073b05da064a6&imgtype=0&src=http%3A%2F%2Fimg.sootuu.com%2Fvector%2F200801%2F097%2F658.jpg");
//        BannerView<String> bannerView = (BannerView<String>) findViewById(R.id.banner_view);
//        bannerView.setView(mDataList, new BannerViewCreator<String>() {
//            @Override
//            public View createView() {
//                ImageView imageView = new ImageView(MainActivity.this);
//                return imageView;
//            }
//
//            @Override
//            public void loadData(String data, int position, View view) {
//                Glide.with(MainActivity.this).load(data).into((ImageView) view);
//            }
//        });
//
//        LabelsView labelsView = (LabelsView) findViewById(R.id.labels_view);
//        List<String> dataList2 = new ArrayList<>();
//        dataList2.add("Android");
//        dataList2.add("iOS");
//        dataList2.add("Windows");
//        dataList2.add("Mac");
//        dataList2.add("Linux");
//        dataList2.add("Android");
//        dataList2.add("iOS");
//        dataList2.add("Windows");
//        dataList2.add("Mac");
//        dataList2.add("Linux");
//        dataList2.add("Android");
//        dataList2.add("iOS");
//        dataList2.add("Windows");
//        dataList2.add("Mac");
//        dataList2.add("Linux");
//        dataList2.add("Android");
//        dataList2.add("iOS");
//        dataList2.add("Windows");
//        dataList2.add("Mac");
//        dataList2.add("Linux");
//        dataList2.add("Android");
//        dataList2.add("iOS");
//        dataList2.add("Windows");
//        dataList2.add("Mac");
//        dataList2.add("Linux");
//        labelsView.setView(dataList2);
    }

    private void loadData() {
        refreshAndLoadView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adapter != null) {
                    adapter.addData(mGroupSize, mGroupCount);
                    adapter.notifyDataSetChanged();
                }
                refreshAndLoadView.setComplete();
            }
        }, 2000);
    }

    private void clearData() {
        if (adapter != null) {
            adapter.clearData();
        }
    }

    private void refresh() {
        mGroupCount = 1;
        clearData();
        loadData();
    }

    private void load() {
        mGroupCount++;
        loadData();
    }

}
