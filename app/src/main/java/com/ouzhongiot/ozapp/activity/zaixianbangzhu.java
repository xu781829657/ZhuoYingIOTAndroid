package com.ouzhongiot.ozapp.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;

public class zaixianbangzhu extends BaseHomeActivity implements View.OnClickListener{
    private LinearLayout llayout_back;
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private WebView wv_zaixianbangzhu;

    @Override
    public int addContentView() {
        return R.layout.activity_zaixianbangzhu;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        wv_zaixianbangzhu = (WebView) findViewById(R.id.wv_zaixianbangzhu);
    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        tv_back_behind.setText(getString(R.string.txt_about_product));
        tv_title.setText(getString(R.string.txt_online_help));
        llayout_back.setOnClickListener(this);

        wv_zaixianbangzhu.setWebViewClient(new WebViewClient());
        //得到webview设置
        WebSettings webSettings = wv_zaixianbangzhu.getSettings();
        //允许使用javascript
        webSettings.setJavaScriptEnabled(true);
        wv_zaixianbangzhu.loadUrl(MainActivity.ip+"smarthome/app/help");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llayout_back){
            //返回
            OZApplication.getInstance().finishActivity();
        }
    }
}

