package com.xinshu.xinxiaoshu.rest.contract;

import com.xinshu.xinxiaoshu.rest.entity.OrderEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by sinyuk on 2017/4/6.
 */

public interface PushContact {
    Observable<List<OrderEntity>> requestOrder();
}
