package com.example.jdgjapp;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.jdgjapp.Bean.Depart;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.example.jdgjapp.Util.RoundImageView;
import com.example.jdgjapp.Util.SpacingTextView;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mwh on 2018/1/21.
 */

public class MyFragment extends Fragment {

    private static final String TAG = "MyFragment";
    private Button meResetpwBtn;
    private Button meExitloginBtn;
    private Button meInfoBtn;
    private RoundImageView iconIv;
    private SpacingTextView usernameTv;
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
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        return inflater.inflate(R.layout.fragment_my, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        usernameTv = (SpacingTextView) getActivity().findViewById(R.id.username);
        departmentTv = (TextView) getActivity().findViewById(R.id.department);
        meInfoBtn = (Button) getActivity().findViewById(R.id.btn_my_infomation);
        meResetpwBtn = (Button) getActivity().findViewById(R.id.btn_me_reset_pw);
        meExitloginBtn = (Button) getActivity().findViewById(R.id.btn_me_exit_login);
        iconIv = (RoundImageView) getActivity().findViewById(R.id.iv_icon);

        //读取姓名和部门并显示
        User user = DataSupport.findFirst(User.class);
        usernameTv.setText(user.getUsr_name());
        usernameTv.setSpacing(4);
        List<Depart> departs = DataSupport.where("dep_id = ?",user.getUsr_deptId()).find(Depart.class);
        for (Depart depart : departs){
            departmentTv.setText(depart.getDep_name());
        }

        refreshIcon();

        IntentFilter filter = new IntentFilter(SetIconActivity.action);
        getActivity().registerReceiver(broadcastReceiver, filter);

        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        meInfoBtn.setOnClickListener(onClick);
        meResetpwBtn.setOnClickListener(onClick);
        meExitloginBtn.setOnClickListener(onClick);
        iconIv.setOnClickListener(onClick);
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
                case R.id.iv_icon:
                    intent = new Intent(getActivity(),SetIconActivity.class);
                    startActivity(intent);
                    break;
                default:break;
            }


        }
    }

    private void refreshIcon(){
        //读取头像并显示
        String iconaddr = ReturnUsrDep.returnUsr().getTouxiang();
        //更新签名
        SharedPreferences pref = getActivity().getSharedPreferences(ReturnUsrDep.returnUsr().getUsr_id(),MODE_PRIVATE);
        String qm = pref.getString("icontime","");
        if (TextUtils.isEmpty(iconaddr)){
            iconIv.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.me_pic));
        }else {
            Glide.with(this)
                    .load(iconaddr)
                    .signature(new StringSignature(qm))
                    .error(R.drawable.me_pic)
                    .into(iconIv);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshIcon();
        }
    };

}
