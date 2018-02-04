package com.example.jdgjapp.work.bangong.cailiao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jdgjapp.Bean.CaiLiaoResponse;
import com.example.jdgjapp.R;

public class DatailOfApply extends AppCompatActivity {
    private TextView userid,username,name,num,time,reason,status,resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datail_of_apply);
        CaiLiaoResponse bean=(CaiLiaoResponse)getIntent().getSerializableExtra("bean");
        userid=(TextView)findViewById(R.id.detail_cl_res_userid);
        username=(TextView)findViewById(R.id.detail_cl_res_username);
        name=(TextView)findViewById(R.id.detail_cl_res_name);
        num=(TextView)findViewById(R.id.detail_cl_res_num);
        time=(TextView)findViewById(R.id.detail_cl_res_time);
        reason=(TextView)findViewById(R.id.detail_cl_res_reason);
        status=(TextView)findViewById(R.id.detail_cl_res_status);
        resp=(TextView)findViewById(R.id.detail_cl_res_response);
        userid.setText("申请者id: "+bean.getUser_id());
        username.setText("申请者姓名: "+bean.getUser_name());
        name.setText("材料名: "+bean.getMat_name());
        num.setText("申请数量: "+bean.getMat_num());
        time.setText("申请时间: "+bean.getDatetime());
        reason.setText("申请理由: "+bean.getReason());
        if (bean.getRmat_status().equals("0")){
            status.setText("申请结果: "+"待批复");
            resp.setText("审核回复: "+"暂无");
        }else if (bean.getRmat_status().equals("1")){
            status.setText("申请结果: "+"通过");
            resp.setText("审核回复: "+bean.getResp());
        }else if (bean.getRmat_status().equals("2")){
            resp.setText("审核回复: "+bean.getResp());
            status.setText("申请结果: "+"未通过");
        }


    }
}
