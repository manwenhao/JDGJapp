package com.example.jdgjapp.work.bangong.gongdan;

import okhttp3.Call;


public interface ReportListener {
public void onError(Call call, Exception e, int id);
    public void onSuccess(String response, int id);

}
