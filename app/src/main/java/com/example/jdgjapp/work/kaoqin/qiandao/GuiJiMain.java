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
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
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
            //drawGJ();
            drawGuiJi();
            drawMaker();
            setPOI();
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
        OverlayOptions ooPolyline = new PolylineOptions().width(12).color(0xAAFF0000).points(latLngs);
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
                if (bundle!=null){
                    final BaiduGJInfo infoUtil = (BaiduGJInfo) bundle.getSerializable("infoGJ");
                    if (infoUtil!=null) {
                        //infowindow位置
                        Double posx = Double.parseDouble(infoUtil.getPosx());
                        Double posy = Double.parseDouble(infoUtil.getPosy());
                        LatLng latLng = new LatLng(posy, posx);
                        //infowindow中的布局
                        tv = new TextView(GuiJiMain.this);
                        tv.setBackgroundResource(R.drawable.baidumap_marker);
                        tv.setPadding(20, 10, 20, 20);
                        tv.setGravity(Gravity.LEFT);
                        tv.setMaxEms(12);
                        tv.setTextColor(getResources().getColor(R.color.black_1));
                        String info = "时间："+infoUtil.getDatime()+"\n"+"地址："+infoUtil.getAddrstr();
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
                    }
                }
                return true;
            }
        });
    }

    //调整地图比例尺
    private void setPOI(){
        //经度范围-180到180，纬度范围-90到90，对所有的轨迹中的经纬度进行比较，找到最大和最小的经度，最大和最小的维度
        double minLongitude=180 ,maxLongitude=-180,minlatitude=90,maxlatitude=-90;
        List<BaiduGJInfo> baiduGJInfoList = DataSupport.findAll(BaiduGJInfo.class);
        for (BaiduGJInfo baiduGJInfo : baiduGJInfoList){
            Double posx = Double.parseDouble(baiduGJInfo.getPosx());
            Double posy = Double.parseDouble(baiduGJInfo.getPosy());
            if(minlatitude > posy) minlatitude=posy;
            if(maxlatitude < posy) maxlatitude = posy;
            if(minLongitude > posx) minLongitude = posx;
            if(maxLongitude < posx) maxLongitude = posx;
        }
        //对角线的距离，单位m
        double maxdis=DistanceUtil.getDistance(new LatLng(minlatitude,minLongitude),new LatLng(maxlatitude,maxLongitude));

        int [] zoomSize={10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 25000, 50000, 100000, 200000, 500000, 1000000, 2000000};
        int [] zoomlevel={20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3};
        //将轨迹显示在地图上，正好是标准尺的9倍。
        double mapWidth = maxdis/9;
        int dx=0;
        //找到合适的单位距离，就是稍微大一点的那个单位
        for(int i=0;i<zoomSize.length;i++){
            if(mapWidth < zoomSize[i]) {
                dx = i;
                break;
            }
        }

        MapStatus.Builder builder = new MapStatus.Builder();
        //地图中心移动到轨迹中间的地方
        builder.target(new LatLng((minlatitude+maxlatitude)/2,(minLongitude+maxLongitude)/2));
        //设置缩放级别
        builder.zoom(zoomlevel[dx]);
        //刷新地图
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

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

    private void drawGuiJi(){
        List<BaiduGJInfo> baiduGJInfoList = DataSupport.findAll(BaiduGJInfo.class);
        for (int i=0; i<baiduGJInfoList.size()-1; i++){
            String startx = baiduGJInfoList.get(i).getPosx();
            String starty = baiduGJInfoList.get(i).getPosy();
            String endx = baiduGJInfoList.get(i+1).getPosx();
            String endy = baiduGJInfoList.get(i+1).getPosy();
            Double dist = getDistance(starty,startx,endy,endx);
            if (dist<=200){   //画直线
                Log.d(TAG, "距离小于200m");
                drawZX(starty,startx,endy,endx);
            }else {   //画行车路线
                driving(starty,startx,endy,endx);
            }

        }
    }

    //两点之间画直线
    private void drawZX(String startLat,String startLon,String endLat,String endLon){
        Double starty = Double.valueOf(startLat);
        Double startx = Double.valueOf(startLon);
        Double endy = Double.valueOf(endLat);
        Double endx = Double.valueOf(endLon);

        LatLng start = new LatLng(starty,startx);
        LatLng end = new LatLng(endy,endx);

        List<LatLng> points = new ArrayList<LatLng>();
        points.add(start);
        points.add(end);

        //绘制折线
        OverlayOptions ooPolyline = new PolylineOptions().width(12)
                .color(0xAAFF0000).points(points);
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
    }

    //两点之间画行车路线
    private void driving(String startLat,String startLon,String endLat,String endLon){
       //其中startLat startLon 是起点的经纬度  endlat endlon是终点的经纬度
        RoutePlanSearch newInstance = RoutePlanSearch.newInstance();
        newInstance.setOnGetRoutePlanResultListener(new MyListener());

        //驾车路线
        DrivingRoutePlanOption drivingOption = new DrivingRoutePlanOption();
        PlanNode from = PlanNode.withLocation(new LatLng(Double.valueOf(startLat), Double.valueOf(startLon)));  //设置起点世界之窗
        PlanNode to = PlanNode.withLocation(new LatLng(Double.valueOf(endLat),Double.valueOf(endLon)));
        drivingOption.from(from);
        drivingOption.to(to);
        drivingOption.policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST); //方案:最短距离 这个自己设置 比如时间短之类的
        newInstance.drivingSearch(drivingOption);
    }

    class MyDrivingOverlay extends DrivingRouteOverlay{

        public MyDrivingOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return null;
        }
    }

    class MyListener implements OnGetRoutePlanResultListener {

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            //驾车
            if(result == null || SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error){
                Toast.makeText(getApplicationContext(), "未搜索到结果", Toast.LENGTH_LONG).show();
                return;
            }
            //开始处理结果了
            DrivingRouteOverlay overlay = new MyDrivingOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);// 把事件传递给overlay
            overlay.setData(result.getRouteLines().get(0));// 设置线路为第一条
            overlay.addToMap();
            overlay.zoomToSpan();
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult result) {
            // 公交换乘

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            // 步行

        }

    }

    /**
     * 计算两点之间距离
     * @return 米
     */
    public double getDistance(String startLat,String startLon,String endLat,String endLon){

        Double starty = Double.valueOf(startLat);
        Double startx = Double.valueOf(startLon);
        Double endy = Double.valueOf(endLat);
        Double endx = Double.valueOf(endLon);

        LatLng start = new LatLng(starty,startx);
        LatLng end = new LatLng(endy,endx);

        double lat1 = (Math.PI/180)*start.latitude;
        double lat2 = (Math.PI/180)*end.latitude;

        double lon1 = (Math.PI/180)*start.longitude;
        double lon2 = (Math.PI/180)*end.longitude;

        //地球半径
        double R = 6371;

        //两点间距离 km
        double d =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*R;

        return d*1000;
    }

}
