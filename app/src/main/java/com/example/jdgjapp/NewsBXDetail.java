package com.example.jdgjapp;

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
import com.example.jdgjapp.Bean.SystemNews;

public class NewsBXDetail extends AppCompatActivity {
    private TextView id,kind,money,time,cont;
    private GridView gridView;
    private String a[]; //存放每个图片真正的url
    private String idstring,kindstring,moneystring,timestring,contstring,gridstring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_bxdetail);
        id=(TextView)findViewById(R.id.news_bx_id);
        kind=(TextView)findViewById(R.id.news_bx_kind);
        money=(TextView)findViewById(R.id.news_bx_money);
        time=(TextView)findViewById(R.id.news_bx_time);
        cont=(TextView)findViewById(R.id.news_bx_cont);
        gridView=(GridView)findViewById(R.id.news_bx_grid);
        SystemNews e=(SystemNews) getIntent().getSerializableExtra("bean");
        String newscont=e.getContent();
        String []strs=newscont.split("@#\\$");
        idstring=strs[2];
        kindstring=strs[1];
        moneystring=strs[5];
        timestring=strs[4];
        contstring=strs[3];
        gridstring=strs[6];
        id.setText("申请者工号："+idstring);
        kind.setText("类型："+kindstring);
        money.setText("申请金额："+moneystring);
        time.setText("申请日期："+timestring);
        cont.setText("申请内容："+contstring);
        String imgpath=gridstring;
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
