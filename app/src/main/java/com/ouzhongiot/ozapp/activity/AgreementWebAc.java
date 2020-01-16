package com.ouzhongiot.ozapp.activity;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;

public class AgreementWebAc extends BaseHomeActivity {
    @Override
    public int addContentView() {
        return R.layout.ac_agreement_web;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initValue() {

        WebView webview = (WebView)findViewById(R.id.web_view);

        webview.loadUrl("file:///android_asset/agreement.html");


        webview.setWebViewClient(new WebViewClient());


        webview.getSettings().setJavaScriptEnabled(true);

//自适应屏幕
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webview.getSettings().setLoadWithOverviewMode(true);

//设置可以支持缩放

        webview.getSettings().setSupportZoom(true);

//扩大比例的缩放

        webview.getSettings().setUseWideViewPort(true);

//设置是否出现缩放工具

        webview.getSettings().setBuiltInZoomControls(true);
    }
}
