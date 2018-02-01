package com.example.jdgjapp.work.bangong.gongdan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.AddressListFragment;
import com.example.jdgjapp.Friends.ContactsSortAdapter;
import com.example.jdgjapp.Friends.DeptMember;
import com.example.jdgjapp.Friends.MyDeptMent;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ActivityUtils;
import com.example.jdgjapp.work.bangong.shipin.ShiPinMain;

import java.util.ArrayList;
import java.util.List;

public class TransmitList extends AppCompatActivity {
    private AddressListFragment fragment;
    public static List<String> useridList=new ArrayList<String>();//转发人员的id集合
    private TextView ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit_list);
        ActivityUtils.getInstance().addActivity(TransmitList.class.getName(),this);
        ok=(TextView)findViewById(R.id.shipin_ok3);
        DeptMember.flag=2;
        fragment=(AddressListFragment) getFragmentManager().findFragmentById(R.id.select_friend3);
        ContactsSortAdapter.flag=2;
        Toast.makeText(this, "请选择转发人员", Toast.LENGTH_SHORT).show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click();
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContactsSortAdapter.flag=0;
        useridList=new ArrayList<String>();
        DeptMember.flag=0;
        ActivityUtils.getInstance().delActivity(TransmitList.class.getName());
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ContactsSortAdapter.flag=0;
            useridList=new ArrayList<String>();
            DeptMember.flag=0;
        }

        return super.onKeyDown(keyCode, event);
    }
    //ok的点击事件
    public static void click(){
        Log.d("我选择的转发人员",TransmitList.useridList.toString());
        ActivityUtils.getInstance().delActivity(MyDeptMent.class.getName());
        ActivityUtils.getInstance().delActivity(DeptMember.class.getName());
        ActivityUtils.getInstance().delActivity(TransmitList.class.getName());

        //下面写具体的点击事件
    }

}
