package com.xinshu.xinxiaoshu.features.widthdraw;

import com.xinshu.xinxiaoshu.base.BaseListView;

/**
 * Created by sinyuk on 2017/3/3.
 */

public class WithdrawView extends BaseListView{
    @Override
    protected boolean registerForEventBus() {
        return false;
    }

    @Override
    protected boolean needPullToRefresh() {
        return false;
    }

    @Override
    protected void onPullDown() {

    }

    @Override
    protected void onReachBottom() {

    }
}
