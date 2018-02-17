package com.example.jdgjapp.work.kaoqin.qiandao;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.jdgjapp.Bean.BaiduGJInfo;
import com.example.jdgjapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class GuiJiMain extends AppCompatActivity {

    private static final String TAG = "GuiJiMain";
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private TextView title;
    private GeoCoder geoCoder;
    private TextView tv;
    private String addr;
    private int flag;

    private LatLng startLatLng;//轨迹起点
    private BitmapDescriptor bitmapDescriptor;
    List<LatLng> latLngs = new ArrayList<LatLng>();
    LatLng target;
    Polyline mPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_ji_main);
        mMapView = (MapView) findViewById(R.id.mapview_gj);
        title = (TextView) findViewById(R.id.title);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        geoCoder = GeoCoder.newInstance();

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        title.setText(username+"的轨迹");

        List<BaiduGJInfo> baiduGJInfoList = DataSupport.findAll(BaiduGJInfo.class);
        if (baiduGJInfoList.size()==1){
            drawMaker();
        }else {
            drawGJ();
            drawMaker();
        }

    }



    //画轨迹
    private void drawGJ(){
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
        //获取轨迹上的每个点
        double lanSum = 0;
        double lonSum = 0;
        List<BaiduGJInfo> baiduGJInfoList = DataSupport.findAll(BaiduGJInfo.class);
        for (BaiduGJInfo baiduGJInfo : baiduGJInfoList){
            Double posx = Double.parseDouble(baiduGJInfo.getPosx());
            Double posy = Double.parseDouble(baiduGJInfo.getPosy());
            LatLng latLng = new LatLng(posy, posx);
            latLngs.add(latLng);
            lanSum += latLng.latitude;
            lonSum += latLng.longitude;
        }
        //设置地图的缩放中心点为所有点的几何中心点
        target = new LatLng(lanSum/latLngs.size(), lonSum/latLngs.size());

        //设置缩放中点LatLng target，和缩放比例
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(target).zoom(15f);
        //地图设置缩放状态
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        OverlayOptions ooPolyline = new PolylineOptions().width(13).color(0xAAFF0000).points(latLngs);
        //在地图上画出线条图层，mPolyline：线条图层
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        mPolyline.setZIndex(9);

    }

    private void drawMaker(){
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
        List<BaiduGJInfo> baiduGJInfoList = DataSupport.findAll(BaiduGJInfo.class);
        //画轨迹上的Marker
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        BitmapDescriptor bitmap1 = BitmapDescriptorFactory.fromResource(R.drawable.start);
        BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.end);
        LatLng latLng = null;
        Marker marker;
        OverlayOptions options;
        for (int i=0; i<baiduGJInfoList.size(); i++){
            Double posx = Double.parseDouble(baiduGJInfoList.get(i).getPosx());
            Double posy = Double.parseDouble(baiduGJInfoList.get(i).getPosy());
            latLng = new LatLng(posy,posx);
            //设置marker
            if (i==0){    //起点
                startLatLng = latLng;
                options = new MarkerOptions()
                        .position(latLng)
                        .icon(bitmap1)
                        .zIndex(9);
                //添加marker
                marker = (Marker) mBaiduMap.addOverlay(options);
                Bundle bundle = new Bundle();
                bundle.putSerializable("infoGJ", baiduGJInfoList.get(0));
                marker.setExtraInfo(bundle);
            }else if (i==baiduGJInfoList.size()-1){   //终点
                options = new MarkerOptions()
                        .position(latLng)
                        .icon(bitmap2)
                        .zIndex(9);
                //添加marker
                marker = (Marker) mBaiduMap.addOverlay(options);
                Bundle bundle = new Bundle();
                bundle.putSerializable("infoGJ", baiduGJInfoList.get(baiduGJInfoList.size()-1));
                marker.setExtraInfo(bundle);
            }else {
                options = new MarkerOptions()
                        .position(latLng)
                        .icon(bitmap)
                        .zIndex(9);
                //添加marker
                marker = (Marker) mBaiduMap.addOverlay(options);
                Bundle bundle = new Bundle();
                bundle.putSerializable("infoGJ", baiduGJInfoList.get(i));
                marker.setExtraInfo(bundle);
            }
        }
        //将地图显示在起点的位置
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(startLatLng);
        mBaiduMap.setMapStatus(msu);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //从marker中获取info信息
                Bundle bundle = marker.getExtraInfo();
                final BaiduGJInfo infoUtil = (BaiduGJInfo) bundle.getSerializable("infoGJ");
                //infowindow位置
                Double posx = Double.parseDouble(infoUtil.getPosx());
                Double posy = Double.parseDouble(infoUtil.getPosy());
                LatLng latLng = new LatLng(posy, posx);
                //infowindow中的布局
                tv = new TextView(GuiJiMain.this);
                tv.setBackgroundResource(R.drawable.baidumap_marker);
                tv.setPadding(20, 10, 20, 20);
                tv.setGravity(Gravity.LEFT);
                tv.setTextColor(getResources().getColor(R.color.black_1));
                String info = "时间："+infoUtil.getDatime();
                tv.setText(info);
                bitmapDescriptor = BitmapDescriptorFactory.fromView(tv);
                //infowindow监听
                InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        mBaiduMap.hideInfoWindow();
                    }
                };
                InfoWindow infoWindow = new InfoWindow(bitmapDescriptor, latLng, -47, listener);
                mBaiduMap.showInfoWindow(infoWindow);
                return true;
            }
        });
    }

    private void latlngToAddress(LatLng latlng) {
        flag=1;
        // 设置反地理经纬度坐标,请求位置时,需要一个经纬度
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
        //设置地址或经纬度反编译后的监听,这里有两个回调方法,
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            //经纬度转换成地址
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (flag==1){
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                }else {
                    addr = result.getAddress();
                    flag=0;
                }
                }
                Log.d(TAG, "addris0"+addr);
            }

            //把地址转换成经纬度
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                // 详细地址转换在经纬度
                String address=result.getAddress();
            }
        });

    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        geoCoder.destroy();
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }
}
