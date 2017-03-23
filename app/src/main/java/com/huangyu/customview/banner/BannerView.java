package com.huangyu.customview.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by huangyu on 2017-3-23.
 */
public class BannerView<T> extends FrameLayout {

    private Context mContext;

    private ViewPager mViewPager;
    private BannerPagerAdapter<T> mAdapter;
    private BannerScroller mScroller;

    private Timer timer;
    private TimerTask timerTask;

    public BannerView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        mViewPager = new ViewPager(mContext);
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

    public void setView(List<T> dataList) {
        mAdapter = new BannerPagerAdapter<>(mContext, dataList);
        mViewPager.setAdapter(mAdapter);

        // 默认滚动至第1项，0为最后一项
        mViewPager.setCurrentItem(1);
    }

    private void replaceViewPagerScroll() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            mScroller = new BannerScroller(mContext, new AccelerateInterpolator());
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
        timer.schedule(timerTask, 5000, 5000);
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
            return getCount() - 1;
        } else if (position == getCount() + 1) {
            return 0;
        } else {
            return position - 1;
        }
    }

    public int getCount() {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return 0;
        }
        return mAdapter.getCount() - 2;
    }

}
