package com.xinshu.xinxiaoshu.injector.modules;

import com.xinshu.xinxiaoshu.features.upload.UploadContract;
import com.xinshu.xinxiaoshu.injector.scopes.FragmentScope;

import org.greenrobot.greendao.annotation.NotNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sinyuk on 2017/3/2.
 */
@Module
public class UploadModule {
    @NotNull
    private final UploadContract.View view;

    public UploadModule(@NotNull UploadContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    UploadContract.View view() {
        return view;
    }
}
