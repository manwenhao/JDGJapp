package com.example.jdgjapp.work.bangong.baoxiao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jdgjapp.R;
import com.example.jdgjapp.work.bangong.gongdan.TransmitList;

public class BaoXiaoMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bso_xiao_main);
        startActivity(new Intent(BaoXiaoMain.this, TransmitList.class));
    }
}
