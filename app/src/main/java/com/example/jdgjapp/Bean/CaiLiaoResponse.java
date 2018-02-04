package com.example.jdgjapp.Bean;

import java.io.Serializable;

/**
 * Created by xuxuxiao on 2018/2/3.
 */

public class CaiLiaoResponse implements Serializable{
    private String mat_id;
    private String mat_name;
    private String mat_num;
    private String datetime;
    private String user_id;
    private String user_name;
    private String reason;
    private String rmat_status;
    private String resp;
    private String sign;
    private String leadstatus;

    public CaiLiaoResponse() {
    }

    public CaiLiaoResponse(String mat_id, String mat_name, String mat_num, String datetime, String user_id, String user_name, String reason, String rmat_status, String resp, String sign, String leadstatus) {
        this.mat_id = mat_id;
        this.mat_name = mat_name;
        this.mat_num = mat_num;
        this.datetime = datetime;
        this.user_id = user_id;
        this.user_name = user_name;
        this.reason = reason;
        this.rmat_status = rmat_status;
        this.resp = resp;
        this.sign = sign;
        this.leadstatus = leadstatus;
    }

    public String getMat_id() {
        return mat_id;
    }

    public void setMat_id(String mat_id) {
        this.mat_id = mat_id;
    }

    public String getMat_name() {
        return mat_name;
    }

    public void setMat_name(String mat_name) {
        this.mat_name = mat_name;
    }

    public String getMat_num() {
        return mat_num;
    }

    public void setMat_num(String mat_num) {
        this.mat_num = mat_num;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRmat_status() {
        return rmat_status;
    }

    public void setRmat_status(String rmat_status) {
        this.rmat_status = rmat_status;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLeadstatus() {
        return leadstatus;
    }

    public void setLeadstatus(String leadstatus) {
        this.leadstatus = leadstatus;
    }

    @Override
    public String toString() {
        return "CaiLiaoResponse{" +
                "mat_id='" + mat_id + '\'' +
                ", mat_name='" + mat_name + '\'' +
                ", mat_num='" + mat_num + '\'' +
                ", datetime='" + datetime + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", reason='" + reason + '\'' +
                ", rmat_status='" + rmat_status + '\'' +
                ", resp='" + resp + '\'' +
                ", sign='" + sign + '\'' +
                ", leadstatus='" + leadstatus + '\'' +
                '}';
    }
}
