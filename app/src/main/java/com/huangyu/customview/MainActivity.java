package com.huangyu.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.huangyu.customview.banner.BannerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> datas = new ArrayList<>();
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490260341476&di=819da57599984079d96f3c901dd421e3&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F12%2F65%2F04%2F16658PICNpU.jpg");
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490260341475&di=c589131f8324d240b73f734dab494f4e&imgtype=0&src=http%3A%2F%2Fimg.wallpaperlist.com%2Fuploads%2Fwallpaper%2Ffiles%2Fand%2Fandroid-logo-wallpaper-5425a4c666803.jpg");
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490260341475&di=162bf35fbd0c7bd25823632a55471977&imgtype=0&src=http%3A%2F%2Fimg0.pconline.com.cn%2Fpconline%2F1206%2F18%2F2829090_3867bd63fd673471aa184c02_500.jpg");
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490260341475&di=be76631b96ca6cf5088cfd09754e5abf&imgtype=0&src=http%3A%2F%2Fpx.thea.cn%2FPublic%2FUpload%2F2467637%2FIntro%2F1426853144.jpg");
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490260341475&di=06485321de1211ad2f7415b7116fb5ee&imgtype=0&src=http%3A%2F%2Ffile.quweiwu.com%2Fnews%2F20151030102559902.jpg");

        BannerView<String> bannerView = (BannerView<String>) findViewById(R.id.banner_view);
        bannerView.setView(datas);
    }

}
