package com.xinshu.xinxiaoshu.rest.contract;

import com.xinshu.xinxiaoshu.rest.entity.UserEntity;

import io.reactivex.Observable;

/**
 * Created by sinyuk on 2017/3/31.
 */
public interface UserContract {
    /**
     * User info observable.
     *
     * @return the observable
     */
    Observable<UserEntity> userInfo();
}
