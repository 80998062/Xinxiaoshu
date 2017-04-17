package com.xinshu.xinxiaoshu.features.upload;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinshu.xinxiaoshu.App;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.LazyListView;
import com.xinshu.xinxiaoshu.databinding.LayoutListBinding;
import com.xinshu.xinxiaoshu.injector.modules.UploadModule;
import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;

import java.util.List;

import javax.inject.Inject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sinyuk on 2017/3/2.
 */

public class UploadView extends LazyListView implements UploadContract.View {

    private LayoutListBinding binding;
    private FriendsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_list, container, false);
        return binding.getRoot();
    }

    @Override
    protected void lazyDo() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), OrientationHelper.VERTICAL);
        dividerItemDecoration.setDrawable(
                ContextCompat.getDrawable(getContext(), R.drawable.divider));

        configLayoutManager(dividerItemDecoration);

        doInjection();
    }

    @Override
    protected boolean needPullToRefresh() {
        return true;
    }

    @Override
    protected int getPrefetchCount() {
        return 0;
    }

    @Override
    public void onPullDown() {
        presenter.refresh();
    }

    @Override
    public void onLoadMore() {

    }


    @Override
    protected boolean registerForEventBus() {
        return false;
    }

    @Inject
    UploadPresenter uploadPresenter;

    @Override
    protected void doInjection() {
        addDisposable(App.get(getContext()).databaseComponentOB()
                .subscribe(c -> {
                    c.plus(new UploadModule(UploadView.this)).inject(UploadView.this);

                    configAdapter();

                    presenter.refresh();
                }));

    }

    private void configAdapter() {
        adapter = new FriendsAdapter(R.layout.item_data, null, presenter);
        adapter.setHasStableIds(true);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setData(List<SnsInfoModel> models, boolean clear) {
        adapter.setData(models, clear);
    }

    @Override
    public void error(Throwable e) {
        toErrorView(R.layout.layout_error_default, e.getLocalizedMessage());
    }

    @Override
    public void loadingCompleted() {
        toLoadMoreCompleted();
    }

    @Override
    public void loadingFailed(Throwable e) {
        toLoadMoreFailed(e);
    }

    @Override
    public void loadingNomore() {
        toLoadMoreEnd();
    }

    @Override
    public void refreshFailed() {
        toEmptyView(R.layout.layout_empty_default);
    }

    @Override
    public void refreshCompleted() {
        toRefreshingCompeted();
    }


    private UploadContract.Presenter presenter;

    @Override
    public void setPresenter(UploadContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void uploadCompleted() {
        Log.d(getTag(), "uploadCompleted: ");
        alertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        alertDialog.setTitleText(getString(R.string.hint_upload_succeed));
        alertDialog.setContentText(null);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.showCancelButton(false);
        alertDialog.setConfirmText(getString(R.string.action_confirm));
        alertDialog.setConfirmClickListener(DialogInterface::cancel);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    @Override
    public void updateProgress(final float p) {
        Log.d(getTag(), "updateProgress: " + p);
    }

    @Override
    public void uploadFailed(Throwable e) {
        alertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        alertDialog.setTitleText(e.getLocalizedMessage());
        alertDialog.setContentText(null);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.showCancelButton(false);
        alertDialog.setConfirmText(getString(R.string.action_confirm));
        alertDialog.setConfirmClickListener(DialogInterface::cancel);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    @Override
    public void showEmpty() {
        alertDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText(getString(R.string.hint_upload_empty));
        alertDialog.setContentText(null);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.showCancelButton(false);
        alertDialog.setConfirmText(getString(R.string.action_confirm));
        alertDialog.setConfirmClickListener(DialogInterface::cancel);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    private SweetAlertDialog alertDialog;

    @Override
    public void uploadStart() {
        alertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        alertDialog.setTitleText(getString(R.string.hint_upload_ing));
        alertDialog.setContentText(getString(R.string.hint_upload_cost_time));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelText(getString(R.string.action_cancel));
        alertDialog.setOnCancelListener(DialogInterface::cancel);
        alertDialog.show();
    }
}
