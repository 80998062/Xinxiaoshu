package com.xinshu.xinxiaoshu.injector.modules;

import com.sinyuk.myutils.system.ToastUtils;
import com.xinshu.xinxiaoshu.App;
import com.xinshu.xinxiaoshu.core.Task;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sinyuk on 2017/2/21.
 */
@Module
public class AppModule {
    public static final String TAG = "AppModule";
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Singleton
    @Provides
    App app() {
        return app;
    }


    @Singleton
    @Provides
    Task task(App app) {
        return new Task(app);
    }

    @Singleton
    @Provides
    ToastUtils toastUtils(App app) {
        return new ToastUtils(app);
    }

}
