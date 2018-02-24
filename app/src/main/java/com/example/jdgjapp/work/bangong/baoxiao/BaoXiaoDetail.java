package com.example.jdgjapp.work.bangong.baoxiao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jdgjapp.Bean.BaoXiaoPerson;
import com.example.jdgjapp.R;

public class BaoXiaoDetail extends AppCompatActivity {
    private TextView type,time,cont,money,status,res,passmoney;
    private GridView gridView;
    private String a[]; //存放每个图片真正的url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_xiao_detail);
        BaoXiaoPerson person=(BaoXiaoPerson)getIntent().getSerializableExtra("bean");
        type=(TextView)findViewById(R.id.bx_type);
        time=(TextView)findViewById(R.id.bx_time);
        cont=(TextView)findViewById(R.id.bx_cont);
        money=(TextView)findViewById(R.id.bx_money);
        status=(TextView)findViewById(R.id.bx_status);
        res=(TextView)findViewById(R.id.bx_res);
        passmoney=(TextView)findViewById(R.id.bx_passmoney);
        gridView=(GridView)findViewById(R.id.detail_gridView);
        type.setText("类型："+person.getAcc_kind());
        time.setText("时间："+person.getAcc_date());
        cont.setText("内容："+person.getAcc_cont());
        money.setText("报销金额："+person.getAcc_money());
        if (person.getAcc_answer()==null){
            status.setText("状态：待审核");
            res.setText("批复理由：暂无");
            passmoney.setText("批准金额：暂无");
        }else if (person.getAcc_answer().equals("1")){
            status.setText("状态：已通过");
            res.setText("批复理由："+person.getAcc_answerreason());
            passmoney.setText("批准金额："+person.getAcc_backmoney());
        }else if (person.getAcc_answer().equals("2")){
            status.setText("状态：未通过");
            res.setText("批复理由："+person.getAcc_answerreason());
            passmoney.setText("批准金额：无");
        }
        String imgpath=person.getAcc_img();
        //处理图片url
        if (imgpath == null || imgpath.isEmpty()){
            Log.d("报销详情", "该简报没有图片信息！");
        }else
        {
            //将图片url分割后放入a[]
            a = imgpath.split(";");
            for (int i = 0; i < a.length; i++) {
                String http = "http://106.14.145.208:80";
                a[i] = http + a[i];
                Log.d("报销详情", "图片url【" +i+ "】=" + a[i]);
            }
            gridView.setAdapter(new GridViewAdapter(a));

        }



    }
    //GridView适配器
    class GridViewAdapter extends BaseAdapter {
        private String listUrls[];
        private LayoutInflater inflater;

        public GridViewAdapter(String listUrls[]) {
            this.listUrls = listUrls;
            inflater = LayoutInflater.from(getApplicationContext());
        }

        public int getCount(){
            return listUrls.length;
        }
        @Override
        public String getItem(int position) {
            return listUrls[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                //convertView=inflater.inflate(R.layout.item)
                convertView = inflater.inflate(R.layout.selectitem_image, parent,false);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            if (listUrls!=null){

                Glide.with(getApplicationContext())
                        .load(listUrls[position])
                        .crossFade()
                        .centerCrop()
                        .placeholder(R.drawable.loading)
                        .animate(android.R.anim.slide_in_left)
                        .error(R.drawable.imgerror)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.image);

            }
            return convertView;
        }
        class ViewHolder {
            ImageView image;
        }
    }
}
