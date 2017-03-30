package com.xinshu.xinxiaoshu.features.history;

import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.mvp.BaseView;

/**
 * Created by sinyuk on 2017/3/3.
 */

public class HistoryViewContract {
    public interface Presenter extends BasePresenter {

        void refresh();

        void loadmore();

    }

    public interface View extends BaseView<Presenter> {
    }
}
