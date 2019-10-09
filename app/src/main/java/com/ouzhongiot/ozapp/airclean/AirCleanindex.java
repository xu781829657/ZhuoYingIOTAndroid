package com.ouzhongiot.ozapp.airclean;

import android.app.Instrumentation;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MainActivity;
import com.ouzhongiot.ozapp.activity.coldfanAindex;
import com.ouzhongiot.ozapp.activity.coldfanAyonghuzhongxin;
import com.ouzhongiot.ozapp.others.CircleImageView;
import com.ouzhongiot.ozapp.others.DampView;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.LoginOutActivity;
import com.ouzhongiot.ozapp.others.Post;
import com.ouzhongiot.ozapp.others.SetPackage;
import com.ouzhongiot.ozapp.tools.DensityUtil;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AirCleanindex extends LoginOutActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private NetworkInfo netInfo;
    private ConnectivityManager mConnectivityManager;
    private ImageView img;
    private Timer timer;
    private OutputStream outputStream;
    GestureDetector mGestureDetector;
    private TextView tv_coldfanaindex_wenxintishi;
    private boolean isopen1 = false;
    private boolean isopen2 = false;
    private boolean isopen3 = false;
    private LinearLayout ll_bjshoumin, ll_swzhuangtai, ll_lwjiedu;
    private TextView tv_AirCleanindex_kaiguan, tv_AirCleanindex_kaiguan0;
    private ImageView iv_AirCleanindex_kaiguan;
    private boolean ison;
    private GradientDrawable gradientDrawable;
    private SharedPreferences preference;
    private SharedPreferences preference2;
    private SharedPreferences.Editor editor2;
    private SharedPreferences preferences3;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editor3;
    private SharedPreferences preferencesmachine;
    private SharedPreferences.Editor editormachine;
    private CircleImageView iv_AirCleanindex_touxiang;
    private String workmachineid;
    private String workmachinetype;
    private boolean fSwitch,fAuto,fSleep,fUV,fAnion;
    public static int  cleanFilterScreen,changeFilterScreen;
    private int fWind,light;
//    private String heartpakage;
    private TextView tv_coldfanaindex_dizhi;
    private TextView tv_coldfanaindex_temperature, tv_coldfanaindex_quality, tv_coldfanaindex_humidity;
    private TextView tv_aircleanindex_leijishijian, tv_coldfanaindex_fencheng, tv_coldfanaindex_jiangwen, tv_AirCleanindex_dami,tv_AirCleanindex_fengchen;
    private ImageView iv_coldfanAindex_wether;
    private AircleanB1shuju aircleanB1shuju;
    private TextView tv_AirCleanindex_bingjingshouming, tv_AirCleanindex_bingjingxiantiao;
    LinearLayout chart,ll_airclean_kongzhitai;
    GraphicalView chartView;
    private AlertDialog.Builder builder;
    private ArcProgress arcProgress;
    private LinearLayout ll_airclean_waichumoshi,ll_airclean_zhinengmoshi,ll_airclean_zidingyimoshi,ll_airclean_zhoumomoshi,ll_airclean_touchuang;
    private ImageView iv_airclean_zidong,iv_airclean_shuimian,iv_airclean_shajun,iv_airclean_fulizi;
    private ImageView iv_AirCleanindex_jieduzhizhen1,iv_AirCleanindex_jieduzhizhen2,iv_AirCleanindex_jieduzhizhen3,iv_AirCleanindex_jieduzhizhen4;
    private TextView tv_airclean_zidong,tv_airclean_shuimian,tv_airclean_shajun,tv_airclean_fulizi;
    private LinearLayout ll_airclean_zidong,ll_airclean_shajun,ll_airclean_fulizi,ll_airclean_shuimian;
    private SeekBar aircleanseekBar;
    private int side;
    private int pm25;
    private String shiwaipm25,shineipm25;
    private TextView tv_airclean_shineishiwai,tv_airclean_mg,tv_airclean_youyushiwai,tv_aircleanindex_kongqizhiliangtishi;
    private ImageView iv_waichumoshigouxuan,iv_waichumoshinext,iv_zhoumomoshigouxuan,iv_zhoumomoshinext,iv_zhinengmoshigouxuan,iv_zhinengmoshinext,iv_zidingyimoshigouxuan,iv_zidingyimoshinext;
    private TextView tv_waichumoshi0,tv_waichumoshi1,tv_zhoumomoshi0,tv_zhoumomoshi1,tv_zhinengmoshi0,tv_zhinengmoshi1,tv_zidingyimoshi0,tv_zidingyimoshi1,tv_airclean_shouming,tv_airclean_jiedu;
    private Aircleanshineikongqi aircleanshineikongqi;
    private int DINGSHIMOSHI= 1;
    public static ArrayList<Integer> shiwaikongqizhiliang = new ArrayList<>();
    public static ArrayList<Integer> shiwaikongqishijian = new ArrayList<>();
    public static ArrayList<Integer> shineikongqizhiliang = new ArrayList<>();

    public static int kongqinumber = 0;
    private int dangqianshiwaikongqizhiliang,lasttime;




    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 检查为开启状态
                case 1:
                    iv_AirCleanindex_kaiguan.setImageResource(R.mipmap.open);
                    tv_AirCleanindex_kaiguan.setText("开启");
                    gradientDrawable.setColor(getResources().getColor(R.color.airclean));
                    fSwitch = true;
//                    traversalView(ll_airclean_kongzhitai);
                    ll_airclean_touchuang.setVisibility(View.GONE);
                    editormachine.putBoolean("fSwitch", fSwitch);
                    editormachine.commit();
                    break;
                case 2:
                    iv_AirCleanindex_kaiguan.setImageResource(R.mipmap.close);
                    tv_AirCleanindex_kaiguan.setText("关闭");
                    gradientDrawable.setColor(getResources().getColor(R.color.gray));
                    fSwitch = false;
                    ll_airclean_touchuang.setVisibility(View.VISIBLE);
//                    traversalView(ll_airclean_kongzhitai);
                    editormachine.putBoolean("fSwitch", fSwitch);
                    editormachine.commit();
                    break;
                case 3:
                    tv_coldfanaindex_wenxintishi.setText(preference2.getString("chuanyi", "今天气温很高，适合穿短袖，注意防晒和避暑,祝您有一个愉快的周末"));
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
                    if (aircleanB1shuju.getData() != null) {
                        DecimalFormat df = new DecimalFormat("######0.00");

                        tv_aircleanindex_leijishijian.setText(df.format((double) aircleanB1shuju.getData().getTotalTime() / 1000 / 3600) + "");
                        tv_AirCleanindex_fengchen.setText(df.format((double) aircleanB1shuju.getData().getTotalTime() / 1000 / 3600 * 0.3575) + "");
                        tv_AirCleanindex_dami.setText("相当于" + df.format((double) aircleanB1shuju.getData().getTotalTime() / 1000 / 3600 * 0.3575 / 25) + "粒大米");

                        editormachine.putString("leijishijian", df.format((double) aircleanB1shuju.getData().getTotalTime() / 1000 / 3600) + "");
                        editormachine.putString("fenchen", df.format((double) aircleanB1shuju.getData().getTotalTime() / 1000 / 3600 * 0.3575) + "");
                        editormachine.putString("dami", "相当于" + df.format((double) aircleanB1shuju.getData().getTotalTime() / 1000 / 3600 * 0.3575 / 25) + "粒大米");
                        editormachine.commit();
//                        tv_coldfanaindex_fencheng.setText(df.format((double) coldfanashuju.getData().getTotalTime() / 1000 / 3600 * 0.1158) + "");
//                        tv_coldfanaindex_jiangwen.setText(df.format(((((double) coldfanashuju.getData().getTotalTime() - coldfanashuju.getData().getTotalC()) * 2 +
//                                (double) coldfanashuju.getData().getTotalC() * 6)) / 1000 / 3600) + "");
//                        tv_AirCleanindex_dami.setText("相当于" + df.format(((((double) coldfanashuju.getData().getTotalTime() - coldfanashuju.getData().getTotalC()) * 2 +
//                                (double) coldfanashuju.getData().getTotalC() * 6)) / 1000 / 3600 / 90) + "粒大米");
                        //滤网寿命
                        tv_AirCleanindex_bingjingshouming.setText(changeFilterScreen + "");
                        tv_AirCleanindex_bingjingxiantiao.getLayoutParams().width = DensityUtil.dip2px(AirCleanindex.this, 320) * changeFilterScreen / 1600;
                        //滤网洁度

                        if(cleanFilterScreen>600){
                            iv_AirCleanindex_jieduzhizhen1.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen2.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen3.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen4.setVisibility(View.VISIBLE);
                        }
                        else if(cleanFilterScreen>400){
                            iv_AirCleanindex_jieduzhizhen1.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen2.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen3.setVisibility(View.VISIBLE);
                            iv_AirCleanindex_jieduzhizhen4.setVisibility(View.INVISIBLE);

                        }else if(cleanFilterScreen>200){
                            iv_AirCleanindex_jieduzhizhen1.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen2.setVisibility(View.VISIBLE);
                            iv_AirCleanindex_jieduzhizhen3.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen4.setVisibility(View.INVISIBLE);
                        } else {
                            iv_AirCleanindex_jieduzhizhen1.setVisibility(View.VISIBLE);
                            iv_AirCleanindex_jieduzhizhen2.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen3.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen4.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;
                case 5:
                    break;
                case 6:
                    tv_AirCleanindex_bingjingshouming.setText("1600");
                    tv_AirCleanindex_bingjingxiantiao.getLayoutParams().width = DensityUtil.dip2px(AirCleanindex.this, 320);

                    break;
                case 7:

                    break;
                case 8:
                    iv_airclean_zidong.setImageResource(R.mipmap.aircleanzidong0);
                    tv_airclean_zidong.setTextColor(getResources().getColor(R.color.white));
                    ll_airclean_zidong.setBackgroundColor(getResources().getColor(R.color.airclean));
                    fAuto = true;
                    editormachine.putBoolean("fAuto", fAuto);
                    editormachine.commit();
                    break;
                case 9:
                    iv_airclean_zidong.setImageResource(R.mipmap.aircleanzidong1);
                    tv_airclean_zidong.setTextColor(getResources().getColor(R.color.airclean));
                    ll_airclean_zidong.setBackgroundColor(getResources().getColor(R.color.white));
                    fAuto = false;
                    editormachine.putBoolean("fAuto", fAuto);
                    editormachine.commit();
                    break;
                case 10:
                    iv_airclean_shuimian.setImageResource(R.mipmap.aircleanshuimian0);
                    tv_airclean_shuimian.setTextColor(getResources().getColor(R.color.white));
                    ll_airclean_shuimian.setBackgroundColor(getResources().getColor(R.color.airclean));
                    fSleep = true;
                    editormachine.putBoolean("fSleep", fSleep);
                    editormachine.commit();
                    break;
                case 11:
                    iv_airclean_shuimian.setImageResource(R.mipmap.aircleanshuimian1);
                    tv_airclean_shuimian.setTextColor(getResources().getColor(R.color.airclean));
                    ll_airclean_shuimian.setBackgroundColor(getResources().getColor(R.color.white));
                    fSleep = false;
                    editormachine.putBoolean("fSleep", fSleep);
                    editormachine.commit();
                    break;
                case 12:
                    iv_airclean_shajun.setImageResource(R.mipmap.aircleanshajun0);
                    tv_airclean_shajun.setTextColor(getResources().getColor(R.color.white));
                    ll_airclean_shajun.setBackgroundColor(getResources().getColor(R.color.airclean));
                    fUV = true;
                    editormachine.putBoolean("fUV", fUV);
                    editormachine.commit();
                    break;
                case 13:
                    iv_airclean_shajun.setImageResource(R.mipmap.aircleanshajun1);
                    tv_airclean_shajun.setTextColor(getResources().getColor(R.color.airclean));
                    ll_airclean_shajun.setBackgroundColor(getResources().getColor(R.color.white));
                    fUV = false;
                    editormachine.putBoolean("fUV", fUV);
                    editormachine.commit();
                    break;
                case 14:
                    iv_airclean_fulizi.setImageResource(R.mipmap.aircleanfulizi0);
                    tv_airclean_fulizi.setTextColor(getResources().getColor(R.color.white));
                    ll_airclean_fulizi.setBackgroundColor(getResources().getColor(R.color.airclean));
                    fAnion = true;
                    editormachine.putBoolean("fAnion", fAnion);
                    editormachine.commit();
                    break;
                case 15:
                    iv_airclean_fulizi.setImageResource(R.mipmap.aircleanfulizi1);
                    tv_airclean_fulizi.setTextColor(getResources().getColor(R.color.airclean));
                    ll_airclean_fulizi.setBackgroundColor(getResources().getColor(R.color.white));
                    fAnion = false;
                    editormachine.putBoolean("fAnion", fAnion);
                    editormachine.commit();
                    break;
                case 16:
                    aircleanseekBar.setProgress(0);
                    fWind = 1;
                    editormachine.putInt("fWind", fWind);
                    editormachine.commit();
                    break;
                case 17:
                    aircleanseekBar.setProgress(50);
                    fWind = 2;
                    editormachine.putInt("fWind", fWind);
                    editormachine.commit();
                    break;
                case 18:
                    aircleanseekBar.setProgress(100);
                    fWind = 3;
                    editormachine.putInt("fWind", fWind);
                    editormachine.commit();
                    break;
                case 19:
                    Log.wtf("室内空气质量室外空气质量对比",pm25+"        "+dangqianshiwaikongqizhiliang);
                        if (pm25 > dangqianshiwaikongqizhiliang) {
                            tv_airclean_mg.setVisibility(View.GONE);
                            tv_airclean_shineishiwai.setText("室外空气优于室内");
                            tv_airclean_youyushiwai.setText("请开窗");
                        } else {
                            DecimalFormat df = new DecimalFormat("######0.00");
                            tv_airclean_youyushiwai.setText(df.format((double) (dangqianshiwaikongqizhiliang- pm25) / dangqianshiwaikongqizhiliang * 100) + "");
                        }
                        editormachine.putString("pm25",pm25+"");
                        editormachine.putInt("dangqianshiwaipm25", dangqianshiwaikongqizhiliang);
                        editormachine.commit();


                    showChart();


                    break;
                case 20:
                    tv_aircleanindex_kongqizhiliangtishi.setText("优");
                    break;
                case 21:
                    tv_aircleanindex_kongqizhiliangtishi.setText("良");
                    break;
                case 22:
                    tv_aircleanindex_kongqizhiliangtishi.setText("差");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_cleanindex);
        outputStream  = ((OZApplication)getApplication()).outputStream;
        tv_airclean_shouming = (TextView) findViewById(R.id.tv_airclean_shouming);
        tv_airclean_jiedu = (TextView) findViewById(R.id.tv_airclean_jiedu);
        preference2 = getSharedPreferences("data", MODE_PRIVATE);
        editor2 = preference2.edit();
        workmachineid = preference2.getString("workmachineid", "");
        workmachinetype = preference2.getString("workmachinetype", "");

        //请求当前状态
        preferencesmachine = getSharedPreferences(workmachineid,MODE_PRIVATE);
        editormachine = preferencesmachine.edit();
        fSwitch = preferencesmachine.getBoolean("fSwitch",false);
        fAuto = preferencesmachine.getBoolean("fAuto",false);
        fSleep = preferencesmachine.getBoolean("fSleep",false);
        fAnion = preferencesmachine.getBoolean("fAnion",false);
        fUV = preferencesmachine.getBoolean("fUV",false);
        fWind = preferencesmachine.getInt("fWind",1);
        pm25 =Integer.parseInt( preferencesmachine.getString("sPm25","48"))/4;
        cleanFilterScreen = preferencesmachine.getInt("sCleanFilterScreen",1600);
        changeFilterScreen = preferencesmachine.getInt("sChangeFilterScreen",800);
        light = preferencesmachine.getInt("fLight",1);
        if (cleanFilterScreen<8)
        {
            tv_airclean_jiedu.setVisibility(View.VISIBLE);
        }else {
            tv_airclean_jiedu.setVisibility(View.GONE);
        }

        if (changeFilterScreen<16)
        {
            tv_airclean_shouming.setVisibility(View.VISIBLE);
        }else {
            tv_airclean_shouming.setVisibility(View.GONE);
        }
        iv_AirCleanindex_jieduzhizhen1 = (ImageView) findViewById(R.id.iv_AirCleanindex_jieduzhizhen1);
        iv_AirCleanindex_jieduzhizhen2 = (ImageView) findViewById(R.id.iv_AirCleanindex_jieduzhizhen2);
        iv_AirCleanindex_jieduzhizhen3 = (ImageView) findViewById(R.id.iv_AirCleanindex_jieduzhizhen3);
        iv_AirCleanindex_jieduzhizhen4 = (ImageView) findViewById(R.id.iv_AirCleanindex_jieduzhizhen4);

        tv_airclean_mg  = (TextView) findViewById(R.id.tv_airclean_mg);
        tv_airclean_shineishiwai  = (TextView) findViewById(R.id.tv_airclean_shineishiwai);
        tv_airclean_youyushiwai  = (TextView) findViewById(R.id.tv_aircleanindex_youyushiwai);
        ll_airclean_waichumoshi = (LinearLayout) findViewById(R.id.ll_airclean_waichumoshi);
        ll_airclean_touchuang = (LinearLayout) findViewById(R.id.ll_airclean_touchuang);
        ll_airclean_zhinengmoshi = (LinearLayout) findViewById(R.id.ll_airclean_zhinengmoshi);
        ll_airclean_zidingyimoshi = (LinearLayout) findViewById(R.id.ll_airclean_zidingyimoshi);
        ll_airclean_zhoumomoshi = (LinearLayout) findViewById(R.id.ll_airclean_zhoumomoshi);

        tv_aircleanindex_kongqizhiliangtishi = (TextView) findViewById(R.id.tv_aircleanindex_kongqizhiliangtishi);
        iv_waichumoshigouxuan = (ImageView) findViewById(R.id.iv_waichumoshigouxuan);
        iv_waichumoshinext = (ImageView) findViewById(R.id.iv_waichumoshinext);
        iv_zhoumomoshigouxuan = (ImageView) findViewById(R.id.iv_zhoumomoshigouxuan);
        iv_zhoumomoshinext = (ImageView) findViewById(R.id.iv_zhoumomoshinext);
        iv_zhinengmoshigouxuan = (ImageView) findViewById(R.id.iv_zhinengmoshigouxuan);
        iv_zhinengmoshinext = (ImageView) findViewById(R.id.iv_zhinengmoshinext);
        iv_zidingyimoshigouxuan = (ImageView) findViewById(R.id.iv_zidingyimoshigouxuan);
        iv_zidingyimoshinext = (ImageView) findViewById(R.id.iv_zidingyimoshinext);
        tv_waichumoshi0 = (TextView) findViewById(R.id.tv_waichumoshi0);
        tv_waichumoshi1 = (TextView) findViewById(R.id.tv_waichumoshi1);
        tv_zhoumomoshi0 = (TextView) findViewById(R.id.tv_zhoumomoshi0);
        tv_zhoumomoshi1 = (TextView) findViewById(R.id.tv_zhoumomoshi1);
        tv_zhinengmoshi0 = (TextView) findViewById(R.id.tv_zhinengmoshi0);
        tv_zhinengmoshi1 = (TextView) findViewById(R.id.tv_zhinengmoshi1);
        tv_zidingyimoshi0 = (TextView) findViewById(R.id.tv_zidingyimoshi0);
        tv_zidingyimoshi1 = (TextView) findViewById(R.id.tv_zidingyimoshi1);
        ll_airclean_kongzhitai = (LinearLayout) findViewById(R.id.ll_airclean_kongzhitai);

        //定时
        ll_airclean_waichumoshi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AirCleanindex.this,aircleanwaichumoshi.class);
                        startActivity(intent);
                    }
                });
        ll_airclean_zhinengmoshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AirCleanindex.this,aircleanzhinengmoshi.class);
                startActivity(intent);
            }
        });
        ll_airclean_zhoumomoshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AirCleanindex.this,aircleanzhoumomoshi.class);
                startActivity(intent);
            }
        });

        ll_airclean_zidingyimoshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AirCleanindex.this,aircleanzidingyimoshi.class);
                startActivity(intent);
            }
        });

//       跑分质量
        arcProgress = (ArcProgress) findViewById(R.id.arc_progress);

        //这个是跑分
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(arcProgress.getProgress()>=pm25){

                        }else
                        {
                            arcProgress.setProgress(arcProgress.getProgress() + 1);
                        }

                    }
                });
            }
        }, 1000, 50);

        chart = (LinearLayout) findViewById(R.id.AirCleanchart);
        tv_AirCleanindex_fengchen = (TextView) findViewById(R.id.tv_AirCleanindex_fengchen);
        iv_airclean_fulizi = (ImageView) findViewById(R.id.iv_airclean_fulizi);
        iv_airclean_zidong = (ImageView) findViewById(R.id.iv_airclean_zidong);
        iv_airclean_shajun = (ImageView) findViewById(R.id.iv_airclean_shajun);
        iv_airclean_shuimian = (ImageView) findViewById(R.id.iv_airclean_shuimian);
        tv_airclean_fulizi = (TextView) findViewById(R.id.tv_airclean_fulizi);
        tv_airclean_zidong = (TextView) findViewById(R.id.tv_airclean_zidong);
        tv_airclean_shajun = (TextView) findViewById(R.id.tv_airclean_shajun);
        tv_airclean_shuimian = (TextView) findViewById(R.id.tv_airclean_shuimian);
        ll_airclean_fulizi = (LinearLayout) findViewById(R.id.ll_airclean_fulizi);
        ll_airclean_zidong = (LinearLayout) findViewById(R.id.ll_airclean_zidong);
        ll_airclean_shajun = (LinearLayout) findViewById(R.id.ll_airclean_shajun);
        ll_airclean_shuimian = (LinearLayout) findViewById(R.id.ll_airclean_shuimian);
        aircleanseekBar = (SeekBar) findViewById(R.id.aircleanseekBar);


        tv_AirCleanindex_bingjingxiantiao = (TextView) findViewById(R.id.tv_AirCleanindex_bingjingxiantiao);
        tv_AirCleanindex_bingjingshouming = (TextView) findViewById(R.id.tv_AirCleanindex_bingjingshouming);
        tv_AirCleanindex_dami = (TextView) findViewById(R.id.tv_AirCleanindex_dami);
        tv_coldfanaindex_jiangwen = (TextView) findViewById(R.id.tv_coldfanaindex_jiangwen);
        tv_coldfanaindex_fencheng = (TextView) findViewById(R.id.tv_coldfanaindex_fencheng);
        tv_aircleanindex_leijishijian = (TextView) findViewById(R.id.tv_AirCleanindex_leijishijian);
        ll_bjshoumin = (LinearLayout) findViewById(R.id.ll_bjshoumin);
        ll_swzhuangtai = (LinearLayout) findViewById(R.id.ll_swzhuangtai);
        ll_lwjiedu = (LinearLayout) findViewById(R.id.ll_lwjiedu);
        iv_AirCleanindex_touxiang = (CircleImageView) findViewById(R.id.iv_AirCleanindex_touxiang);
        tv_coldfanaindex_dizhi = (TextView) findViewById(R.id.tv_coldfanaindex_dizhi);
        iv_AirCleanindex_kaiguan = (ImageView) findViewById(R.id.iv_AirCleanindex_kaiguan);
        tv_AirCleanindex_kaiguan = (TextView) findViewById(R.id.tv_AirCleanindex_kaiguan);
        tv_AirCleanindex_kaiguan0 = (TextView) findViewById(R.id.tv_AirCleanindex_kaiguan0);
        gradientDrawable = (GradientDrawable) tv_AirCleanindex_kaiguan0.getBackground();
        preference = getSharedPreferences("coldfanA", MODE_PRIVATE);
        editor = preference.edit();
        tv_coldfanaindex_humidity = (TextView) findViewById(R.id.tv_coldfanaindex_humidity);
        tv_coldfanaindex_quality = (TextView) findViewById(R.id.tv_coldfanaindex_quality);
        tv_coldfanaindex_temperature = (TextView) findViewById(R.id.tv_coldfanaindex_temperature);
        iv_coldfanAindex_wether = (ImageView) findViewById(R.id.iv_coldfanaindex_wether);

        tv_coldfanaindex_wenxintishi = (TextView) findViewById(R.id.tv_coldfanaindex_wenxintishi);
        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener) this);
        DampView viewSnsLayout = (DampView) findViewById(R.id.dampview1);
        viewSnsLayout.setOnTouchListener(this);
        viewSnsLayout.setLongClickable(true);
        setupView();


        //获取室外pm2.5
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求当前数据
                String uriAPI2 = MainActivity.ip + "smarthome/weather/queryAqi";
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("city",  preference2.getString("city", "杭州市")));
                params2.add(new BasicNameValuePair("times","12"));
                shiwaipm25 = Post.dopost(uriAPI2, params2);
                Log.wtf("室外空气质量",shiwaipm25);
                Map<String, Object> map = JacksonUtil.deserializeJsonToObject(shiwaipm25, Map.class);
                String datastr = JacksonUtil.serializeObjectToJson(map.get("data"));
                Map<String, Object> data = JacksonUtil.deserializeJsonToObject(datastr, Map.class);
                if (data == null)
                {}else {
                    String shiwaipm25str = JacksonUtil.serializeObjectToJson(data.get("pm25"));
//                    List<Map<String,String>> pm25list = JacksonUtil.deserializeJsonToListMap(shiwaipm25str,String.class,String.class);
                    LinkedHashMap<String, Integer> shiwaipm25map = JacksonUtil.deserializeJsonToObject(shiwaipm25str, LinkedHashMap.class);
                    String key = "";
                    dangqianshiwaikongqizhiliang = 0;
                    for (Map.Entry<String, Integer> entry : shiwaipm25map.entrySet()) {

                        shiwaikongqishijian.add(Integer.parseInt(entry.getKey()));
//                        Log.wtf("这个是每个点的时间",entry.getKey());
                        key = entry.getKey();
                        if (entry.getValue() != null) {
                            dangqianshiwaikongqizhiliang = entry.getValue()+30;

                            shiwaikongqizhiliang.add(entry.getValue());
                            lasttime = Integer.parseInt(entry.getKey());
                        }
                        else
                        {
                            shiwaikongqizhiliang.add(shiwaikongqizhiliang.get(shiwaikongqizhiliang.size()-1));
                        }
//                        Log.wtf("这个是最新室外的空气质量", dangqianshiwaikongqizhiliang + "----------------------------------------"+shiwaikongqizhiliang.size());
                    }
                }




                String uriAPI = MainActivity.ip + "smarthome/air/queryDeviceAirAqi";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                params.add(new BasicNameValuePair("devSn", workmachineid));
                params.add(new BasicNameValuePair("lastTime",lasttime+""));
                params.add(new BasicNameValuePair("times","12"));
                shineipm25 = Post.dopost(uriAPI, params);
                Log.wtf("室内空气质量---------",shineipm25);
                Map<String, Object> map2 = JacksonUtil.deserializeJsonToObject(shineipm25, Map.class);

                if (map2.get("data") == null)
                {}else {
                    String datastr2 = JacksonUtil.serializeObjectToJson(map2.get("data"));
                    LinkedHashMap<String, Integer> shiwaipm25map2 = JacksonUtil.deserializeJsonToObject(datastr2, LinkedHashMap.class);
                    for (Map.Entry<String, Integer> entry : shiwaipm25map2.entrySet()) {
                        if(entry.getValue()!=null)
                        {
                            pm25 = entry.getValue()/4;
                        }

                        shineikongqizhiliang.add(entry.getValue());
                    }
                }
//                Log.wtf("这个是室内空气质量的长度","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!======="+shineikongqizhiliang.size());
                for(int i = 0,j=0;i<shineikongqizhiliang.size();i++)
                {
                    if(shineikongqizhiliang.get(i)!=null){
                        break;
                    }else {
                        j++;
                        kongqinumber = j;
                    }



                }

                Message msg = new Message();
                msg.what = 19;
                handler.sendMessage(msg);






            }
        }).start();


        //初始化数据--累计时间
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求当前数据
                String uriAPI2 = MainActivity.ip + "smarthome/air/queryDeviceData";
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                params2.add(new BasicNameValuePair("devSn", workmachineid));
                String str2 = Post.dopost(uriAPI2, params2);
                Log.wtf("这个是收到的机器数据",str2+workmachinetype+workmachineid);
                aircleanB1shuju = AircleanB1shuju.objectFromData(str2);
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);

            }
        }).start();

//        //异步加载使用记录
//       new Thread(new Runnable() {
//           @Override
//           public void run() {
//
//           }
//       }).start();
        //更多数据
        this.findViewById(R.id.airclean_gengduoshuju).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    Intent intent = new Intent(AirCleanindex.this,Aircleangengduoshuju.class);
                    startActivity(intent);
                    }
                });


        //初始化天气
        tv_coldfanaindex_wenxintishi.setText(preference2.getString("chuanyi", "今天气温很高，适合穿短袖，注意防晒和避暑,祝您有一个愉快的周末"));
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
        this.findViewById(R.id.ll_AirCleanindex_kaiguan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (fSwitch) {

//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"S",(byte)0x02));
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        Log.wtf("命令发送报错","5451--------------------+++++++++++++++++++");
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"S",(byte)0x02));
//                    ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"Q",(byte)0x01));

                } else {
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"S",(byte)0x01));
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        Log.wtf("命令发送报错","5451--------------------+++++++++++++++++++");
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"S",(byte)0x01));
//                    ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"Q",(byte)0x01));
                }
            }
        });
        //自动
        this.findViewById(R.id.ll_airclean_zidong).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (fAuto) {

//                            try {
//                                ((OZApplication)getApplication()).getOutputStream().write();
//                                ((OZApplication)getApplication()).getOutputStream().flush();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"A",(byte)0x02));

                        } else {
//                            try {
//                                ((OZApplication)getApplication()).getOutputStream().write();
//                                ((OZApplication)getApplication()).getOutputStream().flush();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"A",(byte)0x01));
                        }
                    }
                });
        //睡眠
        this.findViewById(R.id.ll_airclean_shuimian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fSleep) {

//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"L",(byte)0x02));

                } else {
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"L",(byte)0x01));
                }
            }
        });
        //杀菌
        this.findViewById(R.id.ll_airclean_shajun).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fUV) {

//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"U",(byte)0x02));

                } else {
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"U",(byte)0x01));
                }
            }
        });
        //负离子
        this.findViewById(R.id.ll_airclean_fulizi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fAnion) {

//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"N",(byte)0x02));
                } else {
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"N",(byte)0x01));
                }
            }
        });
        //风速
        aircleanseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                    ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"W",(byte)0x01));
                } else if (side < 75) {
                    seekBar.setProgress(50);
//                    fengshu = 2;
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*W2*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"W",(byte)0x02));
                } else {
                    seekBar.setProgress(100);
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*W3*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"W",(byte)0x03));
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"W",(byte)0x03));
                }

            }
        });
        //设置
        this.findViewById(R.id.iv_AirCleanindex_shezhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AirCleanindex.this, coldfanAyonghuzhongxin.class);
                startActivity(intent);
            }
        });

        //头像
        iv_AirCleanindex_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AirCleanindex.this, coldfanAyonghuzhongxin.class);
                startActivity(intent);
            }
        });


        //点击展开状态
        this.findViewById(R.id.tv_bjshoumin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isopen1) {
                    ll_bjshoumin.setVisibility(View.GONE);
                    isopen1 = false;
                } else {
                    ll_bjshoumin.setVisibility(View.VISIBLE);
                    isopen1 = true;
                }
            }
        });




        this.findViewById(R.id.tv_lwjiedu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isopen3) {
                    ll_lwjiedu.setVisibility(View.GONE);
                    isopen3 = false;
                } else {
                    ll_lwjiedu.setVisibility(View.VISIBLE);
                    isopen3 = true;
                }
            }
        });
        if (!((OZApplication)getApplication()).getISTOUXIANGUPDATE())
        {
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv_AirCleanindex_touxiang,
                    R.mipmap.test, R.mipmap.test);
            Log.wtf("头像地址",preference2.getString("headImageUrl","null"));
            if (!preference2.getString("headImageUrl","null").equals("null"))
            {
                ((OZApplication)getApplication()).getImageLoader().get(preference2.getString("headImageUrl","null"), listener);
            }


        }

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
                    editor2.putString("des", des);
                    editor2.putString("chuanyi",chuanyilist.get(1));
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
        //自动
        if (fAuto) {
            Message msg = new Message();
            msg.what = 8;
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 9;
            handler.sendMessage(msg);
        }
        //睡眠
        if (fSleep) {
            Message msg = new Message();
            msg.what = 10;
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 11;
            handler.sendMessage(msg);
        }
        //杀菌
        if (fUV) {
            Message msg = new Message();
            msg.what = 12;
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 13;
            handler.sendMessage(msg);
        }
        //负离子
        if (fAnion) {
            Message msg = new Message();
            msg.what = 14;
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 15;
            handler.sendMessage(msg);
        }
        //风速
        switch (fWind){
            case 1:
                Message msg = new Message();
                msg.what = 16;
                handler.sendMessage(msg);
                break;
            case 2:
                Message msg2 = new Message();
                msg2.what = 17;
                handler.sendMessage(msg2);
                break;
            case 3:
                Message msg3 = new Message();
                msg3.what = 18;
                handler.sendMessage(msg3);
                break;
            default:
                break;
        }


        //空气质量优良
        switch (light){
            case 1:
                Message msg = new Message();
                msg.what = 20;
                handler.sendMessage(msg);
                break;
            case 2:
                Message msg2 = new Message();
                msg2.what = 21;
                handler.sendMessage(msg2);
                break;
            case 3:
                Message msg3 = new Message();
                msg3.what = 22;
                handler.sendMessage(msg3);
                break;
            default:
                break;
        }



//        Log.wtf("这个是读取的图片地址",preference2.getString("touxiangbendiurl",""));
        if (((OZApplication)getApplication()).getISTOUXIANGUPDATE())
        {
            if (coldfanAindex.getLoacalBitmap(preference2.getString("touxiangbendiurl",Environment.getExternalStorageDirectory() + "/"
                    + "lianxiatouxiang" + ".jpg"))!=null){

                iv_AirCleanindex_touxiang.setImageBitmap(coldfanAindex.getLoacalBitmap(preference2.getString("touxiangbendiurl", Environment.getExternalStorageDirectory() + "/"
                        + "lianxiatouxiang" + ".jpg")));
            }
        }


        tv_coldfanaindex_dizhi.setText(preference2.getString("district", ""));

        //定时模式
        iv_waichumoshigouxuan.setImageResource(R.mipmap.aircleandingshi0);
        iv_waichumoshinext.setImageResource(R.mipmap.next);
        tv_waichumoshi0.setTextColor(getResources().getColor(R.color.gray));
        tv_waichumoshi1.setTextColor(getResources().getColor(R.color.gray));
        iv_zhinengmoshigouxuan.setImageResource(R.mipmap.aircleandingshi0);
        iv_zhinengmoshinext.setImageResource(R.mipmap.next);
        tv_zhinengmoshi0.setTextColor(getResources().getColor(R.color.gray));
        tv_zhinengmoshi1.setTextColor(getResources().getColor(R.color.gray));
        iv_zhoumomoshigouxuan.setImageResource(R.mipmap.aircleandingshi0);
        iv_zhoumomoshinext.setImageResource(R.mipmap.next);
        tv_zhoumomoshi0.setTextColor(getResources().getColor(R.color.gray));
        tv_zhoumomoshi1.setTextColor(getResources().getColor(R.color.gray));
        iv_zidingyimoshigouxuan.setImageResource(R.mipmap.aircleandingshi0);
        iv_zidingyimoshinext.setImageResource(R.mipmap.next);
        tv_zidingyimoshi0.setTextColor(getResources().getColor(R.color.gray));
        tv_zidingyimoshi1.setTextColor(getResources().getColor(R.color.gray));
        DINGSHIMOSHI = preferencesmachine.getInt("DINGSHIMOSHI",1);
        switch (DINGSHIMOSHI){
            case 1:
                iv_waichumoshigouxuan.setImageResource(R.mipmap.aircleandingshi);
                iv_waichumoshinext.setImageResource(R.mipmap.aircleannext);
                tv_waichumoshi0.setTextColor(getResources().getColor(R.color.airclean));
                tv_waichumoshi1.setTextColor(getResources().getColor(R.color.airclean));
                break;
            case 2:
                iv_zhoumomoshigouxuan.setImageResource(R.mipmap.aircleandingshi);
                iv_zhoumomoshinext.setImageResource(R.mipmap.aircleannext);
                tv_zhoumomoshi0.setTextColor(getResources().getColor(R.color.airclean));
                tv_zhoumomoshi1.setTextColor(getResources().getColor(R.color.airclean));
                break;
            case 3:
                iv_zhinengmoshigouxuan.setImageResource(R.mipmap.aircleandingshi);
                iv_zhinengmoshinext.setImageResource(R.mipmap.aircleannext);
                tv_zhinengmoshi0.setTextColor(getResources().getColor(R.color.airclean));
                tv_zhinengmoshi1.setTextColor(getResources().getColor(R.color.airclean));
                break;
            case 4:
                iv_zidingyimoshigouxuan.setImageResource(R.mipmap.aircleandingshi);
                iv_zidingyimoshinext.setImageResource(R.mipmap.aircleannext);
                tv_zidingyimoshi0.setTextColor(getResources().getColor(R.color.airclean));
                tv_zidingyimoshi1.setTextColor(getResources().getColor(R.color.airclean));
                break;
            default:
                break;
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

////          切换Activity
//                Intent intent = new Intent(AirCleanindex.this, coldfanAwork.class);
//                startActivity(intent);
//            Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();
            } else if (e2.getX() - e1.getX() > snsConstant.getFlingMinDistance()
                    && Math.abs(velocityX) > snsConstant.getFlingMinVelocity()) {

//          切换Activity
                onBack();
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
                //开关
                if (MainActivity.b[13]==0x01) {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[13]==0x02) {
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
                //自动
                if (MainActivity.b[14]==0x01) {
                    Message msg = new Message();
                    msg.what = 8;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[14]==0x02) {
                    Message msg = new Message();
                    msg.what = 9;
                    handler.sendMessage(msg);
                }
                //杀菌
                if (MainActivity.b[15]==0x01) {
                    Message msg = new Message();
                    msg.what = 12;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[15]==0x02) {
                    Message msg = new Message();
                    msg.what = 13;
                    handler.sendMessage(msg);
                }
                //睡眠
                if (MainActivity.b[16]==0x01) {
                    Message msg = new Message();
                    msg.what = 10;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[16]==0x02) {
                    Message msg = new Message();
                    msg.what = 11;
                    handler.sendMessage(msg);
                }
                //负离子
                if (MainActivity.b[17]==0x01) {
                    Message msg = new Message();
                    msg.what = 14;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[17]==0x02) {
                    Message msg = new Message();
                    msg.what = 15;
                    handler.sendMessage(msg);
                }
                //风速
                if (MainActivity.b[18]==0x01) {
                    Message msg = new Message();
                    msg.what = 16;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[18]==0x02) {
                    Message msg = new Message();
                    msg.what = 17;
                    handler.sendMessage(msg);
                }else if (MainActivity.b[18]==0x03) {
                    Message msg = new Message();
                    msg.what = 18;
                    handler.sendMessage(msg);
                }
                //指示灯
                if (MainActivity.b[26]==0x01) {
                    Message msg = new Message();
                    msg.what = 20;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[26]==0x02) {
                    Message msg = new Message();
                    msg.what = 21;
                    handler.sendMessage(msg);
                }else if (MainActivity.b[26]==0x03) {
                    Message msg = new Message();
                    msg.what = 22;
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


    private void showChart() {
        XYMultipleSeriesDataset mDataSet=getDataSet();
        XYMultipleSeriesRenderer mRefender=getRefender();
        chartView= ChartFactory.getLineChartView(this, mDataSet, mRefender);
        chart.addView(chartView);
    }
    private XYMultipleSeriesDataset getDataSet() {
        XYMultipleSeriesDataset seriesDataset=new XYMultipleSeriesDataset();
        XYSeries xySeries1=new XYSeries("室外空气质量");
//        xySeries1.add(1, 36);
//        xySeries1.add(2, 30);
//        xySeries1.add(3, 27);
//        xySeries1.add(4, 29);
//        xySeries1.add(5, 34);
//        xySeries1.add(6, 28);
//        xySeries1.add(7, 33);

            for(int i = kongqinumber,j = 1;i<12;i++,j++)
            {
//                Log.wtf("室外空气的Y轴",shiwaikongqizhiliang.get(i)+"----------------------"+kongqinumber+"i:"+i+"   j:"+j+"      这个是长度"+shiwaikongqizhiliang.size());
                xySeries1.add(j,shiwaikongqizhiliang.get(i));
            }


        seriesDataset.addSeries(xySeries1);

        XYSeries xySeries2=new XYSeries("室内空气质量");
//        xySeries2.add(1, 27);
//        xySeries2.add(2, 22);
//        xySeries2.add(3, 20);
//        xySeries2.add(4, 21);
//        xySeries2.add(5, 25);
//        xySeries2.add(6, 22);
//        xySeries2.add(7, 23);



            for(int i = kongqinumber,j = 1;i<12;i++,j++)
            {
                Log.wtf("室外空气的x轴",shineikongqizhiliang.get(i)+"~~~~~~~~~~~~~~~~~~~~~~~~~~~"+i);
                if(shineikongqizhiliang.get(i)==null)
                {

                }
                else
                {
                    xySeries2.add(j,shineikongqizhiliang.get(i)/4);
                }

            }


        seriesDataset.addSeries(xySeries2);

        return seriesDataset;
    }
    private XYMultipleSeriesRenderer getRefender() {
        /*描绘器，设置图表整体效果，比如x,y轴效果，缩放比例，颜色设置*/
        XYMultipleSeriesRenderer seriesRenderer=new XYMultipleSeriesRenderer();

        seriesRenderer.setChartTitleTextSize(20);//设置图表标题的字体大小(图的最上面文字)
        seriesRenderer.setMargins(new int[] { 20, 80, 60, 60 });//设置外边距，顺序为：上左下右
        //坐标轴设置
        seriesRenderer.setAxisTitleTextSize(25);//设置坐标轴标题字体的大小
        seriesRenderer.setYAxisMin(0);//设置y轴的起始值
        seriesRenderer.setYAxisMax(200);//设置y轴的最大值
        seriesRenderer.setXAxisMin(0.5);//设置x轴起始值
        seriesRenderer.setXAxisMax(12.5);//设置x轴最大值
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
//        seriesRenderer.addXTextLabel(1, "0点");//针对特定的x轴值增加文本标签
//        seriesRenderer.addXTextLabel(6, "12点");
//        seriesRenderer.addXTextLabel(12, "24点");

        for(int i = kongqinumber,j = 1;i<shiwaikongqishijian.size();i++,j++)
        {
//            Log.wtf("这个是时间---------------------",shiwaikongqishijian.get(i).toString()+"            "+i);
            seriesRenderer.addXTextLabel(j,shiwaikongqishijian.get(i).toString());
        }
//        seriesRenderer.addXTextLabel(4, "6/27");
//        seriesRenderer.addXTextLabel(5, "6/28");
//        seriesRenderer.addXTextLabel(6, "6/29");
//        seriesRenderer.addXTextLabel(7, "今天");
        seriesRenderer.setPointSize(6);//设置坐标点大小


        seriesRenderer.setMarginsColor(Color.WHITE);//设置外边距空间的颜色
        seriesRenderer.setClickEnabled(false);
//        seriesRenderer.setChartTitle("北京最近7天温度变化趋势图");

        /*某一组数据的描绘器，描绘该组数据的个性化显示效果，主要是字体跟颜色的效果*/
        XYSeriesRenderer xySeriesRenderer1=new XYSeriesRenderer();
        xySeriesRenderer1.setColor(0xFFFF0000);//设置注释（注释可以着重标注某一坐标）的颜色
        xySeriesRenderer1.setChartValuesTextAlign(Paint.Align.CENTER);//设置注释的位置
        xySeriesRenderer1.setChartValuesTextSize(12);//设置注释文字的大小
        xySeriesRenderer1.setPointStyle(PointStyle.CIRCLE);//坐标点的显示风格
        xySeriesRenderer1.setPointStrokeWidth(8);//坐标点的大小
        xySeriesRenderer1.setLineWidth(3);
        xySeriesRenderer1.setFillPoints(true);
        xySeriesRenderer1.setColor(0xFFF46C48);//表示该组数据的图或线的颜色
        xySeriesRenderer1.setDisplayChartValues(false);//设置是否显示坐标点的y轴坐标值
        xySeriesRenderer1.setChartValuesTextSize(12);//设置显示的坐标点值的字体大小

        /*某一组数据的描绘器，描绘该组数据的个性化显示效果，主要是字体跟颜色的效果*/
        XYSeriesRenderer xySeriesRenderer2=new XYSeriesRenderer();
        xySeriesRenderer2.setPointStyle(PointStyle.CIRCLE);//坐标点的显示风格
        xySeriesRenderer2.setPointStrokeWidth(8);//坐标点的大小
        xySeriesRenderer2.setColor(0xFF00C8FF);//表示该组数据的图或线的颜色
        xySeriesRenderer2.setDisplayChartValues(false);//设置是否显示坐标点的y轴坐标值
        xySeriesRenderer2.setChartValuesTextSize(12);//设置显示的坐标点值的字体大小
        xySeriesRenderer2.setFillPoints(true);
        xySeriesRenderer2.setLineWidth(3);
        seriesRenderer.addSeriesRenderer(xySeriesRenderer1);
        seriesRenderer.addSeriesRenderer(xySeriesRenderer2);
        return seriesRenderer;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        ((OZApplication)getApplication()).SendOrder(SetPackage.GetMachineQuit(preference2.getString("workmachineid","123456789111"),preference2.getString("userSn","100000001"),preference2.getString("workmachinetypestring","A1")));
    }

//
//    /**
//     * 遍历所有view
//     *
//     * @param viewGroup
//     */
//    public void traversalView(ViewGroup viewGroup) {
//        int count = viewGroup.getChildCount();
//        for (int i = 0; i < count; i++) {
//            View view = viewGroup.getChildAt(i);
//            if (view instanceof ViewGroup) {
//                traversalView((ViewGroup) view);
//            } else {
//                if (fSwitch)
//                {
//                    doView1(view);
//                }else
//                {
//                    doView(view);
//                }
//            }
//        }
//    }
//
//    /**
//     * 处理view
//     *
//     * @param view
//     */
//    private void doView(View view) {
//        if (view.getId()==R.id.aircleanseekBar)
//        {
//            view.setEnabled(false);
//        }else
//        {
//            view.setClickable(false);
//        }
//
//    }
//    private void doView1(View view) {
//        if (view.getId()==R.id.aircleanseekBar)
//        {
//            view.setEnabled(true);
//        }else
//        {
//            view.setClickable(true);
//        }
//    }



}
