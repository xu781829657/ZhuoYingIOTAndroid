package com.ouzhongiot.ozapp.airclean;

import android.app.Instrumentation;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.MainActivity;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.Post;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class aircleanzhinengmoshi extends AppCompatActivity {
//private TextView tv_airclean_zhinengmoshi_kaiqi,tv_airclean_zhinengmoshi_guanbi;
    private TextView  tv_airclean_zhineng_guanbimoshi,tv_airclean_zhineng_kaiqimoshi;
    private SharedPreferences preferences,preferencesmachine;
    private SharedPreferences.Editor editor,editormachine;
    private String workmachineid,workmachinetype;
    private OutputStream outputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aircleanzhinengmoshi);
//        tv_airclean_zhinengmoshi_guanbi = (TextView) findViewById(R.id.tv_airclean_zhinengmoshi_guanbi);
//        tv_airclean_zhinengmoshi_kaiqi = (TextView) findViewById(R.id.tv_airclean_zhinengmoshi_kaiqi);
        tv_airclean_zhineng_guanbimoshi = (TextView) findViewById(R.id.tv_airclean_zhineng_guanbimoshi);
        tv_airclean_zhineng_kaiqimoshi = (TextView) findViewById(R.id.tv_airclean_zhineng_kaiqimoshi);
        outputStream = ((OZApplication)getApplication()).outputStream;
        preferences = getSharedPreferences("data",MODE_PRIVATE);
        workmachineid = preferences.getString("workmachineid","");
        workmachinetype = preferences.getString("workmachinetype","");
        preferencesmachine = getSharedPreferences(workmachineid,MODE_PRIVATE);
        editormachine =preferencesmachine.edit();

        this.findViewById(R.id.iv_airclean_zhinengmoshi_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    onBack();
                    }
                });

        tv_airclean_zhineng_guanbimoshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_airclean_zhineng_guanbimoshi.setClickable(false);
//                try {
//                    ((OZApplication)getApplication()).getOutputStream().write();
//                    ((OZApplication)getApplication()).getOutputStream().flush();
                ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"A",(byte)0x02));
                    editormachine.putInt("DINGSHIMOSHI",0);
                    editormachine.commit();
                    onBack();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
        tv_airclean_zhineng_kaiqimoshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_airclean_zhineng_kaiqimoshi.setClickable(false);

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
//                            try {
////                              ((OZApplication)getApplication()).getOutputStream().write(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"S",(byte)0x01));
////                              ((OZApplication)getApplication()).getOutputStream().flush();
////                                Thread.sleep(300);
//                                ((OZApplication)getApplication()).getOutputStream().write();
//                                ((OZApplication)getApplication()).getOutputStream().flush();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            ((OZApplication)getApplication()).SendOrder(AircleanOrder.GetAircleanOrder(workmachineid,workmachinetype,"A",(byte)0x01));
                            editormachine.putInt("DINGSHIMOSHI",3);
                            editormachine.commit();
                            onBack();
                        }
                    }
                }).start();
            }
        });


//        tv_airclean_zhinengmoshi_kaiqi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showTimePickerDialogKaiqi();
//            }
//        });
//        tv_airclean_zhinengmoshi_guanbi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showTimePickerDialogGuanbi();
//            }
//        });
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
//    //日期选择
//    public void showTimePickerDialogKaiqi() {
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(System.currentTimeMillis());
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//
//        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//
//            @Override
//            public void onTimeSet(TimePicker arg0, int hour, int minute) {
//                String hour1 = "";
//                String minute1 = "";
//                if (hour<10){
//                    hour1 = "0"+hour;
//                }else
//                {
//                    hour1 = hour+"";
//                }
//                if (minute<10){
//                    minute1 = "0"+minute;
//                }else
//                {
//                    minute1 = minute+"";
//                }
//                tv_airclean_zhinengmoshi_kaiqi.setText(hour1+":"+minute1);
//            }
//
//        }, hour, minute, true);
//        dialog.show();
//    }
//    public void showTimePickerDialogGuanbi() {
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(System.currentTimeMillis());
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//
//        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//
//            @Override
//            public void onTimeSet(TimePicker arg0, int hour, int minute) {
//                String hour1 = "";
//                String minute1 = "";
//                if (hour<10){
//                    hour1 = "0"+hour;
//                }else
//                {
//                    hour1 = hour+"";
//                }
//                if (minute<10){
//                    minute1 = "0"+minute;
//                }else
//                {
//                    minute1 = minute+"";
//                }
//                tv_airclean_zhinengmoshi_guanbi.setText(hour1+":"+minute1);
//            }
//
//        }, hour, minute, true);
//
//        dialog.show();
//    }
}
