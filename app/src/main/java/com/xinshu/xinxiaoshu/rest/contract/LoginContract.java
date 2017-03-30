package com.xinshu.xinxiaoshu.rest.contract;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by sinyuk on 2017/3/28.
 */

public interface LoginContract {
    Observable<Boolean> checkRegisteration(@NonNull String phone);

    Observable getCaptcha(@NonNull String phone);


}
