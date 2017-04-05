package com.huangyu.customviewlibrary.sector_menu_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import com.huangyu.customviewlibrary.R;


/**
 * Created by huangyu on 2017/4/5.
 */
public class CenterView extends View {

    private Bitmap backgroundBitmap;
    public int bitmapWidth;
    public int bitmapHeight;

    public CenterView(Context context) {
        super(context);
        setBitmapResourse(R.drawable.btn_tool_nor);
    }

    public void setBitmapResourse(int id) {
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), id);
        bitmapWidth = backgroundBitmap.getWidth();
        bitmapHeight = backgroundBitmap.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(bitmapWidth, bitmapHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
    }

}
