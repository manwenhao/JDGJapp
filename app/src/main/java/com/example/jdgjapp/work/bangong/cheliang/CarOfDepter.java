package com.example.jdgjapp.work.bangong.cheliang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jdgjapp.Bean.CarOfPerson;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ACache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CarOfDepter extends AppCompatActivity {
    private ListView listView;
    private AdapterOfPerson adapterOfPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_of_depter);
        listView=(ListView)findViewById(R.id.car_depter_listview);
        final String flag=getIntent().getStringExtra("flag");
        final String userid=getIntent().getStringExtra("userid");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getid());
                String datestring=aCache.getAsString(flag);
                Type type=new TypeToken<List<com.example.jdgjapp.Bean.CarOfDepter>>(){}.getType();
                List<com.example.jdgjapp.Bean.CarOfDepter> datelist=new Gson().fromJson(datestring,type);
                final List<CarOfPerson> list=new ArrayList<CarOfPerson>();
                for (com.example.jdgjapp.Bean.CarOfDepter e:datelist){
                    if (e.getUsr_id().equals(userid)){
                        CarOfPerson person=new CarOfPerson();
                        person.setCar_color(e.getCar_color());
                        person.setCar_id(e.getCar_id());
                        person.setCar_name(e.getCar_name());
                        person.setUsr_name(e.getUsr_name());
                        list.add(person);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterOfPerson=new AdapterOfPerson(list,MyApplication.getContext());
                        listView.setAdapter(adapterOfPerson);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent=new Intent(MyApplication.getContext(),DetailofCar.class);
                                intent.putExtra("bean",list.get(i));
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        }).start();

    }
}
