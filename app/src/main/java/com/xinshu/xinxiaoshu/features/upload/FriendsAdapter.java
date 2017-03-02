package com.xinshu.xinxiaoshu.features.upload;

import android.support.v7.widget.RecyclerView;

import com.xinshu.xinxiaoshu.BR;
import com.xinshu.xinxiaoshu.base.BaseSingleAdapter;
import com.xinshu.xinxiaoshu.base.BindingViewHolder;
import com.xinshu.xinxiaoshu.databinding.ItemDataBinding;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;
import com.xinshu.xinxiaoshu.widgets.DownloadProgressButton;

import java.util.List;

/**
 * Created by sinyuk on 2017/3/2.
 */

public class FriendsAdapter extends BaseSingleAdapter<SnsInfoModel> {
    private UploadContract.Presenter presenter;

    public FriendsAdapter(int layoutId, UploadContract.Presenter presenter) {
        super(layoutId);
        this.presenter = presenter;
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

        ItemDataBinding binding = (ItemDataBinding) holder.getBinding();
        binding.uploadButton.setOnDownLoadClickListener(new DownloadProgressButton.SimpleOnDownLoadClickListener() {
            @Override
            public void clickDownload() {
                super.clickDownload();
                binding.uploadButton.setProgress(100);
                presenter.upload(data.getData(), itemPositionInData);
            }
        });
    }


}
