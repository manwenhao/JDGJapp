package com.example.jdgjapp.work.bangong.baoxiao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jdgjapp.Bean.BaoXiaoPerson;
import com.example.jdgjapp.R;

import java.util.List;

/**
 * Created by xuxuxiao on 2018/2/11.
 */

public class BaoXiaoItemAdapter extends BaseAdapter {
private List<BaoXiaoPerson> list;
private LayoutInflater inflater;

    public BaoXiaoItemAdapter(List<BaoXiaoPerson> list, Context context) {
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
           view=inflater.inflate(R.layout.baoxiao_list_item,viewGroup,false);
           vh=new ViewHolder();
           vh.type=(TextView)view.findViewById(R.id.baoxiao_item_type);
           vh.time=(TextView)view.findViewById(R.id.baoxiao_item_time);
           vh.status=(TextView)view.findViewById(R.id.baoxiao_item_status);
           view.setTag(vh);
       }else {
           vh=(ViewHolder)view.getTag();
       }
       vh.type.setText("类型："+list.get(i).getAcc_kind());
       vh.time.setText("时间："+list.get(i).getAcc_date());
       if (list.get(i).getAcc_answer()==null){
           vh.status.setText("状态：待审核");
       }else if (list.get(i).getAcc_answer().equals("1")){
           vh.status.setText("状态：已通过");
       }else if (list.get(i).getAcc_answer().equals("2")){
           vh.status.setText("状态：未通过");
       }
       return view;
    }
    class ViewHolder{
        TextView type;
        TextView time;
        TextView status;
    }
    public void setDate(List<BaoXiaoPerson> list){
        this.list=list;
    }
}
