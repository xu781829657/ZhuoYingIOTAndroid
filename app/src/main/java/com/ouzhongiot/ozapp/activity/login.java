package com.ouzhongiot.ozapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.ouzhongiot.ozapp.Model.Address;
import com.ouzhongiot.ozapp.Model.logindata;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.others.ActivityCollector;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.Post;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hxf
 * @date 修改时间: 2017/2/13
 * @Description 登录页
 */

public class login extends AppCompatActivity {
    private InputMethodManager manager;
    private ImageView logo;
    private EditText et_username;
    private EditText et_password;
    private String username;
    private String password;
    private String userSn;
    private int state;
    //    private List<logindata.DataBean.UserDeviceBean> userDevice;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String userId;
    private AlertDialog.Builder builder;
    private Button login;
    private ImageView iv_reg_req_code_gif_view;
    private logindata logindata;
    private LinearLayout linearLayout;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //参数异常为空
                    Toast.makeText(com.ouzhongiot.ozapp.activity.login.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //用户名不存在
                    Toast.makeText(com.ouzhongiot.ozapp.activity.login.this, "用户不存在", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    //密码错误
                    Toast.makeText(com.ouzhongiot.ozapp.activity.login.this, "密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 4:

                    builder = new AlertDialog.Builder(com.ouzhongiot.ozapp.activity.login.this);
                    builder.setTitle("温馨提示");
                    builder.setMessage("您没有允许联侠开启定位，联侠无法为您提供准确的天气情况和空气状况哦");
                    builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            editor.putString("city", "杭州市");
                            editor.putString("district", "西湖区");
                            editor.commit();
                            if (true) {
                                Intent intent = new Intent(com.ouzhongiot.ozapp.activity.login.this, mymachine.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(com.ouzhongiot.ozapp.activity.login.this, mymachine.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                    builder.create();
                    builder.show();


                    break;
                case 5:
                    linearLayout.setVisibility(View.GONE);
                    iv_reg_req_code_gif_view.clearAnimation();
                    login.setText("登录");
                    login.setClickable(true);

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = preferences.edit();

        new Thread(new Runnable() {
            @Override
            public void run() {

                String uriAPI = MainActivity.ip + "smarthome/device/queryMoreProduct";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String str = Post.dopost(uriAPI, params);
                Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str, Map.class);
                String data = JacksonUtil.serializeObjectToJson(map.get("data"));
                Log.wtf("这个是机器列表", data);
                editor.putString("machinelist", data);
                editor.commit();


                String uriAPI2 = MainActivity.ip + "smarthome/device/queryTypeList";
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                String str2 = Post.dopost(uriAPI2, params2);
                editor.putString("typelist", str2);
                editor.commit();
                Log.wtf("机器类型str", str2);

            }
        }).start();
        linearLayout = (LinearLayout) findViewById(R.id.ll_login_load);
        iv_reg_req_code_gif_view = (ImageView) findViewById(R.id.iv_reg_req_code_gif_view);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        login = (Button) findViewById(R.id.login);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);

        logo = (ImageView) findViewById(R.id.logo);


        //注册
        this.findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(com.ouzhongiot.ozapp.activity.login.this, register.class);
//                startActivity(intent);
                Intent intent = new Intent(com.ouzhongiot.ozapp.activity.login.this, RegisterActivity2.class);
                startActivity(intent);
            }
        });

        //忘记密码
        this.findViewById(R.id.iv_forgetpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(com.ouzhongiot.ozapp.activity.login.this, forgetpassword.class);
//                startActivity(intent);
                Intent intent = new Intent(login.this, ForgetPwdActivity.class);
                intent.putExtra(ForgetPwdActivity.PARAM_TYPE, "forget");
                startActivity(intent);
            }
        });
        //登录
        this.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.getuiClientid = PushManager.getInstance().getClientid(getApplication().getApplicationContext());
                Log.wtf("个推信息", MainActivity.getuiClientid);
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                if (username.equals("") || password.equals("") || username == null || password == null) {
                    Toast.makeText(com.ouzhongiot.ozapp.activity.login.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                } else {


                    login.setText("登录中...");
                    login.setClickable(false);
                    Animation animation = AnimationUtils.loadAnimation(com.ouzhongiot.ozapp.activity.login.this, R.anim.base_loading_large_anim);
                    linearLayout.setVisibility(View.VISIBLE);
                    iv_reg_req_code_gif_view.startAnimation(animation);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String uriAPI = MainActivity.ip + "smarthome/user/login";
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("loginName", username));
                            params.add(new BasicNameValuePair("password", password));
                            params.add(new BasicNameValuePair("ua.clientId", MainActivity.getuiClientid));
                            params.add(new BasicNameValuePair("ua.phoneType", "1"));
                            String str = Post.dopost(uriAPI, params); // 返回用户的所有信息
                            Log.wtf("这个是登录信息", str);
                            logindata = com.ouzhongiot.ozapp.Model.logindata.objectFromData(str);
                            state = logindata.getState();

                            logindata.DataBean dataBean = logindata.getData();
                            if (dataBean != null) {
                                userSn = logindata.getData().getSn() + "";
//                                userId = logindata.getData().getUser().getId()+"";
                                Log.wtf("这个是userSn", userSn);
                                String uriAPI2 = MainActivity.ip + "smarthome/userDevice/queryUserDevice";
                                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                                params2.add(new BasicNameValuePair("userSn", userSn));

                                String str2 = Post.dopost(uriAPI2, params2);
                                Log.wtf("这个是请求我的设备", str2);

                                editor.putString("mymachinedata", str2);
                                editor.commit();
                            }
                            Message msg = new Message();
                            switch (state) {
                                case 0:
                                    //需要记录登录状态

                                    editor.putString("logindata", str);
                                    editor.putString("username", username);
                                    editor.putString("password", password);
                                    editor.putString("userSn", userSn);
                                    editor.putString("phone", logindata.getData().getPhone());
                                    editor.putString("userId", logindata.getData().getId() + "");
//                                    editor.putString("userId",userId);
                                    editor.putString("email", logindata.getData().getEmail());
                                    editor.putString("nickname", logindata.getData().getNickname());
                                    editor.putString("sex", logindata.getData().getSex() + "");
                                    editor.putString("birthdate", logindata.getData().getBirthdate());
                                    editor.putString("headImageUrl", logindata.getData().getHeadImageUrl());
                                    //将头像保存到本地
//
//                                    if (logindata.getData().getHeadImageUrl()==null){
//                                    } else if (logindata.getData().getHeadImageUrl().contains(preferences.getString("touxiangname","touxiangname"))){
//                                    }else
//                                    {
//                                        Bitmap bitmap = getPic(logindata.getData().getHeadImageUrl());
//                                        gerenxinxi.saveBitmap(Environment.getExternalStorageDirectory() + "/"
//                                                + "lianxiatouxiang" + ".jpg",bitmap);
//                                        editor.putString("touxiangbendiurl",Environment.getExternalStorageDirectory() + "/"
//                                                + "lianxiatouxiang" + ".jpg");
//                                        editor.commit();
//                                        Log.wtf("头像保存到本地","是的 ");
//
//                                    }


                                    //获取地址
                                    String uriAPI2 = MainActivity.ip + "smarthome/user/queryUserAddress";
                                    List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("userSn", userSn));
                                    String str2 = Post.dopost(uriAPI2, params);
                                    Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str2, Map.class);
                                    String data = JacksonUtil.serializeObjectToJson(map.get("data"));
                                    Log.wtf("这是得到的地址信息", data);
                                    editor.putString("address", data);
                                    if (data != null && !data.equals("null")) {
                                        editor.putString("address.xiancheng", Address.arrayAddressFromData(data).get(0).getAddrCounty());
                                        editor.putString("address.jiedao", Address.arrayAddressFromData(data).get(0).getAddrStreet());
                                        editor.putString("address.shengfeng", Address.arrayAddressFromData(data).get(0).getAddrProvince());
                                        editor.putString("address.shi", Address.arrayAddressFromData(data).get(0).getAddrCity());
                                        editor.putString("address.receiverName", Address.arrayAddressFromData(data).get(0).getReceiverName());
                                        editor.putString("address.receiverPhone", Address.arrayAddressFromData(data).get(0).getReceiverPhone() + "");

                                    }
                                    editor.commit();

                                    if (preferences.getString("city", "").equals("")) {
                                        Message msg1 = new Message();
                                        msg1.what = 4;
                                        handler.sendMessage(msg1);
                                    } else
                                    {
                                        Intent intent = new Intent(com.ouzhongiot.ozapp.activity.login.this, mymachine.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    break;
                                case 1:
                                    //参数异常为空

                                    msg.what = 1;
                                    handler.sendMessage(msg);
                                    break;
                                case 2:
                                    //用户名不存在
                                    msg.what = 2;
                                    handler.sendMessage(msg);
                                    break;
                                case 3:
                                    //密码错误
                                    msg.what = 3;
                                    handler.sendMessage(msg);
                                    break;
                                default:
                                    break;
                            }
                            Message msglogin = new Message();
                            msglogin.what = 5;
                            handler.sendMessage(msglogin);
                        }
                    }).start();
                }
            }
        });
    }


    // 传输网络图片
    public static Bitmap getPic(String uriPic) {
        URL imageUrl = null;
        Bitmap bitmap = null;
        try {
            imageUrl = new URL(uriPic);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ActivityCollector.finishAll();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton(AlertDialog.BUTTON_POSITIVE, "确定", listener);
            isExit.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", listener);
            // 显示对话框
            isExit.show();

        }

        return false;

    }

    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序

                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };
}
