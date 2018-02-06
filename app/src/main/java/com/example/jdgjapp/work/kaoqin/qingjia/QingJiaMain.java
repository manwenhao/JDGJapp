package com.example.jdgjapp.work.kaoqin.qingjia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.jdgjapp.R;

public class QingJiaMain extends AppCompatActivity implements View.OnClickListener{
    RelativeLayout apply,applying,applyok,applyrefuse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qing_jia_main);
        apply=(RelativeLayout)findViewById(R.id.qj_apply);
        applying=(RelativeLayout)findViewById(R.id.qj_applying);
        applyok=(RelativeLayout)findViewById(R.id.qj_applyok);
        applyrefuse=(RelativeLayout)findViewById(R.id.qj_applyrefuse);
        apply.setOnClickListener(this);
        applying.setOnClickListener(this);
        applyok.setOnClickListener(this);
        applyrefuse.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.qj_apply:
                startActivity(new Intent(QingJiaMain.this,QJApply.class));
                break;
            case R.id.qj_applying:
                startActivity(new Intent(QingJiaMain.this,QJApplying.class));
                break;
            case R.id.qj_applyok:
                startActivity(new Intent(QingJiaMain.this,QJApplyok.class));
                break;
            case R.id.qj_applyrefuse:
                startActivity(new Intent(QingJiaMain.this,QJApplyrefuse.class));
                break;
            default:
                break;
        }
    }
}
