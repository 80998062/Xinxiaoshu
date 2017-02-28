package com.xinshu.xinxiaoshu.injector.components;

import com.xinshu.xinxiaoshu.features.splash.InitView;
import com.xinshu.xinxiaoshu.injector.modules.AppModule;
import com.xinshu.xinxiaoshu.injector.modules.DatabaseModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sinyuk on 2017/2/21.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    DatabaseComponent plus(DatabaseModule module);

    void inject(InitView initView);
}
