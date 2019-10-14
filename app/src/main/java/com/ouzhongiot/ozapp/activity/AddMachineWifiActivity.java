package com.ouzhongiot.ozapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.master.permissionhelper.PermissionHelper;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.tools.IconfontTools;
import com.ouzhongiot.ozapp.tools.LogManager;
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
//        //获取wifi账号
//        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        wifiName = wifiInfo.getSSID().substring(1, wifiInfo.getSSID().length() - 1);
//        if (LogTools.debug) {
//            LogTools.i("wifiName->" + wifiName);
//        }

        if (Build.VERSION.SDK_INT >= 28) {
            showRequestPermissionDialog();
            //requestPOSPermission();
        } else {
            setWifiName();
        }

        //设置点击事件
        setClick();

    }

    private void showRequestPermissionDialog(){
        //创建dialog构造器
        final AlertDialog.Builder dialog  = new AlertDialog.Builder(this);
        //设置title
        dialog.setTitle("提示");
        //设置内容
        dialog.setMessage("9.0以上系统获取wifi名称,需要开启定位权限");
        //设置按钮
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("去授权"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPOSPermission();
                        dialog.dismiss();
                    }
                });
        //创建并显示
        dialog.create().show();
  }

    public void setWifiName(){
        wifiName = getWIFISSID(this);
        LogManager.d("wifiName2:" + wifiName);
        //设置wifi账号
        tv_wifi_name.setText(wifiName);
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


    /**
     * 获取SSID
     * https://blog.csdn.net/Marvinhq/article/details/83957553
     * @param activity 上下文
     * @return  WIFI 的SSID
     */
    public String getWIFISSID(Activity activity) {
        String ssid="unknown id";
        LogManager.d("getWIFISSID:"+Build.VERSION.SDK_INT);
        //<8.0(26) || 9.0(28)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O || Build.VERSION.SDK_INT == 28) {

            WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            assert mWifiManager != null;
            WifiInfo info = mWifiManager.getConnectionInfo();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return info.getSSID();
            } else {
                return info.getSSID().replace("\"", "");
            }
        } else if (Build.VERSION.SDK_INT == 27) {//8.1

            ConnectivityManager connManager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo.isConnected()) {
                if (networkInfo.getExtraInfo() != null) {
                    return networkInfo.getExtraInfo().replace("\"", "");
                }
            }
        }


        return ssid;
    }

    private PermissionHelper permissionHelper;

    private void requestPOSPermission() {
        permissionHelper = new PermissionHelper(AddMachineWifiActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                LogManager.d("onPermissionGranted");
                setWifiName();
            }

            @Override
            public void onIndividualPermissionGranted(String[] grantedPermission) {
                LogManager.d("onIndividualPermissionGranted");
            }

            @Override
            public void onPermissionDenied() {
                LogManager.d("onPermissionDenied");
                Toast.makeText(AddMachineWifiActivity.this, "权限被拒绝，9.0系统无法获取SSID", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDeniedBySystem() {
                LogManager.d("onPermissionDeniedBySystem");
                Toast.makeText(AddMachineWifiActivity.this, "权限被拒绝，9.0系统无法获取SSID", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
