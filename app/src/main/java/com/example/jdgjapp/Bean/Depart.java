package com.example.jdgjapp.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by mwh on 2018/1/22.
 */

public class Depart extends DataSupport{
    private String dep_id;
    private String dep_name;
    private String dep_manager;

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

    public String getDep_manager(){
        return dep_manager;
    }

    public void setDep_manager(String dep_manager){
        this.dep_manager = dep_manager;
    }
}
