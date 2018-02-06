package com.example.jdgjapp.Bean;

/**
 * Created by xuxuxiao on 2018/2/6.
 */

public class CarOfDeptList {
    private String username;
    private String userid;

    public CarOfDeptList() {
    }

    public CarOfDeptList(String username, String userid) {
        this.username = username;
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "CarOfDeptList{" +
                "username='" + username + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
