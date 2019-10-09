package com.ouzhongiot.ozapp.airclean;

import android.app.Instrumentation;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MainActivity;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.Post;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class aircleanwaichumoshi extends AppCompatActivity {

    private TextView tv_airclean_waichumoshi_kaiqi,tv_airclean_waichumoshi_guanbi,tv_airclean_waichu_guanbimoshi,tv_airclean_waichu_kaiqimoshi;
    private SharedPreferences preferences,preferencesmachine;
    private SharedPreferences.Editor editor,editormachine;
    private String workmachineid;
    private ImageView airclean_waichumoshi_dingshimoshi;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 0:
                   airclean_waichumoshi_dingshimoshi.setImageResource(R.mipmap.aircleandingshiguanbi);

                   break;
               case 1:

                   airclean_waichumoshi_dingshimoshi.setImageResource(R.mipmap.aircleandingshikaiqi);

                   break;
               default:
                   break;
           }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aircleanwaichumoshi);
        tv_airclean_waichumoshi_kaiqi = (TextView) findViewById(R.id.tv_airclean_waichumoshi_kaiqi);
        tv_airclean_waichumoshi_guanbi = (TextView) findViewById(R.id.tv_airclean_waichumoshi_guanbi);
        tv_airclean_waichu_guanbimoshi = (TextView) findViewById(R.id.tv_airclean_waichu_guanbimoshi);
        tv_airclean_waichu_kaiqimoshi = (TextView) findViewById(R.id.tv_airclean_waichu_kaiqimoshi);
        airclean_waichumoshi_dingshimoshi = (ImageView) findViewById(R.id.airclean_waichumoshi_dingshimoshi);
        preferences = getSharedPreferences("data",MODE_PRIVATE);
        workmachineid = preferences.getString("workmachineid","");
        preferencesmachine = getSharedPreferences(workmachineid,MODE_PRIVATE);
        editormachine =preferencesmachine.edit();
        tv_airclean_waichumoshi_kaiqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialogKaiqi();
            }
        });
        tv_airclean_waichumoshi_guanbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialogGuanbi();
            }
        });

        airclean_waichumoshi_dingshimoshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferencesmachine.getBoolean("airclean_waichumoshi_dingshimoshi",false))
                {
                    editormachine.putBoolean("airclean_waichumoshi_dingshimoshi",false);
                    editormachine.commit();
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }else
                {
                    editormachine.putBoolean("airclean_waichumoshi_dingshimoshi",true);
                    editormachine.commit();
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        });

        this.findViewById(R.id.iv_airclean_waichumoshi_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                onBack();
                    }
                });
        this.findViewById(R.id.tv_airclean_waichu_guanbimoshi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_airclean_waichu_guanbimoshi.setClickable(false);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //请求当前数据
                                String uriAPI2 = MainActivity.ip + "smarthome/air/jobTask";
                                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                                params2.add(new BasicNameValuePair("devSn", workmachineid));
                                params2.add(new BasicNameValuePair("task.fSwitchOn", "false"));
                                params2.add(new BasicNameValuePair("task.fSwitchOff", "false"));
                                String str2 = Post.dopost(uriAPI2, params2);
                                Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str2, Map.class);
                                String state = map.get("state").toString();
                                if (state.equals("0"))
                                {
                                    editormachine.putInt("DINGSHIMOSHI",0);
                                    editormachine.commit();
                                    onBack();
                                }
                            }
                        }).start();
                    }
                });
        this.findViewById(R.id.tv_airclean_waichu_kaiqimoshi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_airclean_waichu_kaiqimoshi.setClickable(false);
                if (preferencesmachine.getBoolean("airclean_waichumoshi_dingshimoshi",false)){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //请求当前数据
                            String uriAPI2 = MainActivity.ip + "smarthome/air/jobTask";
                            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                            params2.add(new BasicNameValuePair("devSn", workmachineid));
                            params2.add(new BasicNameValuePair("task.fSwitchOn", "true"));
                            params2.add(new BasicNameValuePair("task.fSwitchOff", "true"));
                            params2.add(new BasicNameValuePair("task.runWeek", "1111111"));
                            params2.add(new BasicNameValuePair("task.onJobTime",tv_airclean_waichumoshi_kaiqi.getText().toString() ));
                            params2.add(new BasicNameValuePair("task.offJobTime", tv_airclean_waichumoshi_guanbi.getText().toString() ));
                            String str2 = Post.dopost(uriAPI2, params2);
                            Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str2, Map.class);
                            String state = map.get("state").toString();
                            if (state.equals("0"))
                            {
                                editormachine.putInt("DINGSHIMOSHI",1);
                                editormachine.putBoolean("airclean_waichumoshi_dingshimoshi",true);
                                editormachine.commit();
                                onBack();
                            }

                        }
                    }).start();

                }
                else
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //请求当前数据
                            String uriAPI2 = MainActivity.ip + "smarthome/air/jobTask";
                            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                            params2.add(new BasicNameValuePair("devSn", workmachineid));
                            params2.add(new BasicNameValuePair("task.fSwitchOn", "true"));
                            params2.add(new BasicNameValuePair("task.fSwitchOff", "true"));
                            params2.add(new BasicNameValuePair("task.onJobTime",tv_airclean_waichumoshi_kaiqi.getText().toString() ));
                            params2.add(new BasicNameValuePair("task.offJobTime", tv_airclean_waichumoshi_guanbi.getText().toString() ));
                            String str2 = Post.dopost(uriAPI2, params2);
                            Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str2, Map.class);
                            String state = map.get("state").toString();
                            if (state.equals("0"))
                            {
                                editormachine.putInt("DINGSHIMOSHI",1);
                                editormachine.putBoolean("airclean_waichumoshi_dingshimoshi",false);
                                editormachine.commit();
                                onBack();
                            }

                        }
                    }).start();
                }

            }
        });


    }

    @Override
    protected void onResume() {
        if(preferencesmachine.getBoolean("airclean_waichumoshi_dingshimoshi",false))
        {

            airclean_waichumoshi_dingshimoshi.setImageResource(R.mipmap.aircleandingshikaiqi);
        }else
        {

            airclean_waichumoshi_dingshimoshi.setImageResource(R.mipmap.aircleandingshiguanbi);
        }
        super.onResume();
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

    //日期选择
    public void showTimePickerDialogKaiqi() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker arg0, int hour, int minute) {
                String hour1 = "";
                String minute1 = "";
                if (hour<10){
                    hour1 = "0"+hour;
                }else
                {
                    hour1 = hour+"";
                }
                if (minute<10){
                    minute1 = "0"+minute;
                }else
                {
                    minute1 = minute+"";
                }
                tv_airclean_waichumoshi_kaiqi.setText(hour1+":"+minute1);
            }

        }, hour, minute, true);
        dialog.show();
    }
    public void showTimePickerDialogGuanbi() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker arg0, int hour, int minute) {
                String hour1 = "";
                String minute1 = "";
                if (hour<10){
                    hour1 = "0"+hour;
                }else
                {
                    hour1 = hour+"";
                }
                if (minute<10){
                    minute1 = "0"+minute;
                }else
                {
                    minute1 = minute+"";
                }
                tv_airclean_waichumoshi_guanbi.setText(hour1+":"+minute1);
            }

        }, hour, minute, true);

        dialog.show();
    }


}
