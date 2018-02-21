package com.example.jdgjapp.work.bangong.shenpi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jdgjapp.Bean.CLSPNO;
import com.example.jdgjapp.Bean.CaiLiaoResponse;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ACache;
import com.example.jdgjapp.Util.ActivityUtils;
import com.example.jdgjapp.work.bangong.cailiao.DatailOfApply;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CLNOList extends AppCompatActivity {
    private ListView listView;
    private List<CLSPNO> list;
    private Myadapter myadapter;
    private TextView start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloklist);
        ActivityUtils.getInstance().addActivity(CLNOList.class.getName(),this);
        final String sign=getIntent().getStringExtra("sign");
        final String userid=getIntent().getStringExtra("userid");
        start=(TextView)findViewById(R.id.cl_sp_res);
        listView=(ListView)findViewById(R.id.cl_sp_no_lisy);
        ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
        Type type=new TypeToken<List<CLSPNO>>(){}.getType();
        List<CLSPNO> datalist=new Gson().fromJson(aCache.getAsString("clspnodata"),type);
        list=new ArrayList<CLSPNO>();
        for (CLSPNO e:datalist){
            if (e.getSign().equals(sign)){
                list.add(e);
            }
        }
        myadapter=new Myadapter(list,MyApplication.getContext());
        listView.setAdapter(myadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CaiLiaoResponse caiLiaoResponse=new CaiLiaoResponse(list.get(i));
                Intent intent=new Intent(CLNOList .this, DatailOfApply.class);
                intent.putExtra("bean",caiLiaoResponse);
                startActivity(intent);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CLNOList.this,CLReturn.class);
                intent.putExtra("sign",sign);
                intent.putExtra("userid",userid);
                startActivity(intent);

            }
        });



    }
    class Myadapter extends BaseAdapter{
        private List<CLSPNO> list;
        private LayoutInflater inflater;

        public Myadapter(List<CLSPNO> list, Context context) {
            this.list=list;
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
                view=inflater.inflate(R.layout.cl_sp_no_item,viewGroup,false);
                vh.cont=(TextView)view.findViewById(R.id.cl_no_num);
                vh.name=(TextView)view.findViewById(R.id.cl_no_name);
                vh.time=(TextView)view.findViewById(R.id.cl_no_date);
                vh.user=(TextView)view.findViewById(R.id.cl_no_user);
                view.setTag(vh);
            }else {
                vh=(ViewHolder)view.getTag();
            }
            vh.user.setText("申请者："+list.get(i).getUsername());
            vh.time.setText("日期："+list.get(i).getDatetime());
            vh.name.setText("材料名："+list.get(i).getMatname());
            vh.cont.setText("数量："+list.get(i).getMatnum());
            return view;
        }
        class ViewHolder{
            TextView name;
            TextView time;
            TextView cont;
            TextView user;
        }

        public void serdata(List<CLSPNO> list){
            this.list=list;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.getInstance().delActivity(CLNOList.class.getName());
    }
}
