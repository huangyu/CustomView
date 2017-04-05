package com.huangyu.customviewlibrary.sector_menu_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.View;

import com.huangyu.customviewlibrary.Utils;

/**
 * Created by huangyu on 2017/4/5.
 */
public class SectorView extends View {

    private static final int mDefaultColor = Color.parseColor("#88A7E3FB");
    private static final int mSelectColor = Color.parseColor("#EE6DCFF6");
    private static final int mLineColor = Color.WHITE;
    private static final int mTextSize = (int) (16 * Utils.getScreenDensity());
    private static final int mStrokeWidth = 1;
    private static final int mStrokeAlpha = 76;

    private int mTotalAngle;
    private int mSectorAngle;
    private int mRadius;
    private int mLeft;
    private int mTop;
    private int mPosition;
    private String mText;

    private boolean isSelect;

    private Paint mPaint;
    private Paint mStrokePaint;
    private RectF mRectF;
    private RectF mStrokeRect;

    public SectorView(Context context, String text, int position, int totalAngle, int childAngle) {
        super(context);
        this.mText = text;
        this.mPosition = position;
        this.mTotalAngle = totalAngle;
        this.mSectorAngle = childAngle;
        this.mRectF = new RectF();
        this.mStrokeRect = new RectF();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setColor(mLineColor);
        mStrokePaint.setAlpha(mStrokeAlpha);
        mStrokePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mRectF.left = left;
        this.mRectF.top = top;
        this.mRectF.right = right;
        this.mRectF.bottom = bottom;
        this.mStrokeRect.left = left + 1;
        this.mStrokeRect.top = top + 1;
        this.mStrokeRect.right = right + 1;
        this.mStrokeRect.bottom = bottom + 1;
        this.mRadius = (right - left) / 2;
        this.mLeft = left;
        this.mTop = top;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawArc(canvas);
    }

    private void drawArc(Canvas canvas) {
        // 画弧形
        if (isSelect) {
            mPaint.setAlpha(152);
            mPaint.setColor(mSelectColor);
        } else {
            mPaint.setAlpha(127);
            mPaint.setColor(mDefaultColor);
        }
        canvas.drawArc(mRectF, mTotalAngle - (mPosition + 1) * mSectorAngle, mSectorAngle, true, mPaint);

        canvas.drawArc(mStrokeRect, mTotalAngle - (mPosition + 1) * mSectorAngle, mSectorAngle, true, mStrokePaint);

        // 画文字
        double textPositionX = getTextPositionX(mRadius * 5 / 7, mPosition, mTotalAngle / mSectorAngle);
        double textPositionY = getTextPositionY(mRadius * 5 / 7, mPosition, mTotalAngle / mSectorAngle);

        if (!TextUtils.isEmpty(mText)) {
            drawText(canvas, mText, textPositionX, textPositionY);
        }
    }

    private void drawText(Canvas canvas, String text, double textPositionX, double textPositionY) {
        Rect rect = new Rect();
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(Color.BLACK);
        mPaint.getTextBounds(text, 0, text.length(), rect);
        int textWidth = rect.width();
        int textHeight = rect.height();
        canvas.drawText(text, (float) textPositionX - textWidth / 2, (float) textPositionY - textHeight / 2, mPaint);
    }

    private double getTextPositionX(float radius, int position, int childCount) {
        return this.mLeft + this.mRadius + radius * Math.cos(2 * Math.PI - position * 2 * Math.PI / childCount - Math.PI / childCount);
    }

    private double getTextPositionY(float radius, int position, int childCount) {
        return this.mTop + this.mRadius + radius * Math.sin(2 * Math.PI - position * 2 * Math.PI / childCount - Math.PI / childCount);
    }

}
