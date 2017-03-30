package com.xinshu.xinxiaoshu.features.login;

import android.support.annotation.NonNull;

import com.xinshu.xinxiaoshu.rest.RemoteDataRepository;
import com.xinshu.xinxiaoshu.rest.contract.LoginContract;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by sinyuk on 2017/3/29.
 */

public class LoginViewPresenter implements LoginViewContract.Presenter {
    private final LoginContract mLoginContract;
    private final LoginViewContract.View mView;

    private final CompositeDisposable mCompositeDisposable;

    @Inject
    public LoginViewPresenter(
            final RemoteDataRepository loginContract,
            final LoginViewContract.View view) {
        this.mLoginContract = loginContract;
        this.mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void checkRegistered(@NonNull String phone) {

        DisposableObserver<Boolean> d = mLoginContract.checkRegisteration(phone)
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean registered) {
                        if (registered) {
                            mView.registered();
                        } else {
                            mView.notRegistered();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mCompositeDisposable.add(d);
    }
}
