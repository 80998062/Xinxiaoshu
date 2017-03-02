package com.xinshu.xinxiaoshu.features.upload;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xinshu.xinxiaoshu.App;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseActivity;
import com.xinshu.xinxiaoshu.databinding.LayoutActivityBinding;
import com.xinshu.xinxiaoshu.injector.modules.UploadModule;

import javax.inject.Inject;

/**
 * Created by sinyuk on 2017/3/1.
 */

public class UploadActivity extends BaseActivity {
    private UploadView uploadList;

    @Override
    protected boolean registerEventBus() {
        return false;
    }

    private LayoutActivityBinding binding;

    public static void start(Context context) {
        Intent starter = new Intent(context, UploadActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_activity);
        binding.setToolbarTitle(getString(R.string.activity_upload));
        uploadList = new UploadView();

        addDisposable(App.get(this).databaseComponentOB()
                .doOnComplete(this::afterInjection)
                .subscribe(c -> {
                    c.plus(new UploadModule(uploadList)).inject(UploadActivity.this);
                }));
    }

    @Inject
    UploadPresenter uploadPresenter;

    private void afterInjection() {
        addFragment(uploadList, false);
    }
}
