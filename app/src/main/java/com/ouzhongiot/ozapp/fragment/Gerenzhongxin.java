package com.ouzhongiot.ozapp.fragment;


import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.AboutProductActivity;
import com.ouzhongiot.ozapp.activity.AboutUsActivity;
import com.ouzhongiot.ozapp.activity.MessageNotificationActivity;
import com.ouzhongiot.ozapp.activity.TypeList;
import com.ouzhongiot.ozapp.activity.coldfanAindex;
import com.ouzhongiot.ozapp.activity.coldfanset;
import com.ouzhongiot.ozapp.activity.gerenxinxi;
import com.ouzhongiot.ozapp.activity.youhuiquan;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.http.ConnectDataTask;
import com.ouzhongiot.ozapp.http.PostParamTools;
import com.ouzhongiot.ozapp.others.AndroidShare;
import com.ouzhongiot.ozapp.others.CircleImageView;
import com.ouzhongiot.ozapp.others.DataCleanManager;
import com.ouzhongiot.ozapp.tools.IconfontTools;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.SharedPreferencesTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hxf
 * @date 修改时间: 2017/2/10
 * @Description 个人中心
 */
public class Gerenzhongxin extends Fragment implements View.OnClickListener, ConnectDataTask.OnResultDataLintener {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String userSn;
    private CircleImageView yonghuzhongxin_touxiang;
    private TextView tv_yonghuzhongxin_nicheng, tv_yonghuzhongxin_ID, tv_yonghuzhongxin_qingchuhuancun;
    private LinearLayout ll_share;
    private LinearLayout ll_geren;
    private LinearLayout ll_add_machine;//添加设备
    private LinearLayout ll_message;//消息通知
    private LinearLayout ll_youhui;//优惠券
    private LinearLayout ll_about;//关于产品
    private LinearLayout ll_lianxi;//联系我们
    private LinearLayout ll_more;//更多产品
    private LinearLayout ll_cache;//清除缓存
    private LinearLayout ll_setting;//设置

    private TextView font_red_hot;//消息通知红点

    private String spSysNotiNewTime;//存在本地的系统通知最新时间
    private String newSysNotiTime;//最新的系统通知时间
    private long sptime;
    private long newtime;
    private boolean flag_sys_noti = false;
    private final int SYS_NOTI_REQUEST_CODE = 1000;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gerenzhongxin, container, false);
        ll_share = (LinearLayout) view.findViewById(R.id.ll_share);
        ll_geren = (LinearLayout) view.findViewById(R.id.gerenxinxi);
        ll_add_machine = (LinearLayout) view.findViewById(R.id.yonghuzhongxin_addmachine);
        ll_message = (LinearLayout) view.findViewById(R.id.yonghuzhongxin_message);
        ll_youhui = (LinearLayout) view.findViewById(R.id.yonghuzhongxin_youhuiquan);
        ll_about = (LinearLayout) view.findViewById(R.id.yonghuzhongxin_aboutproduct);
        ll_lianxi = (LinearLayout) view.findViewById(R.id.yonghuzhongxin_lianxiwomen);
        ll_more = (LinearLayout) view.findViewById(R.id.yonghuzhongxin_gengduochanpin);
        ll_cache = (LinearLayout) view.findViewById(R.id.ll_yonghuzhongxin_qingchuhuancun);
        ll_setting = (LinearLayout) view.findViewById(R.id.ll_gerenzhongxin_shezhi);


        tv_yonghuzhongxin_qingchuhuancun = (TextView) view.findViewById(R.id.tv_yonghuzhongxin_qingchuhuancun);
        tv_yonghuzhongxin_nicheng = (TextView) view.findViewById(R.id.tv_yonghuzhongxin_nicheng);
        tv_yonghuzhongxin_ID = (TextView) view.findViewById(R.id.tv_yonghuzhongxin_ID);
        yonghuzhongxin_touxiang = (CircleImageView) view.findViewById(R.id.yonghuzhongxin_touxiang);
        font_red_hot = (TextView) view.findViewById(R.id.font_message_dot);
        font_red_hot.setTypeface(IconfontTools.getTypeface(getActivity()));
        preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = preferences.edit();
        userSn = preferences.getString("userSn", "");
        tv_yonghuzhongxin_nicheng.setText(preferences.getString("nickname", "昵称"));
        tv_yonghuzhongxin_ID.setText(userSn);

        setClick();


        try {
            tv_yonghuzhongxin_qingchuhuancun.setText(DataCleanManager.getTotalCacheSize(getActivity().getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!((OZApplication) getActivity().getApplication()).getISTOUXIANGUPDATE()) {
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(yonghuzhongxin_touxiang,
                    R.mipmap.test, R.mipmap.test);
            Log.wtf("头像地址", preferences.getString("headImageUrl", "null"));
            if (!preferences.getString("headImageUrl", "null").equals("null")) {
                ((OZApplication) getActivity().getApplication()).getImageLoader().get(preferences.getString("headImageUrl", "null"), listener);
            }
        }

        //请求系统通知接口
//        new HcNetWorkTask(getActivity(), this, 1).doPost(UrlConstant.PUBLIC_MESSAGE, null, postParams(1).getBytes());
        return view;
    }

    public void setClick() {
        ll_share.setOnClickListener(this);
        ll_geren.setOnClickListener(this);
        ll_add_machine.setOnClickListener(this);
        ll_message.setOnClickListener(this);
        ll_youhui.setOnClickListener(this);
        ll_about.setOnClickListener(this);
        ll_lianxi.setOnClickListener(this);
        ll_more.setOnClickListener(this);
        ll_cache.setOnClickListener(this);
        ll_setting.setOnClickListener(this);

    }

    private String postParams(int code) {
        Map<String, String> params = new HashMap<String, String>();
        if (code == 1) {
            //系统通知列表
            params.put("page", String.valueOf(1));
            params.put("rows",
                    "1");
            if (LogTools.debug) {
                LogTools.i("系统通知参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        }
        return "";
    }


    @Override
    public void onResume() {
        if (((OZApplication) getActivity().getApplication()).getISTOUXIANGUPDATE()) {
            if (coldfanAindex.getLoacalBitmap(preferences.getString("touxiangbendiurl", Environment.getExternalStorageDirectory() + "/"
                    + "lianxiatouxiang" + ".jpg")) != null) {

                yonghuzhongxin_touxiang.setImageBitmap(coldfanAindex.getLoacalBitmap(preferences.getString("touxiangbendiurl", Environment.getExternalStorageDirectory() + "/"
                        + "lianxiatouxiang" + ".jpg")));
            }
        }

        super.onResume();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_share:
                //分享
                AndroidShare as = new AndroidShare(
                        getActivity(),
                        "微新风——让智能变得简单...",
                        "www.ouzhongiot.com");
                as.show();
                break;
            case R.id.gerenxinxi:
                //个人中心
                Intent intent = new Intent(getActivity(), gerenxinxi.class);
                startActivity(intent);
                break;
            case R.id.yonghuzhongxin_addmachine:
                //添加设备
//                Intent  = new Intent(getActivity(), TypeList.class);
//                startActivity(addIntent);
                Intent addIntent = new Intent(getActivity(),coldfanset.class);
                startActivity(addIntent);
                getActivity().overridePendingTransition(R.anim.activity_open, R.anim.activity_close_donothing);
                break;
            case R.id.yonghuzhongxin_message:
                //消息通知
                Intent meIntent = new Intent(getActivity(), MessageNotificationActivity.class);
                meIntent.putExtra(MessageNotificationActivity.PARAM_SYSTEM_NOTI_FLAG, flag_sys_noti);
                startActivityForResult(meIntent, SYS_NOTI_REQUEST_CODE);
                break;
            case R.id.yonghuzhongxin_youhuiquan:
                //优惠券
                Intent yIntent = new Intent(getActivity(), youhuiquan.class);
                startActivity(yIntent);
                break;
            case R.id.yonghuzhongxin_aboutproduct:
                //关于产品
//                Intent pIntent = new Intent(getActivity(), aboutproduct.class);
//                startActivity(pIntent);
                Intent pIntent = new Intent(getActivity(), AboutProductActivity.class);
                startActivity(pIntent);
                break;
            case R.id.yonghuzhongxin_lianxiwomen:
                //联系我们
//                Intent lIntent = new Intent(getActivity(), lianxiwomen.class);
//                startActivity(lIntent);
                Intent lIntent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(lIntent);
                break;
            case R.id.yonghuzhongxin_gengduochanpin:
                //更多产品
                Intent moreIntent = new Intent(getActivity(), TypeList.class);
                startActivity(moreIntent);
                getActivity().overridePendingTransition(R.anim.activity_open, R.anim.activity_close_donothing);
                break;
            case R.id.ll_gerenzhongxin_shezhi:
                Intent sIntent = new Intent(getActivity(), gerenxinxi.class);
                startActivity(sIntent);
                break;
            case R.id.ll_yonghuzhongxin_qingchuhuancun:
                DataCleanManager.clearAllCache(getActivity().getApplicationContext());
                tv_yonghuzhongxin_qingchuhuancun.setText("0.0Mb");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SYS_NOTI_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                //红点消失
                font_red_hot.setVisibility(View.GONE);
                flag_sys_noti = false;
            }
        }
    }

    @Override
    public void onResult(String result, int code) {
        if (!result.isEmpty()) {
            try {
                JSONObject object = new JSONObject(result);
                if (code == 1) {
                    //系统通知列表
                    if (LogTools.debug) {
                        LogTools.i("系统通知返回数据->" + result);
                    }
                    if (object.getString("total").equals("null")) {
                        //没有数据
                    } else if (object.getInt("total") > 0 && object.getJSONArray("rows").length() > 0) {
                        //读取本地的系统通知最新时间
                        spSysNotiNewTime = SharedPreferencesTools.getInstance(getActivity()).getData(SpConstant.SYSTEM_NOTI_NEWEST_TIME).toString();
                        if (!spSysNotiNewTime.isEmpty()) {
                            //比对两个时间
                            newSysNotiTime = object.getJSONArray("rows").getJSONObject(0).getString("addTime").toString();
                            sptime = Long.valueOf(spSysNotiNewTime.replaceAll("[-\\s:]", ""));
                            newtime = Long.valueOf(newSysNotiTime.replaceAll("[-\\s:]", ""));
                            if (newtime > sptime) {
                                //有新通知
                                font_red_hot.setVisibility(View.VISIBLE);
                                flag_sys_noti = true;
                            } else {
                                font_red_hot.setVisibility(View.GONE);
                                flag_sys_noti = false;
                            }

                        } else {
                            //本地没有存储，说明用户从没有进入过“系统通知列表”页，直接显示红点
                            font_red_hot.setVisibility(View.VISIBLE);
                            flag_sys_noti = true;
                        }


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
