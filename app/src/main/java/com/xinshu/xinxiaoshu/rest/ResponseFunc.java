package com.xinshu.xinxiaoshu.rest;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by sinyuk on 2017/4/5.
 */

public class ResponseFunc<T> implements Function<BaseResponse<T>, Observable<T>> {

    public static final String TAG = "ResponseFunc";


    @Override
    public Observable<T> apply(@NonNull BaseResponse<T> response) throws Exception {
        if (response.succeed())
            return Observable.just(response.getData());
        return Observable.error(new BaseException(response.getCode(), response.getMsg()));
    }
}
