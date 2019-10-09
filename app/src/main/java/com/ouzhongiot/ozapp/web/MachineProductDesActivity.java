package com.ouzhongiot.ozapp.web;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.constant.UrlConstant;

/**
 * @author hxf
 * @date 创建时间: 2017/6/23
 * @Description 具体设备的产品说明
 */

public class MachineProductDesActivity extends BaseHomeActivity implements View.OnClickListener {
    private LinearLayout llayout_back;
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private WebView mWebView;
    public static final String PARAM_BIG_TYPE_SN = "param_big_type_sn";//大类的typeSn
    public static final String PARAM_TYPE_NUMBER = "param_type_number";//具体的typeSn
    private String bigTypeSn;//大类的typeSn
    private String typeNumber;//typeNumber


    @Override
    public int addContentView() {
        return R.layout.activity_load_page;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        mWebView = (WebView) findViewById(R.id.web_load_page);


    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        tv_back_behind.setText(getString(R.string.txt_back));
        tv_title.setText(getString(R.string.xinfeng_more_shuoming));
        llayout_back.setOnClickListener(this);
        if (getIntent().getExtras() != null) {
            bigTypeSn = getIntent().getStringExtra(PARAM_BIG_TYPE_SN);
            typeNumber = getIntent().getStringExtra(PARAM_TYPE_NUMBER);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.loadUrl(UrlConstant.MACHINE_PRODUCT_DES + bigTypeSn + "/" + typeNumber + "/introduction/index.html");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llayout_back) {
            OZApplication.getInstance().finishActivity();
        }
    }
}
