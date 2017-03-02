package com.xinshu.xinxiaoshu.injector.components;

import com.xinshu.xinxiaoshu.injector.modules.DatabaseModule;
import com.xinshu.xinxiaoshu.injector.modules.FriendsViewModule;
import com.xinshu.xinxiaoshu.injector.modules.UploadModule;
import com.xinshu.xinxiaoshu.injector.scopes.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by sinyuk on 2017/2/21.
 */
@ActivityScope
@Subcomponent(modules = {DatabaseModule.class})
public interface DatabaseComponent {
    FriendsViewComponent plus(FriendsViewModule module);

    UploadComponent plus(UploadModule module);
}
