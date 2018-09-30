package com.example.a93403.maintainerservice.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Title: CarService
 * Date: Create in 2018/4/28 21:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class User extends DataSupport implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("portrait")
    private String portrait;

    public User() {
    }

    public User(int id, String phone, String email, String nickname, String portrait1) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.nickname = nickname;
        this.portrait = portrait1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", portrait='" + portrait + '\'' +
                '}';
    }
}
