package com.example.jdgjapp.work.bangong.gongdan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.jdgjapp.Bean.TaskMaterial;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.NumberAddSubView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mwh on 2018/2/5.
 */

public class TaskMaterial_ListViewAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public TaskMaterial_ListViewAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public final class Zujian {
        public CheckBox checkBox;
        public NumberAddSubView numberAddSubView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Zujian zujian = null;
        if (convertView == null) {
            zujian = new Zujian();
            //获得组件，实例化组件
            convertView = layoutInflater.inflate(R.layout.listview_task_material_item, null);
            zujian.checkBox = (CheckBox) convertView.findViewById(R.id.check);
            zujian.numberAddSubView = (NumberAddSubView) convertView.findViewById(R.id.num);
            convertView.setTag(zujian);
        } else {
            zujian = (Zujian) convertView.getTag();
        }
        //绑定数据
        zujian.checkBox.setText((String) data.get(position).get("name"));
        zujian.numberAddSubView.setMaxValue((Integer) data.get(position).get("num"));
        return convertView;
    }
}


