package com.xinshu.xinxiaoshu.rest.contract;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by sinyuk on 2017/4/12.
 */
public interface CloudContract {
    /**
     * Upload observable.
     *
     * @param body the body
     * @return
     */
    Call<ResponseBody> upload(String body);


    String getHid();
}
