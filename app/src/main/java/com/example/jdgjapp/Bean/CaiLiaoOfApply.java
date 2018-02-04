package com.example.jdgjapp.Bean;

/**
 * Created by xuxuxiao on 2018/2/1.
 */

public class CaiLiaoOfApply {
    private String mat_id;
    private String mat_name;
    private String mat_num;

    public CaiLiaoOfApply() {
    }

    public CaiLiaoOfApply(String mat_id, String mat_name, String mat_num) {
        this.mat_id = mat_id;
        this.mat_name = mat_name;
        this.mat_num = mat_num;
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

    @Override
    public String toString() {
        return "CaiLiaoOfApply{" +
                "mat_id='" + mat_id + '\'' +
                ", mat_name='" + mat_name + '\'' +
                ", mat_num='" + mat_num + '\'' +
                '}';
    }
}
