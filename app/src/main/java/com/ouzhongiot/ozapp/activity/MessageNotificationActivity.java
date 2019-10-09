package com.ouzhongiot.ozapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.tools.IconfontTools;

/**
 * @author hxf
 * @date 创建时间: 2017/2/5
 * @Description 消息通知
 */
public class MessageNotificationActivity extends AppCompatActivity implements View.OnClickListener {
    //    private ImageView img_back;
    private TextView font_back;
    private TextView font_system_notification;
    private TextView font_mine_message;
    private TextView font_system_notification_dot;
    private TextView font_mine_message_dot;
    private TextView font_system_notification_enter;
    private TextView font_mine_message_enter;
    private LinearLayout llayout_system_notification;
    private LinearLayout llayout_mine_message;

    public static final String PARAM_SYSTEM_NOTI_FLAG = "param_system_noti_flag";
    private boolean flag_sys_noti;//标识是否有系统通知


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_notification);
        initView();
        initValue();
    }

    //初始化View
    public void initView() {
//        img_back = (ImageView) findViewById(R.id.img_message_notification_back);
        font_back = (TextView) findViewById(R.id.font_message_notification_back);
        font_system_notification = (TextView) findViewById(R.id.font_system_notification);
        font_mine_message = (TextView) findViewById(R.id.font_mine_message);
        font_system_notification_dot = (TextView) findViewById(R.id.font_system_notification_dot);
        font_mine_message_dot = (TextView) findViewById(R.id.font_mine_message_dot);
        font_system_notification_enter = (TextView) findViewById(R.id.font_system_notification_enter);
        font_mine_message_enter = (TextView) findViewById(R.id.font_mine_message_enter);
        llayout_system_notification = (LinearLayout) findViewById(R.id.llayout_system_notification);
        llayout_mine_message = (LinearLayout) findViewById(R.id.llayout_mine_message);

    }

    //初始化Value
    public void initValue() {
        if (getIntent().getExtras() != null) {
            flag_sys_noti = getIntent().getBooleanExtra(PARAM_SYSTEM_NOTI_FLAG, false);
            if (flag_sys_noti) {
                //系统通知显示红点
                font_system_notification_dot.setVisibility(View.VISIBLE);
            } else {
                font_system_notification_dot.setVisibility(View.GONE);
            }
        }
        Typeface typeface = IconfontTools.getTypeface(this);
        font_back.setTypeface(typeface);
        font_system_notification.setTypeface(typeface);
        font_mine_message.setTypeface(typeface);
        font_system_notification_dot.setTypeface(typeface);
        font_mine_message_dot.setTypeface(typeface);
        font_system_notification_enter.setTypeface(typeface);
        font_mine_message_enter.setTypeface(typeface);
//        img_back.setOnClickListener(this);
        font_back.setOnClickListener(this);
        llayout_system_notification.setOnClickListener(this);
        llayout_mine_message.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.img_message_notification_back:
//                //返回
//                onBackPressed();
//                break;
            case R.id.font_message_notification_back:
                //返回
                onBackPressed();
                break;
            case R.id.llayout_system_notification:
                //系统通知,如果之前本页面和消息通知出有红点，就要不显示
                if (flag_sys_noti){
                    font_system_notification_dot.setVisibility(View.GONE);
                    //消息通知处的红点
                    setResult(RESULT_OK);
                }
                startActivity(new Intent(this, SystemNotificationActivity.class));
                break;
            case R.id.llayout_mine_message:
                //我的消息
                startActivity(new Intent(this, MineMessageActivity.class));
                break;
        }
    }

}
