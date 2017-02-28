package com.xinshu.xinxiaoshu.features.splash;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sinyuk.myutils.system.ToastUtils;
import com.xinshu.xinxiaoshu.App;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseFragment;
import com.xinshu.xinxiaoshu.core.Task;
import com.xinshu.xinxiaoshu.databinding.InitViewBinding;
import com.xinshu.xinxiaoshu.features.timeline.FriendsActivity;

import javax.inject.Inject;

/**
 * Created by sinyuk on 2017/2/23.
 */

public class InitView extends BaseFragment {
    public static final String TAG = "InitView";

    private InitViewBinding binding;

    @Inject
    Task task;

    @Inject
    ToastUtils toastUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.init_view, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        addDisposable(App.get(context).appComponentOB()
                .doOnComplete(this::testRoot)
                .subscribe(component -> {
                    component.inject(InitView.this);
                }));
    }

    private void testRoot() {
        addDisposable(task.testRoot()
                .doOnSubscribe(d -> startLoading())
                .subscribe(root -> {
                    if (root) {
                        makeDir();
                    } else {
                        stopLoading(new Throwable(getString(R.string.hint_device_not_rooted)));
                    }
                }));

    }

    private void makeDir() {
        addDisposable(task.makeDirOB()
                .doOnError(Throwable::printStackTrace)
                .doOnError(this::stopLoading)
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
                .doOnError(this::stopLoading)
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
                .doOnError(this::stopLoading)
                .subscribe(file -> {
                    Log.d(TAG, "copySnsDb");
                    if (file.exists()) {
                        Log.d(TAG, "copySnsDb: SUCCEED");
                        stopLoading(null);
                    } else {
                        toastUtils.toastShort(R.string.hint_ayscn_db_failed);
                    }
                }));
    }

    private void showEnterButton(boolean succeed) {
        if (succeed) {
            binding.enterBtn.setText("进入");
            binding.enterBtn.setOnClickListener(view -> FriendsActivity.start(view.getContext()));
        } else {
            binding.enterBtn.setText("关闭");
            binding.enterBtn.setOnClickListener(view -> {
                getActivity().finish();
                System.exit(0);
            });
        }
        binding.enterBtn.setVisibility(View.VISIBLE);
    }


    public void startLoading() {
        binding.loadingView.show();
    }


    public void stopLoading(Throwable throwable) {
        final boolean succeed = throwable == null;
        final String hint = succeed ? getString(R.string.hint_init_succeed) : println(throwable);
        final int textColor = succeed ? R.color.text_color_primary : R.color.theme_red;

        binding.loadingView.hide();
        binding.hint.setText(hint);
        binding.hint.setTextColor(getResources().getColor(textColor));
        showEnterButton(succeed);
    }

    private String println(Throwable throwable) {
        String message = "";
        for (int i = 0; i < throwable.getStackTrace().length; i++) {
            message += throwable.getStackTrace()[i] + "\n";
        }
        return message;
    }

    @Override
    protected boolean registerForEventBus() {
        return false;
    }

}
