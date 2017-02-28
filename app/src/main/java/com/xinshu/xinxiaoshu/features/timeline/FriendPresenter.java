package com.xinshu.xinxiaoshu.features.timeline;

import android.database.sqlite.SQLiteDatabase;

import com.xinshu.xinxiaoshu.core.SnsReader;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by sinyuk on 2017/2/27.
 */

public class FriendPresenter implements FriendsViewContract.Presenter {
    private final FriendsViewContract.View view;
    private final SnsReader snsReader;
    private final CompositeDisposable disposables;

    @Inject
    public FriendPresenter(FriendsViewContract.View view,
                           SnsReader snsReader) {
        this.view = view;
        this.snsReader = snsReader;
        this.disposables = new CompositeDisposable();
    }

    @Inject
    void set() {
        view.setPresenter(this);
    }

    public static final String TAG = "FriendPresenter";

    @Override
    public void refresh() {
        Disposable d = snsReader.copyAndOpenDB()
                .flatMap(func)
                .doOnSubscribe(dis -> view.startRefreshing())
                .doAfterTerminate(view::stopRefreshing)
                .doOnError(view::showError)
                .subscribe(f -> {
                    if (f.isEmpty()) {
                        view.showEmpty();
                    } else {
                        view.setData(f, true);
                    }
                });

        disposables.add(d);
    }

    private final Function<SQLiteDatabase, SingleSource<List<SnsInfoModel>>> func = new Function<SQLiteDatabase, SingleSource<List<SnsInfoModel>>>() {
        @Override
        public SingleSource<List<SnsInfoModel>> apply(@NonNull SQLiteDatabase db) throws Exception {
            return snsReader.distinctTimelineOB()
                    .map(SnsInfoModel.func);
        }
    };

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (!disposables.isDisposed())
            disposables.dispose();
    }
}
