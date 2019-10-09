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
import android.widget.Toast;

import com.ouzhongiot.ozapp.R;

public class nicheng extends AppCompatActivity {
    private InputMethodManager manager;
    private EditText et_nicheng;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nicheng);
        et_nicheng = (EditText) findViewById(R.id.et_nicheng);
        manager =   (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        preferences = getSharedPreferences("data",MODE_PRIVATE);
        editor = preferences.edit();
        //返回
        this.findViewById(R.id.nicheng_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        //取消
        this.findViewById(R.id.nicheng_quxiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });
        //确定
        this.findViewById(R.id.nicheng_queding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_nicheng.getText().equals("")){
                    Toast.makeText(nicheng.this,"请输入昵称",Toast.LENGTH_SHORT).show();
                }else
                {
                    editor.putString("nickname",et_nicheng.getText().toString());
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
