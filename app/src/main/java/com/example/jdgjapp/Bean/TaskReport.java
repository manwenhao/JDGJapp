package com.example.jdgjapp.Bean;


import org.litepal.crud.DataSupport;

public class TaskReport extends DataSupport {

    private String taskid;
    private String datetime;
    private String content;
    private String imgpath;
    private String usematerial;


    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getUsematerial(){
        return usematerial;
    }

    public void setUsematerial(String usematerial){
        this.usematerial = usematerial;
    }
}
