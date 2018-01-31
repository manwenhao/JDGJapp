package com.example.jdgjapp.work.bangong.cailiao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jdgjapp.Bean.PersonCaiLiaoDetail;
import com.example.jdgjapp.R;

public class PersonDetailCaiLiao extends AppCompatActivity {
    private TextView name;
    private TextView num;
    private TextView time;
    private TextView kind;
    private TextView cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail_cai_liao);
        PersonCaiLiaoDetail person=(PersonCaiLiaoDetail) getIntent().getParcelableExtra("person");
        name=(TextView)findViewById(R.id.person_cldetail_name);
        num=(TextView)findViewById(R.id.person_cldetail_num);
        time=(TextView)findViewById(R.id.person_cldetail_time);
        kind=(TextView)findViewById(R.id.person_cldetail_kind);
        cont=(TextView)findViewById(R.id.person_cldetail_cont);
        name.setText("材料: "+person.getUse_name());
        num.setText("使用数量: "+person.getUse_num());
        time.setText("使用时间: "+person.getUse_time());
        kind.setText("使用去处: 日常使用");
        cont.setText("使用详情: "+person.getUse_cont());
    }
}
