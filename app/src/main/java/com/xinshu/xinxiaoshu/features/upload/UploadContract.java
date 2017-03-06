package com.xinshu.xinxiaoshu.features.upload;

import android.support.annotation.NonNull;

import com.xinshu.xinxiaoshu.models.SnsInfo;
import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.mvp.BaseView;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;

import java.util.List;

/**
 * Created by sinyuk on 2017/3/2.
 */

public class UploadContract {
    public interface Presenter extends BasePresenter {
        void refresh();

        void upload(@NonNull SnsInfo data, int position);

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
