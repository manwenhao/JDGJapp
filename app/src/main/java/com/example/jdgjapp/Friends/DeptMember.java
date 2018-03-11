package com.example.jdgjapp.Friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jdgjapp.Bean.Friend;
import com.example.jdgjapp.Bean.User;
import com.example.jdgjapp.FriendInfo;
import com.example.jdgjapp.MyApplication;
import com.example.jdgjapp.R;
import com.example.jdgjapp.Util.ACache;
import com.example.jdgjapp.Util.ActivityUtils;
import com.example.jdgjapp.Util.ReturnUsrDep;
import com.example.jdgjapp.work.bangong.gongdan.TransmitList;
import com.example.jdgjapp.work.bangong.shipin.ShiPinMain;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.model.ConstantApp;
import com.example.jdgjapp.work.bangong.shipin.agora.openvcall.ui.ChatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;

public class DeptMember extends AppCompatActivity {
    private List<Friend> friendList;
    private String dept_id;
    private List<User> userList;
    private String deptname;
    private ImageView back;
    private ListView mListView;
    private EditText etSearch;
    private ImageView ivClearText;
    private SideBar sideBar;
    private TextView dialog;
    private List<SortModel> mAllContactsList;
    private ContactsSortAdapter adapter;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    public static int flag=0;
    private TextView ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_member);
        ActivityUtils.getInstance().addActivity(DeptMember.class.getName(),this);
        dept_id=getIntent().getStringExtra("deptid");
        deptname=getIntent().getStringExtra("deptname");
        back=(ImageView)findViewById(R.id.deptmember_back);
        etSearch=(EditText)findViewById(R.id.et_search2);
        ivClearText=(ImageView)findViewById(R.id.ivClearText2);
        ok=(TextView)findViewById(R.id.shipin_depter_ok);
        if (flag==1||flag==2){
            ok.setVisibility(View.VISIBLE);
        }
        if (flag==1){
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ShiPinMain.useridList.size()==0){
                        Toast.makeText(DeptMember.this, "请选择视频人员", Toast.LENGTH_SHORT).show();
                    }else if(ShiPinMain.useridList.size()>9){
                        Toast.makeText(DeptMember.this, "邀请视频人员不得超过9人，请重试", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Log.d("邀请人员id", ShiPinMain.useridList.toString());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder builder=new StringBuilder();
                                builder.append(";");
                                for (String name:ShiPinMain.useridList){
                                    builder.append(name);
                                    builder.append(";");
                                }
                                String sendlist=builder.toString();
                                final String chanel= MyApplication.getid()+";"+new Date().toString();
                                User u= ReturnUsrDep.returnUsr();
                                String sender=MyApplication.getid()+";"+u.getUsr_name();
                                Log.d("list===",sendlist);
                                OkHttpUtils.post()
                                        .url("http://106.14.145.208:80//JDGJ/SendVideoPush")
                                        .addParams("usr_sender",sender)
                                        .addParams("usr_ids",sendlist)
                                        .addParams("chanel",chanel)
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                Log.d("视频会议出错",e.toString());
                                            }

                                            @Override
                                            public void onResponse(String response, int id) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ActivityUtils.getInstance().delActivity(ShiPinMain.class.getName());
                                                        ActivityUtils.getInstance().delActivity(MyDeptMent.class.getName());
                                                        ActivityUtils.getInstance().delActivity(DeptMember.class.getName());
                                                        ((MyApplication)MyApplication.getContext()).initWorkerThread();
                                                        Intent intent=new Intent(MyApplication.getContext(), ChatActivity.class);
                                                        intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,chanel);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        });

                            }
                        }).start();
                    }

                }
            });
        }
        if (flag==2){
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TransmitList.click();
                }
            });
        }
        mListView=(ListView)findViewById(R.id.lv_contacts2);
        dialog=(TextView)findViewById(R.id.dialog2);
        sideBar=(SideBar)findViewById(R.id.sidrbar2);
        sideBar.setTextView(dialog);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        friendList=new ArrayList<Friend>();
        Log.d("选择部门id",dept_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ACache aCache= ACache.get(MyApplication.getContext(), MyApplication.getid());
                String depters=aCache.getAsString("depterlist");
                Log.d("选择部门成员",depters);
                Type type=new TypeToken<List<User>>(){}.getType();
                List<User> list=new Gson().fromJson(depters,type);
                for (User u:list){
                    if (u.getUsr_deptId().equals(dept_id)){
                        Friend friend=new Friend();
                        friend.setId(u.getUsr_id());
                        friend.setName(u.getUsr_name());
                        friend.setUsr_addr(u.getUsr_addr());
                        friend.setUsr_birth(u.getUsr_birth());
                        friend.setUsr_bossId(u.getUsr_bossId());
                        friend.setUsr_phone(u.getUsr_phone());
                        friend.setUsr_sex(u.getUsr_sex());
                        friend.setUsr_dept(deptname);
                        friendList.add(friend);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        init();
                    }
                });


            }
        }).start();
    }
    public void init(){
        initView();
        initListener();
        loadContacts();
    }
    public void initView(){
        characterParser = CharacterParser.getInstance();
        mAllContactsList = new ArrayList<SortModel>();
        pinyinComparator = new PinyinComparator();
        Collections.sort(mAllContactsList, pinyinComparator);// 根据a-z进行排序源数据
        adapter = new ContactsSortAdapter(MyApplication.getContext(), mAllContactsList);
        mListView.setAdapter(adapter);
    }
    public void initListener(){
        /** 清除输入字符 **/
        ivClearText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable e) {

                String content = etSearch.getText().toString();
                if ("".equals(content)) {
                    ivClearText.setVisibility(View.INVISIBLE);
                } else {
                    ivClearText.setVisibility(View.VISIBLE);
                }
                if (content.length() > 0) {
                    ArrayList<SortModel> fileterList = (ArrayList<SortModel>)search(content);
                    adapter.updateListView(fileterList);
                    // mAdapter.updateData(mContacts);
                } else {
                    adapter.updateListView(mAllContactsList);
                }
                mListView.setSelection(0);

            }

        });

        // 设置右侧[A-Z]快速导航栏触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }
            }
        });
        // item事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                ContactsSortAdapter.ViewHolder viewHolder = (ContactsSortAdapter.ViewHolder) view.getTag();
                viewHolder.cbChecked.performClick();
                adapter.toggleChecked(position);
                if (flag!=1&&flag!=2&&ContactsSortAdapter.flag==0){
                    Intent intent=new Intent(MyApplication.getContext(), FriendInfo.class);
                    intent.putExtra("user_id",mAllContactsList.get(position).getId());
                    startActivity(intent);
                }
            }
        });
    }
    public void loadContacts(){
        Log.d("开始解析","哈哈哈哈");
        for (Friend friend:friendList){
            String id=friend.getId();
            String tal=friend.getUsr_phone();
            String name=friend.getName();
            String deptname=friend.getUsr_dept();
            String sortkey=name;
            SortModel sm=new SortModel(id,name,tal,deptname,sortkey);
            Log.d("name",name);
            String sortletter=getSortLetter(name);
            sm.sortLetters=sortletter;
            sm.sortToken=parseSortKey(sortkey);
            mAllContactsList.add(sm);
        }
        Collections.sort(mAllContactsList, pinyinComparator);
        adapter.updateListView(mAllContactsList);
    }


    private List<SortModel> search(String str) {
        List<SortModel> filterList = new ArrayList<SortModel>();// 过滤后的list
        // if (str.matches("^([0-9]|[/+])*$")) {// 正则表达式 匹配号码
        if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式
            // 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            String simpleStr = str.replaceAll("\\-|\\s", "");
            for (SortModel contact : mAllContactsList) {
                if (contact.number != null && contact.name != null) {
                    if (contact.simpleNumber.contains(simpleStr) || contact.name.contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        } else {
            for (SortModel contact : mAllContactsList) {
                if (contact.number != null && contact.name != null) {
                    // 姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                    if (contact.name.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))
                            || contact.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "")
                            .contains(str.toLowerCase(Locale.CHINESE))
                            || contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE))
                            || contact.sortToken.wholeSpell.toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE))) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        }
        return filterList;
    }
    String chReg = "[\\u4E00-\\u9FA5]+";// 中文字符串匹配

    // String chReg="[^\\u4E00-\\u9FA5]";//除中文外的字符匹配
    /**
     * 解析sort_key,封装简拼,全拼
     *
     * @param sortKey
     * @return
     */
    public SortToken parseSortKey(String sortKey) {
        SortToken token = new SortToken();
        if (sortKey != null && sortKey.length() > 0) {
            // 其中包含的中文字符
            String[] enStrs = sortKey.replace(" ", "").split(chReg);
            for (int i = 0, length = enStrs.length; i < length; i++) {
                if (enStrs[i].length() > 0) {
                    // 拼接简拼
                    token.simpleSpell += enStrs[i].charAt(0);
                    token.wholeSpell += enStrs[i];
                }
            }
        }
        return token;
    }

    /**
     * 取sort_key的首字母
     *
     * @param sortKey
     * @return
     */
    private String getSortLetterBySortKey(String sortKey) {
        if (sortKey == null || "".equals(sortKey.trim())) {
            return null;
        }
        String letter = "#";
        // 汉字转换成拼音
        String sortString = sortKey.trim().substring(0, 1).toUpperCase(Locale.CHINESE);
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        }
        return letter;
    }
    /**
     * 名字转拼音,取首字母
     *
     * @param name
     * @return
     */
    private String getSortLetter(String name) {
        String letter = "#";
        if (name == null) {
            return letter;
        }
        // 汉字转换成拼音
        // String pinyin = characterParser.getSelling(name);
        String pinyin = PinYin.getPinYin(name);
        Log.i("main", "pinyin:" + pinyin);
        String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        }
        return letter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.getInstance().delActivity(DeptMember.class.getName());
    }
}
