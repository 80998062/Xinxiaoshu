package com.xinshu.xinxiaoshu.rest;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by sinyuk on 2017/4/13.
 */
public interface UploadService {
    /**
     * Upload call.
     *
     * @param data the data
     * @return the call
     */
//    @Headers({
//            "Accept: application/json",
//            "charset: utf-8"
//    })
    @POST("upload/phone_items")
    Call<ResponseBody> upload(@Body RequestBody data);
}
