package com.example.jdgjapp.Bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by mwh on 2018/2/13.
 */

public class BaiduMakerInfo extends DataSupport implements Serializable{
    private String user_id;
    private String user_name;
    private String posx;
    private String posy;
    private String datime;
    private String dep_id;
    private String dep_name;


    public String getUser_id(){
        return user_id;
    }

    public void setUser_id(String user_id){
        this.user_id = user_id;
    }

    public String getUser_name(){
        return user_name;
    }

    public void setUser_name(String user_name){
        this.user_name = user_name;
    }

    public String getPosx(){
        return posx;
    }

    public void setPosx(String posx){
        this.posx = posx;
    }

    public String getPosy(){
        return posy;
    }

    public void setPosy(String posy){
        this.posy = posy;
    }

    public String getDatime(){
        return datime;
    }

    public void setDatime(String datime){
        this.datime = datime;
    }

    public String getDep_id(){
        return dep_id;
    }

    public void setDep_id(String dep_id){
        this.dep_id = dep_id;
    }

    public String getDep_name(){
        return dep_name;
    }

    public void setDep_name(String dep_name){
        this.dep_name = dep_name;
    }
}
