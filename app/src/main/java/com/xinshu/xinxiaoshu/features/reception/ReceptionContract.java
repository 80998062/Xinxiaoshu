package com.xinshu.xinxiaoshu.features.reception;

import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.mvp.BaseView;
import com.xinshu.xinxiaoshu.rest.entity.OrderEntity;
import com.xinshu.xinxiaoshu.rest.entity.UserEntity;

import java.util.List;

/**
 * Created by sinyuk on 2017/3/2.
 */
public class ReceptionContract {
    /**
     * The interface Presenter.
     */
    public interface Presenter extends BasePresenter {

        /**
         * Fetch player info.
         */
        void fetchPlayerInfo();

        /**
         * Offline.
         */
        void offline();

        /**
         * Online.
         */
        void online();

        /**
         * Ordering.
         */
        void ordering();

    }

    /**
     * The interface View.
     */
    public interface View extends BaseView<Presenter> {

        /**
         * Bind player.
         *
         * @param entity the entity
         */
        void bindPlayer(UserEntity entity);

        /**
         * Show offline.
         */
        void offlineSucceed();

        /**
         * Offline failed.
         *
         * @param e the e
         */
        void offlineFailed(Throwable e);

        /**
         * Show online.
         */
        void onlineSucceed();

        /**
         * Online failed.
         *
         * @param e the e
         */
        void onlineFailed(Throwable e);

        /**
         * Show get reception.
         *
         * @param reception the reception
         */
        void showGetReception(List<OrderEntity> reception);

        /**
         * Wait ordering result.
         */
        void waitOrderingResult();

        /**
         * Show ordering result.
         *
         * @param succeed the succeed
         */
        void showOrderingResult(boolean succeed);

        /**
         * Start refreshing.
         */
        void startRefreshing();

        /**
         * Refresh failed.
         *
         * @param e the e
         */
        void refreshFailed(Throwable e);

        /**
         * Refresh completed.
         */
        void refreshCompleted();


    }
}
