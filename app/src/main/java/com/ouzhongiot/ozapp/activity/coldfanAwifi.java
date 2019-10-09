package com.ouzhongiot.ozapp.activity;

import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;

import java.io.IOException;
import java.net.Socket;

public class coldfanAwifi extends AppCompatActivity {
    private InputMethodManager manager;
    private TextView tv_wifi;
    private EditText et_wifipassword;
    private String wifipassword;
    private String wifiid;
    private Socket socket;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coldfan_awifi);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tv_wifi = (TextView) findViewById(R.id.tv_wifi);
        et_wifipassword = (EditText) findViewById(R.id.et_wifipassword);
        socket = ((OZApplication)getApplication()).socket;

        this.findViewById(R.id.iv_coldfanAwifi_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });
        //立即注册
        this.findViewById(R.id.btn_coldfanAwifi_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifipassword = et_wifipassword.getText().toString();

//                WifiManager wifi_service = (WifiManager) getSystemService(WIFI_SERVICE);
//                WifiInfo wifiInfo = wifi_service.getConnectionInfo();
//                int wifilength = wifiInfo.getSSID().length();
//                wifiid = wifiInfo.getSSID().substring(1,wifilength-1);
//                Log.wtf("wifiid", wifiid);
                if(wifiid.equals("unknown ssid")||wifiid==null||wifiid.equals(""))
//                if (!MainActivity.isConn(coldfanAwifi.this))
                {
//                    Toast.makeText(coldfanAwifi.this,"您没有连接wifi，请连接wifi后重试",Toast.LENGTH_SHORT).show();
                    showNoNetWorkDlg(coldfanAwifi.this);

                }

                else{
                    Bundle bundle = new Bundle();
                    bundle.putString("wifiid",wifiid);
                    bundle.putString("wifipassword",wifipassword);
                    Intent intent = new Intent(coldfanAwifi.this, machineconnect.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }


    /**
     * 当判断当前手机没有网络时选择是否打开网络设置
     *
     * @param context
     */
    public  void showNoNetWorkDlg(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //
        builder.setTitle(R.string.app_name)            //
                .setMessage("连接机器需要您手机连接wifi，是否设置手机连接wifi?").setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    socket.close();
                    ((OZApplication)getApplication()).setSocket(socket);
                    ((OZApplication)getApplication()).setTCPconnect(false);
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
                context.startActivity(intent);
            }
        }).setNegativeButton("知道了", null).show();
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

    //点击空白处回收键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {

        //获取wifi账号
        WifiManager wifi_service = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifi_service.getConnectionInfo();
        int wifilength = wifiInfo.getSSID().length();
        wifiid = wifiInfo.getSSID().substring(1,wifilength-1);
        Log.wtf("wifiid", wifiid);

        //设置wifi账号
        tv_wifi.setText("WIFI账号：" + wifiid);
        super.onResume();
    }
}
