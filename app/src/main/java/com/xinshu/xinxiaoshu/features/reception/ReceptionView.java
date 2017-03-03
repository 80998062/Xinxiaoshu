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

public class ReceptionView extends BaseFragment {
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
}
