package com.example.jdgjapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jdgjapp.Bean.SystemNews;

import java.util.List;

/**
 * Created by xuxuxiao on 2018/2/8.
 */

public class SystemNewsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<SystemNews> list;

    public SystemNewsAdapter(Context context, List<SystemNews> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view==null){
            vh=new ViewHolder();
            view=inflater.inflate(R.layout.system_news_item,viewGroup,false);
            vh.title=(TextView)view.findViewById(R.id.system_news_item_title);
            vh.content=(TextView)view.findViewById(R.id.system_news_item_cont);
            vh.time=(TextView)view.findViewById(R.id.system_news_item_time);
           view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }
        String type=list.get(i).getType();
        vh.time.setText(list.get(i).getTime());
        if (type.equals("1")){
            vh.title.setText("工单管理通知");
            vh.content.setText("你有一条新工单，请点击查看");
        }else if (type.equals("2")){
            vh.title.setText("材料申请通知");
            if (list.get(i).getContent().equals("1")){
                vh.content.setText("你有材料申请通过审核，请点击查看");
            }else {
                vh.content.setText("你有材料申请未通过审核，请点击查看");
            }
        }else if (type.equals("3")){
            vh.title.setText("视频通知");
            vh.content.setText(list.get(i).getTitle()+"邀请您进行视频，请点击查看");
        }else if (type.equals("4")){
            vh.title.setText("请假申请通知");
            if (list.get(i).getContent().equals("1")){
                vh.content.setText("你有请假申请通过审核，请点击查看");
            }else {
                vh.content.setText("你有请假申请未通过审核，请点击查看");
            }
        }else if (type.equals("5")){
            vh.title.setText("出差申请通知");
            if (list.get(i).getContent().equals("1")){
                vh.content.setText("你有出差申请通过审核，请点击查看");
            }else {
                vh.content.setText("你有出差申请未通过审核，请点击查看");
            }
        }
        return view;
    }
    public void setDate(List<SystemNews> list){
        this.list=list;
    }

    class ViewHolder{
        TextView title ;
        TextView content;
        TextView time;
    }
}
