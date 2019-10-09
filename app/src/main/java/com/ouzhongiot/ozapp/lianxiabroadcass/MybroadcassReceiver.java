package com.ouzhongiot.ozapp.lianxiabroadcass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.activity.LoginActivity;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.tools.SpData;

import java.io.OutputStream;
import java.net.Socket;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by liu on 2016/4/19.
 */
public class MybroadcassReceiver extends BroadcastReceiver{

    public static   byte[] b = {'Q','U','I','T'};
    private OutputStream outputStream;
    private Socket socket = null;
    @Override
    public void onReceive(final Context context, Intent intent) {
        Toast.makeText(context,"您的账号在其他设备登录",Toast.LENGTH_LONG).show();
//        Intent intent1 = new Intent(context,login.class);

        SpData.getInstance(context).putData(SpConstant.LOGIN_PWD, "");
        SpData.getInstance(context).putData(SpConstant.LOGIN_USERNAME, "");
        Intent intent1 = new Intent(context,LoginActivity.class);
        intent1.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
        OZApplication.getInstance().finishAllActivity();
    }

}

