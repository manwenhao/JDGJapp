package com.example.jdgjapp.Bean;

/**
 * Created by xuxuxiao on 2018/2/22.
 */

public class SimpleCl {
    private String mat_name;
    private String mat_num;

    public SimpleCl() {
    }

    public SimpleCl(String mat_name, String mat_num) {
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
        return "SimpleCl{" +
                "mat_name='" + mat_name + '\'' +
                ", mat_num='" + mat_num + '\'' +
                '}';
    }
}
