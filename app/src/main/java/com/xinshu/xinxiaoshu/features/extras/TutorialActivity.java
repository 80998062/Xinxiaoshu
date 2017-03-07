package com.xinshu.xinxiaoshu.features.extras;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseActivity;
import com.xinshu.xinxiaoshu.databinding.LayoutActivityBinding;

/**
 * Created by sinyuk on 2017/3/6.
 */

public class TutorialActivity extends BaseActivity {
    @Override
    protected boolean registerEventBus() {
        return false;
    }

    private LayoutActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_activity);
        binding.setToolbarTitle(getString(R.string.activity_tutorial));

        addFragment(new TutorialView(), false);
    }
}
