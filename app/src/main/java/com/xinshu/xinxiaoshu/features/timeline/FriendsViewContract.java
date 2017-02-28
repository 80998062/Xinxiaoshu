package com.xinshu.xinxiaoshu.features.timeline;

import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.mvp.BaseView;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;

import java.util.List;

/**
 * Created by sinyuk on 2017/2/27.
 */

public class FriendsViewContract {
    public interface Presenter extends BasePresenter {
        void refresh();
    }

    public interface View extends BaseView<Presenter> {
        void setData(List<SnsInfoModel> models, boolean clear);

        void startRefreshing();

        void stopRefreshing();

        void startLoading();

        void stopLoading();

        void showError(Throwable throwable);

        void showNoMore();

        void showEmpty();
    }
}
