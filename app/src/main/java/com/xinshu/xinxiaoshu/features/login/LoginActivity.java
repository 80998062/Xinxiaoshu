package com.xinshu.xinxiaoshu.features.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xinshu.xinxiaoshu.App;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseActivity;
import com.xinshu.xinxiaoshu.databinding.LayoutActivityBinding;
import com.xinshu.xinxiaoshu.injector.modules.LoginViewModule;

import javax.inject.Inject;

/**
 * Created by sinyuk on 2017/3/1.
 */

public class LoginActivity extends BaseActivity {

    private LayoutActivityBinding binding;

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Inject
    LoginViewPresenter loginViewPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_activity);
        binding.setToolbarTitle(getString(R.string.activity_login));
        binding.actionBtn.setVisibility(View.INVISIBLE);

        LoginView loginView = new LoginView();
        App.get(this).appComponentOB().subscribe(c ->
                c.plus(new LoginViewModule(loginView)).inject(LoginActivity.this));

        addFragment(loginView, false);

    }

    @Override
    protected boolean registerEventBus() {
        return false;
    }
}
