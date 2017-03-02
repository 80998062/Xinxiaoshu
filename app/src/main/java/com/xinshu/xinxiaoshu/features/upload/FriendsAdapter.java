package com.xinshu.xinxiaoshu.features.upload;

import android.support.v7.widget.RecyclerView;

import com.xinshu.xinxiaoshu.BR;
import com.xinshu.xinxiaoshu.base.BaseSingleAdapter;
import com.xinshu.xinxiaoshu.base.BindingViewHolder;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;

import java.util.List;

/**
 * Created by sinyuk on 2017/3/2.
 */

public class FriendsAdapter extends BaseSingleAdapter<SnsInfoModel> {

    public FriendsAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected long getMyItemId(int position) {
        if (mDataSet.size() > position)
            return mDataSet.get(position).getData().timestamp;
        return RecyclerView.NO_ID;
    }

    @Override
    protected void onBindMyItemViewHolder(BindingViewHolder holder, int itemPositionInData, List<Object> payloads) {

    }

    @Override
    protected void onBindMyItemViewHolder(BindingViewHolder holder, int itemPositionInData) {
        final SnsInfoModel data = mDataSet.get(itemPositionInData);
        holder.getBinding().setVariable(BR.data, data);
    }
}
