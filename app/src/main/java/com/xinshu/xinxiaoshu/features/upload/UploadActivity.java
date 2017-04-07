package com.xinshu.xinxiaoshu.features.upload;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseActivity;
import com.xinshu.xinxiaoshu.databinding.ActivityUploadBinding;
import com.xinshu.xinxiaoshu.services.PTRService;

/**
 * Created by sinyuk on 2017/3/1.
 */

public class UploadActivity extends BaseActivity {

    @Override
    protected boolean registerEventBus() {
        return false;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, UploadActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityUploadBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_upload);

        final UploadView uploadList = new UploadView();

        addFragment(uploadList, false);

        uploadList.setUserVisibleHint(true);

        setupListeners(binding);
    }


    public void setupListeners(final ActivityUploadBinding binding) {
        binding.uploadBtn.setOnClickListener(view -> {
            PTRService.start(view.getContext());
            finish();
            overridePendingTransition(0, 0);
        });
    }
}
