package com.xinshu.xinxiaoshu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xinshu.xinxiaoshu.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Sinyuk on 16/6/30.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static String TAG = "";
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = this.getClass().getSimpleName();

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (registerEventBus()) {
            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);
        }
    }

    protected abstract boolean registerEventBus();

    protected void addDisposable(Disposable s) {
        mCompositeDisposable.add(s);
    }

    protected void removeDisposable(Disposable s) {
        mCompositeDisposable.remove(s);
    }

    protected void clearDisposable() {
        mCompositeDisposable.clear();
    }

    public void addFragment(Fragment fragment, boolean addToBackStack) {
        final String tag = fragment.getClass().getSimpleName();
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag);
            if (addToBackStack) {
                ft.addToBackStack(tag);
            }
            ft.commit();
        }
    }

    @Subscribe()
    public void onEvent() {
    }

    public void onNavback(View v) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}
