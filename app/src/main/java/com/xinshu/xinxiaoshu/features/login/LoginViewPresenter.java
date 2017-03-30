package com.xinshu.xinxiaoshu.features.login;

import android.support.annotation.NonNull;
import android.util.Log;

import com.xinshu.xinxiaoshu.rest.RemoteDataRepository;
import com.xinshu.xinxiaoshu.rest.contract.LoginContract;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

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

    @Inject
    void set() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.dispose();
    }

    public static final String TAG = "LoginViewPresenter";

    @Override
    public void checkRegistered(@NonNull String phone) {
        Log.d(TAG, "checkRegistered: " + phone);

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

    @Override
    public void getCaptcha(final String phone, final int cd) {
        Disposable coolDownDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Long::intValue)
                .map(duration -> cd - duration)
                .take(cd + 1)
                .doOnTerminate(mView::cdRefresh)
                .doOnDispose(mView::cdRefresh)
                .subscribe(mView::inCD);


        mLoginContract.getCaptcha(phone)
                .doOnTerminate(coolDownDisposable::dispose)
                .subscribeWith(new DisposableObserver<Boolean>() {

                    @Override
                    public void onNext(Boolean s) {
                        if (s) {
                            mView.getCaptchaSucceed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.getCaptchaFailed(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
