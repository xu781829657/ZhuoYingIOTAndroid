package com.ouzhongiot.ozapp.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.http.ConnectDataTask;
import com.ouzhongiot.ozapp.http.HcNetWorkTask;
import com.ouzhongiot.ozapp.http.PostParamTools;
import com.ouzhongiot.ozapp.tools.IconfontTools;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.ToastTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hxf
 * @date 创建时间: 2017/4/18
 * @Description 重置密码
 */

public class ResetPwdActivity extends BaseHomeActivity implements View.OnClickListener, ConnectDataTask.OnResultDataLintener {
    private LinearLayout llayout_back;
    private TextView icon_back;//返回
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private TextView tv_phone;
    private EditText edt_code;//验证码
    private Button btn_send;//发送
    private Button btn_next;//下一步

    private String phone;//当前登录的手机号
    private String messageCode;//短信验证码
    private String userSn;//用户sn


    private TimeCout timeCout;// 计时器

    @Override
    public int addContentView() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        icon_back = (TextView) findViewById(R.id.font_head_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        tv_phone = (TextView) findViewById(R.id.tv_reset_pwd_phone);
        edt_code = (EditText) findViewById(R.id.edt_reset_pwd_message_code);
        btn_send = (Button) findViewById(R.id.btn_reset_pwd_send_code);
        btn_next = (Button) findViewById(R.id.btn_reset_pwd_next);

    }

    @Override
    public void initValue() {
        icon_back.setTypeface(IconfontTools.getTypeface(this));
        tv_back_behind.setText(getString(R.string.txt_personal_userinfo));
        tv_title.setText(getString(R.string.txt_reset_pwd));

        phone = getSharedPreferences("data", MODE_PRIVATE).getString("phone", "0000000");
        tv_phone.setText(getString(R.string.txt_reset_pwd_phone_hint) + phone);

        llayout_back.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        timeCout = new TimeCout(60000, 1000);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset_pwd_next:
                //下一步
                verify();
                break;
            case R.id.btn_reset_pwd_send_code:
                //获取验证码 开始60s倒计时
                new HcNetWorkTask(this, this, 2).doPost(UrlConstant.REGISTER_SEND_CODE, null, postParams(2).getBytes());
                break;
            case R.id.llayout_back:
                //返回
                finish();
                break;
        }
    }

    public void verify() {
        if (edt_code.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, "验证码不能为空");
        } else if (!edt_code.getText().toString().trim().equals(messageCode)) {
            ToastTools.show(this, "验证码不正确");
        } else {
            //进入到下一个页面
            Intent intent = new Intent(this, ForgetSetNewPwdActivity.class);
            intent.putExtra(ForgetSetNewPwdActivity.PARAM_USER_SN, userSn);
            startActivity(intent);

        }

    }

    private String postParams(int code) {
        Map<String, String> params = new HashMap<String, String>();
        if (code == 2) {
            //发送短信验证码
            params.put("bool", "1");
            params.put("dest", phone);
            if (LogTools.debug) {
                LogTools.i("发送短信验证码参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        }
        return "";
    }

    @Override
    public void onResult(String result, int code) {
        if (!result.isEmpty()) {
            try {
                JSONObject object = new JSONObject(result);
                int state = object.getInt("state");
                if (code == 2) {
                    if (LogTools.debug) {
                        LogTools.d("短信验证码返回数据->" + result);
                    }
                    if (state == 0) {
                        timeCout.start();
                        JSONObject dataObject = object.getJSONObject("data");
                        messageCode = dataObject.getString("code");
                        userSn = dataObject.getString("userSn");
                    } else if (state == 1) {
                        ToastTools.show(this, "参数异常或为空");
                    } else if (state == 2) {
                        ToastTools.show(this, "参数格式不正确");
                    } else if (state == 3) {
                        ToastTools.show(this, "阿里大鱼返回异常");
                    } else if (state == 4) {
                        ToastTools.show(this, "阿里大鱼系统异常");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    class TimeCout extends CountDownTimer {

        public TimeCout(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //倒计时
            btn_send.setClickable(false);
            btn_send.setBackgroundResource(R.drawable.shape_gray_small_corner);
            btn_send.setText(millisUntilFinished / 1000 + getString(R.string.txt_forget_pwd_resend_hint1));

        }

        @Override
        public void onFinish() {
            btn_send.setClickable(true);
            btn_send.setBackgroundResource(R.drawable.shape_blue_small_corner);
            btn_send.setText(getString(R.string.txt_forget_pwd_resend_hint2));

        }
    }
}
