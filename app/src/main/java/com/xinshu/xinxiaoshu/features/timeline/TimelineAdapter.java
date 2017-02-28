package com.xinshu.xinxiaoshu.features.timeline;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sinyuk.myutils.ConvertUtils;
import com.xinshu.xinxiaoshu.BR;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseAdapter;
import com.xinshu.xinxiaoshu.base.BaseSingleAdapter;
import com.xinshu.xinxiaoshu.base.BindingViewHolder;
import com.xinshu.xinxiaoshu.databinding.ItemTimelineBinding;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;

import java.util.List;

/**
 * Created by sinyuk on 2017/2/28.
 */

public class TimelineAdapter extends BaseSingleAdapter<SnsInfoModel> {
    public TimelineAdapter(int layoutId) {
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

        final ItemTimelineBinding timelineBinding = (ItemTimelineBinding) holder.getBinding();

        switch (data.getType()) {
            case "image": {
                final int size = data.getImages().size();

                // 解决滑动冲突
                // http://stackoverflow.com/questions/32525314/scroll-behavior-in-nested-recyclerview-with-horizontal-scroll
                timelineBinding.gridView.setNestedScrollingEnabled(false);

                final int spanCount = size == 1 ? 1 : 3;
                // 防止GridView消费滑动事件
                final GridLayoutManager layoutManager = new GridLayoutManager(
                        holder.getBinding().getRoot().getContext(), spanCount) {
                    @Override
                    public boolean canScrollHorizontally() {
                        return false;
                    }

                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };

                layoutManager.setAutoMeasureEnabled(true);

                timelineBinding.gridView.setLayoutManager(layoutManager);

                int padding = size == 1 ? ConvertUtils.dp2px(
                        holder.getBinding().getRoot().getContext(), 72) : 0;

                timelineBinding.gridView.setPadding(0, 0, padding, 0);

                timelineBinding.gridView.setHasFixedSize(true);
                timelineBinding.gridView.setAdapter(new PhotoCellAdapter(data.getImages()));

                break;
            }
        }

    }
}
