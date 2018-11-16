package com.wind.pulltorefresh;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;

public class PullToRefershView implements OnRefreshStateListener {
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_COMPLETE = 3;
    private RecyclerView mRecyclerView;
    private OnRefreshListener mOnRefreshListener;
    private boolean mLoading;
    private RefreshableAdapter mRefreshableAdapter;
    private int mThreshold;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public PullToRefershView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener());
        RecyclerView.Adapter realAdapter = mRecyclerView.getAdapter();
        mRefreshableAdapter = new RefreshableAdapter(realAdapter);
        mRecyclerView.setAdapter(mRefreshableAdapter);


        //查看是否有SwipeRefreshLayout

        mSwipeRefreshLayout = findSwipeRefreshLayout(recyclerView);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onPullDownToRefresh(mRefreshableAdapter);
                    }
                }
            });
        }

    }

    private SwipeRefreshLayout findSwipeRefreshLayout(RecyclerView recyclerView) {
        View view=getParentView(recyclerView);
        while (view != null) {
            if (view instanceof SwipeRefreshLayout) {
                return (SwipeRefreshLayout) view;
            }
            view=getParentView(view);
        }
        return null;
    }

    private View getParentView(View childView){
        ViewParent parent = childView.getParent();
        View view;
        if (parent instanceof View) {
            view = (View) parent;
        } else {
            view = null;
        }
        return view;
    }
    @Override
    public void onRefreshState(RefreshState state) {
        switch (state) {
            case STATE_ERROR:
            case STATE_COMPLETE:
                mLoading = false;
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case STATE_LOADING:
                mLoading = true;
                break;
        }
        mRefreshableAdapter.setRefreshState(state);

    }

    /**
     * 刷新状态回调
     *
     * @return
     */
    public OnRefreshStateListener getRefreshStateListener() {
        return this;
    }

    /**
     * 显示加载更多的最小值
     *
     * @param threshod
     * @return
     */
    public PullToRefershView threshold(int threshod) {
        this.mThreshold = threshod;
        boolean show = mRefreshableAdapter.getRealAdapter().getItemCount() >= threshod;
        mRefreshableAdapter.toggleLoadMoreItem(show);
        return this;
    }

    private class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            /* 当 所有item 都无法填充满屏幕时，向下拉也会触发
            if (!mLoading && !recyclerView.canScrollVertically(1)) {
                mLoading = true;
                mRefreshableAdapter.showLoading();
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onPullUpToRefresh();
                }
            }*/

        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) //向下滚动
            {
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (manager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
                    int visibleItemCount = linearLayoutManager.getChildCount();    //得到显示屏幕内的list数量
                    int totalItemCount = linearLayoutManager.getItemCount();    //得到list的总数量
                    int pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();//得到显示屏内的第一个list的位置数position

                    if (!mLoading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        mLoading = true;
                        mRefreshableAdapter.showLoading();
                        if (mOnRefreshListener != null) {
                            mOnRefreshListener.onPullUpToRefresh(mRefreshableAdapter);
                        }
                    }
                }

            }

        }
    }

    public PullToRefershView onRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
        return this;
    }

    public interface OnRefreshListener {
        void onPullDownToRefresh(RefreshableAdapter adapter);

        void onPullUpToRefresh(RefreshableAdapter adapter);
    }


}
