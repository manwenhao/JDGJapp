package com.example.jdgjapp.Bean;

import java.io.Serializable;

/**
 * Created by xuxuxiao on 2018/2/6.
 */

public class CarRepair  implements Serializable{
    private String repair_id;
    private String repair_content;
    private String repair_date;
    private String repair_add;
    private String usr_name;
    private String repair_money;

    public CarRepair() {
    }

    public CarRepair(String repair_id, String repair_content, String repair_date, String repair_add, String usr_name, String repair_money) {
        this.repair_id = repair_id;
        this.repair_content = repair_content;
        this.repair_date = repair_date;
        this.repair_add = repair_add;
        this.usr_name = usr_name;
        this.repair_money = repair_money;
    }

    public String getRepair_id() {
        return repair_id;
    }

    public void setRepair_id(String repair_id) {
        this.repair_id = repair_id;
    }

    public String getRepair_content() {
        return repair_content;
    }

    public void setRepair_content(String repair_content) {
        this.repair_content = repair_content;
    }

    public String getRepair_date() {
        return repair_date;
    }

    public void setRepair_date(String repair_date) {
        this.repair_date = repair_date;
    }

    public String getRepair_add() {
        return repair_add;
    }

    public void setRepair_add(String repair_add) {
        this.repair_add = repair_add;
    }

    public String getUsr_name() {
        return usr_name;
    }

    public void setUsr_name(String usr_name) {
        this.usr_name = usr_name;
    }

    public String getRepair_money() {
        return repair_money;
    }

    public void setRepair_money(String repair_money) {
        this.repair_money = repair_money;
    }

    @Override
    public String toString() {
        return "CarRepair{" +
                "repair_id='" + repair_id + '\'' +
                ", repair_content='" + repair_content + '\'' +
                ", repair_date='" + repair_date + '\'' +
                ", repair_add='" + repair_add + '\'' +
                ", usr_name='" + usr_name + '\'' +
                ", repair_money='" + repair_money + '\'' +
                '}';
    }
}
