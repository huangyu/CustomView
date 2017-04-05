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
    public Path mPath = null;
    public Paint mPaint = null;

    private float mX, mY;
    private final RectF mDirtyRect = new RectF();
    private static final float HALF_STROKE_WIDTH = 5.0f;
    private static final int DEFAULT_STROKE_WIDTH = 10;

    private ViewGroup mParentViewGroup;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        setPaint();
    }

    public void clear() {
        isClear = true;
        mPath.reset();
        invalidate();
    }

    public void setPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
    }

    public void setEraser() {
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public void setParentView(ViewGroup viewGroup) {
        this.mParentViewGroup = viewGroup;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    private void touchStart(float x, float y, MotionEvent event) {
        mPath.moveTo(x, y);
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
            mPath.quadTo(mX, mY, historicalX, historicalY);

            resetDirtyRect(historicalX, historicalY);
            mX = historicalX;
            mY = historicalY;

            invalidate((int) (mDirtyRect.left - HALF_STROKE_WIDTH), (int) (mDirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (mDirtyRect.right + HALF_STROKE_WIDTH), (int) (mDirtyRect.bottom + HALF_STROKE_WIDTH));
        }
        mPath.quadTo(mX, mY, x, y);

        resetDirtyRect(x, y);
        mX = x;
        mY = y;

        invalidate((int) (mDirtyRect.left - HALF_STROKE_WIDTH), (int) (mDirtyRect.top - HALF_STROKE_WIDTH),
                (int) (mDirtyRect.right + HALF_STROKE_WIDTH), (int) (mDirtyRect.bottom + HALF_STROKE_WIDTH));
    }

    private void touchUp(float x, float y, MotionEvent event) {
        mPath.moveTo(x, y);
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
