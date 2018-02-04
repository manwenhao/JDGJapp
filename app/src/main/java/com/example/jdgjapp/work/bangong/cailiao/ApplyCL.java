package com.example.jdgjapp.work.bangong.cailiao;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.Bean.ApplyCaiLiao;
import com.example.jdgjapp.Bean.CaiLiaoOfApply;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

import okhttp3.Call;

public class ApplyCL extends AppCompatActivity {
    private TextView ok;
    private ListView listView;
    private List<ApplyCaiLiao> datalist=new ArrayList<ApplyCaiLiao>();
    private EditText reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_cl);
        ok=(TextView)findViewById(R.id.apply_cailiao_ok);
        listView=(ListView)findViewById(R.id.cailiao_apply_listview);
        reason=(EditText)findViewById(R.id.apply_cailiao_reason);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/BackGSMaterialleft")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("部门材料申请",e.toString());
                            }
                            @Override
                            public void onResponse(String response, int id) {
                                Log.d("部门材料申请",response);
                                Type type=new TypeToken<List<CaiLiaoOfApply>>(){}.getType();
                                final List<CaiLiaoOfApply> list=new Gson().fromJson(response,type);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Myadapter myadapter=new Myadapter(MyApplication.getContext(),list);
                                        listView.setAdapter(myadapter);
                                    }
                                });
                            }
                        });
            }
        }).start();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("发送的申请材料",datalist.toString());
                        String reasonstring=reason.getText().toString();
                        if (reasonstring.equals("")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ApplyCL.this, "请输入申请原因", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else if (datalist.size()==0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ApplyCL.this, "申请材料不可为空，请选择材料及数量", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            String data=new Gson().toJson(datalist,new TypeToken<List<ApplyCaiLiao>>(){}.getType());
                            Date date=new Date();
                            SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String time=sf.format(date);
                            String matsign=MyApplication.getid()+time;
                            Log.d("部门材料申请","matjson: "+data+" datetime: "+time+" reason: "+reasonstring+" matsign: "+matsign);
                            OkHttpUtils.post()
                                    .url("http://106.14.145.208:8080/JDGJ/RecordMaterialReq")
                                    .addParams("matjson",data)
                                    .addParams("datetime",time)
                                    .addParams("reason",reasonstring)
                                    .addParams("user_id",MyApplication.getid())
                                    .addParams("matsign",matsign)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            Log.d("部门材料申请",e.toString());
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            Log.d("部门材料申请",response);
                                            if (response.equals("ok")){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(ApplyCL.this, "申请成功！", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
                                            }else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(ApplyCL.this, "申请失败，请重试！", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });

                        }
                    }
                }).start();
            }
        });
    }
    class Myadapter extends BaseAdapter{
        private List<CaiLiaoOfApply> list;
        private LayoutInflater inflater;
        private ViewHolder viewHolder;
        public Myadapter(Context context,List<CaiLiaoOfApply> list) {
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final ViewHolder vh;
            if (view==null){
                vh=new ViewHolder();
                view=inflater.inflate(R.layout.cailiao_apply_item,viewGroup,false);
                vh.name=(TextView)view.findViewById(R.id.apply_cailiao_name);
                vh.num=(TextView)view.findViewById(R.id.apply_cailiao_number);
                vh.checkBox=(CheckBox)view.findViewById(R.id.apply_cailiao_check);
                vh.apply=(EditText)view.findViewById(R.id.apply_cailiao_num);
                view.setTag(vh);
                viewHolder=vh;
            }else {
                vh=(ViewHolder)view.getTag();
                viewHolder=vh;
            }
            vh.name.setText("材料名"+list.get(i).getMat_name());
            if (list.get(i).getMat_num().equals("0")){
                vh.num.setText("再无库存");
                vh.checkBox.setFocusable(false);

                vh.checkBox.setClickable(false);
                vh.apply.setFocusable(false);
                vh.apply.setFocusableInTouchMode(false);
            }else {
                vh.num.setText("材料库存:"+list.get(i).getMat_num());
               // final String apply=vh.apply.getText().toString();
                vh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        String apply=vh.apply.getText().toString();
                        if (b){
                            vh.apply.setFocusable(false);
                            vh.apply.setFocusableInTouchMode(false);
                            if (apply==null||apply.equals("")){
                                compoundButton.setChecked(false);
                                vh.apply.setFocusable(true);
                                vh.apply.setFocusableInTouchMode(true);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ApplyCL.this, "申请数量不能为空，请重试", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else if (!isNUmber(apply)){
                                compoundButton.setChecked(false);
                                vh.apply.setFocusable(true);
                                vh.apply.setFocusableInTouchMode(true);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ApplyCL.this, "申请数量需为数字，请重试", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }else if (Integer.parseInt(apply)<=0||Integer.parseInt(apply)>Integer.parseInt(list.get(i).getMat_num())){
                                compoundButton.setChecked(false);
                                vh.apply.setFocusable(true);
                                vh.apply.setFocusableInTouchMode(true);
                                vh.apply.setFocusable(true);
                                vh.apply.setFocusableInTouchMode(true);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ApplyCL.this, "申请数量需在库存范围职能，请重试", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }else {
                                ApplyCaiLiao caiLiao=new ApplyCaiLiao();
                                caiLiao.setId(list.get(i).getMat_id());
                                caiLiao.setNum(apply);
                                boolean flag=true;
                                for (ApplyCaiLiao caiLiao1: datalist){
                                    if (caiLiao1.getId().equals(caiLiao.getId())){
                                        flag=false;
                                    }
                                }
                                if (flag){
                                    datalist.add(caiLiao);
                                }
                            }

                        }else {
                            vh.apply.setFocusable(true);
                            vh.apply.setFocusableInTouchMode(true);
                            ApplyCaiLiao caiLiao=new ApplyCaiLiao();
                            caiLiao.setId(list.get(i).getMat_id());
                            caiLiao.setNum(apply);
                            boolean flag=false;
                            int a=0;
                            for (int i=0;i<datalist.size();i++){
                                if (datalist.get(i).getId().equals(caiLiao.getId())){
                                    flag=true;
                                    a=i;
                                }
                            }
                            if (flag){
                                datalist.remove(a);
                            }
                        }
                    }
                });
            }
            return view;
        }
        class ViewHolder{
            TextView name;
            TextView num;
            CheckBox checkBox;
            EditText apply;
        }
    }
    public boolean isNUmber(String str){
        Pattern pattern=Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
