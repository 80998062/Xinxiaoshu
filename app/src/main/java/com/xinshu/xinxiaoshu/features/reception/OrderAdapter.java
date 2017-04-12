package com.xinshu.xinxiaoshu.features.reception;

import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.rest.entity.OrderEntity;
import com.xinshu.xinxiaoshu.utils.rx.BindingViewHolder;
import com.xinshu.xinxiaoshu.utils.rx.QuickAdapter;

import java.util.List;

/**
 * Created by sinyuk on 2017/4/12.
 */

public class OrderAdapter extends QuickAdapter<OrderEntity> {


    /**
     * Instantiates a new Quick adapter.
     *
     * @param layoutResId the layout res id
     * @param data        the data
     * @param presenter   the presenter
     */
    public OrderAdapter(int layoutResId, List<OrderEntity> data, BasePresenter presenter) {
        super(layoutResId, data, presenter);
    }

    @Override
    protected void bindExtras(BindingViewHolder helper, OrderEntity item) {

    }
}
