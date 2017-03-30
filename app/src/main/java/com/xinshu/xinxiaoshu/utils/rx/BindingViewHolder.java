package com.xinshu.xinxiaoshu.utils.rx;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.xinshu.xinxiaoshu.R;

/**
 * Created by Prefs on 2016/10/13.
 */
public class BindingViewHolder<T extends ViewDataBinding> extends BaseViewHolder {
    /**
     * The M binding.
     */
    private final T mBinding;

    /**
     * Instantiates a new Binding view holder.
     *
     * @param view the view
     */
    public BindingViewHolder(final View view) {
        super(view);
        mBinding = (T) getConvertView().getTag(R.id.BaseQuickAdapter_databinding_support);
    }

    /**
     * Gets binding.
     *
     * @return the binding
     */
    public T getBinding() {
        return mBinding;
    }
}