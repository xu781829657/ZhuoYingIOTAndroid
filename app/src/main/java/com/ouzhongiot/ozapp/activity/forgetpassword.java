package com.ouzhongiot.ozapp.activity;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class forgetpassword extends AppCompatActivity implements View.OnClickListener{
    private CheckView mCheckView ;
    //    private TextView mShowPassViwe;
    private EditText mEditPass;
    private Button mSubmit;
    private int [] checkNum =null;
    private EditText et_number_forgetpassword;
    private AlertDialog.Builder builder;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private InputMethodManager manager;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if(msg.what==0){
                Toast.makeText(forgetpassword.this,"该账号未注册，请先注册",Toast.LENGTH_SHORT).show();
//                builder = new AlertDialog.Builder(forgetpassword.this);
//                builder.setTitle("提示");
//                builder.setMessage("该账号未注册，请先注册...");
//                builder.setPositiveButton("知道了", null);
//                builder.create();
//                builder.show();


            }else
            {
                Toast.makeText(forgetpassword.this,"您输入的电话号码有误",Toast.LENGTH_SHORT).show();
//                builder = new AlertDialog.Builder(forgetpassword.this);
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
        setContentView(R.layout.activity_forgetpassword);
        preference = getSharedPreferences("back",MODE_PRIVATE);
        editor = preference.edit();
        manager =   (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        initCheckNum();



        //点击返回图标
        this.findViewById(R.id.iv_forgetpassword_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
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

    public void initView(){

        mCheckView = (CheckView) findViewById(R.id.forgetpassword_checkView);
        mEditPass = (EditText) findViewById(R.id.forgetpassword_checkTest);
        et_number_forgetpassword = (EditText) findViewById(R.id.et_number_forgetpassword);
        mSubmit = (Button) findViewById(R.id.btn_forgetpassword_next);
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
            case R.id.btn_forgetpassword_next:
                String userInput = mEditPass.getText().toString();
                int  length  = 0;
                length = et_number_forgetpassword.getText().length();

                if(et_number_forgetpassword.getText().toString().equals(""))
                {
                    Toast.makeText(forgetpassword.this,"请输入电话号码",Toast.LENGTH_SHORT).show();
//                    builder = new AlertDialog.Builder(this);
//                    builder.setTitle("提示");
//                    builder.setMessage("请输入电话号码...");
//                    builder.setPositiveButton("知道了",null);
//                    builder.create();
//                    builder.show();

                }else
                if(length!=11){
                    Toast.makeText(forgetpassword.this,"您输入的电话号码有误",Toast.LENGTH_SHORT).show();
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
                            params.add(new BasicNameValuePair("phone", et_number_forgetpassword.getText().toString()));
                            Map<String ,String> map =  JacksonUtil.deserializeJsonToMap(Post.dopost(uriAPI,params),String.class,String.class);
                            Log.wtf("dfsdfsdfsd1111fdsfs",map.get("state"));
                            String state = map.get("state");
                            if(state.equals("3")){
                                Intent intent = new Intent();
                                intent.setClass(forgetpassword.this,forgetpasswordsendmessage.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("number",et_number_forgetpassword.getText().toString());
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
                    Toast.makeText(forgetpassword.this,"您输入的验证码有误",Toast.LENGTH_SHORT).show();
//                    builder = new AlertDialog.Builder(this);
//                    builder.setTitle("提示");
//                    builder.setMessage("您输入的验证码有误...");
//                    builder.setPositiveButton("知道了",null);
//                    builder.create();
//                    builder.show();
                }
                break;
            case R.id.forgetpassword_checkView:
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
}

