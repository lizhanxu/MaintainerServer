package com.example.a93403.maintainerservice.bean;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private Date time;
    private String car_type;
    private int distance;
    private String describe;

    public Order(Date time, String car_type, int distance, String describe)  {
        this.time = time;
        this.car_type = car_type;
        this.distance = distance;
        this.describe = describe;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
