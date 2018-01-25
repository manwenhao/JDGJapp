package com.example.jdgjapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.jdgjapp.work.bangong.baoxiao.BaoXiaoMain;
import com.example.jdgjapp.work.bangong.cailiao.CaiLiaoMain;
import com.example.jdgjapp.work.bangong.cheliang.CheLiangMain;
import com.example.jdgjapp.work.bangong.gongdan.GongDanMain;
import com.example.jdgjapp.work.bangong.shenpi.ShenPiMain;
import com.example.jdgjapp.work.bangong.shipin.ShiPinMain;
import com.example.jdgjapp.work.kaoqin.chuchai.ChuChaiMain;
import com.example.jdgjapp.work.kaoqin.daka.DaKaMain;
import com.example.jdgjapp.work.kaoqin.qiandao.QianDaoMain;
import com.example.jdgjapp.work.kaoqin.qingjia.QingJiaMain;
import com.example.jdgjapp.work.kaoqin.tongji.TongJiMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mwh on 2018/1/21.
 */

public class WorkFragment extends Fragment {
    private GridView kaoqin;
    private GridView bangong;

    public WorkFragment() {

    }

    public static WorkFragment newInstance(){
        WorkFragment fragment = new WorkFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_work, container, false);
        kaoqin=(GridView)view.findViewById(R.id.work_one_gridView);
        bangong=(GridView)view.findViewById(R.id.work_two_gridView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Map<String,Object>> items=getdate();
        List<Map<String,Object>> item=getdate2();
        SimpleAdapter simpleAdapter2 = new SimpleAdapter(getActivity(), item, R.layout.kaoqin_grid_item, new String[] { "itemImage", "itemName" }, new int[] { R.id.work_itemImage, R.id.work_itemName });
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), items, R.layout.kaoqin_grid_item, new String[] { "itemImage", "itemName" }, new int[] { R.id.work_itemImage, R.id.work_itemName });
        kaoqin.setAdapter(simpleAdapter);
        bangong.setAdapter(simpleAdapter2);
        kaoqin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index=i+1;
                Log.d("workfragment","这是第"+index+"个");
                switch (i){
                    case 0:
                        startActivity(new Intent(getActivity(), DaKaMain.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), TongJiMain.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), QingJiaMain.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), ChuChaiMain.class));
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(), QianDaoMain.class));
                        break;
                    default:
                        break;
                }
            }
        });
        bangong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index=i+1;
                Log.d("workfragment","这是第"+index+"个");
                switch (i){
                    case 0:
                        startActivity(new Intent(getActivity(), GongDanMain.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), CaiLiaoMain.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), CheLiangMain.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), ShiPinMain.class));
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(),BaoXiaoMain.class));
                        break;
                    case 5:
                        startActivity(new Intent(getActivity(), ShenPiMain.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private List<Map<String,Object>> getdate(){
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        int [] listImg=new int[]{R.mipmap.kaoqin,R.mipmap.tongji,R.mipmap.qingjia,R.mipmap.chuchai,R.mipmap.qiandao,R.mipmap.nothing};
        String [] listName=new String[]{"考勤打卡","考勤统计","请假申请","出差申请","签到"," "};
        for (int i = 0; i < listImg.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("itemImage", listImg[i]);
            item.put("itemName", listName[i]);
            items.add(item);
        }
        return items;
    }
    private List<Map<String,Object>> getdate2(){
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        int [] listImg=new int[]{R.mipmap.gongdan,R.mipmap.cailiao,R.mipmap.cheliang,R.mipmap.shipin,R.mipmap.baoxiao,R.mipmap.shenpi};
        String [] listName=new String[]{"工单管理","材料管理","车辆管理","视频会议","报销系统","审批"};
        for (int i = 0; i < listImg.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("itemImage", listImg[i]);
            item.put("itemName", listName[i]);
            items.add(item);
        }
        return items;
    }

}
