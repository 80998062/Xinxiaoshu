package com.xinshu.xinxiaoshu.features.reception;

import com.xinshu.xinxiaoshu.config.State;
import com.xinshu.xinxiaoshu.rest.RemoteDataRepository;
import com.xinshu.xinxiaoshu.rest.contract.UserContract;
import com.xinshu.xinxiaoshu.rest.entity.UserEntity;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by sinyuk on 2017/3/6.
 */

public class ReceptionPresenter implements ReceptionContract.Presenter {
    @NonNull
    private final ReceptionContract.View mView;
    private final CompositeDisposable disposables;
    @NonNull
    private final UserContract mUserContract;

    @Inject
    public ReceptionPresenter(@NonNull ReceptionContract.View view,
                              @NonNull RemoteDataRepository remoteDataStore) {
        this.mView = view;
        this.mUserContract = remoteDataStore;
        disposables = new CompositeDisposable();
    }

    @Inject
    void set() {
        mView.setPresenter(this);
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
        DisposableObserver<UserEntity> d = mUserContract.userInfo()
                .subscribeWith(new DisposableObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity entity) {
                        mView.bindPlayer(entity);
                        switch (entity.status) {
                            case State.LOGOUT:
                                break;
                            case State.OFFLINE:
                                mView.showOffline();
                                break;
                            case State.ONLINE:
                                mView.showOnline();
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.refreshFailed(e);

                    }

                    @Override
                    public void onComplete() {
                        mView.refreshCompleted();
                    }
                });

        disposables.add(d);
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
