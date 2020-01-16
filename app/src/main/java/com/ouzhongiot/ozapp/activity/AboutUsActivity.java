package com.ouzhongiot.ozapp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.tools.IconfontTools;
import com.ouzhongiot.ozapp.tools.SystemTools;
import com.ouzhongiot.ozapp.tools.ToastTools;

/**
 * @author hxf
 * @date 创建时间: 2017/4/12
 * @Description 关于我们
 */

public class AboutUsActivity extends BaseHomeActivity implements View.OnClickListener {
    private LinearLayout llayout_back;
    private TextView icon_back;//返回
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private TextView tv_version_code;//版本号
    private LinearLayout llayout_go_evaluate;//去评价
    private LinearLayout llayout_contact_us;//联系我们
    private LinearLayout llayout_user_agreement;
    private LinearLayout llayout_policy;

    @Override
    public int addContentView() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        icon_back = (TextView) findViewById(R.id.font_head_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        llayout_go_evaluate = (LinearLayout) findViewById(R.id.llayout_go_evaluate);
        llayout_contact_us = (LinearLayout) findViewById(R.id.llayout_contact_us);
        llayout_user_agreement = (LinearLayout) findViewById(R.id.llayout_user_agreement);
        llayout_policy = (LinearLayout) findViewById(R.id.llayout_policy);

    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        icon_back.setTypeface(IconfontTools.getTypeface(this));
        tv_back_behind.setText(getString(R.string.txt_personal));
        tv_title.setText(getString(R.string.txt_personal_about_us));
        tv_version_code.setText("V "+ SystemTools.getAppVersionName(this));
        //设置点击事件
        setClick();

    }

    public void setClick() {
        llayout_back.setOnClickListener(this);
        llayout_go_evaluate.setOnClickListener(this);
        llayout_contact_us.setOnClickListener(this);
        llayout_user_agreement.setOnClickListener(this);
        llayout_policy.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_back:
                //返回
                OZApplication.getInstance().finishActivity();
                break;
            case R.id.llayout_go_evaluate:
                //去评价
                ToastTools.show(this,getString(R.string.txt_go_evaluate));
                break;
            case R.id.llayout_contact_us:
                //联系我们
                startActivity(new Intent(this, ContactUsActivity.class));
                break;
            case R.id.llayout_user_agreement:
                //联系我们
                startActivity(new Intent(this, UserAgreementActivity.class));
                break;
            case R.id.llayout_policy:
                //联系我们
                startActivity(new Intent(this, PolicyActivity.class));
                break;
        }

    }
}
