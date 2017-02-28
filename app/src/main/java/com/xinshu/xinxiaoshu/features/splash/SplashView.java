package com.xinshu.xinxiaoshu.features.splash;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseActivity;
import com.xinshu.xinxiaoshu.databinding.LayoutActivityBinding;

public class SplashView extends BaseActivity {


    public static final String TAG = "SplashView";

    private LayoutActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.layout_activity);

        binding.setToolbarTitle("初始化");

        askForPermissions();

        addFragment(new InitView(), false);
    }

    @Override
    protected boolean registerEventBus() {
        return false;
    }

    private void askForPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        // Must be done during an initialization phase like onCreate
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MODIFY_PHONE_STATE,
                Manifest.permission.INTERNET)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        Log.d(TAG, "permissions granted");
                    } else {
                        // Opps permission denied
                        Log.d(TAG, "permissions denied");
                    }
                });
    }
}
