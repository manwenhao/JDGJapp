package com.example.jdgjapp.work.bangong.cheliang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jdgjapp.R;

import java.util.List;

/**
 * Created by xuxuxiao on 2018/2/6.
 */

public class AdapterOfCarRepair extends BaseAdapter {
    private LayoutInflater inflater;
    private List<com.example.jdgjapp.Bean.CarRepair> list;

    public AdapterOfCarRepair(Context context, List<com.example.jdgjapp.Bean.CarRepair> list) {
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
            view=inflater.inflate(R.layout.car_item_repair,viewGroup,false);
            vh.time=(TextView)view.findViewById(R.id.car_repair_time);
            vh.add=(TextView)view.findViewById(R.id.car_repair_add);
            view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }
        vh.time.setText("保养日期："+list.get(i).getRepair_date());
        vh.add.setText("保养地点："+list.get(i).getRepair_add());
        return view;
    }
    class ViewHolder{
        TextView time;
        TextView add;
    }

}
