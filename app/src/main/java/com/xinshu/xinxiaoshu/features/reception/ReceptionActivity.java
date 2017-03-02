package com.xinshu.xinxiaoshu.features.reception;

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
    @Override
    protected boolean registerEventBus() {
        return false;
    }

    private ActivityReceptionBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reception);
    }


}
