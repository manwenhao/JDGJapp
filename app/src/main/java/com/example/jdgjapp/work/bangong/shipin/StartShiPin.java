package com.example.jdgjapp.work.bangong.shipin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.model.ConstantApp;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.ui.ChatActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StartShiPin extends AppCompatActivity {
    private static String chanel;
    private static String sender;
    private RelativeLayout ok;
    private RelativeLayout refuse;
    private CircleImageView okicon;
    private CircleImageView refuseicon;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_shi_pin);
        Bundle bundle=getIntent().getExtras();
        chanel=bundle.getString(ConstantApp.ACTION_KEY_CHANNEL_NAME);
        Log.d("channel name",chanel);
        sender=bundle.getString("sender");
        Log.d("sender",sender);
        ok=(RelativeLayout)findViewById(R.id.shipinaccept);
        refuse=(RelativeLayout)findViewById(R.id.shipinjujue);
        okicon=(CircleImageView)findViewById(R.id.accept_shipin);
        refuseicon=(CircleImageView)findViewById(R.id.refuse_shipin);
        title=(TextView)findViewById(R.id.shipin_title);
        Glide.with(this).load(R.drawable.acceptshipin).into(okicon);
        Glide.with(this).load(R.drawable.canelshipin).into(refuseicon);
        title.setText(sender+"邀请你进行视频");
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyApplication)MyApplication.getContext()).initWorkerThread();
                Intent intent=new Intent(StartShiPin.this, ChatActivity.class);
                intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,chanel);
                startActivity(intent);
                finish();
            }
        });

    }
}
