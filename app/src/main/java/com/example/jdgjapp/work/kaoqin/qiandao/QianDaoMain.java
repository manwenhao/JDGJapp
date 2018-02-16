package com.example.jdgjapp.work.kaoqin.qiandao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.jdgjapp.Bean.BaiduGJInfo;
import com.example.jdgjapp.Bean.BaiduMakerInfo;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.example.jdgjapp.work.kaoqin.daka.DaKaMain;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class QianDaoMain extends AppCompatActivity {

    private static final String TAG = "QianDaoMain";
    private ImageView menu;
    private TopRightMenu mTopRightMenu;
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private View infoView;
    private SelfDialog selfDialog;
    private List<BaiduMakerInfo> baiduMakerInfos;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qian_dao_main);
        menu = (ImageView) findViewById(R.id.caidan);
        mapView = (MapView)findViewById(R.id.mapview);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        //显示员工最新位置
        sendResquestNewLoc(ReturnUsrDep.returnUsr().getUsr_id(),ReturnUsrDep.returnUsr().getUsr_deptId());

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initMenu();
            }
        });

    }

    private void sendResquestNewLoc(final String userid, final String deptid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/BackAppDeptNewestLocate")
                        .addParams("user_id",userid)
                        .addParams("dept_id",deptid)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d(TAG, "最新位置获取失败");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(QianDaoMain.this,"请求失败，请稍后再试！",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(TAG, "最新位置获取成功"+response);

                                if ("[]".equals(response) || TextUtils.isEmpty(response)){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(QianDaoMain.this,"暂无数据！",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    parseJsonNewLoc(response);
                                    List<BaiduMakerInfo> list = DataSupport.findAll(BaiduMakerInfo.class);
                                    addOverlay(list);
                                }

                            }
                        });

            }
        }).start();
    }

    private void parseJsonNewLoc(String response){
        Gson gson = new Gson();
        List<BaiduMakerInfo> list = gson.fromJson(response,new TypeToken<List<BaiduMakerInfo>>(){}.getType());
        DataSupport.deleteAll(BaiduMakerInfo.class);
        for (BaiduMakerInfo baiduMakerInfo : list){
            BaiduMakerInfo baiduMakerInfo0 = new BaiduMakerInfo();
            baiduMakerInfo0.setUser_id(baiduMakerInfo.getUser_id());
            baiduMakerInfo0.setUser_name(baiduMakerInfo.getUser_name());
            baiduMakerInfo0.setDatime(baiduMakerInfo.getDatime());
            baiduMakerInfo0.setPosx(baiduMakerInfo.getPosx());
            baiduMakerInfo0.setPosy(baiduMakerInfo.getPosy());
            baiduMakerInfo0.setDep_id(baiduMakerInfo.getDep_id());
            baiduMakerInfo0.setDep_name(baiduMakerInfo.getDep_name());
            baiduMakerInfo0.save();
        }
    }

    private void initMenu(){
        mTopRightMenu = new TopRightMenu(QianDaoMain.this);
            List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.mipmap.fresh,"刷新定位"));
        menuItems.add(new MenuItem(R.mipmap.chazhao,"查找员工"));
        mTopRightMenu
                .setHeight(300)
                .showIcon(true)
                .dimBackground(true)
                .needAnimationStyle(true)
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        switch (position){
                            case 0:
                                sendResquestNewLoc(ReturnUsrDep.returnUsr().getUsr_id(),ReturnUsrDep.returnUsr().getUsr_deptId());
                                break;
                            case 1:
                                selfDialog = new SelfDialog(QianDaoMain.this);
                                selfDialog.setYesOnclickListener(new SelfDialog.onYesOnclickListener() {
                                    @Override
                                    public void onYesClick() {
                                        baiduMakerInfos = DataSupport.where("user_id = ?",selfDialog.getMessage()).find(BaiduMakerInfo.class);
                                        if (baiduMakerInfos==null || baiduMakerInfos.size()==0){
                                            baiduMakerInfos = DataSupport.where("user_name = ?",selfDialog.getMessage()).find(BaiduMakerInfo.class);
                                        }
                                        if (baiduMakerInfos==null || baiduMakerInfos.size()==0){
                                            Toast.makeText(QianDaoMain.this,"未查找到此人！",Toast.LENGTH_SHORT).show();
                                        }else {
                                            addOverlay(baiduMakerInfos);
                                        }
                                        selfDialog.dismiss();
                                    }
                                });
                                selfDialog.setNoOnclickListener(new SelfDialog.onNoOnclickListener() {
                                    @Override
                                    public void onNoClick() {
                                        selfDialog.dismiss();
                                    }
                                });
                                selfDialog.show();
                                break;
                            default:break;
                        }
                    }
                })
                .showAsDropDown(menu, 100, 35);

    }

    //显示marker
    public void addOverlay(final List<BaiduMakerInfo> infos) {
        //清空地图
        mBaiduMap.clear();
        //地图点击事件
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }
            @Override
            public void onMapClick(LatLng arg0) {
                mBaiduMap.hideInfoWindow();
            }
        });
        //创建marker的显示图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        LatLng latLng = null;
        Marker marker;
        OverlayOptions options;
        for(BaiduMakerInfo info:infos){
            Double posx = Double.parseDouble(info.getPosx());
            Double posy = Double.parseDouble(info.getPosy());
            latLng = new LatLng(posy,posx);
            //设置marker
            options = new MarkerOptions()
                    .position(latLng)
                    .icon(bitmap)
                    .zIndex(9);
            //添加marker
            marker = (Marker) mBaiduMap.addOverlay(options);
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
        //将地图显示在最后一个marker的位置
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
        //marker监听
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //从marker中获取info信息
                Bundle bundle = marker.getExtraInfo();
                final BaiduMakerInfo infoUtil = (BaiduMakerInfo) bundle.getSerializable("info");
                //infowindow位置
                Double posx = Double.parseDouble(infoUtil.getPosx());
                Double posy = Double.parseDouble(infoUtil.getPosy());
                LatLng latLng0 = new LatLng(posy, posx);
                //infowindow监听
                InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {

                    }
                };
                //显示infowindow
                InfoWindow infoWindow = new InfoWindow(getInfoWindowView(marker), latLng0, -47);
                mBaiduMap.showInfoWindow(infoWindow);
                return true;
            }
        });

    }

    private View getInfoWindowView(Marker marker){
        if (infoView == null){
            infoView = (ViewGroup) LayoutInflater.from(QianDaoMain.this).inflate(R.layout.infowindow, null);
        }
        //从marker中获取info信息
        Bundle bundle = marker.getExtraInfo();
        final BaiduMakerInfo infoUtil = (BaiduMakerInfo) bundle.getSerializable("info");
        TextView tv = (TextView)infoView.findViewById(R.id.info);
        Button btn = (Button)infoView.findViewById(R.id.btn);
        String infos = "工号："+infoUtil.getUser_id()+"\n"
                +"姓名："+infoUtil.getUser_name()+"\n"
                +"时间："+infoUtil.getDatime()+"\n"
                +"部门编号："+infoUtil.getDep_id()+"\n"
                +"部门："+infoUtil.getDep_name();
        tv.setText(infos);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        username = infoUtil.getUser_name();
                        sendRequestGJ(infoUtil.getUser_id());
                    }
                });

            }
        });

        return infoView;
    }

    private void sendRequestGJ(final String userid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        .url("http://106.14.145.208:8080/JDGJ/BackAppUserSsPathById")
                        .addParams("user_id",userid)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d(TAG, "运动轨迹获取失败"+e);
                                Toast.makeText(QianDaoMain.this,"请求失败，请稍后再试！",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(TAG, "运动轨迹获取成功"+response);

                                if ("[]".equals(response) || TextUtils.isEmpty(response)){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(QianDaoMain.this,"此员工暂无轨迹！",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    parseJsonGJ(response);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(MyApplication.getContext(),GuiJiMain.class);
                                            intent.putExtra("username",username);
                                            startActivity(intent);
                                        }
                                    });
                                }

                            }
                        });
            }
        }).start();
    }

    private void parseJsonGJ(String response){
        Gson gson = new Gson();
        List<BaiduGJInfo> list = gson.fromJson(response,new TypeToken<List<BaiduGJInfo>>(){}.getType());
        DataSupport.deleteAll(BaiduGJInfo.class);
        for (BaiduGJInfo baiduGJInfo : list){
            BaiduGJInfo baiduGJInfo1 = new BaiduGJInfo();
            baiduGJInfo1.setPosx(baiduGJInfo.getPosx());
            baiduGJInfo1.setPosy(baiduGJInfo.getPosy());
            baiduGJInfo1.setDatime(baiduGJInfo.getDatime());
            baiduGJInfo1.save();
        }
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }
}
