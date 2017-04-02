package com.huangyu.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangyu.customviewlibrary.labels_view.LabelsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyu on 2017-3-31.
 */
public class LabelsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_labels, container, false);
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
        return view;
    }

}
