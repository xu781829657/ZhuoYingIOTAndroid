package com.ouzhongiot.ozapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.http.ConnectDataTask;
import com.ouzhongiot.ozapp.http.HcNetWorkTask;
import com.ouzhongiot.ozapp.http.PostParamTools;
import com.ouzhongiot.ozapp.tools.IconfontTools;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.ToastTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

/**
 * @author hxf
 * @date 创建时间: 2017/2/21
 * @Description 设置定时页
 */

public class SetTimeActivity extends BaseHomeActivity implements View.OnClickListener, ConnectDataTask.OnResultDataLintener {
    private TextView font_back;
    private TextView tv_title;
    private TextView tv_head_right;
    private TextView tv_open_time;//开启时间
    private TextView font_open_switch;//开启时间按钮
    private TextView tv_close_time;//关闭时间
    private TextView font_close_switch;//关闭时间按钮
    private TextView font_repeat_switch;//是否重复开关
    private Button btn_commit;


    private Calendar calendar;
    private StringBuilder openTime;//开启时间
    private StringBuilder closeTime;//关闭时间

    private boolean openSwitch = false;
    private boolean closeSwitch = false;
    private boolean repeatSwitch = false;

    public static final String PARAM_DEV_SN = "devSn";//设备编号
    public static final String PARAM_DEV_TYPE_SN = "devTypeSn";//设备类型编号

    private String devSn;//设备编号
    private String devTypeSn;//设备类型编号


    @Override
    public int addContentView() {
        return R.layout.activity_time_set;
    }

    @Override
    public void initView() {
        font_back = (TextView) findViewById(R.id.font_head_blue_back);
        tv_title = (TextView) findViewById(R.id.txt_head_blue_content);
        tv_head_right = (TextView) findViewById(R.id.txt_head_blue_right);
        tv_open_time = (TextView) findViewById(R.id.time_set_open_time);
        font_open_switch = (TextView) findViewById(R.id.time_set_open_switch);
        tv_close_time = (TextView) findViewById(R.id.time_set_close_time);
        font_close_switch = (TextView) findViewById(R.id.time_set_close_switch);
        font_repeat_switch = (TextView) findViewById(R.id.time_set_repeat_switch);
        btn_commit = (Button) findViewById(R.id.time_set_commit);


    }

    @Override
    public void initValue() {
        if (getIntent().getExtras() != null) {
            devSn = getIntent().getStringExtra(PARAM_DEV_SN);
            devTypeSn = getIntent().getStringExtra(PARAM_DEV_TYPE_SN);
            //查询定时任务
            new HcNetWorkTask(this, this, 2).doPost(UrlConstant.QYERY_XIN_AIR_TIME, null, postParams(2).getBytes());
        }
        setTypeface();
        tv_title.setText(R.string.time_set_title);
        tv_head_right.setVisibility(View.GONE);

        calendar = Calendar.getInstance();
        //设置点击事件
        setClick();


    }

    public void setTypeface() {
        font_back.setTypeface(IconfontTools.getTypeface(this));
        font_open_switch.setTypeface(IconfontTools.getTypeface(this));
        font_close_switch.setTypeface(IconfontTools.getTypeface(this));
        font_repeat_switch.setTypeface(IconfontTools.getTypeface(this));
    }

    public void setClick() {
        font_back.setOnClickListener(this);
        tv_open_time.setOnClickListener(this);
        tv_close_time.setOnClickListener(this);
        font_open_switch.setOnClickListener(this);
        font_close_switch.setOnClickListener(this);
        font_repeat_switch.setOnClickListener(this);
        btn_commit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.font_head_blue_back:
                //返回
                finish();
                break;
            case R.id.time_set_open_time:
                //开启时间
                showTimeDialog("open");
                break;
            case R.id.time_set_close_time:
                //关闭时间
                showTimeDialog("close");
                break;
            case R.id.time_set_open_switch:
                //开启时间的按钮
                if (openSwitch) {
                    //关闭
                    font_open_switch.setText(R.string.time_set_switch_off);
                    font_open_switch.setTextColor(getResources().getColor(R.color.nine_txt_color));
                    openSwitch = false;
                } else {
                    font_open_switch.setText(R.string.time_set_switch_on);
                    font_open_switch.setTextColor(getResources().getColor(R.color.cold_fan_blue));
                    openSwitch = true;
                }
                break;
            case R.id.time_set_close_switch:
                //关闭时间的按钮
                if (closeSwitch) {
                    //关闭
                    font_close_switch.setText(R.string.time_set_switch_off);
                    font_close_switch.setTextColor(getResources().getColor(R.color.nine_txt_color));
                    closeSwitch = false;
                } else {
                    font_close_switch.setText(R.string.time_set_switch_on);
                    font_close_switch.setTextColor(getResources().getColor(R.color.cold_fan_blue));
                    closeSwitch = true;
                }
                break;
            case R.id.time_set_repeat_switch:
                //是否重复的按钮
                if (repeatSwitch) {
                    //关闭
                    font_repeat_switch.setText(R.string.time_set_switch_off);
                    font_repeat_switch.setTextColor(getResources().getColor(R.color.nine_txt_color));
                    repeatSwitch = false;
                } else {
                    font_repeat_switch.setText(R.string.time_set_switch_on);
                    font_repeat_switch.setTextColor(getResources().getColor(R.color.cold_fan_blue));
                    repeatSwitch = true;
                }
                break;
            case R.id.time_set_commit:
                //提交
                if (devSn == null) {
                    ToastTools.show(this, "devSn为空");
                } else {
                    new HcNetWorkTask(this, this, 1).doPost(UrlConstant.SET_XIN_AIR_TIME, null, postParams(1).getBytes());
                }
                break;
        }

    }

    private String postParams(int code) {
        HashMap<String, String> params = new HashMap<>();
        if (code == 1) {
            //提交
            //只要开关按钮是开着的，jobTimeOn(开启时间)  jobTimeOff(关闭时间)
            //如果是关着的，两个参数不传
            //还有如果两个开关按钮都是关闭的，这个时候重复按钮参数也不传了（两个按钮只要有一个是开着的，重复按钮就按照用户选择的来传）
            params.put("devTypeSn", devTypeSn);
            params.put("devSn", devSn);
            if ((!openSwitch) && (!closeSwitch)) {
                //说明  开关按钮都是关着的，这个时候重复参数也不传
            } else {
                if (openSwitch) {
                    params.put("task.jobTimeOn", tv_open_time.getText().toString().trim());
                }
                if (closeSwitch) {
                    params.put("task.jobTimeOff", tv_close_time.getText().toString().trim());
                }
                if (repeatSwitch) {
                    //下面两个参数传的内容一样
                    params.put("task.runWeekOn", "1111111");
                    params.put("task.runWeekOff", "1111111");


                } else {
                    params.put("task.runWeekOn", "0000000");
                    params.put("task.runWeekOff", "0000000");

                }
            }

            if (LogTools.debug) {
                LogTools.i("设置定时参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        } else if (code == 2) {
            //查询定时任务
            params.put("devSn", devSn);
            params.put("devTypeSn", devTypeSn);

            if (LogTools.debug) {
                LogTools.i("查询定时任务参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        }
        return "";
    }

    public void showTimeDialog(final String tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_time_dialog, null);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        calendar.setTimeInMillis(System.currentTimeMillis());
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        builder.setView(view);
        builder.setTitle("设置开启时间");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确认
                if (tag.equals("open")) {
                    openTime = new StringBuilder();
                    int hour = timePicker.getCurrentHour();
                    if (hour < 10) {
                        openTime.append("0");
                    }
                    openTime.append(hour + ":");
                    int mins = timePicker.getCurrentMinute();
                    if (mins < 10) {
                        openTime.append("0");
                    }
                    openTime.append(mins);
                    tv_open_time.setText(openTime.toString());
                } else if (tag.equals("close")) {
                    //关闭时间
                    closeTime = new StringBuilder();
                    int hour = timePicker.getCurrentHour();
                    if (hour < 10) {
                        closeTime.append("0");
                    }
                    closeTime.append(hour + ":");
                    int mins = timePicker.getCurrentMinute();
                    if (mins < 10) {
                        closeTime.append("0");
                    }
                    closeTime.append(mins);
                    tv_close_time.setText(closeTime.toString());
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void onResult(String result, int code) {
        if (!result.isEmpty()) {
            try {
                JSONObject object = new JSONObject(result);
                String state = object.getString("state");
                if (code == 1) {
                    if (LogTools.debug) {
                        LogTools.i("定时返回数据->" + result);
                    }
                    if (state.equals("0")) {
                        //设置成功
                        finish();
                    } else if (state.equals("1")) {
                        ToastTools.show(this, "参数异常");
                    } else if (state.equals("2")) {
                        ToastTools.show(this, "获取数据失败");
                    }
                } else if (code == 2) {
                    //查询定时任务
                    if (LogTools.debug) {
                        LogTools.i("查询定时任务返回数据->" + result);
                    }
                    if (state.equals("0")) {
                        //成功
                        JSONObject dataObject = object.getJSONObject("data");
                        if (dataObject.getString("jobTimeOn").equals("null")) {
                            //开启按钮关闭，然后时间是我的默认值
                            openSwitch = false;
                            font_open_switch.setText(R.string.time_set_switch_off);
                            font_open_switch.setTextColor(getResources().getColor(R.color.nine_txt_color));
                        } else {
                            //开启的时间
                            openSwitch = true;
                            tv_open_time.setText(dataObject.getString("jobTimeOn"));
                            font_open_switch.setText(R.string.time_set_switch_on);
                            font_open_switch.setTextColor(getResources().getColor(R.color.cold_fan_blue));
                        }

                        if (dataObject.getString("jobTimeOff").equals("null")) {
                            //关闭按钮关闭，然后时间是我的默认值
                            closeSwitch = false;
                            font_close_switch.setText(R.string.time_set_switch_off);
                            font_close_switch.setTextColor(getResources().getColor(R.color.nine_txt_color));
                        } else {
                            //关闭的时间
                            closeSwitch = true;
                            tv_close_time.setText(dataObject.getString("jobTimeOff"));
                            font_close_switch.setText(R.string.time_set_switch_on);
                            font_close_switch.setTextColor(getResources().getColor(R.color.cold_fan_blue));
                        }

                        if (dataObject.getString("runWeekOn").equals("0000000")){
                            //表示不重复
                            repeatSwitch = false;
                            font_repeat_switch.setText(R.string.time_set_switch_off);
                            font_repeat_switch.setTextColor(getResources().getColor(R.color.nine_txt_color));
                        }else if (dataObject.getString("runWeekOn").equals("1111111")){
                            //重复
                            repeatSwitch = true;
                            font_repeat_switch.setText(R.string.time_set_switch_on);
                            font_repeat_switch.setTextColor(getResources().getColor(R.color.cold_fan_blue));
                        }
                    }else if (state.equals("1")){
                        ToastTools.show(this, "参数异常");
                    }else if (state.equals("2")){
//                        ToastTools.show(this, "获取数据失败");
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
