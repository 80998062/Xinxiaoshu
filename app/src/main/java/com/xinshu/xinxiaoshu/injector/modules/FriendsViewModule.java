package com.xinshu.xinxiaoshu.injector.modules;

import android.support.annotation.NonNull;

import com.xinshu.xinxiaoshu.features.timeline.FriendsViewContract;
import com.xinshu.xinxiaoshu.injector.scopes.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sinyuk on 2017/2/27.
 */
@Module
public class FriendsViewModule {
    @NonNull
    private final FriendsViewContract.View view;

    public FriendsViewModule(@NonNull FriendsViewContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    FriendsViewContract.View view() {
        return view;
    }
}
