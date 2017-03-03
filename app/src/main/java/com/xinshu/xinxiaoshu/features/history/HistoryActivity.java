package com.xinshu.xinxiaoshu.features.history;

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

public class HistoryActivity extends BaseActivity {
    private HistoryView historyView;

    public static void start(Context context) {
        Intent starter = new Intent(context, HistoryActivity.class);
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
        binding.setToolbarTitle(getString(R.string.activity_history));

        historyView = new HistoryView();

        addFragment(historyView, false);
    }
}
