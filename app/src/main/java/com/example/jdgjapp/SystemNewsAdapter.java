package com.example.jdgjapp;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jdgjapp.Bean.SystemNews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        String time=list.get(i).getTime();
        SimpleDateFormat sfd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf2=new SimpleDateFormat("HH:mm:ss");

        try{
            Date datetime=sfd.parse(time);
            if (isSameDay(new Date(),datetime)){
                //是同一天
                vh.time.setText(sf2.format(datetime));
            }else{
                vh.time.setText(sf.format(datetime));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

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
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(date1);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(date2);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
                .get(Calendar.DAY_OF_MONTH);
    }

}
