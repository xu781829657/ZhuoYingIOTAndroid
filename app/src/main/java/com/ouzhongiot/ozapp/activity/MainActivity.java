package com.ouzhongiot.ozapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.baidu.location.service.LocationService;
import com.igexin.sdk.PushManager;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.http.ConnectDataTask;
import com.ouzhongiot.ozapp.http.HcNetWorkTask;
import com.ouzhongiot.ozapp.http.PostParamTools;
import com.ouzhongiot.ozapp.service.DownloadService;
import com.ouzhongiot.ozapp.service.PushIntentService;
import com.ouzhongiot.ozapp.service.TCPService;
import com.ouzhongiot.ozapp.tools.DensityUtil;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.SpData;
import com.ouzhongiot.ozapp.tools.ToastTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ConnectDataTask.OnResultDataLintener {

    private SharedPreferences preference;

    private SharedPreferences.Editor editor;
    public static final String ip = "http://114.55.5.92:8080/";
    public static final String ip0 = "114.55.5.92";
//    public static final String ip = "http://192.168.1.104:8080/";
//    public static final String ip0 = "192.168.1.104";

    public static final int port = 6003;
    //    public static Boolean TCPconnect = true;
//    public static Socket socket = null;
    public static PrintWriter out = null;
    public static BufferedReader br = null;
    //    public static String heartpakage = "HM*A*123456789111*100000014*#";
//    public static String msg = null;
    private String workmachineid;
    private String userId, userSn;
    //    private List<logindata.DataBean.UserDeviceBean> userDevice;
    private LocationService locationService;
    public static String city = null;
    public static String district = null;
    public static String province = null;
    private ImageView iv_main, iv_mainwenzi;
    //    public static InputStream inputStream;
    public static DataInputStream dataInputStream;
    //    public static OutputStream outputStream;
    public String version;
    public String isForce = "false";
    public String versionid;
    private AlertDialog.Builder builder;
    //    public static byte[] heartbyte = new byte[15];
    public static int dangqianbanbeng = 53;
    public static byte[] b = new byte[40];
    public static String getuiClientid;
    private DownloadService msgService;
    private int downloadprogress = 0;
    private int logoweight;
    private LinearLayout ll_main_xiazai;
    private TextView tv_main_xiazaijindu;
    private ImageView iv_download_logo, iv_download_back;
    private Boolean ISloading = false;


    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OZApplication.getInstance().addActivity(this);
        initWidgets();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });

    }
    private void initWidgets(){
        ll_main_xiazai = (LinearLayout) findViewById(R.id.ll_main_xiazai);
        tv_main_xiazaijindu = (TextView) findViewById(R.id.tv_main_xiazaijindu);
        iv_download_back = (ImageView) findViewById(R.id.iv_download_back);
        iv_download_logo = (ImageView) findViewById(R.id.iv_download_logo);
        iv_main = (ImageView) findViewById(R.id.iv_main);
        iv_mainwenzi = (ImageView) findViewById(R.id.iv_mainwenzi);
    }

    private void initData(){
        //初始化个推SDK
        PushManager.getInstance().initialize(this.getApplicationContext(), com.ouzhongiot.ozapp.service.PushService.class);
        //注册PushIntentService类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), PushIntentService.class);
        getuiClientid = PushManager.getInstance().getClientid(this.getApplicationContext());

        //获取屏幕大小
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;

        preference = getSharedPreferences("data", MODE_PRIVATE);
        editor = preference.edit();

        //记录屏幕的dp
        int changdudp = DensityUtil.px2dip(this, h_screen);
        int kuangdudp = DensityUtil.px2dip(this, w_screen);
//        Log.wtf("~~~~~~~~~~~~~~~",changdudp+"+"+kuangdudp);
        editor.putInt("changdudp", changdudp);
        editor.putInt("kuangdudp", kuangdudp);
        editor.putInt("changdupx", h_screen);
        editor.putInt("kuangdupx", w_screen);
        editor.putBoolean("xintiaojiesu", true);
        editor.commit();

        if (isConn(MainActivity.this)) {
            next();
            //new HcNetWorkTask(this, this, 1,false).doPost(UrlConstant.QUERY_LATEST_VERSION, null, postParams(1).getBytes());

        } else {
            showNoNetWorkDlg(MainActivity.this);
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 加权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    /*
     * 判断网络连接是否已开
     * true 已打开  false 未打开
     * */
    public static boolean isConn(Context context) {
        boolean bisConnFlag = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
        }
        return bisConnFlag;
    }

    /**
     * 当判断当前手机没有网络时选择是否打开网络设置
     *
     * @param context
     */
    public void showNoNetWorkDlg(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //
        builder.setTitle(R.string.app_name)            //

                .setMessage("当前无网络").setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                try {
//                    MainActivity.socket.close();
//                    MainActivity.TCPconnect = false;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

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

    @Override
    protected void onResume() {
        super.onResume();
        //启动tcp服务
        Intent intent = new Intent(this, TCPService.class);
        startService(intent);
    }

    /**
     * post参数
     *
     * @param code
     * @return
     */
    private String postParams(int code) {
        Map<String, String> params = new HashMap<String, String>();
        if (code == 1) {
            // 查询最新版本号
            params.put("type", "1");
            if (LogTools.debug) {
                LogTools.i("查询最新版本号参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        }
        return "";
    }


    @Override
    public void onResult(String result, int code) {
        if (!result.isEmpty()) {
            if (LogTools.debug) {
                LogTools.i("查询版本号返回数据->" + result);
            }
            try {
                JSONObject object = new JSONObject(result);
                int state = object.getInt("state");
                if (state == 0) {
                    JSONObject data = object.getJSONObject("data");
                    version = data.getString("version");
                    isForce = data.getString("isForce");
                    versionid = data.getString("id");
                    if (dangqianbanbeng < Integer.parseInt(versionid)) {
                        if (!ISloading) {
                            update();
                        }


                    } else {
                        //不需要版本更新
                        editor.putBoolean("isNewest", true);
                        editor.commit();
                        next();
                    }
                } else if (state == 2) {
                    //没有数据
                    ToastTools.show(this, "没有数据");
                    next();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            ToastTools.show(this, "获取不到数据");
            //这个接口不能用的时候 也要进行跳转
            editor.putBoolean("isNewest", false);
            editor.commit();
            next();

        }

    }

    public void update() {
        if (isForce.equals("true")) {
            //强制更新
            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("您当前的版本过低，请更新至最新版本，谢谢支持！");
            builder.setPositiveButton("点我更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent it = new Intent(
                            MainActivity.this,
                            DownloadService.class);
                    it.putExtra("apkurl", "http://www.ouzhongiot.com/android/lianxia.apk");
                    MainActivity.this.startService(it);
                    ISloading = true;
                    //代表当前版本是否为最新的
                    editor.putBoolean("isNewest", true);
                    editor.commit();
                }
            });
            builder.setNegativeButton("不更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //不更新，就要退出整个应用程序
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());

                }
            });
            builder.create();
            builder.show();
        } else {
            //不强制更新
            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("您当前的版本过低，请更新至最新版本，谢谢支持！");
            builder.setPositiveButton("点我更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent it = new Intent(
                            MainActivity.this,
                            DownloadService.class);
                    it.putExtra("apkurl", "http://www.ouzhongiot.com/android/lianxia.apk");
                    MainActivity.this.startService(it);
                    ISloading = true;
                    //代表当前版本是否为最新的
                    editor.putBoolean("isNewest", true);
                    editor.commit();

                }
            });
            builder.setNegativeButton("不更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    editor.putBoolean("isNewest", false);
                    editor.commit();
                    //不需要版本更新
                    next();
                }
            });
            builder.create();
            builder.show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    //不需要版本更新
    public void next() {
        int i1 = 0;
        while (city == null) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i1++;
            if (i1 > 5) {
                break;
            }
        }
//        editor.putString("province", province);
//        editor.putString("city", city);
//        editor.putString("district", district);
//        editor.commit();

        if (getSharedPreferences("data", MODE_PRIVATE).getBoolean("isFirst", true)) {
            //第一次安装，跳转到引导页
            Intent intent = new Intent(MainActivity.this, GuideActivity.class);
            startActivity(intent);
            OZApplication.getInstance().finishActivity();


        } else {
            //不是第一次安装
            if (SpData.getInstance(MainActivity.this).getData(SpConstant.LOGIN_USERNAME).equals("")) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                OZApplication.getInstance().finishActivity();
            } else {
                Intent intentlogin = new Intent(MainActivity.this, MyMachineActivity.class);
                startActivity(intentlogin);
                OZApplication.getInstance().finishActivity();
            }
        }
    }

}