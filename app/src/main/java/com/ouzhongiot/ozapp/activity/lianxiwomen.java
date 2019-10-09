package com.ouzhongiot.ozapp.activity;

import android.app.Dialog;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.ouzhongiot.ozapp.R;

public class lianxiwomen extends AppCompatActivity {


    private WebView wv_lianxiwomen;
    protected Context mContext;
    private static final int PROGRESS_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lianxiwomen);
        wv_lianxiwomen = (WebView) findViewById(R.id.wv_lianxiwomen);

//        wv_lianxiwomen.loadUrl("http://www.baidu.com");
        wv_lianxiwomen.setWebViewClient(new WebViewClient());
        //得到webview设置
        WebSettings webSettings = wv_lianxiwomen.getSettings();
        //允许使用javascript
        webSettings.setJavaScriptEnabled(true);
        wv_lianxiwomen.loadUrl(MainActivity.ip+"smarthome/app/aboutus");




        this.findViewById(R.id.lianxiwomen_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

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
