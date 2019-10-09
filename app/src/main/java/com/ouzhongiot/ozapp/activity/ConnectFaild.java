package com.ouzhongiot.ozapp.activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.ouzhongiot.ozapp.R;

public class ConnectFaild extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_faild);
//        this.findViewById(R.id.iv_connectfaild_back).setOnClickListener(new View.OnClickListener() {

//                    @Override
//                    public void onClick(View view) {
//                     onBack();
//                    }
//                });
        this.findViewById(R.id.btn_connectfaild_chongshi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                         Intent intent = new Intent(ConnectFaild.this,TypeList.class);
//                         startActivity(intent);
//                        Intent intent = new Intent(ConnectFaild.this,coldfanset.class);
//                        startActivity(intent);
                        Intent intent = new Intent(ConnectFaild.this, AddMachineSetActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        this.findViewById(R.id.tv_connectfaild_fankui).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

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
