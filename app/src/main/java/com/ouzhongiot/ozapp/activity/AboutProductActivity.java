package com.ouzhongiot.ozapp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.tools.IconfontTools;

/**
 * @author hxf
 * @date 创建时间: 2017/4/12
 * @Description 关于产品
 */

public class AboutProductActivity extends BaseHomeActivity implements View.OnClickListener {
    private LinearLayout llayout_back;
    private TextView icon_back;//返回
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private LinearLayout llayout_product_instructions;//产品说明
    private LinearLayout llayout_online_help;//在线帮助
    private LinearLayout llayout_feedback;//建议反馈
    private LinearLayout llayout_update_log;//更新日志


    @Override
    public int addContentView() {
        return R.layout.activity_about_product;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        icon_back = (TextView) findViewById(R.id.font_head_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        llayout_product_instructions = (LinearLayout) findViewById(R.id.llayout_product_instructions);
        llayout_online_help = (LinearLayout) findViewById(R.id.llayout_online_help);
        llayout_feedback = (LinearLayout) findViewById(R.id.llayout_feedback);
        llayout_update_log = (LinearLayout) findViewById(R.id.llayout_update_log);

    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        icon_back.setTypeface(IconfontTools.getTypeface(this));
        tv_back_behind.setText(getString(R.string.txt_personal));
        tv_title.setText(getString(R.string.txt_about_product));
        //设置点击事件
        setClick();

    }

    public void setClick() {
        llayout_back.setOnClickListener(this);
        llayout_product_instructions.setOnClickListener(this);
        llayout_online_help.setOnClickListener(this);
        llayout_feedback.setOnClickListener(this);
        llayout_update_log.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_back:
                //返回
               OZApplication.getInstance().finishActivity();
                break;
            case R.id.llayout_product_instructions:
                //产品说明
//                startActivity(new Intent(this, chanpinshuoming.class));
                Intent intent = new Intent(this, MachineTypeListActivity.class);
                intent.putExtra(MachineTypeListActivity.PARAM_SWITCH_TYPE,"zhanshi");
                startActivity(intent);
                break;
            case R.id.llayout_online_help:
                //在线帮助
                startActivity(new Intent(this, zaixianbangzhu.class));
                break;
            case R.id.llayout_feedback:
                //建议反馈
              startActivity(new Intent(this,FeedBackActivity.class));
                break;
            case R.id.llayout_update_log:
                //更新日志
               // startActivity(new Intent(this, gengxinrizhi.class));
                break;
        }

    }
}
