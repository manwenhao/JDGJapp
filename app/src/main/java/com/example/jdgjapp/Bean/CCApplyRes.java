package com.example.jdgjapp.Bean;

import java.io.Serializable;

/**
 * Created by xuxuxiao on 2018/2/6.
 */

public class CCApplyRes implements Serializable {
    private String id;
    private String man_id;
    private String datime;
    private String startdate;
    private String enddate;
    private String reason;
    private String answer;
    private String ansreason;
    private String type;

    public CCApplyRes() {
    }

    public CCApplyRes(String id, String man_id, String datime, String startdate, String enddate, String reason, String answer, String ansreason, String type) {
        this.id = id;
        this.man_id = man_id;
        this.datime = datime;
        this.startdate = startdate;
        this.enddate = enddate;
        this.reason = reason;
        this.answer = answer;
        this.ansreason = ansreason;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMan_id() {
        return man_id;
    }

    public void setMan_id(String man_id) {
        this.man_id = man_id;
    }

    public String getDatime() {
        return datime;
    }

    public void setDatime(String datime) {
        this.datime = datime;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
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

    public String getAnsreason() {
        return ansreason;
    }

    public void setAnsreason(String ansreason) {
        this.ansreason = ansreason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CCApplyRes{" +
                "id='" + id + '\'' +
                ", man_id='" + man_id + '\'' +
                ", datime='" + datime + '\'' +
                ", startdate='" + startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", reason='" + reason + '\'' +
                ", answer='" + answer + '\'' +
                ", ansreason='" + ansreason + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
