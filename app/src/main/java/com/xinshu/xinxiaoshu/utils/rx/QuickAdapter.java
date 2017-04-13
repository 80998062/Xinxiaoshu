/*
 * Apache License
 *
 * Copyright [2017] Sinyuk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xinshu.xinxiaoshu.utils.rx;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinshu.xinxiaoshu.BR;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.mvp.BasePresenter;

import java.util.List;

/**
 * Created by sinyuk on 2017/3/19.
 *
 * @param <T> the type parameter
 */
public abstract class QuickAdapter<T> extends BaseQuickAdapter<T, BindingViewHolder> {
    protected final BasePresenter presenter;

    /**
     * Instantiates a new Quick adapter.
     *
     * @param layoutResId the layout res id
     * @param data        the data
     * @param presenter   the presenter
     */
    public QuickAdapter(final int layoutResId, final List<T> data, final BasePresenter presenter) {
        super(layoutResId, data);
        this.presenter = presenter;
    }

    @Override
    protected void convert(final BindingViewHolder helper, final T item) {

        if (item == null) return;

        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.data, item);
//        binding.setVariable(BR.presenter, presenter);
//        binding.setVariable(BR.position, helper.getAdapterPosition());
        bindExtras(helper, item);
        binding.executePendingBindings();
    }

    /**
     * Bind extras.
     *
     * @param helper the helper
     * @param item   the item
     */
    protected abstract void bindExtras(final BindingViewHolder helper, final T item);


    @Override
    protected View getItemView(final int layoutResId, final ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
        return view;
    }

    /**
     * Sets data.
     *
     * @param newData the new data
     * @param clean   the clean
     */
    public void setData(final List<T> newData, final boolean clean) {
        if (clean) {
            setNewData(newData);
        } else {
            addData(newData);
        }
    }
}
