package com.ouzhongiot.ozapp.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;

/**
 * @author hxf
 * @date 创建时间: 2017/5/4
 * @Description
 */

public class PolicyActivity extends BaseHomeActivity {
    private TextView tv_back_behind;//返回后面的字体
    private LinearLayout llayout_back;
    private TextView tv_title;//标题

    @Override
    public int addContentView() {
        return R.layout.activity_policy;
    }

    @Override
    public void initView() {
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        tv_title = (TextView) findViewById(R.id.txt_head_content);

    }

    @Override
    public void initValue() {
        tv_back_behind.setText(getString(R.string.txt_back));
        tv_title.setText(getString(R.string.register_policy_txt));
        llayout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
