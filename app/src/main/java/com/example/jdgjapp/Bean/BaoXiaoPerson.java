package com.example.jdgjapp.Bean;

import java.io.Serializable;

/**
 * Created by xuxuxiao on 2018/2/11.
 */

public class BaoXiaoPerson implements Serializable{
    private String acc_id;
    private String acc_kind;
    private String acc_cont;
    private String acc_date;
    private String acc_money;
    private String acc_img;
    private String acc_answer;
    private String acc_answerreason;
    private String acc_backmoney;

    public BaoXiaoPerson() {
    }
    public BaoXiaoPerson(BaoXiaoDepter e){
        this.acc_id=e.getAcc_id();
        this.acc_kind=e.getAcc_kind();
        this.acc_cont=e.getAcc_cont();
        this.acc_date=e.getAcc_date();
        this.acc_money=e.getAcc_money();
        this.acc_img=e.getAcc_img();
        this.acc_answer=e.getAcc_answer();
        this.acc_answerreason=e.getAcc_answerreason();
        this.acc_backmoney=e.getAcc_backmoney();
    }

    public BaoXiaoPerson(String acc_id, String acc_kind, String acc_cont, String acc_date, String acc_money, String acc_img, String acc_answer, String acc_answerreason, String acc_backmoney) {
        this.acc_id = acc_id;
        this.acc_kind = acc_kind;
        this.acc_cont = acc_cont;
        this.acc_date = acc_date;
        this.acc_money = acc_money;
        this.acc_img = acc_img;
        this.acc_answer = acc_answer;
        this.acc_answerreason = acc_answerreason;
        this.acc_backmoney = acc_backmoney;
    }

    public String getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(String acc_id) {
        this.acc_id = acc_id;
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

    public String getAcc_answer() {
        return acc_answer;
    }

    public void setAcc_answer(String acc_answer) {
        this.acc_answer = acc_answer;
    }

    public String getAcc_answerreason() {
        return acc_answerreason;
    }

    public void setAcc_answerreason(String acc_answerreason) {
        this.acc_answerreason = acc_answerreason;
    }

    public String getAcc_backmoney() {
        return acc_backmoney;
    }

    public void setAcc_backmoney(String acc_backmoney) {
        this.acc_backmoney = acc_backmoney;
    }

    @Override
    public String toString() {
        return "BaoXiaoPerson{" +
                "acc_id='" + acc_id + '\'' +
                ", acc_kind='" + acc_kind + '\'' +
                ", acc_cont='" + acc_cont + '\'' +
                ", acc_date='" + acc_date + '\'' +
                ", acc_money='" + acc_money + '\'' +
                ", acc_img='" + acc_img + '\'' +
                ", acc_answer='" + acc_answer + '\'' +
                ", acc_answerreason='" + acc_answerreason + '\'' +
                ", acc_backmoney='" + acc_backmoney + '\'' +
                '}';
    }
}
