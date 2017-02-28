package com.xinshu.xinxiaoshu.injector.modules;

import com.xinshu.xinxiaoshu.features.timeline.FriendsViewContract;
import com.xinshu.xinxiaoshu.injector.scopes.FragmentScope;

import org.jetbrains.annotations.NotNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sinyuk on 2017/2/27.
 */
@Module
public class FriendsViewModule {
    @NotNull
    private final FriendsViewContract.View view;

    public FriendsViewModule(@NotNull FriendsViewContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    FriendsViewContract.View view() {
        return view;
    }
}
