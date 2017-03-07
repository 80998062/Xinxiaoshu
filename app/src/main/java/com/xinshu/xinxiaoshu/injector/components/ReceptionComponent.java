package com.xinshu.xinxiaoshu.injector.components;

import com.xinshu.xinxiaoshu.features.reception.ReceptionActivity;
import com.xinshu.xinxiaoshu.injector.modules.ReceptionModule;
import com.xinshu.xinxiaoshu.injector.scopes.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by sinyuk on 2017/3/2.
 */
@ActivityScope
@Subcomponent(modules = ReceptionModule.class)
public interface ReceptionComponent {
    void inject(ReceptionActivity receptionActivity);
}
