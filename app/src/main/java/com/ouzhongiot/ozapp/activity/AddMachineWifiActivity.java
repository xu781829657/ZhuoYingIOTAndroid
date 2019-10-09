package com.ouzhongiot.ozapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.tools.IconfontTools;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.ToastTools;

import java.io.IOException;
import java.net.Socket;

/**
 * @author hxf
 * @date 创建时间: 2017/4/24
 * @Description 添加设备设置wifi
 */

public class AddMachineWifiActivity extends BaseHomeActivity implements View.OnClickListener {
    private LinearLayout llayout_back;
    private TextView icon_back;//返回
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private Button btn_search;//搜索设备
    private TextView tv_wifi_name;//wifi名
    private EditText edt_wifi_pwd;//wifi密码
    private String wifiName;

    private Socket socket;


    @Override
    public int addContentView() {
        return R.layout.activity_add_machine_wifi;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        icon_back = (TextView) findViewById(R.id.font_head_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        btn_search = (Button) findViewById(R.id.btn_add_machine_wifi_search);
        tv_wifi_name = (TextView) findViewById(R.id.tv_add_machine_wifi_name);
        edt_wifi_pwd = (EditText) findViewById(R.id.edt_add_machine_wifi);


    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        icon_back.setTypeface(IconfontTools.getTypeface(this));
        tv_back_behind.setText(getString(R.string.txt_back));
        tv_title.setText(getString(R.string.txt_add_machine_wifi_title));
        socket = ((OZApplication) getApplication()).socket;
        //获取wifi账号
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        wifiName = wifiInfo.getSSID().substring(1, wifiInfo.getSSID().length() - 1);
        if (LogTools.debug) {
            LogTools.i("wifiName->" + wifiName);
        }
        //设置wifi账号
        tv_wifi_name.setText(wifiName);
        //设置点击事件
        setClick();


    }

    public void setClick() {
        llayout_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_back:
                //返回
                OZApplication.getInstance().finishActivity();
                break;
            case R.id.btn_add_machine_wifi_search:
                verify();
                break;
        }
    }

    public void verify() {
        if (wifiName.equals("unknown ssid") || wifiName == null || wifiName.equals("")) {
            showNotNetwork();
        } else if (edt_wifi_pwd.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, getString(R.string.txt_add_machine_wifi_pwd_hint));
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("wifiid", wifiName);
            bundle.putString("wifipassword", edt_wifi_pwd.getText().toString());
            Intent intent = new Intent(this, machineconnect.class);
            intent.putExtras(bundle);
            startActivity(intent);
            OZApplication.getInstance().finishActivity();
        }

    }

    //显示没有wifi的Dialog
    public void showNotNetwork() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
                .setMessage(getString(R.string.txt_add_machine_wifi_nonetwork_hint)).setPositiveButton(getString(R.string.txt_add_machine_wifi_nonetwork_set), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (socket != null) {
                        socket.close();
                        ((OZApplication) getApplication()).setSocket(socket);
                        ((OZApplication) getApplication()).setTCPconnect(false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = null;
                // 先判断当前系统版本
                if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                }
                startActivity(intent);
            }
        }).setNegativeButton(getString(R.string.txt_add_machine_wifi_nonetwork_know), null).show();
    }
}
