package com.ouzhongiot.ozapp.dryer;

import android.app.Instrumentation;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MainActivity;
import com.ouzhongiot.ozapp.others.Post;
import com.ouzhongiot.ozapp.others.SetPackage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DryerZidingyi extends AppCompatActivity {

    private LinearLayout ll_dryerzidingyi_jieguoxianshi,ll_dryerzidingyi_tianjiayiwu;
    private TextView tv_dryerzidingyi_hongganshichang;
    private String offtime;
    private SharedPreferences preference2,preference3;
    private SharedPreferences.Editor editor2,editor3;
    private String workmachineid;
    private int MOD=100;
    private long finishtime = 0;
    private int alltime = 0;


    Handler  handler = new Handler(){


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
//                        Date dt= new Date();
//                        finishtime = dt.getTime()+alltime;

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
                   if(alltime!=0)
                   {
                       editor3.putLong("dryerfinishtime",finishtime);
                       editor3.putInt("mod",3);
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
        setContentView(R.layout.activity_dryer_zidingyi);

        preference2 = getSharedPreferences("data", MODE_PRIVATE);
        editor2 = preference2.edit();
        workmachineid = preference2.getString("workmachineid", "");
        preference3 = getSharedPreferences(workmachineid, MODE_PRIVATE);
        editor3 = preference3.edit();



        ll_dryerzidingyi_jieguoxianshi = (LinearLayout) findViewById(R.id.ll_dryerzidingyi_jieguoxianshi);
        ll_dryerzidingyi_tianjiayiwu = (LinearLayout) findViewById(R.id.ll_dryerzidingyi_tianjiayiwu);
        tv_dryerzidingyi_hongganshichang = (TextView) findViewById(R.id.tv_dryerzidingyi_hongganshichang);


        ll_dryerzidingyi_tianjiayiwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialogKaiqi();
            }
        });
        //重新选择
        this.findViewById(R.id.tv_dryerzidingyi_guanbimoshi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_dryerzidingyi_tianjiayiwu.setVisibility(View.VISIBLE);
                ll_dryerzidingyi_jieguoxianshi.setVisibility(View.GONE);
            }
        });

        //返回
        this.findViewById(R.id.iv_dryerzidingyi_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        //开启模式
        this.findViewById(R.id.tv_dryerzidingyi_kaiqimoshi).setOnClickListener(new View.OnClickListener() {
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

    //日期选择
    public void showTimePickerDialogKaiqi() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int hour = 0;
        int minute = 0;

        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker arg0, int hour, int minute) {
                String hour1 = "";
                String minute1 = "";
                if (hour<10){
                    hour1 = "0"+hour;
                }else
                {
                    hour1 = hour+"";
                }
                if (minute<10){
                    minute1 = "0"+minute;
                }else
                {
                    minute1 = minute+"";
                }
                tv_dryerzidingyi_hongganshichang.setText(hour1+"时"+minute1+"分");
                alltime = (hour*3600+minute*60)*1000;
                Date dt= new Date();
                finishtime = dt.getTime()+alltime;
                ll_dryerzidingyi_tianjiayiwu.setVisibility(View.GONE);
                ll_dryerzidingyi_jieguoxianshi.setVisibility(View.VISIBLE);
            }

        }, hour, minute, true);
        dialog.show();
    }

//    public  void showHourPickerDialog(){
//        NumberPicker mPicker = new NumberPicker(DryerZidingyi.this);
//        mPicker.setMinValue(0);
//        mPicker.setMaxValue(24);
//        mPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
//            {
//                // TODO Auto-generated method stub
//
//                String hour = "";
//                if (newVal<10){
//                    hour = "0"+newVal;
//                }else {
//                    hour = newVal+"";
//                }
//
//
//                ll_dryerzidingyi_tianjiayiwu.setVisibility(View.GONE);
//                ll_dryerzidingyi_jieguoxianshi.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//
//        AlertDialog mAlertDialog = new AlertDialog.Builder(DryerZidingyi.this)
//                .setTitle("添加衣物数量").setView(mPicker).setPositiveButton("确定",null).create();
//        mAlertDialog.show();
//    }

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
