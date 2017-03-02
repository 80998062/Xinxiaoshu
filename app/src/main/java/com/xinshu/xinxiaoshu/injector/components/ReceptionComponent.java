package com.xinshu.xinxiaoshu.injector.components;

import com.xinshu.xinxiaoshu.injector.modules.ReceptionModule;
import com.xinshu.xinxiaoshu.injector.scopes.FragmentScope;

import dagger.Subcomponent;

/**
 * Created by sinyuk on 2017/3/2.
 */
@FragmentScope
@Subcomponent(modules = ReceptionModule.class)
public interface ReceptionComponent {
}
