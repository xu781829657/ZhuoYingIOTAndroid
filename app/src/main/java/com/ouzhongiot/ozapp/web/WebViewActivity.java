package com.ouzhongiot.ozapp.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MainActivity;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.others.SetPackage;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.SocketOrderTools;
import com.ouzhongiot.ozapp.tools.ToastTools;

import org.json.JSONException;
import org.json.JSONStringer;

/**
 * @author hxf
 * @date 创建时间: 2017/2/16
 * @Description 公共的WebView
 */

public class WebViewActivity extends BaseHomeActivity {
    private WebView mWebView;
    private ImageView img_load;
    public static final String PARAM_USER_SN = "userSn";//用户编号
    public static final String PARAM_DEV_SN = "devSn";//设备编号
    public static final String PARAM_DEV_TYPE_SN = "devTypeSn";//设备类型编号
    public static final String PARAM_INDEX_URL = "indexUrl";//加载的url
    public static final String PARAM_DEVICE_ID = "deviced";//设备id
    public static final String PARAM_BRAND_NAME = "brandName";//设备名


    private String userSn;//用户编号
    private String devSn;//设备编号
    private String devTypeSn;//设备类型编号
    private String indexUrl;//加载的url
    private String deviceId;//设备id
    private String brandName;//设备名

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private final String spKey = "htmldata";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                img_load.setVisibility(View.GONE);
                img_load.clearAnimation();
                //把一些值传给js
                try {
                    JSONStringer userDataObject = new JSONStringer().object();
                    userDataObject.key("userSn").value(userSn)
                            .key("devTypeSn").value(devTypeSn)
                            .key("devSn").value(devSn)
                            .key("UserDeviceID").value(deviceId)
                            .key("ServieceIP").value(UrlConstant.BASE_URL)
                            .key("BrandName").value(brandName)
                            .endObject();
                    mWebView.loadUrl("javascript:GetUserData(" + userDataObject.toString() + ")");
                    mWebView.loadUrl("javascript:GetWebData(" + sp.getString(spKey, "") + ")");
                    if (LogTools.debug) {
                        LogTools.i("传给js的用户信息->" + userDataObject.toString());
                        LogTools.i("传给js的存储信息->" + sp.getString(spKey, ""));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };


    @Override
    public int addContentView() {
        return R.layout.activity_webview;
    }

    @Override
    public void initView() {
        mWebView = (WebView) findViewById(R.id.id_webview);
        img_load = (ImageView) findViewById(R.id.img_webview_load);

    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        if (getIntent().getExtras() != null) {
            userSn = getIntent().getStringExtra(PARAM_USER_SN);
            devSn = getIntent().getStringExtra(PARAM_DEV_SN);
            devTypeSn = getIntent().getStringExtra(PARAM_DEV_TYPE_SN);
            indexUrl = getIntent().getStringExtra(PARAM_INDEX_URL);
            deviceId = getIntent().getStringExtra(PARAM_DEVICE_ID);
            brandName = getIntent().getStringExtra(PARAM_BRAND_NAME);
            //动画开始旋转
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.base_loading_large_anim);
            img_load.startAnimation(animation);

            sp = getSharedPreferences(devSn, Context.MODE_PRIVATE);
            editor = sp.edit();
            initWebView();


        }

    }

    public void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(indexUrl);
        mWebView.addJavascriptInterface(new JSToAndroid(), "js");

    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册服务器->app指令的广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("broadcass1");
        registerReceiver(serviceToAndroidReceiver, intentFilter);
    }

    BroadcastReceiver serviceToAndroidReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("broadcass1")) {
                //传给js，让他改变页面状态
                try {
                    JSONStringer jsonObject = new JSONStringer().object();
                    for (int i = 0; i < MainActivity.b.length; i++) {
                        jsonObject.key(String.valueOf(i)).value(MainActivity.b[i]);
                    }
                    jsonObject.endObject();
                    if (LogTools.debug) {
                        LogTools.i("发送给js的指令->" + jsonObject.toString());
                    }
                    mWebView.loadUrl("javascript:ReceiveOrder(" + jsonObject.toString() + ")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }
    };

    class JSToAndroid {
        //js发送指令->android
        @JavascriptInterface
        public void OrderWebToAndroid(byte b[]) {
            if (LogTools.debug) {
                Log.i("js->android发送的指令", b.toString());
            }
            ((OZApplication) getApplication()).SendOrder(SocketOrderTools.htmlOrder(devTypeSn, devSn, b));
        }

        //冷风扇4131的js是调用的这个方法（单个指令的发）
        @JavascriptInterface
        public void OrderWebToAndroidColdFanA(String a, byte b) {
            if (LogTools.debug) {
                Log.i("js->android发送的指令", a);
            }
            ((OZApplication) getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(devSn, devTypeSn, a, b));

        }

        //返回
        @JavascriptInterface
        public void BackAndroid() {
            //先发送退出设备指令
            ((OZApplication) getApplication()).SendOrder(SocketOrderTools.quitDevOrder(userSn, devTypeSn, devSn));
            OZApplication.getInstance().finishActivity();
        }
        //删除成功后自动返回
        @JavascriptInterface
        public void BackAndroid(int i) {
            //先发送退出设备指令
            ((OZApplication) getApplication()).SendOrder(SocketOrderTools.quitDevOrder(userSn, devTypeSn, devSn));
            setResult(RESULT_OK);
            OZApplication.getInstance().finishActivity();
        }

        //页面加载出来，取消动画
        @JavascriptInterface
        public void PageLoadAndroid() {
            //一定要注意：不能直接改变ui
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);

        }

        //html需要存储的数据
        @JavascriptInterface
        public void SaveWebDataAndroid(String str) {
            editor.putString(spKey, str).commit();
            if (LogTools.debug) {
                LogTools.i("html需要存储的数据->" + str);
            }
        }

        //提示信息
        @JavascriptInterface
        public void ShowRemind(String str) {
            ToastTools.show(WebViewActivity.this, str);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceToAndroidReceiver);
    }
}
