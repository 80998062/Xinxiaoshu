package com.xinshu.xinxiaoshu.features.reception;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xinshu.xinxiaoshu.App;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseActivity;
import com.xinshu.xinxiaoshu.databinding.ActivityReceptionBinding;
import com.xinshu.xinxiaoshu.features.upload.UploadActivity;
import com.xinshu.xinxiaoshu.injector.modules.ReceptionModule;

import javax.inject.Inject;

/**
 * Created by sinyuk on 2017/3/2.
 */

public class ReceptionActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, ReceptionActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected boolean registerEventBus() {
        return false;
    }

    @Inject
    ReceptionPresenter receptionPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityReceptionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_reception);

        ReceptionView receptionView = new ReceptionView();

        App.get(this).getAppComponent().plus(new ReceptionModule(receptionView)).inject(this);

        addFragment(receptionView, false);

        setupListeners(binding);
    }

    private void setupListeners(ActivityReceptionBinding binding) {
        binding.avatar.setOnClickListener(view -> {
        });

        binding.uploadBtn.setOnClickListener(view -> UploadActivity.start(view.getContext()));
    }


}
