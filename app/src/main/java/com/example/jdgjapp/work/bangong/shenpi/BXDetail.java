package com.example.jdgjapp.work.bangong.shenpi;

import android.content.Intent;
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
import com.example.jdgjapp.Bean.BXSPNO;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ActivityUtils;

public class BXDetail extends AppCompatActivity {
    private TextView start,id,name,cont,type,time,money;
    private GridView gridView;
    private String a[]; //存放每个图片真正的url
    private String acc_id;
    private String acc_userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bxdetail);
        ActivityUtils.getInstance().addActivity(BXDetail.class.getName(),this);
        BXSPNO e=(BXSPNO)getIntent().getSerializableExtra("bean");
        acc_id=e.getAcc_id();
        acc_userid=e.getAcc_userid();
        start=(TextView)findViewById(R.id.sp_bx_no_into);
        id=(TextView)findViewById(R.id.bx_no_userid);
        name=(TextView)findViewById(R.id.bx_no_username);
        cont=(TextView)findViewById(R.id.bx_no_cont);
        type=(TextView)findViewById(R.id.bx_no_type);
        time=(TextView)findViewById(R.id.bx_no_time);
        money=(TextView)findViewById(R.id.bx_no_money);
        gridView=(GridView)findViewById(R.id.bx_no_gridView);
        id.setText("工号："+e.getAcc_userid());
        name.setText("姓名："+e.getAcc_username());
        cont.setText("申请内容："+e.getAcc_cont());
        type.setText("报销类型："+e.getAcc_kind());
        time.setText("时间："+e.getAcc_date());
        money.setText("报销金额："+e.getAcc_money());
        String imgpath=e.getAcc_img();
        //处理图片url
        if (imgpath == null || imgpath.isEmpty()){
            Log.d("报销详情", "该简报没有图片信息！");
        }else
        {
            //将图片url分割后放入a[]
            a = imgpath.split(";");
            for (int i = 0; i < a.length; i++) {
                String http = "http://106.14.145.208:8080";
                a[i] = http + a[i];
                Log.d("报销详情", "图片url【" +i+ "】=" + a[i]);
            }
            gridView.setAdapter(new GridViewAdapter(a));

        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BXDetail.this,BXSPReturn.class);
                intent.putExtra("acc_id",acc_id);
                intent.putExtra("acc_userid",acc_userid);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.getInstance().delActivity(BXDetail.class.getName());
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
