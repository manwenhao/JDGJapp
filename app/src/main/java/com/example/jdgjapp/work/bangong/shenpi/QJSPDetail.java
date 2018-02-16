package com.example.jdgjapp.work.bangong.shenpi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jdgjapp.Bean.QJSPNO;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ActivityUtils;

public class QJSPDetail extends AppCompatActivity {
    private TextView id,name,type,reason,time,start,end,ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qjspdetail);
        ActivityUtils.getInstance().addActivity(QJSPDetail.class.getName(),this);
        final QJSPNO e=(QJSPNO)getIntent().getSerializableExtra("bean");
        id=(TextView)findViewById(R.id.qj_no_userid);
        name=(TextView)findViewById(R.id.qj_no_name);
        type=(TextView)findViewById(R.id.qj_no_type);
        reason=(TextView)findViewById(R.id.qj_no_reason);
        time=(TextView)findViewById(R.id.qj_no_time);
        start=(TextView)findViewById(R.id.qj_no_starttime);
        end=(TextView)findViewById(R.id.qj_no_endtime);
        ok=(TextView)findViewById(R.id.sp_qj_no_into);
        id.setText("工号："+e.getMan_id());
        name.setText("姓名："+e.getUsr_name());
        type.setText("假期类型："+e.getType());
        reason.setText("申请理由："+e.getReason());
        time.setText("申请时间："+e.getDatime());
        start.setText("开始日期："+e.getStartdate());
        end.setText("结束日期："+e.getEnddate());
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(QJSPDetail.this,QJReturn.class);
                intent.putExtra("id",e.getId());
                intent.putExtra("req_id",e.getMan_id());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.getInstance().delActivity(QJSPDetail.class.getName());
    }
}
