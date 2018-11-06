package com.example.jdgjapp.work.kaoqin.tongji;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.Bean.KaoQing;
import com.example.jdgjapp.Bean.TongjiDays;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;

/**
 * author : chen
 * date   : 2018/10/30  19:07
 * desc   :
 */
public class kaoqingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "kaoqingActivity";
    private LinearLayout ll_chooseDate;
    private PieChart pChart;
    private TextView tv_date;
    private TextView tv_cqtime;
    private TextView tv_cctime;
    private TextView tv_qjtime;
    private TextView tv_cdtime;
    private TextView tv_zttime;
    private TextView tv_qqtime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaoqing);
        initView();

    }

    private void initView() {
        ll_chooseDate = (LinearLayout) findViewById(R.id.ll_chooseDate);
        ll_chooseDate.setOnClickListener(this);
        tv_date = (TextView) findViewById(R.id.tv_date);
        pChart = (PieChart) findViewById(R.id.pieChart);
        tv_cdtime = (TextView) findViewById(R.id.tv_cdtime);
        tv_zttime = (TextView) findViewById(R.id.tv_zttime);
        tv_cqtime = (TextView) findViewById(R.id.tv_cqtime);
        tv_cctime = (TextView) findViewById(R.id.tv_cctime);
        tv_qjtime = (TextView) findViewById(R.id.tv_qjtime);
        tv_cdtime = (TextView) findViewById(R.id.tv_cdtime);
        tv_zttime = (TextView) findViewById(R.id.tv_zttime);
        tv_qqtime = (TextView) findViewById(R.id.tv_qqtime);
        inintChart();
    }

    private void inintChart() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        String str_year = String.valueOf(year);
        String str_month = String.valueOf(month);
       tv_date.setText(str_year+"年"+str_month+"月");
        Log.e(TAG,ReturnUsrDep.returnUsr().getUsr_id()+str_year+str_month);
        OkHttpUtils.get()
                .addParams("user_id", ReturnUsrDep.returnUsr().getUsr_id())
                .addParams("year", str_year)
                .addParams("month", str_month)
                .url("http://106.14.145.208/JDGJ/BackAppKqCount")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        Log.e(TAG,response.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                List<KaoQing> list = gson.fromJson(response, new TypeToken<List<KaoQing>>() {
                                }.getType());
                               for(KaoQing kaoQing:list) {
                                   Integer cq = Integer.parseInt(kaoQing.getCq());
                                   Integer cc = Integer.parseInt(kaoQing.getCc());
                                   Integer qj = Integer.parseInt(kaoQing.getQj());
                                   Integer cd = Integer.parseInt(kaoQing.getCd());
                                   Integer zt = Integer.parseInt(kaoQing.getZt());
                                   Integer qq = Integer.parseInt(kaoQing.getQq());
                                   initChart(cq, cc, qj, cd, zt, qq);
                                   tv_cqtime.setText(kaoQing.getCq());
                                   tv_cctime.setText(kaoQing.getCc());
                                   tv_qjtime.setText(kaoQing.getQj());
                                   tv_qqtime.setText(kaoQing.getQq());
                                   tv_cdtime.setText(kaoQing.getCd());
                                   tv_zttime.setText(kaoQing.getZt());
                               }

                            }
                        });
                    }
                });
    }


    //设置数据
    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(2f);//饼图区块之间的距离
        dataSet.setSelectionShift(5f);//

        //数据和颜色
        Integer[] colors = new Integer[]{Color.parseColor("#d87a80"), Color.parseColor("#2ec7c9"), Color.parseColor("#b6a2de"),
                Color.parseColor("#5ab1ef"), Color.parseColor("#ffb980"), Color.parseColor("#8d98b3")};
        //添加对应的颜色值
        List<Integer> colorSum = new ArrayList<>();
        for (Integer color : colors) {
            colorSum.add(color);
        }
        dataSet.setColors(colorSum);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        pChart.setData(data);
        pChart.highlightValues(null);//在给定的数据集中突出显示给定索引的值
        //刷新
        pChart.invalidate();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_chooseDate:
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dlg = new DatePickerDialog(new ContextThemeWrapper(kaoqingActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        tv_date.setText(year + "年" + (month + 1) + "月");
                        int newmonth = month + 1;
                        initChart(year, newmonth);
                        // Toast.makeText(getApplication(), "年月为" + year + (month + 1), Toast.LENGTH_SHORT).show();
                    }
                }, yy, mm, dd) {
                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        LinearLayout mSpinners = (LinearLayout) findViewById(getContext().getResources().getIdentifier("android:id/pickers", null, null));
                        if (mSpinners != null) {
                            NumberPicker mMonthSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/month", null, null));
                            NumberPicker mYearSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/year", null, null));
                            mSpinners.removeAllViews();
                            if (mMonthSpinner != null) {
                                mSpinners.addView(mMonthSpinner);
                            }
                            if (mYearSpinner != null) {
                                mSpinners.addView(mYearSpinner);
                            }
                        }
                        View dayPickerView = findViewById(getContext().getResources().getIdentifier("android:id/day", null, null));
                        if (dayPickerView != null) {
                            dayPickerView.setVisibility(View.GONE);
                        }
                    }


                };
                dlg.show();
                break;

            default:
                break;
        }

    }

    private void initChart(int year, int newmonth) {
        OkHttpUtils.get()
                .addParams("user_id", ReturnUsrDep.returnUsr().getUsr_id())
                .addParams("year", String.valueOf(year))
                .addParams("month", String.valueOf(newmonth))
                .url("http://106.14.145.208/JDGJ/BackAppKqCount")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Gson gson = new Gson();
                                List<KaoQing> list = gson.fromJson(response, new TypeToken<List<KaoQing>>() {
                                }.getType());
                                Log.e(TAG,response);
                                for(KaoQing kaoQing:list) {
                                    Integer cq = Integer.parseInt(kaoQing.getCq());
                                    Integer cc = Integer.parseInt(kaoQing.getCc());
                                    Integer qj = Integer.parseInt(kaoQing.getQj());
                                    Integer cd = Integer.parseInt(kaoQing.getCd());
                                    Integer zt = Integer.parseInt(kaoQing.getZt());
                                    Integer qq = Integer.parseInt(kaoQing.getQq());
                                    initChart(cq, cc, qj, cd, zt, qq);
                                    tv_cqtime.setText(kaoQing.getCq());
                                    tv_cctime.setText(kaoQing.getCc());
                                    tv_qjtime.setText(kaoQing.getQj());
                                    tv_qqtime.setText(kaoQing.getQq());
                                    tv_cdtime.setText(kaoQing.getCd());
                                    tv_zttime.setText(kaoQing.getZt());
                                }
                            }
                        });
                    }
                });
    }


    public void initChart(int cq, int cc, int qj, int cd, int zt, int qq) {
        pChart.setUsePercentValues(true);//设置为TRUE的话，图标中的数据自动变为percent
        pChart.getDescription().setEnabled(false);
        pChart.setExtraOffsets(5, 10, 5, 5);//设置额外的偏移量(在图表视图周围)

        pChart.setDragDecelerationFrictionCoef(0.95f);//设置滑动减速摩擦系数，在0~1之间
        //设置中间文件
        // pieChart.setCenterText(generateCenterSpannableText());
        pChart.setDrawSliceText(false);//设置隐藏饼图上文字，只显示百分比
        pChart.setDrawHoleEnabled(false);//设置为TRUE时，饼中心透明
        pChart.setHoleColor(Color.WHITE);//设置饼中心颜色

        pChart.setTransparentCircleColor(Color.WHITE);//透明的圆
        pChart.setTransparentCircleAlpha(110);//透明度

        pChart.setHoleRadius(30f);//中间圆的半径占总半径的百分数
        // pieChart.setHoleRadius(0);//实心圆
        //pieChart.setTransparentCircleRadius(61f);//// 半透明圈

        pChart.setDrawCenterText(true);//绘制显示在饼图中心的文本

        pChart.setRotationAngle(0);//设置一个抵消RadarChart的旋转度
        // 触摸旋转
        pChart.setRotationEnabled(false);//通过触摸使图表旋转
        pChart.setHighlightPerTapEnabled(true);//通过点击手势突出显示的值


        //变化监听
        // pieChart.setOnChartValueSelectedListener(this);
        //模拟数据
        float all = (float) cq + cc + qj + cd + zt + qq;
        Log.e(TAG,"数字："+all);
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int f = 0;
        if (all != 0.0) {
            Log.e(TAG,"ALL!=0");
            a = (int) ((cq / all) * 100);
            b = (int) ((cc / all) * 100);
            c = (int) ((qj / all) * 100);
            d = (int) ((cd / all) * 100);
            e = (int) ((zt / all) * 100);
            f = (int) ((qq / all) * 100);
        } else {
            Log.e(TAG,"log==0");
            Toast.makeText(kaoqingActivity.this, "暂无数据", Toast.LENGTH_SHORT);
            f = 100;
        }
        Log.e(TAG, a + "--" + b + "--" + c + "--" + d + "--" + e + "--" + f + "-----" + all);
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        if (a != 0) {
            entries.add(new PieEntry(a, "出勤"));
        }
        if (f != 0) {
            entries.add(new PieEntry(f, "缺勤"));
        }
        if (b != 0) {
            entries.add(new PieEntry(b, "出差"));
        }
        if (c != 0) {
            entries.add(new PieEntry(c, "请假"));
        }
        if (d != 0) {
            entries.add(new PieEntry(d, "迟到"));
        }
        if (e != 0) {
            entries.add(new PieEntry(e, "早退"));
        }

        //设置数据
        setData(entries);
        pChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = pChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // 输入标签样式
        pChart.setEntryLabelColor(Color.WHITE);
        pChart.setEntryLabelTextSize(12f);
        /**
         * 设置比例图
         */
        Legend mLegend = pChart.getLegend();
        mLegend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_CENTER);  //在左边中间显示比例图
        mLegend.setFormSize(14f);//比例块字体大小 
        mLegend.setXEntrySpace(4f);//设置距离饼图的距离，防止与饼图重合
        mLegend.setYEntrySpace(4f);
        //设置比例块换行...
        mLegend.setWordWrapEnabled(true);
        mLegend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);//设置字跟图表的左右顺序

        //mLegend.setTextColor(getResources().getColor(R.color.alpha_80));
        mLegend.setForm(Legend.LegendForm.SQUARE);//设置比例块形状，默认为方块
//        mLegend.setEnabled(false);//设置禁用比例块
    }


}
