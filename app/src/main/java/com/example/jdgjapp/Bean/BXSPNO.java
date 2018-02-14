package com.example.jdgjapp.Bean;

import java.io.Serializable;

/**
 * Created by xuxuxiao on 2018/2/13.
 */

public class BXSPNO implements Serializable {
    private String acc_id;
    private String acc_userid;
    private String acc_username;
    private String acc_kind;
    private String acc_cont;
    private String acc_date;
    private String acc_money;
    private String acc_img;

    public BXSPNO() {
    }

    public BXSPNO(String acc_id, String acc_userid, String acc_username, String acc_kind, String acc_cont, String acc_date, String acc_money, String acc_img) {
        this.acc_id = acc_id;
        this.acc_userid = acc_userid;
        this.acc_username = acc_username;
        this.acc_kind = acc_kind;
        this.acc_cont = acc_cont;
        this.acc_date = acc_date;
        this.acc_money = acc_money;
        this.acc_img = acc_img;
    }

    public String getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(String acc_id) {
        this.acc_id = acc_id;
    }

    public String getAcc_userid() {
        return acc_userid;
    }

    public void setAcc_userid(String acc_userid) {
        this.acc_userid = acc_userid;
    }

    public String getAcc_username() {
        return acc_username;
    }

    public void setAcc_username(String acc_username) {
        this.acc_username = acc_username;
    }

    public String getAcc_kind() {
        return acc_kind;
    }

    public void setAcc_kind(String acc_kind) {
        this.acc_kind = acc_kind;
    }

    public String getAcc_cont() {
        return acc_cont;
    }

    public void setAcc_cont(String acc_cont) {
        this.acc_cont = acc_cont;
    }

    public String getAcc_date() {
        return acc_date;
    }

    public void setAcc_date(String acc_date) {
        this.acc_date = acc_date;
    }

    public String getAcc_money() {
        return acc_money;
    }

    public void setAcc_money(String acc_money) {
        this.acc_money = acc_money;
    }

    public String getAcc_img() {
        return acc_img;
    }

    public void setAcc_img(String acc_img) {
        this.acc_img = acc_img;
    }

    @Override
    public String toString() {
        return "BXSPNO{" +
                "acc_id='" + acc_id + '\'' +
                ", acc_userid='" + acc_userid + '\'' +
                ", acc_username='" + acc_username + '\'' +
                ", acc_kind='" + acc_kind + '\'' +
                ", acc_cont='" + acc_cont + '\'' +
                ", acc_date='" + acc_date + '\'' +
                ", acc_money='" + acc_money + '\'' +
                ", acc_img='" + acc_img + '\'' +
                '}';
    }
}
