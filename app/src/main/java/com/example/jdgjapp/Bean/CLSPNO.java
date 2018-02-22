package com.example.jdgjapp.Bean;

import java.io.Serializable;

/**
 * Created by xuxuxiao on 2018/2/17.
 */

public class CLSPNO implements Serializable {
    private String matid;
    private String matname;
    private String matnum;
    private String username;
    private String userid;
    private String datetime;
    private String reason;
    private String sign;

    public CLSPNO() {
    }

    public CLSPNO(String matid, String matname, String matnum, String username, String userid, String datetime, String reason, String sign) {
        this.matid = matid;
        this.matname = matname;
        this.matnum = matnum;
        this.username = username;
        this.userid = userid;
        this.datetime = datetime;
        this.reason = reason;
        this.sign = sign;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "CLSPNO{" +
                "matid='" + matid + '\'' +
                ", matname='" + matname + '\'' +
                ", matnum='" + matnum + '\'' +
                ", username='" + username + '\'' +
                ", userid='" + userid + '\'' +
                ", datetime='" + datetime + '\'' +
                ", reason='" + reason + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
