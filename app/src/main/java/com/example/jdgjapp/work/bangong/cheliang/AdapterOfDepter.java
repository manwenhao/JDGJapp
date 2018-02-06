package com.example.jdgjapp.work.bangong.cheliang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jdgjapp.Bean.CarOfDeptList;
import com.example.jdgjapp.R;

import java.util.List;

/**
 * Created by xuxuxiao on 2018/2/6.
 */

public class AdapterOfDepter extends BaseAdapter {
    private List<CarOfDeptList> list;
    private LayoutInflater inflater;

    public AdapterOfDepter(List<CarOfDeptList> list, Context context) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
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
            view=inflater.inflate(R.layout.car_depter_item,viewGroup,false);
            vh.id=(TextView)view.findViewById(R.id.car_depter_id);
            vh.name=(TextView)view.findViewById(R.id.car_depter_name);
            view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }
        vh.name.setText("姓名："+list.get(i).getUsername());
        vh.id.setText("工号："+list.get(i).getUserid());
        return view;
    }
    class ViewHolder{
        TextView name;
        TextView id;
    }

}
