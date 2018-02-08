package com.example.jdgjapp.work.bangong.cheliang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jdgjapp.Bean.CarOfPerson;
import com.example.jdgjapp.R;

public class DetailofCar extends AppCompatActivity {
    private TextView name,id,color,owner;
    private Button see;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailof_car);
        name=(TextView)findViewById(R.id.car_name);
        id=(TextView)findViewById(R.id.car_id);
        color=(TextView)findViewById(R.id.car_color);
        owner=(TextView)findViewById(R.id.car_owner);
        see=(Button)findViewById(R.id.see_car_repair);
        final CarOfPerson car=(CarOfPerson)getIntent().getSerializableExtra("bean");
        name.setText("车辆名称："+car.getCar_name());
        id.setText("车牌号："+car.getCar_id());
        color.setText("车辆颜色："+car.getCar_color());
        owner.setText("拥有者："+car.getUsr_name());
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetailofCar.this,CarRepair.class);
                intent.putExtra("car_id",car.getCar_id());
                startActivity(intent);
            }
        });


    }
}
