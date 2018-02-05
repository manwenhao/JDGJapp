package com.example.jdgjapp.work.kaoqin.chuchai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.jdgjapp.R;
import com.example.jdgjapp.work.kaoqin.qingjia.QJApply;
import com.example.jdgjapp.work.kaoqin.qingjia.QJApplying;
import com.example.jdgjapp.work.kaoqin.qingjia.QJApplyok;
import com.example.jdgjapp.work.kaoqin.qingjia.QJApplyrefuse;
import com.example.jdgjapp.work.kaoqin.qingjia.QingJiaMain;

public class ChuChaiMain extends AppCompatActivity implements View.OnClickListener{
    RelativeLayout apply,applying,applyok,applyrefuse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chu_chai_main);
        apply=(RelativeLayout)findViewById(R.id.cc_apply);
        applying=(RelativeLayout)findViewById(R.id.cc_applying);
        applyok=(RelativeLayout)findViewById(R.id.cc_applyok);
        applyrefuse=(RelativeLayout)findViewById(R.id.cc_applyrefuse);
        apply.setOnClickListener(this);
        applying.setOnClickListener(this);
        applyok.setOnClickListener(this);
        applyrefuse.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cc_apply:
                startActivity(new Intent(ChuChaiMain.this,CCApply.class));
                break;
            case R.id.cc_applying:
                startActivity(new Intent(ChuChaiMain.this,CCApplying.class));
                break;
            case R.id.cc_applyok:
                startActivity(new Intent(ChuChaiMain.this,CCApplyok.class));
                break;
            case R.id.cc_applyrefuse:
                startActivity(new Intent(ChuChaiMain.this,CCApplyrefuse.class));
                break;
            default:
                break;
        }
    }
}
