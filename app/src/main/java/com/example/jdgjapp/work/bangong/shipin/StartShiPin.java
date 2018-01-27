package com.example.jdgjapp.work.bangong.shipin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;

public class StartShiPin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_shi_pin);
        Bundle bundle=getIntent().getExtras();
        String chanel=bundle.getString("chanel");
        Log.d("channel name",chanel+"==="+ MyApplication.getid());
    }
}
