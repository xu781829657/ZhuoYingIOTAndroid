package com.ouzhongiot.ozapp.web;

import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MainActivity;
import com.ouzhongiot.ozapp.activity.machinelist;
import com.ouzhongiot.ozapp.others.SetPackage;

public class MachineController extends AppCompatActivity {

    private WebView wv_machinecontroller;
    private SharedPreferences preferences, preferencesmachine;
    private SharedPreferences.Editor editor, editormachine;
    private String workmachineid, workmachinetype;
    private LinearLayout ll_mc_pageload;
    private ImageView iv_mc_pageload;
    private String htmldata;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    htmldata = preferencesmachine.getString("htmldata", "undefined");
                    wv_machinecontroller.loadUrl("javascript: GetWebData(" + htmldata + ")");
                    wv_machinecontroller.loadUrl("javascript: GetUserData(" + "{'userSn':'" + preferences.getString("userSn", "undefined") + "','devTypeSn':'" + workmachinetype + "','devSn':'" + workmachineid + "','BrandName':'" + preferences.getString("BrandName", "设备") + "','UserDeviceID':'" + preferences.getString("UserDeviceID", "undefined") + "','ServieceIP':'" + MainActivity.ip + "'}" + ")");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_controller);
        preferences = getSharedPreferences("data", MODE_PRIVATE);
        workmachineid = preferences.getString("workmachineid", "");
        workmachinetype = preferences.getString("workmachinetype", "");
        preferencesmachine = getSharedPreferences(workmachineid, MODE_PRIVATE);
        editormachine = preferencesmachine.edit();


        ll_mc_pageload = (LinearLayout) findViewById(R.id.ll_mc_pageload);
        iv_mc_pageload = (ImageView) findViewById(R.id.iv_mc_pageload);
        Animation animation = AnimationUtils.loadAnimation(MachineController.this, R.anim.base_loading_large_anim);
        iv_mc_pageload.startAnimation(animation);


        initView();
    }


    private void initView() {
        wv_machinecontroller = (WebView) findViewById(R.id.wv_machinecontroller);
        //可以执行javascript
        wv_machinecontroller.getSettings().setJavaScriptEnabled(true);
        Log.wtf("这个是机器的地址", preferences.getString("IndexUrl", "index.html"));
        String url = preferences.getString("IndexUrl", "index.html");
//        String url = "http://192.168.1.167:8080/tipjack1/index.html";
        wv_machinecontroller.loadUrl(url);
        //android添加javascript代码，让H5页面能够调用，第二个参数对应的是H5的
        wv_machinecontroller.addJavascriptInterface(new PayJavaScriptInterface(), "js");
    }

    private class PayJavaScriptInterface {

        @JavascriptInterface
        public void toActivity(String addmachinetype) {

            Intent it = new Intent(MachineController.this, machinelist.class);
            it.putExtra("addmachinetype", addmachinetype);
            startActivity(it);

        }

        //兼容冷风扇
        @JavascriptInterface
        public void OrderWebToAndroidColdFanA(String a, byte b) {

            ((OZApplication) getApplication()).SendOrder(SetPackage.GetGoldFanAOrder(workmachineid, workmachinetype, a, b));


        }


        //兼容冷风扇
        @JavascriptInterface
        public void OrderWebToAndroid(byte b[]) {

//          Log.wtf("+++++++++++++++++++收到的命令数组",b[0]+"   "+b[1]);
            ((OZApplication) getApplication()).SendOrder(SetPackage.GetHtmlOrder(workmachineid, workmachinetype, b));
        }

        @JavascriptInterface
        public void BackAndroid() {

            onBack();

        }


        @JavascriptInterface
        public void ShowRemind(String str) {

            Toast.makeText(getApplication(), str, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void PageLoadAndroid() {
            Log.wtf("PageLoadAndroid:", "执行");
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);

            ll_mc_pageload.setVisibility(View.GONE);
            iv_mc_pageload.clearAnimation();


        }

        @JavascriptInterface
        public void SaveWebDataAndroid(String str) {

            Log.wtf("SaveWebDataAndroid:", str);
            editormachine.putString("htmldata", str);
            editormachine.commit();
        }

    }


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
//                if (MainActivity.b[13]==0x01) {
////
//                } else if (MainActivity.b[13]==0x02) {
//
//                }


                Log.wtf("+++++++++++++++++++++", ByteToString(MainActivity.b));
                wv_machinecontroller.loadUrl("javascript: ReceiveOrder(" + ByteToString(MainActivity.b) + ")");
//                wv_machinecontroller.loadUrl("javascript: ReceiveOrder("+MainActivity.b+")");

            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        ((OZApplication) getApplication()).SendOrder(SetPackage.GetMachineQuit(preferences.getString("workmachineid", "123456789111"), preferences.getString("userSn", "100000001"), preferences.getString("workmachinetypestring", "A1")));
//        unregisterReceiver(receiver2);
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

    public String ByteToString(byte b[]) {
        String str = "";
        for (int i = 0; i < b.length; i++) {
            if (i == b.length - 1) {
                str = str + "'" + i + "':'" + b[i] + "'";
            } else {
                str = str + "'" + i + "':'" + b[i] + "',";
            }
        }
        return "{" + str + "}";
    }
}
