package com.huangyu.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huangyu.customviewlibrary.banner.BannerView;
import com.huangyu.customviewlibrary.banner.BannerViewCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyu on 2017-3-31.
 */
public class BannerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner, container, false);
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
        return view;
    }

}
