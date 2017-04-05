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

    private int mBackgroundRes; // 中心图片
    private int mDiameter; // 直径
    private int mSectorAngle; // 每个扇形角度

    private boolean isOpen = false;
    private boolean isCanTouch = true;
    private int mSelectPosition = -1;

    private String[] mTextArray;
    private SectorView[] mSectorViewArray;
    private CenterView mCenterView;
    private SectorButtonClickListener mSectorButtonClickListener;

    public interface SectorButtonClickListener {
        void onSectorButtonClick(int position, String text);
    }

    public SectorMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttribute(context, attrs);
    }

    public void setOnSectorButtonClick(SectorButtonClickListener sectorButtonClickListener) {
        this.mSectorButtonClickListener = sectorButtonClickListener;
    }

    public void init(final Context context, String[] textArray) {
        mCenterView = new CenterView(context);
        mCenterView.setBitmapResourse(mBackgroundRes);
        addView(mCenterView);

        this.mTextArray = textArray;
        mSectorViewArray = new SectorView[textArray.length];
        SectorView sectorView;
        for (int i = 0; i < mSectorViewArray.length; i++) {
            sectorView = new SectorView(context, textArray[i], i, mTotalAngle, mSectorAngle);
            sectorView.setVisibility(View.INVISIBLE);
            mSectorViewArray[i] = sectorView;
            addView(sectorView);
        }

        bringChildToFront(mCenterView);
    }

    public void openOrCloseMenu(boolean isShowDialog) {
        Animation animation = getCenterAnim(isShowDialog);
        if (animation != null) {
            mCenterView.startAnimation(animation);
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
                    mCenterView.setBitmapResourse(R.drawable.btn_tool_hover);
                    mCenterView.invalidate();
                }
                startSectorAnim();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isOpen) {
                    mCenterView.setBitmapResourse(R.drawable.btn_tool_nor);
                    mCenterView.invalidate();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(mIntervalTime * mSectorViewArray.length);
        return animation;
    }

    private void startSectorAnim() {
        int length = mSectorViewArray.length;
        if (length > 0) {
            if (isOpen) {
                mSectorViewArray[length - 1].startAnimation(getSectorAnim(length - 1));
            } else {
                mSectorViewArray[0].startAnimation(getSectorAnim(0));
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
                    mSectorViewArray[position].setVisibility(View.GONE);

                    newPosition = position - 1;
                    if (newPosition >= 0) {
                        mSectorViewArray[newPosition].startAnimation(getSectorAnim(newPosition));
                    } else {
                        isOpen = !isOpen;
                        isCanTouch = true;
                    }
                } else {
                    mSectorViewArray[position].setVisibility(View.VISIBLE);

                    newPosition = position + 1;
                    if (newPosition < mSectorViewArray.length) {
                        mSectorViewArray[newPosition].startAnimation(getSectorAnim(newPosition));
                    } else {
                        isOpen = !isOpen;
                        isCanTouch = true;
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
                if (!isCanTouch) {
                    return true;
                }
                isCanTouch = false;
                mSelectPosition = touchJudge(event.getX(), event.getY());
                switch (mSelectPosition) {
                    // 选中中心
                    case -1:
                        openOrCloseMenu(false);
                        selectPosition();
                        break;
                    // 没有选中
                    case -2:
                        isCanTouch = true;
                        break;
                    // 选中扇形
                    default:
//						isCanTouch = true;
                        selectPosition();
                        mSectorButtonClickListener.onSectorButtonClick(mSelectPosition, mTextArray[mSelectPosition]);
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
        for (int i = 0; i < mSectorViewArray.length; i++) {
            if (i == mSelectPosition) {
                mSectorViewArray[i].setIsSelect(true);
            } else {
                mSectorViewArray[i].setIsSelect(false);
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
        double radius = (mCenterView.mBitmapWidth + mCenterView.mBitmapHeight) / 2 / 2 * 5 / 6;

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
                for (int i = 0; i < mSectorViewArray.length; i++) {
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
