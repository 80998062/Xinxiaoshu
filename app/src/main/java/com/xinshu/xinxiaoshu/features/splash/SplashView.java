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
import com.xinshu.xinxiaoshu.features.reception.ReceptionActivity;
import com.xinshu.xinxiaoshu.rest.RemoteDataRepository;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class SplashView extends BaseActivity {


    public static final String TAG = "SplashView";

    @Inject
    RemoteDataRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Disposable d = App.get(this).appComponentOB()
                .doOnComplete(this::askForPermissions)
                .subscribe(c -> c.inject(SplashView.this));

        addDisposable(d);
    }

    @Inject
    Task task;

    @Inject
    ToastUtils toastUtils;


    private void askForPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        // Must be done during an initialization phase like onCreate
//

        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MODIFY_PHONE_STATE,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.INTERNET)
                .doOnTerminate(this::testRoot)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        Log.d(TAG, "permissions granted");
                    } else {
                        // Opps permission denied
                        Log.d(TAG, "permissions denied");
                        toastUtils.toastLong(R.string.hint_permissions_denied);
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
                    if (file.exists()) {
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
            if (mRepository.getToken().get() != null) {
                checkCredit();
            } else {
                LoginActivity.start(SplashView.this);
                overridePendingTransition(0, 0);
            }
        } else {
            toastUtils.toastLong(throwable.getMessage());
        }

        finish();
    }

    private void checkCredit() {
        Disposable d = mRepository.userInfo()
                .doOnError(e -> LoginActivity.start(SplashView.this))
                .subscribe(entity -> {
                    if (entity != null) {
                        ReceptionActivity.start(SplashView.this, entity);
                        overridePendingTransition(0, 0);
                    } else {
                        LoginActivity.start(SplashView.this);
                        overridePendingTransition(0, 0);
                    }
                });
        addDisposable(d);
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
