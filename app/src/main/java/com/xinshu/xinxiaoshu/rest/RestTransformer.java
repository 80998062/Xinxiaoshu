package com.xinshu.xinxiaoshu.rest;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import retrofit2.Response;

/**
 * Created by sinyuk on 2017/3/29.
 */
@Singleton
public class RestTransformer<T extends Response<R>, R extends BaseResponse> implements ObservableTransformer<T, R> {

    private final SchedulerTransformer<R> schedulerTransformer;

    private final ErrorTransformer<T, R> errorTransformer;

    @Inject
    public RestTransformer(
            final SchedulerTransformer<R> schedulerTransformer,
            final ErrorTransformer<T, R> errorTransformer) {
        this.schedulerTransformer = schedulerTransformer;
        this.errorTransformer = errorTransformer;
    }

    @Override
    public ObservableSource<R> apply(Observable<T> upstream) {
        return upstream
                .compose(errorTransformer)
                .compose(schedulerTransformer);
    }
}
