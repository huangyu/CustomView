package com.huangyu.customview;

/**
 * Created by huangyu on 2017-3-31.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by huangyu on 2017-3-31.
 */
public class FragmentsAdapter extends FragmentPagerAdapter {

    private String mTabTitles[] = new String[]{"Banner", "Canvas", "Labels", "RAndL"};

    private Fragment bannerFragment;
    private Fragment labelsFragment;
    private Fragment canvasFragment;
    private Fragment refreshAndLoadFragment;

    public FragmentsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        initFragments();
    }

    private void initFragments() {
        bannerFragment = new BannerFragment();
        labelsFragment = new LabelsFragment();
        canvasFragment = new CanvasFragment();
        refreshAndLoadFragment = new RefreshAndLoadFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return bannerFragment;
            case 1:
                return canvasFragment;
            case 2:
                return labelsFragment;
            case 3:
                return refreshAndLoadFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return mTabTitles == null ? 0 : mTabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }

}