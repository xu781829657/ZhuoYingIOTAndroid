package com.ouzhongiot.ozapp.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.tools.IconfontTools;

/**
 * @author hxf
 * @date 创建时间: 2017/4/14
 * @Description 联系我们
 */

public class ContactUsActivity extends BaseHomeActivity implements View.OnClickListener {
    private LinearLayout llayout_back;
    private TextView icon_back;//返回
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private WebView mWebView;


    @Override
    public int addContentView() {
        return R.layout.activity_contact_us;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        icon_back = (TextView) findViewById(R.id.font_head_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        mWebView = (WebView) findViewById(R.id.webview_contact_us);

    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        icon_back.setTypeface(IconfontTools.getTypeface(this));
        tv_back_behind.setText(getString(R.string.txt_personal_about_us));
        tv_title.setText(getString(R.string.txt_contact_us));
        initWebView();
        //设置点击事件
        setClick();

    }

    public void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(UrlConstant.CONTACT_US);

    }

    public void setClick() {
        llayout_back.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_back:
                //返回
                OZApplication.getInstance().finishActivity();
                break;
        }
    }
}
