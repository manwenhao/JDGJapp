package com.example.jdgjapp.work.kaoqin.qingjia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jdgjapp.Bean.QJApplyRes;
import com.example.jdgjapp.R;

public class QJApplyDetail extends AppCompatActivity {
    private TextView time,start,end,type,reason,ans,ansres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qjapply_detail);
        time=(TextView)findViewById(R.id.detail_qj_time);
        start=(TextView)findViewById(R.id.detail_qj_start);
        end=(TextView)findViewById(R.id.detail_qj_end);
        type=(TextView)findViewById(R.id.detail_qj_type);
        reason=(TextView)findViewById(R.id.detail_qj_reason);
        ans=(TextView)findViewById(R.id.detail_qj_ans);
        ansres=(TextView)findViewById(R.id.detail_qj_ansres);
        QJApplyRes e=(QJApplyRes) getIntent().getSerializableExtra("bean");
        time.setText("请假时间："+e.getDatime());
        start.setText("开始时间："+e.getStartdate());
        end.setText("结束时间："+e.getEnddate());
        type.setText("假期类型："+e.getType());
        reason.setText("请假理由："+e.getReason());
        if (e.getAnswer()==null){
            ans.setText("审批结果：暂无");
            ansres.setText("审批理由：暂无");
        }else if (e.getAnswer().equals("1")){
            ans.setText("审批结果：通过");
            ansres.setText("审批理由："+e.getAnsreason());
        }else if (e.getAnswer().equals("2")){
            ans.setText("审批结果：未通过");
            ansres.setText("审批理由："+e.getAnsreason());
        }

    }
}
