package com.xinshu.xinxiaoshu.rest;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;
import com.xinshu.xinxiaoshu.config.Prefs;
import com.xinshu.xinxiaoshu.rest.contract.RemoteDataStore;
import com.xinshu.xinxiaoshu.rest.entity.LoginResponse;
import com.xinshu.xinxiaoshu.rest.entity.OrderEntity;
import com.xinshu.xinxiaoshu.rest.entity.UserEntity;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by sinyuk on 2017/3/28.
 */
public class RemoteDataRepository implements RemoteDataStore {

    private final XinshuService xinshuService;
    private final UploadService uploadService;
    private final SchedulerTransformer schedulerTransformer;
    private final RxSharedPreferences preferences;
    private final Gson mGson;

    private final Preference<String> token;


    /**
     * Instantiates a new Remote data repository.
     *
     * @param gson                 the gson
     * @param xinshuService        the xinshu service
     * @param schedulerTransformer the scheduler transformer
     * @param preferences          the preferences
     */
    @Inject
    public RemoteDataRepository(
            final Gson gson,
            final XinshuService xinshuService,
            final UploadService uploadService,
            final SchedulerTransformer schedulerTransformer,
            final RxSharedPreferences preferences) {
        this.mGson = gson;
        this.xinshuService = xinshuService;
        this.uploadService = uploadService;
        this.schedulerTransformer = schedulerTransformer;
        this.preferences = preferences;

        token = preferences.getString(Prefs.ACCESS_TOKEN, "");
    }

    @Override
    public Observable<Boolean> checkRegisteration(@NonNull String phone) {
        return xinshuService.checkRegistration(phone)
                .compose(new DefaultTransformer<>(schedulerTransformer))
                .map(r -> r.registered);
    }


    @Override
    public Observable<Boolean> getCaptcha(@NonNull String phone) {
        return xinshuService.captcha(phone)
                .compose(new DefaultTransformer<>(schedulerTransformer))
                .map(BaseResponse::succeed);
    }

    @Override
    public Observable<UserEntity> login(@NonNull String phone, @NonNull String captcha) {
        SortedMap<String, String> params = new TreeMap<>();
        params.put("phone", phone);
        params.put("captcha", captcha);
        return xinshuService.login(params)
                .compose(new DefaultTransformer<>(schedulerTransformer))
                .doOnNext(r -> saveToken(r.getAuthToken()))
                .map(LoginResponse::getData);
    }

    private void saveToken(final String authToken) {
        System.out.println("Token: " + authToken);
        if (token.isSet()) {
            token.delete();
        }
        token.set(authToken);
    }

    /**
     * Gets token.
     *
     * @return the token
     */
    public Preference<String> getToken() {
        return token;
    }

    @Override
    public Observable<UserEntity> userInfo() {
        return xinshuService.user_info()
                .compose(new DefaultTransformer<>(schedulerTransformer))
                .flatMap(new ResponseFunc<>());
    }

    @Override
    public Observable<Boolean> online() {
        return xinshuService.online()
                .compose(new DefaultTransformer<>(schedulerTransformer))
                .map(BaseResponse::succeed);
    }

    @Override
    public Observable<List<OrderEntity>> requestOrder() {
        return xinshuService.typeset_requests()
                .compose(new DefaultTransformer<>(schedulerTransformer))
                .flatMap(new ResponseFunc<>());
    }


    @Override
    public Call<ResponseBody> upload(final String b) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                (b));

        return uploadService.upload(body);


    }
}
