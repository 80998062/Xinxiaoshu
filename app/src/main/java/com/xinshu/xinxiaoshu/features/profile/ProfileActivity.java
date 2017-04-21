package com.xinshu.xinxiaoshu.features.profile;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseActivity;
import com.xinshu.xinxiaoshu.databinding.LayoutActivityBinding;

/**
 * Created by sinyuk on 2017/4/21.
 */
public class ProfileActivity extends BaseActivity {

    /**
     * Start.
     *
     * @param context the context
     */
    public static void start(Context context) {
        Intent starter = new Intent(context, ProfileActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected boolean registerEventBus() {
        return false;
    }

    private LayoutActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_activity);
        binding.setToolbarTitle("个人中心");


        addFragment(new ProfileView(), false);
    }
}
