package com.xinshu.xinxiaoshu.rest;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import retrofit2.Response;

/**
 * The type Default transformer.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public class DefaultTransformer<T extends Response<R>, R extends BaseResponse>
        implements ObservableTransformer<T, R> {

    private final SchedulerTransformer<R> schedulerTransformer;

    /**
     * Instantiates a new Default transformer.
     *
     * @param schedulerTransformer the scheduler transformer
     */
    public DefaultTransformer(final SchedulerTransformer schedulerTransformer) {
        this.schedulerTransformer = schedulerTransformer;
    }

    @Override
    public ObservableSource<R> apply(final Observable<T> upstream) {
        return upstream
                .compose(new ErrorTransformer<>())
                .compose(schedulerTransformer);
    }
}