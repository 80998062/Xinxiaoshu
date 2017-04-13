package com.xinshu.xinxiaoshu.features.upload;

import android.support.annotation.NonNull;

import com.xinshu.xinxiaoshu.models.SnsInfo;
import com.xinshu.xinxiaoshu.mvp.BasePresenter;
import com.xinshu.xinxiaoshu.mvp.BaseView;
import com.xinshu.xinxiaoshu.mvp.ListViewContract;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;

/**
 * Created by sinyuk on 2017/3/2.
 */

public class UploadContract {
    public interface Presenter extends BasePresenter {
        void refresh();

        void upload(@NonNull SnsInfo data, int position);

    }

    public interface View extends BaseView<Presenter>, ListViewContract<SnsInfoModel> {
        void uploadCompleted();

        void updateProgress(float p);

        void uploadFailed(Throwable e);

        void showEmpty();
    }
}
