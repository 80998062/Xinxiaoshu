package com.xinshu.xinxiaoshu.rest;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.xinshu.xinxiaoshu.rest.contract.RemoteDataStore;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by sinyuk on 2017/3/28.
 */

public class RemoteDataRepository implements RemoteDataStore {

    private final XinshuService xinshuService;
    private final SchedulerTransformer schedulerTransformer;
    private final RxSharedPreferences preferences;

    @Inject
    public RemoteDataRepository(
            final XinshuService xinshuService,
            final SchedulerTransformer schedulerTransformer,
            final RxSharedPreferences preferences) {
        this.xinshuService = xinshuService;
        this.schedulerTransformer = schedulerTransformer;
        this.preferences = preferences;
    }

    @Override
    public Observable<Boolean> checkRegisteration(@NonNull String phone) {
        return xinshuService.checkRegistration(phone)
                .compose(new ErrorTransformer<>())
                .map(r -> r.registered)
                .compose(schedulerTransformer);
    }


    @Override
    public Observable<Boolean> getCaptcha(@NonNull String phone) {
        return xinshuService.captcha(phone)
                .compose(new ErrorTransformer<>())
                .map(r -> r.succeed())
                .compose(schedulerTransformer);
    }
}
