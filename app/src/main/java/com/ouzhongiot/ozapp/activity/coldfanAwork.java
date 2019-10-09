package com.ouzhongiot.ozapp.activity;


import android.app.Instrumentation;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.LoginOutActivity;
import com.ouzhongiot.ozapp.others.Post;
import com.ouzhongiot.ozapp.others.SetPackage;
import com.ouzhongiot.ozapp.Model.ColdfanAdingshi;
import com.ouzhongiot.ozapp.Model.Lishijilu;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class coldfanAwork extends LoginOutActivity{
    private NetworkInfo netInfo;
    private ConnectivityManager mConnectivityManager;
    private SeekBar seekBar;
    private int side;
    private int moshi;
    private int fengshu;
    private OutputStream outputStream;
    private ImageView iv_coldfanawork_kaiguan;
    private ImageView iv_coldfanawork_ziranfeng, iv_coldfanawork_shuimianfeng, iv_coldfanawork_kongjin, iv_coldfanawork_zhengchangfeng;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences2;
    private SharedPreferences.Editor editor2;
    private SharedPreferences preferences3;
    private SharedPreferences.Editor editor3;
    private boolean ison;
    private ImageView iv_coldfanAzhileng, iv_coldfanAbaifeng, iv_coldfanAshajun, iv_coldfanAtongsuo;
    private String workmachineid;
    private String workmachinetype;
    private boolean fSwitch;
    private int fMode;
    private int fWind;
    private boolean fSwing;
    private boolean fUV;
    private boolean fState;
    private String Msg;
    private TextView tv_zhuangtai_moshi,tv_zhuangtai_fengshu,tv_zhuangtai_baifeng,tv_zhuangtai_zhileng;
    private LinearLayout ll_coldfanawork_dingshi;
    private TextView tv_coldfanawork_dingshiquxiao,tv_coldfanawork_dingshikaiqi,tv_coldfanawork_dingshiguanbi;
    private TextView tv_coldfanawork_kaiqishijian;
    private TextView tv_coldfanawork_dingshi1,tv_coldfanawork_dingshi2,tv_coldfanawork_dingshi3,tv_coldfanawork_dingshi4,tv_coldfanawork_dingshi5,
                     tv_coldfanawork_dingshi6,tv_coldfanawork_dingshi7;
    private String onJobTime;
    private String offJobTime;
    private String durTime;
    private String runWeek;
    private Boolean  fSwitchOn;
    private Boolean  fSwitchOff;
    private String taskfMode;
    private String taskfWind;
    private TextView tv_dingshi[];
    private List<Lishijilu> lishijilu;
// 历史记录   图表
    private LineChartView lineChart;
    String[] date ;//X轴的标注
    int[] score ;//图表的数据点
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private LinearLayout ll_coldfanawork_lishibiaoge;
    private LinearLayout llcoldfanawork_kongzhi;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 检查为开启状态
                case 1:
                    iv_coldfanawork_kaiguan.setImageResource(R.mipmap.open);
                    fSwitch = true;
                    traversalView(llcoldfanawork_kongzhi);
                    editor3.putBoolean("fSwitch", fSwitch);
                    editor3.commit();
                    break;
                case 2:
                    iv_coldfanawork_kaiguan.setImageResource(R.mipmap.close_30);
                    fSwitch = false;
                    traversalView(llcoldfanawork_kongzhi);
                    editor3.putBoolean("fSwitch", fSwitch);
                    editor3.commit();
                    break;
                case 3:
                    iv_coldfanawork_zhengchangfeng.setImageResource(R.mipmap.zhengchangfeng0);
                    iv_coldfanawork_ziranfeng.setImageResource(R.mipmap.ziranfeng);
                    iv_coldfanawork_shuimianfeng.setImageResource(R.mipmap.sleep);
                    tv_zhuangtai_moshi.setText("正常风");
                    fMode = 1;
                    editor3.putInt("fMode", fMode);
                    editor3.commit();
                    break;
                case 4:
                    iv_coldfanawork_zhengchangfeng.setImageResource(R.mipmap.zhengchangfeng);
                    iv_coldfanawork_ziranfeng.setImageResource(R.mipmap.ziranfeng0);
                    iv_coldfanawork_shuimianfeng.setImageResource(R.mipmap.sleep);
                    tv_zhuangtai_moshi.setText("自然风");
                    fMode = 2;
                    editor3.putInt("fMode", fMode);
                    editor3.commit();
                    break;
                case 5:
                    iv_coldfanawork_zhengchangfeng.setImageResource(R.mipmap.zhengchangfeng);
                    iv_coldfanawork_ziranfeng.setImageResource(R.mipmap.ziranfeng);
                    iv_coldfanawork_shuimianfeng.setImageResource(R.mipmap.sleep0);
                    tv_zhuangtai_moshi.setText("睡眠风");
                    fMode = 3;
                    editor3.putInt("fMode", fMode);
                    editor3.commit();
                    break;
                case 6:
                    seekBar.setProgress(0);
                    tv_zhuangtai_fengshu.setText("低速");
                    fWind = 1;
                    editor3.putInt("fWind", fWind);
                    editor3.commit();
                    break;
                case 7:
                    seekBar.setProgress(50);
                    tv_zhuangtai_fengshu.setText("中速");
                    fWind = 2;
                    editor3.putInt("fWind", fWind);
                    editor3.commit();
                    break;
                case 8:
                    seekBar.setProgress(100);
                    tv_zhuangtai_fengshu.setText("高速");
                    fWind = 3;
                    editor3.putInt("fWind", fWind);
                    editor3.commit();
                    break;
                case 9:
                    iv_coldfanAbaifeng.setImageResource(R.mipmap.opengongneng);
                    tv_zhuangtai_baifeng.setText("开启");
                    fSwing = true;
                    editor3.putBoolean("fSwing", fSwing);
                    editor3.commit();
                    break;
                case 10:
                    iv_coldfanAbaifeng.setImageResource(R.mipmap.closegongneng);
                    tv_zhuangtai_baifeng.setText("关闭");
                    fSwing = false;
                    editor3.putBoolean("fSwing", fSwing);
                    editor3.commit();
                    break;
                case 11:
                    iv_coldfanAshajun.setImageResource(R.mipmap.opengongneng);
                    fUV = true;
                    editor3.putBoolean("fUV", fUV);
                    editor3.commit();
                    break;
                case 12:
                    iv_coldfanAshajun.setImageResource(R.mipmap.closegongneng);
                    fUV = false;
                    editor3.putBoolean("fUV", fUV);
                    editor3.commit();
                    break;
                case 13:
                    iv_coldfanAzhileng.setImageResource(R.mipmap.opengongneng);
                    tv_zhuangtai_zhileng.setText("开启");
                    fState = true;
                    editor3.putBoolean("fState", fState);
                    editor3.commit();
                    break;
                case 14:
                    iv_coldfanAzhileng.setImageResource(R.mipmap.closegongneng);
                    tv_zhuangtai_zhileng.setText("关闭");
                    fState = false;
                    editor3.putBoolean("fState", fState);
                    editor3.commit();
                    break;
                case 15:
                    for(int i = 0;i<7;i++){
                        tv_dingshi[i].setVisibility(View.INVISIBLE);
                    }
                    tv_coldfanawork_dingshikaiqi.setText("未设置");
                    tv_coldfanawork_dingshiguanbi.setText("未设置");
                    tv_coldfanawork_kaiqishijian.setText("00");

                    break;
                case 16:
                    ll_coldfanawork_dingshi.setVisibility(View.GONE);
                    break;
                case 17:

                    getAxisXLables();//获取x轴的标注
                    getAxisPoints();//获取坐标点
                    initLineChart();//初始化
                    break;
                case 18:

                    break;
                default:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coldfan_awork);
        outputStream = ((OZApplication)getApplication()).outputStream;
        llcoldfanawork_kongzhi = (LinearLayout) findViewById(R.id.llcoldfanawork_kongzhi);
        lineChart = (LineChartView)findViewById(R.id.chart);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        ll_coldfanawork_lishibiaoge = (LinearLayout) findViewById(R.id.ll_coldfanawork_lishibiaoge);
        iv_coldfanawork_zhengchangfeng = (ImageView) findViewById(R.id.iv_coldfanawork_zhengchangfeng);
        iv_coldfanawork_kaiguan = (ImageView) findViewById(R.id.iv_coldfanawork_kaiguan);
        iv_coldfanawork_ziranfeng = (ImageView) findViewById(R.id.iv_coldfanawork_ziranfeng);
//        iv_coldfanawork_kongjin = (ImageView) findViewById(R.id.iv_coldfanawork_kongjin);
        iv_coldfanawork_shuimianfeng = (ImageView) findViewById(R.id.iv_coldfanawork_shuimianfeng);
        iv_coldfanAzhileng = (ImageView) findViewById(R.id.iv_coldfanAzhileng);
        iv_coldfanAbaifeng = (ImageView) findViewById(R.id.iv_coldfanAbaifeng);
        iv_coldfanAshajun = (ImageView) findViewById(R.id.iv_coldfanAshajun);
        tv_zhuangtai_moshi = (TextView) findViewById(R.id.tv_zhuangtai_moshi);
        tv_zhuangtai_fengshu = (TextView) findViewById(R.id.tv_zhuangtai_fengshu);
        tv_zhuangtai_baifeng = (TextView) findViewById(R.id.tv_zhuangtai_baifeng) ;
        tv_zhuangtai_zhileng = (TextView) findViewById(R.id.tv_zhuangtai_zhileng);
        ll_coldfanawork_dingshi = (LinearLayout) findViewById(R.id.ll_coldfanawork_dingshi);
        tv_coldfanawork_dingshiquxiao = (TextView) findViewById(R.id.tv_coldfanawork_dingshiquxiao);
        tv_coldfanawork_dingshikaiqi = (TextView) findViewById(R.id.tv_coldfanawork_dingshikaiqi);
        tv_coldfanawork_dingshiguanbi = (TextView) findViewById(R.id.tv_coldfanawork_dingshiguanbi);
        tv_coldfanawork_kaiqishijian = (TextView) findViewById(R.id.tv_coldfanawork_kaiqishijian);
        tv_coldfanawork_dingshi1 = (TextView) findViewById(R.id.tv_coldfanawork_dingshi1);
        tv_coldfanawork_dingshi2 = (TextView) findViewById(R.id.tv_coldfanawork_dingshi2);
        tv_coldfanawork_dingshi3 = (TextView) findViewById(R.id.tv_coldfanawork_dingshi3);
        tv_coldfanawork_dingshi4 = (TextView) findViewById(R.id.tv_coldfanawork_dingshi4);
        tv_coldfanawork_dingshi5 = (TextView) findViewById(R.id.tv_coldfanawork_dingshi5);
        tv_coldfanawork_dingshi6 = (TextView) findViewById(R.id.tv_coldfanawork_dingshi6);
        tv_coldfanawork_dingshi7 = (TextView) findViewById(R.id.tv_coldfanawork_dingshi7);



        preference = getSharedPreferences("coldfanA", MODE_PRIVATE);
        editor = preference.edit();
        LinearLayout viewSnsLayout = (LinearLayout) findViewById(R.id.coldfanAwork);
        viewSnsLayout.setLongClickable(true);


        preferences2 = getSharedPreferences("data", MODE_PRIVATE);
        editor2 = preferences2.edit();
        workmachineid = preferences2.getString("workmachineid", "");
        workmachinetype = preferences2.getString("workmachinetype", "");

        preferences3 = getSharedPreferences(workmachineid, MODE_PRIVATE);
        editor3 = preferences3.edit();
        fSwitch = preferences3.getBoolean("fSwitch", false);
        fUV = preferences3.getBoolean("fUV", false);
        fState = preferences3.getBoolean("fState", false);
        fSwing = preferences3.getBoolean("fSwing", false);
        fWind = preferences3.getInt("fWind", 1);
        fMode = preferences3.getInt("fMode", 1);
//定时状态
        onJobTime = preferences3.getString("onJobTime","未设置");
       offJobTime  = preferences3.getString("offJobTime","未设置");
        durTime = preferences3.getString("durTime","00");
        runWeek = preferences3.getString("runWeek","0000000");
        fSwitchOn = preferences3.getBoolean("fSwitchOn",false);
       fSwitchOff = preferences3.getBoolean("fSwitchOff",false);
       taskfMode = "2";
        taskfWind = "2";


        //初始化定时状态
      tv_coldfanawork_dingshikaiqi.setText(onJobTime);
        tv_coldfanawork_dingshiguanbi.setText(offJobTime);
        tv_coldfanawork_kaiqishijian.setText(durTime);
      TextView a[] = {tv_coldfanawork_dingshi1,tv_coldfanawork_dingshi2,tv_coldfanawork_dingshi3,tv_coldfanawork_dingshi4,
               tv_coldfanawork_dingshi5,tv_coldfanawork_dingshi6,tv_coldfanawork_dingshi7};
      tv_dingshi = a;
        for(int i = 0;i<7;i++){
            if (runWeek.substring(i,i+1).equals("0")){
                   tv_dingshi[i].setVisibility(View.INVISIBLE);
            }else
            {
                tv_dingshi[i].setVisibility(View.VISIBLE);
            }
        }


        Log.wtf("~!~!~~~~~~~~~模式 风速", ison + " " + moshi + " " + fengshu);
        //模式正常风
        this.findViewById(R.id.iv_coldfanawork_zhengchangfeng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*M1*#");
//                MainActivity.out.flush();

//                try {
//                    ((OZApplication)getApplication()).getOutputStream().write();
//                    ((OZApplication)getApplication()).getOutputStream().flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"M",(byte)0x31));

            }
        });
        //模式自然风
        this.findViewById(R.id.iv_coldfanawork_ziranfeng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*M2*#");
//                MainActivity.out.flush();
//                try {
//                    ((OZApplication)getApplication()).getOutputStream().write();
//                    ((OZApplication)getApplication()).getOutputStream().flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"M",(byte)0x32));

            }
        });
        //模式睡眠
        this.findViewById(R.id.iv_coldfanawork_shuimianfeng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*M3*#");
//                MainActivity.out.flush();
//                try {
//                    ((OZApplication)getApplication()).getOutputStream().write();
//                    ((OZApplication)getApplication()).getOutputStream().flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"M",(byte)0x33));
            }
        });
//        //模式空净
//        this.findViewById(R.id.iv_coldfanawork_kongjin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                iv_coldfanawork_ziranfeng.setImageResource(R.mipmap.ziranfeng);
//                iv_coldfanawork_shuimianfeng.setImageResource(R.mipmap.sleep);
//                iv_coldfanawork_kongjin.setImageResource(R.mipmap.airclean0);
//                moshi = 3;
//                editor.putInt("moshi",moshi);
//                editor.commit();
//            }
//        });


        //开关
        this.findViewById(R.id.iv_coldfanawork_kaiguan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fSwitch) {
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*S0*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"S",(byte)0x30));
                } else {
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*S1*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"S",(byte)0x31));
                }
            }
        });

        //制冷
        iv_coldfanAzhileng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fState) {
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*C0*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"C",(byte)0x30));
                } else {
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*C1*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"C",(byte)0x31));
                }
            }
        });

        //摆风
        iv_coldfanAbaifeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fSwing) {
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*R0*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"R",(byte)0x30));
                } else {
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*R1*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"R",(byte)0x31));
                }
            }
        });

        //杀菌
        iv_coldfanAshajun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fUV) {
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*U0*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"U",(byte)0x30));
                } else {
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*U1*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"U",(byte)0x31));
                }
            }
        });



        //风速
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                side = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (side < 25) {
                    seekBar.setProgress(0);
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*W1*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"W",(byte)0x31));
                } else if (side < 75) {
                    seekBar.setProgress(50);
                    fengshu = 2;
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*W2*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"W",(byte)0x32));
                } else {
                    seekBar.setProgress(100);
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*W3*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"W",(byte)0x33));
                }

            }
        });



        //定时预约
        this.findViewById(R.id.ll_coldfanawork_dingshiyuyue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_coldfanawork_dingshi.getVisibility()==View.GONE){
                            ll_coldfanawork_dingshi.setVisibility(View.VISIBLE);
                }else
                {
                    ll_coldfanawork_dingshi.setVisibility(View.GONE);
                }
            }
        });

        tv_coldfanawork_dingshiquxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String uriAPI = MainActivity.ip + "smarthome/fan/jobTask";
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                        params.add(new BasicNameValuePair("devSn",workmachineid));
                        params.add(new BasicNameValuePair("task.fSwitchOn","false"));
                        params.add(new BasicNameValuePair("task.fSwitchOff","false"));
                        params.add(new BasicNameValuePair("task.onJobTime","00:00"));
                        params.add(new BasicNameValuePair("task.offJobTime","00:00"));
                        params.add(new BasicNameValuePair("task.fMode",taskfMode));
                        params.add(new BasicNameValuePair("task.fWind",taskfWind));
                        params.add(new BasicNameValuePair("task.durTime","00"));
                        params.add(new BasicNameValuePair("task.runWeek","0000000"));
                        String str = Post.dopost(uriAPI, params);
                        if(ColdfanAdingshi.objectFromData(str).getState()==0){
                            editor3.putString("onJobTime","未设置");
                            editor3.putString("offJobTime","未设置");
                            editor3.putString("durTime","00");
                            editor3.putString("runWeek","0000000");
                            editor3.putBoolean("fSwitchOn",false);
                            editor3.putBoolean("fSwitchOff",false);
                            editor3.commit();
                            Message msg = new Message();
                            msg.what = 15;
                            handler.sendMessage(msg);
                        }

                    }
                }).start();
            }
        });

        //设定
        this.findViewById(R.id.tv_coldfanawork_dingshisheding).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                               new Thread(new Runnable() {
                                   @Override
                                   public void run() {

                                       String uriAPI = MainActivity.ip + "smarthome/fan/jobTask";
                                       List<NameValuePair> params = new ArrayList<NameValuePair>();
                                       params.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                                       params.add(new BasicNameValuePair("devSn",workmachineid));
                                       if (tv_coldfanawork_dingshikaiqi.getText().toString().equals("未设置")||tv_coldfanawork_dingshikaiqi.getText().toString().equals("00:00")){
                                           fSwitchOn = false;
                                           params.add(new BasicNameValuePair("task.fSwitchOn","false"));
                                       }else
                                       {
                                           fSwitchOn = true;
                                           params.add(new BasicNameValuePair("task.fSwitchOn","true"));
                                       }
                                       if (tv_coldfanawork_dingshiguanbi.getText().toString().equals("未设置")||tv_coldfanawork_dingshiguanbi.getText().toString().equals("00:00")){
                                           fSwitchOff = false;
                                           params.add(new BasicNameValuePair("task.fSwitchOff","false"));
                                       }else
                                       {
                                           fSwitchOff = true;
                                           params.add(new BasicNameValuePair("task.fSwitchOff","true"));
                                       }

                                       if (tv_coldfanawork_dingshikaiqi.getText().toString().equals("未设置")){
                                           params.add(new BasicNameValuePair("task.onJobTime","00:00"));
                                       }else{
                                           params.add(new BasicNameValuePair("task.onJobTime",tv_coldfanawork_dingshikaiqi.getText().toString()));
                                       }

                                       if (tv_coldfanawork_dingshiguanbi.getText().toString().equals("未设置")){
                                           params.add(new BasicNameValuePair("task.offJobTime","00:00"));
                                       }else{
                                           params.add(new BasicNameValuePair("task.offJobTime",tv_coldfanawork_dingshiguanbi.getText().toString()));
                                       }

                                       params.add(new BasicNameValuePair("task.fMode",taskfMode));
                                       params.add(new BasicNameValuePair("task.fWind",taskfWind));
                                       params.add(new BasicNameValuePair("task.durTime",tv_coldfanawork_kaiqishijian.getText().toString()));
                                       String week = "";
                                       for (int i=0;i<7;i++){
                                        if(tv_dingshi[i].getVisibility() == View.VISIBLE){
                                            week = week+"1";
                                        }else
                                        {
                                            week = week+"0";
                                        }
                                       }
                                       Log.wtf("选择的星期",week);
                                       params.add(new BasicNameValuePair("task.runWeek",week));
                                       String str = Post.dopost(uriAPI, params);
                                       if(ColdfanAdingshi.objectFromData(str).getState()==0){
                                           editor3.putString("onJobTime",tv_coldfanawork_dingshikaiqi.getText().toString());
                                           editor3.putString("offJobTime",tv_coldfanawork_dingshiguanbi.getText().toString());
                                           editor3.putString("durTime",tv_coldfanawork_kaiqishijian.getText().toString());
                                           editor3.putString("runWeek",week);
                                           editor3.putBoolean("fSwitchOn",fSwitchOn);
                                           editor3.putBoolean("fSwitchOff",fSwitchOff);
                                           editor3.commit();
                                       }
                                      Message msg = new Message();
                                       msg.what = 16;
                                       handler.sendMessage(msg);
                                   }
                               }).start();
                    }
                });

        //返回
        this.findViewById(R.id.iv_coldfanAwork_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });
//定时界面
        this.findViewById(R.id.tv_coldfanawork_dingshikaiqi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showTimePickerDialogKaiqi();
                    }
                });

        tv_coldfanawork_dingshiguanbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialogGuanbi();
            }
        });

        tv_coldfanawork_kaiqishijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPickerDialog();
            }
        });

        tv_coldfanawork_dingshi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        tv_coldfanawork_dingshi1.setVisibility(View.INVISIBLE);

            }
        });
        this.findViewById(R.id.tv_coldfanawork_dingshi01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_coldfanawork_dingshi1.setVisibility(View.VISIBLE);
            }
        });

        tv_coldfanawork_dingshi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_coldfanawork_dingshi2.setVisibility(View.INVISIBLE);

            }
        });
        this.findViewById(R.id.tv_coldfanawork_dingshi02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_coldfanawork_dingshi2.setVisibility(View.VISIBLE);
            }
        });
        tv_coldfanawork_dingshi3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_coldfanawork_dingshi3.setVisibility(View.INVISIBLE);

            }
        });
        this.findViewById(R.id.tv_coldfanawork_dingshi03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_coldfanawork_dingshi3.setVisibility(View.VISIBLE);
            }
        });
        tv_coldfanawork_dingshi4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_coldfanawork_dingshi4.setVisibility(View.INVISIBLE);

            }
        });
        this.findViewById(R.id.tv_coldfanawork_dingshi04).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_coldfanawork_dingshi4.setVisibility(View.VISIBLE);
            }
        });
        tv_coldfanawork_dingshi5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_coldfanawork_dingshi5.setVisibility(View.INVISIBLE);

            }
        });
        this.findViewById(R.id.tv_coldfanawork_dingshi05).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_coldfanawork_dingshi5.setVisibility(View.VISIBLE);
            }
        });
        tv_coldfanawork_dingshi6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_coldfanawork_dingshi6.setVisibility(View.INVISIBLE);

            }
        });
        this.findViewById(R.id.tv_coldfanawork_dingshi06).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_coldfanawork_dingshi6.setVisibility(View.VISIBLE);
            }
        });
        tv_coldfanawork_dingshi7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_coldfanawork_dingshi7.setVisibility(View.INVISIBLE);

            }
        });
        this.findViewById(R.id.tv_coldfanawork_dingshi07).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_coldfanawork_dingshi7.setVisibility(View.VISIBLE);
            }
        });
//历史记录
        if (preferences3.getString("lishijilu","").equals("")) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    String uriAPI = MainActivity.ip + "smarthome/fan/queryDeviceHistory";
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                    params.add(new BasicNameValuePair("devSn", workmachineid));
                    params.add(new BasicNameValuePair("days", "10"));
                    String str = Post.dopost(uriAPI, params);

                    Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str, Map.class);
                    String datastr = JacksonUtil.serializeObjectToJson(map.get("data"));
                     if (!JacksonUtil.serializeObjectToJson(map.get("state")).equals("2")){
                    lishijilu = JacksonUtil.deserializeJsonToList(datastr, Lishijilu.class);
                    date = new String[lishijilu.size()];
                    score = new int[lishijilu.size()];
                    date[0] = "暂无数据";
                    score[0] = 0;
//                        Log.wtf("历史数据", lishijilu.get(0).getUseDate().substring(5, 10));
                        for (int i = 0; i < lishijilu.size(); i++) {
                            date[i] = lishijilu.get(i).getUseDate().substring(5, 10);
                            score[i] = lishijilu.get(i).getUseTime() / 1000 / 3600 + 1;
                        }
                        editor3.putString("lishijilu", datastr);
                        editor3.commit();
                    }else
                     {
                         date = new String[1];
                         score = new int[1];
                         date[0] = "暂无数据";
                         score[0] = 0;
                     }
                        Message msg = new Message();
                        msg.what = 17;
                        handler.sendMessage(msg);

                }
            }).start();
        }
        else
        {
            lishijilu = JacksonUtil.deserializeJsonToList(preferences3.getString("lishijilu",""), Lishijilu.class);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault());
            String dangqianshijian = sdf.format(new Date());

            String dangqianriqi = dangqianshijian.substring(8,10);

            if (!lishijilu.get(lishijilu.size()-1).getUseDate().substring(8,10).equals(dangqianriqi)){
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String uriAPI = MainActivity.ip + "smarthome/fan/queryDeviceHistory";
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                        params.add(new BasicNameValuePair("devSn", workmachineid));
                        params.add(new BasicNameValuePair("days", "10"));
                        String str = Post.dopost(uriAPI, params);

                        Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str, Map.class);
                        String datastr = JacksonUtil.serializeObjectToJson(map.get("data"));

                        lishijilu = JacksonUtil.deserializeJsonToList(datastr, Lishijilu.class);
//                        Log.wtf("历史数据", lishijilu.get(0).getUseDate().substring(5, 10));

                            date = new String[lishijilu.size()];
                            score = new int[lishijilu.size()];
                        date[0] = "暂无数据";
                        score[0] = 0;

                            for (int i = 0; i < lishijilu.size(); i++) {
                                date[i] = lishijilu.get(i).getUseDate().substring(5, 10);
                                score[i] = lishijilu.get(i).getUseTime() / 1000 / 3600 + 1;
                            }




                        editor3.putString("lishijilu",datastr);
                        editor3.commit();
                        Message msg = new Message();
                        msg.what = 17;
                        handler.sendMessage(msg);
                    }
                }).start();

            }else
            {

                    date = new String[lishijilu.size()];
                    score = new int[lishijilu.size()];
                    date[0] = "暂无数据";
                    score[0] = 0;
                    for (int i = 0; i < lishijilu.size(); i++) {
                        date[i] = lishijilu.get(i).getUseDate().substring(5, 10);
                        score[i] = lishijilu.get(i).getUseTime() / 1000 / 3600 + 1;
                    }

                Message msg = new Message();
                msg.what = 17;
                handler.sendMessage(msg);
            }
        }



        this.findViewById(R.id.ll_coldfanawork_lishishuju).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ll_coldfanawork_lishibiaoge.getVisibility() == View.VISIBLE)
                        {
                            ll_coldfanawork_lishibiaoge.setVisibility(View.GONE);
                        }else
                            if (ll_coldfanawork_lishibiaoge.getVisibility() == View.GONE)
                            {
                                ll_coldfanawork_lishibiaoge.setVisibility(View.VISIBLE);
                            }
                    }
                });


    }
    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables(){
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }
    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }

    }


    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#22BE64"));  //折线的颜色（绿色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边



        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }



        @Override
    protected void onRestart() {
//        MainActivity.out.write(preferences3.getString("heartpakage",""));
//        MainActivity.out.flush();
//            try {
//                ((OZApplication)getApplication()).getOutputStream().write(MainActivity.heartbyte);
//                ((OZApplication)getApplication()).getOutputStream().flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        super.onRestart();
    }

    public  void showHourPickerDialog(){
        NumberPicker mPicker = new NumberPicker(coldfanAwork.this);
        mPicker.setMinValue(0);
        mPicker.setMaxValue(24);
        mPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                // TODO Auto-generated method stub

                String hour = "";
                if (newVal<10){
                    hour = "0"+newVal;
                }else {
                    hour = newVal+"";
                }

                tv_coldfanawork_kaiqishijian.setText(hour);
            }
        });


        AlertDialog mAlertDialog = new AlertDialog.Builder(coldfanAwork.this)
                .setTitle("设定最长开启时间").setView(mPicker).setPositiveButton("确定",null).create();
        mAlertDialog.show();
    }
//日期选择
public void showTimePickerDialogKaiqi() {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

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
                    tv_coldfanawork_dingshikaiqi.setText(hour1+":"+minute1);
                }

    }, hour, minute, true);
    dialog.show();
}
    public void showTimePickerDialogGuanbi() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

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
                tv_coldfanawork_dingshiguanbi.setText(hour1+":"+minute1);
            }

        }, hour, minute, true);
        dialog.show();
    }
    @Override
    protected void onResume() {
        //初始化界面
        //开关
        if (fSwitch) {
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 2;
            handler.sendMessage(msg);
        }
        //模式
        if(fMode==1){
            Message msg = new Message();
            msg.what = 3;
            handler.sendMessage(msg);
        }else  if(fMode==2) {
            Message msg = new Message();
            msg.what = 4;
            handler.sendMessage(msg);
        } else  if(fMode==3) {
            Message msg = new Message();
            msg.what = 5;
            handler.sendMessage(msg);
        }

        //风速
        if (fWind==1) {
            Message msg = new Message();
            msg.what = 6;
            handler.sendMessage(msg);
        } else if (fWind==2) {
            Message msg = new Message();
            msg.what = 7;
            handler.sendMessage(msg);
        }else if (fWind==3) {
            Message msg = new Message();
            msg.what = 8;
            handler.sendMessage(msg);
        }
        //摆风
        if (fSwing) {
            Message msg = new Message();
            msg.what = 9;
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 10;
            handler.sendMessage(msg);
        }
        //uv杀菌
        if (fUV) {
            Message msg = new Message();
            msg.what = 11;
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 12;
            handler.sendMessage(msg);
        }
        //制冷
        if (fState) {
            Message msg = new Message();
            msg.what = 13;
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 14;
            handler.sendMessage(msg);
        }





        super.onResume();


    }


//解决ontouch在scorllview中没有作用的问题

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        mGestureDetector.onTouchEvent(ev);
//// scroll.onTouchEvent(ev);
//        return super.dispatchTouchEvent(ev);
//    }

//
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        SnsConstant snsConstant = new SnsConstant();
//        if (e1.getX() - e2.getX() > snsConstant.getFlingMinDistance()
//                && Math.abs(velocityX) > snsConstant.getFlingMinVelocity()) {
//
////          切换Activity
////            Intent intent = new Intent(coldfanAindex.this, coldfanAwork.class);
////            startActivity(intent);
////            Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();
//        } else if (e2.getX() - e1.getX() > snsConstant.getFlingMinDistance()
//                && Math.abs(velocityX) > snsConstant.getFlingMinVelocity()) {
//
////          切换Activity
//            onBack();
////            Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();
//        }
//
//        return false;
//    }

//    public static class SnsConstant {
//        private static final int FLING_MIN_DISTANCE = 130;
//        private static final int FLING_MIN_VELOCITY = 0;
//
//        public static int getFlingMinDistance() {
//            return FLING_MIN_DISTANCE;
//        }
//
//        public static int getFlingMinVelocity() {
//            return FLING_MIN_VELOCITY;
//        }
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        return mGestureDetector.onTouchEvent(event);
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
        super.onStart();
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction("broadcass1");            //添加动态广播的Action
        registerReceiver(receiver, dynamic_filter);    // 注册自定义动态广播消息
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("broadcass1")) {    //动作检测
                String receivedata = intent.getStringExtra("msg");
                if (MainActivity.b[13]==0x01) {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[13]==0x02) {
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
                //模式
                if (MainActivity.b[14]==0x01) {
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[14]==0x02) {
                    Message msg = new Message();
                    msg.what = 4;
                    handler.sendMessage(msg);
                }else if (MainActivity.b[14]==0x03) {
                    Message msg = new Message();
                    msg.what = 5;
                    handler.sendMessage(msg);
                }
                //风速
                if (MainActivity.b[15]==0x01) {
                    Message msg = new Message();
                    msg.what = 6;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[15]==0x02) {
                    Message msg = new Message();
                    msg.what = 7;
                    handler.sendMessage(msg);
                }else if (MainActivity.b[15]==0x03) {
                    Message msg = new Message();
                    msg.what = 8;
                    handler.sendMessage(msg);
                }
                //摆风
                if (MainActivity.b[16]==0x01) {
                    Message msg = new Message();
                    msg.what = 9;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[16]==0x02) {
                    Message msg = new Message();
                    msg.what = 10;
                    handler.sendMessage(msg);
                }
                //uv杀菌
                if (MainActivity.b[17]==0x01) {
                    Message msg = new Message();
                    msg.what = 11;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[17]==0x02) {
                    Message msg = new Message();
                    msg.what = 12;
                    handler.sendMessage(msg);
                }
                //制冷
                if (MainActivity.b[19]==0x01) {
                    Message msg = new Message();
                    msg.what = 13;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[19]==0x02) {
                    Message msg = new Message();
                    msg.what = 14;
                    handler.sendMessage(msg);
                }
            }

        }
    };




    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * 遍历所有view
     *
     * @param viewGroup
     */
    public void traversalView(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                traversalView((ViewGroup) view);
            } else {
                if (fSwitch)
                {
                    doView1(view);
                }else
                {
                    doView(view);
                }
            }
        }
    }

    /**
     * 处理view
     *
     * @param view
     */
    private void doView(View view) {
        if (view.getId()==R.id.seekBar)
        {
            view.setEnabled(false);
        }else
        {
            view.setClickable(false);
        }

    }
    private void doView1(View view) {
        if (view.getId()==R.id.seekBar)
        {
            view.setEnabled(true);
        }else
        {
            view.setClickable(true);
        }
    }
}
