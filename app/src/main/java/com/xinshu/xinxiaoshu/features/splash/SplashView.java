package com.xinshu.xinxiaoshu.features.splash;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.sinyuk.myutils.system.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinshu.xinxiaoshu.App;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseActivity;
import com.xinshu.xinxiaoshu.core.Task;
import com.xinshu.xinxiaoshu.features.login.LoginActivity;

import javax.inject.Inject;

public class SplashView extends BaseActivity {


    public static final String TAG = "SplashView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addDisposable(App.get(this).appComponentOB()
                .doOnComplete(this::askForPermissions)
                .subscribe(component -> {
                    component.inject(SplashView.this);
                }));

    }

    @Inject
    Task task;

    @Inject
    ToastUtils toastUtils;


    private void askForPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        // Must be done during an initialization phase like onCreate
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MODIFY_PHONE_STATE,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.INTERNET)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        Log.d(TAG, "permissions granted");
                        testRoot();
                    } else {
                        // Opps permission denied
                        Log.d(TAG, "permissions denied");
                        toastUtils.toastLong(R.string.hint_permissions_denied);
                        finish();
                        System.exit(0);
                    }
                });
    }

    private void testRoot() {
        toastUtils.toastShort(R.string.hint_app_init);
        addDisposable(task.testRoot()
                .subscribe(root -> {
                    if (root) {
                        makeDir();
                    } else {
                        initTerminate(new Throwable(getString(R.string.hint_device_not_rooted)));
                    }
                }));
    }


    private void makeDir() {
        addDisposable(task.makeDirOB()
                .doOnError(Throwable::printStackTrace)
                .doOnError(this::initTerminate)
                .subscribe(file -> {
                    if (file.exists()) {
                        moveWechatApk();
                    } else {
                        toastUtils.toastShort(R.string.hint_make_root_dir_failed);
                    }
                }));
    }

    private void moveWechatApk() {
        addDisposable(task.moveApkOB()
                .doOnError(Throwable::printStackTrace)
                .doOnError(this::initTerminate)
                .subscribe(file -> {
                    if (file.exists()) {
                        copySnsDb();
                    } else {
                        toastUtils.toastShort(R.string.hint_move_apk_failed);
                    }
                }));
    }

    private void copySnsDb() {
        addDisposable(task.copySnsDbOB()
                .doOnError(Throwable::printStackTrace)
                .doOnError(this::initTerminate)
                .subscribe(file -> {
                    Log.d(TAG, "copySnsDb");
                    if (file.exists()) {
                        Log.d(TAG, "copySnsDb: SUCCEED");
                        initTerminate(null);
                    } else {
                        toastUtils.toastShort(R.string.hint_ayscn_db_failed);
                    }
                }));
    }

    private void initTerminate(Throwable throwable) {
        final boolean succeed = throwable == null;
        final String hint = succeed ? getString(R.string.hint_init_succeed) : println(throwable);

        toastUtils.toastLong(hint);

        if (succeed) {
            LoginActivity.start(this);
        } else {
            finish();
            System.exit(0);
        }
    }

    private String println(Throwable throwable) {
        String message = "";
        for (int i = 0; i < throwable.getStackTrace().length; i++) {
            message += throwable.getStackTrace()[i] + "\n";
        }
        return message;
    }

    @Override
    protected boolean registerEventBus() {
        return false;
    }


}
