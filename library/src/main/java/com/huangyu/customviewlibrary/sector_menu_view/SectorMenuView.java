package com.huangyu.customviewlibrary.sector_menu_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.huangyu.customviewlibrary.R;
import com.huangyu.customviewlibrary.Utils;

/**
 * Created by huangyu on 2017/4/5.
 */
public class SectorMenuView extends FrameLayout {

    private static final int mTotalAngle = 360; // 总角度
    private static final int mIntervalTime = 10; // 切换时间

    private int mDiameter; // 直径
    private int mSectorAngle; // 每个扇形角度
    private int mBackgroundRes; // 中心图片

    private boolean isOpen = false;
    private boolean canTouch = true;
    private int selectPosition = -1;

    private String[] textArray;
    private SectorView[] sectorViewArray;
    private CenterView centerView;
    private SectorButtonClickListener sectorButtonClickListener;

    public interface SectorButtonClickListener {
        void onSectorButtonClick(int position, String text);
    }

    public SectorMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttribute(context, attrs);
    }

    public void setOnSectorButtonClick(SectorButtonClickListener sectorButtonClickListener) {
        this.sectorButtonClickListener = sectorButtonClickListener;
    }

    public void init(final Context context, String[] textArray) {
        centerView = new CenterView(context);
        centerView.setBitmapResourse(mBackgroundRes);
        addView(centerView);

        this.textArray = textArray;
        sectorViewArray = new SectorView[textArray.length];
        SectorView sectorView;
        for (int i = 0; i < sectorViewArray.length; i++) {
            sectorView = new SectorView(context, textArray[i], i, mTotalAngle, mSectorAngle);
            sectorView.setVisibility(View.INVISIBLE);
            sectorViewArray[i] = sectorView;
            addView(sectorView);
        }

        bringChildToFront(centerView);
    }

    public void openOrCloseMenu(boolean isShowDialog) {
        Animation animation = getCenterAnim(isShowDialog);
        if (animation != null) {
            centerView.startAnimation(animation);
        }
    }

    private Animation getCenterAnim(boolean isShowDialog) {
        Animation animation;
        if (isOpen) {
            if (isShowDialog) {
                animation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
            } else {
                animation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
            }
        } else {
            animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        }
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (!isOpen) {
                    centerView.setBitmapResourse(R.drawable.btn_tool_hover);
                    centerView.invalidate();
                }
                startSectorAnim();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isOpen) {
                    centerView.setBitmapResourse(R.drawable.btn_tool_nor);
                    centerView.invalidate();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(mIntervalTime * sectorViewArray.length);
        return animation;
    }

    private void startSectorAnim() {
        int length = sectorViewArray.length;
        if (length > 0) {
            if (isOpen) {
                sectorViewArray[length - 1].startAnimation(getSectorAnim(length - 1));
            } else {
                sectorViewArray[0].startAnimation(getSectorAnim(0));
            }
        }
    }

    private Animation getSectorAnim(final int position) {
        Animation animation;
        if (isOpen) {
            animation = new AlphaAnimation(1, 0);
        } else {
            animation = new AlphaAnimation(0, 1);
        }
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int newPosition;
                if (isOpen) {
                    sectorViewArray[position].setVisibility(View.GONE);

                    newPosition = position - 1;
                    if (newPosition >= 0) {
                        sectorViewArray[newPosition].startAnimation(getSectorAnim(newPosition));
                    } else {
                        isOpen = !isOpen;
                        canTouch = true;
                    }
                } else {
                    sectorViewArray[position].setVisibility(View.VISIBLE);

                    newPosition = position + 1;
                    if (newPosition < sectorViewArray.length) {
                        sectorViewArray[newPosition].startAnimation(getSectorAnim(newPosition));
                    } else {
                        isOpen = !isOpen;
                        canTouch = true;
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(mIntervalTime);
        return animation;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!canTouch) {
                    return true;
                }
                canTouch = false;
                selectPosition = touchJudge(event.getX(), event.getY());
                switch (selectPosition) {
                    // 选中中心
                    case -1:
                        openOrCloseMenu(false);
                        selectPosition();
                        break;
                    // 没有选中
                    case -2:
                        canTouch = true;
                        break;
                    // 选中扇形
                    default:
//						canTouch = true;
                        selectPosition();
                        sectorButtonClickListener.onSectorButtonClick(selectPosition, textArray[selectPosition]);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 选中的扇形改变颜色
     */
    private void selectPosition() {
        for (int i = 0; i < sectorViewArray.length; i++) {
            if (i == selectPosition) {
                sectorViewArray[i].setIsSelect(true);
            } else {
                sectorViewArray[i].setIsSelect(false);
            }
        }
    }

    /**
     * 判断选中状态
     *
     * @param touchX 点击的x坐标
     * @param touchY 点击的y坐标
     * @return
     */
    private int touchJudge(double touchX, double touchY) {
        // 实际按钮并没有填充全部布局，乘以一个系数避免点击冲突
        double radius = (centerView.mBitmapWidth + centerView.mBitmapHeight) / 2 / 2 * 5 / 6;

        double x = touchX - mDiameter / 2;
        double y = touchY - mDiameter / 2;
        double distance = Math.sqrt(x * x + y * y);

        // 判断是否点击了中心
        if (distance <= radius) {
            return -1; // 选中中心
        } else {
            if (!isOpen) {
                return -2; // 没有选中
            }
            x = touchX - mDiameter / 2;
            y = touchY - mDiameter / 2;
            distance = Math.sqrt(x * x + y * y);
            // 判断是否超过了扇形半径
            if (distance > mDiameter / 2) {
                return -2; // 没有选中
            }
            // 判断属于哪个扇形
            else {
                double angle = mTotalAngle - Math.round(Math.atan2(y, x) / Math.PI * mTotalAngle / 2);
                angle = angle > mTotalAngle ? angle - mTotalAngle : angle;
                for (int i = 0; i < sectorViewArray.length; i++) {
                    if (isSelect(angle, i)) {
                        return i; // 选中第i个扇形
                    }
                }
            }
            return -2; // 没有选中
        }
    }

    private boolean isSelect(double angle, int position) {
        return angle > (position * mSectorAngle) && angle < ((position + 1) * mSectorAngle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(mDiameter, widthMeasureSpec);
        int height = measureDimension(mDiameter, heightMeasureSpec);
        measureChildren(width, height);
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);

            if (childView instanceof CenterView) {
                layoutCenterView((CenterView) childView);
            } else {
                childView.layout(0, 0, mDiameter, mDiameter);
            }
        }
    }

    private void layoutCenterView(CenterView centerView) {
        int childWidth = centerView.mBitmapWidth;
        int childHeight = centerView.mBitmapHeight;
        centerView.layout((mDiameter - childWidth) / 2, (mDiameter - childHeight) / 2, (mDiameter - childWidth) / 2 + childWidth, (mDiameter - childHeight) / 2
                + childHeight);
    }

    private void getAttribute(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SectorMenuView);
        try {
            mBackgroundRes = a.getResourceId(R.styleable.SectorMenuView_backgroundRes, R.drawable.btn_tool_nor);
            mDiameter = a.getResourceId(R.styleable.SectorMenuView_diameter, (int) (240 * Utils.getScreenDensity()));
            mSectorAngle = a.getResourceId(R.styleable.SectorMenuView_sectorAngle, 40);
        } finally {
            a.recycle();
        }
    }

}
