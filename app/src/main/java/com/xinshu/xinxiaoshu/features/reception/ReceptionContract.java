package com.xinshu.xinxiaoshu.features.reception;

import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.mvp.BaseView;

/**
 * Created by sinyuk on 2017/3/2.
 */

public class ReceptionContract {
    public interface Presenter extends BasePresenter {

        void fetchPlayerInfo();

        void offline();

        void online();

        void ordering();

    }

    public interface View extends BaseView<Presenter> {

        void bindPlayer(Object player);

        void showOffline();

        void showOnline();

        void showGetReception(Object reception);

        void waitOrderingResult();

        void showOrderingResult(boolean succeed);
    }
}
