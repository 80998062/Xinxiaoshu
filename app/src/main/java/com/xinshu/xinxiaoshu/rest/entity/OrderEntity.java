package com.xinshu.xinxiaoshu.rest.entity;

import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

    public String getNickname() {
        try {
            return "https://media.xinshu.me/fetch?url=" + URLEncoder.encode(nickname, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return nickname;
        }
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    @Override
    public String toString() {
        return nickname;
    }
}
