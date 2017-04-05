package com.huangyu.customviewlibrary.sector_menu_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by huangyu on 2017/4/5.
 */
public class SectorView extends View {

	private static final int defaultColor = Color.parseColor("#88A7E3FB");
	private static final int selectColor = Color.parseColor("#EE6DCFF6");
	private static final int lineColor = Color.WHITE;
	private static final int textSize = 32;

	private int totalAngle;
	private int sectorAngle;
	private int radius;
	private int left;
	private int top;

	private String text;
	private int position;
	private boolean isSelect;

	private Paint paint;
	private Paint strokePaint;
	private RectF rectF;
	private RectF strokeRect;

	public SectorView(Context context, String text, int position, int totalAngle, int childAngle) {
		super(context);
		this.text = text;
		this.position = position;
		this.totalAngle = totalAngle;
		this.sectorAngle = childAngle;
		this.rectF = new RectF();
		this.strokeRect = new RectF();

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		strokePaint.setStrokeWidth(1);
		strokePaint.setColor(lineColor);
		strokePaint.setAlpha(76);
		strokePaint.setStyle(Paint.Style.STROKE);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		this.rectF.left = left;
		this.rectF.top = top;
		this.rectF.right = right;
		this.rectF.bottom = bottom;
		this.strokeRect.left = left + 1;
		this.strokeRect.top = top + 1;
		this.strokeRect.right = right + 1;
		this.strokeRect.bottom = bottom + 1;
		this.radius = (right - left) / 2;
		this.left = left;
		this.top = top;
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
		paint.setStyle(Paint.Style.FILL);
		// 画弧形
		if (isSelect) {
			paint.setAlpha(152);
			paint.setColor(selectColor);
		} else {
			paint.setAlpha(127);
			paint.setColor(defaultColor);
		}
		canvas.drawArc(rectF, totalAngle - (position + 1) * sectorAngle, sectorAngle, true, paint);

		canvas.drawArc(strokeRect, totalAngle - (position + 1) * sectorAngle, sectorAngle, true, strokePaint);

		// 画文字
		double textPositionX = getTextPositionX(radius * 5 / 7, position, totalAngle / sectorAngle);
		double textPositionY = getTextPositionY(radius * 5 / 7, position, totalAngle / sectorAngle);

		if (!TextUtils.isEmpty(text)) {
			// 默认为4个文字，每行2个字
			if (text.length() == 4) {
				drawText(canvas, text.substring(0, 2), textPositionX, textPositionY);
				drawText(canvas, text.substring(2, 4), textPositionX, textPositionY + textSize);
			}
			// 其他情况下只显示一行
			else {
				drawText(canvas, text, textPositionX, textPositionY);
			}
		}
	}

	private void drawText(Canvas canvas, String text, double textPositionX, double textPositionY) {
		Rect rect = new Rect();
		paint.setTextSize(textSize);
		paint.setColor(Color.BLACK);
		paint.getTextBounds(text, 0, text.length(), rect);
		int textWidth = rect.width();
		int textHeight = rect.height();
		canvas.drawText(text, (float) textPositionX - textWidth / 2, (float) textPositionY - textHeight / 2, paint);
	}

	private double getTextPositionX(float radius, int position, int childCount) {
		return this.left + this.radius + radius * Math.cos(2 * Math.PI - position * 2 * Math.PI / childCount - Math.PI / childCount);
	}

	private double getTextPositionY(float radius, int position, int n) {
		return this.top + this.radius + radius * Math.sin(2 * Math.PI - position * 2 * Math.PI / n - Math.PI / n);
	}

}
