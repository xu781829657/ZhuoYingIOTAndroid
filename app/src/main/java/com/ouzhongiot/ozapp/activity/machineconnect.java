package com.ouzhongiot.ozapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.esptouch.EsptouchTask;
import com.ouzhongiot.ozapp.esptouch.IEsptouchListener;
import com.ouzhongiot.ozapp.esptouch.IEsptouchResult;
import com.ouzhongiot.ozapp.esptouch.IEsptouchTask;
import com.ouzhongiot.ozapp.esptouch.demo_activity.EspWifiAdminSimple;
import com.ouzhongiot.ozapp.esptouch.task.__IEsptouchTask;
import com.ouzhongiot.ozapp.others.CircleProgressView;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.Post;
import com.ouzhongiot.ozapp.others.UDPClient;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.SpData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class machineconnect extends AppCompatActivity {

    private TextView tv_connect1, tv_connect2, tv_connect3;
    private String wifipassword, wifiid;
    private String udpsenddata;//UDP发送的协议
    private static final String TAG = "machineconnect";
    private EspWifiAdminSimple mWifiAdmin;
    private String mokuaiid;
    private SharedPreferences addmachine;
    private SharedPreferences logindata;
    private String username;
    private SharedPreferences.Editor editor;
    private String typeSn;
    private String typeNumber;
    private String bindUrl;//绑定设备Url
    private String userSn;
    private String userId;
    private String password;
    public static final String SERVERIP = "255.255.255.255";
    public static final int SERVERPORT = 3001;
    private boolean start = true;
    private String udpreceivedata = "";
    private String checktype;
    private String url = "";
    private int progress = 0;
    private CircleProgressView mCircleBar;
    private int step = 1;
    private Boolean CONECTING = true;

    private TimeCout timeCout;// 计时器
    private long mm;

    private String udpDeviceSn;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Resources resource = (Resources) getBaseContext().getResources();
            ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.green);
            switch (msg.what) {
                case 1:
//                    addmachine = getSharedPreferences("addmachine", MODE_PRIVATE);
                    //修改之后typeSn得不到了
//                    typeSn = addmachine.getString("addmachineType", "");

                    username = logindata.getString("username", "username");
                    password = logindata.getString("password", "");
                    userId = logindata.getString("userId", "");
                    userSn = logindata.getString("userSn", "userSn");
                    tv_connect1.setText("成功搜索到设备！");
                    tv_connect1.setTextColor(csl);
                    step = 2;
//                    progress = 33;

//                    //配置完smartlink之后进行UDP
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UDPClient client = new UDPClient(udpsenddata);
                            udpreceivedata = client.send();
                            Log.wtf("这个是返回的udp信息", udpreceivedata);
                            //484d 4232 2a 5ccf7f8d08bf  新风空气净化器
                            //484d 41 2a  5ccf7f8d09ba 23  冷风扇

                            //截取12位就好
                            udpDeviceSn = udpreceivedata.substring(udpreceivedata.indexOf("2a") + 2, udpreceivedata.indexOf("2a") + 2 + 12);
                            if (udpDeviceSn.equals(mokuaiid)) {
                                //相等  匹配
                                if (LogTools.debug) {
                                    LogTools.d("sn匹配成功");
                                }
                                //接着匹配类型
                                String udpTypeSn = udpreceivedata.substring(4, 8);
                                if (udpTypeSn.equals("412a")) {
                                    //因为4131和4132的机器udp返回过来的类型sn 都是412a
                                    udpTypeSn = "4131";
                                }
                                if (udpTypeSn.equals(typeSn)) {
                                    //类型匹配成功
                                    if (LogTools.debug) {
                                        LogTools.d("类型匹配成功");
                                    }
                                    Message msg = new Message();
                                    msg.what = 2;
                                    handler.sendMessage(msg);

                                } else {
                                    //类型匹配失败
                                    if (LogTools.debug) {
                                        LogTools.d("类型匹配失败");
                                    }
                                    Intent intent = new Intent(machineconnect.this, AddMachineFailActivity.class);
                                    startActivity(intent);
                                    OZApplication.getInstance().finishActivity();
                                }


                            } else {
                                if (LogTools.debug) {
                                    LogTools.d("id不匹配");
                                }
//                                    Intent intent = new Intent(machineconnect.this, AddMachineFailActivity.class);
//                                    startActivity(intent);
//                                    finish();
                                Message msg = new Message();
                                msg.what = 7;
                                handler.sendMessage(msg);
                            }

                        }


                    }).start();
                    break;
                case 2:
                    tv_connect2.setText("连接设备成功！");
                    tv_connect2.setTextColor(csl);
                    step = 3;
//                    progress = 66;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
//                            switch (typeSn.substring(0,2)){
//                                case "41":
//                                    url = "smarthome/fan/bindDevice";
//                                    break;
//                                case "42":
//                                    url = "smarthome/air/bindDevice";
//                                    break;
//                                case "43":
//                                    url = "smarthome/dryer/bindDevice";
//                                    break;
//                                default:
//                                    break;
//                            }
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("province", logindata.getString("province", null)));
                            params.add(new BasicNameValuePair("city", logindata.getString("city", null)));
                            params.add(new BasicNameValuePair("ud.devTypeSn", typeSn));
                            params.add(new BasicNameValuePair("ud.devTypeNumber", typeNumber));
                            params.add(new BasicNameValuePair("ud.userSn", userSn));
                            params.add(new BasicNameValuePair("ud.devSn", mokuaiid));
                            params.add(new BasicNameValuePair("phoneType", "1"));
                            String str = Post.dopost(bindUrl, params);
                            if (LogTools.debug) {
                                LogTools.i("注册设备参数->" + params.toString());
                                LogTools.i("注册设备返回数据->" + str);
                            }
                            Map<String, String> map = JacksonUtil.deserializeJsonToMap(str, String.class, String.class);
                            String state = map.get("state");
                            switch (state) {
                                case "0":
                                    Message msg = new Message();
                                    msg.what = 3;
                                    handler.sendMessage(msg);
                                    break;
                                case "1":
                                    //失败
//                                    Intent intent = new Intent(machineconnect.this, ConnectFaild.class);
//                                    startActivity(intent);
//                                    finish();
                                    Intent intent = new Intent(machineconnect.this, AddMachineFailActivity.class);
                                    startActivity(intent);
                                    OZApplication.getInstance().finishActivity();
                                    break;
                                case "2":
                                    Message msg2 = new Message();
                                    msg2.what = 5;
                                    handler.sendMessage(msg2);
                                    break;
                                default:
                                    break;
                            }

                        }
                    }).start();

                    break;
                case 3:
                    step = 4;
                    progress = 100;
                    tv_connect3.setText("设备注册成功！");
                    tv_connect3.setTextColor(csl);

                    MainActivity.getuiClientid = PushManager.getInstance().getClientid(getApplication().getApplicationContext());
                    Log.wtf("个推信息", MainActivity.getuiClientid);

                    Intent newIntent = new Intent(machineconnect.this, MyMachineActivity.class);
                    SpData.getInstance(machineconnect.this).putData(SpConstant.MACHINE_LIST_UPDATE, "yes");
                    startActivity(newIntent);
                    OZApplication.getInstance().finishActivity();

//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            String uriAPI2 = MainActivity.ip + "smarthome/userDevice/queryUserDevice";
//                            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
//                            params2.add(new BasicNameValuePair("userSn", userSn));
//                            String str2 = Post.dopost(uriAPI2, params2);
//
//                            Log.wtf("这个是请求我的设备", str2);
//                            editor.putString("mymachinedata", str2);
//                            editor.commit();
//
//
////                            Intent intent = new Intent(machineconnect.this, mymachine.class);
//                            Intent intent = new Intent(machineconnect.this, MyMachineActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }).start();
                    break;
                case 4:
//                    Intent intent = new Intent(machineconnect.this, ConnectFaild.class);
//                    startActivity(intent);
//                    finish();
                    Intent intent = new Intent(machineconnect.this, AddMachineFailActivity.class);
                    startActivity(intent);
                    OZApplication.getInstance().finishActivity();
                    break;
                case 5:
                    progress = 100;
                    Toast.makeText(machineconnect.this, "该设备已经注册，无需再次注册", Toast.LENGTH_SHORT).show();
//                    Intent intent2 = new Intent(machineconnect.this, mymachine.class);
                    Intent intent2 = new Intent(machineconnect.this, MyMachineActivity.class);
                    startActivity(intent2);
                    OZApplication.getInstance().finishActivity();
                    break;
                case 6:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            while (CONECTING) {
                                try {
//                                    Thread.sleep((long) ((Math.random() * 500)));
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

//                                if (step == 1 && progress < 33) {
//                                    progress = progress + 1;
//                                } else if (step == 2 && progress < 66) {
//                                    progress = progress + 1;
//                                } else if (step == 3 && progress < 100) {
//                                    progress = progress + 1;
//                                }
                                //上面那段代码先注释下
                                if (progress < 99) {
                                    progress = progress + 1;
                                }

                                mCircleBar.setProgressNotInUiThread(progress);
                            }


                        }
                    }).start();

                    break;
                case 7:
                    timeCout.start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (mm > 2000) {
                                LogTools.d("mm->" + mm);
                                LogTools.d("执行while循环");
                                UDPClient client2 = new UDPClient(udpsenddata);
                                udpreceivedata = client2.send();
                                Log.wtf("这个是返回的udp信息", udpreceivedata);
                                //484d 4232 2a 5ccf7f8d08bf  新风空气净化器
                                //484d 41 2a  5ccf7f8d09ba 23  冷风扇
                                //截取12位就好
                                udpDeviceSn = udpreceivedata.substring(udpreceivedata.indexOf("2a") + 2, udpreceivedata.indexOf("2a") + 2 + 12);
                                if (udpDeviceSn.equals(mokuaiid)) {
                                    //相等  匹配
                                    if (LogTools.debug) {
                                        LogTools.d("id匹配成功");
                                    }

                                    //接着匹配类型
                                    String udpTypeSn = udpreceivedata.substring(4, 8);
                                    if (udpTypeSn.equals("412a")) {
                                        //因为4131和4132的机器udp返回过来的类型sn 都是412a
                                        udpTypeSn = "4131";
                                    }
                                    if (udpTypeSn.equals(typeSn)) {
                                        //类型匹配成功
                                        if (LogTools.debug) {
                                            LogTools.d("类型匹配成功");
                                        }
                                        Message msg = new Message();
                                        msg.what = 2;
                                        handler.sendMessage(msg);

                                    } else {
                                        //类型匹配失败
                                        if (LogTools.debug) {
                                            LogTools.d("类型匹配失败");
                                        }
                                        Intent intent = new Intent(machineconnect.this, AddMachineFailActivity.class);
                                        startActivity(intent);
                                        OZApplication.getInstance().finishActivity();
                                    }
                                    timeCout.cancel();
                                    break;
                                } else {
                                    if (LogTools.debug) {
                                        LogTools.d("id不匹配");
                                    }
                                }

                            }


                        }
                    }).start();

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machineconnect);
        OZApplication.getInstance().addActivity(this);
        mCircleBar = (CircleProgressView) findViewById(R.id.mCircleBar);
        logindata = getSharedPreferences("data", MODE_PRIVATE);
        editor = logindata.edit();
        tv_connect1 = (TextView) findViewById(R.id.tv_connect1);
        tv_connect2 = (TextView) findViewById(R.id.tv_connect2);
        tv_connect3 = (TextView) findViewById(R.id.tv_connect3);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        wifiid = bundle.getString("wifiid");
        wifipassword = bundle.getString("wifipassword");
        udpsenddata = SpData.getInstance(this).getData(SpConstant.ADD_MACHINE_SMARTLINK_PROTOCOL).toString();
        typeSn = SpData.getInstance(this).getData(SpConstant.ADD_MACHINE_TYPESN).toString();
        typeNumber = SpData.getInstance(this).getData(SpConstant.ADD_MACHINE_TYPENUMBER).toString();
        bindUrl = SpData.getInstance(this).getData(SpConstant.ADD_MACHINE_BINDURL).toString();
        mWifiAdmin = new EspWifiAdminSimple(this);
        //返回
        this.findViewById(R.id.iv_machineconnect_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OZApplication.getInstance().finishActivity();

            }
        });
        initViews();
        timeCout = new TimeCout(60000, 1000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {


                //配置SmartLink
                String apSsid = wifiid;
                String apPassword = wifipassword;
                String apBssid = mWifiAdmin.getWifiConnectedBssid();
                Log.wtf("apBssid---------------", apBssid);
                String isSsidHiddenStr = "NO";
                String taskResultCountStr = "1";
                Log.wtf("taskResultCountStr---------------", taskResultCountStr);
                if (__IEsptouchTask.DEBUG) {
                    Log.d(TAG, "mBtnConfirm is clicked, mEdtApSsid = " + apSsid
                            + ", " + " mEdtApPassword = " + apPassword);
                }
                new EsptouchAsyncTask3().execute(apSsid, apBssid, apPassword,
                        isSsidHiddenStr, taskResultCountStr);


            }
        });
        thread1.start();


        Message msg = new Message();
        msg.what = 6;
        handler.sendMessage(msg);


    }

    private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String text = result.getBssid() + " is connected to the wifi";
//                Toast.makeText(machineconnect.this, text,
//                        Toast.LENGTH_LONG).show();
            }

        });
    }

    private IEsptouchListener myListener = new IEsptouchListener() {

        @Override
        public void onEsptouchResultAdded(final IEsptouchResult result) {
            onEsptoucResultAddedPerform(result);
        }
    };


    private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {

//        private ProgressDialog mProgressDialog;

        private IEsptouchTask mEsptouchTask;
        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();

        @Override
        protected void onPreExecute() {
//            mProgressDialog = new ProgressDialog(machineconnect.this);
//            mProgressDialog
//                    .setMessage("Esptouch is configuring, please wait for a moment...");
//            mProgressDialog.setCanceledOnTouchOutside(false);
//            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    synchronized (mLock) {
//                        if (__IEsptouchTask.DEBUG) {
//                            Log.i(TAG, "progress dialog is canceled");
//                        }
//                        if (mEsptouchTask != null) {
//                            mEsptouchTask.interrupt();
//                        }
//                    }
//                }
//            });
//            mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
//                    "Waiting...", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
//            mProgressDialog.show();
//            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                    .setEnabled(false);
        }

        @Override
        protected List<IEsptouchResult> doInBackground(String... params) {
            int taskResultCount = -1;
            synchronized (mLock) {
                String apSsid = params[0];
                String apBssid = params[1];
                String apPassword = params[2];
                String isSsidHiddenStr = params[3];
                String taskResultCountStr = params[4];
                boolean isSsidHidden = false;
                if (isSsidHiddenStr.equals("YES")) {
                    isSsidHidden = true;
                }
                taskResultCount = Integer.parseInt(taskResultCountStr);
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword,
                        isSsidHidden, machineconnect.this);
                mEsptouchTask.setEsptouchListener(myListener);
            }
            List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
            return resultList;
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
//            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                    .setEnabled(true);
//            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
//                    "Confirm");
            IEsptouchResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                if (firstResult.isSuc()) {
                    StringBuilder sb = new StringBuilder();
                    for (IEsptouchResult resultInList : result) {
                        sb.append("Esptouch success, bssid2 = "
                                + resultInList.getBssid()
                                + ",InetAddress = "
                                + resultInList.getInetAddress()
                                .getHostAddress() + "\n");
                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\nthere's " + (result.size() - count)
                                + " more result(s) without showing\n");
                    }
                    mokuaiid = firstResult.getBssid();
                    Log.wtf("模块id", mokuaiid);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);


//                    Toast.makeText(machineconnect.this,sb,Toast.LENGTH_SHORT).show();
//                    mProgressDialog.setMessage(sb.toString());
                } else {
                    Message msg = new Message();
                    msg.what = 4;
                    handler.sendMessage(msg);

                }
            }
        }
    }


    private void initViews() {


        mCircleBar.setProgress(0);

    }


    @Override
    protected void onDestroy() {

        CONECTING = false;
        super.onDestroy();
    }

    class TimeCout extends CountDownTimer {

        public TimeCout(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //倒计时
            mm = millisUntilFinished;


        }

        @Override
        public void onFinish() {
            //时间结束
            if (udpDeviceSn.equals(mokuaiid)) {

            } else {
                timeCout.cancel();
                Intent intent = new Intent(machineconnect.this, AddMachineFailActivity.class);
                startActivity(intent);
                OZApplication.getInstance().finishActivity();
            }

        }
    }

}

