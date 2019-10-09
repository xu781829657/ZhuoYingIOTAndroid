package com.ouzhongiot.ozapp.activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ouzhongiot.ozapp.Model.machine;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.Myadapter;

import java.util.ArrayList;
import java.util.List;

//异步加载viewlist
public class machinelist extends AppCompatActivity implements AdapterView.OnItemClickListener{
    /** Called when the activity is first created. */
    public List<String> URL;
    private List<machine> list;
    ListView lv;
    Myadapter adapter;
    private   List<machine> typemachinelist = new ArrayList<machine>();
    private String addmachinetype;
    private AlertDialog.Builder builder;
    private SharedPreferences preferences;
    private SharedPreferences preferencesmachinelist;
    private SharedPreferences.Editor editor;

Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case 1000:
           // 生成list

                for (int i = 0,j = 0;i<list.size();i++)
                {
                    if(list.get(i).getTypeSn().substring(0,2).equals(addmachinetype))
                    {
                        typemachinelist.add(j,list.get(i));
                        j++;
                    }
                }
               lv.setDividerHeight(-1);
            adapter = new Myadapter(machinelist.this, typemachinelist,lv,addmachinetype);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(machinelist.this);
                break;
            case 1001:
    //无法生成list
            builder = new AlertDialog.Builder(machinelist.this);
            builder.setMessage("未能连接网络，请检查网络情况");
            builder.setPositiveButton("知道了", null);
            builder.create();
            builder.show();
                break;
            case 0:
                //需要升级版本
                builder = new AlertDialog.Builder(machinelist.this);
                builder.setMessage("当前app版本过低，需要添加该设备请先升级版本！");
                builder.setPositiveButton("知道了", null);
                builder.create();
                builder.show();

        }
    }
};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machinelist);
        lv = (ListView) findViewById(R.id.lv_machinelist);
        addmachinetype = getIntent().getStringExtra("addmachinetype");

        //加载产品列表
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String uriAPI = MainActivity.ip+"fan/device/queryMoreProduct";
//                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                String str = Post.dopost(uriAPI, params);
//                Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str,Map.class);
                preferencesmachinelist = getSharedPreferences("data",MODE_PRIVATE);
                String data = preferencesmachinelist.getString("machinelist","");
                Log.wtf("+++++++++++++++",data);
                list = JacksonUtil.deserializeJsonToList(data,machine.class);
                Log.wtf("---------------",list+"");
                if(list!=null){
                Message msg = new Message();
                msg.what = 1000;
                handler.sendMessage(msg);
                }else
                {
                    Message msg = new Message();
                    msg.what = 1001;
                    handler.sendMessage(msg);
                }
            }
        }).start();



    //返回
        this.findViewById(R.id.iv_machinelist_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      onBack();
                    }
                });


    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                preferences = getSharedPreferences("addmachine",MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("addmachineType",typemachinelist.get(i).getTypeSn());
                editor.putString("addmachinetypeName",typemachinelist.get(i).getBrand()+typemachinelist.get(i).getTypeName());
                editor.putString("addmachineprotocol",typemachinelist.get(i).getProtocol());
                editor.putString("addmachinebindurl",typemachinelist.get(i).getBindUrl());
                editor.putInt("addmachineslType",typemachinelist.get(i).getSlType());
                editor.commit();
                Intent intent = new Intent(machinelist.this,coldfanset.class);
                startActivity(intent);


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
