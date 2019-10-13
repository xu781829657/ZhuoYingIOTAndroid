package com.ouzhongiot.ozapp.activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.others.AndroidShare;
import com.ouzhongiot.ozapp.others.CircleImageView;
import com.ouzhongiot.ozapp.others.LoginOutActivity;


public class coldfanAyonghuzhongxin extends LoginOutActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String userSn;
    private CircleImageView yonghuzhongxin_touxiang;
    private TextView tv_yonghuzhongxin_nicheng,tv_yonghuzhongxin_ID;
    private LinearLayout ll_share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coldfan_ayonghuzhongxin);
        ll_share = (LinearLayout) findViewById(R.id.ll_share);

        tv_yonghuzhongxin_nicheng = (TextView) findViewById(R.id.tv_yonghuzhongxin_nicheng);
        tv_yonghuzhongxin_ID = (TextView) findViewById(R.id.tv_yonghuzhongxin_ID);
        yonghuzhongxin_touxiang = (CircleImageView) findViewById(R.id.yonghuzhongxin_touxiang);
        preferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = preferences.edit();
        userSn = preferences.getString("userSn", "");
        tv_yonghuzhongxin_nicheng.setText(preferences.getString("nickname","昵称"));
        tv_yonghuzhongxin_ID.setText(userSn);

        //分享
        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidShare as = new AndroidShare(
                        coldfanAyonghuzhongxin.this,
                        "微新风——让智能变得简单...",
                        "www.ouzhongiot.com");
                as.show();
            }
        });

        //个人信息
        this.findViewById(R.id.gerenxinxi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                                Intent intent = new Intent(coldfanAyonghuzhongxin.this,gerenxinxi.class);
                                startActivity(intent);


                    }
                });


        //返回按钮
        this.findViewById(R.id.iv_yonghuzhongxin_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        //添加设备
        this.findViewById(R.id.yonghuzhongxin_addmachine).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                       Intent intent = new Intent(coldfanAyonghuzhongxin.this,TypeList.class);
//                        startActivity(intent);
//                       overridePendingTransition(R.anim.activity_open,R.anim.activity_close_donothing);
                        Intent intent = new Intent(coldfanAyonghuzhongxin.this,coldfanset.class);
                        startActivity(intent);
                    }
                });

        //我的设备
        this.findViewById(R.id.yonghuzhongxin_mymachine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(coldfanAyonghuzhongxin.this,mymachine.class);
                startActivity(intent);
            }
        });

        //消息通知
        this.findViewById(R.id.yonghuzhongxin_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(coldfanAyonghuzhongxin.this,mymachine.class);
//                startActivity(intent);
            }
        });

        //消息通知
        this.findViewById(R.id.yonghuzhongxin_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(coldfanAyonghuzhongxin.this,MessageNotificationActivity.class);
                startActivity(intent);
            }
        });

        //优惠券
        this.findViewById(R.id.yonghuzhongxin_youhuiquan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(coldfanAyonghuzhongxin.this,youhuiquan.class);
                startActivity(intent);
            }
        });
        //关于产品
        this.findViewById(R.id.yonghuzhongxin_aboutproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(coldfanAyonghuzhongxin.this,aboutproduct.class);
                startActivity(intent);
            }
        });
        //联系我们
        this.findViewById(R.id.yonghuzhongxin_lianxiwomen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(coldfanAyonghuzhongxin.this,lianxiwomen.class);
                startActivity(intent);
            }
        });
        //更多产品
        this.findViewById(R.id.yonghuzhongxin_gengduochanpin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(coldfanAyonghuzhongxin.this,TypeList.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        //设置缓存
        super.onResume();
    }



    //点击返回上个页面
    public void onBack() {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                } catch (Exception e) {
                    Log.e("Exception when onBack", e.toString());
                }
            }
        }.start();
    }


}






