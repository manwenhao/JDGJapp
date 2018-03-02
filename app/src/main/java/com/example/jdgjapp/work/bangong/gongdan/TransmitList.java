package com.example.jdgjapp.work.bangong.gongdan;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.AddressListFragment;
import com.example.jdgjapp.Bean.Task;
import com.example.jdgjapp.Bean.TaskReport;
import com.example.jdgjapp.Friends.ContactsSortAdapter;
import com.example.jdgjapp.Friends.DeptMember;
import com.example.jdgjapp.Friends.MyDeptMent;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ActivityUtils;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.example.jdgjapp.work.bangong.shipin.ShiPinMain;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TransmitList extends AppCompatActivity {

    private static final String TAG = "TransmitList";
    private AddressListFragment fragment;
    public static List<String> useridList=new ArrayList<String>();//转发人员的id集合
    private TextView ok;
    public static String taskid;
    public static final int UPDATE = 1;
    public static final String action1 = "transmit.action";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit_list);
        ActivityUtils.getInstance().addActivity(TransmitList.class.getName(),this);
        ok=(TextView)findViewById(R.id.shipin_ok3);
        DeptMember.flag=2;
        fragment=(AddressListFragment) getFragmentManager().findFragmentById(R.id.select_friend3);
        ContactsSortAdapter.flag=2;
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click();
            }
        });

        //获取传递过来的taskid
        Intent intent = getIntent();
        taskid = intent.getStringExtra("taskid");

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ContactsSortAdapter.flag=0;
        useridList=new ArrayList<String>();
        DeptMember.flag=0;
        ActivityUtils.getInstance().delActivity(TransmitList.class.getName());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ContactsSortAdapter.flag=0;
            useridList=new ArrayList<String>();
            DeptMember.flag=0;
        }
        return super.onKeyDown(keyCode, event);
    }

    //ok的点击事件
    public static void click(){
        Log.d("我选择的转发人员",TransmitList.useridList.toString());
        Log.d(TAG, "转发的taskid = " + taskid);

        //下面写具体的点击事件
        if (useridList.size()==0){
            Toast.makeText(MyApplication.getContext(),"请选择要转发的对象！",Toast.LENGTH_SHORT).show();
        }else if (useridList.size()>=2){
            Toast.makeText(MyApplication.getContext(),"只能选择一个转发对象！",Toast.LENGTH_SHORT).show();
        }else {
            String id = useridList.get(0);
            sendRequestTransmitList(MyApplication.getContext(),ReturnUsrDep.returnUsr().getUsr_id(),id,taskid);

            ActivityUtils.getInstance().delActivity(MyDeptMent.class.getName());
            ActivityUtils.getInstance().delActivity(DeptMember.class.getName());
            ActivityUtils.getInstance().delActivity(TransmitList.class.getName());


        }
    }


    private static void sendRequestTransmitList(final Context context,final String meid, final String youid, final String workid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("meid", meid)
                            .add("youid", youid)
                            .add("workid", workid)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://106.14.145.208:80/JDGJ/ReSendWorkOrder")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseDate = response.body().string();

                    if (responseDate.equals("ok")){
                        //修改转发工单状态为已完成
                        Task task = new Task();
                        task.setStatus("3");
                        task.updateAll("taskid = ?",taskid);

                        NotReceivedTaskInfoActivity.instance.finish();

                        Intent intent = new Intent(action1);
                        context.sendBroadcast(intent);

                        Looper.prepare();
                        Toast.makeText(MyApplication.getContext(),"转发工单成功！",Toast.LENGTH_SHORT).show();
                        Looper.loop();


                    }
                    else{
                        Looper.prepare();
                        Toast.makeText(MyApplication.context,"转发工单失败！",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
