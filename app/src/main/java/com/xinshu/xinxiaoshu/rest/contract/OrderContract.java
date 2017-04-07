package com.xinshu.xinxiaoshu.rest.contract;

import io.reactivex.Observable;

/**
 * Created by sinyuk on 2017/3/31.
 */

public interface OrderContract {
    Observable<Boolean> online();
}
