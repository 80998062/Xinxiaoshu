package com.xinshu.xinxiaoshu.viewmodels;

/**
 * Created by sinyuk on 2017/1/3.
 */

public interface ViewModel<T> {
    T getData();

    void setData(T data);
}
