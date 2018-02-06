package com.example.jdgjapp.work.bangong.cheliang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jdgjapp.Bean.CarOfPerson;
import com.example.jdgjapp.R;

import java.util.List;

/**
 * Created by xuxuxiao on 2018/2/6.
 */

public class AdapterOfPerson extends BaseAdapter{
    private List<CarOfPerson> list;
    private LayoutInflater inflater;

    public AdapterOfPerson(List<CarOfPerson> list, Context context) {
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
            view=inflater.inflate(R.layout.car_item,viewGroup,false);
            vh.id=(TextView)view.findViewById(R.id.cat_item_id);
            view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }
        vh.id.setText("车牌号："+list.get(i).getCar_id());
        return view;
    }
     class ViewHolder{
        TextView id;
     }
}
