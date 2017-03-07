package com.xinshu.xinxiaoshu.features.extras;

import android.content.Context;
import android.content.Intent;
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

    public static void start(Context context) {
        Intent starter = new Intent(context, TutorialActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_activity);
        binding.setToolbarTitle(getString(R.string.activity_tutorial));

        addFragment(new TutorialView(), false);
    }
}
