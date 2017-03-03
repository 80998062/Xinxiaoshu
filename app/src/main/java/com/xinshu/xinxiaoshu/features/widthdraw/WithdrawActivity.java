package com.xinshu.xinxiaoshu.features.widthdraw;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseActivity;
import com.xinshu.xinxiaoshu.databinding.LayoutActivityBinding;

/**
 * Created by sinyuk on 2017/3/3.
 */

public class WithdrawActivity extends BaseActivity {

    private WithdrawView withdrawView;

    public static void start(Context context) {
        Intent starter = new Intent(context, WithdrawActivity.class);
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
        
        binding.setToolbarTitle(getString(R.string.activity_withdraw));

        withdrawView = new WithdrawView();

        addFragment(withdrawView, false);
    }
}
