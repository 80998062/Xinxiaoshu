package com.xinshu.xinxiaoshu.rest.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sinyuk on 2017/3/30.
 */
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -9181072754961674630L;
    /**
     * The Province.
     */
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
    public float orderMoney;

    /**
     * Gets province.
     *
     * @return the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * Sets province.
     *
     * @param province the province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Gets openid.
     *
     * @return the openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * Sets openid.
     *
     * @param openid the openid
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * Gets headimgurl.
     *
     * @return the headimgurl
     */
    public String getHeadimgurl() {
        return headimgurl;
    }

    /**
     * Sets headimgurl.
     *
     * @param headimgurl the headimgurl
     */
    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets city.
     *
     * @param city the city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets grade.
     *
     * @return the grade
     */
    public int getGrade() {
        return grade;
    }

    /**
     * Sets grade.
     *
     * @param grade the grade
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * Gets weixin hao.
     *
     * @return the weixin hao
     */
    public String getWeixinHao() {
        return weixinHao;
    }

    /**
     * Sets weixin hao.
     *
     * @param weixinHao the weixin hao
     */
    public void setWeixinHao(String weixinHao) {
        this.weixinHao = weixinHao;
    }

    /**
     * Is approved boolean.
     *
     * @return the boolean
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * Sets approved.
     *
     * @param approved the approved
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * Gets follow count.
     *
     * @return the follow count
     */
    public int getFollowCount() {
        return followCount;
    }

    /**
     * Sets follow count.
     *
     * @param followCount the follow count
     */
    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets sex.
     *
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets sex.
     *
     * @param sex the sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets create time.
     *
     * @return the create time
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * Sets create time.
     *
     * @param createTime the create time
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets hid.
     *
     * @return the hid
     */
    public String getHid() {
        return hid;
    }

    /**
     * Sets hid.
     *
     * @param hid the hid
     */
    public void setHid(String hid) {
        this.hid = hid;
    }

    /**
     * Gets approve time.
     *
     * @return the approve time
     */
    public String getApproveTime() {
        return approveTime;
    }

    /**
     * Sets approve time.
     *
     * @param approveTime the approve time
     */
    public void setApproveTime(String approveTime) {
        this.approveTime = approveTime;
    }

    /**
     * Gets qrcode.
     *
     * @return the qrcode
     */
    public String getQrcode() {
        return qrcode;
    }

    /**
     * Sets qrcode.
     *
     * @param qrcode the qrcode
     */
    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    /**
     * Gets order count.
     *
     * @return the order count
     */
    public int getOrderCount() {
        return orderCount;
    }

    /**
     * Sets order count.
     *
     * @param orderCount the order count
     */
    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets order money.
     *
     * @return the order money
     */
    public float getOrderMoney() {
        return orderMoney;
    }

    /**
     * Sets order money.
     *
     * @param orderMoney the order money
     */
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
