package com.ouzhongiot.ozapp.activity;

import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ouzhongiot.ozapp.R;

public class youxiang extends AppCompatActivity {
    private InputMethodManager manager;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private EditText et_youxiang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youxiang);
        et_youxiang = (EditText) findViewById(R.id.et_youxiang);
        preferences  = getSharedPreferences("data",MODE_PRIVATE);
        editor = preferences.edit();
        manager =   (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //返回
        this.findViewById(R.id.youxiang_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        //取消
        this.findViewById(R.id.youxiang_quxiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });
        //确定
        this.findViewById(R.id.youxiang_queding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_youxiang.getText().equals("")){
                    Toast.makeText(youxiang.this,"请输入昵称",Toast.LENGTH_SHORT).show();
                }else if(!et_youxiang.getText().toString().contains("@")){
                    Toast.makeText(youxiang.this,"请输入正确的邮箱地址",Toast.LENGTH_SHORT).show();
                }
                else{
                    editor.putString("email",et_youxiang.getText().toString());
                    editor.commit();
                    onBack();
                }
            }
        });

    }

    //点击空白处回收键盘
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
}
