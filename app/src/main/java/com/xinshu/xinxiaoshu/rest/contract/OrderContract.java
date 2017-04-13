package com.xinshu.xinxiaoshu.rest.contract;

import com.xinshu.xinxiaoshu.rest.entity.OrderEntity;

import io.reactivex.Observable;

/**
 * Created by sinyuk on 2017/3/31.
 */
public interface OrderContract {
    /**
     * Online observable.
     *
     * @return the observable
     */
    Observable<Boolean> online();

    /**
     * Assignments observable.
     *
     * @return the observable
     */
    Observable<OrderEntity> assignments();
}
