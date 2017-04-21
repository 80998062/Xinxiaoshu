package com.xinshu.xinxiaoshu.rest.contract;

import com.xinshu.xinxiaoshu.rest.entity.UserEntity;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by sinyuk on 2017/3/28.
 */
public interface LoginContract {
    /**
     * Check registeration observable.
     *
     * @param phone the phone
     * @return the observable
     */
    Observable<Boolean> checkRegisteration(@NonNull String phone);

    /**
     * Gets captcha.
     *
     * @param phone the phone
     * @return the captcha
     */
    Observable<Boolean> getCaptcha(@NonNull String phone);

    /**
     * Login observable.
     *
     * @param phone   the phone
     * @param captcha the captcha
     * @return the observable
     */
    Observable<UserEntity> login(@NonNull String phone, @NonNull String captcha);


    void logout();


}
