package com.ouzhongiot.ozapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.http.ConnectDataTask;
import com.ouzhongiot.ozapp.http.HcNetWorkTask;
import com.ouzhongiot.ozapp.http.PostParamTools;
import com.ouzhongiot.ozapp.tools.DensityUtil;
import com.ouzhongiot.ozapp.tools.IconfontTools;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.SocketOrderTools;
import com.ouzhongiot.ozapp.tools.SystemTools;
import com.ouzhongiot.ozapp.tools.TimeFormatTools;
import com.ouzhongiot.ozapp.tools.ToastTools;
import com.ouzhongiot.ozapp.web.MachineProductDesActivity;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * @author hxf
 * @date 创建时间: 2016/11/3
 * @Description 新风空气净化器页面
 */

public class XFAirActivity extends BaseHomeActivity implements View.OnClickListener, ConnectDataTask.OnResultDataLintener {
    private FrameLayout root_layout;
    private LinearLayout llayout_back;//返回
    private TextView tv_device_name;//设备名称
    //    private ImageView img_more;//更多
    private TextView tv_more;
    private ScrollView scroll_layout;
    private LinearLayout llayout_module1;//模块1
    private ImageView img_rotate;
    private TextView pm_tv;//PM2.5的值
    private TextView filter_tv;//滤芯剩余
    private ImageView img_current_wind;//当前的风速
    private TextView current_model_tv;//当前的模式
    private TextView temp_tv;//温度
    private TextView humi_tv;//湿度
    private TextView co2_tv;//co2
    private TextView form_tv;//甲醛

    private LinearLayout llayout_auto;//自动
    private ImageView img_auto;
    private LinearLayout llayout_hand;//手动
    private ImageView img_hand;
    private Button btn_led_auto;//自动模式
    private LinearLayout llayout_LED;//LED彩灯
    private LinearLayout llayout_ion;//负离子
    private ImageView img_ion;
    private LinearLayout llayout_wind;//风速
    private LinearLayout llayout_model;//模式
    private ImageView img_model;

    private LinearLayout llayout_time_task;
    private TextView time_tv;//定时字体
    private ImageView img_time;//定时开关

    private LinearLayout llayout_history;//历史记录


    private LinearLayout layout_bg_layer;//透明度图层
    private LinearLayout layout_btn_layer;//按钮的透明度图层

    private LinearLayout llayout_switch;//开关布局
    private ImageView img_switch;//开关图片
    private TextView switch_tv;//开关字体


    private PopupWindow moreWindow;//更多
    private PopupWindow modelWindow;//模式


    //more下的控件
    private TextView pm25_tv;//修改名称
    private TextView modify_name_tv;//修改名称
    private TextView remove_tv;//移除设备
    private TextView shuoming_tv;//产品说明
    private TextView help_tv;//使用帮助
    private TextView contact_tv;//联系我们
    private TextView cancle_tv;//取消
    //model下的控件
    private TextView sleep_tv;//睡眠
    private TextView natural_tv;//自然
    private TextView eff_tv;//高效
    private TextView com_tv;//舒适
    private TextView model_cancle_tv;//取消

    public static String PARAM_DEVICE_ID = "param_device_id";//用户与设备关联关系的ID，删除关联时用到
    public static String PARAM_DEVICE_TYPE_SN = "param_device_type_sn";//设备类型编号
    public static String PARAM_DEVICE_SN = "param_device_sn";//设备编号
    public static String PARAM_USER_SN = "param_user_sn";//用户编号
    public static String PARAM_DEVICE_NAME = "param_device_name";//设备名

    private String deviceID;//用户与设备关联关系的ID，删除关联时用到
    private String deviceTypeSn;//设备类型编号
    private String deviceSn;//设备编号
    private String userSn;//用户编号
    private String deviceName;//设备名
    private Boolean isAuto = false;//自动按钮的状态
    private Boolean isIon = false;//负离子按钮的状态
    private int windSpeed = 1;//风速

    private Boolean isSetTime = false;//是否设置了定时
    private byte setHour;//设置定时的小时
    private byte setMin;//设置定时的分钟

    private Boolean isSwitch = false;//开关的状态
    private int led = -1;//led彩灯值

    private ValueAnimator valueAnimator;
    private float currenttime;
    private ImageView img_LED;
    //LED彩灯
    private Dialog LEDDialog;
    private RelativeLayout LEDlayout;
    private Button btn_led_pause;//暂停
    private Button btn_led_close;//关闭
    private TextView font_led_red;//红
    private TextView font_led_green;//绿
    private TextView font_led_blue;//蓝
    private TextView font_led_yellow;//黄
    private TextView font_led_qing;//青
    private TextView font_led_zi;//紫
    private TextView font_led_white;//白
    private TextView font_led_red_green_blue;//红绿蓝
    private TextView font_led_yellow_qing_zi;//黄青紫
    private TextView font_led_seven_run;//七色转动
    private TextView font_led_seven_shadow;//七色渐变
    private TextView font_led_run;//幻彩转动
    private TextView font_led_seven_breathe;//七色呼吸

    private Boolean isPm = true;//pm25的状态（默认是开着的）
    //    private Boolean isFirstEnter = true;//主要是为了标识：第一次进来第31位没有收到1指令就默认是开启pm2.5
    BroadcastReceiver serviceToAppReveiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("broadcass1")) {
                //根据服务->App的返回值展示页面
                if (LogTools.debug) {
                    StringBuilder stringBuilder = new StringBuilder("");
                    for (int i = 0; i < MainActivity.b.length; i++) {
                        int v = MainActivity.b[i] & 0xFF;
                        String hv = Integer.toHexString(v);
                        if (hv.length() < 2) {
                            stringBuilder.append(0);
                        }
                        stringBuilder.append(hv);
                    }
                    LogTools.e("服务器->App返回的数据->" + stringBuilder.toString());
                }
//                if (isFirstEnter) {
//                    isFirstEnter = false;
//                    if (MainActivity.b[31] != 0x01) {
//                        //刚进来没有收到开启锁定指令，就默认为开起检测
//                        isPm = true;
//                        if (MainActivity.b[21] != 0x00 || MainActivity.b[22] != 0x00) {
//                            pm_tv.setText(String.valueOf((MainActivity.b[21] & 0xFF) * 256 + (MainActivity.b[22] & 0xFF)));
//                        }
//                        pm25_tv.setText("关闭pm2.5检测");
//
//                    } else {
//                        isPm = false;
//                        pm_tv.setText("立即检测");
//                        pm25_tv.setText("立即检测");
//
//                    }
//                } else {
//                    //不是第一次，就按照发1代表开启锁定(没有检测)，发2代表正在检测，发0无效不理会
//                    if (MainActivity.b[31] == 0x02) {
//                        //开启了检测，显示数值
//                        isPm = true;
//                        if (MainActivity.b[21] != 0x00 || MainActivity.b[22] != 0x00) {
//                            pm_tv.setText(String.valueOf((MainActivity.b[21] & 0xFF) * 256 + (MainActivity.b[22] & 0xFF)));
//                        }
//                        pm25_tv.setText("关闭pm2.5检测");
//
//                    } else if (MainActivity.b[31] == 0x01) {
//                        isPm = false;
//                        pm_tv.setText("立即检测");
//                        pm25_tv.setText("立即检测");
//
//                    }
//                }
                if (isPm) {
                    pm_tv.setTextSize(60);
                    pm_tv.setText(String.valueOf((MainActivity.b[21] & 0xFF) * 256 + (MainActivity.b[22] & 0xFF)));
                }

                //开关
                if (MainActivity.b[14] == 0x01) {
                    //开，展示页面效果
                    isSwitch = true;
                    llayout_switch.setBackgroundResource(R.drawable.shape_xinfeng_switch_on_bg);
                    img_switch.setImageResource(R.mipmap.xinfeng_switch_off);
                    switch_tv.setText(getString(R.string.xinfeng_switch_on_txt));
                    switch_tv.setTextColor(getResources().getColor(R.color.white));
                    layout_bg_layer.setVisibility(View.GONE);
//                    layout_btn_layer.setVisibility(View.GONE);
                    //旋转动画
                    if (valueAnimator.isRunning()) {

                    } else {
                        valueAnimator.start();
                    }

                } else if (MainActivity.b[14] == 0x02) {
                    //关
                    if (valueAnimator.isRunning()) {
                        valueAnimator.cancel();
                    }
                    isSwitch = false;
                    llayout_switch.setBackgroundResource(R.drawable.shape_xinfeng_switch_off_bg);
                    img_switch.setImageResource(R.mipmap.xinfeng_switch_on);
                    switch_tv.setText(getString(R.string.xinfeng_switch_off_txt));
                    switch_tv.setTextColor(getResources().getColor(R.color.xinfeng));
                    layout_bg_layer.setVisibility(View.VISIBLE);
//                    layout_btn_layer.setVisibility(View.VISIBLE);
                }
                //自动
                if (MainActivity.b[15] == 0x01) {
                    //自动
                    isAuto = true;
                    img_auto.setImageResource(R.mipmap.xinfengzidong1);
                    img_hand.setImageResource(R.mipmap.xinfenghand1);
                } else if (MainActivity.b[15] == 0x02) {
                    //手工
                    isAuto = false;
                    img_auto.setImageResource(R.mipmap.xinfengzidong2);
                    img_hand.setImageResource(R.mipmap.xinfenghand2);
                }
                //负离子
                if (MainActivity.b[17] == 0x01) {
                    //开启
                    isIon = true;
                    img_ion.setImageResource(R.mipmap.xinfengfulizi1);
                } else if (MainActivity.b[17] == 0x02) {
                    //关闭
                    isIon = false;
                    img_ion.setImageResource(R.mipmap.xinfengfulizi2);

                }
                //风速
                if (MainActivity.b[20] == 0x02) {
                    //二级
                    windSpeed = 2;
                    img_current_wind.setImageResource(R.mipmap.xinfengwind2);
                    currenttime = valueAnimator.getAnimatedFraction();
                    valueAnimator.setDuration(24000);
                    valueAnimator.setCurrentPlayTime((long) (24000 * currenttime));
                } else if (MainActivity.b[20] == 0x03) {
                    //三级
                    windSpeed = 3;
                    img_current_wind.setImageResource(R.mipmap.xinfengwind3);
                    currenttime = valueAnimator.getAnimatedFraction();
                    valueAnimator.setDuration(16000);
                    valueAnimator.setCurrentPlayTime((long) (16000 * currenttime));
                } else if (MainActivity.b[20] == 0x04) {
                    //四级
                    windSpeed = 4;
                    img_current_wind.setImageResource(R.mipmap.xinfengwind4);
                    currenttime = valueAnimator.getAnimatedFraction();
                    valueAnimator.setDuration(8000);
                    valueAnimator.setCurrentPlayTime((long) (8000 * currenttime));

                } else if (MainActivity.b[20] == 0x01) {
                    //风速一级
                    windSpeed = 1;
                    img_current_wind.setImageResource(R.mipmap.xinfengwind1);
                    currenttime = valueAnimator.getAnimatedFraction();
                    valueAnimator.setDuration(32000);
                    valueAnimator.setCurrentPlayTime((long) (32000 * currenttime));
                } else if (MainActivity.b[20] == 0x00) {
                    //出现了定时以后关，然后转圈仍然转，然后才加上下面的代码
                    if (valueAnimator.isRunning()) {
                        valueAnimator.cancel();
                    }
                }
                //睡眠模式（风速1档，自动关  负离子开）
                if (MainActivity.b[15] == 0x02 && MainActivity.b[17] == 0x01 && MainActivity.b[20] == 0x01) {
                    //风速1档
                    windSpeed = 1;
                    img_current_wind.setImageResource(R.mipmap.xinfengwind1);
                    currenttime = valueAnimator.getAnimatedFraction();
                    valueAnimator.setDuration(32000);
                    valueAnimator.setCurrentPlayTime((long) (32000 * currenttime));
                    //自动关
                    isAuto = false;
                    img_auto.setImageResource(R.mipmap.xinfengzidong2);
                    //负离子开
                    isIon = true;
                    img_ion.setImageResource(R.mipmap.xinfengfulizi1);
                    //睡眠模式的字体颜色
                    setModelTvColor();
                    sleep_tv.setTextColor(getResources().getColor(R.color.xinfeng));
                    //显示睡眠模式
                    current_model_tv.setText("睡眠模式");
                    img_model.setImageResource(R.mipmap.xinfengmoshi1);
                }
                //节能模式(负离子关 灯光关 手动模式 风速为1)
                if (MainActivity.b[17] == 0x02 && MainActivity.b[16] == 0x01 && MainActivity.b[15] == 0x02 && MainActivity.b[20] == 0x01) {
                    isIon = false;
                    img_ion.setImageResource(R.mipmap.xinfengfulizi2);
                    //手动模式
                    isAuto = false;
                    img_auto.setImageResource(R.mipmap.xinfengzidong2);
                    img_hand.setImageResource(R.mipmap.xinfenghand2);
                    //风速1档
                    windSpeed = 1;
                    img_current_wind.setImageResource(R.mipmap.xinfengwind1);
                    currenttime = valueAnimator.getAnimatedFraction();
                    valueAnimator.setDuration(32000);
                    valueAnimator.setCurrentPlayTime((long) (32000 * currenttime));

                    //自然模式的字体颜色
                    setModelTvColor();
                    natural_tv.setTextColor(getResources().getColor(R.color.xinfeng));
                    //显示自然模式
                    current_model_tv.setText("节能模式");
                    img_model.setImageResource(R.mipmap.xinfengmoshi1);
                    //关闭灯光的下面有判断
                }
                //高效模式(自动关 负离子关  风速4档)
                if (MainActivity.b[15] == 0x02 && MainActivity.b[17] == 0x02 && MainActivity.b[20] == 0x04) {
                    //自动关
                    isAuto = false;
                    img_auto.setImageResource(R.mipmap.xinfengzidong2);
                    //负离子关
                    isIon = false;
                    img_ion.setImageResource(R.mipmap.xinfengfulizi2);
                    //风速4档
                    windSpeed = 4;
                    img_current_wind.setImageResource(R.mipmap.xinfengwind4);
                    currenttime = valueAnimator.getAnimatedFraction();
                    valueAnimator.setDuration(8000);
                    valueAnimator.setCurrentPlayTime((long) (8000 * currenttime));
                    //高效模式的字体颜色
                    setModelTvColor();
                    eff_tv.setTextColor(getResources().getColor(R.color.xinfeng));
                    //显示高效模式
                    current_model_tv.setText("高效模式");
                    img_model.setImageResource(R.mipmap.xinfengmoshi1);

                }
                //舒适模式（自动关  负离子开  风速2档）
                if (MainActivity.b[15] == 0x02 && MainActivity.b[17] == 0x01 && MainActivity.b[20] == 0x02) {
                    //自动关
                    isAuto = false;
                    img_auto.setImageResource(R.mipmap.xinfengzidong2);
                    //负离子开
                    isIon = true;
                    img_ion.setImageResource(R.mipmap.xinfengfulizi1);
                    //风速2档
                    windSpeed = 2;
                    img_current_wind.setImageResource(R.mipmap.xinfengwind2);
                    currenttime = valueAnimator.getAnimatedFraction();
                    valueAnimator.setDuration(24000);
                    valueAnimator.setCurrentPlayTime((long) (24000 * currenttime));
                    //舒适模式的字体颜色
                    setModelTvColor();
                    com_tv.setTextColor(getResources().getColor(R.color.xinfeng));
                    //显示舒适模式
                    current_model_tv.setText("舒适模式");
                    img_model.setImageResource(R.mipmap.xinfengmoshi1);
                }
//                //设置定时
//                if (MainActivity.b[23] == setHour && MainActivity.b[24] == setMin) {
//                    //定时开
//                    isSetTime = true;
//                    img_time.setImageResource(R.mipmap.xinfengdingshion);
//                    //当前的时间加上定时时间
//                    Calendar c = Calendar.getInstance();
//                    int hour = c.get(Calendar.HOUR_OF_DAY);
//                    int min = c.get(Calendar.MINUTE);
//                    int last_h = 0;
//                    int last_m = 0;
//                    if ((min + minute) >= 60) {
//                        last_m = min + minute - 60;
//                        last_h = hour + hourOfDay + 1;
//
//                    } else {
//                        last_m = min + minute;
//                        last_h = hour + hourOfDay;
//                    }
//                    if (last_h >= 24) {
//                        last_h = last_h - 24;
//                        time_tv.setText("本次净化任务将于明天" + last_h + "点" + last_m + "分结束");
//                    } else {
//                        time_tv.setText("本次净化任务将于" + last_h + "点" + last_m + "分结束");
//                    }
//                }
                if (MainActivity.b[23] == 0x00 && MainActivity.b[24] == 0x00) {
                    //定时关
                    isSetTime = false;
                    img_time.setImageResource(R.mipmap.xinfengdingshioff);
                    time_tv.setText("暂未设置");
                } else {
                    //定时开
                    isSetTime = true;
                    img_time.setImageResource(R.mipmap.xinfengdingshion);
                    int hour = 0, mins = 0;
                    if (MainActivity.b[23] != 0x00) {
                        //定时的小时（byte->10进制）
                        hour = MainActivity.b[23] & 0xFF;
                    }
                    if (MainActivity.b[24] != 0x00) {
                        //定时的分钟
                        mins = MainActivity.b[24] & 0xFF;
                    }
                    time_tv.setText("本次净化任务将于" + hour + "时" + mins + "分后结束");
                }
                //温度
                if (MainActivity.b[18] != 0x00) {
                    temp_tv.setText(String.valueOf(MainActivity.b[18] & 0xFF));
                }
                //湿度
                if (MainActivity.b[19] != 0x00) {
                    humi_tv.setText(String.valueOf(MainActivity.b[19] & 0xFF));
                }
                //甲醛
                if (MainActivity.b[25] != 0x00) {
                    form_tv.setText(String.valueOf(MainActivity.b[25] & 0xFF));
                }
                //滤芯剩余
                if (MainActivity.b[26] != 0x00 || MainActivity.b[27] != 0x00) {
//                    filter_tv.setText(String.valueOf((MainActivity.b[26] & 0xFF) * 100 + (MainActivity.b[27] & 0xFF)) + "小时");
//                    高低位硬件发反了 计算结果：低位*10+高位
                    filter_tv.setText(String.valueOf((MainActivity.b[27] & 0xFF) * 10 + (MainActivity.b[26] & 0xFF)) + "小时");

                }
                //led彩灯
                if (MainActivity.b[16] == 0x09) {
                    //LED彩灯为红
                    changeLedState(9);

                } else if (MainActivity.b[16] == 0x0a) {
                    //LED彩灯为绿
                    changeLedState(10);
                } else if (MainActivity.b[16] == 0x0b) {
                    //LED彩灯为蓝
                    changeLedState(11);
                } else if (MainActivity.b[16] == 0x0c) {
                    //LED彩灯为黄
                    changeLedState(12);

                } else if (MainActivity.b[16] == 0x0d) {
                    //LED彩灯为青
                    changeLedState(13);
                } else if (MainActivity.b[16] == 0x0e) {
                    //LED彩灯为紫
                    changeLedState(14);
                } else if (MainActivity.b[16] == 0x0f) {
                    //LED彩灯为白
                    changeLedState(15);
                } else if (MainActivity.b[16] == 0x06) {
                    //LED彩灯为红绿蓝
                    changeLedState(6);
                } else if (MainActivity.b[16] == 0x07) {
                    //LED彩灯为黄青紫
                    changeLedState(7);
                } else if (MainActivity.b[16] == 0x08) {
                    //LED彩灯为七色转动
                    changeLedState(8);
                } else if (MainActivity.b[16] == 0x03) {
                    //LED彩灯为七色渐变
                    changeLedState(3);
                } else if (MainActivity.b[16] == 0x04) {
                    //LED彩灯为幻彩转动
                    changeLedState(4);
                } else if (MainActivity.b[16] == 0x05) {
                    //LED彩灯为七色呼吸
                    changeLedState(5);
                } else if (MainActivity.b[16] == 0x10) {
                    //LED彩灯为暂停
                    changeLedState(16);
                } else if (MainActivity.b[16] == 0x02) {
                    //LED彩灯为自动
                    changeLedState(2);
                } else if (MainActivity.b[16] == 0x01) {
                    //LED彩灯为关闭
                    changeLedState(1);
                }



            }
        }
    };
    private ImageView img_led_close;//关闭

    //修改设备名称
    private TextView tv_old_name;
    private EditText edt_new_name;
    private Button btn_cancle;
    private Button btn_sure;
    private Dialog modifyNameDialog;
    private boolean isModifyName = false;
    public static final int RESULT_CODE_XINFENG_MODIFY_NAME = 1000;
    public static final String RESULT_XINFENG_MODIFY_NAME_KEY = "isModify";
    public static final String RESULT_XINFENG_NEW_NAME_KEY = "newName";


    @Override
    public int addContentView() {
        return R.layout.activity_xfair;
    }


    @Override
    public void initView() {
        root_layout = (FrameLayout) findViewById(R.id.xinfeng_root);
        llayout_back = (LinearLayout) findViewById(R.id.llayout_xinfeng_back);
        tv_device_name = (TextView) findViewById(R.id.xinfeng_device_name);
//        img_more = (ImageView) findViewById(R.id.img_xinfeng_title_more);
        tv_more = (TextView) findViewById(R.id.tv_xinfeng_title_more);
        scroll_layout = (ScrollView) findViewById(R.id.scroll_xinfeng);
        llayout_module1 = (LinearLayout) findViewById(R.id.llayout_xinfeng_module1);
        img_rotate = (ImageView) findViewById(R.id.img_xinfeng_rotate);
        pm_tv = (TextView) findViewById(R.id.txt_xinfeng_pm);
        filter_tv = (TextView) findViewById(R.id.txt_xinfeng_filter);
        img_current_wind = (ImageView) findViewById(R.id.img_xinfeng_current_wind);
        current_model_tv = (TextView) findViewById(R.id.txt_xinfeng_selected_model);
        temp_tv = (TextView) findViewById(R.id.txt_xinfeng_temp);
        humi_tv = (TextView) findViewById(R.id.txt_xinfeng_humi);
        co2_tv = (TextView) findViewById(R.id.txt_xinfeng_co2);
        form_tv = (TextView) findViewById(R.id.txt_xinfeng_form);

        llayout_auto = (LinearLayout) findViewById(R.id.llayout_xinfeng_auto);
        img_auto = (ImageView) findViewById(R.id.img_xinfeng_auto);
        llayout_hand = (LinearLayout) findViewById(R.id.llayout_xinfeng_hand);
        img_hand = (ImageView) findViewById(R.id.img_xinfeng_hand);
        llayout_LED = (LinearLayout) findViewById(R.id.llayout_xinfeng_LED);
        img_LED = (ImageView) findViewById(R.id.img_xinfeng_LED);
        llayout_ion = (LinearLayout) findViewById(R.id.llayout_xinfeng_ion);
        img_ion = (ImageView) findViewById(R.id.img_xinfeng_ion);
        llayout_wind = (LinearLayout) findViewById(R.id.llayout_xinfeng_wind);
        llayout_model = (LinearLayout) findViewById(R.id.llayout_xinfeng_model);
        img_model = (ImageView) findViewById(R.id.img_xinfeng_model);

        llayout_time_task = (LinearLayout) findViewById(R.id.llayout_xinfeng_time_task);
        time_tv = (TextView) findViewById(R.id.txt_time_task);
        img_time = (ImageView) findViewById(R.id.img_xinfeng_time_task_switch);
        llayout_history = (LinearLayout) findViewById(R.id.llayout_xinfeng_history);

        layout_bg_layer = (LinearLayout) findViewById(R.id.llayout_bg_layer);
        layout_btn_layer = (LinearLayout) findViewById(R.id.llayout_btn_layer);

        llayout_switch = (LinearLayout) findViewById(R.id.llayout_xinfeng_switch);
        img_switch = (ImageView) findViewById(R.id.img_xinfeng_switch);
        switch_tv = (TextView) findViewById(R.id.tv_xinfeng_switch);
    }

    //查询当前状态
    public void queryState() {
        new HcNetWorkTask(this, this, 1).doPost(UrlConstant.QUERY_XIN_AIR_STATE, null, postParams(1).getBytes());
    }

    //查询设备历史记录
    public void queryHistoryRecord() {
        new HcNetWorkTask(this, this, 2).doPost(UrlConstant.QUERY_XIN_AIR_HISTORY, null, postParams(2).getBytes());
    }


    //初始化popUpWindow
    public void initMorePopUpWindow() {
        final View more_view = LayoutInflater.from(this).inflate(
                R.layout.popwindow_xinfeng_more, null);
        pm25_tv = (TextView) more_view.findViewById(R.id.window_xinfeng_more_pm25_tv);
        modify_name_tv = (TextView) more_view.findViewById(R.id.window_xinfeng_more_modify_name_tv);
        remove_tv = (TextView) more_view.findViewById(R.id.window_xinfeng_more_remove_tv);
        shuoming_tv = (TextView) more_view.findViewById(R.id.window_xinfeng_more_shuoming_tv);
        help_tv = (TextView) more_view.findViewById(R.id.window_xinfeng_more_help_tv);
        contact_tv = (TextView) more_view.findViewById(R.id.window_xinfeng_more_contact_tv);
        cancle_tv = (TextView) more_view.findViewById(R.id.window_xinfeng_more_cancle_tv);
        pm25_tv.setOnClickListener(this);
        modify_name_tv.setOnClickListener(this);
        remove_tv.setOnClickListener(this);
        shuoming_tv.setOnClickListener(this);
        help_tv.setOnClickListener(this);
        contact_tv.setOnClickListener(this);
        cancle_tv.setOnClickListener(this);
        //注意最后一个参数：true：点击手机自带的返回键，window消失   false：点击返回键，直接返回整个Activity
        moreWindow = new PopupWindow(more_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        // 设置弹出窗体的背景半透明（注意：要实现背景半透明，不仅需要设置窗体背景，还需要popwindow的布局位置为底部）
        moreWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        moreWindow.setAnimationStyle(R.style.xinfeng_more_anim);
        more_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = more_view.findViewById(R.id.xinfeng_more_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        moreWindow.dismiss();
                    }
                }
                return true;
            }
        });
    }


    //初始化modelPopWindow
    public void initModelPopUpWindow() {
        final View model_view = LayoutInflater.from(this).inflate(
                R.layout.popwindow_xinfeng_model, null);
        sleep_tv = (TextView) model_view.findViewById(R.id.window_xinfeng_model_sleep);
        natural_tv = (TextView) model_view.findViewById(R.id.window_xinfeng_model_natural);
        eff_tv = (TextView) model_view.findViewById(R.id.window_xinfeng_model_eff);
        com_tv = (TextView) model_view.findViewById(R.id.window_xinfeng_model_com);
        model_cancle_tv = (TextView) model_view.findViewById(R.id.window_xinfeng_model_cancle);

        sleep_tv.setOnClickListener(this);
        natural_tv.setOnClickListener(this);
        eff_tv.setOnClickListener(this);
        com_tv.setOnClickListener(this);
        model_cancle_tv.setOnClickListener(this);

        //注意最后一个参数：true：点击手机自带的返回键，window消失   false：点击返回键，直接返回整个Activity
        modelWindow = new PopupWindow(model_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        modelWindow.setAnimationStyle(R.style.xinfeng_more_anim);
        // 设置弹出窗体的背景半透明（注意：要实现背景半透明，不仅需要设置窗体背景，还需要popwindow的布局位置为底部）
        modelWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        //下面的操作是：点击外部区域，window消失
        model_view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = model_view.findViewById(R.id.xinfeng_model_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        modelWindow.dismiss();
                    }
                }
                return true;
            }
        });

    }

    //绘制历史记录图表
    public void historyChart() {
        XYSeries series = new XYSeries("历史使用时间", 0);

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
            params.put("devTypeSn", deviceTypeSn);
            params.put("devSn", deviceSn);
            if (LogTools.debug) {
                LogTools.i("查询当前状态参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        } else if (code == 2) {
            //查询历史记录
            params.put("devTypeSn", deviceTypeSn);
            params.put("devSn", deviceSn);
            params.put("days", String.valueOf(7));
            if (LogTools.debug) {
                LogTools.i("查询历史记录参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        } else if (code == 3) {
            //删除设备
            params.put("id", deviceID);
            if (LogTools.debug) {
                LogTools.i("删除设备参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        } else if (code == 4) {
            //修改设备名称
            params.put("ud.devTypeSn", deviceTypeSn);
            params.put("ud.devSn", deviceSn);
            params.put("ud.definedName", edt_new_name.getText().toString().trim());

            if (LogTools.debug) {
                LogTools.i("修改设备名称参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        }
        return "";
    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        //设置PM25的字体
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/LockClock.ttf");
//        pm_tv.setTypeface(face);
        //设置半透明图层的touch监听，目的是当它显示时，后面布局中的控件不能获取点击事件
        layout_bg_layer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (layout_bg_layer.getVisibility() == View.VISIBLE) {
                    return true;
                }
                return false;
            }
        });
        //设置6个按钮上面的透明图层的touch监听，目的是当它显示时，后面的6个按钮不能获取点击事件
        layout_btn_layer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (layout_btn_layer.getVisibility() == View.VISIBLE) {
                    return true;
                }
                return false;
            }
        });
        //获取屏幕的高度（像素）
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        //设置模块1的高度
        LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) llayout_module1.getLayoutParams();
        mLayoutParams.height = screenHeight - DensityUtil.getStatusBarHeight(this) - DensityUtil.dip2px(this, 50.6f);
        llayout_module1.setLayoutParams(mLayoutParams);

        if (getIntent().getExtras() != null) {
            deviceID = getIntent().getStringExtra(PARAM_DEVICE_ID);
            deviceTypeSn = getIntent().getStringExtra(PARAM_DEVICE_TYPE_SN);
            deviceSn = getIntent().getStringExtra(PARAM_DEVICE_SN);
            userSn = getIntent().getStringExtra(PARAM_USER_SN);
            deviceName = getIntent().getStringExtra(PARAM_DEVICE_NAME);
            tv_device_name.setText(deviceName);
            queryState();
            queryHistoryRecord();
            //刚一进来就发送一条指令（传时间）
            ((OZApplication) getApplication()).SendOrder(SocketOrderTools.xinFengTime(deviceTypeSn, deviceSn, SystemTools.getSystemTime()));
        }
        //初始化morepopUpWindow
        initMorePopUpWindow();
        //初始化modelPopUpWindow
        initModelPopUpWindow();
        //绘制历史记录图表
        //初始化旋转动画
        initRotateAnimation();

        //设置点击事件
        setClick();
        initLEDDialog();
        //刚进来从本地读取pm2.5状态
        isPm = getSharedPreferences(deviceSn, Context.MODE_PRIVATE).getBoolean(SpConstant.STATE_PM, true);
        if (!isPm) {
            pm_tv.setText("PM2.5");
            pm25_tv.setText("立即检测");
            pm_tv.setTextSize(40);
        }

    }

    public void setClick() {
        llayout_back.setOnClickListener(this);
//        img_more.setOnClickListener(this);
        tv_more.setOnClickListener(this);
        llayout_auto.setOnClickListener(this);
        llayout_hand.setOnClickListener(this);
        llayout_LED.setOnClickListener(this);
        llayout_ion.setOnClickListener(this);
        llayout_wind.setOnClickListener(this);
        llayout_model.setOnClickListener(this);
        img_time.setOnClickListener(this);
        llayout_time_task.setOnClickListener(this);

        llayout_switch.setOnClickListener(this);

        pm_tv.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_xinfeng_back:
                //返回
                onBackPressed();
                break;
            case R.id.tv_xinfeng_title_more:
                //更多
                moreWindow.showAtLocation(root_layout, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.llayout_xinfeng_hand:
                //点击手动按钮
            case R.id.llayout_xinfeng_auto:
                //点击“自动”按钮
                if (isAuto) {
                    //关闭，先通信，然后根据服务器->App指令改变按钮状态
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 15, (byte) 0x02, SystemTools.getSystemTime()));
                    //最后测试的时候要把下面的代码注释
//                    isAuto = false;
//                    img_auto.setImageResource(R.mipmap.xinfengzidong2);
                } else {
                    //开启
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 15, (byte) 0x01, SystemTools.getSystemTime()));
//                    isAuto = true;
//                    img_auto.setImageResource(R.mipmap.xinfengzidong1);

                }
                break;
            case R.id.llayout_xinfeng_LED:
                //LED彩灯
//                LEDDialog = new AlertDialog.Builder(this).create();
                LEDDialog.show();
                LEDDialog.getWindow().setContentView(LEDlayout);
                break;
            case R.id.llayout_xinfeng_ion:
                //点击“负离子”按钮
                if (isIon) {
                    //关闭
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 17, (byte) 0x02, SystemTools.getSystemTime()));
                    //最后测试的时候要把下面的代码注释
//                    isIon = false;
//                    img_ion.setImageResource(R.mipmap.xinfengfulizi2);
                } else {
                    //开启
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 17, (byte) 0x01, SystemTools.getSystemTime()));
//                    isIon = true;
//                    img_ion.setImageResource(R.mipmap.xinfengfulizi1);

                }
                break;
            case R.id.llayout_xinfeng_wind:
                //点击“风速”按钮
                if (windSpeed == 1) {
                    //风速设为2级
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 20, (byte) 0x02, SystemTools.getSystemTime()));
                    //测试的时候，下面的代码注释
//                    windSpeed = 2;
//                    img_current_wind.setImageResource(R.mipmap.xinfengwind2);
//                    currenttime = valueAnimator.getAnimatedFraction();
//                    valueAnimator.setDuration(24000);
//                    valueAnimator.setCurrentPlayTime((long) (24000 * currenttime));


                } else if (windSpeed == 2) {
                    //风速设为3级
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 20, (byte) 0x03, SystemTools.getSystemTime()));
//                    windSpeed = 3;
//                    img_current_wind.setImageResource(R.mipmap.xinfengwind3);
//                    currenttime = valueAnimator.getAnimatedFraction();
//                    valueAnimator.setDuration(16000);
//                    valueAnimator.setCurrentPlayTime((long) (16000 * currenttime));

                } else if (windSpeed == 3) {
                    //风速设为4级
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 20, (byte) 0x04, SystemTools.getSystemTime()));
//                    windSpeed = 4;
//                    img_current_wind.setImageResource(R.mipmap.xinfengwind4);
//                    currenttime = valueAnimator.getAnimatedFraction();
//                    valueAnimator.setDuration(8000);
//                    valueAnimator.setCurrentPlayTime((long) (8000 * currenttime));

                } else if (windSpeed == 4) {
                    //风速设为1级
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 20, (byte) 0x01, SystemTools.getSystemTime()));
//                    windSpeed = 1;
//                    img_current_wind.setImageResource(R.mipmap.xinfengwind1);
//                    currenttime = valueAnimator.getAnimatedFraction();
//                    valueAnimator.setDuration(32000);
//                    valueAnimator.setCurrentPlayTime((long) (32000 * currenttime));
                }
                break;
            case R.id.llayout_xinfeng_model:
                //模式
                modelWindow.showAtLocation(root_layout, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.window_xinfeng_more_pm25_tv:
                if (isSwitch) {
                    if (isPm) {
                        //不再检测，锁定发1
                        ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 31, (byte) 0x01, SystemTools.getSystemTime()));
                        //pm2.5检测关闭
                        isPm = false;
                        pm_tv.setText("PM2.5");
                        pm_tv.setTextSize(40);
                        pm25_tv.setText("立即检测");
                    } else {
                        ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 31, (byte) 0x02, SystemTools.getSystemTime()));
//                        ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 14, (byte) 0x01, SystemTools.getSystemTime()));
//                        ((OZApplication) getApplication()).SendOrder(SocketOrderTools.pm25OpenOrder(deviceTypeSn, deviceSn, SystemTools.getSystemTime()));
                        //pm2.5在检测
                        isPm = true;
                        pm25_tv.setText("关闭pm2.5检测");
                    }
                    moreWindow.dismiss();
                } else {
                    //设备处于关闭状态
                    ToastTools.show(this, "设备处于关闭状态，不可进行操作");
                }
                break;
            case R.id.window_xinfeng_more_modify_name_tv:
                //修改名称
                moreWindow.dismiss();
                showModifyNameDialog();
                break;
            case R.id.window_xinfeng_more_remove_tv:
                //移除设备
//                ToastTools.show(this, "移除设备");
                moreWindow.dismiss();
                //弹出是否移除的dialog
                showDeleteDeviceDialog();
                break;
            case R.id.window_xinfeng_more_shuoming_tv:
                //产品说明
                moreWindow.dismiss();
                Intent desIntent = new Intent(this, MachineProductDesActivity.class);
                desIntent.putExtra(MachineProductDesActivity.PARAM_BIG_TYPE_SN, "4200");
                desIntent.putExtra(MachineProductDesActivity.PARAM_TYPE_NUMBER, "4232A");
                startActivity(desIntent);
                break;
            case R.id.window_xinfeng_more_help_tv:
                //使用帮助
                ToastTools.show(this, "使用帮助");
                break;
            case R.id.window_xinfeng_more_contact_tv:
                //联系我们
                moreWindow.dismiss();
                Uri uri = Uri.parse("tel:4009909918");
                startActivity(new Intent(Intent.ACTION_DIAL, uri));
                break;
            case R.id.window_xinfeng_more_cancle_tv:
                //取消
                moreWindow.dismiss();
                break;
            case R.id.window_xinfeng_model_sleep:
                //睡眠，发送“风速1档 自动关  负离子关”的指令（负离子改为开）
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.xinfengSleepModelOrder(deviceTypeSn, deviceSn, SystemTools.getSystemTime()));
                modelWindow.dismiss();
                //测试的时候，把下面的代码去掉，要根据服务->App的数据改变页面效果
//                风速1档
//                windSpeed = 1;
//                img_current_wind.setImageResource(R.mipmap.xinfengwind1);
//                currenttime = valueAnimator.getAnimatedFraction();
//                valueAnimator.setDuration(32000);
//                valueAnimator.setCurrentPlayTime((long) (32000 * currenttime));
//                //自动关
//                isAuto = false;
//                img_auto.setImageResource(R.mipmap.xinfengzidong2);
//                //负离子关
//                isIon = false;
//                img_ion.setImageResource(R.mipmap.xinfengfulizi2);
//                //睡眠模式的字体颜色
//                setModelTvColor();
//                sleep_tv.setTextColor(getResources().getColor(R.color.xinfeng));
//                //显示睡眠模式
//                current_model_tv.setText("睡眠模式");
//                img_model.setImageResource(R.mipmap.xinfengmoshi1);
                break;
            case R.id.window_xinfeng_model_natural:
                //由原来的自然改为节能，发送“负离子开，自动开 灯光关闭”的指令
                //再次修改为：“负离子关 灯光关闭 风速为1”
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.xinfengNaturalModelOrder(deviceTypeSn, deviceSn, SystemTools.getSystemTime()));
                modelWindow.dismiss();
                //测试的时候，把下面的代码去掉，要根据服务->App的数据改变页面效果
//                isIon = true;
//                img_ion.setImageResource(R.mipmap.xinfengfulizi1);
//                isAuto = true;
//                img_auto.setImageResource(R.mipmap.xinfengzidong1);
//                //自然模式的字体颜色
//                setModelTvColor();
//                natural_tv.setTextColor(getResources().getColor(R.color.xinfeng));
//                //显示自然模式
//                current_model_tv.setText("自然模式");
//                img_model.setImageResource(R.mipmap.xinfengmoshi1);
                break;
            case R.id.window_xinfeng_model_eff:
                //高效，发送“风速4档 自动关  负离子关”的指令
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.xinfengEffModelOrder(deviceTypeSn, deviceSn, SystemTools.getSystemTime()));
                modelWindow.dismiss();
                //测试的时候，把下面的代码去掉，要根据服务->App的数据改变页面效果
                //自动关
//                isAuto = false;
//                img_auto.setImageResource(R.mipmap.xinfengzidong2);
//                //负离子关
//                isIon = false;
//                img_ion.setImageResource(R.mipmap.xinfengfulizi2);
//                //风速4档
//                windSpeed = 4;
//                img_current_wind.setImageResource(R.mipmap.xinfengwind4);
//                currenttime = valueAnimator.getAnimatedFraction();
//                valueAnimator.setDuration(8000);
//                valueAnimator.setCurrentPlayTime((long) (8000 * currenttime));
//                //高效模式的字体颜色
//                setModelTvColor();
//                eff_tv.setTextColor(getResources().getColor(R.color.xinfeng));
//                //显示高效模式
//                current_model_tv.setText("高效模式");
//                img_model.setImageResource(R.mipmap.xinfengmoshi1);
                break;
            case R.id.window_xinfeng_model_com:
                //舒适模式，发送“自动关  负离子开  风速2档”的指令
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.xinfengComModelOrder(deviceTypeSn, deviceSn, SystemTools.getSystemTime()));
                modelWindow.dismiss();
                //测试的时候，把下面的代码去掉，要根据服务->App的数据改变页面效果
                //自动关
//                isAuto = false;
//                img_auto.setImageResource(R.mipmap.xinfengzidong2);
//                //负离子开
//                isIon = true;
//                img_ion.setImageResource(R.mipmap.xinfengfulizi1);
//                //风速2档
//                windSpeed = 2;
//                img_current_wind.setImageResource(R.mipmap.xinfengwind2);
//                currenttime = valueAnimator.getAnimatedFraction();
//                valueAnimator.setDuration(24000);
//                valueAnimator.setCurrentPlayTime((long) (24000 * currenttime));
//                //舒适模式的字体颜色
//                setModelTvColor();
//                com_tv.setTextColor(getResources().getColor(R.color.xinfeng));
//                //显示舒适模式
//                current_model_tv.setText("舒适模式");
//                img_model.setImageResource(R.mipmap.xinfengmoshi1);
                break;
            case R.id.window_xinfeng_model_cancle:
                //模式的取消
                modelWindow.dismiss();
                break;
            case R.id.img_xinfeng_time_task_switch:
                //定时开关
                if (isSetTime) {
                    //关闭定时b[23]=(byte)0x00  b[24]=(byte)0x00
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 23, (byte) 0x00, SystemTools.getSystemTime()));
                    //测试的时候去掉
//                    isSetTime = false;
//                    img_time.setImageResource(R.mipmap.xinfengdingshioff);
//                    time_tv.setText("暂未设置");

                } else {
                    //开启定时
                    showTimeDialog();
                }
                break;
            case R.id.llayout_xinfeng_time_task:
                //定时布局（弹出dialog）
//                showTimeDialog();
                //现在要跳转到另一个页面中
                Intent intent = new Intent(this, SetTimeActivity.class);
                intent.putExtra(SetTimeActivity.PARAM_DEV_SN, deviceSn);
                intent.putExtra(SetTimeActivity.PARAM_DEV_TYPE_SN, deviceTypeSn);
                startActivity(intent);

                break;
            case R.id.llayout_xinfeng_switch:
                //点击“开关”按钮
                if (isSwitch) {
                    //关闭
                    /**
                     * 1)发送"关闭"指令
                     * HMFFAttffffffw0x020x000x00....(一共23个0x00)#
                     */
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 14, (byte) 0x02, SystemTools.getSystemTime()));
                } else {
                    //发送开启指令
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 14, (byte) 0x01, SystemTools.getSystemTime()));
                }
                break;
            case R.id.txt_xinfeng_pm:
                //PM25
                if (isPm) {
                    //不要检测，开启锁定
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 31, (byte) 0x01, SystemTools.getSystemTime()));
                    //pm2.5检测关闭
                    isPm = false;
                    pm_tv.setText("PM2.5");
                    pm_tv.setTextSize(40);
                    pm25_tv.setText("立即检测");

                } else {
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 31, (byte) 0x02, SystemTools.getSystemTime()));
//                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 14, (byte) 0x01, SystemTools.getSystemTime()));
//                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.pm25OpenOrder(deviceTypeSn, deviceSn, SystemTools.getSystemTime()));
                    //pm2.5在检测
                    isPm = true;
                    pm25_tv.setText("关闭pm2.5检测");
                    //显示的值要根据服务器返回的指令来显示
                }
                break;
        }

    }

    //弹出彩灯Dialog
    public void initLEDDialog() {
        LEDDialog = new AlertDialog.Builder(this).create();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LEDlayout = (RelativeLayout) layoutInflater.inflate(
                R.layout.dialog_led2, null);
        //自动模式
        btn_led_auto = (Button) LEDlayout.findViewById(R.id.btn_led_auto);
        //暂停
        btn_led_pause = (Button) LEDlayout.findViewById(R.id.btn_led_pause);
        //关闭
        btn_led_close = (Button) LEDlayout.findViewById(R.id.btn_led_close);
        //红
        font_led_red = (TextView) LEDlayout.findViewById(R.id.font_led_red);
        //绿
        font_led_green = (TextView) LEDlayout.findViewById(R.id.font_led_green);
        //蓝
        font_led_blue = (TextView) LEDlayout.findViewById(R.id.font_led_blue);
        //黄
        font_led_yellow = (TextView) LEDlayout.findViewById(R.id.font_led_yellow);
        //青
        font_led_qing = (TextView) LEDlayout.findViewById(R.id.font_led_qing);
        //紫
        font_led_zi = (TextView) LEDlayout.findViewById(R.id.font_led_zi);
        //白
        font_led_white = (TextView) LEDlayout.findViewById(R.id.font_led_white);
        //红绿蓝
        font_led_red_green_blue = (TextView) LEDlayout.findViewById(R.id.font_led_red_green_blue);
        //黄青紫
        font_led_yellow_qing_zi = (TextView) LEDlayout.findViewById(R.id.font_led_yellow_qing_zi);
        //七色转动
        font_led_seven_run = (TextView) LEDlayout.findViewById(R.id.font_led_seven_run);
        //七彩渐变
        font_led_seven_shadow = (TextView) LEDlayout.findViewById(R.id.font_led_seven_shadow);
        //幻彩转动
        font_led_run = (TextView) LEDlayout.findViewById(R.id.font_led_run);
        //七色呼吸
        font_led_seven_breathe = (TextView) LEDlayout.findViewById(R.id.font_led_seven_breathe);
        img_led_close = (ImageView) LEDlayout.findViewById(R.id.img_xinfeng_led_close);
        //设置字体图标
        Typeface typeface = IconfontTools.getTypeface(XFAirActivity.this);
        font_led_red.setTypeface(typeface);
        font_led_green.setTypeface(typeface);
        font_led_blue.setTypeface(typeface);
        font_led_yellow.setTypeface(typeface);
        font_led_qing.setTypeface(typeface);
        font_led_zi.setTypeface(typeface);
        font_led_white.setTypeface(typeface);
        font_led_red_green_blue.setTypeface(typeface);
        font_led_yellow_qing_zi.setTypeface(typeface);
        font_led_seven_run.setTypeface(typeface);
        font_led_seven_shadow.setTypeface(typeface);
        font_led_run.setTypeface(typeface);
        font_led_seven_breathe.setTypeface(typeface);
        //设置点击事件
        //自动模式
        btn_led_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发2
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(2), SystemTools.getSystemTime()));

//                changeLedState(7);

            }
        });
        //暂停
        btn_led_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发16
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(16), SystemTools.getSystemTime()));

//                changeLedState(8);

            }
        });
        //关闭
        btn_led_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发1
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(1), SystemTools.getSystemTime()));

//                changeLedState(8);

            }
        });

        //红
        font_led_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点红，发9

                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(9), SystemTools.getSystemTime()));

//                changeLedState(0);


            }
        });

        //绿
        font_led_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发a
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(10), SystemTools.getSystemTime()));
//                changeLedState(1);

            }
        });
        //蓝
        font_led_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发b
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(11), SystemTools.getSystemTime()));
//                changeLedState(1);

            }
        });
        //黄
        font_led_yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发c
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(12), SystemTools.getSystemTime()));
//                changeLedState(1);

            }
        });
        //青
        font_led_qing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发d
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(13), SystemTools.getSystemTime()));
//                changeLedState(1);

            }
        });
        //紫
        font_led_zi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发e
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(14), SystemTools.getSystemTime()));
//                changeLedState(1);

            }
        });
        //白
        font_led_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发f
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(15), SystemTools.getSystemTime()));
//                changeLedState(1);

            }
        });
        //红绿蓝
        font_led_red_green_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点红绿蓝，发6
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(6), SystemTools.getSystemTime()));
//                changeLedState(2);

            }
        });

        //黄青紫
        font_led_yellow_qing_zi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点黄青紫，发7
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(7), SystemTools.getSystemTime()));

            }
        });
        //七色转动
        font_led_seven_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发8
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(8), SystemTools.getSystemTime()));
//                changeLedState(4);

            }
        });

        //七色渐变
        font_led_seven_shadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发3
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(3), SystemTools.getSystemTime()));

//                changeLedState(5);

            }
        });

        //幻彩转动
        font_led_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发4
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(4), SystemTools.getSystemTime()));

//                changeLedState(6);

            }
        });
        //七色呼吸
        font_led_seven_breathe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发5
                ((OZApplication) getApplication()).SendOrder(SocketOrderTools.appToServiceOrder(deviceTypeSn, deviceSn, 16, ledIntToByte(5), SystemTools.getSystemTime()));

//                changeLedState(6);

            }
        });

        img_led_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LEDDialog.dismiss();
            }
        });


    }

    //弹出修改名称的dialog
    public void showModifyNameDialog() {
        modifyNameDialog = new AlertDialog.Builder(this).create();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        RelativeLayout modifyNameLayout = (RelativeLayout) layoutInflater.inflate(
                R.layout.dialog_xinfeng_modify_name, null);
        modifyNameDialog.show();
        //加上下面这样一行代码：dialog中的edittext就可以弹出输入法了
        modifyNameDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        modifyNameDialog.getWindow().setContentView(modifyNameLayout);
        tv_old_name = (TextView) modifyNameLayout.findViewById(R.id.xinfeng_old_name);
        edt_new_name = (EditText) modifyNameLayout.findViewById(R.id.edt_xinfeng_new_name);
        btn_cancle = (Button) modifyNameLayout.findViewById(R.id.btn_xinfeng_modify_name_cancle);
        btn_sure = (Button) modifyNameLayout.findViewById(R.id.btn_xinfeng_modify_name_sure);
        if (deviceName != null) {
            tv_old_name.setText(deviceName);
        }
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                modifyNameDialog.dismiss();
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确认
                if (edt_new_name.getText().toString().trim().isEmpty()) {
                    ToastTools.show(XFAirActivity.this, "请输入新的设备名称");
                } else {
                    new HcNetWorkTask(XFAirActivity.this, XFAirActivity.this, 4).doPost(UrlConstant.MODIFY_DEVICE_NAME, null, postParams(4).getBytes());
                }

            }
        });

    }

    public byte ledIntToByte(int number) {
        String ledNumber16 = Integer.toHexString(number);
        if (ledNumber16.length() < 2) {
            ledNumber16 = "0" + ledNumber16;
        }
        byte order = (byte) Integer.parseInt(ledNumber16.substring(0, 2), 16);
        return order;
    }

    public void changeLedState(int i) {
        btn_led_auto.setSelected(false);
        btn_led_pause.setSelected(false);
        btn_led_close.setSelected(false);
        font_led_red.setSelected(false);
        font_led_green.setSelected(false);
        font_led_blue.setSelected(false);
        font_led_yellow.setSelected(false);
        font_led_qing.setSelected(false);
        font_led_zi.setSelected(false);
        font_led_white.setSelected(false);
        font_led_red_green_blue.setSelected(false);
        font_led_yellow_qing_zi.setSelected(false);
        font_led_seven_run.setSelected(false);
        font_led_seven_shadow.setSelected(false);
        font_led_run.setSelected(false);
        font_led_seven_breathe.setSelected(false);
        font_led_red.setText(getString(R.string.time_set_switch_off));
        font_led_green.setText(getString(R.string.time_set_switch_off));
        font_led_blue.setText(getString(R.string.time_set_switch_off));
        font_led_yellow.setText(getString(R.string.time_set_switch_off));
        font_led_qing.setText(getString(R.string.time_set_switch_off));
        font_led_zi.setText(getString(R.string.time_set_switch_off));
        font_led_white.setText(getString(R.string.time_set_switch_off));
        font_led_red_green_blue.setText(getString(R.string.time_set_switch_off));
        font_led_yellow_qing_zi.setText(getString(R.string.time_set_switch_off));
        font_led_seven_run.setText(getString(R.string.time_set_switch_off));
        font_led_seven_shadow.setText(getString(R.string.time_set_switch_off));
        font_led_run.setText(getString(R.string.time_set_switch_off));
        font_led_seven_breathe.setText(getString(R.string.time_set_switch_off));

        if (i == 9) {
            //红
            font_led_red.setSelected(true);
            font_led_red.setText(getString(R.string.time_set_switch_on));
        } else if (i == 10) {
            //绿
            font_led_green.setSelected(true);
            font_led_green.setText(getString(R.string.time_set_switch_on));
        } else if (i == 11) {
            //蓝
            font_led_blue.setSelected(true);
            font_led_blue.setText(getString(R.string.time_set_switch_on));
        } else if (i == 12) {
            //黄
            font_led_yellow.setSelected(true);
            font_led_yellow.setText(getString(R.string.time_set_switch_on));
        } else if (i == 13) {
            //青
            font_led_qing.setSelected(true);
            font_led_qing.setText(getString(R.string.time_set_switch_on));
        } else if (i == 14) {
            //紫
            font_led_zi.setSelected(true);
            font_led_zi.setText(getString(R.string.time_set_switch_on));
        } else if (i == 15) {
            //白
            font_led_white.setSelected(true);
            font_led_white.setText(getString(R.string.time_set_switch_on));
        } else if (i == 6) {
            //红绿蓝
            font_led_red_green_blue.setSelected(true);
            font_led_red_green_blue.setText(getString(R.string.time_set_switch_on));
        } else if (i == 7) {
            //黄青紫
            font_led_yellow_qing_zi.setSelected(true);
            font_led_yellow_qing_zi.setText(getString(R.string.time_set_switch_on));
        } else if (i == 8) {
            //七色转动
            font_led_seven_run.setSelected(true);
            font_led_seven_run.setText(getString(R.string.time_set_switch_on));
        } else if (i == 3) {
            //七色渐变
            font_led_seven_shadow.setSelected(true);
            font_led_seven_shadow.setText(getString(R.string.time_set_switch_on));
        } else if (i == 4) {
            //幻彩转动
            font_led_run.setSelected(true);
            font_led_run.setText(getString(R.string.time_set_switch_on));
        } else if (i == 5) {
            //七色呼吸
            font_led_seven_breathe.setSelected(true);
            font_led_seven_breathe.setText(getString(R.string.time_set_switch_on));
        } else if (i == 16) {
            //暂停
            btn_led_pause.setSelected(true);
        } else if (i == 2) {
            //自动模式
            btn_led_auto.setSelected(true);
        } else if (i == 1) {
            //关闭
            btn_led_close.setSelected(true);
        }


    }

    //弹出选择时间的dialog
    public void showTimeDialog() {
        final TimePickerDialog time = new TimePickerDialog(XFAirActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                ToastTools.show(XFAirActivity.this, hourOfDay + "小时" + minute + "分钟");
                if (hourOfDay == 0 && minute == 0) {
                    //设置的时间为0小时0分钟
                } else {
                    //App->服务发送指令
                    String hour16 = Integer.toHexString(hourOfDay);
                    if (hour16.length() < 2) {
                        hour16 = "0" + hour16;
                    }
                    setHour = (byte) Integer.parseInt(hour16.substring(0, 2), 16);

                    String min16 = Integer.toHexString(minute);
                    if (min16.length() < 2) {
                        min16 = "0" + min16;
                    }
                    setMin = (byte) Integer.parseInt(min16.substring(0, 2), 16);
                    ((OZApplication) getApplication()).SendOrder(SocketOrderTools.xinfengSetTimeOrder(deviceTypeSn, deviceSn, setHour, setMin, SystemTools.getSystemTime()));
                    //测试的时候，把下面的代码去掉
//                    isSetTime = true;
//                    img_time.setImageResource(R.mipmap.xinfengdingshion);
//                    //当前的时间加上定时时间
//                    Calendar c = Calendar.getInstance();
//                    int hour = c.get(Calendar.HOUR_OF_DAY);
//                    int min = c.get(Calendar.MINUTE);
//                    int last_h = 0;
//                    int last_m = 0;
//                    if ((min + minute) >= 60) {
//                        last_m = min + minute - 60;
//                        last_h = hour + hourOfDay + 1;
//
//                    } else {
//                        last_m = min + minute;
//                        last_h = hour + hourOfDay;
//                    }
//                    if (last_h >= 24) {
//                        last_h = last_h - 24;
//                        time_tv.setText("本次净化任务将于明天" + last_h + "点" + last_m + "分结束");
//                    } else {
//                        time_tv.setText("本次净化任务将于" + last_h + "点" + last_m + "分结束");
//                    }


                }
            }
        }, 2, 10, true);
        time.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("broadcass1");
        registerReceiver(serviceToAppReveiver, filter);
    }

    @Override
    public void onResult(String result, int code) {
        if (!result.isEmpty()) {
            try {
                JSONObject object = new JSONObject(result);
                if (code == 1) {
                    //当前状态
                    if (object.getInt("state") == 0) {
                        JSONObject data = object.getJSONObject("data");
                        //PM25
                        if (isPm) {
                            pm_tv.setText(data.getString("sPm25"));
                        }
//                        //滤芯剩余百分比
//                        DecimalFormat df = new DecimalFormat("#.##");
//                        Double d1 = Double.parseDouble(df.format(data.getInt("changeFilterScreen") / 2700.0));
//                        filter_tv.setText("滤芯剩余" + (int) (d1 * 100) + "%");
                        //现在显示剩余小时
                        filter_tv.setText(String.valueOf(data.getInt("sChangeFilterScreen")) + "小时");
                        //温度
                        temp_tv.setText(data.getString("sCurrentC"));
                        //湿度
                        humi_tv.setText(data.getString("sCurrentH"));
//                        co2_tv.setText(data.getString("co2"));
                        //甲醛
                        form_tv.setText(data.getString("sMethanal"));
                        if (data.getInt("fMode") == 1) {
                            //自动
                            isAuto = true;

                        } else if (data.getInt("fMode") == 2) {
                            //手工
                            isAuto = false;
                        }

                        if (isAuto) {
                            //自动按钮开启
                            img_auto.setImageResource(R.mipmap.xinfengzidong1);
                            img_hand.setImageResource(R.mipmap.xinfenghand1);
                        } else {
                            //自动按钮关闭
                            img_hand.setImageResource(R.mipmap.xinfenghand2);
                            img_auto.setImageResource(R.mipmap.xinfengzidong2);
                        }
                        //负离子
                        if (data.getString("fAnion").equals("null")) {
                        } else {
                            isIon = data.getBoolean("fAnion");
                        }
                        if (isIon) {
                            //开启
                            img_ion.setImageResource(R.mipmap.xinfengfulizi1);
                        } else {
                            //关闭
                            img_ion.setImageResource(R.mipmap.xinfengfulizi2);
                        }
                        //风速
                        if (data.getString("fWind").equals("null")) {

                        } else {
                            windSpeed = data.getInt("fWind");
                        }
                        if (windSpeed <= 1) {
                            img_current_wind.setImageResource(R.mipmap.xinfengwind1);
                            //根据风速展示旋转动画(1级风转1圈需要30s)
                            valueAnimator.setDuration(32000);
                        } else if (windSpeed == 2) {
                            img_current_wind.setImageResource(R.mipmap.xinfengwind2);
                            valueAnimator.setDuration(24000);
                        } else if (windSpeed == 3) {
                            img_current_wind.setImageResource(R.mipmap.xinfengwind3);
                            valueAnimator.setDuration(16000);
                        } else if (windSpeed == 4) {
                            img_current_wind.setImageResource(R.mipmap.xinfengwind4);
                            valueAnimator.setDuration(8000);
                        }

                        isSwitch = data.getBoolean("fSwitch");
                        if (isSwitch) {
                            //开
                            llayout_switch.setBackgroundResource(R.drawable.shape_xinfeng_switch_on_bg);
                            img_switch.setImageResource(R.mipmap.xinfeng_switch_off);
                            switch_tv.setText(getString(R.string.xinfeng_switch_on_txt));
                            switch_tv.setTextColor(getResources().getColor(R.color.white));
                            //开始旋转动画
                            if (valueAnimator.isRunning()) {

                            } else {
                                valueAnimator.start();
                            }


                        } else {
                            //关
                            llayout_switch.setBackgroundResource(R.drawable.shape_xinfeng_switch_off_bg);
                            img_switch.setImageResource(R.mipmap.xinfeng_switch_on);
                            switch_tv.setText(getString(R.string.xinfeng_switch_off_txt));
                            switch_tv.setTextColor(getResources().getColor(R.color.xinfeng));
                            if (valueAnimator.isRunning()) {
                                valueAnimator.cancel();
                            }
                            layout_bg_layer.setVisibility(View.VISIBLE);
//                            layout_btn_layer.setVisibility(View.VISIBLE);


                        }


                        //定时时间
                        if (data.getString("durTime").equals("null")) {
                            //没有定时
                            isSetTime = false;
                            img_time.setImageResource(R.mipmap.xinfengdingshioff);
                            time_tv.setText("暂未设置");
                        } else {
                            isSetTime = true;
                            img_time.setImageResource(R.mipmap.xinfengdingshion);
                            int minsecs = data.getInt("durTime");
                            time_tv.setText("本次净化任务将于" + TimeFormatTools.getTimeFromMillisecond((long) minsecs) + "后结束");
                        }
                        led = data.getInt("fLight");
                        if (led == 1) {
                            //关闭
                            changeLedState(1);
                        } else if (led == 2) {
                            //自动模式
                            changeLedState(2);
                        } else if (led == 3) {
                            //七彩渐变
                            changeLedState(3);
                        } else if (led == 4) {
                            //幻彩转动
                            changeLedState(4);
                        } else if (led == 5) {
                            //七色呼吸
                            changeLedState(5);
                        } else if (led == 6) {
                            //红绿蓝
                            changeLedState(6);
                        } else if (led == 7) {
                            //黄青紫
                            changeLedState(7);
                        } else if (led == 8) {
                            //七色转动
                            changeLedState(8);
                        } else if (led == 9) {
                            //红
                            changeLedState(9);
                        } else if (led == 10) {
                            //绿
                            changeLedState(10);
                        } else if (led == 11) {
                            //蓝
                            changeLedState(11);
                        } else if (led == 12) {
                            //黄
                            changeLedState(12);
                        } else if (led == 13) {
                            //青
                            changeLedState(13);
                        } else if (led == 14) {
                            //紫
                            changeLedState(14);
                        } else if (led == 15) {
                            //白
                            changeLedState(15);
                        }


                    } else if (object.getInt("state") == 1) {
                        ToastTools.show(this, "参数异常");
                    } else if (object.getInt("state") == 2) {
                        ToastTools.show(this, "没有数据");
                        //现在是测试，有数据的时候把下面的去掉
//                        windSpeed = 1;
//                        valueAnimator.setDuration(32000);
//                        isSwitch = true;
//                        if (isSwitch) {
//                            //开
//                            llayout_switch.setBackgroundResource(R.drawable.shape_xinfeng_switch_on_bg);
//                            img_switch.setImageResource(R.mipmap.xinfeng_switch_off);
//                            switch_tv.setText(getString(R.string.xinfeng_switch_off_txt));
//                            switch_tv.setTextColor(getResources().getColor(R.color.white));
//                            valueAnimator.start();
//                        } else {
//                            layout_bg_layer.setVisibility(View.VISIBLE);
//                            llayout_switch.setBackgroundResource(R.drawable.shape_xinfeng_switch_off_bg);
//                            img_switch.setImageResource(R.mipmap.xinfeng_switch_on);
//                            switch_tv.setText(getString(R.string.xinfeng_switch_on_txt));
//                            switch_tv.setTextColor(getResources().getColor(R.color.xinfeng));
//                        }


                    }
                } else if (code == 2) {
                    XYMultipleSeriesDataset seriesDataset = new XYMultipleSeriesDataset();
                    XYSeries xySeries2 = new XYSeries("历史使用时间");
                    if (object.getInt("state") == 1 || object.getInt("state") == 2) {
                        xySeries2.add(1, 0);


                    } else {
                        for (int i = 0; i < object.getJSONArray("data").length(); i++) {
                            xySeries2.add(i + 1, object.getJSONArray("data").getJSONObject(i).getLong("useTime") / 1000 / 3600);
                        }
                    }
                    seriesDataset.addSeries(xySeries2);

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
                    //颜色设置
                    seriesRenderer.setApplyBackgroundColor(true);//是应用设置的背景颜色
                    seriesRenderer.setLabelsColor(0xFF85848D);//设置标签颜色
                    seriesRenderer.setBackgroundColor(Color.WHITE);//设置图表的背景颜色
                    //缩放设置
                    seriesRenderer.setZoomButtonsVisible(false);//设置缩放按钮是否可见
                    seriesRenderer.setZoomEnabled(false); //图表是否可以缩放设置
                    seriesRenderer.setZoomInLimitX(7);
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
                    if (object.getInt("state") == 1 || object.getInt("state") == 2) {
                        seriesRenderer.addXTextLabel(1, "暂无数据");
                    } else {
                        for (int i = 0; i < object.getJSONArray("data").length(); i++) {
                            seriesRenderer.addXTextLabel(i + 1, object.getJSONArray("data").getJSONObject(i).getString("useDate").substring(5, 10));
                        }
                    }
                    seriesRenderer.setPointSize(6);//设置坐标点大小
                    seriesRenderer.setMarginsColor(Color.WHITE);//设置外边距空间的颜色
                    seriesRenderer.setClickEnabled(false);

                   /*某一组数据的描绘器，描绘该组数据的个性化显示效果，主要是字体跟颜色的效果*/
                    XYSeriesRenderer xySeriesRenderer2 = new XYSeriesRenderer();
                    xySeriesRenderer2.setPointStyle(PointStyle.CIRCLE);//坐标点的显示风格
                    xySeriesRenderer2.setPointStrokeWidth(8);//坐标点的大小
                    xySeriesRenderer2.setColor(0xFF049eff);//表示该组数据的图或线的颜色
                    xySeriesRenderer2.setDisplayChartValues(false);//设置是否显示坐标点的y轴坐标值
                    xySeriesRenderer2.setChartValuesTextSize(12);//设置显示的坐标点值的字体大小
                    xySeriesRenderer2.setFillPoints(true);
                    xySeriesRenderer2.setLineWidth(3);
                    seriesRenderer.addSeriesRenderer(xySeriesRenderer2);
                    llayout_history.addView(ChartFactory.getLineChartView(this, seriesDataset, seriesRenderer));
                } else if (code == 3) {
                    //删除设备
//                    state：0 - 删除成功
//                    state：1 - 参数异常
//                    state：2 - 删除失败
                    if (object.getInt("state") == 0) {
                        //删除成功
                        setResult(RESULT_OK);
                        OZApplication.getInstance().finishActivity();
                    } else if (object.getInt("state") == 1) {
                        ToastTools.show(XFAirActivity.this, "参数异常");
                    } else if (object.getInt("state") == 2) {
                        ToastTools.show(XFAirActivity.this, "删除失败");
                    }

                } else if (code == 4) {
                    //修改设备名称
                    if (object.getInt("state") == 0) {
                        //修改成功
                        //1 dialog隐藏
                        modifyNameDialog.dismiss();
                        //2标题处的设备名称
                        tv_device_name.setText(edt_new_name.getText().toString().trim());
                        //设备列表中的名称也要修改
                        isModifyName = true;
                    } else if (object.getInt("state") == 1) {
                        //参数异常
                        isModifyName = false;
                        ToastTools.show(this, "参数异常");
                    } else if (object.getInt("state") == 2) {
                        //修改失败
                        isModifyName = false;
                        ToastTools.show(this, "修改失败");

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
                ToastTools.show(this, "解析异常");
            }


        }

    }

    public void setModelTvColor() {
        sleep_tv.setTextColor(getResources().getColor(R.color.xinfeng_more_txt_color));
        natural_tv.setTextColor(getResources().getColor(R.color.xinfeng_more_txt_color));
        eff_tv.setTextColor(getResources().getColor(R.color.xinfeng_more_txt_color));
        com_tv.setTextColor(getResources().getColor(R.color.xinfeng_more_txt_color));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceToAppReveiver);
    }

    public void initRotateAnimation() {
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        valueAnimator = ObjectAnimator.ofFloat(img_rotate, "rotation", 0, 360);
        valueAnimator.setInterpolator(linearInterpolator);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    public void showDeleteDeviceDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(
                R.layout.dialog_delete_device, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        Button btn_cancle = (Button) layout.findViewById(R.id.delete_device_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                dialog.cancel();
            }
        });
        Button btn_sure = (Button) layout.findViewById(R.id.delete_device_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                new HcNetWorkTask(XFAirActivity.this, XFAirActivity.this, 3).doPost(UrlConstant.DELETE_USER_DEVICE, null, postParams(3).getBytes());
            }
        });

    }

    @Override
    public void onBackPressed() {
        //先把pm2.5的状态存下来
        getSharedPreferences(deviceSn, Context.MODE_PRIVATE).edit().putBoolean(SpConstant.STATE_PM, isPm).commit();
        if (isModifyName) {
            Intent resultData = new Intent();
            resultData.putExtra(RESULT_XINFENG_MODIFY_NAME_KEY, isModifyName);
            resultData.putExtra(RESULT_XINFENG_NEW_NAME_KEY, edt_new_name.getText().toString().trim());
            setResult(RESULT_CODE_XINFENG_MODIFY_NAME, resultData);
        }
        OZApplication.getInstance().finishActivity();

    }
}
