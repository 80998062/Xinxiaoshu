package com.xinshu.xinxiaoshu.rest.entity;

import com.google.gson.annotations.SerializedName;
import com.xinshu.xinxiaoshu.rest.BaseResponse;

/**
 * Created by sinyuk on 2017/3/30.
 */

public class LoginResponse extends BaseResponse{

    @SerializedName("auth_token")
    public String authToken;
    @SerializedName("data")
    public UserEntity user;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
