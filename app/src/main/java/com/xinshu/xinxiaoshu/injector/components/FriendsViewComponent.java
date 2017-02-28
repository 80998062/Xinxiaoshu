package com.xinshu.xinxiaoshu.injector.components;

import com.xinshu.xinxiaoshu.features.timeline.FriendsActivity;
import com.xinshu.xinxiaoshu.injector.modules.FriendsViewModule;
import com.xinshu.xinxiaoshu.injector.scopes.FragmentScope;

import dagger.Subcomponent;

/**
 * Created by sinyuk on 2017/2/27.
 */
@FragmentScope
@Subcomponent( modules = FriendsViewModule.class)
public interface FriendsViewComponent {
    void inject(FriendsActivity friendsActivity);
}
