package com.xinshu.xinxiaoshu.features.upload;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.xinshu.xinxiaoshu.core.SnsReader;
import com.xinshu.xinxiaoshu.models.SnsInfo;
import com.xinshu.xinxiaoshu.rest.BaseException;
import com.xinshu.xinxiaoshu.rest.RemoteDataRepository;
import com.xinshu.xinxiaoshu.rest.contract.CloudContract;
import com.xinshu.xinxiaoshu.viewmodels.SnsInfoModel;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sinyuk on 2017/3/2.
 */
public class UploadPresenter implements UploadContract.Presenter {
    private final UploadContract.View view;
    private final SnsReader snsReader;
    private final CloudContract mCloudContract;
    private final CompositeDisposable disposables;
    private List<JSONArray> jsonArrayList;
    private CompositeDisposable uploadDisposables;

    /**
     * The constant TAG.
     */
    public static final String TAG = "UploadPresenter";

    /**
     * Instantiates a new Upload presenter.
     *
     * @param view       the view
     * @param snsReader  the sns reader
     * @param repository the repository
     */
    @Inject
    public UploadPresenter(
            final UploadContract.View view,
            final SnsReader snsReader,
            final RemoteDataRepository repository) {
        this.view = view;
        this.snsReader = snsReader;
        this.mCloudContract = repository;
        this.disposables = new CompositeDisposable();
        this.uploadDisposables = new CompositeDisposable();
        this.jsonArrayList = new ArrayList<>();
    }

    /**
     * Sets listener.
     */
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
                        view.showEmpty();
                    } else {
                        startUploading(f);
                    }
                });

        disposables.add(d);
    }

    private void startUploading(final List<SnsInfo> f) throws Exception {
        Disposable d = Observable.fromCallable(() -> {
            JSONArray array = snsReader.convertToJSON(f);
            jsonArrayList = subpack(array);
            Log.d(TAG, "JSONARRAY_LIST SIZE: " + jsonArrayList.size());
            upload1By1();
            return true;
        })
                .doOnSubscribe(disposable -> {
                    jsonArrayList.clear();
                    currentPart = 0;
                })
                .doOnTerminate(() -> {
                    uploadDisposables.dispose();
                    view.uploadCompleted();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> {

                });

        disposables.add(d);
    }

    private void upload1By1() {

        if (currentPart == jsonArrayList.size()) {
            return;
        }

        mCloudContract.upload(jsonArrayList.get(currentPart).toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> c, Response<ResponseBody> r) {
                        if (r.isSuccessful()) {
                            ++currentPart;
                            float progress = (int) (100 * (currentPart * 1.f / jsonArrayList.size()));
                            view.updateProgress(progress);
                            upload1By1();
                        } else {
                            view.uploadFailed(buildErrorMsg(r));
                            c.cancel();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> c, Throwable t) {
                        view.uploadFailed(t);
                        c.cancel();
                    }
                });

    }


    private static final int PER_SIZE = 200;
    private int currentPart = 0;


    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    private BaseException buildErrorMsg(final Response<ResponseBody> response) {
        String msg = "Oh,no!";
        if (response.message() != null) {
            msg = response.message();
        } else if (response.errorBody() != null) {
            try {
                msg = response.errorBody().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new BaseException(response.code(), msg);
    }


    private List<JSONArray> subpack(final JSONArray jsonArray) throws Exception {
        if (jsonArray.length() < PER_SIZE) {
            jsonArrayList.add(jsonArray);
            return jsonArrayList;
        }

        for (int i = 0; i < jsonArray.length() / PER_SIZE + 1; i++) {
            JSONArray spilt = new JSONArray();
            for (int j = 0; j < PER_SIZE; j++) {
                if (jsonArray.length() <= PER_SIZE * i + j)
                    break;
                spilt.put(jsonArray.get(PER_SIZE * i + j));
            }

            jsonArrayList.add(spilt);
        }

        return jsonArrayList;

    }
}
