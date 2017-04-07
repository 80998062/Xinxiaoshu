package com.xinshu.xinxiaoshu.events;

import org.greenrobot.eventbus.util.HasExecutionScope;

/**
 * Created by sinyuk on 2017/4/7.
 */

public class StopPollingEvent implements HasExecutionScope {
    private Object executionScope;

    @Override
    public Object getExecutionScope() {
        return executionScope;
    }

    @Override
    public void setExecutionScope(Object o) {
        executionScope = o;
    }
}