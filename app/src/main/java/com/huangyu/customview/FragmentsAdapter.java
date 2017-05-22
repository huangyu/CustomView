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

    private String mTabTitles[] = new String[]{"1", "2", "3", "4", "5", "6"};

    private Fragment bannerFragment;
    private Fragment centerBannerFragment;
    private Fragment labelsFragment;
    private Fragment canvasFragment;
    private Fragment refreshAndLoadFragment;
    private Fragment sectorFragment;

    public FragmentsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        initFragments();
    }

    private void initFragments() {
        bannerFragment = new BannerFragment();
        centerBannerFragment = new CenterBannerFragment();
        labelsFragment = new LabelsFragment();
        canvasFragment = new CanvasFragment();
        refreshAndLoadFragment = new RefreshAndLoadFragment();
        sectorFragment = new SectorFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return bannerFragment;
            case 1:
                return centerBannerFragment;
            case 2:
                return canvasFragment;
            case 3:
                return labelsFragment;
            case 4:
                return refreshAndLoadFragment;
            case 5:
                return sectorFragment;
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