package com.example.jdgjapp.work.bangong.shenpi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jdgjapp.R;

public class ShenPiMain extends AppCompatActivity {
    private TextView bx,cl,cc,qj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shen_pi_main);
        bx=(TextView)findViewById(R.id.sp_baoxiao);
        cl=(TextView)findViewById(R.id.sp_cailiao);
        cc=(TextView)findViewById(R.id.sp_chuchai);
        qj=(TextView)findViewById(R.id.sp_qingjia);
        bx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShenPiMain.this,ShenPiView.class);
                intent.putExtra("flag","bx");
                startActivity(intent);
            }
        });
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShenPiMain.this,ShenPiView.class);
                intent.putExtra("flag","cl");
                startActivity(intent);
            }
        });
        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShenPiMain.this,ShenPiView.class);
                intent.putExtra("flag","cc");
                startActivity(intent);
            }
        });
        qj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShenPiMain.this,ShenPiView.class);
                intent.putExtra("flag","qj");
                startActivity(intent);
            }
        });
    }
}
