package com.example.jdgjapp.Bean;

/**
 * Created by xuxuxiao on 2018/1/31.
 */

public class PersonCaiLiaoDetail {
    private String use_name;
    private String use_num;
    private String use_cont;
    private String use_time;
    private String use_kind;

    public PersonCaiLiaoDetail() {
    }

    public PersonCaiLiaoDetail(String use_name, String use_num, String use_cont, String use_time, String use_kind) {
        this.use_name = use_name;
        this.use_num = use_num;
        this.use_cont = use_cont;
        this.use_time = use_time;
        this.use_kind = use_kind;
    }

    public String getUse_name() {
        return use_name;
    }

    public void setUse_name(String use_name) {
        this.use_name = use_name;
    }

    public String getUse_num() {
        return use_num;
    }

    public void setUse_num(String use_num) {
        this.use_num = use_num;
    }

    public String getUse_cont() {
        return use_cont;
    }

    public void setUse_cont(String use_cont) {
        this.use_cont = use_cont;
    }

    public String getUse_time() {
        return use_time;
    }

    public void setUse_time(String use_time) {
        this.use_time = use_time;
    }

    public String getUse_kind() {
        return use_kind;
    }

    public void setUse_kind(String use_kind) {
        this.use_kind = use_kind;
    }

    @Override
    public String toString() {
        return "PersonCaiLiaoDetail{" +
                "use_name='" + use_name + '\'' +
                ", use_num='" + use_num + '\'' +
                ", use_cont='" + use_cont + '\'' +
                ", use_time='" + use_time + '\'' +
                ", use_kind='" + use_kind + '\'' +
                '}';
    }
}
