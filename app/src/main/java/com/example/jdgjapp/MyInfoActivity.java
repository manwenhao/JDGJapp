package com.example.jdgjapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jdgjapp.Bean.Depart;
import com.example.jdgjapp.Bean.User;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MyInfoActivity extends AppCompatActivity {

    private RelativeLayout setnameBtn;
    private RelativeLayout setsexBtn;
    private RelativeLayout setbrithBtn;
    private RelativeLayout setaddressBtn;
    private RelativeLayout setphoneBtn;
    private TextView idTv;
    private TextView nameTv;
    private TextView sexTv;
    private TextView phoneTv;
    private TextView brithTv;
    private TextView addressTv;
    private TextView deptTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        setnameBtn = (RelativeLayout) findViewById(R.id.layout_name);
        setsexBtn = (RelativeLayout) findViewById(R.id.layout_sex);
        setbrithBtn = (RelativeLayout) findViewById(R.id.layout_brith);
        setaddressBtn = (RelativeLayout) findViewById(R.id.layout_address);
        setphoneBtn = (RelativeLayout) findViewById(R.id.layout_phone);

        idTv = (TextView) findViewById(R.id.tv_id);
        nameTv = (TextView) findViewById(R.id.tv_name);
        sexTv = (TextView) findViewById(R.id.tv_sex);
        phoneTv = (TextView) findViewById(R.id.tv_phone);
        brithTv = (TextView) findViewById(R.id.tv_brith);
        addressTv = (TextView) findViewById(R.id.tv_address);
        deptTv = (TextView) findViewById(R.id.tv_deptname);

        showMyInfo();
        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        setnameBtn.setOnClickListener(onClick);
        setsexBtn.setOnClickListener(onClick);
        setbrithBtn.setOnClickListener(onClick);
        setaddressBtn.setOnClickListener(onClick);
        setphoneBtn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()){
                case R.id.layout_name:
                    intent = new Intent(MyInfoActivity.this,SetNameActivity.class);
                    break;
                case R.id.layout_sex:
                    intent = new Intent(MyInfoActivity.this,SetSexActivity.class);
                    break;
                case R.id.layout_brith:
                    intent = new Intent(MyInfoActivity.this,SetBirthActivity.class);
                    break;
                case R.id.layout_address:
                    intent = new Intent(MyInfoActivity.this,SetAddressActivity.class);
                    break;
                case R.id.layout_phone:
                    intent = new Intent(MyInfoActivity.this,SetPhoneActivity.class);
                    break;
            }
            startActivity(intent);
            finish();
        }
    }

    private void showMyInfo(){
        User user = DataSupport.findFirst(User.class);
        idTv.setText(user.getUsr_id());
        nameTv.setText(user.getUsr_name());
        sexTv.setText(user.getUsr_sex());
        phoneTv.setText(user.getUsr_phone());
        brithTv.setText(user.getUsr_birth());
        addressTv.setText(user.getUsr_addr());
        List<Depart> departs = DataSupport.where("dep_id = ?",user.getUsr_deptId()).find(Depart.class);
        for (Depart depart : departs){
            deptTv.setText(depart.getDep_name());
        }
    }

}
