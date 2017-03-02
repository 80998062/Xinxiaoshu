package com.xinshu.xinxiaoshu.features.upload;

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
import com.xinshu.xinxiaoshu.databinding.LayoutListBinding;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;

import java.util.List;

/**
 * Created by sinyuk on 2017/3/2.
 */

public class UploadView extends BaseListView implements UploadContract.View {

    private LayoutListBinding binding;
    private FriendsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListView();

        initData();

        presenter.refresh();
    }


    private void initListView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setItemPrefetchEnabled(true);
        manager.setInitialPrefetchItemCount(6);
        manager.setAutoMeasureEnabled(true);
//        FrameLayout.LayoutParams lps = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lps.topMargin = ConvertUtils.dp2px(getContext(), 15);
//        binding.recyclerView.setLayoutParams(lps);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemAnimator(null);
        final DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_tl));
        binding.recyclerView.addItemDecoration(decoration);
    }

    private void initData() {
        adapter = new FriendsAdapter(R.layout.item_data, presenter);
        adapter.setHasStableIds(true);
        binding.recyclerView.setAdapter(adapter);
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
    protected boolean registerForEventBus() {
        return false;
    }

    @Override
    public void setData(List<SnsInfoModel> models, boolean clear) {
        adapter.setData(models, clear);
    }

    @Override
    public void startRefreshing() {
        binding.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopRefreshing() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(Throwable throwable) {

    }

    @Override
    public void showNoMore() {

    }

    @Override
    public void showEmpty() {

    }

    private UploadContract.Presenter presenter;

    @Override
    public void setPresenter(UploadContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.subscribe();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.unsubscribe();
        }
    }
}
