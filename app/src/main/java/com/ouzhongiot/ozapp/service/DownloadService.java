package com.ouzhongiot.ozapp.service;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.others.FileTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class DownloadService extends Service {
    private static final int NOTIFY_ID = 0;

    /**
     * 进度条的进度值
     */
    private int progress = 0;

    private Intent intent = new Intent("Download_RECEIVER");

    /**
     * 增加get()方法，供Activity调用
     * @return 下载进度
     */
    public int getProgress() {
        return progress;
    }
    private NotificationManager mNotificationManager;
    private boolean canceled;
    // 返回的安装包url
    private String apkUrl;// ="http://softfile.3g.qq.com:8080/msoft/179/24659/43549/qq_hd_mini_1.4.apk";
    // private String apkUrl = MyApp.downloadApkUrl;
	/* 下载包安装路径 */
    private String savePath;// = "/sdcard/updateApkDemo/";

    private String saveFileName;// = savePath + "3GQQ_AppUpdate.apk";
//    Notification.Builder builder = new Notification.Builder(this);
    private Context mContext = this;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    // app.setDownload(false);
                    // 下载完毕
                    // 取消通知
                    Log.wtf("下载完毕0","--------------------------");
                    mNotificationManager.cancel(NOTIFY_ID);
                    installApk();
                    break;
                case 2:
                    // app.setDownload(false);
                    // 这里是用户界面手动取消，所以会经过activity的onDestroy();方法
                    // 取消通知
                    Log.wtf("取消下载2","--------------------------");
                    mNotificationManager.cancel(NOTIFY_ID);
                    break;
                case 1:


                    int rate = msg.arg1;
                    // app.setDownload(true);
                    if (rate < 100) {
//                        Log.wtf("进度条1",rate+"--------------------------");
                        progress = rate;
                        //发送Action为com.example.communication.RECEIVER的广播
                        intent.putExtra("progress", progress);
                        sendBroadcast(intent);
////                        RemoteViews contentview = mNotification.contentView;
//                        contentview.setTextViewText(R.id.tv_progress, rate + "%");
//                        contentview.setProgressBar(R.id.progressbar, 100, rate,
//                                false);
                    } else {
                        // System.out.println("下载完毕!!!!!!!!!!!");
                        // 下载完毕后变换通知形式
//                        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
//                        mNotification.contentView = null;
                        // Intent intent = new Intent(mContext,
                        // FragmentChangeActivity.class);
                        Intent intent = new Intent();
                        // // 告知已完成
                        // intent.putExtra("completed", "yes");
                        // 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
                        PendingIntent contentIntent = PendingIntent.getActivity(
                                mContext, 0, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
//                        mNotification.setLatestEventInfo(mContext, "下载完成",
//                                "文件已下载完毕", contentIntent);
//                         serviceIsDestroy = true;

//                        NotificationManager manager = (NotificationManager) Context.getSystemService(Context.NOTIFICATION_SERVICE);
//                        //API level 11
//                        Notification.Builder builder = new Notification.Builder(context);
//                        builder.setContentTitle("Bmob Test");
//                        builder.setContentText(MessageNotificationActivity);
//                        builder.setSmallIcon(R.drawable.ic_launcher);
//                        Notification notification = builder.getNotification();
//                        manager.notify(R.drawable.ic_launcher, notification);


                        stopSelf();// 停掉服务自身 这样就不需要在其它代码中进行停止了
                    }
//                    mNotificationManager.notify(NOTIFY_ID, mNotification);
                    break;
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        // System.out.println("onStartCommand is start！");

        if (intent != null) {
            apkUrl = intent.getStringExtra("apkurl");
            savePath = FileTool.getPath2(mContext, "SavaAPK");
            saveFileName = savePath + "微新风智能.apk";
            startDownLoadAPK();
        }

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // System.out.println("downloadservice ondestroy");
        // 假如被销毁了，无论如何都默认取消了。
        // app.setDownload(false);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        // System.out.println("downloadservice onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        // TODO Auto-generated method stub
        super.onRebind(intent);
        // System.out.println("downloadservice onRebind");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        // binder = new DownloadBinder();
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        // setForeground(true);// 这个不确定是否有作用
        // app = (MyApp) getApplication();
    }

    public void startDownLoadAPK() {
        if (downLoadThread == null || !downLoadThread.isAlive()) {


//            setUpNotification();
            new Thread() {
                public void run() {
                    // 下载
                    startDownload();
                };
            }.start();
        }
    }

    private void startDownload() {
        // TODO Auto-generated method stub
        canceled = false;
        downloadApk();
    }

//    Notification mNotification;

    // 通知栏
    /**
     * 创建通知
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setUpNotification() {
//        int icon = R.drawable.icon;
//        CharSequence tickerText = "开始下载";
//        long when = System.currentTimeMillis();
//        mNotification = new Notification(icon, tickerText, when);

//        builder.setSmallIcon(R.drawable.icon); //设置图标
//        builder.setTicker("显示第二个通知");
//        builder.setContentTitle("通知"); //设置标题
//        builder.setContentText("点击查看详细内容"); //消息内容
//        builder.setWhen(System.currentTimeMillis()); //发送时间
//        builder.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
//        builder.setAutoCancel(true);//打开程序后图标消失
////        Intent intent =new Intent (MainActivity.this,Center.class);
////        PendingIntent pendingIntent =PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
////        builder.setContentIntent(pendingIntent);
//        Notification notification1 = builder.build();


//        notificationManager.notify(124, notification1); // 通过通知管理器发送通知
//        // 放置在"正在运行"栏目中
//        mNotification.flags = Notification.FLAG_ONGOING_EVENT;

        RemoteViews contentView = new RemoteViews(getPackageName(),
                R.layout.download_notification_layout);
        contentView.setTextViewText(R.id.name, "apk正在下载...");
        // 指定个性化视图
//        mNotification.contentView = contentView;

        // Intent intent = new Intent(this, FragmentChangeActivity.class); //

        Intent intent = new Intent();
        // 下面两句是
        // 在按home后，点击通知栏，返回之前activity
        // 状态;
        // 有下面两句的话，假如service还在后台下载， 在点击程序图片重新进入程序时，直接到下载界面，相当于把程序MAIN 入口改了 - -
        // 是这么理解么。。。
        // intent.setAction(Intent.ACTION_MAIN);
        // intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        // 指定内容意图
//        mNotification.contentIntent = contentIntent;
//        mNotificationManager.notify(NOTIFY_ID, mNotification);
    }

    //
    /**
     * 下载apk
     *
     * @param url
     */
    private Thread downLoadThread;

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }


    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }

    private int lastRate = 0;
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    msg.arg1 = progress;
                    if (progress >= lastRate + 1) {
                        mHandler.sendMessage(msg);
                        lastRate = progress;
                        // if (callback != null)
                        // callback.OnBackResult(progress);
                    }
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(0);
                        // 下载完了，cancelled也要设置
                        canceled = true;
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!canceled);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}

