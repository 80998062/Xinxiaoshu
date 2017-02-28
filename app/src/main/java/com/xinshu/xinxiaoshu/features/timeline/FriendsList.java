package com.xinshu.xinxiaoshu.features.timeline;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseListView;
import com.xinshu.xinxiaoshu.databinding.TimelineViewBinding;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;

import java.util.List;

/**
 * Created by sinyuk on 2017/2/27.
 */

public class FriendsList extends BaseListView implements FriendsViewContract.View {

    private TimelineViewBinding binding;
    private TimelineAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.timeline_view, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initListView();
        initData();

        if (presenter != null) {
            presenter.refresh();
        }
    }

    private void initData() {
        adapter = new TimelineAdapter(R.layout.item_timeline);
        adapter.setHasStableIds(true);
        binding.recyclerView.setAdapter(adapter);
    }

    private void initListView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setItemPrefetchEnabled(true);
        manager.setInitialPrefetchItemCount(4);
        manager.setAutoMeasureEnabled(true);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemAnimator(null);
        final DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_tl));
        binding.recyclerView.addItemDecoration(decoration);
    }

    @Override
    protected boolean registerForEventBus() {
        return false;
    }

    @Override
    protected boolean needPullToRefresh() {
        return false;
    }

    @Override
    protected void onPullDown() {
        presenter.refresh();
    }

    @Override
    protected void onReachBottom() {

    }

    @Override
    public void setData(List<SnsInfoModel> friends, boolean clear) {
        adapter.setData(friends, true);
    }

    @Override
    public void startRefreshing() {

    }

    @Override
    public void stopRefreshing() {

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showError(Throwable throwable) {

    }

    @Override
    public void showNoMore() {
        toNomoreView(getString(R.string.hint_nomore_friend));
    }

    @Override
    public void showEmpty() {

    }

    private FriendsViewContract.Presenter presenter;

    @Override
    public void setPresenter(FriendsViewContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
