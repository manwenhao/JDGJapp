package com.example.jdgjapp;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jdgjapp.Bean.SystemNews;
import com.example.jdgjapp.Util.ACache;
import com.example.jdgjapp.work.bangong.cailiao.ApplyPass;
import com.example.jdgjapp.work.bangong.cailiao.ApplyRefuse;
import com.example.jdgjapp.work.bangong.cailiao.TaskInfoOfCL;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.model.ConstantApp;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.ui.ChatActivity;
import com.example.jdgjapp.work.kaoqin.chuchai.CCApplyok;
import com.example.jdgjapp.work.kaoqin.chuchai.CCApplyrefuse;
import com.example.jdgjapp.work.kaoqin.qingjia.QJApplyok;
import com.example.jdgjapp.work.kaoqin.qingjia.QJApplyrefuse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {
    private TextView text;
    private ListView listView;
    private SystemNewsAdapter adapter;
    public static List<SystemNews> list=new ArrayList<SystemNews>();
    private Myreceiver myreceiver;

    public NewsFragment() {

    }

    public static NewsFragment newInstance(){
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_news, container, false);
        text=(TextView)view.findViewById(R.id.tv_nodata);
        listView=(ListView)view.findViewById(R.id.main_news);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (list.size()!=0){
            text.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
            adapter=new SystemNewsAdapter(MyApplication.getContext(),list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    SystemNews e=list.get(i);
                    if (e.getType().equals("1")){
                        Intent intent=new Intent(getActivity(), TaskInfoOfCL.class);
                        intent.putExtra("task_id",e.getContent());
                        getActivity().startActivity(intent);
                    }else if (e.getType().equals("2")){
                        if (e.getContent().equals("1")){
                            Intent intent=new Intent(getActivity(), ApplyPass.class);
                            getActivity().startActivity(intent);
                        }else {
                            Intent intent=new Intent(getActivity(), ApplyRefuse.class);
                            getActivity().startActivity(intent);
                        }
                    }else if (e.getType().equals("3")){
                        ((MyApplication)MyApplication.getContext()).initWorkerThread();
                        Intent intent=new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,e.getContent());
                        getActivity().startActivity(intent);
                    }else if (e.getType().equals("4")){
                        if (e.getContent().equals("1")){
                            Intent intent=new Intent(getActivity(), QJApplyok.class);
                            getActivity().startActivity(intent);
                        }else {
                            Intent intent=new Intent(getActivity(), QJApplyrefuse.class);
                            getActivity().startActivity(intent);
                        }
                    }else if (e.getType().equals("5")){
                        if (e.getContent().equals("1")){
                            Intent intent=new Intent(getActivity(), CCApplyok.class);
                            getActivity().startActivity(intent);
                        }else {
                            Intent intent=new Intent(getActivity(), CCApplyrefuse.class);
                            getActivity().startActivity(intent);
                        }
                    }
                }
            });

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("systemnews");
        myreceiver=new Myreceiver();
        getActivity().registerReceiver(myreceiver,intentFilter);


    }
    class Myreceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            listView.setVisibility(View.VISIBLE);
            text.setVisibility(View.INVISIBLE);
            adapter.setDate(list);
            adapter.notifyDataSetChanged();

        }
    }
}
