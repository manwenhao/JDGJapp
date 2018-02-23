package com.example.jdgjapp;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.jdgjapp.Bean.SystemNews;
import com.example.jdgjapp.Util.ACache;
import com.example.jdgjapp.work.bangong.baoxiao.BaoXiaoMain;
import com.example.jdgjapp.work.bangong.cailiao.ApplyPass;
import com.example.jdgjapp.work.bangong.cailiao.ApplyRefuse;
import com.example.jdgjapp.work.bangong.cailiao.TaskInfoOfCL;
import com.example.jdgjapp.work.bangong.cailiao.TaskInfoOfDepterCL;
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
    private SwipeMenuListView listView;
    private SystemNewsAdapter adapter;
    public  List<SystemNews> list=new ArrayList<SystemNews>();
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
        listView=(SwipeMenuListView)view.findViewById(R.id.main_news);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        MyApplication.getContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        MyApplication.getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         final ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
        String datastring=aCache.getAsString("systemnews");
        Type type=new TypeToken<List<SystemNews>>(){}.getType();
        if (datastring!=null){
            text.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            list=new Gson().fromJson(datastring,type);
        }
        if (list.size()==0){
            listView.setVisibility(View.INVISIBLE);
            text.setVisibility(View.VISIBLE);
        }
            adapter=new SystemNewsAdapter(MyApplication.getContext(),list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("=======",list.get(i).toString());
                    SystemNews e=list.get(i);
                    if (e.getStatus()==null){
                        e.setStatus("1");
                        list.remove(i);
                        list.add(i,e);
                        adapter.notifyDataSetChanged();
                        aCache.put("systemnews",new Gson().toJson(list));
                    }else if (e.getStatus().equals("0")){
                        e.setStatus("1");
                        list.remove(i);
                        list.add(i,e);
                        adapter.notifyDataSetChanged();
                        aCache.put("systemnews",new Gson().toJson(list));
                    }
                    if (e.getType().equals("1")){
                        Intent intent=new Intent(getActivity(), TaskInfoOfDepterCL.class);
                        intent.putExtra("task_id",e.getContent());
                        getActivity().startActivity(intent);
                    }else if (e.getType().equals("2")){
                        if (e.getContent().equals("1")){
                            Intent intent=new Intent(getActivity(), ApplyPass.class);
                            intent.putExtra("newsid",e.getTitle());
                            getActivity().startActivity(intent);
                        }else {
                            Intent intent=new Intent(getActivity(), ApplyRefuse.class);
                            intent.putExtra("newsid",e.getTitle());
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
                            intent.putExtra("newsid",e.getTitle());
                            getActivity().startActivity(intent);
                        }else {
                            Intent intent=new Intent(getActivity(), QJApplyrefuse.class);
                            intent.putExtra("newsid",e.getTitle());
                            getActivity().startActivity(intent);
                        }
                    }else if (e.getType().equals("5")){
                        if (e.getContent().equals("1")){
                            Intent intent=new Intent(getActivity(), CCApplyok.class);
                            intent.putExtra("newsid",e.getTitle());
                            getActivity().startActivity(intent);
                        }else {
                            Intent intent=new Intent(getActivity(), CCApplyrefuse.class);
                            intent.putExtra("newsid",e.getTitle());
                            getActivity().startActivity(intent);
                        }
                    }else if (e.getType().equals("6")){
                        Intent intent=new Intent(getActivity(), BaoXiaoMain.class);
                        intent.putExtra("newsid",e.getTitle());
                        getActivity().startActivity(intent);
                    }else if (e.getType().equals("7")){
                        Intent intent=new Intent(getActivity(),NewsBXDetail.class);
                        intent.putExtra("bean",e);
                        getActivity().startActivity(intent);
                    }else if (e.getType().equals("8")||e.getType().equals("9")){
                        Intent intent=new Intent(getActivity(),NewsQJAndCC.class);
                        intent.putExtra("bean",e);
                        getActivity().startActivity(intent);
                    }else if (e.getType().equals("10")){
                        Intent intent=new Intent(getActivity(),NewsCL.class);
                        intent.putExtra("bean",e);
                        getActivity().startActivity(intent);
                    }
                    //adapter.notifyDataSetChanged();
                }
            });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int i, SwipeMenu menu, int index) {
                Log.d("=======",list.get(i).toString());
                switch (index) {

                    case 0:
                        // open
                        SystemNews e=list.get(i);
                        if (e.getStatus()==null){
                            e.setStatus("1");
                            list.remove(i);
                            list.add(i,e);
                            adapter.notifyDataSetChanged();
                            aCache.put("systemnews",new Gson().toJson(list));
                        }else if (e.getStatus().equals("0")){
                            e.setStatus("1");
                            list.remove(i);
                            list.add(i,e);
                            adapter.notifyDataSetChanged();
                            aCache.put("systemnews",new Gson().toJson(list));
                        }
                        if (e.getType().equals("1")){
                            Intent intent=new Intent(getActivity(), TaskInfoOfCL.class);
                            intent.putExtra("task_id",e.getContent());
                            getActivity().startActivity(intent);
                        }else if (e.getType().equals("2")){
                            if (e.getContent().equals("1")){
                                Intent intent=new Intent(getActivity(), ApplyPass.class);
                                intent.putExtra("newsid",e.getTitle());
                                getActivity().startActivity(intent);
                            }else {
                                Intent intent=new Intent(getActivity(), ApplyRefuse.class);
                                intent.putExtra("newsid",e.getTitle());
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
                                intent.putExtra("newsid",e.getTitle());
                                getActivity().startActivity(intent);
                            }else {
                                Intent intent=new Intent(getActivity(), QJApplyrefuse.class);
                                intent.putExtra("newsid",e.getTitle());
                                getActivity().startActivity(intent);
                            }
                        }else if (e.getType().equals("5")){
                            if (e.getContent().equals("1")){
                                Intent intent=new Intent(getActivity(), CCApplyok.class);
                                intent.putExtra("newsid",e.getTitle());
                                getActivity().startActivity(intent);
                            }else {
                                Intent intent=new Intent(getActivity(), CCApplyrefuse.class);
                                intent.putExtra("newsid",e.getTitle());
                                getActivity().startActivity(intent);
                            }
                        }else if (e.getType().equals("6")){
                           Intent intent=new Intent(getActivity(), BaoXiaoMain.class);
                           intent.putExtra("newsid",e.getTitle());
                           getActivity().startActivity(intent);
                        }else if (e.getType().equals("7")){
                            Intent intent=new Intent(getActivity(),NewsBXDetail.class);
                            intent.putExtra("bean",e);
                            getActivity().startActivity(intent);
                        }else if (e.getType().equals("8")||e.getType().equals("9")){
                            Intent intent=new Intent(getActivity(),NewsQJAndCC.class);
                            intent.putExtra("bean",e);
                            getActivity().startActivity(intent);
                        }else if (e.getType().equals("10")){
                            Intent intent=new Intent(getActivity(),NewsCL.class);
                            intent.putExtra("bean",e);
                            getActivity().startActivity(intent);
                        }


                        break;
                    case 1:
                        // delete
                        list.remove(i);
                        if (list.size()==0){
                            listView.setVisibility(View.INVISIBLE);
                            text.setVisibility(View.VISIBLE);
                        }
                        String da=new Gson().toJson(list);
                        ACache aCache1=ACache.get(MyApplication.getContext(),MyApplication.getid());
                        aCache1.put("systemnews",da);
                        adapter.setDate(list);
                        adapter.notifyDataSetChanged();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("systemnews");
        myreceiver=new Myreceiver();
        getActivity().registerReceiver(myreceiver,intentFilter);


    }

    @Override
    public void onResume() {
        super.onResume();
//        ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
//        Type type=new TypeToken<List<SystemNews>>(){}.getType();
//        list=new Gson().fromJson(aCache.getAsString("systemnews"),type);
//        adapter.setDate(list);
//        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myreceiver);
    }

    class Myreceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            listView.setVisibility(View.VISIBLE);
            text.setVisibility(View.INVISIBLE);
            ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
            Type type=new TypeToken<List<SystemNews>>(){}.getType();
            String datastring=aCache.getAsString("systemnews");
            list=new Gson().fromJson(datastring,type);
            adapter.setDate(list);
            adapter.notifyDataSetChanged();


        }
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
