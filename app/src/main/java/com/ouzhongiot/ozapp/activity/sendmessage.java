package com.ouzhongiot.ozapp.activity;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import java.util.Timer;
import java.util.TimerTask;

public class sendmessage extends AppCompatActivity {

    private TextView tv_sendmessage;
    private int second = 60;
    private String number,data_checknumber,userSn;
    private TextView tv_number;
    private Bundle bundle;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private InputMethodManager manager;
    private String phone;
    private String data="";
    private EditText checkTest;
    private String checknumber="";


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //进入倒计时
            if (msg.what == 1) {
                tv_sendmessage.setEnabled(false);
                tv_sendmessage.setText(second + "s重发");
                try {
                    GradientDrawable myGrad = (GradientDrawable) tv_sendmessage.getBackground();
                    myGrad.setColor(getResources().getColor(R.color.gray));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                second--;
                editor.putInt("second", second);
                editor.commit();
            }
            //倒计时结束
            if (msg.what == 0) {
                try {
                    GradientDrawable myGrad = (GradientDrawable) tv_sendmessage.getBackground();
                    myGrad.setColor(getResources().getColor(R.color.green));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv_sendmessage.setText("发送短信");
                tv_sendmessage.setEnabled(true);
                second = 60;
                editor.putInt("second", second);
                editor.commit();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmessage);
        manager =   (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tv_sendmessage = (TextView) findViewById(R.id.tv_sendmessage);
        checkTest = (EditText)findViewById(R.id.checkTest);
        tv_number = (TextView) findViewById(R.id.tv_number);
        bundle = getIntent().getExtras();
        number =bundle.getString("number");
        phone = bundle.getString("number");
        preference = getSharedPreferences("second", MODE_PRIVATE);
        editor = preference.edit();

        tv_number.setText(number);



        //返回该页面的倒计时
        if (second == 60) {
            GradientDrawable myGrad = (GradientDrawable) tv_sendmessage.getBackground();
            myGrad.setColor(getResources().getColor(R.color.green));
        }
        if (preference.getInt("second", 60) == 60) {
            Log.wtf("刚进入页面的时间是：", preference.getInt("second", 60) + "这个是默认时间");
        } else {
            second = preference.getInt("second", 60);
            Log.wtf("刚进入页面的时间是：", second + "");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (second >= 0) {
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                            Thread.sleep(1000);
                        }
                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }




        //下一步（需要判断短信内容）
        this.findViewById(R.id.btn_sendmessage_next).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            checknumber = checkTest.getText().toString();

                            if (checknumber.equals("")||checknumber==null){
                                Toast.makeText(sendmessage.this,"请输入短信验证码",Toast.LENGTH_SHORT).show();
                            }else
                            if (!checknumber.equals(data_checknumber)){
                                Toast.makeText(sendmessage.this,"验证码错误，请重新输入",Toast.LENGTH_SHORT).show();
                            }
                             else{
                                Intent intent = new Intent();
                                intent.setClass(sendmessage.this,registerpassword.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }


                    }
                });


        //返回
        this.findViewById(R.id.btn_sendmessage_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                   onBack();
                    }
                });

        //返回按钮
        this.findViewById(R.id.iv_sendmessage_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                   onBack();
                    }
                });




        //发送短信
        tv_sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String uriAPI = MainActivity.ip+"smarthome/user/sendCode";
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("dest", phone));
                        params.add(new BasicNameValuePair("bool", "0"));
                        Map<String, Object> map = JacksonUtil.deserializeJsonToObject(Post.dopost(uriAPI, params), Map.class);
                        data =JacksonUtil.serializeObjectToJson (map.get("data"));
                        Map<String, Object> datamap = JacksonUtil.deserializeJsonToObject(data, Map.class);
                        data_checknumber = JacksonUtil.serializeObjectToJson (datamap.get("code"));
                        userSn = JacksonUtil.serializeObjectToJson (datamap.get("userSn"));
                        Log.wtf("修改密码返回值",data+"  "+data_checknumber+"  "+userSn);


                        try {
                            while (second >= 0) {
                                Message msg = new Message();
                                msg.what = 1;
                                handler.sendMessage(msg);
                                Thread.sleep(1000);
                            }
                            Message msg = new Message();
                            msg.what = 0;
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }

    //点击返回上个页面
    public void onBack(){
        new Thread(){
            public void run() {
                try{
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                }
                catch (Exception e) {
                    Log.e("Exception when onBack", e.toString());
                }
            }
        }.start();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
}
