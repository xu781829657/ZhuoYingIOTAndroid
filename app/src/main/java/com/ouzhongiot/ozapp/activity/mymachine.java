package com.ouzhongiot.ozapp.activity;


import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.fragment.PersonalFragment;
import com.ouzhongiot.ozapp.others.LoginOutActivity;


public class mymachine extends LoginOutActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Fragment mymachine, gerenzhongxin;
    private TextView tv_wodeshebei, tv_gerenzhongxin;
    private ImageView iv_wodeshebei, iv_gerenzhongxin;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //我的设备
                    iv_wodeshebei.setImageResource(R.mipmap.wodeshebeixuanzhong);
                    tv_wodeshebei.setTextColor(getResources().getColor(R.color.green));
                    iv_gerenzhongxin.setImageResource(R.mipmap.gerenzhongxinweixuanzhong);
                    tv_gerenzhongxin.setTextColor(getResources().getColor(R.color.gray));
                    break;
                case 1:
                    //个人中心
                    iv_wodeshebei.setImageResource(R.mipmap.wodeshebeiweixuanzhong);
                    tv_wodeshebei.setTextColor(getResources().getColor(R.color.gray));
                    iv_gerenzhongxin.setImageResource(R.mipmap.gerenzhongxinxuanzhong);
                    tv_gerenzhongxin.setTextColor(getResources().getColor(R.color.green));
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymachine);

        Intent intentreconnected = new Intent();
        intentreconnected.setAction("reconnect");
        sendBroadcast(intentreconnected);

        tv_gerenzhongxin = (TextView) findViewById(R.id.tv_gerenzhongxin);
        tv_wodeshebei = (TextView) findViewById(R.id.tv_wodeshebei);
        iv_gerenzhongxin = (ImageView) findViewById(R.id.iv_gerenzhongxin);
        iv_wodeshebei = (ImageView) findViewById(R.id.iv_wodeshebei);


        //网络连接
        preferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = preferences.edit();


        FragmentManager fragmentManager = getSupportFragmentManager();
        gerenzhongxin = fragmentManager.findFragmentById(R.id.fg_gerenzhongxin);
        mymachine = fragmentManager.findFragmentById(R.id.fg_mymachine);


        this.findViewById(R.id.ll_wodeshebei).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchContent(gerenzhongxin, mymachine);
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        });


        this.findViewById(R.id.ll_gerenzhongxin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gerenzhongxin = new PersonalFragment();
                switchContent(mymachine, gerenzhongxin);
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });


    }


    @Override
    protected void onResume() {

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


    public void switchContent(Fragment from, Fragment to) {
        if (!to.isAdded()) {    // 先判断是否被add过
            if (to.getId() == R.id.fg_gerenzhongxin) {
                getSupportFragmentManager().beginTransaction().hide(from).add(R.id.fg_gerenzhongxin, to).commitAllowingStateLoss();
            } else {
                getSupportFragmentManager().beginTransaction().hide(from).add(R.id.fg_mymachine, to).commitAllowingStateLoss();
            }
            // 隐藏当前的fragment，add下一个到Activity中
        } else {
            getSupportFragmentManager().beginTransaction().hide(from).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
        }
    }


}






