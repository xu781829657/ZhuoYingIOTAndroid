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
 * Created by admin on 2017/4/14.
 */

public class MessageNotificationActivity2 extends BaseHomeActivity implements View.OnClickListener {
    private LinearLayout llayout_back;
    private TextView icon_back;//返回
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private LinearLayout llayout_system;//系统消息
    private LinearLayout llayout_mine;//我的消息
    private TextView font_system_dot;
    private TextView font_mine_dot;



    public static final String PARAM_SYSTEM_NOTI_FLAG = "param_system_noti_flag";
    private boolean flag_sys_noti;//标识是否有系统通知

    @Override
    public int addContentView() {
        return R.layout.activity_message_notification2;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        icon_back = (TextView) findViewById(R.id.font_head_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        llayout_system = (LinearLayout) findViewById(R.id.llayout_message_system);
        llayout_mine = (LinearLayout) findViewById(R.id.llayout_message_mine);
        font_system_dot = (TextView) findViewById(R.id.font_message_system_dot);
        font_mine_dot = (TextView) findViewById(R.id.font_message_mine_dot);


    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        icon_back.setTypeface(IconfontTools.getTypeface(this));
        font_system_dot.setTypeface(IconfontTools.getTypeface(this));
        font_mine_dot.setTypeface(IconfontTools.getTypeface(this));
        tv_back_behind.setText(getString(R.string.txt_personal));
        tv_title.setText(getString(R.string.txt_personal_message_notification));
        if (getIntent().getExtras() != null) {
            flag_sys_noti = getIntent().getBooleanExtra(PARAM_SYSTEM_NOTI_FLAG, false);
            if (flag_sys_noti) {
                //系统通知显示红点
                font_system_dot.setVisibility(View.VISIBLE);
            } else {
                font_system_dot.setVisibility(View.GONE);
            }
        }

        //设置点击事件
        setClick();

    }

    public void setClick() {
        llayout_back.setOnClickListener(this);
        llayout_system.setOnClickListener(this);
        llayout_mine.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_back:
                //返回
                OZApplication.getInstance().finishActivity();
                break;
            case R.id.llayout_message_system:
                //系统消息
                if (flag_sys_noti){
                    font_system_dot.setVisibility(View.GONE);
                    //消息通知处的红点
                    setResult(RESULT_OK);
                }
                startActivity(new Intent(this, SystemNotificationActivity.class));
                break;
            case R.id.llayout_message_mine:
                //我的消息
                startActivity(new Intent(this, MineMessageActivity.class));
                break;
        }

    }
}
