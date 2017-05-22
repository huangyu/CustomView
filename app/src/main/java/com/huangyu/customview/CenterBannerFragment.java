package com.huangyu.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huangyu.customviewlibrary.banner.BannerViewCreator;
import com.huangyu.customviewlibrary.centerbanner.BannerCenterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyu on 2017-3-31.
 */
public class CenterBannerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_center_banner, container, false);
        List<String> dataList = new ArrayList<>();
        dataList.add("http://img0.imgtn.bdimg.com/it/u=4040470692,460373187&fm=214&gp=0.jpg");
        dataList.add("http://img2.niutuku.com/desk/1207/1043/ntk120541.jpg");
        dataList.add("http://dl.bizhi.sogou.com/images/1680x1050/2014/04/24/590270.jpg");
        dataList.add("http://img2.niutuku.com/desk/130220/23/23-niutuku.com-246.jpg");
        dataList.add("http://bizhi.zhuoku.com/2014/01/24/zhuoku/zhuoku210.jpg");

        BannerCenterView<String> bannerCenterView = (BannerCenterView<String>) view.findViewById(R.id.banner_center_view);
        bannerCenterView.setView(dataList, new BannerViewCreator<String>() {
            @Override
            public View createView(final int position) {
                return new ImageView(getContext());
            }

            @Override
            public void loadData(String data, int position, View view) {
                Glide.with(getContext()).load(data).into((ImageView) view);
            }
        });
        return view;
    }

}
