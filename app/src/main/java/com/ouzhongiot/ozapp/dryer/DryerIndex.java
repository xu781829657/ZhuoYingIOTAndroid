package com.ouzhongiot.ozapp.dryer;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MainActivity;
import com.ouzhongiot.ozapp.activity.coldfanAyonghuzhongxin;
import com.ouzhongiot.ozapp.others.CircleImageView;
import com.ouzhongiot.ozapp.others.DampView;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.LoginOutActivity;
import com.ouzhongiot.ozapp.others.Post;
import com.ouzhongiot.ozapp.others.SetPackage;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DryerIndex extends LoginOutActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    //模式：0 衣物选择，1 婴儿护理，2 旧衣除湿，3 自定义模式


    private ImageView img;
    GestureDetector mGestureDetector;
    private TextView tv_coldfanAindex_wenxintishi,tv_dryerindex_bao,tv_dryerindex_zhong,tv_dryerindex_hou,tv_dryerindex_yiwugongji,
            tv_dryerindex_yiwuyuji,tv_dryerindex_kaishihonggan;
    private LinearLayout ll_dryerindex_yiwuxuanzhe,ll_dryerindex_yiwuxuanzhe1,ll_dryerindex_yinerhuli,ll_dryerindex_jiuyichushi,ll_dryerindex_zidingyi,ll_dryerindex_leijishijian;
    private TextView tv_coldfanaindex_kaiguan, tv_coldfanaindex_kaiguan0,tv_dyrerindex_leijishijian;
    private ImageView iv_coldfanaindex_kaiguan;
    private  DampView viewSnsLayout;
    private GradientDrawable gradientDrawable;
    private SharedPreferences preference;
    private SharedPreferences preference2;
    private SharedPreferences.Editor editor2;
    private SharedPreferences preferences3;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editor3;
    private CircleImageView iv_clodfanAindex_touxiang;
    private String workmachineid;
    private String workmachinetype;
    private int bao = 0,zhong = 0,hou = 0;
    private boolean fSwitch;
    private int fShift;
    private boolean fAnion;
    private boolean fUV;
    private OutputStream outputStream;
    int mod  = 100;
//    private String heartpakage;
    private TextView tv_coldfanaindex_dizhi,tv_dryerindex_leijiyunxing;
    private TextView tv_coldfanaindex_temperature, tv_coldfanaindex_quality, tv_coldfanaindex_humidity;
    private ImageView iv_coldfanAindex_wether;
   private DryerShiyongshijian dryerShiyongshijian;
    private ImageView iv_dryerindex_yinerhuli_gouxuan,iv_dryerindex_yinerhuli_next,iv_dryerindex_jiuyichushi_gouxuan,iv_dryerindex_jiuyichushi_next,iv_dryerindex_zidingyi_gouxuan,iv_dryerindex_zidingyi_next;
    private TextView tv_dryerindex_yinerhuli,tv_dryerindex_jiuyichushi,tv_dryerindex_zidingyi;
    private TextView tv_dryerindex_gaore,tv_dryerindex_jieneng,tv_dryerindex_fulizi,tv_dryerindex_shajun;
    private ImageView iv_dryerindex_gaore,iv_dryerindex_jieneng,iv_dryerindex_fulizi,iv_dryerindex_shajun;
    private LinearLayout ll_dryerindex_gaore,ll_dryerindex_jieneng,ll_dryerindex_fulizi,ll_dryerindex_shajun,Dryerlishishuju_shijian;
    private LinearLayout ll_dryerindex_unchuangtou;
    private static int baotime = 1200;
    private static int zhongtime = 1800;
    private static int houtime = 2400;
    private static int jiuyitime = 1;
    private static int yingertime = 1;
    private static double dijianlv1 =0.3; //同种衣服之间
    private static double dijianlv2 =0.6; //不同种衣服之间
    int  allbaotime = 0;
    int allhoutime = 0;
    int allsecondtime = 0;
    int alltime = 0;
    long starttime = 0;
    long finishtime = 0;
    private TextView timerTextView;
    public static boolean notfinish = true ;
    public static boolean notruning = true;
    int hour = 0;
    int min =  0;
    int seconds  = 0;
    GraphicalView chartView_shijian;
    private List<LishijiluBean> jilu;
    private int jilustate;
    private String offtime;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 检查为开启状态
                case 1:
                    iv_coldfanaindex_kaiguan.setImageResource(R.mipmap.open);
                    tv_coldfanaindex_kaiguan.setText("开启");
                    gradientDrawable.setColor(getResources().getColor(R.color.airclean));
                    ll_dryerindex_unchuangtou.setVisibility(View.GONE);
                    fSwitch = true;
                    editor3.putBoolean("fSwitch", fSwitch);
                    editor3.commit();
                    break;
                case 2:
                    DryerIndex.notfinish = false;
                    notruning = true;
                    iv_coldfanaindex_kaiguan.setImageResource(R.mipmap.close);
                    tv_coldfanaindex_kaiguan.setText("关闭");
                    tv_dryerindex_leijiyunxing.setText("累计运行时间");
                    ll_dryerindex_leijishijian.setVisibility(View.VISIBLE);
                    timerTextView.setVisibility(View.GONE);

                    ll_dryerindex_unchuangtou.setVisibility(View.VISIBLE);
                    gradientDrawable.setColor(getResources().getColor(R.color.gray));
                    fSwitch = false;
                    editor3.putLong("dryerfinishtime",0);
                    editor3.putBoolean("fSwitch", fSwitch);
                    editor3.putInt("mod",100);
                    editor3.commit();
                    iv_dryerindex_yinerhuli_gouxuan.setImageResource(R.mipmap.aircleandingshi);
                    tv_dryerindex_yinerhuli.setTextColor(getResources().getColor(R.color.airclean));
                    iv_dryerindex_yinerhuli_next.setImageResource(R.mipmap.aircleannext);
                    ll_dryerindex_yinerhuli.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_dryerindex_jiuyichushi_gouxuan.setImageResource(R.mipmap.aircleandingshi);
                    tv_dryerindex_jiuyichushi.setTextColor(getResources().getColor(R.color.airclean));
                    iv_dryerindex_jiuyichushi_next.setImageResource(R.mipmap.aircleannext);
                    ll_dryerindex_jiuyichushi.setBackgroundColor(getResources().getColor(R.color.white));
                    iv_dryerindex_zidingyi_gouxuan.setImageResource(R.mipmap.aircleandingshi);
                    tv_dryerindex_zidingyi.setTextColor(getResources().getColor(R.color.airclean));
                    iv_dryerindex_zidingyi_next.setImageResource(R.mipmap.aircleannext);
                    ll_dryerindex_zidingyi.setBackgroundColor(getResources().getColor(R.color.white));
                    break;
                case 3:
                    tv_coldfanAindex_wenxintishi.setText(preference2.getString("ganmao", "今天气温很高，适合穿短袖，注意防晒和避暑,祝您有一个愉快的周末"));
                    tv_coldfanaindex_humidity.setText("湿度  " + preference2.getString("humidity", "") + "%");
                    tv_coldfanaindex_temperature.setText("温度  " + preference2.getString("temperature", "") + "℃");
                    tv_coldfanaindex_quality.setText("空气质量  " + preference2.getString("quality", ""));
                    if (preference2.getString("weatherinfo", "").contains("阴")) {
                        iv_coldfanAindex_wether.setImageResource(R.mipmap.yingtian);
                    }
                    if (preference2.getString("weatherinfo", "").contains("云")) {
                        iv_coldfanAindex_wether.setImageResource(R.mipmap.duoyun);
                    }
                    if (preference2.getString("weatherinfo", "").contains("晴")) {
                        iv_coldfanAindex_wether.setImageResource(R.mipmap.sun);
                    }
                    if (preference2.getString("weatherinfo", "").contains("雨")) {
                        iv_coldfanAindex_wether.setImageResource(R.mipmap.rain);
                    }
                    if (preference2.getString("weatherinfo", "").contains("雪")) {
                        iv_coldfanAindex_wether.setImageResource(R.mipmap.snow);
                    }
                    if (preference2.getString("weatherinfo", "").contains("雾") || preference2.getString("weatherinfo", "").contains("霾")) {
                        iv_coldfanAindex_wether.setImageResource(R.mipmap.snow);
                    }
                    break;
                case 4:
                    int mod = preferences3.getInt("mod",100);

                    if(mod==100){
                        ll_dryerindex_leijishijian.setVisibility(View.VISIBLE);
                        timerTextView.setVisibility(View.GONE);
                        if (dryerShiyongshijian.getData()!=null) {
                            DecimalFormat df = new DecimalFormat("######0.00");
                            tv_dyrerindex_leijishijian.setText(df.format((double) dryerShiyongshijian.getData().getTotalTime() / 1000 / 3600)+"");
                            tv_dryerindex_leijiyunxing.setText("累计运行时间");

                        }
                    }else
                    {
                        finishtime = preferences3.getLong("dryerfinishtime",0);
                        Date dt= new Date();
                        starttime = dt.getTime();
                        if(finishtime<starttime)
                        {
                            ll_dryerindex_leijishijian.setVisibility(View.VISIBLE);
                            timerTextView.setVisibility(View.GONE);

                            if (dryerShiyongshijian.getData()!=null) {

                                DecimalFormat df = new DecimalFormat("######0.00");
                                tv_dyrerindex_leijishijian.setText(df.format((double) dryerShiyongshijian.getData().getTotalTime() / 1000 / 3600)+"");

                            }
                        }else
                        {

                            ll_dryerindex_leijishijian.setVisibility(View.GONE);
                            timerTextView.setVisibility(View.VISIBLE);
                            timerTextView.setText("");
                            timerTextView.setTextColor(getResources().getColor(R.color.airclean));
                            timerTextView.setTextSize(40);
                            alltime = (int)(finishtime-starttime)/1000;
                            hour = alltime/3600;
                            min =  (alltime-hour*3600)/60;
                            seconds = (alltime-hour*3600-min*60);
                            tv_dryerindex_leijiyunxing.setText("任务剩余时间");
                            Log.wtf("开始倒计前","------------------------------------");
                            if (notruning)
                            {
                                notruning = false;

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    DryerIndex.notfinish = true;
//                                    Log.wtf("开始倒计时","------------------------------------");
                                    while(notfinish){
                                        Message msg = new Message();
                                        msg.what = 10;
                                        handler.sendMessage(msg);
//                                        Log.wtf("这个是倒计时中","------------------------------------");
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        seconds--;
                                    if (seconds < 0) {
                                        min--;
                                        seconds = 59;
                                    }
                                        if (min < 0) {
                                            min = 59;
                                            hour--;
                                        }
                                            if (hour < 0) {
                                                // 倒计时结束，一天有24个小时
//                                                Log.wtf("结束这个是倒计","------------------------------------");
                                                DryerIndex.notfinish = false;
                                                notruning = true;
                                                Message msg2 = new Message();
                                                msg2.what = 11;
                                                handler.sendMessage(msg2);
                                            }


                                        }
                                }
                            }).start();
                            }
                            if(!preferences3.getBoolean("fSwitch",false))
                            {
                                byte[] order = {0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
                                ((OZApplication)getApplication()).SendOrder(SetPackage.GetDryerOrderC1(workmachineid,preference2.getString("workmachinetype", ""),order));
                            }

                        }
                    }


                    break;

                case 7:

                    break;
                case 8:
                    viewSnsLayout.smoothScrollTo(0,500);
                    break;
                case 9:
                    tv_dryerindex_bao.setText(bao+" 件");
                    tv_dryerindex_zhong.setText(zhong+" 件");
                    tv_dryerindex_hou.setText(hou+" 件");
                    int i = bao+zhong+hou;
                    tv_dryerindex_yiwugongji.setText("共 "+i+" 件");
                    int  allbaotime = 0;
                    int allhoutime = 0;
                    int allzhongtime = 0;

//               if(bao!=1) {
//                   allbaotime = baotime;
//                   for (int j = 1; j < bao; j++) {
//                       double a = dijianlv1;
//                       for (int k = 1; k < j; k++) {
//                           a = a * a;
//                       }
//                       allbaotime = (int) (allbaotime + baotime * a);
//                       Log.wtf("这个是单独薄衣服的时间", allbaotime + "0");
//                   }
//               }
//                    else
//               {
//                   allbaotime = baotime;
//               }
            if(bao == 1)
            {
                allbaotime = baotime;
            }else
            if(bao>1)
            {
                {
                    allbaotime = baotime;
                    for(i = 1;i<bao;i++){
                        allbaotime = (int)(allbaotime+baotime*dijianlv1);
                    }
                }
            }

                     alltime =allbaotime+zhongtime*zhong+houtime*hou;

                     int hourxuanzhe = alltime/3600;
                      int  minxuanzhe =  (alltime-hour*3600)/60;
                     int  secondsxuanzhe = (alltime-hour*3600-min*60);
                    String hourstr = "00";
                    String minstr = "00";
                    String secndsstr = "00";
                    if(hourxuanzhe<10)
                    {
                        hourstr = "0"+hourxuanzhe;
                    }else
                    {
                        hourstr = ""+hourxuanzhe;
                    }
                    if(minxuanzhe<10)
                    {
                        minstr = "0"+minxuanzhe;
                    }else
                    {
                        minstr = ""+minxuanzhe;
                    }

                    tv_dryerindex_yiwuyuji.setText("预计："+hourstr+"时"+minstr+"分");


                    break;
                case 10:
                    String hourstr1 = hour+"";
                    String minstr1 = min+"";
                    String secondsstr1 = seconds+"";
                    if (hour<10)
                    {
                        hourstr1 = "0"+hour;
                    }
                    if (min<10)
                    {
                        minstr1 = "0"+min;
                    }
                    if (seconds<10)
                    {
                        secondsstr1 = "0"+seconds;
                    }
                    String strTime=  hourstr1+":"+ minstr1+":"+secondsstr1;
                    timerTextView.setText(strTime);
                    break;
                case 11:
                    timerTextView.setText("主人~~您的衣物已烘干！");
                    timerTextView.setTextColor(getResources().getColor(R.color.black));
                    timerTextView.setTextSize(20);
                    break;
                case 12:

//                    Log.wtf("跳转了---------------高热","--------------------------");
                    ll_dryerindex_gaore.setBackgroundColor(getResources().getColor(R.color.airclean));
                    tv_dryerindex_gaore.setTextColor(getResources().getColor(R.color.white));
                    iv_dryerindex_gaore.setImageResource(R.mipmap.gaore0);
                    ll_dryerindex_jieneng.setBackgroundColor(getResources().getColor(R.color.white));
                    tv_dryerindex_jieneng.setTextColor(getResources().getColor(R.color.airclean));
                    iv_dryerindex_jieneng.setImageResource(R.mipmap.jieneng1);
                    fShift = 1;
                    editor3.putInt("fShift", 1);
                    editor3.commit();
                    break;
                case 13:
                    ll_dryerindex_jieneng.setBackgroundColor(getResources().getColor(R.color.airclean));
                    tv_dryerindex_jieneng.setTextColor(getResources().getColor(R.color.white));
                    iv_dryerindex_jieneng.setImageResource(R.mipmap.jieneng0);
                    ll_dryerindex_gaore.setBackgroundColor(getResources().getColor(R.color.white));
                    tv_dryerindex_gaore.setTextColor(getResources().getColor(R.color.airclean));
                    iv_dryerindex_gaore.setImageResource(R.mipmap.gaore1);
                    fShift = 2;
                    editor3.putInt("fShift", 2);
                    editor3.commit();
                    break;
                case 14:
                    ll_dryerindex_shajun.setBackgroundColor(getResources().getColor(R.color.airclean));
                    tv_dryerindex_shajun.setTextColor(getResources().getColor(R.color.white));
                    iv_dryerindex_shajun.setImageResource(R.mipmap.aircleanshajun0);
                    fUV = true;
                    editor3.putBoolean("fUV", fUV);
                    editor3.commit();
                    break;
                case 15:
                    ll_dryerindex_shajun.setBackgroundColor(getResources().getColor(R.color.white));
                    tv_dryerindex_shajun.setTextColor(getResources().getColor(R.color.airclean));
                    iv_dryerindex_shajun.setImageResource(R.mipmap.aircleanshajun1);
                    fUV = false;
                    editor3.putBoolean("fUV", fUV);
                    editor3.commit();
                    break;
                case 16:
                    ll_dryerindex_fulizi.setBackgroundColor(getResources().getColor(R.color.airclean));
                    tv_dryerindex_fulizi.setTextColor(getResources().getColor(R.color.white));
                    iv_dryerindex_fulizi.setImageResource(R.mipmap.aircleanfulizi0);
                    fAnion = true;
                    editor3.putBoolean("fAnion", fAnion);
                    editor3.commit();
                    break;
                case 17:
                    ll_dryerindex_fulizi.setBackgroundColor(getResources().getColor(R.color.white));
                    tv_dryerindex_fulizi.setTextColor(getResources().getColor(R.color.airclean));
                    iv_dryerindex_fulizi.setImageResource(R.mipmap.aircleanfulizi1);
                    fAnion = false;
                    editor3.putBoolean("fAnion", fAnion);
                    editor3.commit();
                    break;
                case 18:
                    showChart_shijian();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dryer_index);
        OZApplication.getInstance().addActivity(this);
        outputStream = ((OZApplication)getApplication()).outputStream;

        preference2 = getSharedPreferences("data", MODE_PRIVATE);
        editor2 = preference2.edit();
        workmachineid = preference2.getString("workmachineid", "");
        workmachinetype = preference2.getString("workmachinetype", "");





        //请求当前状态
        preferences3 = getSharedPreferences(workmachineid, MODE_PRIVATE);
        fSwitch = preferences3.getBoolean("fSwitch", false);
        fUV = preferences3.getBoolean("fUV", false);
        fAnion = preferences3.getBoolean("fAnion",false);
        fShift = preferences3.getInt("fShift", 1);
//        heartpakage = preferences3.getString("heartpakage", "");
        editor3 = preferences3.edit();

        tv_coldfanAindex_wenxintishi = (TextView) findViewById(R.id.tv_coldfanAindex_wenxintishi);
        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener) this);
        viewSnsLayout = (DampView) findViewById(R.id.dampview1);
        viewSnsLayout.setOnTouchListener(this);
        viewSnsLayout.setLongClickable(true);
        setupView();

        //初始化数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求当前数据
                String uriAPI2 = MainActivity.ip + "smarthome/dryer/queryDeviceData";
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                params2.add(new BasicNameValuePair("devSn", workmachineid));
                String str2 = Post.dopost(uriAPI2, params2);
                Map<String, Object> shiyongshijian = JacksonUtil.deserializeJsonToObject(str2, Map.class);
                String shijiandata = JacksonUtil.serializeObjectToJson(shiyongshijian.get("data"));
                Map<String, Object> datamap = JacksonUtil.deserializeJsonToObject(shijiandata, Map.class);
                int shiyongshijiandata = Integer.parseInt(JacksonUtil.serializeObjectToJson(shiyongshijian.get("state")));
                Log.wtf("这个是收到的机器数据",str2+datamap.get("totalTime")+shiyongshijiandata);
                if (shiyongshijiandata==2||shiyongshijiandata==1)
                {

                }else
                {
                    dryerShiyongshijian = DryerShiyongshijian.objectFromData(str2);
                    if (dryerShiyongshijian!=null)
                        if(dryerShiyongshijian.getData()!=null){
                            Message msg = new Message();
                            msg.what = 4;
                            handler.sendMessage(msg);
                        }

                }




                //请求历史数据
                String uriAPI3 = MainActivity.ip + "smarthome/dryer/queryDeviceHistory";
                List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                params3.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                params3.add(new BasicNameValuePair("devSn", workmachineid));
                params3.add(new BasicNameValuePair("days", "7"));
                String str3 = Post.dopost(uriAPI3, params3);
                Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str3, Map.class);
                String data = JacksonUtil.serializeObjectToJson(map.get("data"));
                jilu = JacksonUtil.deserializeJsonToList(data, LishijiluBean.class);
                jilustate =Integer.parseInt(JacksonUtil.serializeObjectToJson(map.get("state")));
                if(jilustate == 1||jilustate==2){

                }else
                {
//                    Log.wtf("这个是日期",jilu.get(0).getUseTime()+"     "+jilustate);
                    Message msg = new Message();
                    msg.what = 18;
                    handler.sendMessage(msg);
                }

            }
        }).start();




        ll_dryerindex_unchuangtou = (LinearLayout) findViewById(R.id.ll_dryerindex_unchuangtou);
        ll_dryerindex_leijishijian = (LinearLayout) findViewById(R.id.ll_dryerindex_leijishijian);
        timerTextView = (TextView)findViewById(R.id.dryerindex_timer_text_view);
        iv_dryerindex_yinerhuli_gouxuan = (ImageView)findViewById(R.id.iv_dryerindex_yinerhuli_gouxuan);
        iv_dryerindex_yinerhuli_next = (ImageView)findViewById(R.id.iv_dryerindex_yinerhuli_next);
        iv_dryerindex_jiuyichushi_gouxuan = (ImageView)findViewById(R.id.iv_dryerindex_jiuyichushi_gouxuan);
        iv_dryerindex_jiuyichushi_next = (ImageView)findViewById(R.id.iv_dryerindex_jiuyichushi_next);
        iv_dryerindex_zidingyi_gouxuan = (ImageView)findViewById(R.id.iv_dryerindex_zidingyi_gouxuan);
        iv_dryerindex_zidingyi_next = (ImageView)findViewById(R.id.iv_dryerindex_zidingyi_next);
        tv_dryerindex_yinerhuli = (TextView) findViewById(R.id.tv_dryerindex_yinerhuli);
        tv_dryerindex_jiuyichushi = (TextView) findViewById(R.id.tv_dryerindex_jiuyichushi);
        tv_dryerindex_zidingyi = (TextView) findViewById(R.id.tv_dryerindex_zidingyi);
        tv_dryerindex_leijiyunxing = (TextView) findViewById(R.id.tv_dryerindex_leijiyunxing);

        tv_dryerindex_bao = (TextView) findViewById(R.id.tv_dryerindex_bao);
        tv_dryerindex_zhong = (TextView) findViewById(R.id.tv_dryerindex_zhong);
        tv_dryerindex_hou = (TextView) findViewById(R.id.tv_dryerindex_hou);
        tv_dryerindex_yiwugongji = (TextView) findViewById(R.id.tv_dryerindex_yiwugongji);
        tv_dryerindex_yiwuyuji = (TextView) findViewById(R.id.tv_dryerindex_yiwuyuji);
        tv_dryerindex_kaishihonggan = (TextView) findViewById(R.id.tv_dryerindex_kaishihonggan);

        ll_dryerindex_yinerhuli = (LinearLayout) findViewById(R.id.ll_dryerindex_yinerhuli);
        ll_dryerindex_zidingyi = (LinearLayout) findViewById(R.id.ll_dryerindex_zidingyi);
        ll_dryerindex_jiuyichushi = (LinearLayout) findViewById(R.id.ll_dryerindex_jiuyichushi);
        ll_dryerindex_yiwuxuanzhe = (LinearLayout) findViewById(R.id.ll_dryerindex_yiwuxuanzhe);
        ll_dryerindex_yiwuxuanzhe1 = (LinearLayout) findViewById(R.id.ll_dryerindex_yiwuxuanzhe1);
        tv_dyrerindex_leijishijian = (TextView) findViewById(R.id.tv_dyrerindex_leijishijian);
        iv_clodfanAindex_touxiang = (CircleImageView) findViewById(R.id.iv_coldfanAindex_touxiang);
        tv_coldfanaindex_dizhi = (TextView) findViewById(R.id.tv_coldfanaindex_dizhi);
        iv_coldfanaindex_kaiguan = (ImageView) findViewById(R.id.iv_coldfanaindex_kaiguan);
        tv_coldfanaindex_kaiguan = (TextView) findViewById(R.id.tv_coldfanaindex_kaiguan);
        tv_coldfanaindex_kaiguan0 = (TextView) findViewById(R.id.tv_coldfanaindex_kaiguan0);
        gradientDrawable = (GradientDrawable) tv_coldfanaindex_kaiguan0.getBackground();
//        preference = getSharedPreferences("coldfanA", MODE_PRIVATE);
//        editor = preference.edit();
        tv_coldfanaindex_humidity = (TextView) findViewById(R.id.tv_coldfanaindex_humidity);
        tv_coldfanaindex_quality = (TextView) findViewById(R.id.tv_coldfanaindex_quality);
        tv_coldfanaindex_temperature = (TextView) findViewById(R.id.tv_coldfanaindex_temperature);
        iv_coldfanAindex_wether = (ImageView) findViewById(R.id.iv_coldfanAindex_wether);

        ll_dryerindex_gaore = (LinearLayout) findViewById(R.id.ll_dryerindex_gaore);
        ll_dryerindex_jieneng = (LinearLayout) findViewById(R.id.ll_dryerindex_jieneng);
        ll_dryerindex_fulizi = (LinearLayout) findViewById(R.id.ll_dryerindex_fulizi);
        ll_dryerindex_shajun = (LinearLayout) findViewById(R.id.ll_dryerindex_shajun);

        tv_dryerindex_gaore = (TextView) findViewById(R.id.tv_dryerindex_gaore);
        tv_dryerindex_jieneng = (TextView) findViewById(R.id.tv_dryerindex_jieneng);
        tv_dryerindex_fulizi = (TextView) findViewById(R.id.tv_dryerindex_fulizi);
        tv_dryerindex_shajun = (TextView) findViewById(R.id.tv_dryerindex_shajun);

        iv_dryerindex_gaore = (ImageView) findViewById(R.id.iv_dryerindex_gaore);
        iv_dryerindex_jieneng = (ImageView) findViewById(R.id.iv_dryerindex_jieneng);
        iv_dryerindex_fulizi = (ImageView) findViewById(R.id.iv_dryerindex_fulizi);
        iv_dryerindex_shajun = (ImageView) findViewById(R.id.iv_dryerindex_shajun);

        Dryerlishishuju_shijian = (LinearLayout) findViewById(R.id.Dryerlishishuju_shijian);



        //初始化天气
        tv_coldfanAindex_wenxintishi.setText(preference2.getString("ganmao", "今天气温很高，适合穿短袖，注意防晒和避暑,祝您有一个愉快的周末"));
        tv_coldfanaindex_humidity.setText("湿度  " + preference2.getString("humidity", "") + "%");
        tv_coldfanaindex_temperature.setText("温度  " + preference2.getString("temperature", "") + "℃");
        tv_coldfanaindex_quality.setText("空气质量  " + preference2.getString("quality", ""));
        if (preference2.getString("weatherinfo", "").contains("阴")) {
            iv_coldfanAindex_wether.setImageResource(R.mipmap.yingtian);
        }
        if (preference2.getString("weatherinfo", "").contains("云")) {
            iv_coldfanAindex_wether.setImageResource(R.mipmap.duoyun);
        }
        if (preference2.getString("weatherinfo", "").contains("晴")) {
            iv_coldfanAindex_wether.setImageResource(R.mipmap.sun);
        }
        if (preference2.getString("weatherinfo", "").contains("雨")) {
            iv_coldfanAindex_wether.setImageResource(R.mipmap.rain);
        }
        if (preference2.getString("weatherinfo", "").contains("雪")) {
            iv_coldfanAindex_wether.setImageResource(R.mipmap.snow);
        }
        if (preference2.getString("weatherinfo", "").contains("雾") || preference2.getString("weatherinfo", "").contains("霾")) {
            iv_coldfanAindex_wether.setImageResource(R.mipmap.snow);
        }
        //开关
        this.findViewById(R.id.ll_coldfanindex_kaiguan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (fSwitch) {
                 byte[] order = {0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetDryerOrderC1(workmachineid,workmachinetype,order));

                } else {
                    byte[] order = {0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetDryerOrderC1(workmachineid,workmachinetype,order));
                }
            }
        });
        //高热
        ll_dryerindex_gaore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] order = {0x00,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//                try {
//                    ((OZApplication)getApplication()).getOutputStream().write();
//                    ((OZApplication)getApplication()).getOutputStream().flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                ((OZApplication)getApplication()).SendOrder(SetPackage.GetDryerOrderC1(workmachineid,workmachinetype,order));
            }
        });
        //节能
        ll_dryerindex_jieneng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] order = {0x00,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//                try {
//                    ((OZApplication)getApplication()).getOutputStream().write();
//                    ((OZApplication)getApplication()).getOutputStream().flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                ((OZApplication)getApplication()).SendOrder(SetPackage.GetDryerOrderC1(workmachineid,workmachinetype,order));
            }
        });
        //负离子
        ll_dryerindex_fulizi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fAnion) {
                    byte[] order = {0x00,0x00,0x00,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetDryerOrderC1(workmachineid,workmachinetype,order));
                } else {
                    byte[] order = {0x00,0x00,0x00,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetDryerOrderC1(workmachineid,workmachinetype,order));
                }
            }
        });
        //杀菌
        ll_dryerindex_shajun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fUV) {
                    byte[] order = {0x00,0x00,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetDryerOrderC1(workmachineid,workmachinetype,order));
                } else {
                    byte[] order = {0x00,0x00,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetDryerOrderC1(workmachineid,workmachinetype,order));
                }
            }
        });
        //设置
        this.findViewById(R.id.iv_coldfanAindex_shezhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DryerIndex.this, coldfanAyonghuzhongxin.class);
                startActivity(intent);
            }
        });

        //头像
        iv_clodfanAindex_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DryerIndex.this, coldfanAyonghuzhongxin.class);
                startActivity(intent);
            }
        });

        //衣物选择
        ll_dryerindex_yiwuxuanzhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_dryerindex_yiwuxuanzhe1.getVisibility() == View.GONE) {
                    ll_dryerindex_yiwuxuanzhe1.setVisibility(View.VISIBLE);
                    Message msg = new Message();
                    msg.what = 8;
                    handler.sendMessage(msg);

                } else
                {
                    ll_dryerindex_yiwuxuanzhe1.setVisibility(View.GONE);

                }

            }
        });

        this.findViewById(R.id.ll_dryerindex_baojia).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bao = bao+1;
                        Message msg = new Message();
                        msg.what = 9;
                        handler.sendMessage(msg);
                    }
                });

        this.findViewById(R.id.ll_dryerindex_baojian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bao = bao-1;
                if (bao<0)
                {
                    bao = 0;
                }
                Message msg = new Message();
                msg.what = 9;
                handler.sendMessage(msg);
            }
        });

        this.findViewById(R.id.ll_dryerindex_zhongjia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zhong = zhong+1;
                Message msg = new Message();
                msg.what = 9;
                handler.sendMessage(msg);
            }
        });

        this.findViewById(R.id.ll_dryerindex_zhongjian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zhong = zhong-1;
                if (zhong<0)
                {
                    zhong = 0;
                }
                Message msg = new Message();
                msg.what = 9;
                handler.sendMessage(msg);
            }
        });

        this.findViewById(R.id.ll_dryerindex_houjia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hou = hou+1;
                Message msg = new Message();
                msg.what = 9;
                handler.sendMessage(msg);
            }
        });

        this.findViewById(R.id.ll_dryerindex_houjian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hou = hou-1;
                if (hou<0)
                {
                    hou = 0;
                }
                Message msg = new Message();
                msg.what = 9;
                handler.sendMessage(msg);
            }
        });
        tv_dryerindex_kaishihonggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DryerIndex.notfinish = false;
                notruning = true;
                ll_dryerindex_yiwuxuanzhe1.setVisibility(View.GONE);
                Date dt= new Date();
                starttime = dt.getTime();
                finishtime = starttime+alltime*1000;
                editor3.putLong("dryerfinishtime",finishtime);
                editor3.putInt("mod",0);
                editor3.commit();
              if (bao!=0||hou!=0||zhong!=0)
                {
                    Message msg = new Message();
                    msg.what = 4;
                    handler.sendMessage(msg);
                }

//                Log.wtf("跳转到4","-----------------------------");

                GregorianCalendar gc = new GregorianCalendar();
                gc.setTimeInMillis(finishtime);
                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Log.wtf("这个是结束时间",format.format(gc.getTime()).substring(11,16));
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

                iv_dryerindex_yinerhuli_gouxuan.setImageResource(R.mipmap.aircleandingshi);
                tv_dryerindex_yinerhuli.setTextColor(getResources().getColor(R.color.airclean));
                iv_dryerindex_yinerhuli_next.setImageResource(R.mipmap.aircleannext);
                ll_dryerindex_yinerhuli.setBackgroundColor(getResources().getColor(R.color.white));
                iv_dryerindex_jiuyichushi_gouxuan.setImageResource(R.mipmap.aircleandingshi);
                tv_dryerindex_jiuyichushi.setTextColor(getResources().getColor(R.color.airclean));
                iv_dryerindex_jiuyichushi_next.setImageResource(R.mipmap.aircleannext);
                ll_dryerindex_jiuyichushi.setBackgroundColor(getResources().getColor(R.color.white));
                iv_dryerindex_zidingyi_gouxuan.setImageResource(R.mipmap.aircleandingshi);
                tv_dryerindex_zidingyi.setTextColor(getResources().getColor(R.color.airclean));
                iv_dryerindex_zidingyi_next.setImageResource(R.mipmap.aircleannext);
                ll_dryerindex_zidingyi.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
        ll_dryerindex_yinerhuli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DryerIndex.this,DryerYinerhuli.class);
                startActivity(intent);
            }
        });
        ll_dryerindex_jiuyichushi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DryerIndex.this,DryerJiuyichushi.class);
                startActivity(intent);
            }
        });
        ll_dryerindex_zidingyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DryerIndex.this,DryerZidingyi.class);
                startActivity(intent);
            }
        });




    }

    @Override
    protected void onRestart() {


        super.onRestart();
    }

    @Override
    protected void onResume() {






//        Log.wtf("isclosed情况restart", MainActivity.socket.isClosed() + "   " + MainActivity.socket.isInputShutdown() + "    " + MainActivity.socket.isOutputShutdown());
        //请求天气接口

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault());
        String dangqianshijian = sdf.format(new Date());

        int dangqianxiaoshi = Integer.parseInt(dangqianshijian.substring(11, 13));
        if (Integer.parseInt(preference2.getString("weathertime", "0")) - dangqianxiaoshi >= 2 || Integer.parseInt(preference2.getString("weathertime", "0")) - dangqianxiaoshi <= -2) {
            new Thread(new Runnable() {
                @Override
                public void run() {


                    String uriAPI = "http://op.juhe.cn/onebox/weather/query";
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("cityname", preference2.getString("city", "杭州市").substring(0, preference2.getString("city", "杭州市").length() - 1)));
                    params.add(new BasicNameValuePair("key", "b7cefb6e073ff6dd52f624061971141c"));
                    params.add(new BasicNameValuePair("dtype", "json"));
                    String str = Post.dopost(uriAPI, params);
                    Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str, Map.class);
                    String reasonstr = JacksonUtil.serializeObjectToJson(map.get("reason"));
                    String resultstr = JacksonUtil.serializeObjectToJson(map.get("result"));
                    Map<String, Object> result = JacksonUtil.deserializeJsonToObject(resultstr, Map.class);
                    String datastr = JacksonUtil.serializeObjectToJson(result.get("data"));
                    Map<String, Object> data = JacksonUtil.deserializeJsonToObject(datastr, Map.class);
                    String realtimestr = JacksonUtil.serializeObjectToJson(data.get("realtime"));
                    Map<String, Object> realtime = JacksonUtil.deserializeJsonToObject(realtimestr, Map.class);
                    String timestr = JacksonUtil.serializeObjectToJson(realtime.get("time"));
                    String weatherstr = JacksonUtil.serializeObjectToJson(realtime.get("weather"));
                    Map<String, Object> weather = JacksonUtil.deserializeJsonToObject(weatherstr, Map.class);
                    String temperature = JacksonUtil.serializeObjectToJson(weather.get("temperature"));
                    String humidity = JacksonUtil.serializeObjectToJson(weather.get("humidity"));
                    String info = JacksonUtil.serializeObjectToJson(weather.get("info"));

                    String lifestr = JacksonUtil.serializeObjectToJson(data.get("life"));
                    Map<String, Object> life = JacksonUtil.deserializeJsonToObject(lifestr, Map.class);
                    String infolifestr = JacksonUtil.serializeObjectToJson(life.get("info"));
                    Map<String, Object> infolife = JacksonUtil.deserializeJsonToObject(infolifestr, Map.class);


                    String chuanyistr = JacksonUtil.serializeObjectToJson(infolife.get("ganmao"));
                    List<String> chuanyilist = JacksonUtil.deserializeJsonToList(chuanyistr, String.class);


                    String ziwaixianstr = JacksonUtil.serializeObjectToJson(infolife.get("ziwaixian"));
                    List<String> ziwaixianlist = JacksonUtil.deserializeJsonToList(ziwaixianstr, String.class);
                    String ziwaixian = ziwaixianlist.get(1);
//                    Log.wtf("这个是紫外线--------------",ziwaixian);


                    String pm25str = JacksonUtil.serializeObjectToJson(data.get("pm25"));
                    Map<String, Object> pm25 = JacksonUtil.deserializeJsonToObject(pm25str, Map.class);
                    String pm25infostr = JacksonUtil.serializeObjectToJson(pm25.get("pm25"));
                    Map<String, Object> pm25info = JacksonUtil.deserializeJsonToObject(pm25infostr, Map.class);
                    String quality = JacksonUtil.serializeObjectToJson(pm25info.get("quality"));
                    String des = JacksonUtil.serializeObjectToJson(pm25info.get("des"));
//                    Log.wtf("我是真的", info + humidity + temperature + quality + des);
                    int j = info.length();
                    info = info.substring(1, j - 1);
                    j = humidity.length();
                    humidity = humidity.substring(1, j - 1);
                    j = temperature.length();
                    temperature = temperature.substring(1, j - 1);
                    j = quality.length();
                    quality = quality.substring(1, j - 1);
                    j = des.length();
                    des = des.substring(1, j - 1);

//                    Log.wtf("我是真的", info + humidity + temperature + quality + des);

                    editor2.putString("humidity", humidity);
                    editor2.putString("weatherinfo", info);
                    editor2.putString("temperature", temperature);
                    editor2.putString("quality", quality);
                    editor2.putString("des", ziwaixian);
                    editor2.putString("ganmao",chuanyilist.get(1));
                    editor2.putString("weathertime", timestr.substring(1, 3));
                    editor2.commit();

                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);

                }
            }).start();

        }


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
        if(fShift==1){

            Message msg = new Message();
            msg.what = 12;
            handler.sendMessage(msg);

        }else
        if(fShift==2){
            Message msg = new Message();
            msg.what = 13;
            handler.sendMessage(msg);
        }

        if(fUV){
            Message msg = new Message();
            msg.what = 14;
            handler.sendMessage(msg);
        }else
        {
            Message msg = new Message();
            msg.what = 15;
            handler.sendMessage(msg);
        }

        if(fAnion){
            Message msg = new Message();
            msg.what = 16;
            handler.sendMessage(msg);

        }else
        {
            Message msg = new Message();
            msg.what = 17;
            handler.sendMessage(msg);
        }


//        Log.wtf("这个是读取的图片地址",preference2.getString("touxiangbendiurl",""));
        if (getLoacalBitmap(preference2.getString("touxiangbendiurl", Environment.getExternalStorageDirectory() + "/"
                + "lianxiatouxiang" + ".jpg")) != null) {

            iv_clodfanAindex_touxiang.setImageBitmap(getLoacalBitmap(preference2.getString("touxiangbendiurl", Environment.getExternalStorageDirectory() + "/"
                    + "lianxiatouxiang" + ".jpg")));
        }

        tv_coldfanaindex_dizhi.setText(preference2.getString("district", ""));

        //初始化模式界面
        mod = preferences3.getInt("mod",100);
        switch (mod){
            case 1:
            iv_dryerindex_yinerhuli_gouxuan.setImageResource(R.mipmap.aircleandingshi2);
            tv_dryerindex_yinerhuli.setTextColor(getResources().getColor(R.color.white));
            iv_dryerindex_yinerhuli_next.setImageResource(R.mipmap.nextwhite);
            ll_dryerindex_yinerhuli.setBackgroundColor(getResources().getColor(R.color.airclean));
                iv_dryerindex_jiuyichushi_gouxuan.setImageResource(R.mipmap.aircleandingshi);
                tv_dryerindex_jiuyichushi.setTextColor(getResources().getColor(R.color.airclean));
                iv_dryerindex_jiuyichushi_next.setImageResource(R.mipmap.aircleannext);
                ll_dryerindex_jiuyichushi.setBackgroundColor(getResources().getColor(R.color.white));
                iv_dryerindex_zidingyi_gouxuan.setImageResource(R.mipmap.aircleandingshi);
                tv_dryerindex_zidingyi.setTextColor(getResources().getColor(R.color.airclean));
                iv_dryerindex_zidingyi_next.setImageResource(R.mipmap.aircleannext);
                ll_dryerindex_zidingyi.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 2:
                iv_dryerindex_yinerhuli_gouxuan.setImageResource(R.mipmap.aircleandingshi);
                tv_dryerindex_yinerhuli.setTextColor(getResources().getColor(R.color.airclean));
                iv_dryerindex_yinerhuli_next.setImageResource(R.mipmap.aircleannext);
                ll_dryerindex_yinerhuli.setBackgroundColor(getResources().getColor(R.color.white));
                iv_dryerindex_jiuyichushi_gouxuan.setImageResource(R.mipmap.aircleandingshi2);
                tv_dryerindex_jiuyichushi.setTextColor(getResources().getColor(R.color.white));
                iv_dryerindex_jiuyichushi_next.setImageResource(R.mipmap.nextwhite);
                ll_dryerindex_jiuyichushi.setBackgroundColor(getResources().getColor(R.color.airclean));
                iv_dryerindex_zidingyi_gouxuan.setImageResource(R.mipmap.aircleandingshi);
                tv_dryerindex_zidingyi.setTextColor(getResources().getColor(R.color.airclean));
                iv_dryerindex_zidingyi_next.setImageResource(R.mipmap.aircleannext);
                ll_dryerindex_zidingyi.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 3:
                iv_dryerindex_yinerhuli_gouxuan.setImageResource(R.mipmap.aircleandingshi);
                tv_dryerindex_yinerhuli.setTextColor(getResources().getColor(R.color.airclean));
                iv_dryerindex_yinerhuli_next.setImageResource(R.mipmap.aircleannext);
                ll_dryerindex_yinerhuli.setBackgroundColor(getResources().getColor(R.color.white));
                iv_dryerindex_jiuyichushi_gouxuan.setImageResource(R.mipmap.aircleandingshi);
                tv_dryerindex_jiuyichushi.setTextColor(getResources().getColor(R.color.airclean));
                iv_dryerindex_jiuyichushi_next.setImageResource(R.mipmap.aircleannext);
                ll_dryerindex_jiuyichushi.setBackgroundColor(getResources().getColor(R.color.white));
                iv_dryerindex_zidingyi_gouxuan.setImageResource(R.mipmap.aircleandingshi2);
                tv_dryerindex_zidingyi.setTextColor(getResources().getColor(R.color.white));
                iv_dryerindex_zidingyi_next.setImageResource(R.mipmap.nextwhite);
                ll_dryerindex_zidingyi.setBackgroundColor(getResources().getColor(R.color.airclean));
                break;
            default:
                break;
        }
        if(dryerShiyongshijian!=null){
            Log.wtf("执行了返回后倒计时","+++++++++++++++++++++++++");
            Message moshifanhui = new Message();
            moshifanhui.what = 4;
            handler.sendMessage(moshifanhui);
        }

                super.onResume();

    }

    //左右切换
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        SnsConstant snsConstant = new SnsConstant();
        try {
            if (e1.getX() - e2.getX() > snsConstant.getFlingMinDistance()
                    && Math.abs(velocityX) > snsConstant.getFlingMinVelocity()) {

//          切换Activity
//                Intent intent = new Intent(DryerIndex.this, coldfanAwork.class);
//                startActivity(intent);
//            Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();
            } else if (e2.getX() - e1.getX() > snsConstant.getFlingMinDistance()
                    && Math.abs(velocityX) > snsConstant.getFlingMinVelocity()) {

//          切换Activity
               OZApplication.getInstance().finishActivity();
//            Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public static class SnsConstant {
        private static final int FLING_MIN_DISTANCE = 300;
        private static final int FLING_MIN_VELOCITY = 0;

        public static int getFlingMinDistance() {
            return FLING_MIN_DISTANCE;
        }

        public static int getFlingMinVelocity() {
            return FLING_MIN_VELOCITY;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    //拉伸效果
    public void setupView() {
        img = (ImageView) findViewById(R.id.img);
        DampView view = (DampView) findViewById(R.id.dampview1);
        view.setImageView(img);
    }

    public void onImgClick(View view) {
//        Toast.makeText(this, "单击背景", Toast.LENGTH_SHORT).show();
    }

    public void onPhotoClick(View view) {
//        Toast.makeText(this, "单击图像", Toast.LENGTH_SHORT).show();
    }



    //广播接收按键改变
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction("broadcass1");            //添加动态广播的Action
        registerReceiver(receiver, dynamic_filter);// 注册自定义动态广播消息



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
                } else if (MainActivity.b[14]==0x02) {
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }

                if (MainActivity.b[15]==0x01) {
                    Message msg = new Message();
                    msg.what = 12;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[15]==0x02) {
                    Message msg = new Message();
                    msg.what = 13;
                    handler.sendMessage(msg);
                }

                if (MainActivity.b[16]==0x01) {
                    Message msg = new Message();
                    msg.what = 14;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[16]==0x02) {
                    Message msg = new Message();
                    msg.what = 15;
                    handler.sendMessage(msg);
                }


                if (MainActivity.b[17]==0x01) {
                    Message msg = new Message();
                    msg.what = 16;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[17]==0x02) {
                    Message msg = new Message();
                    msg.what = 17;
                    handler.sendMessage(msg);
                }

            }

        }
    };

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void showChart_shijian() {
        XYMultipleSeriesDataset mDataSet=getDataSet_shijian();
        XYMultipleSeriesRenderer mRefender=getRefender_shijian();
        chartView_shijian= ChartFactory.getLineChartView(this, mDataSet, mRefender);
        Dryerlishishuju_shijian.addView(chartView_shijian);
    }


    private XYMultipleSeriesDataset getDataSet_shijian() {
        XYMultipleSeriesDataset seriesDataset=new XYMultipleSeriesDataset();
//        XYSeries xySeries1=new XYSeries("室外空气质量");
//        xySeries1.add(1, 36);
//        xySeries1.add(2, 30);
//        xySeries1.add(3, 27);
//        xySeries1.add(4, 29);
//        xySeries1.add(5, 34);
//        xySeries1.add(6, 28);
//        xySeries1.add(7, 33);
//        seriesDataset.addSeries(xySeries1);

        XYSeries xySeries2=new XYSeries("历史使用时间");

        if (jilustate==1||jilustate==2){
            xySeries2.add(1, 0);
        }
        else {
            for (int i = 0; i < jilu.size(); i++) {
                xySeries2.add(i + 1, jilu.get(i).getUseTime() / 1000 / 3600);
            }
        }
//        xySeries2.add(1, 1);
//        xySeries2.add(2, 6);
//        xySeries2.add(3, 8);
//        xySeries2.add(4, 2);
//        xySeries2.add(5, 18);
//        xySeries2.add(6, 10);
//        xySeries2.add(7, 16);
        seriesDataset.addSeries(xySeries2);

        return seriesDataset;
    }
    private XYMultipleSeriesRenderer getRefender_shijian() {
        /*描绘器，设置图表整体效果，比如x,y轴效果，缩放比例，颜色设置*/
        XYMultipleSeriesRenderer seriesRenderer=new XYMultipleSeriesRenderer();

        seriesRenderer.setChartTitleTextSize(20);//设置图表标题的字体大小(图的最上面文字)
        seriesRenderer.setMargins(new int[] { 20, 80, 60, 60 });//设置外边距，顺序为：上左下右
        //坐标轴设置
        seriesRenderer.setAxisTitleTextSize(25);//设置坐标轴标题字体的大小
        seriesRenderer.setYAxisMin(0);//设置y轴的起始值
        seriesRenderer.setYAxisMax(12);//设置y轴的最大值
        seriesRenderer.setXAxisMin(0.5);//设置x轴起始值
        seriesRenderer.setXAxisMax(7.5);//设置x轴最大值
//        seriesRenderer.setXTitle("日期");//设置x轴标题
//        seriesRenderer.setYTitle("温度");//设置y轴标题
        //颜色设置
        seriesRenderer.setApplyBackgroundColor(true);//是应用设置的背景颜色
        seriesRenderer.setLabelsColor(0xFF85848D);//设置标签颜色
        seriesRenderer.setBackgroundColor(Color.WHITE);//设置图表的背景颜色
        //缩放设置
        seriesRenderer.setZoomButtonsVisible(false);//设置缩放按钮是否可见
        seriesRenderer.setZoomEnabled(false); //图表是否可以缩放设置
        seriesRenderer.setZoomInLimitX(7);
//      seriesRenderer.setZoomRate(1);//缩放比例设置
        //图表移动设置
        seriesRenderer.setPanEnabled(false);//图表是否可以移动

        //legend(最下面的文字说明)设置
        seriesRenderer.setShowLegend(true);//控制legend（说明文字 ）是否显示
        seriesRenderer.setLegendHeight(80);//设置说明的高度，单位px
        seriesRenderer.setLegendTextSize(30);//设置说明字体大小
        //坐标轴标签设置
        seriesRenderer.setXLabelsColor(Color.GRAY);
        seriesRenderer.setYLabelsColor(0,Color.GRAY);
        seriesRenderer.setLabelsTextSize(25);//设置标签字体大小
        seriesRenderer.setXLabelsAlign(Paint.Align.CENTER);
        seriesRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        seriesRenderer.setShowGrid(true);
        seriesRenderer.setAxesColor(Color.GRAY);
        seriesRenderer.setGridColor(Color.LTGRAY);
        seriesRenderer.setXLabels(0);//显示的x轴标签的个数
        if (jilustate==1||jilustate==2){
            seriesRenderer.addXTextLabel(1, "暂无数据");
        }
        else{
            for(int i = 0;i<jilu.size();i++){
                seriesRenderer.addXTextLabel(i+1,jilu.get(i).getUseDate().substring(5,10) );
            }
        }
//        seriesRenderer.addXTextLabel(1, "6/24");//针对特定的x轴值增加文本标签
//        seriesRenderer.addXTextLabel(2, "6/25");
//        seriesRenderer.addXTextLabel(3, "6/26");
//        seriesRenderer.addXTextLabel(4, "6/27");
//        seriesRenderer.addXTextLabel(5, "6/28");
//        seriesRenderer.addXTextLabel(6, "6/29");
//        seriesRenderer.addXTextLabel(7, "今天");
        seriesRenderer.setPointSize(6);//设置坐标点大小


        seriesRenderer.setMarginsColor(Color.WHITE);//设置外边距空间的颜色
        seriesRenderer.setClickEnabled(false);
//        seriesRenderer.setChartTitle("北京最近7天温度变化趋势图");

//        /*某一组数据的描绘器，描绘该组数据的个性化显示效果，主要是字体跟颜色的效果*/
//        XYSeriesRenderer xySeriesRenderer1=new XYSeriesRenderer();
//        xySeriesRenderer1.setColor(0xFFFF0000);//设置注释（注释可以着重标注某一坐标）的颜色
//        xySeriesRenderer1.setChartValuesTextAlign(Paint.Align.CENTER);//设置注释的位置
//        xySeriesRenderer1.setChartValuesTextSize(12);//设置注释文字的大小
//        xySeriesRenderer1.setPointStyle(PointStyle.CIRCLE);//坐标点的显示风格
//        xySeriesRenderer1.setPointStrokeWidth(8);//坐标点的大小
//        xySeriesRenderer1.setLineWidth(3);
//        xySeriesRenderer1.setFillPoints(true);
//        xySeriesRenderer1.setColor(0xFFF46C48);//表示该组数据的图或线的颜色
//        xySeriesRenderer1.setDisplayChartValues(false);//设置是否显示坐标点的y轴坐标值
//        xySeriesRenderer1.setChartValuesTextSize(12);//设置显示的坐标点值的字体大小

        /*某一组数据的描绘器，描绘该组数据的个性化显示效果，主要是字体跟颜色的效果*/
        XYSeriesRenderer xySeriesRenderer2=new XYSeriesRenderer();
        xySeriesRenderer2.setPointStyle(PointStyle.CIRCLE);//坐标点的显示风格
        xySeriesRenderer2.setPointStrokeWidth(8);//坐标点的大小
        xySeriesRenderer2.setColor(0xFF00C8FF);//表示该组数据的图或线的颜色
        xySeriesRenderer2.setDisplayChartValues(false);//设置是否显示坐标点的y轴坐标值
        xySeriesRenderer2.setChartValuesTextSize(12);//设置显示的坐标点值的字体大小
        xySeriesRenderer2.setFillPoints(true);
        xySeriesRenderer2.setLineWidth(3);
//        seriesRenderer.addSeriesRenderer(xySeriesRenderer1);
        seriesRenderer.addSeriesRenderer(xySeriesRenderer2);
        return seriesRenderer;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notruning = true;
        unregisterReceiver(receiver);
        ((OZApplication)getApplication()).SendOrder(SetPackage.GetMachineQuit(preference2.getString("workmachineid","123456789111"),preference2.getString("userSn","100000001"),preference2.getString("workmachinetypestring","A1")));
    }
}
