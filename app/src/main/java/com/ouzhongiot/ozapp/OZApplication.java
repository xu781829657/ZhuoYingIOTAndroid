package com.ouzhongiot.ozapp;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Vibrator;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.baidu.location.service.LocationService;
import com.baidu.location.service.WriteLog;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ouzhongiot.ozapp.Model.machine;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.tools.SocketOrderTools;
import com.ouzhongiot.ozapp.tools.SpData;
import com.ouzhongiot.ozapp.tools.SystemTools;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Created by liu on 2016/10/25.
 */
public class OZApplication  extends Application{
    public OutputStream outputStream;
    //public LocationService locationService;
    public Vibrator mVibrator;
    public byte[] heartbyte = new byte[8];
    public  String msg = "";
    public Boolean TCPconnect = true;
    public Socket socket = null;
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;

    public Boolean getISTOUXIANGUPDATE() {
        return ISTOUXIANGUPDATE;
    }

    public void setISTOUXIANGUPDATE(Boolean ISTOUXIANGUPDATE) {
        this.ISTOUXIANGUPDATE = ISTOUXIANGUPDATE;
    }

    public Boolean ISTOUXIANGUPDATE = false;
    public  RequestQueue mQueue;

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public  ImageLoader imageLoader;
    public List<machine> getData() {
        return data;
    }

    public void setData(List<machine> data) {
        this.data = data;
    }

    public List<machine> data = new ArrayList<machine>();
    private static Stack<Activity> activityStack;// 一个Activity类
    private static OZApplication instance;

    public boolean isLogin = false;//判断用户是否处于登录状态

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //网络连接
        preferences = getSharedPreferences("data",MODE_PRIVATE);
        editor  = preferences.edit();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
       // locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        WriteLog.getInstance().init(); // 初始化日志
        SDKInitializer.initialize(getApplicationContext());
//        mQueue  = Volley.newRequestQueue(this);
//        imageLoader  = new ImageLoader(mQueue, new BitmapCache(){
//            @Override
//            public void putBitmap(String url, Bitmap bitmap) {
//            }
//
//            @Override
//            public Bitmap getBitmap(String url) {
//                return null;
//            }
//        });
        initImageLoader(getApplicationContext());


        CrashReport.initCrashReport(getApplicationContext(), "1c88c987fb", false);

    }

    @Override
    public void onTerminate() {

        super.onTerminate();
    }

    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public OutputStream getOutputStream() {
        return outputStream;
    }
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
    public Boolean getTCPconnect() {
        return TCPconnect;
    }
    public void setTCPconnect(Boolean TCPconnect) {
        this.TCPconnect = TCPconnect;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public byte[] getHeartbyte() {
        return heartbyte;
    }
    public void setHeartbyte(byte[] heartbyte) {
        this.heartbyte = heartbyte;
    }

    public void SendOrder(final byte[] b) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (getSharedPreferences("data", MODE_PRIVATE).getBoolean("xintiaojiesu", true)) {
                        //需要在有网的情况下，否则new socket不成功 然后前面那个置空了就会报空指针
                        if (SystemTools.isNetWorkConnected(getApplicationContext())) {

                            socket.close();
                            socket = null;
                            TCPconnect = false;
                            outputStream = null;

                            Intent intentreconnected = new Intent();
                            intentreconnected.setAction("reconnect");
                            sendBroadcast(intentreconnected);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //要先发送加入设备指令
                            getOutputStream().write(SocketOrderTools.addDeviceOrder(SpData.getInstance(getApplicationContext()).getData(SpConstant.LOGIN_USERSN).toString(),
                                    SpData.getInstance(getApplicationContext()).getData(SpConstant.MY_MACHINE_ITEM_CLICKED_TYPE_SN).toString(),
                                    SpData.getInstance(getApplicationContext()).getData(SpConstant.MY_MACHINE_ITEM_CLICKED_DEVICE_SN).toString()));
                            getOutputStream().flush();
                            getOutputStream().write(b);
                            getOutputStream().flush();

                        } else {
                            Intent intent = new Intent();
                            intent.setAction("UNCONNECTED");
                            sendBroadcast(intent);
                        }
                    } else {
                        getOutputStream().write(b);
                        getOutputStream().flush();
                    }
                } catch (IOException e) {
                    Intent intent = new Intent();
                    intent.setAction("UNCONNECTED");
                    sendBroadcast(intent);
                    e.printStackTrace();
                }

            }
        }).start();
    }


    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        //先把UTL/cache目录下的缓存清空
//        SystemTools.deleteDirectory(Environment.getExternalStorageDirectory() + "/UIL/cache");
        File cacheDir = new File(Environment.getExternalStorageDirectory() + "/UIL/cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(5)
                .threadPriority(Thread.MIN_PRIORITY + 3)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .writeDebugLogs()
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    /**
     * 单例
     */
    public static OZApplication getInstance() {
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() { // 防止反应慢的异常的处理
        try {
            Activity activity = activityStack.lastElement();
            finishActivity(activity);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

}
