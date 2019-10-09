package com.ouzhongiot.ozapp.activity;


import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.others.CircleImageView;
import com.ouzhongiot.ozapp.others.DampView;
import com.ouzhongiot.ozapp.tools.DensityUtil;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.LoginOutActivity;
import com.ouzhongiot.ozapp.others.Post;
import com.ouzhongiot.ozapp.others.SetPackage;
import com.ouzhongiot.ozapp.Model.Coldfanashuju;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class coldfanAindex extends LoginOutActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private OutputStream outputStream;
    private NetworkInfo netInfo;
    private ConnectivityManager mConnectivityManager;
    private ImageView img;
    GestureDetector mGestureDetector;
    private TextView tv_coldfanAindex_wenxintishi;
    private boolean isopen1 = false;
    private boolean isopen2 = false;
    private boolean isopen3 = false;
    private LinearLayout ll_bjshoumin, ll_swzhuangtai, ll_lwjiedu;
    private TextView tv_coldfanaindex_kaiguan, tv_coldfanaindex_kaiguan0;
    private ImageView iv_coldfanaindex_kaiguan;
    private boolean ison;
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
    private boolean fSwitch;
    private int fMode;
    private int fWind;
    private boolean fSwing;
    private boolean fUV;
    private boolean fState;
    private String Msg;
//    private String heartpakage;
    private TextView tv_coldfanaindex_dizhi;
    private TextView tv_coldfanaindex_temperature, tv_coldfanaindex_quality, tv_coldfanaindex_humidity;
    private TextView tv_coldfanaindex_leijishijian, tv_coldfanaindex_fencheng, tv_coldfanaindex_jiangwen, tv_coldfanaindex_jidan;
    private ImageView iv_coldfanAindex_wether;
    private Coldfanashuju coldfanashuju;
    private TextView tv_coldfanaindex_bingjingshouming, tv_coldfanaindex_bingjingxiantiao, tv_coldfanaindex_shengyushuiwei;
    private ImageView iv_coldfanaindex_shuiwei;
    private TextView tv_coldfanaindex_zhuangtaifuwei1, tv_coldfanaindex_zhuangtaifuwei2, tv_coldfanaindex_zhuangtaifuwei3;
    private AlertDialog.Builder builder;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 检查为开启状态
                case 1:
                    iv_coldfanaindex_kaiguan.setImageResource(R.mipmap.open);
                    tv_coldfanaindex_kaiguan.setText("开启");
                    gradientDrawable.setColor(getResources().getColor(R.color.green));
                    fSwitch = true;
                    editor3.putBoolean("fSwitch", fSwitch);
                    editor3.commit();
                    break;
                case 2:
                    iv_coldfanaindex_kaiguan.setImageResource(R.mipmap.close);
                    tv_coldfanaindex_kaiguan.setText("关闭");
                    gradientDrawable.setColor(getResources().getColor(R.color.gray));
                    fSwitch = false;
                    editor3.putBoolean("fSwitch", fSwitch);
                    editor3.commit();
                    break;
                case 3:
                    tv_coldfanAindex_wenxintishi.setText(preference2.getString("des", "今天气温很高，适合穿短袖，注意防晒和避暑,祝您有一个愉快的周末"));
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
                    if (coldfanashuju.getData()!=null) {
                        DecimalFormat df = new DecimalFormat("######0.00");

                        tv_coldfanaindex_leijishijian.setText(df.format((double) coldfanashuju.getData().getTotalTime() / 1000 / 3600) + "");
                        tv_coldfanaindex_fencheng.setText(df.format((double) coldfanashuju.getData().getTotalTime() / 1000 / 3600 * 0.1158) + "");
                        tv_coldfanaindex_jiangwen.setText(df.format(((((double) coldfanashuju.getData().getTotalTime() - coldfanashuju.getData().getTotalC()) * 2 +
                                (double) coldfanashuju.getData().getTotalC() * 6)) / 1000 / 3600) + "");
                        tv_coldfanaindex_jidan.setText("可以煮熟" + df.format(((((double) coldfanashuju.getData().getTotalTime() - coldfanashuju.getData().getTotalC()) * 2 +
                                (double) coldfanashuju.getData().getTotalC() * 6)) / 1000 / 3600 / 90) + "个鸡蛋");
                        //冰晶寿命
                        double i = 2400.00 - ((double) coldfanashuju.getData().getIceCrystalTime() / 1000 / 3600);
                        if (i < 0) {
                            i = 0;
                        }
                        int j = (int) i;
                        tv_coldfanaindex_bingjingshouming.setText(j + "");

                        tv_coldfanaindex_bingjingxiantiao.getLayoutParams().width = DensityUtil.dip2px(coldfanAindex.this, 320) * j / 2400;
//                   //水位状态
                        double c = 720.00 - ((double) coldfanashuju.getData().getWaterStateTime() / 1000 / 60);
                        if (c < 0) {
                            c = 0;
                        }
                        int d = (int) c;
                        tv_coldfanaindex_shengyushuiwei.setText(d + "");
                        if (d >= 650) {
                            iv_coldfanaindex_shuiwei.setBackgroundResource(R.mipmap.shuiwei1);
                        } else if (d >= 480) {
                            iv_coldfanaindex_shuiwei.setBackgroundResource(R.mipmap.shuiwei2);
                        } else if (d >= 360) {
                            iv_coldfanaindex_shuiwei.setBackgroundResource(R.mipmap.shuiwei3);
                        } else if (d >= 240) {
                            iv_coldfanaindex_shuiwei.setBackgroundResource(R.mipmap.shuiwei4);
                        } else if (d >= 120) {
                            iv_coldfanaindex_shuiwei.setBackgroundResource(R.mipmap.shuiwei5);
                        } else if (d >= 120) {
                            iv_coldfanaindex_shuiwei.setBackgroundResource(R.mipmap.shuiwei6);
                        }
                    }
                    break;
                case 5:
                    tv_coldfanaindex_shengyushuiwei.setText("720");
                    iv_coldfanaindex_shuiwei.setBackgroundResource(R.mipmap.shuiwei1);

                    break;
                case 6:
                    tv_coldfanaindex_bingjingshouming.setText("2400");
                    tv_coldfanaindex_bingjingxiantiao.getLayoutParams().width = DensityUtil.dip2px(coldfanAindex.this, 320);

                    break;
                case 7:
//                    try {
//                        MainActivity.socket.close();
//                        MainActivity.TCPconnect = false;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (MainActivity.socket.isClosed() || MainActivity.socket.isInputShutdown() || MainActivity.socket.isOutputShutdown()||MainActivity.socket==null) {
//                        MainActivity.TCPconnect = true;
//                        Log.wtf("进行网络重连", "--------------------------------");
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//
//
//                                try {
//                                    MainActivity.socket = new Socket(MainActivity.ip0, MainActivity.port);
//                                    MainActivity.outputStream = MainActivity.socket.getOutputStream();
//                                    try {
//                                        Log.wtf("这个是建立连接",preference2.getString("workmachineid","123456789111")+preference2.getString("userSn","100000001")+preference2.getString("workmachinetypestring","A1"));
//                                        MainActivity.outputStream.write(SetPackage.GetConected(preference2.getString("workmachineid","123456789111"),preference2.getString("userSn","100000001"),preference2.getString("workmachinetypestring","A1")));
//                                        MainActivity.outputStream.flush();
//                                        Log.wtf("这个是建立连接","发送了！！！--------------------------");
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    MainActivity.outputStream.write(MainActivity.heartbyte);
//                                    MainActivity.outputStream.flush();
////                                    MainActivity.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(MainActivity.socket.getOutputStream())), true);
////                                    MainActivity.out.write(preferences3.getString("heartpakage", ""));
////                                    MainActivity.out.flush();
//                                    InputStream inputStream = MainActivity.socket.getInputStream();
//                                    DataInputStream input = new DataInputStream(inputStream);
//
//                                    while (true) {
//                                        if (MainActivity.TCPconnect == false) {
//                                            break;
//                                        }
//                                        int length = input.read(MainActivity.b);
//                                        Log.wtf("这个是重连读到的长度", length + MainActivity.b.toString() + "");
//                                        MainActivity.msg = new String(MainActivity.b, 0, length, "utf-8");
//                                        Log.wtf("data", "|" + MainActivity.msg.length() + "|");
//                                        if (MainActivity.msg.equals("QUIT")) {
//                                            Intent intent = new Intent();
//                                            intent.setAction("QUIT");
//                                            intent.putExtra("msg", MainActivity.msg.substring(0, 4));
//                                            sendBroadcast(intent);
//                                        } else {
////                                        if (MainActivity.msg.substring(16, 28).equals(workmachineid)) {
//                                            Intent intent = new Intent();
//                                            intent.setAction("broadcass1");
//                                            intent.putExtra("msg", MainActivity.msg);
//                                            sendBroadcast(intent);
//                                        }
//                                    }
//                                } catch (IOExcepton e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();
//
//                        if (preference2.getBoolean("xintiaojiesu", true)) {
//                            editor2.putBoolean("xintiaojiesu", false);
//                            editor2.commit();
//                            Log.wtf("这个是重连的包", heartpakage);
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    while (true) {
//                                        if (MainActivity.TCPconnect == false) {
//                                            editor2.putBoolean("xintiaojiesu", true);
//                                            editor2.commit();
//                                            break;
//                                        }
//                                        try {
//                                            MainActivity.outputStream.write(MainActivity.heartbyte);
//                                            MainActivity.outputStream.flush();
////                                            Log.wtf("这个是重连的包", heartpakage);
////                                            MainActivity.out.write(heartpakage);
////                                            MainActivity.out.flush();
//                                            Thread.sleep(30000);
//                                        } catch (InterruptedException e) {
//                                            e.printStackTrace();
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                    editor2.putBoolean("xintiaojiesu", true);
//                                    editor2.commit();
//                                }
//                            }).start();
//                        }
//                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coldfan_aindex);
        outputStream = ((OZApplication)getApplication()).outputStream;
        tv_coldfanaindex_zhuangtaifuwei1 = (TextView) findViewById(R.id.tv_coldfanaindex_zhuangtaifuwei1);
        tv_coldfanaindex_zhuangtaifuwei2 = (TextView) findViewById(R.id.tv_coldfanaindex_zhuangtaifuwei2);
        tv_coldfanaindex_zhuangtaifuwei3 = (TextView) findViewById(R.id.tv_coldfanaindex_zhuangtaifuwei3);
        iv_coldfanaindex_shuiwei = (ImageView) findViewById(R.id.iv_coldfanaindex_shuiwei);
        tv_coldfanaindex_shengyushuiwei = (TextView) findViewById(R.id.tv_coldfanaindex_shengyushuiwei);
        tv_coldfanaindex_bingjingxiantiao = (TextView) findViewById(R.id.tv_coldfanaindex_bingjingxiantiao);
        tv_coldfanaindex_bingjingshouming = (TextView) findViewById(R.id.tv_coldfanaindex_bingjingshouming);
        tv_coldfanaindex_jidan = (TextView) findViewById(R.id.tv_coldfanaindex_jidan);
        tv_coldfanaindex_jiangwen = (TextView) findViewById(R.id.tv_coldfanaindex_jiangwen);
        tv_coldfanaindex_fencheng = (TextView) findViewById(R.id.tv_coldfanaindex_fencheng);
        tv_coldfanaindex_leijishijian = (TextView) findViewById(R.id.tv_coldfanaindex_leijishijian);
        ll_bjshoumin = (LinearLayout) findViewById(R.id.ll_bjshoumin);
        ll_swzhuangtai = (LinearLayout) findViewById(R.id.ll_swzhuangtai);
        ll_lwjiedu = (LinearLayout) findViewById(R.id.ll_lwjiedu);
        iv_clodfanAindex_touxiang = (CircleImageView) findViewById(R.id.iv_coldfanAindex_touxiang);
        tv_coldfanaindex_dizhi = (TextView) findViewById(R.id.tv_coldfanaindex_dizhi);
        iv_coldfanaindex_kaiguan = (ImageView) findViewById(R.id.iv_coldfanaindex_kaiguan);
        tv_coldfanaindex_kaiguan = (TextView) findViewById(R.id.tv_coldfanaindex_kaiguan);
        tv_coldfanaindex_kaiguan0 = (TextView) findViewById(R.id.tv_coldfanaindex_kaiguan0);
        gradientDrawable = (GradientDrawable) tv_coldfanaindex_kaiguan0.getBackground();
        preference = getSharedPreferences("coldfanA", MODE_PRIVATE);
        editor = preference.edit();
        tv_coldfanaindex_humidity = (TextView) findViewById(R.id.tv_coldfanaindex_humidity);
        tv_coldfanaindex_quality = (TextView) findViewById(R.id.tv_coldfanaindex_quality);
        tv_coldfanaindex_temperature = (TextView) findViewById(R.id.tv_coldfanaindex_temperature);
        iv_coldfanAindex_wether = (ImageView) findViewById(R.id.iv_coldfanAindex_wether);

        preference2 = getSharedPreferences("data", MODE_PRIVATE);
        editor2 = preference2.edit();
        workmachineid = preference2.getString("workmachineid", "");
        workmachinetype = preference2.getString("workmachinetype", "");


        //请求当前状态
        preferences3 = getSharedPreferences(workmachineid, MODE_PRIVATE);
        fSwitch = preferences3.getBoolean("fSwitch", false);
        fUV = preferences3.getBoolean("fUV", false);
        fState = preferences3.getBoolean("fState", false);
        fSwing = preferences3.getBoolean("fSwing", false);
        fWind = preferences3.getInt("fWind", 1);
        fMode = preferences3.getInt("fMode", 1);
//        heartpakage = preferences3.getString("heartpakage", "");
        editor3 = preferences3.edit();

        tv_coldfanAindex_wenxintishi = (TextView) findViewById(R.id.tv_coldfanAindex_wenxintishi);
        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener) this);
        DampView viewSnsLayout = (DampView) findViewById(R.id.dampview1);
        viewSnsLayout.setOnTouchListener(this);
        viewSnsLayout.setLongClickable(true);
        setupView();

        //初始化数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求当前数据
                String uriAPI2 = MainActivity.ip + "smarthome/fan/queryDeviceData";
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                params2.add(new BasicNameValuePair("devSn", workmachineid));
                String str2 = Post.dopost(uriAPI2, params2);
                Log.wtf("这个是收到的机器数据",str2+workmachinetype+workmachineid);
                coldfanashuju = Coldfanashuju.objectFromData(str2);
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

        //初始化天气
        tv_coldfanAindex_wenxintishi.setText(preference2.getString("des", "今天气温很高，适合穿短袖，注意防晒和避暑,祝您有一个愉快的周末"));
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
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*S0*#");
//                    MainActivity.out.flush();
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        Log.wtf("命令发送报错","5451--------------------+++++++++++++++++++");
//                        e.printStackTrace();
//
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"S",(byte)0x30));

                } else {
//                    try {
//                        ((OZApplication)getApplication()).getOutputStream().write();
//                        ((OZApplication)getApplication()).getOutputStream().flush();
//                    } catch (IOException e) {
//                        Log.wtf("命令发送报错","5451--------------------+++++++++++++++++++");
//                        e.printStackTrace();
//
//                    }
                    ((OZApplication)getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid,workmachinetype,"S",(byte)0x31));
//                    MainActivity.out.write("HM*FF*" + workmachinetype + "*" + workmachineid + "*S1*#");
//                    MainActivity.out.flush();
//                    try {
//                        MainActivity.outputStream.write(MainActivity.heartbyte);
//                        MainActivity.outputStream.flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
            }
        });
        //设置
        this.findViewById(R.id.iv_coldfanAindex_shezhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(coldfanAindex.this, coldfanAyonghuzhongxin.class);
                startActivity(intent);
            }
        });

        //头像
        iv_clodfanAindex_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(coldfanAindex.this, coldfanAyonghuzhongxin.class);
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

        this.findViewById(R.id.tv_swzhuangtai).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isopen2) {
                    ll_swzhuangtai.setVisibility(View.GONE);
                    isopen2 = false;
                } else {
                    ll_swzhuangtai.setVisibility(View.VISIBLE);
                    isopen2 = true;
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

        tv_coldfanaindex_zhuangtaifuwei1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(coldfanAindex.this);
                builder.setTitle("水位状态复位");
                builder.setMessage("请确保已经重新为冷风扇加水，点击复位后水位将重新计算");
                builder.setPositiveButton("已经加了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                String uriAPI = MainActivity.ip + "smarthome/work/resetDeviceData";
                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                                params.add(new BasicNameValuePair("devSn", workmachineid));
                                params.add(new BasicNameValuePair("reset", "1"));
                                String str = Post.dopost(uriAPI, params);


                                Message msg = new Message();
                                msg.what = 5;
                                handler.sendMessage(msg);
                            }
                        }).start();

                    }
                });
                builder.setNegativeButton("还没有", null);
                builder.create();
                builder.show();
            }
        });
        tv_coldfanaindex_zhuangtaifuwei2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(coldfanAindex.this);
                builder.setTitle("冰晶状态复位");
                builder.setMessage("请确保已经更换新的冰晶，点击复位后冰晶寿命将重新计算");
                builder.setPositiveButton("已经更换", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                String uriAPI = MainActivity.ip + "smarthome/work/resetDeviceData";
                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                                params.add(new BasicNameValuePair("devSn", workmachineid));
                                params.add(new BasicNameValuePair("reset", "2"));
                                String str = Post.dopost(uriAPI, params);


                                Message msg = new Message();
                                msg.what = 6;
                                handler.sendMessage(msg);
                            }
                        }).start();

                    }
                });
                builder.setNegativeButton("还没有", null);
                builder.create();
                builder.show();
            }
        });

        tv_coldfanaindex_zhuangtaifuwei3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(coldfanAindex.this);
                builder.setTitle("水帘洁度");
                builder.setMessage("请确保已经清洗或更换新的水帘，点击复位后水帘洁度将重新计算");
                builder.setPositiveButton("已经更换", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
                builder.setNegativeButton("还没有", null);
                builder.create();
                builder.show();
            }
        });
    }

    @Override
    protected void onRestart() {


        super.onRestart();
    }

    @Override
    protected void onResume() {


//        if (MainActivity.socket.isClosed() || MainActivity.socket.isInputShutdown() || MainActivity.socket.isOutputShutdown()||MainActivity.socket==null) {
//            MainActivity.TCPconnect = true;
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//
//                    try {
//                        MainActivity.socket = new Socket(MainActivity.ip0, MainActivity.port);
//                        MainActivity.outputStream = MainActivity.socket.getOutputStream();
//                        try {
//                            Log.wtf("这个是建立连接",preference2.getString("workmachineid","123456789111")+preference2.getString("userSn","100000001")+preference2.getString("workmachinetypestring","A1"));
//                            MainActivity.outputStream.write(SetPackage.GetConected(preference2.getString("workmachineid","123456789111"),preference2.getString("userSn","100000001"),preference2.getString("workmachinetypestring","A1")));
//                            MainActivity.outputStream.flush();
//                            Log.wtf("这个是建立连接","发送了！！！--------------------------");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        MainActivity.outputStream.write(MainActivity.heartbyte);
//                        MainActivity.outputStream.flush();
////                        MainActivity.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(MainActivity.socket.getOutputStream())), true);
////                        MainActivity.out.write(preferences3.getString("heartpakage", ""));
////                        MainActivity.out.flush();
//                        InputStream inputStream = MainActivity.socket.getInputStream();
//                        DataInputStream input = new DataInputStream(inputStream);
//
//                        while (true) {
//                            if (MainActivity.TCPconnect == false) {
//                                break;
//                            }
//                            int length = input.read(MainActivity.b);
//                            Log.wtf("这个是重连读到的长度", length + MainActivity.b.toString() + "");
//                            MainActivity.msg = new String(MainActivity.b, 0, length, "utf-8");
//                            Log.wtf("data", MainActivity.msg);
//                            Log.wtf("data", "|" + MainActivity.msg.length() + "|");
//                            if (MainActivity.msg.equals("QUIT")) {
//                                Intent intent = new Intent();
//                                intent.setAction("QUIT");
//                                intent.putExtra("msg", MainActivity.msg.substring(0, 4));
//                                sendBroadcast(intent);
//                            } else {
////                            if (MainActivity.msg.substring(16, 28).equals(workmachineid)) {
//                                Intent intent = new Intent();
//                                intent.setAction("broadcass1");
//                                intent.putExtra("msg", MainActivity.msg);
//                                sendBroadcast(intent);
//                                Log.wtf("data2", MainActivity.msg);
//                            }
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//
//            if (preference2.getBoolean("xintiaojiesu", true)) {
//                editor2.putBoolean("xintiaojiesu", false);
//                editor2.commit();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while (true) {
//                            if (MainActivity.TCPconnect == false) {
//                                break;
//                            }
//                            try {
//                                MainActivity.outputStream.write(MainActivity.heartbyte);
//                                MainActivity.outputStream.flush();
////                                Log.wtf("这个是out", heartpakage);
////                                MainActivity.out.write(heartpakage);
////                                MainActivity.out.flush();
//                                Thread.sleep(30000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        editor2.putBoolean("xintiaojiesu", true);
//                        editor2.commit();
//                    }
//                }).start();
//            }
//        }


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
                    String chuanyistr = JacksonUtil.serializeObjectToJson(infolife.get("chuanyi"));
                    List<String> chuanyilist = JacksonUtil.deserializeJsonToList(chuanyistr, String.class);
                    String  chuanyi = chuanyilist.get(1);

                    String pm25str = JacksonUtil.serializeObjectToJson(data.get("pm25"));
                    Map<String, Object> pm25 = JacksonUtil.deserializeJsonToObject(pm25str, Map.class);
                    String pm25infostr = JacksonUtil.serializeObjectToJson(pm25.get("pm25"));
                    Map<String, Object> pm25info = JacksonUtil.deserializeJsonToObject(pm25infostr, Map.class);
                    String quality = JacksonUtil.serializeObjectToJson(pm25info.get("quality"));
                    String des = JacksonUtil.serializeObjectToJson(pm25info.get("des"));
                    Log.wtf("我是真的", info + humidity + temperature + quality + des);
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

                    Log.wtf("我是真的", info + humidity + temperature + quality + des);

                    editor2.putString("humidity", humidity);
                    editor2.putString("weatherinfo", info);
                    editor2.putString("temperature", temperature);
                    editor2.putString("quality", quality);
                    editor2.putString("des", chuanyi);
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
//        Log.wtf("这个是读取的图片地址",preference2.getString("touxiangbendiurl",""));
        if (getLoacalBitmap(preference2.getString("touxiangbendiurl", Environment.getExternalStorageDirectory() + "/"
                + "lianxiatouxiang" + ".jpg")) != null) {

            iv_clodfanAindex_touxiang.setImageBitmap(getLoacalBitmap(preference2.getString("touxiangbendiurl", Environment.getExternalStorageDirectory() + "/"
                    + "lianxiatouxiang" + ".jpg")));
        }

        tv_coldfanaindex_dizhi.setText(preference2.getString("district", ""));

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
                Intent intent = new Intent(coldfanAindex.this, coldfanAwork.class);
                startActivity(intent);
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


//    //广播监听网络状态
//    private BroadcastReceiver receiver2 = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            String action = intent.getAction();
//            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//
//                mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                netInfo = mConnectivityManager.getActiveNetworkInfo();
//                if (netInfo != null && netInfo.isAvailable()) {
//
//                    /////////////网络连接
//                    String name = netInfo.getTypeName();
//
//                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                        /////WiFi网络
//                        Log.wtf("网络连接", "网络连接");
//                        Message msg = new Message();
//                        msg.what = 7;
//                        handler.sendMessage(msg);
//                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
//                        /////有线网络
//                        Message msg = new Message();
//                        msg.what = 7;
//                        handler.sendMessage(msg);
//                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//                        //3g
//                        Message msg = new Message();
//                        msg.what = 7;
//                        handler.sendMessage(msg);
//
//                    }
//                } else {
//                    ////////网络断开
//                    Log.wtf("网络断开", "网络断开");
//                    try {
//                        MainActivity.socket.close();
//                        MainActivity.TCPconnect = false;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    };


    //广播接收按键改变
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction("broadcass1");            //添加动态广播的Action
        registerReceiver(receiver, dynamic_filter);// 注册自定义动态广播消息

//        IntentFilter mFilter = new IntentFilter();
//        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(receiver2, mFilter);

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        ((OZApplication)getApplication()).SendOrder(SetPackage.GetMachineQuit(preference2.getString("workmachineid","123456789111"),preference2.getString("userSn","100000001"),preference2.getString("workmachinetypestring","A1")));
//        unregisterReceiver(receiver2);
    }

}
