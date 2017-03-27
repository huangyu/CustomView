package com.huangyu.customviewlibrary.refreshandload_view;

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

/**
 * Created by huangyu on 2017/3/25.
 */

public class RefreshAndLoadView extends LinearLayout {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private RefreshAndLoadListener mRefreshAndLoadListener;

    private boolean isLoading;
    private boolean isRefreshing;

    public RefreshAndLoadView(Context context) {
        super(context);
        init(context);
    }

    public RefreshAndLoadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private boolean isRefreshing() {
        return isRefreshing;
    }

    private void setIsRefreshing(final boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });
    }

    private boolean isLoading() {
        return isLoading;
    }

    private void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setComplete() {
        setIsRefreshing(false);
        setIsLoading(false);
    }

    public RefreshAndLoadView setRefreshAndLoadListener(RefreshAndLoadListener refreshAndLoadListener) {
        this.mRefreshAndLoadListener = refreshAndLoadListener;
        return this;
    }

    public RefreshAndLoadView setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.mRecyclerView.setLayoutManager(layoutManager);
        return this;
    }

    public RefreshAndLoadView setAdapter(RecyclerView.Adapter adapter) {
        this.mRecyclerView.setAdapter(adapter);
        return this;
    }

    public void startRefresh() {
        setIsRefreshing(true);
        if (mRefreshAndLoadListener != null) {
            mRefreshAndLoadListener.onRefresh();
        }
    }

    private void init(Context context) {
        View mView = LayoutInflater.from(context).inflate(R.layout.layout_pull_load_more, this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_red_dark, android.R.color.holo_orange_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isRefreshing()) {
                    startRefresh();
                }
            }
        });

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
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

                if (firstItem == 0 || firstItem == RecyclerView.NO_POSITION) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }

                if (!isRefreshing() && !isLoading()
                        && (lastItem == totalItemCount - 1)
                        && (dx > 0 || dy > 0)) {
                    setIsLoading(true);
                    if (mRefreshAndLoadListener != null) {
                        mRefreshAndLoadListener.onLoad();
                    }
                }
            }
        });
    }

}
