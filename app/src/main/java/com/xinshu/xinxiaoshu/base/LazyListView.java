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

package com.xinshu.xinxiaoshu.base;

import android.os.Bundle;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by sinyuk on 2017/3/29.
 */

public abstract class LazyListView extends BaseListView {

    /**
     * The Is view initiated.
     */
    protected boolean isViewInitiated;
    /**
     * The Is visible to user.
     */
    protected boolean isVisibleToUser;
    /**
     * The Is data initiated.
     */
    protected boolean isDataInitiated;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }


    /**
     * Prepare fetch data boolean.
     *
     * @return the boolean
     */
    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    /**
     * Prepare fetch data boolean.
     *
     * @param forceUpdate the force update
     * @return the boolean
     */
    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            lazyDo();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    /**
     * Lazy do.
     */
    protected abstract void lazyDo();


    /**
     * On lazy event.
     */
    @Subscribe
    public void onLazyListEvent() {
    }
}
