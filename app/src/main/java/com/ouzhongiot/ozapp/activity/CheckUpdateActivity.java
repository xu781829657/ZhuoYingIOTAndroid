package com.ouzhongiot.ozapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.service.DownloadService;
import com.ouzhongiot.ozapp.tools.IconfontTools;
import com.ouzhongiot.ozapp.tools.SystemTools;

/**
 * @author hxf
 * @date 创建时间: 2017/3/24
 * @Description 检查更新
 */

public class CheckUpdateActivity extends BaseHomeActivity implements View.OnClickListener {
    private TextView font_back;//返回
    private TextView tv_title;//标题
    private TextView tv_title_right;//标题右侧
    private TextView tv_current_version;//当前版本号
    private Button btn_update;

    public static final String PARAM_ISNEWEST = "param_isnewest";
    private Boolean isNewest = false;

    private LinearLayout ll_main_xiazai;
    private int downloadprogress = 0;
    private int logoweight;

    private ImageView iv_download_logo, iv_download_back;
    private TextView tv_main_xiazaijindu;

    private MsgReceiver msgReceiver;

    @Override
    public int addContentView() {
        return R.layout.activity_check_update;
    }

    public void initView() {
        font_back = (TextView) findViewById(R.id.font_head_back);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        tv_title_right = (TextView) findViewById(R.id.txt_head_right);
        tv_current_version = (TextView) findViewById(R.id.tv_update_version_code);
        btn_update = (Button) findViewById(R.id.btn_update);
        ll_main_xiazai = (LinearLayout) findViewById(R.id.ll_main_xiazai);
        iv_download_logo = (ImageView) findViewById(R.id.iv_download_logo);
        tv_main_xiazaijindu = (TextView) findViewById(R.id.tv_main_xiazaijindu);
        iv_download_back = (ImageView) findViewById(R.id.iv_download_back);
    }

    public void initValue() {
        font_back.setTypeface(IconfontTools.getTypeface(this));
        tv_title.setText(getString(R.string.txt_check_update_title));
        tv_title_right.setVisibility(View.GONE);
        tv_current_version.setText(getString(R.string.txt_current_version) + SystemTools.getAppVersionName(this));
        if (getIntent().getExtras() != null) {
            isNewest = getIntent().getBooleanExtra(PARAM_ISNEWEST, false);
            if (isNewest) {
                //已经是最新版
                btn_update.setVisibility(View.GONE);
            } else {
                btn_update.setVisibility(View.VISIBLE);


            }
        }
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Download_RECEIVER");
        registerReceiver(msgReceiver, intentFilter);

        btn_update.setOnClickListener(this);
        font_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.font_head_back:
                //返回
                finish();
                break;
            case R.id.btn_update:
                //更新
                Intent it = new Intent(CheckUpdateActivity.this, DownloadService.class);
                it.putExtra("apkurl", "http://www.ouzhongiot.com/android/lianxia.apk");
                startService(it);
                break;
        }

    }

    /**
     * 广播接收器
     *
     * @author len
     */
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //拿到进度，更新UI
            int progress = intent.getIntExtra("progress", 0);
            if (ll_main_xiazai.getVisibility() == View.VISIBLE) {

            } else {
                ll_main_xiazai.setVisibility(View.VISIBLE);

            }
            downloadprogress = progress;
            logoweight = iv_download_logo.getWidth();
            tv_main_xiazaijindu.setText(downloadprogress + "%");
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) iv_download_back.getLayoutParams();
            params.width = (int) ((double) downloadprogress / 100 * logoweight - 4);
            iv_download_back.setLayoutParams(params);
        }

    }
}

