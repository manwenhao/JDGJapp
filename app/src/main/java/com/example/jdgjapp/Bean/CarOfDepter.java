package com.example.jdgjapp.Bean;

/**
 * Created by xuxuxiao on 2018/2/6.
 */

public class CarOfDepter {
    private String car_id;
    private String car_name;
    private String car_color;
    private String usr_name;
    private String dep_name;
    private String usr_id;

    public CarOfDepter() {
    }

    public CarOfDepter(String car_id, String car_name, String car_color, String usr_name, String dep_name, String usr_id) {
        this.car_id = car_id;
        this.car_name = car_name;
        this.car_color = car_color;
        this.usr_name = usr_name;
        this.dep_name = dep_name;
        this.usr_id = usr_id;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_color() {
        return car_color;
    }

    public void setCar_color(String car_color) {
        this.car_color = car_color;
    }

    public String getUsr_name() {
        return usr_name;
    }

    public void setUsr_name(String usr_name) {
        this.usr_name = usr_name;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public String getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(String usr_id) {
        this.usr_id = usr_id;
    }

    @Override
    public String toString() {
        return "CarOfDepter{" +
                "car_id='" + car_id + '\'' +
                ", car_name='" + car_name + '\'' +
                ", car_color='" + car_color + '\'' +
                ", usr_name='" + usr_name + '\'' +
                ", dep_name='" + dep_name + '\'' +
                ", usr_id='" + usr_id + '\'' +
                '}';
    }
}
