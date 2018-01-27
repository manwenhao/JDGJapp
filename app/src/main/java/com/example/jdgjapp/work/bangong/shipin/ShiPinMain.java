package com.example.jdgjapp.work.bangong.shipin;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.AddressListFragment;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.Friends.DeptMember;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ReturnUsrDep;

import java.util.ArrayList;
import java.util.List;

public class ShiPinMain extends AppCompatActivity {
    private ImageView back;
    private AddressListFragment fragment;
    public static List<String> useridList=new ArrayList<String>();
    private TextView ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shi_pin_main);
        ok=(TextView)findViewById(R.id.shipin_ok);
        DeptMember.flag=1;
        fragment=(AddressListFragment) getFragmentManager().findFragmentById(R.id.select_friend);
        back=(ImageView)findViewById(R.id.shipin_back);
        fragment.setChecked();
        Toast.makeText(this, "最多选择9人进行会议", Toast.LENGTH_LONG).show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.serunChecked();
                useridList=new ArrayList<String>();
                DeptMember.flag=0;
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("邀请人员id",ShiPinMain.useridList.toString());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragment.serunChecked();
        useridList=new ArrayList<String>();
        DeptMember.flag=0;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            fragment.serunChecked();
            useridList=new ArrayList<String>();
            DeptMember.flag=0;
        }

        return super.onKeyDown(keyCode, event);
    }
}
