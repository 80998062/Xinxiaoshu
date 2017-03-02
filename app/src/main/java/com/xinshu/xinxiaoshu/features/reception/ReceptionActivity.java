package com.xinshu.xinxiaoshu.features.reception;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseActivity;
import com.xinshu.xinxiaoshu.databinding.ActivityReceptionBinding;

/**
 * Created by sinyuk on 2017/3/2.
 */

public class ReceptionActivity extends BaseActivity {
    private ReceptionView receptionView;

    public static void start(Context context) {
        Intent starter = new Intent(context, ReceptionActivity.class);
        context.startActivity(starter);
    }
    @Override
    protected boolean registerEventBus() {
        return false;
    }

    private ActivityReceptionBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reception);

        receptionView = new ReceptionView();

        addFragment(receptionView, false);
    }


}
