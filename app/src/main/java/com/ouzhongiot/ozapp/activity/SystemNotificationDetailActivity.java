package com.ouzhongiot.ozapp.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.tools.IconfontTools;

/**
 * @author hxf
 * @date 创建时间: 2017/2/8
 * @Description 系统通知详情页
 */

public class SystemNotificationDetailActivity extends Activity implements View.OnClickListener{
    private TextView font_back;
    private TextView txt_head_content;
    private TextView txt_head_right;

    private WebView mWebView;
    public static final String PARAM_URL = "param_url";
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_notification_detail);
        initView();
        initValue();

    }

    public void initView() {
        font_back = (TextView) findViewById(R.id.font_head_back);
        txt_head_content = (TextView) findViewById(R.id.txt_head_content);
        txt_head_right = (TextView) findViewById(R.id.txt_head_right);
        mWebView = (WebView) findViewById(R.id.web_sys_noti_detail);
    }

    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        Typeface typeface = IconfontTools.getTypeface(this);
        font_back.setTypeface(typeface);
        txt_head_content.setText(getString(R.string.notification_detail_txt));
        txt_head_right.setVisibility(View.GONE);
        font_back.setOnClickListener(this);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (getIntent().getExtras()!=null){
            url = getIntent().getStringExtra(PARAM_URL);
            mWebView.loadUrl(url);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.font_head_back){
            //返回
            OZApplication.getInstance().finishActivity();
        }
    }
}
