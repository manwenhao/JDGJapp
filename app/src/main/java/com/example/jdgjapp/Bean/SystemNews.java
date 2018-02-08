package com.example.jdgjapp.Bean;

/**
 * Created by xuxuxiao on 2018/2/7.
 */

public class SystemNews {
    private String title;
    private String content;
    private String time;
    private String type;

    public SystemNews() {
    }

    public SystemNews(String title, String content, String time, String type) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.type = type;
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

    @Override
    public String toString() {
        return "SystemNews{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
