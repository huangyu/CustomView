package com.huangyu.customviewlibrary.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.huangyu.customviewlibrary.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyu on 2017-4-6.
 */
public class DrawView extends View {

    private float HALF_STROKE_WIDTH = 5.0f;

    private int VIEW_W;
    private int VIEW_H;

    private List<DrawFreeCurve> mElementList;
    private List<DrawFreeCurve> mDelElements;

    private DrawFreeCurve mDrawFreeCurve;

    private Path mPath;
    private Paint mPaint;
    private Paint mCirclePaint;

    private Canvas mPaintCanvas;
    private Canvas mEraserCanvas;

    private Bitmap mPaintBitmap;
    private Bitmap mEraserBitmap;

    private Boolean isEraser = false;
    private Boolean isTouchUp = false;

    private int mPaintSize = 5;
    private int mEraserSize = 30;
    private int paintColor;

    private float mLastTouchX = 0;
    private float mLastTouchY = 0;
    private RectF mDirtyRect;

    private ViewGroup mParentViewGroup;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mElementList = new ArrayList<>();
        mDelElements = new ArrayList<>();
        mPath = new Path();
        mPaint = new Paint();
        mCirclePaint = new Paint();
        mDirtyRect = new RectF();

        setViewWH(attrs);
        setPaintBitmap();
        setEraserBitmap();
        setPaint();
        setPaintColor(Color.BLACK);
    }

    public void setParentView(ViewGroup viewGroup) {
        this.mParentViewGroup = viewGroup;
    }

    /**
     * 设置上层画布尺寸
     */
    private void setViewWH(AttributeSet attrs) {
        String name, width = null, height = null;
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            name = attrs.getAttributeName(i);
            if (name.equals("layout_width")) {
                width = attrs.getAttributeValue(i);
            }
            if (name.equals("layout_height")) {
                height = attrs.getAttributeValue(i);
            }
        }

        int screenWidth = 0;
        int screenHeight = 0;
        try {
            screenWidth = (int) (Utils.getScreenDensity() * (Integer.parseInt(width.split("\\.")[0])));
            screenHeight = (int) (Utils.getScreenDensity() * (Integer.parseInt(height.split("\\.")[0])));
        } catch (Exception e) {
//            e.printStackTrace();
        }

        VIEW_W = screenWidth;
        VIEW_H = screenHeight;
    }

    private void setPaintBitmap() {
        mPaintBitmap = Bitmap.createBitmap(VIEW_W, VIEW_H, Config.ARGB_4444);
        mPaintCanvas = new Canvas();
        mPaintCanvas.setBitmap(mPaintBitmap);
    }

    private void setEraserBitmap() {
        mEraserBitmap = Bitmap.createBitmap(VIEW_W, VIEW_H, Config.ALPHA_8);
        mEraserCanvas = new Canvas();
        mEraserCanvas.setBitmap(mEraserBitmap);
    }

    public void clearBitmap() {
        if (mPaintBitmap != null) {
            mPaintBitmap.recycle();
        }
        if (mEraserBitmap != null) {
            mEraserBitmap.recycle();
        }
    }

    /**
     * 画笔
     */
    public void setPaint() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mPaintSize);
        isEraser = false;
    }

    /**
     * 重设画笔颜色
     */
    public void setPaintColor(int color) {
        paintColor = color;
        mPaint.setColor(paintColor);
    }

    /**
     * 重设画笔粗细/颜色
     */
    public void setPaintSize(int size) {
        HALF_STROKE_WIDTH = size / 2 + 2;
        mPaint.setStrokeWidth(size);
        if (isEraser) {
            mEraserSize = size;
        }
    }

    /**
     * 橡皮擦
     */
    public void setEraser() {
        mPath = new Path();
        mPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        mPaint.setStrokeWidth(mEraserSize);
        isEraser = true;
    }

    /**
     * 清除整个画布
     */
    public void clear() {
        mElementList.clear();
        mDelElements.clear();
        clearCoverCanvas();
        clearEraserCanvas();
        isEraser = false;
        invalidate();
    }

    private void clearCoverCanvas() {
        mPath = new Path();
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        mPaintCanvas.drawPaint(paint);
    }

    private void clearEraserCanvas() {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        mEraserCanvas.drawPaint(paint);
    }

    /**
     * 上一步
     */
    public void goBack() {
        if (mElementList != null && mElementList.size() > 0) {
            mDelElements.add(mElementList.get(mElementList.size() - 1));
            mElementList.remove(mElementList.size() - 1);
        }
        clearCoverCanvas();
        setDrawBitmap();
        invalidate();
    }

    /**
     * 下一步
     */
    public void goForward() {
        if (mDelElements != null && mDelElements.size() > 0) {
            mElementList.add(mDelElements.get(mDelElements.size() - 1));
            mDelElements.remove(mDelElements.size() - 1);
        }
        clearCoverCanvas();
        setDrawBitmap();
        invalidate();
    }

    private void setDrawBitmap() {
        if (mElementList != null && mElementList.size() > 0) {
            for (int i = 0; i < mElementList.size(); i++) {
                mElementList.get(i).drawObject(mPaintCanvas);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isEraser) {
            mPaintCanvas.drawPath(mPath, mPaint);
            canvas.drawBitmap(mPaintBitmap, 0, 0, null);
            if (isTouchUp) {
                clearEraserCanvas();
            } else {
                drawEraserCircle(mLastTouchX, mLastTouchY);
                canvas.drawBitmap(mEraserBitmap, 0, 0, null);
            }
        } else {
            canvas.drawBitmap(mPaintBitmap, 0, 0, null);
            canvas.drawPath(mPath, mPaint);
        }
        super.onDraw(canvas);
    }

    private void drawEraserCircle(float x, float y) {
        clearEraserCanvas();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setColor(Color.BLACK);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(2);
        mEraserCanvas.drawCircle(x, y, mPaint.getStrokeWidth() / 2, mCirclePaint);
    }

    private void touchStart(float x, float y) {
        mDrawFreeCurve = new DrawFreeCurve();
        mDrawFreeCurve.setPaint(mPaint);
        mDrawFreeCurve.setIsEraser(isEraser);
        mDrawFreeCurve.setColor(paintColor);
        PointF p = new PointF(x, y);
        mDrawFreeCurve.points.add(p);
        mElementList.add(mDrawFreeCurve);
        mLastTouchX = x;
        mLastTouchY = y;
        isTouchUp = false;
        resetDirtyRect(x, y);

        mPath = new Path();
        mPath.moveTo(x, y);
        invalidate((int) (mDirtyRect.left - HALF_STROKE_WIDTH), (int) (mDirtyRect.top - HALF_STROKE_WIDTH),
                (int) (mDirtyRect.right + HALF_STROKE_WIDTH), (int) (mDirtyRect.bottom + HALF_STROKE_WIDTH));
    }

    private void touchMove(float x, float y, MotionEvent event) {
        int historySize = event.getHistorySize();
        for (int i = 0; i < historySize; i++) {
            PointF point = new PointF();
            float historicalX = event.getHistoricalX(i);
            float historicalY = event.getHistoricalY(i);
            point.set(historicalX, historicalY);
            mDrawFreeCurve.points.add(point);

            mPath.quadTo(mLastTouchX, mLastTouchY, historicalX, historicalY);

            resetDirtyRect(historicalX, historicalY);
            mLastTouchX = historicalX;
            mLastTouchY = historicalY;
            invalidate((int) (mDirtyRect.left - HALF_STROKE_WIDTH), (int) (mDirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (mDirtyRect.right + HALF_STROKE_WIDTH), (int) (mDirtyRect.bottom + HALF_STROKE_WIDTH));
        }
        mDrawFreeCurve.points.add(new PointF(x, y));
        resetDirtyRect(x, y);

        mPath.quadTo(mLastTouchX, mLastTouchY, x, y);

        invalidate((int) (mDirtyRect.left - HALF_STROKE_WIDTH), (int) (mDirtyRect.top - HALF_STROKE_WIDTH),
                (int) (mDirtyRect.right + HALF_STROKE_WIDTH), (int) (mDirtyRect.bottom + HALF_STROKE_WIDTH));
        mLastTouchX = x;
        mLastTouchY = y;
    }

    private void touchUp(float x, float y, MotionEvent event) {
        int historySize = event.getHistorySize();
        for (int i = 0; i < historySize; i++) {
            PointF point = new PointF();
            float historicalX = event.getHistoricalX(i);
            float historicalY = event.getHistoricalY(i);
            point.set(historicalX, historicalY);
            mPath.quadTo(mLastTouchX, mLastTouchY, historicalX, historicalY);
            mDrawFreeCurve.points.add(point);
            mLastTouchX = historicalX;
            mLastTouchY = historicalY;
        }
        mDrawFreeCurve.points.add(new PointF(x, y));
        resetDirtyRect(x, y);
        mPath.quadTo(mLastTouchX, mLastTouchY, x, y);
        if (isEraser) {
            isTouchUp = true;
            setDrawBitmap();
        }
        setDrawBitmap();
        invalidate((int) (mDirtyRect.left - HALF_STROKE_WIDTH), (int) (mDirtyRect.top - HALF_STROKE_WIDTH),
                (int) (mDirtyRect.right + HALF_STROKE_WIDTH), (int) (mDirtyRect.bottom + HALF_STROKE_WIDTH));
        mLastTouchX = x;
        mLastTouchY = y;
    }

    private void resetDirtyRect(float eventX, float eventY) {
        mDirtyRect.left = Math.min(mLastTouchX, eventX);
        mDirtyRect.right = Math.max(mLastTouchX, eventX);
        mDirtyRect.top = Math.min(mLastTouchY, eventY);
        mDirtyRect.bottom = Math.max(mLastTouchY, eventY);
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
                    touchStart(x, y);
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
        }
        return true;
    }

    /**
     * 保存到文件中
     *
     * @param path
     */
    public void save(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
//            String headString = "DrawFile";
//            out.writeUTF(headString);
            out.writeInt(mElementList.size());
            for (int i = 0; i < mElementList.size(); i++) {
                mElementList.get(i).save(out);
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载文件
     *
     * @param path
     */
    public void load(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                DataInputStream in = new DataInputStream(new FileInputStream(file));
//                String headString = in.readUTF();
                int size = in.readInt();
                if (mElementList != null && mElementList.size() > 0) {
                    for (int i = 0; i < mElementList.size(); i++) {
                        mElementList.remove(i);
                    }
                }
                for (int i = 0; i < size; i++) {
                    mDrawFreeCurve = new DrawFreeCurve();
                    mDrawFreeCurve.load(in);
                    mElementList.add(mDrawFreeCurve);
                }
                in.close();
                setDrawBitmap();
                invalidate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
