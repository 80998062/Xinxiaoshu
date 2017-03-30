/*
 * Apache License
 *
 * Copyright [2017] Sinyuk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xinshu.xinxiaoshu.rest;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;


/**
 * The type Io scheduler transformer.
 *
 * @param <T> the type parameter
 */
@Singleton
public final class SchedulerTransformer<T> implements ObservableTransformer<T, T> {
    private final Scheduler executeScheduler;
    private final Scheduler postScheduler;

    /**
     * Instantiates a new Scheduler transformer.
     *
     * @param executeScheduler the execute scheduler
     * @param postScheduler    the post scheduler
     */
    public SchedulerTransformer(Scheduler executeScheduler, Scheduler postScheduler) {
        this.executeScheduler = executeScheduler;
        this.postScheduler = postScheduler;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .subscribeOn(executeScheduler)
                .observeOn(postScheduler);
    }
}