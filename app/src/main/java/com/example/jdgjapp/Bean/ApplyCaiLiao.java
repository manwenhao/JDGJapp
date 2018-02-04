package com.example.jdgjapp.Bean;

/**
 * Created by xuxuxiao on 2018/2/2.
 */

public class ApplyCaiLiao {
    private String id;
    private String num;

    public ApplyCaiLiao() {
    }

    public ApplyCaiLiao(String id, String num) {
        this.id = id;
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "ApplyCaiLiao{" +
                "id='" + id + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
