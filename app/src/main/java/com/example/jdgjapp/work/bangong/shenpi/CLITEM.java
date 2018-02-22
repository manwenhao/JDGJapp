package com.example.jdgjapp.work.bangong.shenpi;

import java.io.Serializable;

/**
 * Created by xuxuxiao on 2018/2/17.
 */

public class CLITEM implements Serializable {
    private String name;
    private String id;
    private String time;
    private String sign;

    public CLITEM() {
    }

    public CLITEM(String name, String id, String time, String sign) {
        this.name = name;
        this.id = id;
        this.time = time;
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "CLITEM{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
