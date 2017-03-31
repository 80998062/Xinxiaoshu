package com.xinshu.xinxiaoshu.rest.contract;

import com.xinshu.xinxiaoshu.rest.entity.OrderEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by sinyuk on 2017/3/31.
 */

public interface OrderContract {
    Observable<Boolean> online();

    Observable<List<OrderEntity>> requestOrder();
}
