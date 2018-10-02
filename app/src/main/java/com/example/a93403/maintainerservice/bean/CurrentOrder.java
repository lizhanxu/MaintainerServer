package com.example.a93403.maintainerservice.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

public class CurrentOrder extends DataSupport implements Serializable {

    @SerializedName("order_no")
    private String order_id;

    private Date publish_time;
    private Date ack_time;

    @SerializedName("finish_time")
    private Date end_time;

    @SerializedName("cust_name")
    private String nickname;

    @SerializedName("cust_phone")
    private String phone;

    private String car_brand;

    @SerializedName("car_id")
    private String car_type;

    private String fault_code;

    @SerializedName("fault_describe")
    private String describe;

    public String getOrder_id() {
        return order_id;
    }

    public Date getPublish_time() {
        return publish_time;
    }

    public Date getAck_time() {
        return ack_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhone() {
        return phone;
    }

    public String getCar_brand() {
        return car_brand;
    }

    public String getCar_type() {
        return car_type;
    }

    public String getFault_code() {
        return fault_code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setPublish_time(Date publish_time) {
        this.publish_time = publish_time;
    }

    public void setAck_time(Date ack_time) {
        this.ack_time = ack_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCar_brand(String car_brand) {
        this.car_brand = car_brand;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public void setFault_code(String fault_code) {
        this.fault_code = fault_code;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
