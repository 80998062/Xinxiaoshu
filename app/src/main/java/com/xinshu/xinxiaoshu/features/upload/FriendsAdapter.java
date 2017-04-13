package com.xinshu.xinxiaoshu.features.upload;

import android.support.v7.widget.RecyclerView;

import com.xinshu.xinxiaoshu.databinding.ItemDataBinding;
import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.utils.rx.BindingViewHolder;
import com.xinshu.xinxiaoshu.utils.rx.QuickAdapter;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;

import java.util.List;

/**
 * Created by sinyuk on 2017/3/2.
 */

public class FriendsAdapter extends QuickAdapter<SnsInfoModel> {


    /**
     * Instantiates a new Quick adapter.
     *
     * @param layoutResId the layout res id
     * @param data        the data
     * @param presenter   the presenter
     */
    public FriendsAdapter(int layoutResId, List<SnsInfoModel> data, BasePresenter presenter) {
        super(layoutResId, data, presenter);
    }

    @Override
    public long getItemId(int position) {
        if (getData().size() > position)
            return getData().get(position).getData().timestamp;
        return RecyclerView.NO_ID;
    }

    @Override
    protected void bindExtras(BindingViewHolder helper, SnsInfoModel item) {
        ((ItemDataBinding) helper.getBinding()).uploadButton.setOnClickListener(v ->
                ((UploadPresenter) presenter).upload(item.getData(), helper.getAdapterPosition()));
    }
}
