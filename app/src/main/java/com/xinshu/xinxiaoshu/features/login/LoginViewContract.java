package com.xinshu.xinxiaoshu.features.login;

import android.support.annotation.NonNull;

import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.mvp.BaseView;
import com.xinshu.xinxiaoshu.rest.entity.UserEntity;

/**
 * Created by sinyuk on 2017/3/29.
 */

public interface LoginViewContract {
    public interface Presenter extends BasePresenter {
        void checkRegistered(@NonNull String phone);

        void getCaptcha(String phone, int cd);

        void login(String phone, String captcha);
    }

    public interface View extends BaseView<Presenter> {
        void registered();

        void notRegistered();

        void inCD(int countDown);

        void cdRefresh();

        void getCaptchaSucceed();

        void getCaptchaFailed(Throwable e);

        void loginSucceed(UserEntity userEntity);

        void loginFailed(Throwable e);
    }
}
