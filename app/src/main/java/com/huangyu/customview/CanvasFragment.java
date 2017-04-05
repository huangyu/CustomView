package com.huangyu.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.huangyu.customviewlibrary.canvas.CanvasView;

/**
 * Created by huangyu on 2017-3-31.
 */
public class CanvasFragment extends Fragment {

    private boolean isEraser;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_canvas, container, false);
        final CanvasView canvasView = (CanvasView) view.findViewById(R.id.canvas);
        canvasView.setParentView(((MainActivity) getActivity()).viewPager);

        final Button btnClear = (Button) view.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.clear();
            }
        });
        final Button btnEraser = (Button) view.findViewById(R.id.btn_eraser);
        btnEraser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEraser) {
                    canvasView.setPaint();
                    btnEraser.setText("eraser");
                } else {
                    canvasView.setEraser();
                    btnEraser.setText("paint");
                }
                isEraser = !isEraser;
            }
        });
        return view;
    }

}
