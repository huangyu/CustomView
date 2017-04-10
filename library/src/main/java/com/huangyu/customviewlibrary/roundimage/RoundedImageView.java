package com.huangyu.customviewlibrary.roundimage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by huangyu on 2017-4-5.
 */
public class RoundedImageView extends ImageView {

    /**
     * 图形宽度
     */
    private int mWidth;
    /**
     * 是否为圆形图形展示
     */
    private boolean isCircle = true;
    /**
     * 圆形图半径
     */
    private int mRadius;

    /**
     * 矩形图形圆角半径
     */
    private float mRectRadius = 10;
    private final RectF mRoundRect = new RectF();
    private final Paint mMaskPaint = new Paint();
    private final Paint mZonePaint = new Paint();

    public RoundedImageView(Context context) {
        this(context, null);
        init();
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMaskPaint.setAntiAlias(true);
        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mZonePaint.setAntiAlias(true);
        mZonePaint.setColor(Color.WHITE);
        float density = getResources().getDisplayMetrics().density;
        mRectRadius = mRectRadius * density;
    }

    /**
     * 自定义圆角半径
     *
     * @param radius
     */
    public void setRectRadius(float radius) {
        mRectRadius = radius;
        invalidate();
    }

    /**
     * 自定义是否为圆形图片，默认圆形图
     *
     * @param circle
     */
    public void setCircle(boolean circle) {
        isCircle = circle;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = mWidth / 2;
        setMeasuredDimension(mWidth, mWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();
        mRoundRect.set(0, 0, w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(mRoundRect, mZonePaint, Canvas.ALL_SAVE_FLAG);
        if (isCircle) {
            canvas.drawCircle(mWidth / 2, mWidth / 2, mRadius, mZonePaint);
        } else {
            canvas.drawRoundRect(mRoundRect, mRectRadius, mRectRadius, mZonePaint);
        }
        canvas.saveLayer(mRoundRect, mMaskPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }

}
