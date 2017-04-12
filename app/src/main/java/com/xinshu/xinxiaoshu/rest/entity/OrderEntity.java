package com.xinshu.xinxiaoshu.rest.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sinyuk on 2017/3/31.
 */

public class OrderEntity {

    /**
     * nickname : 趴趴熊
     * headimgurl : http://wx.qlogo.cn/mmopen/I1aEWaTzLU6iaBmicGB0ic2Iibzj3UuSBEOAMF42FHTSwPOdcmtBPyKsP891vAUu3nVCHgeSxweTr3UHBarNgicNWcgZQxNtpz7fs/0
     */

    @SerializedName("nickname")
    public String nickname;
    @SerializedName("headimgurl")
    public String headimgurl;

    @Override
    public String toString() {
        return nickname;
    }
}
