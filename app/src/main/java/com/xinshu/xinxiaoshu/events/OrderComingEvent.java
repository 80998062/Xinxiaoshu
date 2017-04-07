package com.xinshu.xinxiaoshu.events;

import com.xinshu.xinxiaoshu.rest.entity.OrderEntity;

import org.greenrobot.eventbus.util.HasExecutionScope;

import java.util.List;

/**
 * Created by sinyuk on 2017/4/7.
 */
public class OrderComingEvent implements HasExecutionScope {
    private Object executionScope;

    @Override
    public Object getExecutionScope() {
        return executionScope;
    }

    @Override
    public void setExecutionScope(Object o) {
        executionScope = o;
    }


    private final List<OrderEntity> orderEntities;

    /**
     * Instantiates a new Order coming event.
     *
     * @param orderEntities the order entities
     */
    public OrderComingEvent(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    /**
     * Gets order entities.
     *
     * @return the order entities
     */
    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }
}
