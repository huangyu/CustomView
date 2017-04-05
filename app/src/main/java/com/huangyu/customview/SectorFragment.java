package com.huangyu.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huangyu.customviewlibrary.sector_menu_view.SectorMenuView;

/**
 * Created by huangyu on 2017-3-31.
 */
public class SectorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sector, container, false);
        final SectorMenuView sectorMenuView = (SectorMenuView) view.findViewById(R.id.sector_menu_view);
        String[] textArray = new String[]{"item1", "item2", "item3", "item4", "item5", "item6"};
        sectorMenuView.init(getContext(), textArray);
        sectorMenuView.setOnSectorButtonClick(new SectorMenuView.SectorButtonClickListener() {
            @Override
            public void onSectorButtonClick(int position, String text) {
                Toast.makeText(getContext(), "click " + text, Toast.LENGTH_SHORT).show();
                sectorMenuView.openOrCloseMenu(true);
            }
        });
        return view;
    }

}
