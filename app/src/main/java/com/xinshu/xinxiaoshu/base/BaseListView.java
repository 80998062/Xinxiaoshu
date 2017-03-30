package com.xinshu.xinxiaoshu.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinshu.xinxiaoshu.R;

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

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
    }

    /**
     * Config.
     *
     * @param root the root
     */
    public void findViews(final View root) {
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);

        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);

        if (recyclerView == null) {
            throw new AssertionError("You must a recyclerView in your root view");
        }

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

    /**
     * Config linear.
     *
     * @param decoration the decoration
     */
    @CallSuper
    protected void configLayoutManager(final RecyclerView.ItemDecoration decoration) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        manager.setItemPrefetchEnabled(true);
        manager.setInitialPrefetchItemCount(getPrefetchCount());
        manager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
    }


    /**
     * Need pull to refresh boolean.
     *
     * @return the boolean
     */
    protected abstract boolean needPullToRefresh();


    /**
     * Gets prefetch count.
     *
     * @return the prefetch count
     */
    protected abstract int getPrefetchCount();

    /**
     * Sets load more listener.
     *
     * @param adapter          the adapter
     * @param preloadThreshold the preload threshold
     */
    public void cofigLoadMore(
            final BaseQuickAdapter adapter,
            final int preloadThreshold) {
        adapter.disableLoadMoreIfNotFullPage(recyclerView);
        adapter.setAutoLoadMoreSize(preloadThreshold);
        adapter.disableLoadMoreIfNotFullPage(recyclerView);
        adapter.setOnLoadMoreListener(this::onLoadMore, recyclerView);
    }


    /**
     * On pull down.
     */
    public abstract void onPullDown();


    /**
     * On load more.
     */
    public abstract void onLoadMore();


    /**
     * Stop refreshing.
     */
    @CallSuper
    public void toRefreshingCompeted() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Load more failed.
     *
     * @param e the e
     */
    @CallSuper
    public void toLoadMoreFailed(Throwable e) {
        toast(e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        checkAdapter();
        ((BaseQuickAdapter) recyclerView.getAdapter()).loadMoreFail();
    }

    /**
     * Load more completed.
     */
    @CallSuper
    public void toLoadMoreCompleted() {
        checkAdapter();
        ((BaseQuickAdapter) recyclerView.getAdapter()).loadMoreComplete();
    }

    /**
     * Load more end.
     */
    public void toLoadMoreEnd() {
        checkAdapter();
        ((BaseQuickAdapter) recyclerView.getAdapter()).loadMoreEnd(false);
    }


    /**
     * Show empty view.
     *
     * @param view the view
     */
    @CallSuper
    public void toEmptyView(final View view) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        checkAdapter();
        ((BaseQuickAdapter) recyclerView.getAdapter()).setEmptyView(view);
    }


    /**
     * Show empty view.
     *
     * @param resId the res id
     */
    @CallSuper
    public void toEmptyView(@LayoutRes final int resId) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        checkAdapter();
        ((BaseQuickAdapter) recyclerView.getAdapter()).setEmptyView(resId);
    }


    /**
     * Show error view.
     *
     * @param view    the view
     * @param message the message
     */
    @CallSuper
    public void toErrorView(final View view, @Nullable final String message) {
        String msg = TextUtils.isEmpty(message) ? getString(R.string.hint_default_error_message) : message;
        toast(msg, Toast.LENGTH_SHORT);
        checkAdapter();
        ((BaseQuickAdapter) recyclerView.getAdapter()).setEmptyView(view);
    }


    /**
     * Show error view.
     *
     * @param resId   the res id
     * @param message the message
     */
    @CallSuper
    public void toErrorView(@LayoutRes final int resId, @Nullable final String message) {
        String msg = TextUtils.isEmpty(message) ? getString(R.string.hint_default_error_message) : message;
        toast(msg, Toast.LENGTH_SHORT);
        checkAdapter();
        ((BaseQuickAdapter) recyclerView.getAdapter()).setEmptyView(resId);
    }


    private void checkAdapter() {
        if (recyclerView.getAdapter() == null) {
            throw new AssertionError("Your recyclerView must attach a adapter first");
        }
        if (recyclerView.getAdapter() instanceof BaseQuickAdapter) {
            // pass
        } else {
            throw new AssertionError("Your adapter must be" + BaseQuickAdapter.class.getSimpleName());
        }
    }


    /**
     * On list event.
     */
    @Subscribe()
    public void onListEvent() {

    }


    /**
     * Toast.
     *
     * @param text     the text
     * @param duration the duration
     */
    public void toast(final CharSequence text, final int duration) {
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
