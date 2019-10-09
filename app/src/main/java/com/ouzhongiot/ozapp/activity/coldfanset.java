package com.ouzhongiot.ozapp.activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ouzhongiot.ozapp.R;

public class coldfanset extends AppCompatActivity {

    private TextView tv_machineset0,tv_machineset_title,tv_machineset1;
    private ImageView iv_machineset1,iv_machineset0;
    private SharedPreferences preferences;
    private boolean donghua = true;
    private boolean xianshi = true;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                      iv_machineset1.setVisibility(View.GONE);
                    xianshi = false;
                    break;
                case 1:
                    iv_machineset1.setVisibility(View.VISIBLE);
                    xianshi = true;
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coldfanset);
        tv_machineset0 = (TextView)findViewById(R.id.tv_machineset0);
        tv_machineset1 = (TextView)findViewById(R.id.tv_machineset1);
        tv_machineset_title = (TextView) findViewById(R.id.tv_machineset_title);
        iv_machineset1 = (ImageView) findViewById(R.id.iv_machineset1);
        iv_machineset0 = (ImageView) findViewById(R.id.iv_machineset0);
        preferences = getSharedPreferences("addmachine",MODE_PRIVATE);
//        tv_machineset_title.setText("设置"+preferences.getString("addmachinetypeName","智能冷风扇"));

        switch (preferences.getInt("addmachineslType",1)){
            case 1:
//                tv_machineset0.setText(R.string.kaiguankongzhipeiwangmoshi);
                break;
            case 2:
                tv_machineset0.setText(R.string.kaiguankongzhipeiwangmoshi);
                break;
            case 3:
                tv_machineset0.setText(R.string.wifianjianpeiwangmoshi);
                tv_machineset1.setText(R.string.wifishansuo);
                iv_machineset1.setImageResource(R.mipmap.wifianjianpeiwangmoshi1);
                iv_machineset0.setImageResource(R.mipmap.wifianjianpeiwangmoshi0);

                break;
            default:
                break;
        }


                new Thread(new Runnable() {
                            @Override
                            public void run() {

                                while (donghua){

                                    try {
                                        Thread.sleep(600);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if (xianshi)
                                    {
                                        Message msg = new Message();
                                        msg.what = 0;
                                        handler.sendMessage(msg);
                                    }else {
                                        Message msg = new Message();
                                        msg.what = 1;
                                        handler.sendMessage(msg);
                                    }
                                }
                            }
                        }).start();



        this.findViewById(R.id.iv_coldfanset_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    onBack();
                    }
                });
        this.findViewById(R.id.btn_coflfanAset_next).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(coldfanset.this,coldfanAwifi.class);
                        startActivity(intent);
                        finish();
                    }
                });


    }

    @Override
    protected void onDestroy() {
        donghua = false;
        super.onDestroy();
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
