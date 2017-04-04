package com.huangyu.customviewlibrary.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huangyu on 2017-3-24.
 */
public class CanvasView extends View {

    public boolean isClear = false;
    public Path path = null;
    public Paint paint = null;

    private float mX, mY;
    private final RectF mDirtyRect = new RectF();
    private static final float HALF_STROKE_WIDTH = 5.0f;
    private static final int DEFAULT_STROKE_WIDTH = 10;

    private ViewGroup mParentViewGroup;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        path = new Path();
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void clear() {
        isClear = true;
        path.reset();
        invalidate();
    }

    public void eraser() {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public void setParentView(ViewGroup viewGroup) {
        this.mParentViewGroup = viewGroup;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    private void touchStart(float x, float y, MotionEvent event) {
        path.moveTo(x, y);
        resetDirtyRect(x, y);
        mX = x;
        mY = y;
        invalidate((int) (mDirtyRect.left - HALF_STROKE_WIDTH), (int) (mDirtyRect.top - HALF_STROKE_WIDTH),
                (int) (mDirtyRect.right + HALF_STROKE_WIDTH), (int) (mDirtyRect.bottom + HALF_STROKE_WIDTH));
    }

    private void touchMove(float x, float y, MotionEvent event) {
        int historySize = event.getHistorySize();
        for (int i = 0; i < historySize; i++) {
            float historicalX = event.getHistoricalX(i);
            float historicalY = event.getHistoricalY(i);
            path.quadTo(mX, mY, historicalX, historicalY);

            resetDirtyRect(historicalX, historicalY);
            mX = historicalX;
            mY = historicalY;

            invalidate((int) (mDirtyRect.left - HALF_STROKE_WIDTH), (int) (mDirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (mDirtyRect.right + HALF_STROKE_WIDTH), (int) (mDirtyRect.bottom + HALF_STROKE_WIDTH));
        }
        path.quadTo(mX, mY, x, y);

        resetDirtyRect(x, y);
        mX = x;
        mY = y;

        invalidate((int) (mDirtyRect.left - HALF_STROKE_WIDTH), (int) (mDirtyRect.top - HALF_STROKE_WIDTH),
                (int) (mDirtyRect.right + HALF_STROKE_WIDTH), (int) (mDirtyRect.bottom + HALF_STROKE_WIDTH));
    }

    private void touchUp(float x, float y, MotionEvent event) {
        path.moveTo(x, y);
        resetDirtyRect(x, y);
        mX = x;
        mY = y;
        invalidate((int) (mDirtyRect.left - HALF_STROKE_WIDTH), (int) (mDirtyRect.top - HALF_STROKE_WIDTH),
                (int) (mDirtyRect.right + HALF_STROKE_WIDTH), (int) (mDirtyRect.bottom + HALF_STROKE_WIDTH));
    }

    private void resetDirtyRect(float eventX, float eventY) {
        mDirtyRect.left = Math.min(mX, eventX);
        mDirtyRect.right = Math.max(mX, eventX);
        mDirtyRect.top = Math.min(mY, eventY);
        mDirtyRect.bottom = Math.max(mY, eventY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() < 2) {

            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mParentViewGroup != null) {
                        mParentViewGroup.requestDisallowInterceptTouchEvent(true);
                    }
                    touchStart(x, y, event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchMove(x, y, event);
                    break;
                case MotionEvent.ACTION_UP:
                    touchUp(x, y, event);
                    if (mParentViewGroup != null) {
                        mParentViewGroup.requestDisallowInterceptTouchEvent(false);
                    }
                    break;
            }
            invalidate();
        }
        return true;
    }

}
