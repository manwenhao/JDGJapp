package com.example.jdgjapp.work.bangong.shenpi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jdgjapp.Bean.*;
import com.example.jdgjapp.Bean.SPCCNO;
import com.example.jdgjapp.R;

public class SPCCDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spccdetail);
        com.example.jdgjapp.Bean.SPCCNO e=(SPCCNO) getIntent().getSerializableExtra("bean");
        
    }
}
