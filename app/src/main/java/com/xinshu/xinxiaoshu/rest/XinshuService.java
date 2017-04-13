package com.xinshu.xinxiaoshu.rest;

import com.xinshu.xinxiaoshu.rest.entity.LoginResponse;
import com.xinshu.xinxiaoshu.rest.entity.OrderEntity;
import com.xinshu.xinxiaoshu.rest.entity.Registeration;
import com.xinshu.xinxiaoshu.rest.entity.UserEntity;

import java.util.List;
import java.util.SortedMap;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by sinyuk on 2017/3/28.
 */
public interface XinshuService {

    /**
     * Check registration observable.
     *
     * @param phone the phone
     * @return the observable
     */
    @GET("xiaobians/human/phones/{phone}/registration_check")
    Observable<Response<Registeration>> checkRegistration(@Path("phone") String phone);

    /**
     * Captcha observable.
     *
     * @param phone the phone
     * @return the observable
     */
    @POST("xiaobians/human/phones/{phone}/captcha")
    Observable<Response<BaseResponse>> captcha(@Path("phone") String phone);

    /**
     * Login observable.
     *
     * @param params the params
     * @return the observable
     */
    @POST("xiaobians/human/login")
    Observable<Response<LoginResponse>> login(
            @Body SortedMap<String, String> params);

    /**
     * User info observable.
     *
     * @return the observable
     */
    @GET("xiaobians/human/info")
    Observable<Response<BaseResponse<UserEntity>>> user_info();

    /**
     * Typeset requests observable.
     *
     * @return the observable
     */
    @GET("xiaobians/human/typeset_requests")
    Observable<Response<BaseResponse<List<OrderEntity>>>> typeset_requests();


    /**
     * Online observable.
     *
     * @return the observable
     */
    @POST("xiaobians/human/online")
    Observable<Response<BaseResponse>> online();

    /**
     * Assignments observable.
     *
     * @return the observable
     */
    @POST("xiaobians/human/assignments")
    Observable<Response<BaseResponse<OrderEntity>>> assignments();
}
