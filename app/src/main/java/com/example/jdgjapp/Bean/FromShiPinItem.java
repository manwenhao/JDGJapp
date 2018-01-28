package com.example.jdgjapp.Bean;

/**
 * Created by xuxuxiao on 2018/1/28.
 */

public class FromShiPinItem {
    private String vid_sender;
    private String vid_invited;
    private String vid_chanel;
    private String vid_date;

    public FromShiPinItem() {
    }

    public FromShiPinItem(String vid_sender, String vid_invited, String vid_chanel, String vid_date) {
        this.vid_sender = vid_sender;
        this.vid_invited = vid_invited;
        this.vid_chanel = vid_chanel;
        this.vid_date = vid_date;
    }

    public String getVid_sender() {
        return vid_sender;
    }

    public void setVid_sender(String vid_sender) {
        this.vid_sender = vid_sender;
    }

    public String getVid_invited() {
        return vid_invited;
    }

    public void setVid_invited(String vid_invited) {
        this.vid_invited = vid_invited;
    }

    public String getVid_chanel() {
        return vid_chanel;
    }

    public void setVid_chanel(String vid_chanel) {
        this.vid_chanel = vid_chanel;
    }

    public String getVid_date() {
        return vid_date;
    }

    public void setVid_date(String vid_date) {
        this.vid_date = vid_date;
    }

    @Override
    public String toString() {
        return "FromShiPinItem{" +
                "vid_sender='" + vid_sender + '\'' +
                ", vid_invited='" + vid_invited + '\'' +
                ", vid_chanel='" + vid_chanel + '\'' +
                ", vid_date='" + vid_date + '\'' +
                '}';
    }
}
