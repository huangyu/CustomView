package com.huangyu.customviewlibrary.canvas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyu on 2017-4-6.
 */
public class DrawFreeCurve {

    private int color;
    private float paintSize;
    private Boolean isEraser;
    protected List<PointF> points;

    protected DrawFreeCurve() {
        color = Color.BLACK;
        paintSize = 10;
        points = new ArrayList<>();
        isEraser = false;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setIsEraser(Boolean isEraser) {
        this.isEraser = isEraser;
    }

    public void setPaint(Paint paint) {
        this.paintSize = paint.getStrokeWidth();
        this.color = paint.getColor();
    }

    public void drawObject(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(paintSize);
        if (isEraser) {
            paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        }
        Path path = new Path();

        float x = points.get(0).x;
        float y = points.get(0).y;
        path.moveTo(x, y);
        for (int i = 1; i < points.size(); i++) {
            path.quadTo(x, y, points.get(i).x, points.get(i).y);
            x = points.get(i).x;
            y = points.get(i).y;
        }
        canvas.drawPath(path, paint);
    }

    public void load(DataInputStream in) {
        try {
            color = in.readInt();
            paintSize = in.readFloat();
            isEraser = in.readBoolean();
            int pointCount = in.readInt();
            float fx, fy;
            for (int i = 0; i < pointCount; i++) {
                fx = in.readFloat();
                fy = in.readFloat();
                PointF newPt = new PointF(fx, fy);
                points.add(newPt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(DataOutputStream out) {
        int i;
        try {
            out.writeInt(color);
            out.writeFloat(paintSize);
            out.writeBoolean(isEraser);
            out.writeInt(points.size());
            for (i = 0; i < points.size(); i++) {
                out.writeFloat(points.get(i).x);
                out.writeFloat(points.get(i).y);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
