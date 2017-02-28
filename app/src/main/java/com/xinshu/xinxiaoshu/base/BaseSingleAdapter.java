package com.xinshu.xinxiaoshu.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by sinyuk on 2017/2/28.
 */

public abstract class BaseSingleAdapter<T> extends BaseAdapter<T> {
    protected final int layoutId;

    public BaseSingleAdapter(int layoutId) {
        super();
        this.layoutId = layoutId;
    }

    @Override
    protected BindingViewHolder onCreateMyItemViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), layoutId, parent, false);
        return new BindingViewHolder<>(binding);
    }
}
