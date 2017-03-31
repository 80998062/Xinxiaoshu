package com.xinshu.xinxiaoshu.rest.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sinyuk on 2017/3/30.
 */

public class UserEntity {

    @SerializedName("province")
    public String province;
    @SerializedName("openid")
    public String openid;
    @SerializedName("headimgurl")
    public String headimgurl;
    @SerializedName("name")
    public String name;
    @SerializedName("city")
    public String city;
    @SerializedName("grade")
    public int grade;
    @SerializedName("weixin_hao")
    public String weixinHao;
    @SerializedName("approved")
    public boolean approved;
    @SerializedName("follow_count")
    public int followCount;
    @SerializedName("status")
    public int status;
    @SerializedName("sex")
    public String sex;
    @SerializedName("phone")
    public String phone;
    @SerializedName("create_time")
    public String createTime;
    @SerializedName("hid")
    public String hid;
    @SerializedName("approve_time")
    public String approveTime;
    @SerializedName("qrcode")
    public String qrcode;
    @SerializedName("order_count")
    public int orderCount;
    @SerializedName("id")
    public int id;
    @SerializedName("order_money")
    public int orderMoney;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getWeixinHao() {
        return weixinHao;
    }

    public void setWeixinHao(String weixinHao) {
        this.weixinHao = weixinHao;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(String approveTime) {
        this.approveTime = approveTime;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(int orderMoney) {
        this.orderMoney = orderMoney;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "province='" + province + '\'' +
                ", openid='" + openid + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", grade=" + grade +
                ", weixinHao='" + weixinHao + '\'' +
                ", approved=" + approved +
                ", followCount=" + followCount +
                ", status=" + status +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", createTime='" + createTime + '\'' +
                ", hid='" + hid + '\'' +
                ", approveTime='" + approveTime + '\'' +
                ", qrcode='" + qrcode + '\'' +
                ", orderCount=" + orderCount +
                ", id=" + id +
                ", orderMoney=" + orderMoney +
                '}';
    }
}
