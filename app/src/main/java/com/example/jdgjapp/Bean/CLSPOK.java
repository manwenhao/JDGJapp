package com.example.jdgjapp.Bean;

import java.io.Serializable;

/**
 * Created by xuxuxiao on 2018/2/17.
 */

public class CLSPOK implements Serializable {
    private String matid;
    private String matname;
    private String matnum;
    private String username;
    private String userid;
    private String datetime;
    private String reason;
    private String answer;
    private String answercont;
    private String leadststus;
    private String rmat_sign;

    public CLSPOK() {
    }

    public CLSPOK(String matid, String matname, String matnum, String username, String userid, String datetime, String reason, String answer, String answercont, String leadststus, String rmat_sign) {
        this.matid = matid;
        this.matname = matname;
        this.matnum = matnum;
        this.username = username;
        this.userid = userid;
        this.datetime = datetime;
        this.reason = reason;
        this.answer = answer;
        this.answercont = answercont;
        this.leadststus = leadststus;
        this.rmat_sign = rmat_sign;
    }

    public String getMatid() {
        return matid;
    }

    public void setMatid(String matid) {
        this.matid = matid;
    }

    public String getMatname() {
        return matname;
    }

    public void setMatname(String matname) {
        this.matname = matname;
    }

    public String getMatnum() {
        return matnum;
    }

    public void setMatnum(String matnum) {
        this.matnum = matnum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswercont() {
        return answercont;
    }

    public void setAnswercont(String answercont) {
        this.answercont = answercont;
    }

    public String getLeadststus() {
        return leadststus;
    }

    public void setLeadststus(String leadststus) {
        this.leadststus = leadststus;
    }

    public String getRmat_sign() {
        return rmat_sign;
    }

    public void setRmat_sign(String rmat_sign) {
        this.rmat_sign = rmat_sign;
    }

    @Override
    public String toString() {
        return "CLSPOK{" +
                "matid='" + matid + '\'' +
                ", matname='" + matname + '\'' +
                ", matnum='" + matnum + '\'' +
                ", username='" + username + '\'' +
                ", userid='" + userid + '\'' +
                ", datetime='" + datetime + '\'' +
                ", reason='" + reason + '\'' +
                ", answer='" + answer + '\'' +
                ", answercont='" + answercont + '\'' +
                ", leadststus='" + leadststus + '\'' +
                ", rmat_sign='" + rmat_sign + '\'' +
                '}';
    }
}
