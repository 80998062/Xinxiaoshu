package com.xinshu.xinxiaoshu.rest.entity;

import com.google.gson.annotations.SerializedName;
import com.xinshu.xinxiaoshu.rest.BaseResponse;

/**
 * Created by sinyuk on 2017/3/30.
 */

public class LoginResponse extends BaseResponse<UserEntity> {
    @SerializedName("auth_token")
    public String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
