package com.example.jdgjapp.work.bangong.cheliang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jdgjapp.Bean.*;
import com.example.jdgjapp.Bean.CarRepair;
import com.example.jdgjapp.R;

public class DetailOfCarRepair extends AppCompatActivity {
    private TextView id,time,add,man,money,content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_car_repair);
        com.example.jdgjapp.Bean.CarRepair e=(CarRepair)getIntent().getSerializableExtra("bean");
        id=(TextView)findViewById(R.id.car_rep_carid);
        time=(TextView)findViewById(R.id.car_rep_time);
        add=(TextView)findViewById(R.id.car_rep_add);
        man=(TextView)findViewById(R.id.car_rep_man);
        money=(TextView)findViewById(R.id.car_rep_money);
        content=(TextView)findViewById(R.id.car_rep_cont);
        id.setText("车牌号："+e.getRepair_id());
        time.setText("保养时间："+e.getRepair_date());
        add.setText("保养地点："+e.getRepair_add());
        man.setText("保养人："+e.getUsr_name());
        money.setText("保养费用："+e.getRepair_money());
        content.setText("保养内容："+e.getRepair_content());

    }
}
