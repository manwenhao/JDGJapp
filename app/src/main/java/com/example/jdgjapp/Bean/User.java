package com.example.jdgjapp.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by mwh on 2018/1/22.
 */

public class User extends DataSupport{
    private String usr_id;
    private String usr_name;
    private String usr_paswprd;
    private String usr_sex;
    private String usr_addr;
    private String usr_phone;
    private String usr_birth;
    private String usr_deptId;
    private String usr_bossId;


    public String getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(String usr_id){
        this.usr_id =usr_id;
    }

    public String getUsr_name(){
        return usr_name;
    }

    public void setUsr_name(String usr_name){
        this.usr_name = usr_name;
    }

    public String getUsr_paswprd(){
        return usr_paswprd;
    }

    public void setUsr_paswprd(String usr_paswprd){
        this.usr_paswprd = usr_paswprd;
    }

    public String getUsr_sex(){
        return usr_sex;
    }

    public void setUsr_sex(String usr_sex){
        this.usr_sex = usr_sex;
    }

    public String getUsr_addr(){
        return usr_addr;
    }

    public void setUsr_addr(String usr_addr){
        this.usr_addr = usr_addr;
    }

    public String getUsr_phone(){
        return usr_phone;
    }

    public void setUsr_phone(String usr_phone){
        this.usr_phone = usr_phone;
    }

    public String getUsr_birth(){
        return usr_birth;
    }

    public void setUsr_birth(String usr_birth){
        this.usr_birth = usr_birth;
    }

    public String getUsr_deptId(){
        return usr_deptId;
    }

    public void setUsr_deptId(String usr_deptId){
        this.usr_deptId = usr_deptId;
    }

    public String getUsr_bossId(){
        return usr_bossId;
    }

    public void setUsr_bossId(String usr_bossId){
        this.usr_bossId = usr_bossId;
    }
}
