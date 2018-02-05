package com.example.jdgjapp.work.kaoqin.chuchai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jdgjapp.Bean.CCApplyRes;
import com.example.jdgjapp.Bean.QJApplyRes;
import com.example.jdgjapp.R;
import com.example.jdgjapp.work.kaoqin.qingjia.QJAdapter;

import java.util.List;

/**
 * Created by xuxuxiao on 2018/2/6.
 */

public class CCAdapter extends BaseAdapter {
    private List<CCApplyRes> list;
    private LayoutInflater inflater;

    public CCAdapter(List<CCApplyRes> list, Context context) {
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
            view=inflater.inflate(R.layout.qj_apply_res_list_item,viewGroup,false);
            vh.time=(TextView)view.findViewById(R.id.qj_apply_res_list_time);
            vh.type=(TextView)view.findViewById(R.id.qj_apply_res_list_type);
            view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }
        vh.type.setText("出差类型："+list.get(i).getType());
        vh.time.setText("出差日期："+list.get(i).getDatime());


        return view;
    }
    public void setdate(List<CCApplyRes> list){
        this.list=list;
    }
    class ViewHolder{
        TextView time;
        TextView type;
    }
}
