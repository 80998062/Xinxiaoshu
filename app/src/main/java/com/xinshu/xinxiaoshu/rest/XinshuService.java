package com.xinshu.xinxiaoshu.rest;

import com.xinshu.xinxiaoshu.rest.entity.CaptchaResponse;
import com.xinshu.xinxiaoshu.rest.entity.Registeration;

import io.reactivex.Observable;
import retrofit2.Response;
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
    Observable<Response<CaptchaResponse>> captcha(@Path("phone") String phone);

}
