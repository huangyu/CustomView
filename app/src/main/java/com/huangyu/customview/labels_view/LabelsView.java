package com.huangyu.customview.labels_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by huangyu on 2017-3-24.
 */
public class LabelsView extends ViewGroup {

    private Context mContext;

    private int mChildMargin = 20; // 行间距
    private int mLineMargin = 20; // 单行间子元素间距
    private int mTextSize = 20; // 文字大小
    private int mPadding = 10;

    public LabelsView(Context context) {
        super(context);
        this.mContext = context;
    }

    public LabelsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void setView(List<String> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        removeAllViews();
        for (String data : dataList) {
            TextView textView = new TextView(mContext);
            textView.setText(data);
            textView.setTextSize(mTextSize);
            this.addView(textView);
        }
        setPadding(mPadding, mPadding, mPadding, mPadding);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int parentWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight(); // 父元素宽度
        int childCount = getChildCount(); // 子元素数量
        int lineWidth = 0; // 单行宽度
        int lineMaxWidth = 0; // 最长行宽
        int parentTotalHeight = 0; // 全部行总高度
        int childMaxHeight = 0; // 子元素最高高度
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            // 子元素总宽大于父元素宽度，另起一行
            if (lineWidth > parentWidth) {
                lineMaxWidth = Math.max(lineMaxWidth, lineWidth);
                parentTotalHeight = parentTotalHeight + mLineMargin + childMaxHeight;
                lineWidth = 0;
                childMaxHeight = 0;
            }

            childMaxHeight = Math.max(childMaxHeight, childView.getMeasuredHeight());
            lineWidth = lineWidth + childView.getMeasuredWidth() + mChildMargin;
        }
        setMeasuredDimension(setMeasureWidth(widthMeasureSpec, lineMaxWidth), setMeasureHeight(heightMeasureSpec, parentTotalHeight));
    }

    private int setMeasureWidth(int measureSpec, int lineMaxWidth) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        result = lineMaxWidth + getPaddingLeft() + getPaddingRight();
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        return result;
    }

    private int setMeasureHeight(int measureSpec, int parentTotalHeight) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        result = parentTotalHeight + getPaddingTop() + getPaddingBottom();
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int x = getPaddingLeft();
        int y = getPaddingTop();

        int lineWidth = right - left; // 单行宽度
        int childMaxHeight = 0; // 子元素最高高度

        int childCount = getChildCount(); // 子元素数量
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            //当前行显示不下item时换行。
            if (lineWidth < x + childView.getMeasuredWidth()) {
                x = getPaddingLeft();
                y = y + mLineMargin + childMaxHeight;
                childMaxHeight = 0;
            }
            childView.layout(x, y, x + childView.getMeasuredWidth(), y + childView.getMeasuredHeight());
            x = x + mChildMargin + childView.getMeasuredWidth();
            childMaxHeight = Math.max(childMaxHeight, childView.getMeasuredHeight());
        }

    }

}
