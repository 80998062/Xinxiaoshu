package com.xinshu.xinxiaoshu.features.extras;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xinshu.xinxiaoshu.base.BaseFragment;
import com.xinshu.xinxiaoshu.mvp.BasePresenter;

/**
 * Created by sinyuk on 2017/3/6.
 */

public class TutorialView extends BaseFragment {

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
