package com.huangyu.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.huangyu.customviewlibrary.canvas.DrawView;

/**
 * Created by huangyu on 2017-3-31.
 */
public class CanvasFragment extends Fragment {

    private DrawView drawView;
    private Button btnClear, btnEraser, btnBack, btnForward;

    private boolean isEarser;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_canvas, container, false);
        drawView = (DrawView) view.findViewById(R.id.drawview);
        drawView.setParentView(((MainActivity) getActivity()).viewPager);

        btnClear = (Button) view.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.clear();

                isEarser = false;
                btnEraser.setText("earser");
                drawView.setPaint();
            }
        });

        btnEraser = (Button) view.findViewById(R.id.btn_eraser);
        btnEraser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEarser) {
                    btnEraser.setText("earser");
                    drawView.setPaint();
                } else {
                    btnEraser.setText("paint");
                    drawView.setEraser();
                }
                isEarser = !isEarser;
            }
        });

        btnBack = (Button) view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.goBack();
            }
        });

        btnForward = (Button) view.findViewById(R.id.btn_forward);
        btnForward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.goForward();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        drawView.clearBitmap();
        super.onDestroy();
    }

}
