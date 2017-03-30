package com.xinshu.xinxiaoshu.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sinyuk on 2017/3/28.
 */

public abstract class BaseResponse {

    /**
     * errcode : 1
     * errmsg : 信息
     */
    @SerializedName("errcode")
    public int code;
    @SerializedName("errmsg")
    public String msg;

    public boolean succeed() {
        return code == 0;
    }

}
