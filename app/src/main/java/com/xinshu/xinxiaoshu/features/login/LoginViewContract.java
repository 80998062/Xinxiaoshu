package com.xinshu.xinxiaoshu.features.login;

import android.support.annotation.NonNull;

import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.mvp.BaseView;

/**
 * Created by sinyuk on 2017/3/29.
 */

public interface LoginViewContract {
    public interface Presenter extends BasePresenter {
        void checkRegistered(@NonNull String phone);
    }

    public interface View extends BaseView<Presenter> {
        void registered();

        void notRegistered();
    }
}
