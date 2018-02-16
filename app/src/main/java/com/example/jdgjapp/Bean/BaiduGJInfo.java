package com.example.jdgjapp.Bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by mwh on 2018/2/14.
 */

public class BaiduGJInfo extends DataSupport implements Serializable {
    private String posx;
    private String posy;
    private String datime;
    private String addr;

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

    public String getAddr(){
        return addr;
    }

    public void setAddr(String addr){
        this.addr = addr;
    }
}
