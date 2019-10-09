package com.ouzhongiot.ozapp.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MachineTypeListActivity;
import com.ouzhongiot.ozapp.activity.XFAirActivity;
import com.ouzhongiot.ozapp.adapter.MyMachineGridAdapter;
import com.ouzhongiot.ozapp.airclean.B1Activity;
import com.ouzhongiot.ozapp.base.BaseHomeFragment;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.dryer.DryerIndex;
import com.ouzhongiot.ozapp.http.ConnectDataTask;
import com.ouzhongiot.ozapp.http.HcNetWorkTask;
import com.ouzhongiot.ozapp.http.PostParamTools;
import com.ouzhongiot.ozapp.others.SetPackage;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.SocketOrderTools;
import com.ouzhongiot.ozapp.tools.SpData;
import com.ouzhongiot.ozapp.tools.ToastTools;
import com.ouzhongiot.ozapp.view.DefineGridView;
import com.ouzhongiot.ozapp.web.WebViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hxf
 * @date 创建时间: 2017/4/21
 * @Description 绑定的设备列表fragment
 */

public class MymachineFragment extends BaseHomeFragment implements ConnectDataTask.OnResultDataLintener, View.OnClickListener, AdapterView.OnItemClickListener {
    private final int SKIP_REQUEST_CODE = 1000;
    private ImageView img_add;//添加设备
    private DefineGridView mGridView;
    private MyMachineGridAdapter mAdapter;
    private ImageView img_weather;
    private TextView tv_weather;//天气
    private TextView tv_temp;//温度
    private TextView tv_hum;//湿度
    private TextView tv_quality;//空气质量
    private TextView tv_wind;//风
    private TextView tv_city;//城市
    private String temp;//温度
    private String hum;//湿度
    private String weather;//天气情况
    private String wind;//风
    private String quality;//空气质量
    private String chuanyi;//穿衣
    private String city;
    private ScrollView scrollView;
    private LinearLayout llayout_nodata;
    private Button btn_add;
    private int delete_position = -1;//删除的某一项
    private int modify_name_position = -1;//修改名称的某一项

    private RelativeLayout rlayout_my_machine;
    private LinearLayout llayout_weather;



    @Override
    public int getLayoutResID() {
        return R.layout.fragment_my_machine;
    }

    @Override
    public void initView() {
        img_add = (ImageView) mView.findViewById(R.id.img_my_machine_head_add);
        mGridView = (DefineGridView) mView.findViewById(R.id.grid_my_machine);
        img_weather = (ImageView) mView.findViewById(R.id.img_my_machine_weather);
        tv_weather = (TextView) mView.findViewById(R.id.tv_my_machine_weather);
        tv_temp = (TextView) mView.findViewById(R.id.tv_my_machine_temp);
        tv_hum = (TextView) mView.findViewById(R.id.tv_my_machine_shidu);
        tv_quality = (TextView) mView.findViewById(R.id.tv_my_machine_zhiliang);
        tv_wind = (TextView) mView.findViewById(R.id.tv_my_machine_wind);
        tv_city = (TextView) mView.findViewById(R.id.tv_my_machine_city);

        scrollView = (ScrollView) mView.findViewById(R.id.scrollview_my_machine);
        llayout_nodata = (LinearLayout) mView.findViewById(R.id.llayout_my_machine_nodata);
        btn_add = (Button) mView.findViewById(R.id.btn_my_machine_nodata_add);
        rlayout_my_machine = (RelativeLayout) mView.findViewById(R.id.rlayout_my_machine);
        llayout_weather = (LinearLayout) mView.findViewById(R.id.llayout_my_machine_weather);


    }

    @Override
    public void initValue() {
        //请求我的设备列表接口
        requestMyMachineList();
        //请求天气接口
        requestWeather();
        //设置点击事件
        setClick();

    }

    public void requestMyMachineList() {
        new HcNetWorkTask(getActivity(), this, 1).doPost(UrlConstant.QUERY_USER_DEVICE, null, postParams(1).getBytes());
    }

    public void requestWeather() {
        new HcNetWorkTask(getActivity(), this, 2).doPost(UrlConstant.QUERY_WEATHER, null, postParams(2).getBytes());
    }

    public void setClick() {
        img_add.setOnClickListener(this);
        mGridView.setOnItemClickListener(this);
        btn_add.setOnClickListener(this);
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
            // 查询绑定设备列表
            params.put("userSn", SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_USERSN).toString());
            if (LogTools.debug) {
                LogTools.i("查询绑定设备参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        } else if (code == 2) {
            //查询天气
            city = SpData.getInstance(getActivity()).getData(SpConstant.LOCATION_CITY).toString();
            if (city.equals("")) {
                params.put("city", getString(R.string.txt_current_city));
            } else {
                params.put("city", city.substring(0, city.length() - 1));
            }
            if (LogTools.debug) {
                LogTools.i("查询天气参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        }
        return "";
    }

    @Override
    public void onResult(String result, int code) {
        if (!result.isEmpty()) {
            try {
                JSONObject object = new JSONObject(result);
                if (code == 1) {
                    //查询绑定的设备
                    if (LogTools.debug) {
                        LogTools.d("绑定设备返回数据->" + result);
                    }
                    int state = object.getInt("state");
                    if (state == 0) {
                        //注意一个问题：当没有设备的时候，返回的data:null 把它当成数组来处理是不正确的，直接会执行下面的catch语句，暂无设备布局不显示
                        if (object.getString("data").equals("null")) {
                            //没有绑定设备
                            scrollView.setVisibility(View.GONE);
                            llayout_nodata.setVisibility(View.VISIBLE);
                        } else {
                            JSONArray dataArray = object.getJSONArray("data");
                            if (dataArray.length() > 0) {
                                llayout_nodata.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                                mAdapter = new MyMachineGridAdapter(getActivity(), dataArray);
                                mGridView.setAdapter(mAdapter);
                            }


                        }


                    } else if (state == 1) {
                        //参数异常
                        ToastTools.show(getActivity(), getString(R.string.txt_param_exception));
                    }

                } else if (code == 2) {
                    //查询天气
                    JSONArray HeWeatherArray = object.getJSONArray("HeWeather5");
                    JSONObject dataObject = HeWeatherArray.getJSONObject(0);
                    if (dataObject.getString("status").equals("ok")) {
                        JSONObject weatherObject = dataObject.getJSONObject("now");
                        weather = weatherObject.getJSONObject("cond").getString("txt");
                        tv_weather.setText(weather);
                        //图片的显示
                        if (weather.equals("晴")) {
                            img_weather.setImageResource(R.mipmap.weather_qing);
                        } else if (weather.equals("多云") || weather.equals("少云")) {
                            img_weather.setImageResource(R.mipmap.weather_duoyun);
                        } else if (weather.equals("晴间多云")) {
                            img_weather.setImageResource(R.mipmap.weather_duoyunzhuanqing);
                        } else if (weather.equals("阴")) {
                            img_weather.setImageResource(R.mipmap.weather_yin);
                        } else if (weather.contains("风") || weather.equals("平静")) {
                            img_weather.setImageResource(R.mipmap.weather_wind);
                        } else if (weather.equals("阵雨") || weather.equals("强阵雨")) {
                            img_weather.setImageResource(R.mipmap.weather_zhenyu);
                        } else if (weather.contains("雷")) {
                            img_weather.setImageResource(R.mipmap.weather_leiyu);
                        } else if (weather.equals("小雨") || weather.equals("毛毛雨/细雨")) {
                            img_weather.setImageResource(R.mipmap.weather_xiaoyu);
                        } else if (weather.equals("中雨") || weather.equals("冻雨")) {
                            img_weather.setImageResource(R.mipmap.weather_zhongyu);
                        } else if (weather.equals("大雨") || weather.equals("极端降雨") || weather.contains("暴雨")) {
                            img_weather.setImageResource(R.mipmap.weather_dayu);

                        } else if (weather.equals("小雪")) {
                            img_weather.setImageResource(R.mipmap.weather_xiaoxue);
                        } else if (weather.equals("中雪") || weather.equals("阵雪")) {
                            img_weather.setImageResource(R.mipmap.weather_zhongxue);
                        } else if (weather.equals("大雪") || weather.equals("暴雪")) {
                            img_weather.setImageResource(R.mipmap.weather_daxue);
                        } else if (weather.equals("雨夹雪") || weather.equals("雨雪天气") || weather.equals("阵雨夹雪")) {
                            img_weather.setImageResource(R.mipmap.weather_yujiaxue);
                        } else if (weather.contains("雾")) {
                            img_weather.setImageResource(R.mipmap.weather_wu);
                        } else if (weather.equals("霾")) {
                            img_weather.setImageResource(R.mipmap.weather_mai);
                        }
                        //温度
                        temp = weatherObject.getString("tmp");
                        tv_temp.setText(temp);
                        //湿度
                        hum = weatherObject.getString("hum");
                        tv_hum.setText(hum);
                        //空气质量
                        quality = dataObject.getJSONObject("aqi").getJSONObject("city").getString("qlty");
                        if (quality.equals("轻度污染") || quality.equals("中度污染")) {
                            quality = "中";
                        } else if (quality.equals("重度污染") || quality.equals("严重污染")) {
                            quality = "差";
                        }
                        tv_quality.setText(quality);
                        //风
                        wind = weatherObject.getJSONObject("wind").getString("sc").substring(0, 1);
                        //wind：3-4  截取第一位
                        if (wind.equals("0")) {
                            //无风
                            wind = "无风";
                        } else if (wind.equals("1")) {
                            wind = "软风";
                        } else if (wind.equals("2")) {
                            wind = "轻风";
                        } else if (wind.equals("3")) {
                            wind = "微风";
                        } else if (wind.equals("4")) {
                            wind = "和风";
                        } else if (wind.equals("5")) {
                            wind = "轻劲风";
                        } else if (wind.equals("6")) {
                            wind = "强风";
                        } else if (wind.equals("7")) {
                            wind = "疾风";
                        } else if (wind.equals("8")) {
                            wind = "大风";
                        } else if (wind.equals("9")) {
                            wind = "裂风";
                        } else if (wind.equals("10")) {
                            wind = "狂风";
                        }
                        tv_wind.setText(wind);
                        //城市
                        tv_city.setText(city);
                        chuanyi = dataObject.getJSONObject("suggestion").getJSONObject("drsg").getString("txt");
                        //存在本地
                        SpData.getInstance(getActivity()).putData(SpConstant.WEATHER_INFO, weather);
                        SpData.getInstance(getActivity()).putData(SpConstant.WEATHER_TEMP, temp);
                        SpData.getInstance(getActivity()).putData(SpConstant.WEATHER_HUM, hum);
                        SpData.getInstance(getActivity()).putData(SpConstant.WEATHER_QUALITY, quality);
                        SpData.getInstance(getActivity()).putData(SpConstant.WEATHER_WIND, wind);
                        SpData.getInstance(getActivity()).putData(SpConstant.WEATHER_CHUANYI, chuanyi);
                    } else {
                        ToastTools.show(getActivity(), dataObject.getString("status"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_my_machine_head_add) {
            //添加设备
//            Intent intent = new Intent(getActivity(), coldfanset.class);
//            Intent intent = new Intent(getActivity(), AddMachineSetActivity.class);
//            startActivity(intent);
            Intent intent = new Intent(getActivity(), MachineTypeListActivity.class);
            intent.putExtra(MachineTypeListActivity.PARAM_SWITCH_TYPE,"add");
            startActivity(intent);

        } else if (v.getId() == R.id.btn_my_machine_nodata_add) {
            //添加设备
            Intent intent = new Intent(getActivity(), MachineTypeListActivity.class);
            intent.putExtra(MachineTypeListActivity.PARAM_SWITCH_TYPE,"add");
            startActivity(intent);
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            SpData.getInstance(getActivity()).putData(SpConstant.MY_MACHINE_ITEM_CLICKED_TYPE_SN, mAdapter.getValArray().getJSONObject(position).getString("devTypeSn"));
            SpData.getInstance(getActivity()).putData(SpConstant.MY_MACHINE_ITEM_CLICKED_DEVICE_SN, mAdapter.getValArray().getJSONObject(position).getString("devSn"));

            String devTypeSn = mAdapter.getValArray().getJSONObject(position).getString("devTypeSn");
            ((OZApplication) getActivity().getApplication()).setHeartbyte(SetPackage.GetHeartpackage(SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_USERSN).toString()));
            ((OZApplication) getActivity().getApplication()).SendOrder(SocketOrderTools.addDeviceOrder(SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_USERSN).toString(), devTypeSn, mAdapter.getValArray().getJSONObject(position).getString("devSn")));
            switch (devTypeSn) {
                case "4231":
                    SpData.getInstance(getActivity()).putData(SpConstant.MY_MACHINE_ITEM_CLICKED_TYPE_SN, mAdapter.getValArray().getJSONObject(position).getString("devTypeSn"));
                    SpData.getInstance(getActivity()).putData(SpConstant.MY_MACHINE_ITEM_CLICKED_DEVICE_SN, mAdapter.getValArray().getJSONObject(position).getString("devSn"));
                    SpData.getInstance(getActivity()).putData(SpConstant.MY_MACHINE_ITEM_CLICKED_ID, mAdapter.getValArray().getJSONObject(position).getString("id"));
                    SpData.getInstance(getActivity()).putData(SpConstant.MY_MACHINE_ITEM_CLICKED_INDEX_URL, mAdapter.getValArray().getJSONObject(position).getString("indexUrl"));
                    SpData.getInstance(getActivity()).putData(SpConstant.MY_MACHINE_ITEM_CLICKED_TYPE_STRING, "B1");

                    Intent intent = new Intent(getActivity(), B1Activity.class);
                    startActivity(intent);
                    break;
                case "4232":
                    //新风空气净化器
                    //发送“加入新设备”指令
                    Intent XFIntent = new Intent(getActivity(), XFAirActivity.class);
                    XFIntent.putExtra(XFAirActivity.PARAM_DEVICE_ID, mAdapter.getValArray().getJSONObject(position).getString("id"));
                    XFIntent.putExtra(XFAirActivity.PARAM_DEVICE_SN, mAdapter.getValArray().getJSONObject(position).getString("devSn"));
                    XFIntent.putExtra(XFAirActivity.PARAM_DEVICE_TYPE_SN, devTypeSn);
                    XFIntent.putExtra(XFAirActivity.PARAM_USER_SN, SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_USERSN).toString());
                    if (mAdapter.getValArray().getJSONObject(position).getString("definedName").equals("null")) {
                        XFIntent.putExtra(XFAirActivity.PARAM_DEVICE_NAME, mAdapter.getValArray().getJSONObject(position).getString("brand") + mAdapter.getValArray().getJSONObject(position).getString("typeName"));
                    } else {
                        XFIntent.putExtra(XFAirActivity.PARAM_DEVICE_NAME, mAdapter.getValArray().getJSONObject(position).getString("brand") + mAdapter.getValArray().getJSONObject(position).getString("definedName"));

                    }
                    delete_position = position;
                    modify_name_position = position;
                    startActivityForResult(XFIntent, SKIP_REQUEST_CODE);
                    break;
                case "4331":

                    SpData.getInstance(getActivity()).putData(SpConstant.MY_MACHINE_ITEM_CLICKED_TYPE_SN, mAdapter.getValArray().getJSONObject(position).getString("devTypeSn"));
                    SpData.getInstance(getActivity()).putData(SpConstant.MY_MACHINE_ITEM_CLICKED_DEVICE_SN, mAdapter.getValArray().getJSONObject(position).getString("devSn"));
                    SpData.getInstance(getActivity()).putData(SpConstant.MY_MACHINE_ITEM_CLICKED_ID, mAdapter.getValArray().getJSONObject(position).getString("id"));
                    SpData.getInstance(getActivity()).putData(SpConstant.MY_MACHINE_ITEM_CLICKED_INDEX_URL, mAdapter.getValArray().getJSONObject(position).getString("indexUrl"));
                    SpData.getInstance(getActivity()).putData(SpConstant.MY_MACHINE_ITEM_CLICKED_TYPE_STRING, "C1");

                    Intent DItent = new Intent(getActivity(), DryerIndex.class);
                    startActivity(DItent);
                    break;
                case "4332":
                    //C2干衣机
                    //1）设置心跳包
//                    ((OZApplication) getActivity().getApplication()).setHeartbyte(SetPackage.GetHeartpackage(SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_USERSN).toString()));
                    //2）发送加入设备指令
//                    ((OZApplication) getActivity().getApplication()).SendOrder(SocketOrderTools.addDeviceOrder(SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_USERSN).toString(), devTypeSn, mAdapter.getValArray().getJSONObject(position).getString("devSn")));
                    //3) 跳转activity
                    Intent webViewIntent = new Intent(getActivity(), WebViewActivity.class);
                    webViewIntent.putExtra(WebViewActivity.PARAM_USER_SN, SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_USERSN).toString());
                    webViewIntent.putExtra(WebViewActivity.PARAM_DEV_SN, mAdapter.getValArray().getJSONObject(position).getString("devSn"));
                    webViewIntent.putExtra(WebViewActivity.PARAM_DEV_TYPE_SN, devTypeSn);
                    webViewIntent.putExtra(WebViewActivity.PARAM_INDEX_URL, mAdapter.getValArray().getJSONObject(position).getString("indexUrl"));
                    webViewIntent.putExtra(WebViewActivity.PARAM_DEVICE_ID, mAdapter.getValArray().getJSONObject(position).getString("id"));
                    webViewIntent.putExtra(WebViewActivity.PARAM_BRAND_NAME, mAdapter.getValArray().getJSONObject(position).getString("brand") + mAdapter.getValArray().getJSONObject(position).getString("typeName"));
                    startActivity(webViewIntent);
                    break;
                default:
//                    ((OZApplication) getActivity().getApplication()).setHeartbyte(SetPackage.GetHeartpackage(SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_USERSN).toString()));
//                    ((OZApplication) getActivity().getApplication()).SendOrder(SocketOrderTools.addDeviceOrder(SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_USERSN).toString(), devTypeSn, mAdapter.getValArray().getJSONObject(position).getString("devSn")));
                    Intent intentDefault = new Intent(getActivity(), WebViewActivity.class);
                    intentDefault.putExtra(WebViewActivity.PARAM_USER_SN, SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_USERSN).toString());
                    intentDefault.putExtra(WebViewActivity.PARAM_DEV_SN, mAdapter.getValArray().getJSONObject(position).getString("devSn"));
                    intentDefault.putExtra(WebViewActivity.PARAM_DEV_TYPE_SN, devTypeSn);
                    intentDefault.putExtra(WebViewActivity.PARAM_INDEX_URL, mAdapter.getValArray().getJSONObject(position).getString("indexUrl"));
                    intentDefault.putExtra(WebViewActivity.PARAM_DEVICE_ID, mAdapter.getValArray().getJSONObject(position).getString("id"));
                    if (mAdapter.getValArray().getJSONObject(position).getString("brand").equals("null")) {
                        if (mAdapter.getValArray().getJSONObject(position).getString("definedName").equals("null")) {
                            intentDefault.putExtra(WebViewActivity.PARAM_BRAND_NAME, mAdapter.getValArray().getJSONObject(position).getString("typeName"));
                        } else {
                            intentDefault.putExtra(WebViewActivity.PARAM_BRAND_NAME, mAdapter.getValArray().getJSONObject(position).getString("definedName"));
                        }
                    } else {
                        if (mAdapter.getValArray().getJSONObject(position).getString("definedName").equals("null")) {
                            intentDefault.putExtra(WebViewActivity.PARAM_BRAND_NAME, mAdapter.getValArray().getJSONObject(position).getString("brand") + mAdapter.getValArray().getJSONObject(position).getString("typeName"));
                        } else {
                            intentDefault.putExtra(WebViewActivity.PARAM_BRAND_NAME, mAdapter.getValArray().getJSONObject(position).getString("brand") + mAdapter.getValArray().getJSONObject(position).getString("definedName"));
                        }
                    }
//                    startActivity(intentDefault);
                    startActivityForResult(intentDefault, 1001);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SKIP_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                mAdapter.getValArray().remove(delete_position);
                mAdapter.notifyDataSetChanged();
            } else if (resultCode == XFAirActivity.RESULT_CODE_XINFENG_MODIFY_NAME) {
                if (data.getBooleanExtra(XFAirActivity.RESULT_XINFENG_MODIFY_NAME_KEY, false)) {

                } else {
                    //没有修改
                }
            }
        } else if (requestCode == 1001) {
            //刷新一下页面
            if (resultCode == getActivity().RESULT_OK) {
                requestMyMachineList();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
