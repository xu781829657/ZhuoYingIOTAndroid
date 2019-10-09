package com.ouzhongiot.ozapp.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.tools.IconfontTools;

/**
 * @author hxf
 * @date 创建时间: 2017/4/24
 * @Description 添加设备的设置页
 */

public class AddMachineSetActivity extends BaseHomeActivity implements View.OnClickListener {
    private LinearLayout llayout_back;
    private TextView icon_back;//返回
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private TextView tv_sltype_remind;//按键提醒
    private Button btn_next;//下一步
    private ImageView img_animation;
    private AnimationDrawable animationDrawable;

    public static final String PARAM_MACHINE_NAME = "param_machine_name";
    public static final String PARAM_SL_TYPE = "param_sltype";


    private String machineName;//设备名称
    private String sltype;//SmartLink进入方式



    @Override
    public int addContentView() {
        return R.layout.activity_add_machine_set;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        icon_back = (TextView) findViewById(R.id.font_head_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        tv_sltype_remind = (TextView) findViewById(R.id.tv_sltype_remind1);
        btn_next = (Button) findViewById(R.id.btn_add_machine_set_next);
        img_animation = (ImageView) findViewById(R.id.img_add_machine_set0);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        icon_back.setTypeface(IconfontTools.getTypeface(this));
        tv_back_behind.setText(getString(R.string.txt_back));
        if (getIntent().getExtras() != null) {
            machineName = getIntent().getStringExtra(PARAM_MACHINE_NAME);
            tv_title.setText(machineName);
            sltype = getIntent().getStringExtra(PARAM_SL_TYPE);
            if (sltype.equals("1")) {
                //按定时3s
                tv_sltype_remind.setText("请开机长按定时键3秒");

            } else if (sltype.equals("2")) {
                //开关3s
                tv_sltype_remind.setText("请开机长按开关键3秒");
            } else if (sltype.equals("3")) {
                //wifi3s
                tv_sltype_remind.setText("请开机长按wifi键3秒");

            }
        } else {
            tv_title.setText(getString(R.string.txt_add_machine_set_title));
        }
        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.animation_add_machine_set);
        img_animation.setBackgroundDrawable(animationDrawable);
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }

        //设置点击事件
        setClick();


    }

    public void setClick() {
        llayout_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_machine_set_next:
                //下一步
                Intent intent = new Intent(this, AddMachineWifiActivity.class);
                startActivity(intent);
                OZApplication.getInstance().finishActivity();
                break;
            case R.id.llayout_back:
                //返回
                OZApplication.getInstance().finishActivity();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }
}
