package com.xinshu.xinxiaoshu.injector.modules;

import android.support.annotation.NonNull;

import com.xinshu.xinxiaoshu.features.upload.UploadContract;
import com.xinshu.xinxiaoshu.injector.scopes.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sinyuk on 2017/3/2.
 */
@Module
public class UploadModule {
    @NonNull
    private final UploadContract.View view;

    public UploadModule(@NonNull UploadContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    UploadContract.View view() {
        return view;
    }
}
