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

    private String mTabTitles[] = new String[]{"Banner", "Canvas", "Labels", "RefreshAndLoad"};

    public FragmentsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position);
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