package com.huangyu.customviewlibrary.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huangyu.customviewlibrary.R;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by huangyu on 2017-3-23.
 */
public class BannerView<T> extends FrameLayout {

    /**
     * 自定义属性
     */
    private int mIntervalTime = 4000; // 切换间隔时间（不宜设置太小，否则会造成界面卡死）
    private int mScrollDuration = 250; // 切换速度
    private int mUnSelectedIndicatorColor = Color.WHITE;
    private int mSelectedIndicatorColor = Color.BLUE;
    private int mUnSelectedIndicatorSize = 25;
    private int mSelectedIndicatorSize = 25;

    private ViewPager mViewPager;
    private BannerPagerAdapter<T> mAdapter;
    private BannerScroller mScroller;
    private LinearLayout mIndicatorLayout;
    private Drawable mUnSelectedDrawable;
    private Drawable mSelectedDrawable;

    private Timer timer;
    private TimerTask timerTask;

    public BannerView(Context context) {
        super(context);
        init();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttribute(context, attrs);
        init();
    }

    public void setIntervalTime(int intervalTime) {
        this.mIntervalTime = intervalTime;
    }

    public void setScrollDuration(int scrollDuration) {
        this.mScrollDuration = scrollDuration;
    }

    private void init() {
        initViewPager();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            // 用户可见时开启循环滚动
            startLoop();
        } else {
            // 用户不可见时关闭循环滚动
            stopLoop();
        }
    }

    public void setView(List<T> dataList, BannerViewCreator<T> creator) {
        mAdapter = new BannerPagerAdapter<>(dataList, creator);
        mViewPager.setAdapter(mAdapter);

        initIndicator();
        // 默认滚动至第1项，0为最后一项
        mViewPager.setCurrentItem(1);
    }

    private void initViewPager() {
        mViewPager = new ViewPager(getContext());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 以下无法解决瞬间跳转问题
//                mScroller.setSudden(true);
//                mViewPager.setCurrentItem(getRealPosition(mViewPager.getCurrentItem()) + 1);
//                mScroller.setSudden(false);
                selectIndicator(getRealPosition(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int position = mViewPager.getCurrentItem();
                // 滑动停止时跳转，解决瞬间跳转问题
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    // 第0个滑动，跳转到倒数第二个
                    if (position == 0) {
                        mScroller.setSudden(true);
                        mViewPager.setCurrentItem(mAdapter.getCount() - 2, true);
                        mScroller.setSudden(false);
                    }
                    // 最后一个滑动，跳转到第二项
                    else if (position == mAdapter.getCount() - 1) {
                        mScroller.setSudden(true);
                        mViewPager.setCurrentItem(1, true);
                        mScroller.setSudden(false);
                    }
                }
            }
        });
        // 设置触摸时不允许自动滚动
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stopLoop();
                        break;
                    case MotionEvent.ACTION_UP:
                        startLoop();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        replaceViewPagerScroll();
        this.addView(mViewPager);
    }

    private void initIndicator() {
        if (mAdapter != null) {
            initIndicatorDrawable();
            initIndicaterLayout();
            initIndicatorLayoutContent();
        }
    }

    private void initIndicaterLayout() {
        mIndicatorLayout = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        layoutParams.setMargins(0, 0, 0, 16);
        mIndicatorLayout.setGravity(Gravity.CENTER);
        mIndicatorLayout.setLayoutParams(layoutParams);
        this.addView(mIndicatorLayout);
    }

    private void initIndicatorLayoutContent() {
        int realCount = getRealCount();
        for (int i = 0; i < realCount; i++) {
            ImageView indicator = new ImageView(getContext());
            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            indicator.setPadding(10, 10, 10, 10);
            indicator.setImageDrawable(mUnSelectedDrawable);
            mIndicatorLayout.addView(indicator);
        }
    }

    private void initIndicatorDrawable() {
        LayerDrawable unSelectedLayerDrawable;
        LayerDrawable selectedLayerDrawable;
        GradientDrawable unSelectedGradientDrawable;
        unSelectedGradientDrawable = new GradientDrawable();

        GradientDrawable selectedGradientDrawable;
        selectedGradientDrawable = new GradientDrawable();
        unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
        selectedGradientDrawable.setShape(GradientDrawable.OVAL);
        unSelectedGradientDrawable.setColor(mUnSelectedIndicatorColor);
        unSelectedGradientDrawable.setSize(mUnSelectedIndicatorSize, mUnSelectedIndicatorSize);
        unSelectedLayerDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
        mUnSelectedDrawable = unSelectedLayerDrawable;

        selectedGradientDrawable.setColor(mSelectedIndicatorColor);
        selectedGradientDrawable.setSize(mSelectedIndicatorSize, mSelectedIndicatorSize);
        selectedLayerDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
        mSelectedDrawable = selectedLayerDrawable;
    }

    private void selectIndicator(int selectPosition) {
        int realCount = getRealCount();
        for (int i = 0; i < realCount; i++) {
            ((ImageView) mIndicatorLayout.getChildAt(i)).setImageDrawable(i == selectPosition ? mSelectedDrawable : mUnSelectedDrawable);
        }
    }

    private void replaceViewPagerScroll() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            mScroller = new BannerScroller(getContext(), new AccelerateInterpolator());
            mScroller.setScrollDuration(mScrollDuration);
            field.set(mViewPager, mScroller);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private void startLoop() {
        stopLoop();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        // 自动滚动到后一项
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    }
                });
            }
        };
        timer.schedule(timerTask, mIntervalTime, mIntervalTime);
    }

    private void stopLoop() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    private int getRealPosition(int position) {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return -1;
        }

        if (position == 0) {
            return getRealCount() - 1;
        } else if (position == getRealCount() + 1) {
            return 0;
        } else {
            return position - 1;
        }
    }

    public int getRealCount() {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return 0;
        }
        return mAdapter.getCount() - 2;
    }

    private void getAttribute(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        try {
            mIntervalTime = a.getInteger(R.styleable.BannerView_intervalTime, 4000);
            mScrollDuration = a.getInteger(R.styleable.BannerView_scrollDuration, 250);
        } finally {
            a.recycle();
        }
    }

}
