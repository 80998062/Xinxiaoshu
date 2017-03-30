package com.xinshu.xinxiaoshu.injector.modules;

import com.xinshu.xinxiaoshu.features.login.LoginViewContract;
import com.xinshu.xinxiaoshu.injector.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sinyuk on 2017/3/29.
 */
@Module
public class LoginViewModule {
    private final LoginViewContract.View view;

    public LoginViewModule(LoginViewContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    LoginViewContract.View view() {
        return view;
    }
}
