package com.xinshu.xinxiaoshu.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sinyuk on 2017/3/28.
 */

public class BaseResponse<T> {

    /**
     * errcode : 1
     * errmsg : 信息
     */
    @SerializedName("errcode")
    public int code;
    @SerializedName("errmsg")
    public String msg;

    @SerializedName("data")
    public T data;



    public boolean succeed() {
        return code == 0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
