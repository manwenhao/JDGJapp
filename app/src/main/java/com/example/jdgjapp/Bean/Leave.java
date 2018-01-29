package com.example.jdgjapp.Bean;


import org.litepal.crud.DataSupport;

public class Leave extends DataSupport {

    private String leaveid;
    private String userid;
    private String username;
    private String type;
    private String sendtime;
    private String startDate;
    private String endDate;
    private String reason;
    private String status;   //0审核中 1申请成功 2申请失败
    private String ansreason;

    public String getLeaveid() {
        return leaveid;
    }

    public void setLeaveid(String leaveid){
        this.leaveid = leaveid;
    }

    public String getUserid(){
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendtime(){
        return sendtime;
    }

    public void setSendtime(String sendtime){
        this.sendtime = sendtime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnsreason() {
        return ansreason;
    }

    public void setAnsreason(String ansreason) {
        this.ansreason = ansreason;
    }

}
