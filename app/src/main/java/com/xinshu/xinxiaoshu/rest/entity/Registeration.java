package com.xinshu.xinxiaoshu.rest.entity;

import com.google.gson.annotations.SerializedName;
import com.xinshu.xinxiaoshu.rest.BaseResponse;

/**
 * Created by sinyuk on 2017/3/28.
 */

public class Registeration extends BaseResponse{

    /**
     * phone : 13486117912
     * registered : true
     */

    @SerializedName("phone")
    public String phone;
    @SerializedName("registered")
    public boolean registered;
}
