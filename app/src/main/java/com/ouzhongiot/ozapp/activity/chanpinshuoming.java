package com.ouzhongiot.ozapp.activity;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;

public class chanpinshuoming extends BaseHomeActivity implements View.OnClickListener {
    private LinearLayout llayout_back;
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private WebView mWebView;
    private LinearLayout ll_changpinshuoming_jiazai;
    private ImageView iv_changpinshuoming_jiazai;


    @Override
    public int addContentView() {
        return R.layout.activity_chanpinshuoming;
    }


    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);

        ll_changpinshuoming_jiazai = (LinearLayout) findViewById(R.id.ll_changpinshuoming_jiazai);
        iv_changpinshuoming_jiazai = (ImageView) findViewById(R.id.iv_changpinshuoming_jiazai);
        mWebView = (WebView) findViewById(R.id.wv_chanpingshuoming);

    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        tv_back_behind.setText(getString(R.string.txt_about_product));
        tv_title.setText(getString(R.string.txt_product_instructions));
        llayout_back.setOnClickListener(this);

        Animation animation = AnimationUtils.loadAnimation(chanpinshuoming.this, R.anim.base_loading_large_anim);
        iv_changpinshuoming_jiazai.startAnimation(animation);

        //可以执行javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        String url = "http://www.ouzhongiot.com";
        this.mWebView.loadUrl(url);

        //android添加javascript代码，让H5页面能够调用，第二个参数对应的是H5的
        this.mWebView.addJavascriptInterface(new PayJavaScriptInterface(), "js");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llayout_back) {
            //返回
            OZApplication.getInstance().finishActivity();
        }

    }

    private class PayJavaScriptInterface {

        @JavascriptInterface
        public void PageLoadAndroid() {

            Log.wtf("函数执行了", "PageLoadAndroid");
            ll_changpinshuoming_jiazai.setVisibility(View.GONE);
            iv_changpinshuoming_jiazai.clearAnimation();

        }

    }

}
