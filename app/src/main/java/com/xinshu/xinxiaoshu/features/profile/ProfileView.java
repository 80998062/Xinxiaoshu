package com.xinshu.xinxiaoshu.features.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseFragment;
import com.xinshu.xinxiaoshu.databinding.ProfileViewBinding;
import com.xinshu.xinxiaoshu.mvp.BasePresenter;

/**
 * Created by sinyuk on 2017/4/21.
 */

public class ProfileView extends BaseFragment {
    @Override
    protected boolean registerForEventBus() {
        return false;
    }

    @Override
    protected void doInjection() {

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }


    private ProfileViewBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_view, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.logout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("确认退出当前账号吗?")
                .setCancelable(true)
                .setPositiveButton("取消", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("退出", (dialog, which) -> {
                    // pass
                    dialog.cancel();
                })
                .show();
    }
}
