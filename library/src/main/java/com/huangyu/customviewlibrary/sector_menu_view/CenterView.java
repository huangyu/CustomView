package com.huangyu.customviewlibrary.sector_menu_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;


/**
 * Created by huangyu on 2017/4/5.
 */
public class CenterView extends View {

    private Bitmap mBitmap;
    public int mBitmapWidth;
    public int mBitmapHeight;

    public CenterView(Context context) {
        super(context);
    }

    public void setBitmapResourse(int id) {
        mBitmap = BitmapFactory.decodeResource(getResources(), id);
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mBitmapWidth, mBitmapHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

}
