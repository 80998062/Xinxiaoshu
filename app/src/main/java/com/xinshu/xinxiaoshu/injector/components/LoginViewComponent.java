package com.xinshu.xinxiaoshu.injector.components;

import com.xinshu.xinxiaoshu.features.login.LoginActivity;
import com.xinshu.xinxiaoshu.injector.modules.LoginViewModule;
import com.xinshu.xinxiaoshu.injector.scopes.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by sinyuk on 2017/3/29.
 */
@ActivityScope
@Subcomponent(modules = LoginViewModule.class)
public interface LoginViewComponent {
    void inject(LoginActivity activity);
}
