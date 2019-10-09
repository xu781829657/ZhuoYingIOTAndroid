package com.ouzhongiot.ozapp.activity;

import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.Post;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class registerpassword extends AppCompatActivity {
    private EditText et_register_password1, et_register_password2;
    private TextView tv_number_register_passwrod;
    private String password1;
    private String password2;
    private AlertDialog.Builder builder;
    private Bundle bundle;
    private InputMethodManager manager;
    private String phone;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                builder = new AlertDialog.Builder(registerpassword.this);
                builder.setMessage("注册成功，马上登录!");
                builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(registerpassword.this, login.class);
                        startActivity(intent);
                    }
                });
                builder.create();
                builder.show();

            } else {
                builder = new AlertDialog.Builder(registerpassword.this);
                builder.setMessage("注册失败，请重新注册");
                builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(registerpassword.this, register.class);
                        startActivity(intent);
                    }
                });
                builder.create();
                builder.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpassword);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        et_register_password1 = (EditText) findViewById(R.id.et_register_password1);
        et_register_password2 = (EditText) findViewById(R.id.et_register_password2);
        tv_number_register_passwrod = (TextView) findViewById(R.id.tv_number_register_password);

        bundle = getIntent().getExtras();
        Log.wtf("再一次电话号码", bundle.getString("number"));
        phone = bundle.getString("number");
        tv_number_register_passwrod.setText(bundle.getString("number"));


        this.findViewById(R.id.btn_login_register).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        password1 = et_register_password1.getText().toString();
                password2 = et_register_password2.getText().toString();
                if (password1.equals("") || password2.equals("")) {
                    Toast.makeText(registerpassword.this, "请输入密码", Toast.LENGTH_SHORT).show();
//                            builder = new AlertDialog.Builder(registerpassword.this);
//                            builder.setTitle("提示");
//                            builder.setMessage("请输入密码");
//                            builder.setPositiveButton("知道了",null);
//                            builder.create();
//                            builder.show();
                } else if (password1.length() < 6 || password2.length() < 6) {

                    Toast.makeText(registerpassword.this, "密码必须大于6位", Toast.LENGTH_SHORT).show();
//                            builder = new AlertDialog.Builder(registerpassword.this);
//                            builder.setTitle("提示");
//                            builder.setMessage("密码必须大于6位");
//                            builder.setPositiveButton("知道了",null);
//                            builder.create();
//                            builder.show();
                } else if (!password1.equals(password2)) {

                    Toast.makeText(registerpassword.this, "您两次输入的密码不一致", Toast.LENGTH_SHORT).show();
//                                builder = new AlertDialog.Builder(registerpassword.this);
//                                builder.setTitle("提示");
//                                builder.setMessage("您两次输入的密码不一致");
//                                builder.setPositiveButton("知道了",null);
//                                builder.create();
//                                builder.show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String uriAPI = MainActivity.ip+"smarthome/user/register";
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("user.phone", phone));
                            params.add(new BasicNameValuePair("user.password", password1));
                            Map<String, Object> map = JacksonUtil.deserializeJsonToObject(Post.dopost(uriAPI, params), Map.class);
                            String state =JacksonUtil.serializeObjectToJson(map.get("state"));
                            if (state.equals("0")) {
                                Message msg = new Message();
                                msg.what = 0;
                                handler.sendMessage(msg);
                            } else {
                                Message msg = new Message();
                                msg.what = Integer.parseInt(state);
                                handler.sendMessage(msg);
                            }
                        }
                    }).start();
                }
            }

        });


        this.findViewById(R.id.btn_register_password_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });
        this.findViewById(R.id.iv_register_password_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });


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
}
