package com.ouzhongiot.ozapp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.tools.IconfontTools;

/**
 * @author hxf
 * @date 创建时间: 2017/4/24
 * @Description 添加设备失败
 */

public class AddMachineFailActivity extends BaseHomeActivity implements View.OnClickListener {
    private LinearLayout llayout_back;
    private TextView icon_back;//返回
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private Button btn_retry;//重试
    private Button btn_feedback;//反馈

    @Override
    public int addContentView() {
        return R.layout.activity_add_machine_connect_fail;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        icon_back = (TextView) findViewById(R.id.font_head_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        btn_retry = (Button) findViewById(R.id.btn_add_machine_fail_retry);
        btn_feedback = (Button) findViewById(R.id.btn_add_machine_fail_feedback);

    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        icon_back.setTypeface(IconfontTools.getTypeface(this));
        tv_back_behind.setText(getString(R.string.txt_back));
        tv_title.setText(getString(R.string.txt_add_machine_fail));
        //设置点击事件
        setClick();

    }

    public void setClick() {
        llayout_back.setOnClickListener(this);
        btn_retry.setOnClickListener(this);
        btn_feedback.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_back:
                //返回
                OZApplication.getInstance().finishActivity();
                break;
            case R.id.btn_add_machine_fail_retry:
                //重试
                Intent intent = new Intent(this, MachineTypeListActivity.class);
                intent.putExtra(MachineTypeListActivity.PARAM_SWITCH_TYPE,"add");
                startActivity(intent);
                OZApplication.getInstance().finishActivity();
                break;
            case R.id.btn_add_machine_fail_feedback:
                //反馈
                break;
        }

    }
}
