package com.xinshu.xinxiaoshu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.xinshu.xinxiaoshu.mvp.BasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Sinyuk on 16/7/6.
 */
public abstract class BaseFragment extends Fragment {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (registerForEventBus()) {
            EventBus.getDefault().register(this);
        }

        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }


    }

    protected abstract boolean registerForEventBus();

    protected abstract void doInjection();

    protected abstract BasePresenter getPresenter();

    /**
     * 保存Fragment的Hidden状态
     * 避免内存重启导致的Fragment重叠
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    protected void addDisposable(Disposable s) {
        mCompositeDisposable.add(s);
    }

    protected void removeDisposable(Disposable s) {
        mCompositeDisposable.remove(s);
    }

    protected void clearDisposable() {
        mCompositeDisposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }

        if (getPresenter() != null) {
            getPresenter().unsubscribe();
        }
    }

    @Subscribe()
    public void onEvent() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getPresenter() != null) {
            getPresenter().subscribe();
        }
    }
}
