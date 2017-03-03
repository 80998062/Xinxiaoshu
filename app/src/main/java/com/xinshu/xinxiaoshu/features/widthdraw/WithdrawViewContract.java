package com.xinshu.xinxiaoshu.features.widthdraw;

import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.mvp.BaseView;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;

import java.util.List;

/**
 * Created by sinyuk on 2017/3/3.
 */

public class WithdrawViewContract {
    public interface Presenter extends BasePresenter {

        void refresh();

        void loadmore();

    }

    public interface View extends BaseView<Presenter> {

        void setData(List<SnsInfoModel> models, boolean clear);

        void startRefreshing();

        void stopRefreshing();

        void showError(Throwable throwable);

        void showNoMore();

        void showEmpty();
    }
}
