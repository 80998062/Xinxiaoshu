package com.xinshu.xinxiaoshu.features.reception;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseFragment;
import com.xinshu.xinxiaoshu.databinding.ReceptionViewBinding;
import com.xinshu.xinxiaoshu.features.history.HistoryActivity;
import com.xinshu.xinxiaoshu.features.widthdraw.WithdrawActivity;

/**
 * Created by sinyuk on 2017/3/2.
 */

public class ReceptionView extends BaseFragment implements ReceptionContract.View{
    @Override
    protected boolean registerForEventBus() {
        return false;
    }

    private ReceptionViewBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.reception_view, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListener();
    }

    private void setupListener() {


        binding.tutorial.setOnClickListener(this::onClickTutorial);
        binding.historyCount.setOnClickListener(this::gotoHistory);
        binding.withdrawAmount.setOnClickListener(this::gotoWithdraw);

        binding.offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.comming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void gotoWithdraw(View view) {
        WithdrawActivity.start(view.getContext());
    }

    private void gotoHistory(View view) {
        HistoryActivity.start(view.getContext());
    }

    /**
     * <p>
     * 阅读小编教程
     * </p>
     *
     * @param view
     */
    private void onClickTutorial(View view) {

    }

    private ReceptionContract.Presenter presenter;
    @Override
    public void setPresenter(ReceptionContract.Presenter presenter) {
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

    @Override
    public void bindPlayer(Object player) {

    }

    @Override
    public void showOffline() {

    }

    @Override
    public void showOnline() {

    }

    @Override
    public void showGetReception(Object reception) {

    }

    @Override
    public void waitOrderingResult() {

    }

    @Override
    public void showOrderingResult() {

    }
}
