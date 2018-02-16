package com.example.jdgjapp.Bean;

/**
 * Created by xuxuxiao on 2018/2/7.
 */

public class SystemNews {
    private String title;
    private String content;
    private String time;
    private String type;
    private String status;   //0 -> 未读 1 -> 已读

    public SystemNews() {
    }

    public SystemNews(String title, String content, String time, String type, String status) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.type = type;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SystemNews{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
