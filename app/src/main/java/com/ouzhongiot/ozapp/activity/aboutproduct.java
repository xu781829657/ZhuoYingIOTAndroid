package com.ouzhongiot.ozapp.activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.tools.IconfontTools;

public class aboutproduct extends AppCompatActivity {
    private TextView font_update_dot;//更新红点
    private Boolean isNewest = false;//是否为最新版本

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutproduct);
        font_update_dot = (TextView)findViewById(R.id.font_about_update_dot);
        font_update_dot.setTypeface(IconfontTools.getTypeface(this));
        isNewest = getSharedPreferences("data", MODE_PRIVATE).getBoolean("isNewest",false);
        if (isNewest){
            //最新版
            font_update_dot.setVisibility(View.GONE);
        }else{
            font_update_dot.setVisibility(View.VISIBLE);
        }

        this.findViewById(R.id.guanyuchanpin_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        this.findViewById(R.id.guanyuchanpin_shuoming).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            Intent intent = new Intent(aboutproduct.this,chanpinshuoming.class);
                            startActivity(intent);
                    }
                });
        this.findViewById(R.id.guanyuchanpin_bangzhu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(aboutproduct.this,zaixianbangzhu.class);
                startActivity(intent);
            }
        });

        this.findViewById(R.id.guanyuchanpin_fankui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(aboutproduct.this,jianyifankui.class);
                startActivity(intent);
            }
        });

        this.findViewById(R.id.guanyuchanpin_rizhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(aboutproduct.this,gengxinrizhi.class);
                startActivity(intent);
            }
        });

        this.findViewById(R.id.guanyuchanpin_gengxin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(aboutproduct.this,CheckUpdateActivity.class);
                intent.putExtra(CheckUpdateActivity.PARAM_ISNEWEST,isNewest);
                startActivity(intent);
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
}
