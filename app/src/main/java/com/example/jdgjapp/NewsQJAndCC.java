package com.example.jdgjapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jdgjapp.Bean.SystemNews;

public class NewsQJAndCC extends AppCompatActivity {
    private TextView id,kind,reason,time,start,end;
    private String idstring,kindstring,reasonstring,timestring,startstring,endstring;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_qjand_cc);
        SystemNews e=(SystemNews)getIntent().getSerializableExtra("bean");
        id=(TextView)findViewById(R.id.cj_id);
        kind=(TextView)findViewById(R.id.cj_kind);
        reason=(TextView)findViewById(R.id.cj_reason);
        time=(TextView)findViewById(R.id.cj_time);
        start=(TextView)findViewById(R.id.cj_start);
        end=(TextView)findViewById(R.id.cj_end);
        String contstring=e.getContent();
        String []strs=contstring.split("@#\\$");
        idstring=strs[2];
        kindstring=strs[1];
        reasonstring=strs[6];
        timestring=strs[3];
        startstring=strs[4];
        endstring=strs[5];
        id.setText("申请者工号："+idstring);
        kind.setText("类型："+kindstring);
        reason.setText("申请理由："+reasonstring);
        time.setText("申请日期："+timestring);
        start.setText("开始日期："+startstring);
        end.setText("结束日期："+endstring);


    }
}
