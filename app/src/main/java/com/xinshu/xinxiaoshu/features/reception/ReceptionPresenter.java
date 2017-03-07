package com.xinshu.xinxiaoshu.features.reception;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by sinyuk on 2017/3/6.
 */

public class ReceptionPresenter implements ReceptionContract.Presenter {
    private final ReceptionContract.View view;
    private final CompositeDisposable disposables;

    @Inject
    public ReceptionPresenter(ReceptionContract.View view) {
        this.view = view;
        disposables = new CompositeDisposable();
    }

    @Inject
    void set() {
        view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        disposables.clear();
    }

    @Override
    public void fetchPlayerInfo() {

    }

    @Override
    public void offline() {

    }

    @Override
    public void online() {

    }

    @Override
    public void ordering() {

    }
}
