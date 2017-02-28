package com.xinshu.xinxiaoshu.features.timeline;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xinshu.xinxiaoshu.App;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseActivity;
import com.xinshu.xinxiaoshu.core.SnsReader;
import com.xinshu.xinxiaoshu.databinding.ActivityTimelineBinding;
import com.xinshu.xinxiaoshu.injector.modules.FriendsViewModule;

import javax.inject.Inject;

/**
 * Created by sinyuk on 2017/2/27.
 */

public class FriendsActivity extends BaseActivity {

    public static final String TAG = "FriendsActivity";

    private ActivityTimelineBinding binding;

    public static void start(Context context) {
        Intent starter = new Intent(context, FriendsActivity.class);
        context.startActivity(starter);
    }

    private FriendsList friendList;

    @Inject
    FriendPresenter friendPresenter;

    @Inject
    SnsReader snsReader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);

        friendList = new FriendsList();

        addDisposable(
                App.get(this).databaseComponentOB()
                        .doOnComplete(this::afterInjection)
                        .subscribe(c -> {
                            c.plus(new FriendsViewModule(friendList)).inject(FriendsActivity.this);
                        }));


    }

    private void afterInjection() {
        if (friendPresenter == null) {
            Log.e(TAG, "friendPresenter: NULL");
        }

        if (snsReader == null) {
            Log.e(TAG, "snsReader: NULL");
        }
        addFragment(friendList, false);
    }

    @Override
    protected boolean registerEventBus() {
        return false;
    }
}
