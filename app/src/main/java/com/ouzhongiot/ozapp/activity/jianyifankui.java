package com.ouzhongiot.ozapp.activity;

import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.ouzhongiot.ozapp.R;

public class jianyifankui extends AppCompatActivity {

    private WebView wv_machinecontroller;
    private SharedPreferences preferences;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:

                    wv_machinecontroller.loadUrl("javascript: JianYiFanKui("+ "{'userSn':'"+preferences.getString("userSn","undefined")+"','userId':'"+preferences.getString("userId","undefined")+"','ServieceIP':'"+MainActivity.ip +"'}"+")");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jianyifankui);
        preferences = getSharedPreferences("data",MODE_PRIVATE);

        this.findViewById(R.id.jianyifankui_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        initView();
    }




    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    private void initView() {
        this.wv_machinecontroller = (WebView) this.findViewById(R.id.wv_jianyifankui);
        //可以执行javascript
        this.wv_machinecontroller.getSettings().setJavaScriptEnabled(true);
        String url = MainActivity.ip+"smarthome/app/feedback";
        this.wv_machinecontroller.loadUrl(url);
        //android添加javascript代码，让H5页面能够调用，第二个参数对应的是H5的
        this.wv_machinecontroller.addJavascriptInterface(new PayJavaScriptInterface(), "js");
    }

    private class PayJavaScriptInterface {

        @JavascriptInterface
        public void PageLoadAndroid() {
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
        }

        @JavascriptInterface
        public void BackAndroid() {

            onBack();

        }
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

}
