package com.xinshu.xinxiaoshu.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.databinding.LayoutNomoreBinding;
import com.xinshu.xinxiaoshu.widgets.BetterViewAnimator;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by sinyuk on 2017/1/4.
 */

public abstract class BaseListView extends BaseFragment {
    private static CharSequence oldMsg = null;
    private static int oldDuration = 0;
    private static Toast toast = null;
    private static long firstTime = 0L;
    private static long secondTime = 0L;

    private BetterViewAnimator viewAnimator;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    protected int PRELOAD_THRESHOLD = 3;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewAnimator = (BetterViewAnimator) view.findViewById(R.id.viewAnimator);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        if (swipeRefreshLayout != null) {
            if (needPullToRefresh()) {
                setSwipeRefreshLayout();
            } else {
                swipeRefreshLayout.setEnabled(false);
            }
        }
    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
        swipeRefreshLayout.setOnRefreshListener(this::onPullDown);
    }


    protected abstract boolean needPullToRefresh();


    public RecyclerView.OnScrollListener getLoadMoreListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                boolean isBottom =
                        layoutManager.findLastCompletelyVisibleItemPosition() >= recyclerView.getAdapter().getItemCount() - PRELOAD_THRESHOLD;
                if (isBottom) {
                    onReachBottom();
                }
            }
        };
    }

    protected abstract void onPullDown();

    protected abstract void onReachBottom();


    @CallSuper
    public void toLoadingView() {
        ((BaseAdapter) recyclerView.getAdapter()).removeFooterBinding();
        if (swipeRefreshLayout == null) {
            viewAnimator.setDisplayedChildId(R.id.recyclerView);
        } else {
            viewAnimator.setDisplayedChildId(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setRefreshing(true);
        }
    }


    @CallSuper
    public void toErrorView(String message) {
        toast(message, Toast.LENGTH_SHORT);
        viewAnimator.setDisplayedChildId(R.id.errorLayout);
    }

    @CallSuper
    public void toEmptyView(String message) {
        toast(message, Toast.LENGTH_SHORT);
        viewAnimator.setDisplayedChildId(R.id.emptyLayout);
    }

    protected LayoutNomoreBinding footerBinding;

    public void toNomoreView(String message) {
        if (footerBinding == null) {
            footerBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(getContext()), R.layout.layout_nomore, recyclerView, false);
        }
        if (TextUtils.isEmpty(message)) {
            footerBinding.hint.setText(message);
        }
        ((BaseAdapter) recyclerView.getAdapter()).addFooterBinding(footerBinding);
    }


    @Subscribe()
    public void onListEvent() {

    }


    protected void toast(CharSequence text, int duration) {
        if (toast == null) {
            firstTime = System.currentTimeMillis();
            toast = Toast.makeText(getActivity().getApplicationContext(), text, duration);
            toast.show();
        } else {
            secondTime = System.currentTimeMillis();
            if (text.equals(oldMsg)) {
                if (secondTime - firstTime > (long) oldDuration) {
                    toast.show();
                }
            } else {
                oldMsg = text;
                toast.setText(text);
                toast.show();
            }
        }

        firstTime = secondTime;
        oldDuration = duration;
    }

}
