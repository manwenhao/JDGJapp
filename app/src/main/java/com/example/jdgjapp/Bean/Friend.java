package com.example.jdgjapp.Bean;

import android.support.annotation.NonNull;

/**
 * Created by xuxuxiao on 2018/1/24.
 */

public class Friend implements Comparable<Friend>{
    private String id;
    private String name;
    private String usr_sex;
    private String usr_addr;
    private String usr_phone;
    private String usr_birth;
    private String usr_dept;
    private String usr_bossId;

    public Friend() {
    }

    public Friend(String id, String name, String usr_sex, String usr_addr, String usr_phone, String usr_birth, String usr_dept, String usr_bossId) {
        this.id = id;
        this.name = name;
        this.usr_sex = usr_sex;
        this.usr_addr = usr_addr;
        this.usr_phone = usr_phone;
        this.usr_birth = usr_birth;
        this.usr_dept = usr_dept;
        this.usr_bossId = usr_bossId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsr_sex() {
        return usr_sex;
    }

    public void setUsr_sex(String usr_sex) {
        this.usr_sex = usr_sex;
    }

    public String getUsr_addr() {
        return usr_addr;
    }

    public void setUsr_addr(String usr_addr) {
        this.usr_addr = usr_addr;
    }

    public String getUsr_phone() {
        return usr_phone;
    }

    public void setUsr_phone(String usr_phone) {
        this.usr_phone = usr_phone;
    }

    public String getUsr_birth() {
        return usr_birth;
    }

    public void setUsr_birth(String usr_birth) {
        this.usr_birth = usr_birth;
    }

    public String getUsr_dept() {
        return usr_dept;
    }

    public void setUsr_dept(String usr_dept) {
        this.usr_dept = usr_dept;
    }

    public String getUsr_bossId() {
        return usr_bossId;
    }

    public void setUsr_bossId(String usr_bossId) {
        this.usr_bossId = usr_bossId;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", usr_sex='" + usr_sex + '\'' +
                ", usr_addr='" + usr_addr + '\'' +
                ", usr_phone='" + usr_phone + '\'' +
                ", usr_birth='" + usr_birth + '\'' +
                ", usr_dept='" + usr_dept + '\'' +
                ", usr_bossId='" + usr_bossId + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Friend friend) {
        if (this.usr_dept.compareTo(friend.getUsr_dept())==0){
            return this.name.compareTo(friend.getName());
        }else {
            return this.usr_dept.compareTo(friend.getUsr_dept());
        }
    }
}
