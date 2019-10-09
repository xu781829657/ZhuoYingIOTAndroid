package com.ouzhongiot.ozapp.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ouzhongiot.ozapp.Model.TypeListData;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.others.Myadapter;
import com.ouzhongiot.ozapp.others.TypeListAdapter;
import com.ouzhongiot.ozapp.zxing.activity.CaptureActivity;

import java.util.List;

public class TypeList extends AppCompatActivity implements AdapterView.OnItemClickListener{
//    private WebView mWebView;
//    private LinearLayout ll_typelist_jiazaidonghua;
//    private ImageView iv_typelist_jiazaidonghua;
    private ListView lv_typelist;
    private SharedPreferences preferences;
    private String typeliststr;
    TypeListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_list);
//        iv_typelist_jiazaidonghua = (ImageView) findViewById(R.id.iv_typelist_jiazaidonghua);
//        ll_typelist_jiazaidonghua = (LinearLayout) findViewById(R.id.ll_typelist_jiazaidonghua);
////        ll_typelist_jiazaidonghua.setVisibility(View.GONE);
//        Animation animation = AnimationUtils.loadAnimation(TypeList.this,R.anim.base_loading_large_anim);
//        iv_typelist_jiazaidonghua.startAnimation(animation);
//
//
//
//        mWebView = (WebView) findViewById(R.id.wv_typelist);
////        wv_lianxiwomen.loadUrl("http://www.baidu.com");
//        typelist.setWebViewClient(new WebViewClient());
//        //得到webview设置
//        WebSettings webSettings = typelist.getSettings();
//        //允许使用javascript
//        webSettings.setJavaScriptEnabled(true);
//
//
//        typelist.loadUrl("http://192.168.1.167:8080/list.html");

//
//        initView();
       preferences = getSharedPreferences("data",MODE_PRIVATE);
        typeliststr = preferences.getString("typelist","");
        lv_typelist  = (ListView) findViewById(R.id.lv_typelist);
         adapter = new TypeListAdapter(TypeList.this, TypeListData.objectFromData(typeliststr).getData(),lv_typelist);
        lv_typelist.setAdapter(adapter);
        lv_typelist.setOnItemClickListener(this);
this.findViewById(R.id.tv_typelist_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onBack();
            }
        });


        this.findViewById(R.id.ll_erweimasaomiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeList.this, CaptureActivity.class);
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

//    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
//    private void initView() {
//        this.mWebView = (WebView) this.findViewById(R.id.wv_typelist);
//        //可以执行javascript
//        this.mWebView.getSettings().setJavaScriptEnabled(true);
//        String url = "http://www.ouzhongiot.com/webpage/machine/list.html";
//        this.mWebView.loadUrl(url);
//        //android添加javascript代码，让H5页面能够调用，第二个参数对应的是H5的
//        this.mWebView.addJavascriptInterface(new PayJavaScriptInterface(), "js");
//    }
//
//    private class PayJavaScriptInterface {
//
//        @JavascriptInterface
//        public void toActivity(String addmachinetype) {
//
//            Intent it = new Intent(TypeList.this, machinelist.class);
//            it.putExtra("addmachinetype",addmachinetype);
//            startActivity(it);
//
//        }
//
//        @JavascriptInterface
//        public void PageLoadAndroid() {
//
//            Log.wtf("函数执行了","PageLoadAndroid");
//            ll_typelist_jiazaidonghua.setVisibility(View.GONE);
//            iv_typelist_jiazaidonghua.clearAnimation();
//
//
//        }
//
//
//
//    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0,R.anim.activity_close);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
   Intent it = new Intent(TypeList.this, machinelist.class);
   it.putExtra("addmachinetype", TypeListData.objectFromData(typeliststr).getData().get(i).getTypeSn().substring(0,2));
   startActivity(it);

    }
}
