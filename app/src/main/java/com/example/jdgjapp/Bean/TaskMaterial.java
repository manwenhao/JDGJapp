package com.example.jdgjapp.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by mwh on 2018/2/4.
 */

public class TaskMaterial extends DataSupport {
    private String mat_id;
    private String mat_name;
    private String mat_num;

    public String getMat_id(){
        return mat_id;
    }

    public void setMat_id(String mat_id){
        this.mat_id = mat_id;
    }

    public String getMat_name(){
        return mat_name;
    }

    public void setMat_name(String mat_name){
        this.mat_name = mat_name;
    }

    public String getMat_num(){
        return mat_num;
    }

    public void setMat_num(String mat_num){
        this.mat_num = mat_num;
    }
}
