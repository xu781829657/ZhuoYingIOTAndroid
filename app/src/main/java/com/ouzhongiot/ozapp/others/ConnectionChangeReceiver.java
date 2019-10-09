//package com.ouzhongiot.ozapp.others;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.util.Log;
//
//import com.ouzhongiot.ozapp.activity.MainActivity;
//
//import java.io.BufferedWriter;
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.Socket;
//
///**
// * Created by liu on 2016/5/27.
// */
//    /**
//     * @author Javen
//     *
//     */
//    public class ConnectionChangeReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//            NetworkInfo mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//            NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
//                Log.wtf("mark", "连接断开");
//
//                try {
//                    MainActivity.socket.close();
//                    MainActivity.TCPconnect = false;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //改变背景或者 处理网络的全局变量
//            }else {
//
//                Log.wtf("mark", "连接中");
//                //改变背景或者 处理网络的全局变量
//            }
//            Log.wtf("mark", "当前网络状态");
//        }
//    }
//
