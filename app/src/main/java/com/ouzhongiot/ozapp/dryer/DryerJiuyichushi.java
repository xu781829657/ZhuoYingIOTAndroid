package com.ouzhongiot.ozapp.dryer;

import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MainActivity;
import com.ouzhongiot.ozapp.others.Post;
import com.ouzhongiot.ozapp.others.SetPackage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DryerJiuyichushi extends AppCompatActivity {

    private LinearLayout ll_dryerjiuyichushi_jieguoxianshi,ll_dryerjiuyichushi_tianjiayiwu;
    private TextView tv_dryerjiuyichushi_yifushuliang,tv_dryerjiuyichushi_yujishijian;
    private String offtime;
    private SharedPreferences preference2,preference3;
    private SharedPreferences.Editor editor2,editor3;
    private String workmachineid;
    private int MOD=100;
    private long finishtime = 0;
    private long starttime = 0;
    private int alltime = 0;
    private int jiuyichushi = 300;
    private  int yifushuliang = 0;

    Handler handler = new Handler(){


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 1:
                    editor3.putBoolean("fSwitch", true);
                    editor3.commit();
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                    break;
                case 2:
                    DryerIndex.notfinish = false;
                    DryerIndex.notruning = true;
                    Date dt= new Date();
                    starttime = dt.getTime();
                    if (yifushuliang == 1)
                    {
                        alltime = jiuyichushi;
                    }else
                    if(yifushuliang>1)
                    {
                        alltime = jiuyichushi;
                        for(int i = 1;i<yifushuliang;i++){
                            alltime = (int)(alltime+jiuyichushi*0.3);
                        }
                    }

                    finishtime = starttime+alltime*1000;

                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setTimeInMillis(finishtime);
                    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Log.wtf("这个是结束时间",format.format(gc.getTime()).substring(11,16));
                    offtime = format.format(gc.getTime()).substring(11,16);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String uriAPI3 = MainActivity.ip + "smarthome/dryer/jobTask";
                            List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                            params3.add(new BasicNameValuePair("devSn", workmachineid));
                            params3.add(new BasicNameValuePair("task.fSwitchOn", "false"));
                            params3.add(new BasicNameValuePair("task.fSwitchOff", "true"));
                            params3.add(new BasicNameValuePair("task.onJobTime", ""));
                            params3.add(new BasicNameValuePair("task.offJobTime", offtime));
                            String str3 = Post.dopost(uriAPI3, params3);


                        }
                    }).start();


                    if(alltime!=0){
                        editor3.putLong("dryerfinishtime",finishtime);
                        editor3.putInt("mod",2);
                        editor3.commit();
                    }

                    onBack();
                    break;
                default:
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dryer_jiuyichushi);

        preference2 = getSharedPreferences("data", MODE_PRIVATE);
        editor2 = preference2.edit();
        workmachineid = preference2.getString("workmachineid", "");
        preference3 = getSharedPreferences(workmachineid, MODE_PRIVATE);
        editor3 = preference3.edit();

        ll_dryerjiuyichushi_jieguoxianshi = (LinearLayout) findViewById(R.id.ll_dryerjiuyichushi_jieguoxianshi);
        ll_dryerjiuyichushi_tianjiayiwu = (LinearLayout) findViewById(R.id.ll_dryerjiuyichushi_tianjiayiwu);
        tv_dryerjiuyichushi_yifushuliang = (TextView) findViewById(R.id.tv_dryerjiuyichushi_yifushuliang);
        tv_dryerjiuyichushi_yujishijian = (TextView) findViewById(R.id.tv_dryerjiuyichushi_yujishijian);

        ll_dryerjiuyichushi_tianjiayiwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPickerDialog();
            }
        });
        //重新选择
        this.findViewById(R.id.tv_dryerjiuyichushi_guanbimoshi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_dryerjiuyichushi_tianjiayiwu.setVisibility(View.VISIBLE);
                ll_dryerjiuyichushi_jieguoxianshi.setVisibility(View.GONE);
            }
        });

        //返回
        this.findViewById(R.id.iv_dryerjiuyichushi_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        //开启模式
        this.findViewById(R.id.tv_dryerjiuyichushi_kaiqimoshi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!preference3.getBoolean("fSwitch",false))
                {
                    byte[] order = {0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetDryerOrderC1(workmachineid,preference2.getString("workmachinetype", ""),order));
                }
                else
                {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }
        });

    }

    public  void showHourPickerDialog(){
        NumberPicker mPicker = new NumberPicker(DryerJiuyichushi.this);
        mPicker.setMinValue(0);
        mPicker.setMaxValue(24);
        mPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                // TODO Auto-generated method stub

                String hour1 = "";
                if (newVal<10){
                    hour1 = "0"+newVal;
                }else {
                    hour1 = newVal+"";
                }

                yifushuliang = newVal;
                tv_dryerjiuyichushi_yifushuliang.setText(hour1+" 件");

                Date dt= new Date();
                starttime = dt.getTime();
                if (newVal == 1)
                {
                    alltime = jiuyichushi;
                }else
                if(newVal>1)
                {
                    alltime = jiuyichushi;
                    for(int i = 1;i<newVal;i++){
                        alltime = (int)(alltime+jiuyichushi*0.3);
                    }
                }

                finishtime = starttime+alltime*1000;

                int hour = alltime/3600;
                int min =  (alltime-hour*3600)/60;
                int seconds = (alltime-hour*3600-min*60);
                String hourstr = "00";
                String minstr = "00";
                String secndsstr = "00";
                if(hour<10)
                {
                    hourstr = "0"+hour;
                }else
                {
                    hourstr = ""+hour;
                }
                if(min<10)
                {
                    minstr = "0"+min;
                }else
                {
                    minstr = ""+min;
                }
                tv_dryerjiuyichushi_yujishijian.setText(hourstr+"时"+minstr+"分");


                ll_dryerjiuyichushi_tianjiayiwu.setVisibility(View.GONE);
                ll_dryerjiuyichushi_jieguoxianshi.setVisibility(View.VISIBLE);

            }
        });


        AlertDialog mAlertDialog = new AlertDialog.Builder(DryerJiuyichushi.this)
                .setTitle("添加衣物数量").setView(mPicker).setPositiveButton("确定",null).create();
        mAlertDialog.show();
    }

    //点击返回上个页面
    public void onBack() {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                } catch (Exception e) {
                    Log.e("Exception when onBack", e.toString());
                }
            }
        }.start();
    }

    @Override
    protected void onStart() {
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction("broadcass1");            //添加动态广播的Action
        registerReceiver(receiver, dynamic_filter);// 注册自定义动态广播消息
        super.onStart();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("broadcass1")) {    //动作检测
                String receivedata = intent.getStringExtra("msg");
                if (MainActivity.b[14]==0x01) {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

}
