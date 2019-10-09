package com.ouzhongiot.ozapp.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.tools.IconfontTools;

/**
 * @author hxf
 * @date 创建时间: 2017/2/6
 * @Description 我的消息
 */

public class MineMessageActivity extends Activity implements View.OnClickListener {
    private TextView font_back;
    private TextView txt_head_content;
    private TextView txt_head_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_message);
        initView();
        initValue();
    }

    public void initView() {
        font_back = (TextView) findViewById(R.id.font_head_back);
        txt_head_content = (TextView) findViewById(R.id.txt_head_content);
        txt_head_right = (TextView) findViewById(R.id.txt_head_right);

    }

    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        Typeface typeface = IconfontTools.getTypeface(this);
        font_back.setTypeface(typeface);
        txt_head_content.setText(getString(R.string.txt_message_mine));
        txt_head_right.setVisibility(View.GONE);
        font_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.font_head_back) {
            //返回
            OZApplication.getInstance().finishActivity();
        }

    }
}
