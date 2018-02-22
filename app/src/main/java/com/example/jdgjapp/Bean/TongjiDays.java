package com.example.jdgjapp.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by mwh on 2018/2/20.
 */

public class TongjiDays extends DataSupport{
    private String cq;  //出勤天数
    private String cc;  //出差天数
    private String qj;  //请假天数
    private String cd;  //迟到天数
    private String zt;  //早退天数

    public String getCq(){
        return cq;
    }

    public void setCq(String cq){
        this.cq = cq;
    }

    public String getCc(){
        return cc;
    }

    public void setCc(String cc){
        this.cc = cc;
    }

    public String getQj(){
        return qj;
    }

    public void setQj(String qj){
        this.qj = qj;
    }

    public String getCd(){
        return cd;
    }

    public void setCd(String cd){
        this.cd = cd;
    }

    public String getZt(){
        return zt;
    }

    public void setZt(String zt){
        this.zt = zt;
    }
}
