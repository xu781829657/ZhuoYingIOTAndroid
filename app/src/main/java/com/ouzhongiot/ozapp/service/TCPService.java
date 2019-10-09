package com.ouzhongiot.ozapp.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MainActivity;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.others.SetPackage;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.SocketOrderTools;
import com.ouzhongiot.ozapp.tools.SpData;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPService extends Service {

    //    private NetworkInfo netInfo;
//    private ConnectivityManager mConnectivityManager;
    private Socket socket;
    private OutputStream outputStream;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String ISCONNECTED = "";
    private Boolean ISCONNECTING = false;
    private DataInputStream input;

    public TCPService() {
    }

    @Override
    public void onCreate() {

        //网络连接
        preferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = preferences.edit();
        //请求当前状态

        IntentFilter reconnect = new IntentFilter();
        reconnect.addAction("reconnect");
        registerReceiver(receiver2, reconnect);

        IntentFilter heartpackage = new IntentFilter();
        heartpackage.addAction("heartpackage");
        registerReceiver(receiver1, heartpackage);

        super.onCreate();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        //获取一个Notification构造器
        Intent nfIntent = new Intent(this, MainActivity.class);

        builder.setContentIntent(PendingIntent.
                getActivity(this, 0, nfIntent, 0))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon_bule))
                .setSmallIcon(R.mipmap.icon_bule).setContentText("")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("联侠运行中");
        // 设置PendingIntent

        // 设置下拉列表中的图标(大图标)

        // 设置下拉列表里的标题

        // 设置状态栏内的小图标

        // 设置上下文内容

        // 设置该通知发生的时间

        Notification notification = builder.build();
        // 获取构建好的Notification
//        notification.defaults = Notification.DEFAULT_SOUND;
        //设置为默认的声音
        startForeground(0, notification);// 开始前台服务
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private BroadcastReceiver receiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("reconnect")) {
                Log.wtf("收到广播:", "      " + "reconnect" + "   " + 3);
                Connetct();
            }
        }
    };


    private BroadcastReceiver receiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("heartpackage")) {
                Log.wtf("开始心跳包", "      " + "heartpackage" + "   " + 99);
                HeartPackage();
            }
        }
    };


//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            String action = intent.getAction();
//            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//
//                mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//                netInfo = mConnectivityManager.getActiveNetworkInfo();
//                if (netInfo != null && netI.nfo.isAvailable()) {
////                try {
////                    MainActivity.socket.close();
////                    MainActivity.TCPconnect = false;
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//                ///////////网络连接
//                String name = netInfo.getTypeName();
//
//                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                    /////WiFi网络
//                    Log.wtf("网络连接", "网络连接"+name);
//                    Connetct();
//
//                } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
//                    /////有线网络
//                    Log.wtf("网络连接", "网络连接"+name);
//                    Connetct();
//                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//                    //3g
//
//                    Log.wtf("网络连接", "网络连接"+name);
//                    Connetct();
//                }
//            }
//
//
//
//            }
//        }
//    };

    @Override
    public void onDestroy() {
        stopForeground(true);
//        unregisterReceiver(receiver);
        unregisterReceiver(receiver1);
        unregisterReceiver(receiver2);
        super.onDestroy();
    }


    //网络连接
    public void Connetct() {
        socket = ((OZApplication) getApplication()).getSocket();
        outputStream = ((OZApplication) getApplication()).getOutputStream();
        if (socket == null) {
            LogTools.d("socket==null");
            ((OZApplication) getApplication()).setTCPconnect(true);
            Log.wtf("进入重连", "进入4");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket = new Socket(MainActivity.ip0, MainActivity.port);
                        socket.setSoLinger(true, 0);
                        ((OZApplication) getApplication()).setSocket(socket);
                        outputStream = socket.getOutputStream();
                        ((OZApplication) getApplication()).setOutputStream(outputStream);
                        Log.wtf("这个是连接状态1", socket.isClosed() + "  " + socket.isInputShutdown() + "   " + socket.isOutputShutdown() + "   " + socket.isConnected() + "   ");
                        //加入用户
                        if (LogTools.debug) {
                            LogTools.d("加入用户userSn->" + preferences.getString("userSn", "100000001"));
                        }
                        outputStream.write(SetPackage.GetAddConnected(preferences.getString("userSn", "100000001")));
                        outputStream.flush();
                        //加入设备吧
                        if (!SpData.getInstance(getApplicationContext()).getData(SpConstant.MY_MACHINE_ITEM_CLICKED_TYPE_SN).toString().equals("")) {
                            outputStream.write(SocketOrderTools.addDeviceOrder(SpData.getInstance(getApplicationContext()).getData(SpConstant.LOGIN_USERSN).toString(),
                                    SpData.getInstance(getApplicationContext()).getData(SpConstant.MY_MACHINE_ITEM_CLICKED_TYPE_SN).toString(),
                                    SpData.getInstance(getApplicationContext()).getData(SpConstant.MY_MACHINE_ITEM_CLICKED_DEVICE_SN).toString()));
                        }
                        Log.wtf("这个是建立连接", "发送了！！！--------------------------");
//                        if (preferences.getBoolean("xintiaojiesu",true)) {
//                            editor.putBoolean("xintiaojiesu", false);
//                            editor.commit();
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        while (true) {
//                                            if (((OZApplication) getApplication()).getTCPconnect() == false) {
//                                                break;
//                                            }
//                                            ((OZApplication) getApplication()).getOutputStream().write(((OZApplication) getApplication()).getHeartbyte());
//                                            ((OZApplication) getApplication()).getOutputStream().flush();
//                                            Log.wtf("这个是out", ((OZApplication) getApplication()).getHeartbyte() + "" + ((OZApplication) getApplication()).outputStream);
//                                            try {
//                                                Thread.sleep(30000);
//                                            } catch (InterruptedException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }   catch (IOException e) {
//                                        Log.wtf("这个是心跳包异常","0000000000000000000000000000000");
//                                        e.printStackTrace();
//                                    }
//                                    editor.putBoolean("xintiaojiesu", true);
//                                    editor.commit();
//                                    Log.wtf("第一个心跳包结束", "````````````````````````````````````````");
//                                }
//                            }).start();
//                        }

                        //通知发送心跳包
                        Intent heartpackage = new Intent();
                        heartpackage.setAction("heartpackage");
                        sendBroadcast(heartpackage);

                        InputStream inputStream = socket.getInputStream();
                        input = new DataInputStream(inputStream);
                        while (true) {
                            if (((OZApplication) getApplication()).getTCPconnect() == false) {
                                break;
                            }
                            if (((OZApplication) getApplication()).getMsg().equals("QUIT")) {
                                Intent intent = new Intent();
                                intent.setAction("QUIT");
                                intent.putExtra("msg", ((OZApplication) getApplication()).getMsg().substring(0, 4));
                                sendBroadcast(intent);
                                socket.close();
                                socket = null;
                                ((OZApplication) getApplication()).setSocket(socket);
                                ((OZApplication) getApplication()).setTCPconnect(false);
                                Log.wtf("这个是被挤掉", ((OZApplication) getApplication()).getMsg());
                                ((OZApplication) getApplication()).setMsg("");
                                break;
                            }
//                            Log.wtf("退出while","false"+((OZApplication) getApplication()).getMsg());
                            int length = input.read(MainActivity.b);
//                            Log.wtf("这个是收到的数据",MainActivity.b+"长度："+length);

                            if (length == -1) {
                                throw new IOException();
                            } else {
                                ((OZApplication) getApplication()).setMsg(new String(MainActivity.b, 0, length, "utf-8"));
                            }


//                            Log.wtf("data", ((OZApplication) getApplication()).getMsg());
                            switch (((OZApplication) getApplication()).getMsg()) {
                                case "CONNECTED":
                                    //加入机器（先暂时注释掉）
//                                    outputStream.write(SetPackage.GetMachineConnected(preferences.getString("workmachineid", "123456789111"), preferences.getString("userSn", "100000001"), preferences.getString("workmachinetypestring", "A1")));
//                                    outputStream.flush();
                                    ISCONNECTED = ((OZApplication) getApplication()).getMsg();
                                    ISCONNECTING = false;
                                    break;
                                case "QUIT":
//                                    Intent intent = new Intent();
//                                    intent.setAction("QUIT");
//                                    intent.putExtra("msg", ((OZApplication) getApplication()).getMsg().substring(0, 4));
//                                    sendBroadcast(intent);
//                                    socket.close();
//                                    socket = null;
//                                    ((OZApplication) getApplication()).setSocket(socket);
//                                    ((OZApplication) getApplication()).setTCPconnect(false);
//                                    Log.wtf("这个是被挤掉",((OZApplication) getApplication()).getMsg());
                                    break;
                                default:
                                    Intent intentorder = new Intent();
                                    intentorder.setAction("broadcass1");
                                    intentorder.putExtra("msg", ((OZApplication) getApplication()).getMsg());
                                    sendBroadcast(intentorder);
                                    Log.wtf("data", ((OZApplication) getApplication()).getMsg());
                                    break;
                            }
                        }
                    } catch (IOException e) {
                        if (((OZApplication) getApplication()).isLogin()) {
                            Log.wtf("这个是断网状态", "-----------------------------------1");
                            if (socket != null) {
                                try {

                                    socket.close();
                                } catch (IOException e1) {
                                    Log.wtf("这个是100", "-+++++++++++++++++++++++++++++++++++");
                                    e1.printStackTrace();
                                }
                                socket = null;
                                ((OZApplication) getApplication()).setSocket(socket);
                                ((OZApplication) getApplication()).setTCPconnect(false);
                            }

                            if (!ISCONNECTING) {
                                Log.wtf("进入重连循环", "+++++++++++++++++8951122" + ISCONNECTING);
                                ISCONNECTING = true;
                                ISCONNECTED = "DISCONNECTED";

                                while (ISCONNECTED.equals("DISCONNECTED")) {
                                    Log.wtf("正在断网重连", "-----------------------------------2");
                                    Intent intentreconnected = new Intent();
                                    intentreconnected.setAction("reconnect");
                                    sendBroadcast(intentreconnected);
                                    try {
                                        Thread.sleep(10000);
                                    } catch (InterruptedException e1) {
                                        Log.wtf("这个是等待连接异常", "55555555555555555555555555");
                                        e1.printStackTrace();
                                    }
                                }

                            }
                        }
                    }
                }
            }).start();

        } else {
        }

    }

    private void HeartPackage() {

        if (preferences.getBoolean("xintiaojiesu", true)) {
            editor.putBoolean("xintiaojiesu", false);
            editor.commit();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            if (((OZApplication) getApplication()).getTCPconnect() == false) {
                                break;
                            }
                            ((OZApplication) getApplication()).setHeartbyte(SetPackage.GetHeartpackage(preferences.getString("userSn", "100000001")));
                            ((OZApplication) getApplication()).getOutputStream().write(((OZApplication) getApplication()).getHeartbyte());
                            ((OZApplication) getApplication()).getOutputStream().flush();
                            Log.wtf("这个是out", ((OZApplication) getApplication()).getHeartbyte() + "" + ((OZApplication) getApplication()).outputStream);
                            try {
                                Thread.sleep(30000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        Log.wtf("这个是心跳包异常", "0000000000000000000000000000000");
                        e.printStackTrace();
                    }
                    editor.putBoolean("xintiaojiesu", true);
                    editor.commit();
                    Log.wtf("第一个心跳包结束", "````````````````````````````````````````");
                }
            }).start();
        }
    }


}
