package com.xinshu.xinxiaoshu.features.reception;

import com.xinshu.xinxiaoshu.config.State;
import com.xinshu.xinxiaoshu.rest.RemoteDataRepository;
import com.xinshu.xinxiaoshu.rest.contract.OrderContract;
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
    @NonNull
    private final OrderContract mOrderContract;

    @Inject
    public ReceptionPresenter(@NonNull ReceptionContract.View view,
                              @NonNull RemoteDataRepository remoteDataStore) {
        this.mView = view;
        this.mUserContract = remoteDataStore;
        this.mOrderContract = remoteDataStore;
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
                                mView.offlineSucceed();
                                break;
                            case State.ONLINE:
                                mView.onlineSucceed();
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.refreshFailed(e);
                        e.printStackTrace();
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
        mView.offlineSucceed();
    }

    @Override
    public void online() {
        DisposableObserver<Boolean> d = mOrderContract.online()
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(final Boolean succeed) {
                        if (succeed) {
                            mView.onlineSucceed();
                        } else {
                            mView.onlineFailed(new Throwable("上线操作失败"));
                        }
                    }

                    @Override
                    public void onError(final Throwable e) {
                        mView.onlineFailed(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        disposables.add(d);
    }

    @Override
    public void ordering() {

    }
}
