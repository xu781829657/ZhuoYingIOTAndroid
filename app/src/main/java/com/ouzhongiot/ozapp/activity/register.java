package com.ouzhongiot.ozapp.activity;

import android.app.Instrumentation;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.check.CheckUtil;
import com.ouzhongiot.ozapp.check.CheckView;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.Post;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class register extends AppCompatActivity implements View.OnClickListener {
    private CheckView mCheckView ;
//    private TextView mShowPassViwe;
    private EditText mEditPass;
    private Button mSubmit;
    private Button mRef;
    private int [] checkNum =null;
    private TextView tv_argument;
    private EditText et_number_register;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private  InputMethodManager manager;
    private String phone;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if(msg.what==3){
                builder = new AlertDialog.Builder(register.this);
                builder.setTitle("提示");
                builder.setMessage("该账号已注册请直接登录...");
                builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(register.this, login.class);
                            startActivity(intent);
                            finish();
                    }
                });
                builder.create();
                builder.show();


            }else
            {
                Toast.makeText(register.this, "您输入的电话号码有误", Toast.LENGTH_SHORT).show();
//                builder = new AlertDialog.Builder(register.this);
//                builder.setTitle("提示");
//                builder.setMessage("您输入的电话号码有误...");
//                builder.setPositiveButton("知道了",null);
//                builder.create();
//                builder.show();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        manager =   (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        initCheckNum();




        //用户协议
        tv_argument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this, argument.class);
                startActivity(intent);
            }
        });
        //隐私政策
        this.findViewById(R.id.tv_privacy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(register.this, privacy.class);
                    startActivity(intent);
                    }
                });

        //点击返回图标
        this.findViewById(R.id.iv_register_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       onBack();
                    }
                });
        //验证是否已被注册
        et_number_register.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });


    }


    public void initView(){

        mCheckView = (CheckView) findViewById(R.id.checkView);
        mEditPass = (EditText) findViewById(R.id.checkTest);
        et_number_register = (EditText) findViewById(R.id.et_number_register);
        mSubmit = (Button) findViewById(R.id.btn_number_register);
        tv_argument = (TextView) findViewById(R.id.tv_argument);
        mSubmit.setOnClickListener(this);
        mCheckView.setOnClickListener(this);

    }

    public void initCheckNum(){
        checkNum = CheckUtil.getCheckNum();
        mCheckView.setCheckNum(checkNum);
        mCheckView.invaliChenkNum();
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_number_register:
                String userInput = mEditPass.getText().toString();
                int  length  = 0;
                length = et_number_register.getText().length();

                if(et_number_register.getText().toString().equals(""))
                {
                    Toast.makeText(register.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
//                   builder = new AlertDialog.Builder(this);
//                   builder.setTitle("提示");
//                   builder.setMessage("请输入电话号码...");
//                    builder.setPositiveButton("知道了",null);
//                    builder.create();
//                    builder.show();

                }else
                if(length!=11){
                    Toast.makeText(register.this, "您输入的电话号码有误", Toast.LENGTH_SHORT).show();
//                    builder = new AlertDialog.Builder(this);
//                    builder.setTitle("提示");
//                    builder.setMessage("您输入的电话号码有误...");
//                    builder.setPositiveButton("知道了",null);
//                    builder.create();
//                    builder.show();
                }
                else
                if(CheckUtil.checkNum(userInput, checkNum)){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String uriAPI = MainActivity.ip+"smarthome/user/queryPhone";
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("phone", et_number_register.getText().toString()));
                            Map<String ,String> map =  JacksonUtil.deserializeJsonToMap(Post.dopost(uriAPI,params),String.class,String.class);
                            Log.wtf("dfsdfsdfsd1111fdsfs",map.get("state"));
                            String state = map.get("state");
                           if(state.equals("0")){
                            Intent intent = new Intent();
                               intent.setClass(register.this, sendmessage.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("number",et_number_register.getText().toString());
                            intent.putExtras(bundle);
                            startActivity(intent);}else
                               {
                                   Message msg = new Message();
                                   msg.what = Integer.parseInt(state);
                                   handler.sendMessage(msg);
                               }
                        }
                    }).start();
                }else{
                    Toast.makeText(register.this, "您输入的验证码有误", Toast.LENGTH_SHORT).show();
//                    builder = new AlertDialog.Builder(this);
//                    builder.setTitle("提示");
//                    builder.setMessage("您输入的验证码有误...");
//                    builder.setPositiveButton("知道了",null);
//                    builder.create();
//                    builder.show();
                }
                break;
            case R.id.checkView:
                Log.wtf("更换图片","执行");
                initCheckNum();//重置验证码图片
                break;
            default:
                break;
        }
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
}

