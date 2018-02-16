package com.example.jdgjapp.Bean;

import java.io.Serializable;

/**
 * Created by xuxuxiao on 2018/2/14.
 */

public class QJSPNO implements Serializable{
    private String id;
    private String man_id;
    private String usr_name;
    private String type;
    private String reason;
    private String datime;
    private String startdate;
    private String enddate;

    public QJSPNO() {
    }

    public QJSPNO(String id, String man_id, String usr_name, String type, String reason, String datime, String startdate, String enddate) {
        this.id = id;
        this.man_id = man_id;
        this.usr_name = usr_name;
        this.type = type;
        this.reason = reason;
        this.datime = datime;
        this.startdate = startdate;
        this.enddate = enddate;
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

    public String getUsr_name() {
        return usr_name;
    }

    public void setUsr_name(String usr_name) {
        this.usr_name = usr_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    @Override
    public String toString() {
        return "QJSPNO{" +
                "id='" + id + '\'' +
                ", man_id='" + man_id + '\'' +
                ", usr_name='" + usr_name + '\'' +
                ", type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", datime='" + datime + '\'' +
                ", startdate='" + startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                '}';
    }
}
