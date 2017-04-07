package com.xinshu.xinxiaoshu.features.reception;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xinshu.xinxiaoshu.App;
import com.xinshu.xinxiaoshu.R;
import com.xinshu.xinxiaoshu.base.BaseActivity;
import com.xinshu.xinxiaoshu.injector.modules.ReceptionModule;
import com.xinshu.xinxiaoshu.rest.entity.UserEntity;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by sinyuk on 2017/3/2.
 */
public class ReceptionActivity extends BaseActivity {

    /**
     * Start.
     *
     * @param context the context
     * @param entity  the entity
     */
    public static void start(final Context context, final UserEntity entity) {
        Intent starter = new Intent(context, ReceptionActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle extras = new Bundle();
        extras.putSerializable("user", entity);
        starter.putExtras(extras);
        context.startActivity(starter);
    }

    /**
     * Start.
     *
     * @param context the context
     */
    public static void start(Context context) {
        Intent starter = new Intent(context, ReceptionActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected boolean registerEventBus() {
        return false;
    }

    /**
     * The Reception presenter.
     */
    @Inject
    ReceptionPresenter receptionPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_reception);
        ReceptionView receptionView = new ReceptionView();

        if (getIntent().getExtras() != null) {
            UserEntity entity = (UserEntity) getIntent().getExtras().getSerializable("user");
            if (entity != null) {
                if (receptionView.getArguments() == null) {
                    Bundle extras = new Bundle();
                    extras.putSerializable("user", entity);
                    receptionView.setArguments(extras);
                } else {
                    receptionView.getArguments().putSerializable("user", entity);
                }
            }

        }

        Disposable d =
                App.get(this).appComponentOB().subscribe(c -> {

                    c.plus(new ReceptionModule(receptionView)).inject(ReceptionActivity.this);
                    addFragment(receptionView, false);
                });

        addDisposable(d);

    }

}
