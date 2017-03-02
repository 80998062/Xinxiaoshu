package com.xinshu.xinxiaoshu.injector.modules;

import com.xinshu.xinxiaoshu.features.reception.ReceptionContract;
import com.xinshu.xinxiaoshu.injector.scopes.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sinyuk on 2017/3/2.
 */
@Module
public class ReceptionModule {
    private final ReceptionContract.View view;

    public ReceptionModule(ReceptionContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    ReceptionContract.View view() {
        return view;
    }
}
