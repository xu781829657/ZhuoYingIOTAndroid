package com.ouzhongiot.ozapp.xinfengairclean;

import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MainActivity;
import com.ouzhongiot.ozapp.airclean.Aircleanlishishijian;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.http.ConnectDataTask;
import com.ouzhongiot.ozapp.http.HcNetWorkTask;
import com.ouzhongiot.ozapp.http.PostParamTools;
import com.ouzhongiot.ozapp.tools.DensityUtil;
import com.ouzhongiot.ozapp.others.LoginOutActivity;
import com.ouzhongiot.ozapp.others.Post;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.ToastTools;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新风空气净化器
 */
public class XinfengIndex extends LoginOutActivity implements View.OnClickListener, ConnectDataTask.OnResultDataLintener {
    //模式：1，睡眠   2，自然    3，高效   4，舒适
    private NetworkInfo netInfo;
    private ConnectivityManager mConnectivityManager;
    private ImageView iv_xingfengindex_yuanquan, iv_xinfengindex_dingshikongzhi, iv_xingfengindex_wind, iv_xinfengindex_zidong, iv_xinfengindex_fulizi, iv_xinfengindex_moshi;
    private TextView tv_Xinfengindex_kaiguan0, tv_xinfengindex_shijian, tv_xinfengindex_pm25, tv_xinfengindex_co2, tv_xinfengindex_shidu,
            tv_xinfengindex_wendu, tv_xinfengindex_jiaquan, tv_xinfengindex_moshi;
    private TextView tv_xinfengindex_shuimian, tv_xinfengindex_ziran, tv_xinfengindex_shushi, tv_xinfengindex_gaoxiao;
    private Animation operatingAnim;
    private LinearLayout ll_xinfengindex_mokuai1, ll_xinfengindex_kaiguan, ll_xinfengindex_shijianquxian, ll_xinfengindex_touming, ll_xinfengindex_caidang, ll_xinfengindex_offtouming, ll_xinfengindex_moshi1;
    private ScrollView sl_xinfeng_index;
    GraphicalView chartView_shijian;
    private boolean fSwich = true;
    private AlertDialog.Builder builder;
    private Aircleanlishishijian aircleanlishishijian;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferencesmachine;
    private SharedPreferences.Editor editormachine;
    private String workmachineid;
    private String workmachinetype;
    private String time, hour1;
    private float currenttime;
    private int hourfinish, hour;
    private Animation animation;
    private ValueAnimator valueAnimator;
    public static String PARAM_DEVTYPESN = "devTypeSn";//设备类型编号
    public static String PARAM_DEVSN = "devSn";//设备编号
    private String devSn;
    private String devTypeSn;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                    iv_xingfengindex_yuanquan.clearAnimation();
//                    ObjectAnimator.ofFloat(iv_xingfengindex_yuanquan,"rotation",0,360).setDuration(10000).start();

                    valueAnimator.cancel();
                    animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewshow0);
                    ll_xinfengindex_offtouming.setVisibility(View.VISIBLE);
                    ll_xinfengindex_offtouming.startAnimation(animation);
                    fSwich = false;
                    break;
                case 1:
//                    iv_xingfengindex_yuanquan.startAnimation(operatingAnim);
                    valueAnimator.start();
                    animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewhide0);
                    ll_xinfengindex_offtouming.setVisibility(View.GONE);
                    ll_xinfengindex_offtouming.startAnimation(animation);
                    fSwich = true;
                    break;
                case 2:
                    showChart_shijian();
                    break;
                case 3:
//                    Connection();
                    break;
                case 4:
                    sl_xinfeng_index.smoothScrollTo(0, 500);
                    break;
                case 5:
                    builder = new AlertDialog.Builder(XinfengIndex.this);
                    builder.setMessage("移除后可重新添加设备，是否移除？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }).start();

                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create();
                    builder.show();
                    break;
                //自动
                case 6:
                    iv_xinfengindex_zidong.setImageResource(R.mipmap.xinfengzidong2);
                    editormachine.putInt("fAuto", 2);
                    editormachine.commit();
                    break;
                case 7:
                    iv_xinfengindex_zidong.setImageResource(R.mipmap.xinfengzidong1);
                    editormachine.putInt("fAuto", 1);
                    editormachine.commit();
                    break;
                //风速
                case 8:
                    iv_xingfengindex_wind.setImageResource(R.mipmap.xinfengwind1);
                    currenttime = valueAnimator.getAnimatedFraction();
                    valueAnimator.setDuration(30000);
                    valueAnimator.setCurrentPlayTime((long) (30000 * currenttime));
                    editormachine.putInt("fWind", 2);
                    editormachine.commit();
                    break;
                case 9:
                    iv_xingfengindex_wind.setImageResource(R.mipmap.xinfengwind2);
                    currenttime = valueAnimator.getAnimatedFraction();
                    valueAnimator.setDuration(24000);
                    valueAnimator.setCurrentPlayTime((long) (24000 * currenttime));
                    editormachine.putInt("fWind", 3);
                    editormachine.commit();
                    break;
                case 10:
                    iv_xingfengindex_wind.setImageResource(R.mipmap.xinfengwind3);
                    currenttime = valueAnimator.getAnimatedFraction();
                    valueAnimator.setDuration(14000);
                    valueAnimator.setCurrentPlayTime((long) (14000 * currenttime));
                    editormachine.putInt("fWind", 4);
                    editormachine.commit();
                    break;
                case 11:
                    iv_xinfengindex_fulizi.setImageResource(R.mipmap.xinfengfulizi2);
                    editormachine.putInt("fAnion", 2);
                    editormachine.commit();
                    break;
                case 12:
                    iv_xinfengindex_fulizi.setImageResource(R.mipmap.xinfengfulizi1);
                    editormachine.putInt("fAnion", 1);
                    editormachine.commit();
                    break;
                case 13:
                    iv_xingfengindex_wind.setImageResource(R.mipmap.xinfengwind4);
                    currenttime = valueAnimator.getAnimatedFraction();
                    valueAnimator.setDuration(8000);
                    valueAnimator.setCurrentPlayTime((long) (8000 * currenttime));
                    editormachine.putInt("fWind", 1);
                    editormachine.commit();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xinfeng_index);
        //初始化布局
        initView();
        //初始化数据
        initValue();
        //设置点击事件
        setClick();

        preferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = preferences.edit();
        workmachineid = preferences.getString("workmachineid", "");
        workmachinetype = preferences.getString("workmachinetype", "");
        preferencesmachine = getSharedPreferences(workmachineid, MODE_PRIVATE);
        editormachine = preferencesmachine.edit();
        //获取屏幕长度
        int changdupx = preferences.getInt("changdupx", 1920);


        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ll_xinfengindex_mokuai1.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = changdupx - DensityUtil.dip2px(this, 60) - DensityUtil.getStatusBarHeight(this);// 控件的高强制设成20
//        linearParams.width = 30;// 控件的宽强制设成30
        ll_xinfengindex_mokuai1.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        Xuanzhuang();
        tv_Xinfengindex_kaiguan0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fSwich) {
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

            }
        });


        this.findViewById(R.id.ll_xinfengindex_xiala).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
            }
        });

        //定时
        this.findViewById(R.id.ll_xinfengindex_dingshi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPickerDialog();
            }
        });

        //more
        this.findViewById(R.id.ll_xinfengindex_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ll_xinfengindex_caidang.getVisibility() == View.GONE) {
                    animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewshow0);
                    ll_xinfengindex_touming.setVisibility(View.VISIBLE);
                    ll_xinfengindex_touming.startAnimation(animation);
                    animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewshow1);
                    ll_xinfengindex_caidang.setVisibility(View.VISIBLE);
                    ll_xinfengindex_caidang.startAnimation(animation);
                    sl_xinfeng_index.setClickable(false);
//                            ll_xinfengindex_caidang.onInterceptTouchEvent(ll_xinfengindex_caidang)
                } else {
                    animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewhide0);
                    ll_xinfengindex_touming.setVisibility(View.GONE);
                    ll_xinfengindex_touming.startAnimation(animation);
                    animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewhide1);
                    ll_xinfengindex_caidang.setVisibility(View.GONE);
                    ll_xinfengindex_caidang.startAnimation(animation);
                    sl_xinfeng_index.setClickable(true);
                }
            }
        });

        //模式
        this.findViewById(R.id.iv_xinfengindex_moshi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewshow1);
                ll_xinfengindex_moshi1.setVisibility(View.VISIBLE);
                ll_xinfengindex_moshi1.startAnimation(animation);
                sl_xinfeng_index.setClickable(false);

            }
        });
        //睡眠模式
        this.findViewById(R.id.ll_xinfengindex_moshi_shuimian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_xinfengindex_moshi.setText("睡眠模式");
                tv_xinfengindex_shuimian.setTextColor(getResources().getColor(R.color.xinfeng));
                tv_xinfengindex_ziran.setTextColor(getResources().getColor(R.color.gray));
                tv_xinfengindex_shushi.setTextColor(getResources().getColor(R.color.gray));
                tv_xinfengindex_gaoxiao.setTextColor(getResources().getColor(R.color.gray));
                iv_xinfengindex_moshi.setImageResource(R.mipmap.xinfengmoshi1);
                editormachine.putInt("xinfengmoshi", 1);
                editormachine.commit();
                animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewhide1);
                ll_xinfengindex_moshi1.setVisibility(View.GONE);
                ll_xinfengindex_moshi1.startAnimation(animation);
                sl_xinfeng_index.setClickable(true);
            }
        });
        //自然模式
        this.findViewById(R.id.ll_xinfengindex_moshi_zirang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_xinfengindex_moshi.setText("自然模式");
                tv_xinfengindex_shuimian.setTextColor(getResources().getColor(R.color.gray));
                tv_xinfengindex_ziran.setTextColor(getResources().getColor(R.color.xinfeng));
                tv_xinfengindex_shushi.setTextColor(getResources().getColor(R.color.gray));
                tv_xinfengindex_gaoxiao.setTextColor(getResources().getColor(R.color.gray));
                iv_xinfengindex_moshi.setImageResource(R.mipmap.xinfengmoshi1);
                editormachine.putInt("xinfengmoshi", 2);
                editormachine.commit();
                animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewhide1);
                ll_xinfengindex_moshi1.setVisibility(View.GONE);
                ll_xinfengindex_moshi1.startAnimation(animation);
                sl_xinfeng_index.setClickable(true);
            }
        });
        //高效模式
        this.findViewById(R.id.ll_xinfengindex_moshi_gaoxiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_xinfengindex_moshi.setText("高效模式");
                tv_xinfengindex_shuimian.setTextColor(getResources().getColor(R.color.gray));
                tv_xinfengindex_ziran.setTextColor(getResources().getColor(R.color.gray));
                tv_xinfengindex_shushi.setTextColor(getResources().getColor(R.color.gray));
                tv_xinfengindex_gaoxiao.setTextColor(getResources().getColor(R.color.xinfeng));
                iv_xinfengindex_moshi.setImageResource(R.mipmap.xinfengmoshi1);
                editormachine.putInt("xinfengmoshi", 3);
                editormachine.commit();
                animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewhide1);
                ll_xinfengindex_moshi1.setVisibility(View.GONE);
                ll_xinfengindex_moshi1.startAnimation(animation);
                sl_xinfeng_index.setClickable(true);
            }
        });

        //舒适模式
        this.findViewById(R.id.ll_xinfengindex_moshi_shushi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_xinfengindex_moshi.setText("舒适模式");
                tv_xinfengindex_shuimian.setTextColor(getResources().getColor(R.color.gray));
                tv_xinfengindex_ziran.setTextColor(getResources().getColor(R.color.gray));
                tv_xinfengindex_shushi.setTextColor(getResources().getColor(R.color.xinfeng));
                tv_xinfengindex_gaoxiao.setTextColor(getResources().getColor(R.color.gray));
                iv_xinfengindex_moshi.setImageResource(R.mipmap.xinfengmoshi1);
                editormachine.putInt("xinfengmoshi", 4);
                editormachine.commit();
                animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewhide1);
                ll_xinfengindex_moshi1.setVisibility(View.GONE);
                ll_xinfengindex_moshi1.startAnimation(animation);
                sl_xinfeng_index.setClickable(true);
            }
        });

        //模式取消
        this.findViewById(R.id.ll_xinfengindex_moshi_quxiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewhide1);
                ll_xinfengindex_moshi1.setVisibility(View.GONE);
                ll_xinfengindex_moshi1.startAnimation(animation);
                sl_xinfeng_index.setClickable(true);

            }
        });


        //取消
        this.findViewById(R.id.ll_xinfengindex_quxiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewhide0);
                ll_xinfengindex_touming.setVisibility(View.GONE);
                ll_xinfengindex_touming.startAnimation(animation);
                animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewhide1);
                ll_xinfengindex_caidang.setVisibility(View.GONE);
                ll_xinfengindex_caidang.startAnimation(animation);
                sl_xinfeng_index.setClickable(true);
            }
        });


        //返回
        this.findViewById(R.id.ll_xinfengindex_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueAnimator.cancel();
                onBack();
            }
        });

        //移除设备
        this.findViewById(R.id.ll_xinfengindex_yichushebei).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 5;
                handler.sendMessage(msg);
            }
        });


        ll_xinfengindex_touming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewhide0);
                ll_xinfengindex_touming.setVisibility(View.GONE);
                ll_xinfengindex_touming.startAnimation(animation);
                animation = AnimationUtils.loadAnimation(XinfengIndex.this, R.anim.viewhide1);
                ll_xinfengindex_caidang.setVisibility(View.GONE);
                ll_xinfengindex_caidang.startAnimation(animation);
                sl_xinfeng_index.setClickable(true);
            }
        });

        this.findViewById(R.id.ll_xinfengindex_dingshikongzhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferencesmachine.getLong("finishtime", 0) > System.currentTimeMillis()) {
                    iv_xinfengindex_dingshikongzhi.setImageResource(R.mipmap.xinfengdingshioff);
                    tv_xinfengindex_shijian.setText("暂未设置定时任务");
                    editormachine.putLong("finishtime", 0);
                    editormachine.commit();

                } else {
                    showHourPickerDialog();
                }
            }
        });


        //初始化数据--历史时间
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求当前数据
                String uriAPI2 = MainActivity.ip + "smarthome/air/queryDeviceHistory";
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                params2.add(new BasicNameValuePair("devSn", workmachineid));
                params2.add(new BasicNameValuePair("days", "7"));
                String str2 = Post.dopost(uriAPI2, params2);
//                Log.wtf("这个是收到的历史时间", str2 + workmachinetype + workmachineid);
                aircleanlishishijian = Aircleanlishishijian.objectFromData(str2);
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }).start();

        //初始化定时
        if (preferencesmachine.getLong("finishtime", 0) > System.currentTimeMillis()) {
            tv_xinfengindex_shijian.setText(preferencesmachine.getString("dingshi", "暂未设置定时任务"));
            iv_xinfengindex_dingshikongzhi.setImageResource(R.mipmap.xinfengdingshion);
        } else {
            tv_xinfengindex_shijian.setText("暂未设置定时任务");
            iv_xinfengindex_dingshikongzhi.setImageResource(R.mipmap.xinfengdingshioff);
        }


        //风速
        this.findViewById(R.id.ll_xinfengindex_fengsu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (preferencesmachine.getInt("fWind", 1)) {
                    case 1:
                        Message msg1 = new Message();
                        msg1.what = 8;
                        handler.sendMessage(msg1);
                        break;
                    case 2:
                        Message msg2 = new Message();
                        msg2.what = 9;
                        handler.sendMessage(msg2);
                        break;
                    case 3:
                        Message msg3 = new Message();
                        msg3.what = 10;
                        handler.sendMessage(msg3);
                        break;
                    case 4:
                        Message msg4 = new Message();
                        msg4.what = 13;
                        handler.sendMessage(msg4);
                        break;
                    default:
                        break;
                }
            }
        });

        //自动
        this.findViewById(R.id.ll_xinfengindex_zidong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferencesmachine.getInt("fAuto", 1) == 1) {
                    Message msg = new Message();
                    msg.what = 6;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 7;
                    handler.sendMessage(msg);
                }
            }
        });
        //负离子
        this.findViewById(R.id.ll_xinfengindex_fulizi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferencesmachine.getInt("fAnion", 1) == 1) {
                    Message msg = new Message();
                    msg.what = 11;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 12;
                    handler.sendMessage(msg);
                }
            }
        });


    }

    //初始化布局
    public void initView() {
        ll_xinfengindex_kaiguan = (LinearLayout) findViewById(R.id.ll_xinfengindex_kaiguan);
        iv_xingfengindex_yuanquan = (ImageView) findViewById(R.id.iv_xingfengindex_yuanquan);
        tv_Xinfengindex_kaiguan0 = (TextView) findViewById(R.id.tv_Xinfengindex_kaiguan0);
        ll_xinfengindex_mokuai1 = (LinearLayout) findViewById(R.id.ll_xinfengindex_mokuai1);
        ll_xinfengindex_shijianquxian = (LinearLayout) findViewById(R.id.ll_xinfengindex_shijianquxian);
        sl_xinfeng_index = (ScrollView) findViewById(R.id.sl_xinfeng_index);
        tv_xinfengindex_shijian = (TextView) findViewById(R.id.tv_xinfengindex_shijian);
        iv_xinfengindex_dingshikongzhi = (ImageView) findViewById(R.id.iv_xinfengindex_dingshikongzhi);
        ll_xinfengindex_touming = (LinearLayout) findViewById(R.id.ll_xinfengindex_touming);
        ll_xinfengindex_caidang = (LinearLayout) findViewById(R.id.ll_xinfengindex_caidang);
        ll_xinfengindex_offtouming = (LinearLayout) findViewById(R.id.ll_xinfengindex_offtouming);
        tv_xinfengindex_pm25 = (TextView) findViewById(R.id.tv_xinfengindex_pm25);
        tv_xinfengindex_shidu = (TextView) findViewById(R.id.tv_xinfengindex_shidu);
        tv_xinfengindex_wendu = (TextView) findViewById(R.id.tv_xinfengindex_wendu);
        tv_xinfengindex_jiaquan = (TextView) findViewById(R.id.tv_xinfengindex_jiaquan);
        tv_xinfengindex_co2 = (TextView) findViewById(R.id.tv_xinfengindex_co2);
        iv_xingfengindex_wind = (ImageView) findViewById(R.id.iv_xingfengindex_wind);
        iv_xinfengindex_zidong = (ImageView) findViewById(R.id.iv_xinfengindex_zidong);
        iv_xinfengindex_fulizi = (ImageView) findViewById(R.id.iv_xinfengindex_fulizi);
        ll_xinfengindex_moshi1 = (LinearLayout) findViewById(R.id.ll_xinfengindex_moshi1);
        tv_xinfengindex_moshi = (TextView) findViewById(R.id.tv_xinfengindex_moshi);
        iv_xinfengindex_moshi = (ImageView) findViewById(R.id.iv_xinfengindex_moshi);
        tv_xinfengindex_shuimian = (TextView) findViewById(R.id.tv_xinfengindex_shuimian);
        tv_xinfengindex_ziran = (TextView) findViewById(R.id.tv_xinfengindex_ziran);
        tv_xinfengindex_gaoxiao = (TextView) findViewById(R.id.tv_xinfengindex_gaoxiao);
        tv_xinfengindex_shushi = (TextView) findViewById(R.id.tv_xinfengindex_shushi);

    }

    //初始化数据
    public void initValue() {
        if (getIntent().getExtras() != null) {
            //获取设备编号
            devSn = getIntent().getStringExtra(PARAM_DEVSN);
            //获取设备类型编号
            devTypeSn = getIntent().getStringExtra(PARAM_DEVTYPESN);
            //查询设备当前状态
            new HcNetWorkTask(this, this, 1).doPost(UrlConstant.QUERY_XIN_AIR_STATE, null, postParams(1).getBytes());

        } else {
            ToastTools.show(this, "没有获取到设备编号");
        }

    }

    //设置点击事件
    public void setClick() {
        tv_Xinfengindex_kaiguan0.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.tv_Xinfengindex_kaiguan0:
               //开关
               break;
       }

    }

    /**
     * post参数
     *
     * @param code
     * @return
     */
    private String postParams(int code) {
        Map<String, String> params = new HashMap<String, String>();
        if (code == 1) {
            // 查询当前状态
            params.put("devTypeSn", devTypeSn);
            params.put("devSn", devSn);
            if (LogTools.debug) {
                LogTools.i("查询当前状态参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        }
        return "";
    }

    @Override
    public void onResult(String result, int code) throws JSONException {
        if (!result.isEmpty()) {
            JSONObject object = new JSONObject(result);


        }

    }


    private void showChart_shijian() {
        XYMultipleSeriesDataset mDataSet = getDataSet_shijian();
        XYMultipleSeriesRenderer mRefender = getRefender_shijian();
        chartView_shijian = ChartFactory.getLineChartView(this, mDataSet, mRefender);
        ll_xinfengindex_shijianquxian.addView(chartView_shijian);
    }

    private XYMultipleSeriesDataset getDataSet_shijian() {
        XYMultipleSeriesDataset seriesDataset = new XYMultipleSeriesDataset();

        XYSeries xySeries2 = new XYSeries("历史使用时间");

        if (aircleanlishishijian.getState() == 1 || aircleanlishishijian.getState() == 2) {
            xySeries2.add(1, 0);
        } else {
            for (int i = 0; i < aircleanlishishijian.getData().size(); i++) {
                xySeries2.add(i + 1, aircleanlishishijian.getData().get(i).getUseTime() / 1000 / 3600);
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
        XYMultipleSeriesRenderer seriesRenderer = new XYMultipleSeriesRenderer();

        seriesRenderer.setChartTitleTextSize(20);//设置图表标题的字体大小(图的最上面文字)
        seriesRenderer.setMargins(new int[]{20, 80, 60, 60});//设置外边距，顺序为：上左下右
        //坐标轴设置
        seriesRenderer.setAxisTitleTextSize(25);//设置坐标轴标题字体的大小
        seriesRenderer.setYAxisMin(0);//设置y轴的起始值
        seriesRenderer.setYAxisMax(24);//设置y轴的最大值
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
//        seriesRenderer.setZoomRate(0.5f);//缩放比例设置
        //图表移动设置
        seriesRenderer.setPanEnabled(false);//图表是否可以移动

        //legend(最下面的文字说明)设置
        seriesRenderer.setShowLegend(true);//控制legend（说明文字 ）是否显示
        seriesRenderer.setLegendHeight(80);//设置说明的高度，单位px
        seriesRenderer.setLegendTextSize(30);//设置说明字体大小
        //坐标轴标签设置
        seriesRenderer.setXLabelsColor(Color.GRAY);
        seriesRenderer.setYLabelsColor(0, Color.GRAY);
        seriesRenderer.setLabelsTextSize(25);//设置标签字体大小
        seriesRenderer.setXLabelsAlign(Paint.Align.CENTER);
        seriesRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        seriesRenderer.setShowGrid(true);
        seriesRenderer.setAxesColor(Color.GRAY);
        seriesRenderer.setGridColor(Color.LTGRAY);
        seriesRenderer.setXLabels(0);//显示的x轴标签的个数
        if (aircleanlishishijian.getState() == 1 || aircleanlishishijian.getState() == 2) {
            seriesRenderer.addXTextLabel(1, "暂无数据");
        } else {
            for (int i = 0; i < aircleanlishishijian.getData().size(); i++) {
                seriesRenderer.addXTextLabel(i + 1, aircleanlishishijian.getData().get(i).getUseDate().substring(5, 10));
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
        XYSeriesRenderer xySeriesRenderer2 = new XYSeriesRenderer();
        xySeriesRenderer2.setPointStyle(PointStyle.CIRCLE);//坐标点的显示风格
        xySeriesRenderer2.setPointStrokeWidth(8);//坐标点的大小
        xySeriesRenderer2.setColor(0xFF049eff);//表示该组数据的图或线的颜色
        xySeriesRenderer2.setDisplayChartValues(false);//设置是否显示坐标点的y轴坐标值
        xySeriesRenderer2.setChartValuesTextSize(12);//设置显示的坐标点值的字体大小
        xySeriesRenderer2.setFillPoints(true);
        xySeriesRenderer2.setLineWidth(3);
//        seriesRenderer.addSeriesRenderer(xySeriesRenderer1);
        seriesRenderer.addSeriesRenderer(xySeriesRenderer2);
        return seriesRenderer;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        valueAnimator.clearAllAnimations();
//        unregisterReceiver(receiver2);
    }

//    private void Connection() {
//        if (MainActivity.socket.isClosed() || MainActivity.socket.isInputShutdown() || MainActivity.socket.isOutputShutdown()) {
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
//                            Log.wtf("这个是建立连接", preferences.getString("workmachineid", "123456789111") + preferences.getString("userSn", "100000001") + preferences.getString("workmachinetypestring", "A1"));
//                            MainActivity.outputStream.write(SetPackage.GetConected(preferences.getString("workmachineid", "123456789111"), preferences.getString("userSn", "100000001"), preferences.getString("workmachinetypestring", "A1")));
//                            MainActivity.outputStream.flush();
//                            Log.wtf("这个是建立连接", "发送了！！！--------------------------");
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
//            if (preferences.getBoolean("xintiaojiesu", true)) {
//                editor.putBoolean("xintiaojiesu", false);
//                editor.commit();
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
//                        editor.putBoolean("xintiaojiesu", true);
//                        editor.commit();
//                    }
//                }).start();
//            }
//        }
//
//    }


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
//                        msg.what = 3;
//                        handler.sendMessage(msg);
//                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
//                        /////有线网络
//                        Message msg = new Message();
//                        msg.what = 3;
//                        handler.sendMessage(msg);
//                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//                        //3g
//                        Message msg = new Message();
//                        msg.what = 3;
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

    public void showHourPickerDialog() {
        NumberPicker mPicker = new NumberPicker(XinfengIndex.this);
        mPicker.setMinValue(0);
        mPicker.setMaxValue(24);
        mPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
                hour = newVal;
                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time = format.format(date);
                hourfinish = Integer.parseInt(time.substring(11, 13)) + newVal;
//                int min = Integer.parseInt(time.substring(14,16));
//                Log.wtf("这个是获取到的时间",hour+"+"+min);
                if (hourfinish >= 24) {
                    hourfinish = hourfinish - 24;
                }
                hour1 = "";
                if (hourfinish < 10) {
                    hour1 = "0" + hourfinish;
                } else {
                    hour1 = "" + hourfinish;
                }


            }
        });
        AlertDialog mAlertDialog = new AlertDialog.Builder(XinfengIndex.this)
                .setTitle("选择定时时长").setView(mPicker).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (hour1 != null && time != null && !hour1.equals("0")) {
                            tv_xinfengindex_shijian.setText("本次净化任务将于 " + hour1 + ":" + time.substring(14, 16) + " 结束");
                            iv_xinfengindex_dingshikongzhi.setImageResource(R.mipmap.xinfengdingshion);
                            editormachine.putString("dingshi", "本次净化任务将于 " + hour1 + ":" + time.substring(14, 16) + " 结束");
                            editormachine.putLong("finishtime", System.currentTimeMillis() + 3600 * 1000 * hour);
                            editormachine.commit();
                        }

                    }
                }).create();
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

    public void Xuanzhuang() {
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        valueAnimator = ObjectAnimator.ofFloat(iv_xingfengindex_yuanquan, "rotation", 0, 360);
        valueAnimator.setInterpolator(linearInterpolator);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(24000);
    }


}
