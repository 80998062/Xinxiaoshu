package com.xinshu.xinxiaoshu.features.upload;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseActivity;
import com.xinshu.xinxiaoshu.core.Config;
import com.xinshu.xinxiaoshu.databinding.ActivityUploadBinding;
import com.xinshu.xinxiaoshu.events.HideFloatingEvent;
import com.xinshu.xinxiaoshu.services.PTRService;
import com.xinshu.xinxiaoshu.utils.PermissionHelper;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

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

        EventBus.getDefault().post(new HideFloatingEvent());

        RxPermissions rxPermissions = new RxPermissions(UploadActivity.this);
        Disposable d = rxPermissions
                .request(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .subscribe(granted -> {
                    if (granted) {
                        // pass
                    } else {
                        PermissionHelper.showAlertWindowDialog(UploadActivity.this);
                    }
                });

        addDisposable(d);
    }


    public void setupListeners(final ActivityUploadBinding binding) {
        binding.uploadBtn.setOnClickListener(view -> {
            PTRService.start(view.getContext());
            goToWechat();
            finish();
        });
    }

    private void goToWechat() {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(Config.WECHAT_PACKAGE);
        startActivity(launchIntent);
    }
}
