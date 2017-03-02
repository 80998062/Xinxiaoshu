package com.xinshu.xinxiaoshu.injector.components;

import com.xinshu.xinxiaoshu.features.upload.UploadActivity;
import com.xinshu.xinxiaoshu.injector.modules.UploadModule;
import com.xinshu.xinxiaoshu.injector.scopes.FragmentScope;

import dagger.Subcomponent;

/**
 * Created by sinyuk on 2017/3/2.
 */
@FragmentScope
@Subcomponent( modules = UploadModule.class)
public interface UploadComponent {
    void inject(UploadActivity activity);
}
