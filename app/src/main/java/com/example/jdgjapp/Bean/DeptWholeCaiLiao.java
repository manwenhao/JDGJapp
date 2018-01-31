package com.example.jdgjapp.Bean;

/**
 * Created by xuxuxiao on 2018/1/31.
 */

public class DeptWholeCaiLiao {
    private String use_name;
    private String use_num;
    private String use_left;

    public DeptWholeCaiLiao() {
    }

    public DeptWholeCaiLiao(String use_name, String use_num, String use_left) {
        this.use_name = use_name;
        this.use_num = use_num;
        this.use_left = use_left;
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

    public String getUse_left() {
        return use_left;
    }

    public void setUse_left(String use_left) {
        this.use_left = use_left;
    }

    @Override
    public String toString() {
        return "DeptWholeCaiLiao{" +
                "use_name='" + use_name + '\'' +
                ", use_num='" + use_num + '\'' +
                ", use_left='" + use_left + '\'' +
                '}';
    }
}
