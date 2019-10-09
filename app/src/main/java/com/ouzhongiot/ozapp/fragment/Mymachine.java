package com.ouzhongiot.ozapp.fragment;


import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ouzhongiot.ozapp.Model.MymachineData;
import com.ouzhongiot.ozapp.Model.logindata;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MainActivity;
import com.ouzhongiot.ozapp.activity.XFAirActivity;
import com.ouzhongiot.ozapp.activity.coldfanset;
import com.ouzhongiot.ozapp.airclean.AirCleanindex;
import com.ouzhongiot.ozapp.airclean.Aircleanstate;
import com.ouzhongiot.ozapp.dryer.DryerData;
import com.ouzhongiot.ozapp.dryer.DryerIndex;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.MyadapterCommon;
import com.ouzhongiot.ozapp.others.Post;
import com.ouzhongiot.ozapp.others.SetPackage;
import com.ouzhongiot.ozapp.tools.SocketOrderTools;
import com.ouzhongiot.ozapp.web.MachineController;
import com.ouzhongiot.ozapp.web.WebViewActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Mymachine extends Fragment implements AdapterView.OnItemClickListener {

    private OutputStream outputStream;

    private ListView lv_mymachine;

    private String loginstr, mymachinedata;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferencesmachine;
    private SharedPreferences.Editor editormachine;
    List<MymachineData.DataBean> userDeviceBeen;
    private String userid;
    private int machinelistnumber;
    private LinearLayout ll_zanwushebei, ll_mymachine_load;

    MyadapterCommon myadapterCommon;

    private ImageView iv_reg_req_code_gif_view, iv_mymachine_weather;
    private TextView tv_mymachine_temp, tv_mymachine_tempo, tv_mymachine_kongqi, tv_mymachine_shidu;

    private final int SKIP_REQUEST_CODE = 1000;
    private int delete_position = -1;

    public Mymachine() {
        // Required empty public constructor
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    ll_mymachine_load.setVisibility(View.GONE);
                    break;
                case 2:


                    break;
                case 3:
                    tv_mymachine_shidu.setText(preferences.getString("humidity", ""));
                    tv_mymachine_temp.setText(preferences.getString("temperature", ""));
                    tv_mymachine_kongqi.setText(preferences.getString("quality", ""));
                    if (preferences.getString("weatherinfo", "").contains("阴")) {
                        iv_mymachine_weather.setImageResource(R.mipmap.yingtian);
                    }
                    if (preferences.getString("weatherinfo", "").contains("云")) {
                        iv_mymachine_weather.setImageResource(R.mipmap.duoyun);
                    }
                    if (preferences.getString("weatherinfo", "").contains("晴")) {
                        iv_mymachine_weather.setImageResource(R.mipmap.sun);
                    }
                    if (preferences.getString("weatherinfo", "").contains("雨")) {
                        iv_mymachine_weather.setImageResource(R.mipmap.rain);
                    }
                    if (preferences.getString("weatherinfo", "").contains("雪")) {
                        iv_mymachine_weather.setImageResource(R.mipmap.snow);
                    }
                    if (preferences.getString("weatherinfo", "").contains("雾") || preferences.getString("weatherinfo", "").contains("霾")) {
                        iv_mymachine_weather.setImageResource(R.mipmap.snow);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.base_loading_large_anim);


        View view = inflater.inflate(R.layout.fragment_mymachine, container, false);
        view.setClickable(true);

        ll_mymachine_load = (LinearLayout) view.findViewById(R.id.ll_mymachine_load);
        iv_reg_req_code_gif_view = (ImageView) view.findViewById(R.id.iv_reg_req_code_gif_view_mymachine);
        iv_reg_req_code_gif_view.startAnimation(animation);
        ll_zanwushebei = (LinearLayout) view.findViewById(R.id.ll_zanwushebei);
        lv_mymachine = (ListView) view.findViewById(R.id.lv_mymachine);
        tv_mymachine_temp = (TextView) view.findViewById(R.id.tv_mymachine_temp);
        tv_mymachine_tempo = (TextView) view.findViewById(R.id.tv_mymachine_tempo);
        tv_mymachine_kongqi = (TextView) view.findViewById(R.id.tv_mymachine_kongqi);
        tv_mymachine_shidu = (TextView) view.findViewById(R.id.tv_mymachine_shidu);
        iv_mymachine_weather = (ImageView) view.findViewById(R.id.iv_mymachine_weather);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LockClock.ttf");
        tv_mymachine_temp.setTypeface(face);
        tv_mymachine_tempo.setTypeface(face);
        //请求我的设备列表
        preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        editor = preferences.edit();

        loginstr = preferences.getString("logindata", null);
        mymachinedata = preferences.getString("mymachinedata", null);

        userDeviceBeen = MymachineData.objectFromData(mymachinedata).getData();
        userid = logindata.objectFromData(loginstr).getData().getSn() + "";
        if (userDeviceBeen == null) {
//            Log.wtf("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~",""+ll_zanwushebei);
            ll_zanwushebei.setVisibility(View.VISIBLE);
            lv_mymachine.setVisibility(View.GONE);
        } else {

            lv_mymachine.setDivider(new ColorDrawable(getResources().getColor(R.color.gray)));
            lv_mymachine.setDividerHeight(-1);
            myadapterCommon = new MyadapterCommon(getActivity(), userDeviceBeen, lv_mymachine, (OZApplication) getActivity().getApplication());
            lv_mymachine.setAdapter(myadapterCommon);
            lv_mymachine.setOnItemClickListener(this);


        }


        //添加设备
        view.findViewById(R.id.ll_lijitianjia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), TypeList.class);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.activity_open, R.anim.activity_close_donothing);
                Intent intent = new Intent(getActivity(),coldfanset.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.tv_lijitianjia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), TypeList.class);
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), coldfanset.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_open, R.anim.activity_close_donothing);

//                Intent intent = new Intent();
//                intent.setClass(getActivity(),TypeList.class);//打开一个activity
//                startActivity(intent);

            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                String uriAPI = "http://op.juhe.cn/onebox/weather/query";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("cityname", preferences.getString("city", "杭州市").substring(0, preferences.getString("city", "杭州市").length() - 1)));
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
                String chuanyi = chuanyilist.get(1);

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

                editor.putString("humidity", humidity);
                editor.putString("weatherinfo", info);
                editor.putString("temperature", temperature);
                editor.putString("quality", quality);
                editor.putString("des", chuanyi);
                editor.putString("weathertime", timestr.substring(1, 3));
                editor.commit();

                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);


            }
        }).start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        outputStream = ((OZApplication) getActivity().getApplication()).outputStream;
        machinelistnumber = i;
//        dialog.show();
        switch (userDeviceBeen.get(i).getDevTypeSn()) {
//            case "4131":
//
//                ll_mymachine_load.setVisibility(View.VISIBLE);
//                preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
//                editor = preferences.edit();
//                editor.putString("workmachinetype", userDeviceBeen.get(i).getDevTypeSn());
//                editor.putString("workmachineid", userDeviceBeen.get(i).getDevSn());
//                editor.putString("UserDeviceID",userDeviceBeen.get(i).getId()+"");
//                editor.putString("IndexUrl",userDeviceBeen.get(i).getIndexUrl());
//                 editor.putString("workmachinetypestring", "A1");
//                editor.commit();
////                MainActivity.heartpakage = "HM*A*"+userDeviceBeen.get(machinelistnumber).getDevSn()+"*"+userid+"*#";
//                ((OZApplication) getActivity().getApplication()).setHeartbyte(SetPackage.GetHeartpackage(userid));
////                MainActivity.out.write(MainActivity.heartpakage);
////                MainActivity.out.flush();
//                try {
//                    Log.wtf("这个是加入指令", preferences.getString("workmachineid", "123456789111") + preferences.getString("userSn", "100000001") + preferences.getString("workmachinetypestring", "A1"));
//                    ((OZApplication) getActivity().getApplication()).outputStream.write(SetPackage.GetMachineConnected(preferences.getString("workmachineid", "123456789111"), preferences.getString("userSn", "100000001"), preferences.getString("workmachinetypestring", "A1")));
//                    ((OZApplication) getActivity().getApplication()).outputStream.flush();
//                    Log.wtf("这个是建立连接", "发送了！！！--------------------------");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    ((OZApplication) getActivity().getApplication()).outputStream.write(((OZApplication) getActivity().getApplication()).getHeartbyte());
//                    ((OZApplication) getActivity().getApplication()).outputStream.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                //请求当前状态
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String uriAPI = MainActivity.ip + "smarthome/fan/queryDeviceState";
//                        List<NameValuePair> params = new ArrayList<NameValuePair>();
//                        Log.wtf("这个是型号加编号", userDeviceBeen.get(machinelistnumber).getDevTypeSn() + userDeviceBeen.get(machinelistnumber).getDevSn());
//                        params.add(new BasicNameValuePair("devTypeSn", userDeviceBeen.get(machinelistnumber).getDevTypeSn()));
//                        params.add(new BasicNameValuePair("devSn", userDeviceBeen.get(machinelistnumber).getDevSn()));
//                        String str = Post.dopost(uriAPI, params);
//                        Log.wtf("这个是冷风扇当前状态", str);
//                        if ((Danqianzhuangtai.objectFromData(str).getState() + "").equals("0")) {
//                            fSwitch = Danqianzhuangtai.objectFromData(str).getData().isFSwitch();
//                            fMode = Danqianzhuangtai.objectFromData(str).getData().getFMode();
//                            fSwing = Danqianzhuangtai.objectFromData(str).getData().isFSwing();
//                            fWind = Danqianzhuangtai.objectFromData(str).getData().getFWind();
//                            fUV = Danqianzhuangtai.objectFromData(str).getData().isFUV();
//                            fState = Danqianzhuangtai.objectFromData(str).getData().isFState();
//                            preferencesmachine = getActivity().getSharedPreferences(userDeviceBeen.get(machinelistnumber).getDevSn(), Context.MODE_PRIVATE);
//                            editormachine = preferencesmachine.edit();
//                            editormachine.putBoolean("fSwitch", fSwitch);
//                            editormachine.putInt("fMode", fMode);
//                            editormachine.putInt("fWind", fWind);
//                            editormachine.putBoolean("fUV", fUV);
//                            editormachine.putBoolean("fSwing", fSwing);
//                            editormachine.putBoolean("fState", fState);
////                            editormachine.putString("heartpakage",MainActivity.heartpakage);
//                            editormachine.commit();
//                        }
//
//                        Intent intent = new Intent(getActivity(), MachineController.class);
//                        startActivity(intent);
//                        Message msg = new Message();
//                        msg.what = 1;
//                        handler.sendMessage(msg);
////                        dialog.cancel();
//                    }
//                }).start();
//
//
//                break;

            case "4231":
//                getActivity().getSharedPreferences("data", MODE_PRIVATE).getString("userSn","111111");
                ll_mymachine_load.setVisibility(View.VISIBLE);
                preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("workmachinetype", userDeviceBeen.get(i).getDevTypeSn());
                editor.putString("workmachineid", userDeviceBeen.get(i).getDevSn());
                editor.putString("UserDeviceID", userDeviceBeen.get(i).getId() + "");
                editor.putString("IndexUrl", userDeviceBeen.get(i).getIndexUrl());
                editor.putString("workmachinetypestring", "B1");
                editor.commit();
//                MainActivity.heartpakage = "HM*A*"+userDeviceBeen.get(machinelistnumber).getDevSn()+"*"+userid+"*#";
                ((OZApplication) getActivity().getApplication()).setHeartbyte(SetPackage.GetHeartpackage(userid));
                try {
                    Log.wtf("这个是建立连接", preferences.getString("workmachineid", "123456789111") + preferences.getString("userSn", "100000001") + preferences.getString("workmachinetypestring", "A1"));
                    ((OZApplication) getActivity().getApplication()).outputStream.write(SetPackage.GetMachineConnected(preferences.getString("workmachineid", "123456789111"), preferences.getString("userSn", "100000001"), preferences.getString("workmachinetypestring", "A1")));
                    ((OZApplication) getActivity().getApplication()).outputStream.flush();
                    Log.wtf("这个是建立连接", "发送了！！！--------------------------");
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                MainActivity.out.write(MainActivity.heartpakage);
//                MainActivity.out.flush();
                try {
                    ((OZApplication) getActivity().getApplication()).outputStream.write(((OZApplication) getActivity().getApplication()).getHeartbyte());
                    ((OZApplication) getActivity().getApplication()).outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //请求当前状态
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String uriAPI = MainActivity.ip + "smarthome/air/queryDeviceState";
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        Log.wtf("这个是型号加编号", userDeviceBeen.get(machinelistnumber).getDevTypeSn() + userDeviceBeen.get(machinelistnumber).getDevSn());
                        params.add(new BasicNameValuePair("devTypeSn", userDeviceBeen.get(machinelistnumber).getDevTypeSn()));
                        params.add(new BasicNameValuePair("devSn", userDeviceBeen.get(machinelistnumber).getDevSn()));
                        String str = Post.dopost(uriAPI, params);
                        if ((Aircleanstate.objectFromData(str).getState() + "").equals("0")) {
                            preferencesmachine = getActivity().getSharedPreferences(userDeviceBeen.get(machinelistnumber).getDevSn(), MODE_PRIVATE);
                            editormachine = preferencesmachine.edit();
                            editormachine.putBoolean("fSwitch", Aircleanstate.objectFromData(str).getData().isFSwitch());
                            editormachine.putBoolean("fAuto", Aircleanstate.objectFromData(str).getData().isFAuto());
                            editormachine.putBoolean("fSleep", Aircleanstate.objectFromData(str).getData().isFSleep());
                            editormachine.putBoolean("fUV", Aircleanstate.objectFromData(str).getData().isFUV());
                            editormachine.putBoolean("fAnion", Aircleanstate.objectFromData(str).getData().isFAnion());
                            editormachine.putInt("fWind", Aircleanstate.objectFromData(str).getData().getFWind());
                            editormachine.putString("sCurrentC", Aircleanstate.objectFromData(str).getData().getCurrentC());
                            editormachine.putString("sPm25", Aircleanstate.objectFromData(str).getData().getPm25() + "");
                            editormachine.putInt("sCleanFilterScreen", Aircleanstate.objectFromData(str).getData().getCleanFilterScreen());
                            editormachine.putInt("sChangeFilterScreen", Aircleanstate.objectFromData(str).getData().getChangeFilterScreen());
                            editormachine.putInt("fLight", Aircleanstate.objectFromData(str).getData().getLight());
                            editormachine.commit();
                        }

                        Intent intent = new Intent(getContext(), AirCleanindex.class);
                        startActivity(intent);
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
//                        dialog.cancel();
                    }
                }).start();
                break;
            case "4232":
                //新风空气净化器
                //发送“加入新设备”指令
                ((OZApplication) getActivity().getApplication()).SendOrder(SocketOrderTools.addDeviceOrder(preferences.getString("userSn", ""), "4232", userDeviceBeen.get(i).getDevSn()));
//                Intent intent = new Intent(getActivity(),XinfengIndex.class);
//                intent.putExtra(XinfengIndex.PARAM_DEVSN,"18fe34f56bcc");
//                intent.putExtra(XinfengIndex.PARAM_DEVTYPESN,"4232");
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), XFAirActivity.class);
                intent.putExtra(XFAirActivity.PARAM_DEVICE_ID, userDeviceBeen.get(i).getId());
                intent.putExtra(XFAirActivity.PARAM_DEVICE_SN, userDeviceBeen.get(i).getDevSn());
                intent.putExtra(XFAirActivity.PARAM_DEVICE_TYPE_SN, "4232");
                intent.putExtra(XFAirActivity.PARAM_USER_SN, preferences.getString("userSn", ""));
                if (userDeviceBeen.get(i).getDefinedName() == null) {
                    intent.putExtra(XFAirActivity.PARAM_DEVICE_NAME, userDeviceBeen.get(i).getBrand() + userDeviceBeen.get(i).getTypeName());

                } else {
                    intent.putExtra(XFAirActivity.PARAM_DEVICE_NAME, userDeviceBeen.get(i).getBrand() + userDeviceBeen.get(i).getDefinedName());

                }

//                startActivity(intent);
                delete_position = i;
                startActivityForResult(intent, SKIP_REQUEST_CODE);
                break;
            case "4331":
                ll_mymachine_load.setVisibility(View.VISIBLE);
                preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("workmachinetype", userDeviceBeen.get(i).getDevTypeSn());
                editor.putString("workmachineid", userDeviceBeen.get(i).getDevSn());
                editor.putString("UserDeviceID", userDeviceBeen.get(i).getId() + "");
                editor.putString("IndexUrl", userDeviceBeen.get(i).getIndexUrl());
                editor.putString("workmachinetypestring", "C1");
                editor.commit();
//                MainActivity.heartpakage = "HM*A*"+userDeviceBeen.get(machinelistnumber).getDevSn()+"*"+userid+"*#";
                ((OZApplication) getActivity().getApplication()).setHeartbyte(SetPackage.GetHeartpackage(userid));
                try {
                    Log.wtf("这个是建立连接", preferences.getString("workmachineid", "123456789111") + preferences.getString("userSn", "100000001") + preferences.getString("workmachinetypestring", "A1"));
                    ((OZApplication) getActivity().getApplication()).outputStream.write(SetPackage.GetMachineConnected(preferences.getString("workmachineid", "123456789111"), preferences.getString("userSn", "100000001"), preferences.getString("workmachinetypestring", "A1")));
                    ((OZApplication) getActivity().getApplication()).outputStream.flush();
                    Log.wtf("这个是建立连接", "发送了！！！--------------------------");
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                MainActivity.out.write(MainActivity.heartpakage);
//                MainActivity.out.flush();
                try {
                    ((OZApplication) getActivity().getApplication()).outputStream.write(((OZApplication) getActivity().getApplication()).getHeartbyte());
                    ((OZApplication) getActivity().getApplication()).outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //请求当前状态
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String uriAPI = MainActivity.ip + "smarthome/dryer/queryDeviceState";
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        Log.wtf("这个是型号加编号", userDeviceBeen.get(machinelistnumber).getDevTypeSn() + userDeviceBeen.get(machinelistnumber).getDevSn());
                        params.add(new BasicNameValuePair("devTypeSn", userDeviceBeen.get(machinelistnumber).getDevTypeSn()));
                        params.add(new BasicNameValuePair("devSn", userDeviceBeen.get(machinelistnumber).getDevSn()));
                        String str = Post.dopost(uriAPI, params);
                        if ((Aircleanstate.objectFromData(str).getState() + "").equals("0")) {
                            preferencesmachine = getActivity().getSharedPreferences(userDeviceBeen.get(machinelistnumber).getDevSn(), MODE_PRIVATE);
                            editormachine = preferencesmachine.edit();
                            editormachine.putBoolean("fSwitch", DryerData.objectFromData(str).getData().isFSwitch());
                            editormachine.putInt("fShift", DryerData.objectFromData(str).getData().getFShift());
                            editormachine.putBoolean("fUV", DryerData.objectFromData(str).getData().isFUV());
                            editormachine.putBoolean("fAnion", DryerData.objectFromData(str).getData().isFAnion());
                            editormachine.commit();
                        }

                        Intent intent = new Intent(getActivity(), DryerIndex.class);
                        startActivity(intent);
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
//                        dialog.cancel();
                    }
                }).start();
                break;
            case "4332":
                //C2干衣机
                //1）设置心跳包
                ((OZApplication) getActivity().getApplication()).setHeartbyte(SetPackage.GetHeartpackage(userid));
                //2）发送加入设备指令
                ((OZApplication) getActivity().getApplication()).SendOrder(SocketOrderTools.addDeviceOrder(preferences.getString("userSn", ""), userDeviceBeen.get(i).getDevTypeSn(), userDeviceBeen.get(i).getDevSn()));
                //3) 跳转activity
                Intent webViewIntent = new Intent(getActivity(), WebViewActivity.class);
                webViewIntent.putExtra(WebViewActivity.PARAM_USER_SN, preferences.getString("userSn", ""));
                webViewIntent.putExtra(WebViewActivity.PARAM_DEV_SN, userDeviceBeen.get(i).getDevSn());
                webViewIntent.putExtra(WebViewActivity.PARAM_DEV_TYPE_SN, userDeviceBeen.get(i).getDevTypeSn());
                webViewIntent.putExtra(WebViewActivity.PARAM_INDEX_URL, userDeviceBeen.get(i).getIndexUrl());
                webViewIntent.putExtra(WebViewActivity.PARAM_DEVICE_ID, userDeviceBeen.get(i).getId());
                webViewIntent.putExtra(WebViewActivity.PARAM_BRAND_NAME, userDeviceBeen.get(i).getBrand() + userDeviceBeen.get(i).getTypeName());
                startActivity(webViewIntent);
                break;
            default:
                ((OZApplication) getActivity().getApplication()).setHeartbyte(SetPackage.GetHeartpackage(userid));
                ((OZApplication) getActivity().getApplication()).SendOrder(SocketOrderTools.addDeviceOrder(preferences.getString("userSn", ""), userDeviceBeen.get(i).getDevTypeSn(), userDeviceBeen.get(i).getDevSn()));
                editor.putString("workmachinetype", userDeviceBeen.get(i).getDevTypeSn());
                editor.putString("workmachineid", userDeviceBeen.get(i).getDevSn());
                editor.putString("UserDeviceID", userDeviceBeen.get(i).getId() + "");
                editor.putString("IndexUrl", userDeviceBeen.get(i).getIndexUrl());
                editor.putString("BrandName", userDeviceBeen.get(i).getBrand() + userDeviceBeen.get(i).getTypeName());
                editor.commit();
                Intent intentdefault = new Intent(getActivity(), MachineController.class);
                startActivity(intentdefault);
                break;
        }

    }


//    /**
//     * 得到自定义的progressDialog
//     *
//     * @param context
//     * @param msg
//     * @return
//     */
//    public static Dialog createLoadingDialog(Context context, String msg) {
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
//
//        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
//        // 中的ImageView
//        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.dialog_img);
////        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
//        // 加载动画
//        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//                context, R.anim.loading_animation);
//        // 使用ImageView显示动画
//        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
////        tipTextView.setText(msg);// 设置加载信息
//
//        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
//
//        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
//        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
//        return loadingDialog;
//
//    }

    @Override
    public void onDestroy() {
        iv_reg_req_code_gif_view.clearAnimation();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SKIP_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                userDeviceBeen.remove(delete_position);
                myadapterCommon.notifyDataSetChanged();
                if (userDeviceBeen.size() == 0) {
                    ll_zanwushebei.setVisibility(View.VISIBLE);
                    lv_mymachine.setVisibility(View.GONE);
                } else {
                    //删除设备，重新请求列表
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String uriAPI2 = MainActivity.ip + "smarthome/userDevice/queryUserDevice";
                            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                            params2.add(new BasicNameValuePair("userSn", userid));
                            String str2 = Post.dopost(uriAPI2, params2);
                            Log.wtf("这个是请求我的设备", str2);

                            preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                            editor = preferences.edit();
                            editor.putString("mymachinedata", str2);
                            editor.commit();
                        /*userDeviceBeen = MymachineData.objectFromData(str2).getData();
                        if (userDeviceBeen == null) {

                            ll_zanwushebei.setVisibility(View.VISIBLE);
                            lv_mymachine.setVisibility(View.GONE);
                        } else {
                            myadapterCommon = new MyadapterCommon(getActivity(), userDeviceBeen, lv_mymachine, (OZApplication) getActivity().getApplication());
                            lv_mymachine.setAdapter(myadapterCommon);
                        }*/

                        }
                    }).start();
                }
            } else if (resultCode == XFAirActivity.RESULT_CODE_XINFENG_MODIFY_NAME) {
                if (data.getBooleanExtra(XFAirActivity.RESULT_XINFENG_MODIFY_NAME_KEY, false)) {
                    //修改了名称
                    userDeviceBeen.get(delete_position).setDefinedName(data.getStringExtra(XFAirActivity.RESULT_XINFENG_NEW_NAME_KEY));
                    myadapterCommon.notifyDataSetChanged();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String uriAPI2 = MainActivity.ip + "smarthome/userDevice/queryUserDevice";
                            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                            params2.add(new BasicNameValuePair("userSn", userid));
                            String str2 = Post.dopost(uriAPI2, params2);
                            Log.wtf("这个是请求我的设备", str2);

                            preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                            editor = preferences.edit();
                            editor.putString("mymachinedata", str2);
                            editor.commit();
                        }
                    }).start();

                } else {
                    //没有修改
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showUI(){

    }
}
