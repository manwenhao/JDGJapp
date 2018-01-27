package com.example.jdgjapp;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.Bean.Depart;
import com.example.jdgjapp.Bean.User;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by mwh on 2018/1/21.
 */

public class MyFragment extends Fragment {

    private static final String TAG = "MyFragment";
    private Button meResetpwBtn;
    private Button meBindphoneBtn;
    private Button meExitloginBtn;
    private Button meNoticeBtn;
    private Button meInfoBtn;
    private ImageView iconIv;
    private Bitmap mBitmap;
    private TextView usernameTv;
    private TextView departmentTv;

    public MyFragment() {

    }

    public static MyFragment newInstance(){
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        usernameTv = (TextView) getActivity().findViewById(R.id.username);
        departmentTv = (TextView) getActivity().findViewById(R.id.department);
        meInfoBtn = (Button) getActivity().findViewById(R.id.btn_my_infomation);
        meResetpwBtn = (Button) getActivity().findViewById(R.id.btn_me_reset_pw);
        meExitloginBtn = (Button) getActivity().findViewById(R.id.btn_me_exit_login);


        //读取姓名和部门并显示
        User user = DataSupport.findFirst(User.class);
        usernameTv.setText(user.getUsr_name());
        List<Depart> departs = DataSupport.where("dep_id = ?",user.getUsr_deptId()).find(Depart.class);
        for (Depart depart : departs){
            departmentTv.setText(depart.getDep_name());
        }

        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        meInfoBtn.setOnClickListener(onClick);
        meResetpwBtn.setOnClickListener(onClick);
        meExitloginBtn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()){
                case R.id.btn_my_infomation:
                    intent = new Intent(getActivity(),MyInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_me_reset_pw:
                    intent = new Intent(getActivity(),SetPasswordActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_me_exit_login:
                    intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;
            }


        }
    }

}
