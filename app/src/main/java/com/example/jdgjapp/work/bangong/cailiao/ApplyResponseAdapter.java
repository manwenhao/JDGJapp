package com.example.jdgjapp.work.bangong.cailiao;

import android.content.Context;
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

public class ApplyResponseAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<CaiLiaoResponse> list;

    public ApplyResponseAdapter(Context context, List<CaiLiaoResponse> list) {
       inflater=LayoutInflater.from(context);
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
            view=inflater.inflate(R.layout.cailiao_response_item,viewGroup,false);
            vh.name=(TextView)view.findViewById(R.id.cl_res_name);
            vh.num=(TextView)view.findViewById(R.id.cl_res_num);
            vh.data=(TextView)view.findViewById(R.id.cl_res_user);
            vh.user=(TextView)view.findViewById(R.id.cl_res_date);
            view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }
        vh.name.setText("材料名:"+list.get(i).getMat_name());
        vh.num.setText("申请数量:"+list.get(i).getMat_num());
        vh.data.setText("申请日期:"+list.get(i).getDatetime());
        vh.user.setText("申请者:"+list.get(i).getUser_name());
        return view;
    }
    class ViewHolder{
        TextView name;
        TextView num;
        TextView data;
        TextView user;

    }

}
