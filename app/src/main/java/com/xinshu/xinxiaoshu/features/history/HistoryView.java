package com.xinshu.xinxiaoshu.features.history;

import com.xinshu.xinxiaoshu.base.LazyListView;
import com.xinshu.xinxiaoshu.mvp.BasePresenter;

/**
 * Created by sinyuk on 2017/3/3.
 */

public class HistoryView extends LazyListView{

    @Override
    protected boolean registerForEventBus() {
        return false;
    }

    @Override
    protected void doInjection() {

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void lazyDo() {

    }

    @Override
    protected boolean needPullToRefresh() {
        return false;
    }

    @Override
    protected int getPrefetchCount() {
        return 0;
    }

    @Override
    public void onPullDown() {

    }

    @Override
    public void onLoadMore() {

    }
}
