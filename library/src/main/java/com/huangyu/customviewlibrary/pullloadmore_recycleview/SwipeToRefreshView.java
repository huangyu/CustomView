package com.huangyu.customviewlibrary.pullloadmore_recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.huangyu.customviewlibrary.R;

import static android.R.attr.max;

/**
 * Created by huangyu on 2017/3/25.
 */

public class SwipeToRefreshView extends LinearLayout {

    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private RefreshAndLoadListener mRefreshAndLoadListener;

    private boolean isCanRefresh = true;
    private boolean isCanLoad = true;
    private boolean isLoading;

    public SwipeToRefreshView(Context context) {
        super(context);
        init(context);
    }

    public SwipeToRefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeToRefreshView setIsCanRefresh(boolean isCanRefresh) {
        this.isCanRefresh = isCanRefresh;
        return this;
    }

    public SwipeToRefreshView setIsCanoad(boolean isCanLoad) {
        this.isCanLoad = isCanLoad;
        return this;
    }

    public SwipeToRefreshView setRefreshAndLoadListener(RefreshAndLoadListener refreshAndLoadListener) {
        this.mRefreshAndLoadListener = refreshAndLoadListener;
        return this;
    }

    public SwipeToRefreshView setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.mRecyclerView.setLayoutManager(layoutManager);
        return this;
    }

    private void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_pull_load_more, null);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isCanRefresh && isRefreshing()) {
                    setIsRefreshing(true);
                    if (mRefreshAndLoadListener != null) {
                        mRefreshAndLoadListener.onRefresh();
                    }
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = 0;
                int firstItem = 0;

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();

                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = ((GridLayoutManager) layoutManager);
                    firstItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                    lastItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastItem == -1) {
                        lastItem = gridLayoutManager.findLastVisibleItemPosition();
                    }
                } else if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) layoutManager);
                    firstItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastItem == -1) {
                        lastItem = linearLayoutManager.findLastVisibleItemPosition();
                    }
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = ((StaggeredGridLayoutManager) layoutManager);
                    int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
                    firstItem = staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions)[0];
                    lastItem = lastPositions[0];
                    for (int value : lastPositions) {
                        if (value > lastItem) {
                            lastItem = value;
                        }
                    }
                }

                // refresh
                if (firstItem == 0 || firstItem == RecyclerView.NO_POSITION) {
                    if (isCanRefresh) {
                        mSwipeRefreshLayout.setEnabled(true);
                    }
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }

                // load
                if (isCanLoad
                        && !mSwipeRefreshLayout.isRefreshing()
                        && (lastItem == totalItemCount - 1)
                        && !isLoading
                        && (dx > 0 || dy > 0)) {
                    isLoading = true;
                    if (mRefreshAndLoadListener != null) {
                        mRefreshAndLoadListener.onLoad();
                    }
                }

            }
        });
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public boolean isRefreshing() {
        return mSwipeRefreshLayout.isRefreshing();
    }

    public void setIsRefreshing(boolean isRefreshing) {
        mSwipeRefreshLayout.setRefreshing(isRefreshing);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setComplete() {
        setIsRefreshing(false);
        setIsLoading(false);
    }

}
