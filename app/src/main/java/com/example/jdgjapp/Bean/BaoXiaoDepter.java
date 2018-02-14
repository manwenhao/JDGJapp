package com.example.jdgjapp.Bean;

/**
 * Created by xuxuxiao on 2018/2/11.
 */

public class BaoXiaoDepter {
    private String acc_id;
    private String acc_kind;
    private String acc_cont;
    private String acc_date;
    private String acc_money;
    private String acc_img;
    private String acc_answer;
    private String acc_answerreason;
    private String acc_backmoney;
    private String user_id;
    private String user_name;
    private String dep_id;
    private String dep_name;

    public BaoXiaoDepter() {
    }

    public BaoXiaoDepter(String acc_id, String acc_kind, String acc_cont, String acc_date, String acc_money, String acc_img, String acc_answer, String acc_answerreason, String acc_backmoney, String user_id, String user_name, String dep_id, String dep_name) {
        this.acc_id = acc_id;
        this.acc_kind = acc_kind;
        this.acc_cont = acc_cont;
        this.acc_date = acc_date;
        this.acc_money = acc_money;
        this.acc_img = acc_img;
        this.acc_answer = acc_answer;
        this.acc_answerreason = acc_answerreason;
        this.acc_backmoney = acc_backmoney;
        this.user_id = user_id;
        this.user_name = user_name;
        this.dep_id = dep_id;
        this.dep_name = dep_name;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDep_id() {
        return dep_id;
    }

    public void setDep_id(String dep_id) {
        this.dep_id = dep_id;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    @Override
    public String toString() {
        return "BaoXiaoDepter{" +
                "acc_id='" + acc_id + '\'' +
                ", acc_kind='" + acc_kind + '\'' +
                ", acc_cont='" + acc_cont + '\'' +
                ", acc_date='" + acc_date + '\'' +
                ", acc_money='" + acc_money + '\'' +
                ", acc_img='" + acc_img + '\'' +
                ", acc_answer='" + acc_answer + '\'' +
                ", acc_answerreason='" + acc_answerreason + '\'' +
                ", acc_backmoney='" + acc_backmoney + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", dep_id='" + dep_id + '\'' +
                ", dep_name='" + dep_name + '\'' +
                '}';
    }
}
