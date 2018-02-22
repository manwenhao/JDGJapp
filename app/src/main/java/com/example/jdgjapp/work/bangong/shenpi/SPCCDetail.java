package com.example.jdgjapp.work.bangong.shenpi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jdgjapp.Bean.*;
import com.example.jdgjapp.Bean.SPCCNO;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ActivityUtils;

public class SPCCDetail extends AppCompatActivity {
    private TextView ok,id,name,type,reason,time,start,end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spccdetail);
        ActivityUtils.getInstance().addActivity(SPCCDetail.class.getName(),this);
       final com.example.jdgjapp.Bean.SPCCNO e=(SPCCNO) getIntent().getSerializableExtra("bean");
        ok=(TextView)findViewById(R.id.sp_cc_no_into);
        id=(TextView)findViewById(R.id.cc_no_userid);
        name=(TextView)findViewById(R.id.cc_no_name);
        type=(TextView)findViewById(R.id.cc_no_type);
        reason=(TextView)findViewById(R.id.cc_no_reason);
        time=(TextView)findViewById(R.id.cc_no_time);
        start=(TextView)findViewById(R.id.cc_no_starttime);
        end=(TextView)findViewById(R.id.cc_no_endtime);
        id.setText("工号："+e.getMan_id());
        name.setText("姓名："+e.getUsr_name());
        type.setText("类型："+e.getType());
        reason.setText("申请理由："+e.getReason());
        time.setText("申请时间："+e.getDatime());
        start.setText("开始日期："+e.getStartdate());
        end.setText("结束日期："+e.getEnddate());
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SPCCDetail.this,CCReturn.class);
                intent.putExtra("sign",e.getId());
                intent.putExtra("req_id",e.getMan_id());
                startActivity(intent);
            }
        });
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.getInstance().delActivity(SPCCDetail.class.getName());
    }
}
