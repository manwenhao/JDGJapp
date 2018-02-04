package com.example.jdgjapp.work.bangong.cailiao;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jdgjapp.Bean.CaiLiaoResponse;
import com.example.jdgjapp.R;

import java.util.List;

/**
 * Created by xuxuxiao on 2018/2/3.
 */

public class ResponseWholeAdapter extends BaseAdapter{
    private List<CaiLiaoResponse> list;
    private LayoutInflater inflater;

    public ResponseWholeAdapter(List<CaiLiaoResponse> list, Context context) {
        this.list = list;
        inflater=LayoutInflater.from(context);
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
           view=inflater.inflate(R.layout.cailiao_whole_res,viewGroup,false);
           vh.user=(TextView)view.findViewById(R.id.cl_res_whole_name);
           vh.timm=(TextView)view.findViewById(R.id.cl_res_whole_time);
           vh.isget=(TextView)view.findViewById(R.id.cl_res_whole_isget);
           view.setTag(vh);
       }else {
           vh=(ViewHolder)view.getTag();
       }
       vh.user.setText(list.get(i).getUser_name());
       vh.timm.setText(list.get(i).getDatetime());
       if (list.get(i).getRmat_status().equals("1")){
           vh.isget.setVisibility(View.VISIBLE);
           if (list.get(i).getLeadstatus().equals("0")){
               vh.isget.setText("领取状态: 待领取");
               vh.isget.setTextColor(Color.RED);
           }else if (list.get(i).getLeadstatus().equals("1")){
               vh.isget.setTextColor(Color.BLUE);
               vh.isget.setText("领取状态: 已领取");
           }
       }
       return view;
    }
    public void setdata(List<CaiLiaoResponse> list){
        this.list=list;
    }
    class ViewHolder{
        TextView user;
        TextView timm;
        TextView isget;
    }
}
