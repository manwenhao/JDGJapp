package com.example.jdgjapp.work.bangong.cailiao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jdgjapp.R;

public class CaiLiaoApply extends AppCompatActivity {
    private TextView apply,ing,pass,fail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_liao_apply);
        apply=(TextView)findViewById(R.id.apply_cailiao);
        ing=(TextView)findViewById(R.id.apply_cailiao_ing);
        pass=(TextView)findViewById(R.id.apply_cailiao_pass);
        fail=(TextView)findViewById(R.id.apply_cailiao_refuse);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaiLiaoApply.this,ApplyCL.class));
            }
        });
        ing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaiLiaoApply.this,ApplyIng.class));
            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaiLiaoApply.this,ApplyPass.class));
            }
        });
        fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CaiLiaoApply.this,ApplyRefuse.class));
            }
        });

    }
}
