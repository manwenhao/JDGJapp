package com.example.jdgjapp.Bean;

/**
 * Created by xuxuxiao on 2018/1/31.
 */

public class PersonCaiLiao {
    private String mat_name;
    private String mat_num;

    public PersonCaiLiao() {
    }

    public PersonCaiLiao(String mat_name, String mat_num) {
        this.mat_name = mat_name;
        this.mat_num = mat_num;
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

    @Override
    public String toString() {
        return "PersonCaiLiao{" +
                "mat_name='" + mat_name + '\'' +
                ", mat_num='" + mat_num + '\'' +
                '}';
    }
}
