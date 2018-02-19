package com.example.jdgjapp.work.bangong.shenpi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.jdgjapp.R;

public class ShenPiView extends AppCompatActivity {
    private RelativeLayout no,ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shen_pi_view);
        no=(RelativeLayout)findViewById(R.id.shenpi_no);
        ok=(RelativeLayout)findViewById(R.id.shenpi_ok);
        String flag=getIntent().getStringExtra("flag");
        if (flag.equals("bx")){
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ShenPiView.this,BXNo.class);
                    startActivity(intent);
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ShenPiView.this,BXOk.class);
                    startActivity(intent);
                }
            });
        }else if (flag.equals("qj")){
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ShenPiView.this,QJNo.class));
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ShenPiView.this,QJOk.class));
                }
            });
        }else if (flag.equals("cc")){
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ShenPiView.this,SPCCNO.class));
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ShenPiView.this,SPCCOK.class));
                }
            });
        }else if (flag.equals("cl")){
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ShenPiView.this,CLNo.class));
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ShenPiView.this,CLOk.class));
                }
            });
        }

    }
}
