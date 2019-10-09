package com.ouzhongiot.ozapp.airclean;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MainActivity;
import com.ouzhongiot.ozapp.activity.coldfanAyonghuzhongxin;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.http.ConnectDataTask;
import com.ouzhongiot.ozapp.http.HcNetWorkTask;
import com.ouzhongiot.ozapp.http.PostParamTools;
import com.ouzhongiot.ozapp.others.CircleImageView;
import com.ouzhongiot.ozapp.others.DampView;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.Post;
import com.ouzhongiot.ozapp.others.SetPackage;
import com.ouzhongiot.ozapp.tools.DensityUtil;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.SpData;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author hxf
 * @date 创建时间: 2017/4/25
 * @Description 空气净化器（B1）
 */

public class B1Activity extends BaseHomeActivity implements ConnectDataTask.OnResultDataLintener, View.OnClickListener, View.OnTouchListener, GestureDetector.OnGestureListener {
    public static int cleanFilterScreen = 1600, changeFilterScreen = 800;
    public static ArrayList<Integer> shiwaikongqizhiliang = new ArrayList<>();
    public static ArrayList<Integer> shiwaikongqishijian = new ArrayList<>();
    public static ArrayList<Integer> shineikongqizhiliang = new ArrayList<>();
    public static int kongqinumber = 0;
    GestureDetector mGestureDetector;
    LinearLayout chart, ll_airclean_kongzhitai;
    GraphicalView chartView;
    DampView viewSnsLayout;
    TextView tv_more_data;
    ImageView img_setting;
    LinearLayout llayout_bjshoumin;
    LinearLayout llayout_lwjiedu;
    private NetworkInfo netInfo;
    private ConnectivityManager mConnectivityManager;
    private ImageView img;
    private Timer timer;
    private OutputStream outputStream;
    private TextView tv_coldfanaindex_wenxintishi;
    private boolean isopen1 = false;
    private boolean isopen2 = false;
    private boolean isopen3 = false;
    private LinearLayout ll_bjshoumin, ll_swzhuangtai, ll_lwjiedu;
    private TextView tv_AirCleanindex_kaiguan, tv_AirCleanindex_kaiguan0;
    private ImageView iv_AirCleanindex_kaiguan;
    private boolean ison;
    private GradientDrawable gradientDrawable;
    private CircleImageView iv_AirCleanindex_touxiang;
    private String workmachineid;
    private String workmachinetype;
    private boolean fSwitch, fAuto, fSleep, fUV, fAnion;
    private int fWind = 1, light = 1;
    private TextView tv_coldfanaindex_dizhi;
    private TextView tv_coldfanaindex_temperature, tv_coldfanaindex_quality, tv_coldfanaindex_humidity;
    private TextView tv_aircleanindex_leijishijian, tv_coldfanaindex_fencheng, tv_coldfanaindex_jiangwen, tv_AirCleanindex_dami, tv_AirCleanindex_fengchen;
    private ImageView iv_coldfanAindex_wether;
    private AircleanB1shuju aircleanB1shuju;
    private TextView tv_AirCleanindex_bingjingshouming, tv_AirCleanindex_bingjingxiantiao;
    private AlertDialog.Builder builder;
    private ArcProgress arcProgress;
    private LinearLayout ll_airclean_waichumoshi, ll_airclean_zhinengmoshi, ll_airclean_zidingyimoshi, ll_airclean_zhoumomoshi, ll_airclean_touchuang;
    private ImageView iv_airclean_zidong, iv_airclean_shuimian, iv_airclean_shajun, iv_airclean_fulizi;
    private ImageView iv_AirCleanindex_jieduzhizhen1, iv_AirCleanindex_jieduzhizhen2, iv_AirCleanindex_jieduzhizhen3, iv_AirCleanindex_jieduzhizhen4;
    private TextView tv_airclean_zidong, tv_airclean_shuimian, tv_airclean_shajun, tv_airclean_fulizi;
    private LinearLayout ll_airclean_zidong, ll_airclean_shajun, ll_airclean_fulizi, ll_airclean_shuimian;
    private SeekBar aircleanseekBar;
    private int side;
    private int pm25;
    private String shiwaipm25, shineipm25;
    private TextView tv_airclean_shineishiwai, tv_airclean_mg, tv_airclean_youyushiwai, tv_aircleanindex_kongqizhiliangtishi;
    private ImageView iv_waichumoshigouxuan, iv_waichumoshinext, iv_zhoumomoshigouxuan, iv_zhoumomoshinext, iv_zhinengmoshigouxuan, iv_zhinengmoshinext, iv_zidingyimoshigouxuan, iv_zidingyimoshinext;
    private TextView tv_waichumoshi0, tv_waichumoshi1, tv_zhoumomoshi0, tv_zhoumomoshi1, tv_zhinengmoshi0, tv_zhinengmoshi1, tv_zidingyimoshi0, tv_zidingyimoshi1, tv_airclean_shouming, tv_airclean_jiedu;
    private Aircleanshineikongqi aircleanshineikongqi;
    private int DINGSHIMOSHI = 1;
    private int dangqianshiwaikongqizhiliang, lasttime;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    iv_AirCleanindex_kaiguan.setImageResource(R.mipmap.open);
                    tv_AirCleanindex_kaiguan.setText("开启");
                    gradientDrawable.setColor(getResources().getColor(R.color.airclean));
                    fSwitch = true;
                    break;
                case 2:
                    iv_AirCleanindex_kaiguan.setImageResource(R.mipmap.close);
                    tv_AirCleanindex_kaiguan.setText("关闭");
                    gradientDrawable.setColor(getResources().getColor(R.color.gray));
                    fSwitch = false;
                    ll_airclean_touchuang.setVisibility(View.VISIBLE);
                    break;


                case 8:
                    iv_airclean_zidong.setImageResource(R.mipmap.aircleanzidong0);
                    tv_airclean_zidong.setTextColor(getResources().getColor(R.color.white));
                    ll_airclean_zidong.setBackgroundColor(getResources().getColor(R.color.airclean));
                    fAuto = true;
                    break;
                case 9:
                    iv_airclean_zidong.setImageResource(R.mipmap.aircleanzidong1);
                    tv_airclean_zidong.setTextColor(getResources().getColor(R.color.airclean));
                    ll_airclean_zidong.setBackgroundColor(getResources().getColor(R.color.white));
                    fAuto = false;
                    break;
                case 10:
                    iv_airclean_shuimian.setImageResource(R.mipmap.aircleanshuimian0);
                    tv_airclean_shuimian.setTextColor(getResources().getColor(R.color.white));
                    ll_airclean_shuimian.setBackgroundColor(getResources().getColor(R.color.airclean));
                    fSleep = true;
                    break;
                case 11:
                    iv_airclean_shuimian.setImageResource(R.mipmap.aircleanshuimian1);
                    tv_airclean_shuimian.setTextColor(getResources().getColor(R.color.airclean));
                    ll_airclean_shuimian.setBackgroundColor(getResources().getColor(R.color.white));
                    fSleep = false;
                    break;
                case 12:
                    iv_airclean_shajun.setImageResource(R.mipmap.aircleanshajun0);
                    tv_airclean_shajun.setTextColor(getResources().getColor(R.color.white));
                    ll_airclean_shajun.setBackgroundColor(getResources().getColor(R.color.airclean));
                    fUV = true;
                    break;
                case 13:
                    iv_airclean_shajun.setImageResource(R.mipmap.aircleanshajun1);
                    tv_airclean_shajun.setTextColor(getResources().getColor(R.color.airclean));
                    ll_airclean_shajun.setBackgroundColor(getResources().getColor(R.color.white));
                    fUV = false;
                    break;
                case 14:
                    iv_airclean_fulizi.setImageResource(R.mipmap.aircleanfulizi0);
                    tv_airclean_fulizi.setTextColor(getResources().getColor(R.color.white));
                    ll_airclean_fulizi.setBackgroundColor(getResources().getColor(R.color.airclean));
                    fAnion = true;
                    break;
                case 15:
                    iv_airclean_fulizi.setImageResource(R.mipmap.aircleanfulizi1);
                    tv_airclean_fulizi.setTextColor(getResources().getColor(R.color.airclean));
                    ll_airclean_fulizi.setBackgroundColor(getResources().getColor(R.color.white));
                    fAnion = false;
                    break;
                case 16:
                    aircleanseekBar.setProgress(0);
                    fWind = 1;
                    break;
                case 17:
                    aircleanseekBar.setProgress(50);
                    fWind = 2;
                    break;
                case 18:
                    aircleanseekBar.setProgress(100);
                    fWind = 3;
                    break;

                case 19:
                    if (pm25 > dangqianshiwaikongqizhiliang) {
                        tv_airclean_mg.setVisibility(View.GONE);
                        tv_airclean_shineishiwai.setText("室外空气优于室内");
                        tv_airclean_youyushiwai.setText("请开窗");
                    } else {
                        DecimalFormat df = new DecimalFormat("######0.00");
                        tv_airclean_youyushiwai.setText(df.format((double) (dangqianshiwaikongqizhiliang - pm25) / dangqianshiwaikongqizhiliang * 100) + "");
                    }
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
    private LinearLayout llayout_switch;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("broadcass1")) {    //动作检测
                String receivedata = intent.getStringExtra("msg");
                //开关
                if (MainActivity.b[13] == 0x01) {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[13] == 0x02) {
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
                //自动
                if (MainActivity.b[14] == 0x01) {
                    Message msg = new Message();
                    msg.what = 8;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[14] == 0x02) {
                    Message msg = new Message();
                    msg.what = 9;
                    handler.sendMessage(msg);
                }
                //杀菌
                if (MainActivity.b[15] == 0x01) {
                    Message msg = new Message();
                    msg.what = 12;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[15] == 0x02) {
                    Message msg = new Message();
                    msg.what = 13;
                    handler.sendMessage(msg);
                }
                //睡眠
                if (MainActivity.b[16] == 0x01) {
                    Message msg = new Message();
                    msg.what = 10;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[16] == 0x02) {
                    Message msg = new Message();
                    msg.what = 11;
                    handler.sendMessage(msg);
                }
                //负离子
                if (MainActivity.b[17] == 0x01) {
                    Message msg = new Message();
                    msg.what = 14;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[17] == 0x02) {
                    Message msg = new Message();
                    msg.what = 15;
                    handler.sendMessage(msg);
                }
                //风速
                if (MainActivity.b[18] == 0x01) {
                    Message msg = new Message();
                    msg.what = 16;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[18] == 0x02) {
                    Message msg = new Message();
                    msg.what = 17;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[18] == 0x03) {
                    Message msg = new Message();
                    msg.what = 18;
                    handler.sendMessage(msg);
                }
                //指示灯
                if (MainActivity.b[26] == 0x01) {
                    Message msg = new Message();
                    msg.what = 20;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[26] == 0x02) {
                    Message msg = new Message();
                    msg.what = 21;
                    handler.sendMessage(msg);
                } else if (MainActivity.b[26] == 0x03) {
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

    @Override
    public int addContentView() {
        return R.layout.activity_air_clean_b1;
    }

    @Override
    public void initView() {
        tv_airclean_shouming = (TextView) findViewById(R.id.tv_airclean_shouming);
        tv_airclean_jiedu = (TextView) findViewById(R.id.tv_airclean_jiedu);
        iv_AirCleanindex_jieduzhizhen1 = (ImageView) findViewById(R.id.iv_AirCleanindex_jieduzhizhen1);
        iv_AirCleanindex_jieduzhizhen2 = (ImageView) findViewById(R.id.iv_AirCleanindex_jieduzhizhen2);
        iv_AirCleanindex_jieduzhizhen3 = (ImageView) findViewById(R.id.iv_AirCleanindex_jieduzhizhen3);
        iv_AirCleanindex_jieduzhizhen4 = (ImageView) findViewById(R.id.iv_AirCleanindex_jieduzhizhen4);

        tv_airclean_mg = (TextView) findViewById(R.id.tv_airclean_mg);
        tv_airclean_shineishiwai = (TextView) findViewById(R.id.tv_airclean_shineishiwai);
        tv_airclean_youyushiwai = (TextView) findViewById(R.id.tv_aircleanindex_youyushiwai);
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
        arcProgress = (ArcProgress) findViewById(R.id.arc_progress);
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
        tv_coldfanaindex_humidity = (TextView) findViewById(R.id.tv_coldfanaindex_humidity);
        tv_coldfanaindex_quality = (TextView) findViewById(R.id.tv_coldfanaindex_quality);
        tv_coldfanaindex_temperature = (TextView) findViewById(R.id.tv_coldfanaindex_temperature);
        iv_coldfanAindex_wether = (ImageView) findViewById(R.id.iv_coldfanaindex_wether);

        tv_coldfanaindex_wenxintishi = (TextView) findViewById(R.id.tv_coldfanaindex_wenxintishi);
        viewSnsLayout = (DampView) findViewById(R.id.dampview1);
        tv_more_data = (TextView) findViewById(R.id.airclean_gengduoshuju);
        llayout_switch = (LinearLayout) findViewById(R.id.ll_AirCleanindex_kaiguan);
        img_setting = (ImageView) findViewById(R.id.iv_AirCleanindex_shezhi);
        llayout_bjshoumin = (LinearLayout) findViewById(R.id.tv_bjshoumin);
        llayout_lwjiedu = (LinearLayout) findViewById(R.id.tv_lwjiedu);

    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        outputStream = ((OZApplication) getApplication()).outputStream;
        workmachineid = SpData.getInstance(this).getData(SpConstant.MY_MACHINE_ITEM_CLICKED_DEVICE_SN).toString();
        workmachinetype = SpData.getInstance(this).getData(SpConstant.MY_MACHINE_ITEM_CLICKED_TYPE_SN).toString();
        //这个是跑分
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (arcProgress.getProgress() >= pm25) {

                        } else {
                            arcProgress.setProgress(arcProgress.getProgress() + 1);
                        }

                    }
                });
            }
        }, 1000, 50);
        gradientDrawable = (GradientDrawable) tv_AirCleanindex_kaiguan0.getBackground();
        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener) this);
        //请求当前状态
        requestCurState();
        //查询当前数据
        queryCurData();
        //设置点击事件
        setClick();
        viewSnsLayout.setOnTouchListener(this);
        viewSnsLayout.setLongClickable(true);
        setupView();
        //获取室外pm2.5
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求当前数据
                String uriAPI2 = UrlConstant.BASE_URL_NINETY + "adapter/weather/queryAqi";
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                if (SpData.getInstance(B1Activity.this).getData(SpConstant.LOCATION_CITY).toString().equals("")) {
                    //不能带市
                    params2.add(new BasicNameValuePair("city", "杭州"));

                } else {
                    String city = SpData.getInstance(B1Activity.this).getData(SpConstant.LOCATION_CITY).toString().substring(0, SpData.getInstance(B1Activity.this).getData(SpConstant.LOCATION_CITY).toString().length() - 1);
                    params2.add(new BasicNameValuePair("city", city));

                }
                params2.add(new BasicNameValuePair("times", "12"));
                shiwaipm25 = Post.dopost(uriAPI2, params2);
                Log.wtf("室外空气质量", shiwaipm25);
                Map<String, Object> map = JacksonUtil.deserializeJsonToObject(shiwaipm25, Map.class);
                String datastr = JacksonUtil.serializeObjectToJson(map.get("data"));
                Map<String, Object> data = JacksonUtil.deserializeJsonToObject(datastr, Map.class);
                if (data == null) {
                } else {
                    String shiwaipm25str = JacksonUtil.serializeObjectToJson(data.get("pm25"));
                    LinkedHashMap<String, String> shiwaipm25map = JacksonUtil.deserializeJsonToObject(shiwaipm25str, LinkedHashMap.class);
                    dangqianshiwaikongqizhiliang = 0;
                    for (Map.Entry<String, String> entry : shiwaipm25map.entrySet()) {

                        shiwaikongqishijian.add(Integer.parseInt(entry.getKey()));
//                        Log.wtf("这个是每个点的时间",entry.getKey());
                        if (entry.getValue() != null) {
                            dangqianshiwaikongqizhiliang = Integer.valueOf(entry.getValue()) + 30;

                            shiwaikongqizhiliang.add(Integer.valueOf(entry.getValue()));
                            lasttime = Integer.parseInt(entry.getKey());
                        } else {
                            if (shiwaikongqizhiliang.size() > 0) {
                                shiwaikongqizhiliang.add(shiwaikongqizhiliang.get(shiwaikongqizhiliang.size() - 1));
                            }
                        }
//                        Log.wtf("这个是最新室外的空气质量", dangqianshiwaikongqizhiliang + "----------------------------------------"+shiwaikongqizhiliang.size());
                    }
                }

                String uriAPI = MainActivity.ip + "smarthome/air/queryDeviceAirAqi";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("devTypeSn", workmachinetype));
                params.add(new BasicNameValuePair("devSn", workmachineid));
                params.add(new BasicNameValuePair("lastTime", lasttime + ""));
                params.add(new BasicNameValuePair("times", "12"));
                shineipm25 = Post.dopost(uriAPI, params);
                Log.wtf("室内空气质量---------", shineipm25);
                Map<String, Object> map2 = JacksonUtil.deserializeJsonToObject(shineipm25, Map.class);

                if (map2.get("data") == null) {
                } else {
                    String datastr2 = JacksonUtil.serializeObjectToJson(map2.get("data"));
                    LinkedHashMap<String, Integer> shiwaipm25map2 = JacksonUtil.deserializeJsonToObject(datastr2, LinkedHashMap.class);
                    for (Map.Entry<String, Integer> entry : shiwaipm25map2.entrySet()) {
                        if (entry.getValue() != null) {
                            pm25 = entry.getValue() / 4;
                        }

                        shineikongqizhiliang.add(entry.getValue());
                    }
                }
//                Log.wtf("这个是室内空气质量的长度","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!======="+shineikongqizhiliang.size());
                for (int i = 0, j = 0; i < shineikongqizhiliang.size(); i++) {
                    if (shineikongqizhiliang.get(i) != null) {
                        break;
                    } else {
                        j++;
                        kongqinumber = j;
                    }


                }

                Message msg = new Message();
                msg.what = 19;
                handler.sendMessage(msg);


            }
        }).start();

        //初始化天气
        if (SpData.getInstance(this).getData(SpConstant.WEATHER_CHUANYI).equals("")) {
            tv_coldfanaindex_wenxintishi.setText("今天气温很高，适合穿短袖，注意防晒和避暑,祝您有一个愉快的周末");
        } else {
            tv_coldfanaindex_wenxintishi.setText(SpData.getInstance(this).getData(SpConstant.WEATHER_CHUANYI).toString());

        }

        tv_coldfanaindex_humidity.setText("湿度  " + SpData.getInstance(this).getData(SpConstant.WEATHER_HUM).toString() + "%");
        tv_coldfanaindex_temperature.setText("温度  " + SpData.getInstance(this).getData(SpConstant.WEATHER_TEMP).toString() + "℃");
        tv_coldfanaindex_quality.setText("空气质量  " + SpData.getInstance(this).getData(SpConstant.WEATHER_QUALITY).toString());
        String weather_info;
        weather_info = SpData.getInstance(this).getData(SpConstant.WEATHER_INFO).toString();
        if (weather_info.contains("阴")) {
            iv_coldfanAindex_wether.setImageResource(R.mipmap.weather_yin);
        }
        if (weather_info.contains("云")) {
            iv_coldfanAindex_wether.setImageResource(R.mipmap.weather_duoyun);
        }
        if (weather_info.contains("晴")) {
            iv_coldfanAindex_wether.setImageResource(R.mipmap.weather_qing);
        }
        if (weather_info.contains("雨")) {
            iv_coldfanAindex_wether.setImageResource(R.mipmap.weather_yu);
        }
        if (weather_info.contains("雪")) {
            iv_coldfanAindex_wether.setImageResource(R.mipmap.weather_xue);
        }
        if (weather_info.contains("雾") || weather_info.contains("霾")) {
            iv_coldfanAindex_wether.setImageResource(R.mipmap.weather_xue);
        }

        tv_coldfanaindex_dizhi.setText(SpData.getInstance(this).getData(SpConstant.LOCATION_DISTRICT).toString());

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
                    ((OZApplication) getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid, workmachinetype, "W", (byte) 0x01));
                } else if (side < 75) {
                    seekBar.setProgress(50);
                    ((OZApplication) getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid, workmachinetype, "W", (byte) 0x02));
                } else {
                    seekBar.setProgress(100);
                    ((OZApplication) getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid, workmachinetype, "W", (byte) 0x03));
                }

            }
        });

        if (!((OZApplication) getApplication()).getISTOUXIANGUPDATE()) {
            if (!SpData.getInstance(this).getData(SpConstant.LOGIN_HEADURL).toString().equals("")) {
                ImageLoader.getInstance().displayImage(SpData.getInstance(this).getData(SpConstant.LOGIN_HEADURL).toString(), iv_AirCleanindex_touxiang);
            }
        } else {
          /*  if (coldfanAindex.getLoacalBitmap(preference2.getString("touxiangbendiurl", Environment.getExternalStorageDirectory() + "/"
                    + "lianxiatouxiang" + ".jpg")) != null) {

                iv_AirCleanindex_touxiang.setImageBitmap(coldfanAindex.getLoacalBitmap(preference2.getString("touxiangbendiurl", Environment.getExternalStorageDirectory() + "/"
                        + "lianxiatouxiang" + ".jpg")));
            }*/

        }
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
//        DINGSHIMOSHI = preferencesmachine.getInt("DINGSHIMOSHI", 1);
//        switch (DINGSHIMOSHI) {
//            case 1:
//                iv_waichumoshigouxuan.setImageResource(R.mipmap.aircleandingshi);
//                iv_waichumoshinext.setImageResource(R.mipmap.aircleannext);
//                tv_waichumoshi0.setTextColor(getResources().getColor(R.color.airclean));
//                tv_waichumoshi1.setTextColor(getResources().getColor(R.color.airclean));
//                break;
//            case 2:
//                iv_zhoumomoshigouxuan.setImageResource(R.mipmap.aircleandingshi);
//                iv_zhoumomoshinext.setImageResource(R.mipmap.aircleannext);
//                tv_zhoumomoshi0.setTextColor(getResources().getColor(R.color.airclean));
//                tv_zhoumomoshi1.setTextColor(getResources().getColor(R.color.airclean));
//                break;
//            case 3:
//                iv_zhinengmoshigouxuan.setImageResource(R.mipmap.aircleandingshi);
//                iv_zhinengmoshinext.setImageResource(R.mipmap.aircleannext);
//                tv_zhinengmoshi0.setTextColor(getResources().getColor(R.color.airclean));
//                tv_zhinengmoshi1.setTextColor(getResources().getColor(R.color.airclean));
//                break;
//            case 4:
//                iv_zidingyimoshigouxuan.setImageResource(R.mipmap.aircleandingshi);
//                iv_zidingyimoshinext.setImageResource(R.mipmap.aircleannext);
//                tv_zidingyimoshi0.setTextColor(getResources().getColor(R.color.airclean));
//                tv_zidingyimoshi1.setTextColor(getResources().getColor(R.color.airclean));
//                break;
//            default:
//                break;
//        }


    }

    public void requestCurState() {
        new HcNetWorkTask(this, this, 1).doPost(UrlConstant.QUERY_STATE_B1, null, postParams(1).getBytes());

    }

    public void queryCurData() {
        new HcNetWorkTask(this, this, 2).doPost(UrlConstant.QUERY_DATA_B1, null, postParams(2).getBytes());


    }

    public void setClick() {
        ll_airclean_waichumoshi.setOnClickListener(this);
        ll_airclean_zhinengmoshi.setOnClickListener(this);
        ll_airclean_zhoumomoshi.setOnClickListener(this);
        ll_airclean_zidingyimoshi.setOnClickListener(this);
        tv_more_data.setOnClickListener(this);
        llayout_switch.setOnClickListener(this);
        ll_airclean_zidong.setOnClickListener(this);
        ll_airclean_shuimian.setOnClickListener(this);
        ll_airclean_shajun.setOnClickListener(this);
        ll_airclean_fulizi.setOnClickListener(this);
        img_setting.setOnClickListener(this);
        iv_AirCleanindex_touxiang.setOnClickListener(this);
        llayout_bjshoumin.setOnClickListener(this);
        llayout_lwjiedu.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_airclean_waichumoshi:
                //外出模式
                Intent intent = new Intent(this, aircleanwaichumoshi.class);
                startActivity(intent);
                break;
            case R.id.ll_airclean_zhinengmoshi:
                //智能模式
                startActivity(new Intent(this, aircleanzhinengmoshi.class));
                break;
            case R.id.ll_airclean_zhoumomoshi:
                //周末模式
                startActivity(new Intent(this, aircleanzhoumomoshi.class));
                break;
            case R.id.ll_airclean_zidingyimoshi:
                //自定义模式
                startActivity(new Intent(this, aircleanzidingyimoshi.class));
                break;
            case R.id.airclean_gengduoshuju:
                Intent moreIntent = new Intent(this, Aircleangengduoshuju.class);
                moreIntent.putExtra(Aircleangengduoshuju.PARAM_PM25, pm25);
                moreIntent.putExtra(Aircleangengduoshuju.PARAM_OUTWIDE_PM25, dangqianshiwaikongqizhiliang);
                moreIntent.putExtra(Aircleangengduoshuju.PARAM_FENCHEN, tv_AirCleanindex_fengchen.getText().toString());
                moreIntent.putExtra(Aircleangengduoshuju.PARAM_DAMI, tv_AirCleanindex_dami.getText().toString());
                moreIntent.putExtra(Aircleangengduoshuju.PARAM_LEIJI, tv_aircleanindex_leijishijian.getText().toString());
                startActivity(moreIntent);
                break;
            case R.id.ll_AirCleanindex_kaiguan:
                //开关
                if (fSwitch) {
                    ((OZApplication) getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid, workmachinetype, "S", (byte) 0x02));
                } else {
                    ((OZApplication) getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid, workmachinetype, "S", (byte) 0x01));
                }
                break;
            case R.id.ll_airclean_zidong:
                //自动
                if (fAuto) {
                    ((OZApplication) getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid, workmachinetype, "A", (byte) 0x02));
                } else {
                    ((OZApplication) getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid, workmachinetype, "A", (byte) 0x01));
                }
                break;
            case R.id.ll_airclean_shuimian:
                //睡眠
                if (fSleep) {
                    ((OZApplication) getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid, workmachinetype, "L", (byte) 0x02));
                } else {
                    ((OZApplication) getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid, workmachinetype, "L", (byte) 0x01));
                }
                break;
            case R.id.ll_airclean_shajun:
                //杀菌
                if (fUV) {
                    ((OZApplication) getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid, workmachinetype, "U", (byte) 0x02));

                } else {
                    ((OZApplication) getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid, workmachinetype, "U", (byte) 0x01));
                }
                break;
            case R.id.ll_airclean_fulizi:
                if (fAnion) {

                    ((OZApplication) getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid, workmachinetype, "N", (byte) 0x02));
                } else {

                    ((OZApplication) getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid, workmachinetype, "N", (byte) 0x01));
                }
                break;
            case R.id.iv_AirCleanindex_shezhi:
                startActivity(new Intent(this, coldfanAyonghuzhongxin.class));
                break;
            case R.id.iv_AirCleanindex_touxiang:
                startActivity(new Intent(this, coldfanAyonghuzhongxin.class));
                break;
            case R.id.tv_bjshoumin:
                if (isopen1) {
                    ll_bjshoumin.setVisibility(View.GONE);
                    isopen1 = false;
                } else {
                    ll_bjshoumin.setVisibility(View.VISIBLE);
                    isopen1 = true;
                }
                break;
            case R.id.tv_lwjiedu:
                if (isopen3) {
                    ll_lwjiedu.setVisibility(View.GONE);
                    isopen3 = false;
                } else {
                    ll_lwjiedu.setVisibility(View.VISIBLE);
                    isopen3 = true;
                }
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
            params.put("devTypeSn", workmachinetype);
            params.put("devSn", workmachineid);
            if (LogTools.debug) {
                LogTools.i("当前状态参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        } else if (code == 2) {
            //查询当前数据
            params.put("devTypeSn", workmachinetype);
            params.put("devSn", workmachineid);
            if (LogTools.debug) {
                LogTools.i("当前数据参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);

        }
        return "";
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
        AirCleanindex.SnsConstant snsConstant = new AirCleanindex.SnsConstant();
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
                OZApplication.getInstance().finishActivity();
//            Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onResult(String result, int code) {
        if (result != null) {
            try {
                JSONObject object = new JSONObject(result);
                if (code == 1) {
                    //查询当前状态
                    int state = object.getInt("state");
                    if (state == 0) {
                        JSONObject dataObject = object.getJSONObject("data");
                        fSwitch = dataObject.getBoolean("fSwitch");
                        if (fSwitch) {
                            iv_AirCleanindex_kaiguan.setImageResource(R.mipmap.open);
                            tv_AirCleanindex_kaiguan.setText("开启");
                            gradientDrawable.setColor(getResources().getColor(R.color.airclean));
                            ll_airclean_touchuang.setVisibility(View.GONE);
                        } else {
                            iv_AirCleanindex_kaiguan.setImageResource(R.mipmap.close);
                            tv_AirCleanindex_kaiguan.setText("关闭");
                            gradientDrawable.setColor(getResources().getColor(R.color.gray));
                            ll_airclean_touchuang.setVisibility(View.VISIBLE);
                        }

                        fAuto = dataObject.getBoolean("fAuto");
                        if (fAuto) {
                            iv_airclean_zidong.setImageResource(R.mipmap.aircleanzidong0);
                            tv_airclean_zidong.setTextColor(getResources().getColor(R.color.white));
                            ll_airclean_zidong.setBackgroundColor(getResources().getColor(R.color.airclean));
                        } else {
                            iv_airclean_zidong.setImageResource(R.mipmap.aircleanzidong1);
                            tv_airclean_zidong.setTextColor(getResources().getColor(R.color.airclean));
                            ll_airclean_zidong.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                        fSleep = dataObject.getBoolean("fSleep");
                        if (fSleep) {
                            iv_airclean_shuimian.setImageResource(R.mipmap.aircleanshuimian0);
                            tv_airclean_shuimian.setTextColor(getResources().getColor(R.color.white));
                            ll_airclean_shuimian.setBackgroundColor(getResources().getColor(R.color.airclean));
                        } else {
                            iv_airclean_shuimian.setImageResource(R.mipmap.aircleanshuimian1);
                            tv_airclean_shuimian.setTextColor(getResources().getColor(R.color.airclean));
                            ll_airclean_shuimian.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                        fAnion = dataObject.getBoolean("fAnion");
                        if (fAnion) {
                            iv_airclean_fulizi.setImageResource(R.mipmap.aircleanfulizi0);
                            tv_airclean_fulizi.setTextColor(getResources().getColor(R.color.white));
                            ll_airclean_fulizi.setBackgroundColor(getResources().getColor(R.color.airclean));

                        } else {
                            iv_airclean_fulizi.setImageResource(R.mipmap.aircleanfulizi1);
                            tv_airclean_fulizi.setTextColor(getResources().getColor(R.color.airclean));
                            ll_airclean_fulizi.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                        fUV = dataObject.getBoolean("fUV");
                        if (fUV) {
                            iv_airclean_shajun.setImageResource(R.mipmap.aircleanshajun0);
                            tv_airclean_shajun.setTextColor(getResources().getColor(R.color.white));
                            ll_airclean_shajun.setBackgroundColor(getResources().getColor(R.color.airclean));

                        } else {
                            iv_airclean_shajun.setImageResource(R.mipmap.aircleanshajun1);
                            tv_airclean_shajun.setTextColor(getResources().getColor(R.color.airclean));
                            ll_airclean_shajun.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                        fWind = dataObject.getInt("fWind");
                        //风速
                        if (fWind == 1) {
                            aircleanseekBar.setProgress(0);
                        } else if (fWind == 2) {
                            aircleanseekBar.setProgress(50);
                        } else if (fWind == 3) {
                            aircleanseekBar.setProgress(100);
                        }
                        pm25 = dataObject.getInt("sPm25") / 4;

                        cleanFilterScreen = dataObject.getInt("sCleanFilterScreen");
                        changeFilterScreen = dataObject.getInt("sChangeFilterScreen");
                        if (cleanFilterScreen < 8) {
                            tv_airclean_jiedu.setVisibility(View.VISIBLE);
                        } else {
                            tv_airclean_jiedu.setVisibility(View.GONE);
                        }

                        if (changeFilterScreen < 16) {
                            tv_airclean_shouming.setVisibility(View.VISIBLE);
                        } else {
                            tv_airclean_shouming.setVisibility(View.GONE);
                        }
                        light = dataObject.getInt("fLight");
                        if (light == 1) {
                            tv_aircleanindex_kongqizhiliangtishi.setText("优");
                        } else if (light == 2) {
                            tv_aircleanindex_kongqizhiliangtishi.setText("良");
                        } else if (light == 3) {
                            tv_aircleanindex_kongqizhiliangtishi.setText("差");
                        }

                    } else if (state == 1) {
                        //参数异常或为空
                        ToastTools.show(this, getString(R.string.txt_query_state_param));
                    } else if (state == 2) {
                        //没有数据
                        ToastTools.show(this, getString(R.string.txt_query_state_nodata));
                    }
                } else if (code == 2) {
                    //查询当前数据
                    int state = object.getInt("state");
                    if (state == 0) {
                        JSONObject dataObject = object.getJSONObject("data");
                        DecimalFormat df = new DecimalFormat("######0.00");
                        tv_aircleanindex_leijishijian.setText(df.format((double) dataObject.getLong("totalTime") / 1000 / 3600) + "");
                        tv_AirCleanindex_fengchen.setText(df.format((double) dataObject.getLong("totalTime") / 1000 / 3600 * 0.3575) + "");
                        tv_AirCleanindex_dami.setText("相当于" + df.format((double) dataObject.getLong("totalTime") / 1000 / 3600 * 0.3575 / 25) + "粒大米");
                        //滤网寿命
                        tv_AirCleanindex_bingjingshouming.setText(changeFilterScreen + "");
                        tv_AirCleanindex_bingjingxiantiao.getLayoutParams().width = DensityUtil.dip2px(B1Activity.this, 320) * changeFilterScreen / 1600;
                        //滤网洁度
                        if (cleanFilterScreen > 600) {
                            iv_AirCleanindex_jieduzhizhen1.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen2.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen3.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen4.setVisibility(View.VISIBLE);
                        } else if (cleanFilterScreen > 400) {
                            iv_AirCleanindex_jieduzhizhen1.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen2.setVisibility(View.INVISIBLE);
                            iv_AirCleanindex_jieduzhizhen3.setVisibility(View.VISIBLE);
                            iv_AirCleanindex_jieduzhizhen4.setVisibility(View.INVISIBLE);

                        } else if (cleanFilterScreen > 200) {
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

                    } else if (state == 1) {
                        //参数异常或为空
                        ToastTools.show(this, getString(R.string.txt_query_state_param));
                    } else if (state == 2) {
                        //没有数据
                        ToastTools.show(this, getString(R.string.txt_query_state_nodata));
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


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

    private void showChart() {
        XYMultipleSeriesDataset mDataSet = getDataSet();
        XYMultipleSeriesRenderer mRefender = getRefender();
        chartView = ChartFactory.getLineChartView(this, mDataSet, mRefender);
        chart.addView(chartView);
    }

    private XYMultipleSeriesDataset getDataSet() {
        XYMultipleSeriesDataset seriesDataset = new XYMultipleSeriesDataset();
        XYSeries xySeries1 = new XYSeries("室外空气质量");


        for (int i = kongqinumber, j = 1; i < 12; i++, j++) {
            xySeries1.add(j, shiwaikongqizhiliang.get(i));
        }


        seriesDataset.addSeries(xySeries1);

        XYSeries xySeries2 = new XYSeries("室内空气质量");

        for (int i = kongqinumber, j = 1; i < 12; i++, j++) {
            Log.wtf("室外空气的x轴", shineikongqizhiliang.get(i) + "~~~~~~~~~~~~~~~~~~~~~~~~~~~" + i);
            if (shineikongqizhiliang.get(i) == null) {

            } else {
                xySeries2.add(j, shineikongqizhiliang.get(i) / 4);
            }

        }


        seriesDataset.addSeries(xySeries2);

        return seriesDataset;
    }

    private XYMultipleSeriesRenderer getRefender() {
        /*描绘器，设置图表整体效果，比如x,y轴效果，缩放比例，颜色设置*/
        XYMultipleSeriesRenderer seriesRenderer = new XYMultipleSeriesRenderer();

        seriesRenderer.setChartTitleTextSize(20);//设置图表标题的字体大小(图的最上面文字)
        seriesRenderer.setMargins(new int[]{20, 80, 60, 60});//设置外边距，顺序为：上左下右
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
        seriesRenderer.setYLabelsColor(0, Color.GRAY);
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

        for (int i = kongqinumber, j = 1; i < shiwaikongqishijian.size(); i++, j++) {
//            Log.wtf("这个是时间---------------------",shiwaikongqishijian.get(i).toString()+"            "+i);
            seriesRenderer.addXTextLabel(j, shiwaikongqishijian.get(i).toString());
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
        XYSeriesRenderer xySeriesRenderer1 = new XYSeriesRenderer();
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
        XYSeriesRenderer xySeriesRenderer2 = new XYSeriesRenderer();
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
        ((OZApplication) getApplication()).SendOrder(SetPackage.GetMachineQuit(SpData.getInstance(this).getData(SpConstant.MY_MACHINE_ITEM_CLICKED_DEVICE_SN).toString(),
                SpData.getInstance(this).getData(SpConstant.LOGIN_USERSN).toString(),
                SpData.getInstance(this).getData(SpConstant.MY_MACHINE_ITEM_CLICKED_TYPE_STRING).toString()));
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

}
