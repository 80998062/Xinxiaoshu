package com.xinshu.xinxiaoshu.features.upload;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.xinshu.xinxiaoshu.core.SnsReader;
import com.xinshu.xinxiaoshu.models.SnsInfo;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by sinyuk on 2017/3/2.
 */

public class UploadPresenter implements UploadContract.Presenter {
    private final UploadContract.View view;
    private final SnsReader snsReader;
    private final CompositeDisposable disposables;

    public static final String TAG = "UploadPresenter";

    @Inject
    public UploadPresenter(UploadContract.View view, SnsReader snsReader) {
        this.view = view;
        this.snsReader = snsReader;
        this.disposables = new CompositeDisposable();
    }

    @Inject
    public void setupListener() {
        view.setPresenter(this);
    }

    @Override
    public void refresh() {
        Disposable d = snsReader.copyAndOpenDB()
                .flatMap(getFriendsFunc)
                .subscribeWith(new DisposableSingleObserver<List<SnsInfoModel>>() {
                    @Override
                    public void onSuccess(final List<SnsInfoModel> f) {
                        if (f.isEmpty()) {
                            view.refreshFailed();
                        } else {
                            view.setData(f, true);
                            view.refreshCompleted();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.error(e);
                    }
                });

        disposables.add(d);
    }

    private final Function<SQLiteDatabase, SingleSource<List<SnsInfoModel>>> getFriendsFunc = new Function<SQLiteDatabase, SingleSource<List<SnsInfoModel>>>() {
        @Override
        public SingleSource<List<SnsInfoModel>> apply(@NonNull SQLiteDatabase db) throws Exception {
            return snsReader.distinctTimelineOB()
                    .map(SnsInfoModel.func);
        }
    };

    @Override
    public void upload(@NonNull SnsInfo data, int position) {
        Disposable d = snsReader.copyAndOpenDB()
                .flatMap(new Function<SQLiteDatabase, SingleSource<List<SnsInfo>>>() {
                    @Override
                    public SingleSource<List<SnsInfo>> apply(@NonNull SQLiteDatabase db) throws Exception {
                        return snsReader.timelineByUsernameOB(data.authorId);
                    }
                })
                .subscribe(f -> {
                    if (f.isEmpty()) {
                        // pass
                    } else {
                        Log.d(TAG, "upload: " + f.toString());
                        upload2Server(f, position);
                    }
                });

        disposables.add(d);
    }

    private void upload2Server(List<SnsInfo> f, int position) {

//        snsReader.convert2JSONOB(f)
//                .flatMap(new Function<JSONArray, SingleSource<?>>() {
//                    @Override
//                    public SingleSource<?> apply(@NonNull JSONArray jsonArray) throws Exception {
//                        return null;
//                    }
//                });
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
