package com.example.jdgjapp.Bean;

import org.litepal.crud.DataSupport;


//工单
public class Task extends DataSupport {

    private String taskid;
    private String sender;
    private String createtime;
    private String startime;
    private String addr;
    private String content;
    private String cycle;
    private String status; //工单状态：0：未开始 1：待接收 2:进行中 3：已完成 4：逾期未完成 5：逾期已完成 6：转发工单

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStartime(){
        return startime;
    }

    public void setStartime(String startime){
        this.startime = startime;
    }

    public String getAddr(){
        return addr;
    }

    public void setAddr(String addr){
        this.addr = addr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
