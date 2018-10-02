package com.example.a93403.maintainerservice.bean;

import com.example.a93403.maintainerservice.bean.json.OrderJson;
import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

public class CurrentOrder extends DataSupport implements Serializable {

    @SerializedName("order_no")
    private String order_id;

    @SerializedName("create_time")
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

    private Double longitude;

    private Double latitude;

    public CurrentOrder() {
    }

    public CurrentOrder(OrderJson orderJson) {

        if (null != orderJson) {
            this.order_id = orderJson.getOrderNo();
            this.publish_time = orderJson.getCreateTime();
            this.nickname = orderJson.getCustomer().getCustName();
            this.phone = orderJson.getCustomer().getCustPhone();
            this.car_brand = orderJson.getCustomer().getCarBrand();
            this.car_type = orderJson.getCustomer().getCarId();

            if (orderJson.getFaultCodeList() != null && !orderJson.getFaultCodeList().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (FaultCode fc : orderJson.getFaultCodeList()) {
                    sb.append(fc.getCode()).append("; ");
                }
                this.fault_code = sb.toString();
//                sb = new StringBuilder();
                sb.delete(0, sb.length());
                for (FaultCode fc : orderJson.getFaultCodeList()) {
                    sb.append(fc.getDescribe()).append("; ");
                }
                this.describe = sb.toString();
            }

            this.longitude = orderJson.getLongitude();
            this.latitude = orderJson.getLatitude();
        }
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Date getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(Date publish_time) {
        this.publish_time = publish_time;
    }

    public Date getAck_time() {
        return ack_time;
    }

    public void setAck_time(Date ack_time) {
        this.ack_time = ack_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCar_brand() {
        return car_brand;
    }

    public void setCar_brand(String car_brand) {
        this.car_brand = car_brand;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getFault_code() {
        return fault_code;
    }

    public void setFault_code(String fault_code) {
        this.fault_code = fault_code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
