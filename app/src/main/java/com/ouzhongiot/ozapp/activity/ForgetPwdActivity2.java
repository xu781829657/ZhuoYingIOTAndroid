package com.ouzhongiot.ozapp.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ouzhongiot.ozapp.tools.RegularMatchTools;
import com.ouzhongiot.ozapp.tools.ToastTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hxf
 * @date 创建时间: 2017/4/18
 * @Description 忘记密码
 */

public class ForgetPwdActivity2 extends BaseHomeActivity implements View.OnClickListener, ConnectDataTask.OnResultDataLintener {
    private TextView font_back;//返回
    private TextView tv_back_behind;//返回后面的字体
    private LinearLayout llayout_back;
    private TextView tv_title;//标题
    private EditText edt_phone;//手机号
    private EditText edt_code;//验证码
    private Button btn_send;//发送
    private Button btn_next;//下一步

    private String inputPhone;//用户输入的手机号
    private boolean isCheckPhone = false;//校验输入的手机号是否已经校验
    private String messageCode;//短信验证码
    private String userSn;//用户sn


    private TimeCout timeCout;// 计时器


    @Override
    public int addContentView() {
        return R.layout.activity_forget_pwd2;
    }

    @Override
    public void initView() {
        font_back = (TextView) findViewById(R.id.font_head_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        edt_phone = (EditText) findViewById(R.id.edt_forget_pwd_phone);
        edt_code = (EditText) findViewById(R.id.edt_forget_pwd_message_code);
        btn_send = (Button) findViewById(R.id.btn_forget_pwd_send_code);
        btn_next = (Button) findViewById(R.id.btn_forget_pwd_next);


    }

    @Override
    public void initValue() {
        font_back.setTypeface(IconfontTools.getTypeface(this));
        tv_back_behind.setText(getString(R.string.txt_back));
        tv_title.setVisibility(View.GONE);
        //"短信验证"按钮不可点击
        btn_send.setClickable(false);
        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputPhone = s.toString();
                if (inputPhone.length() == 11 && RegularMatchTools.isMobileNO(inputPhone)) {
                    //输入11位并且格式正确
                    isCheckPhone = true;
                    //校验输入的手机号是否已经注册
                    new HcNetWorkTask(ForgetPwdActivity2.this, ForgetPwdActivity2.this, 1).doPost(UrlConstant.REGISTER_QUERY_PHONE, null, postParams(1).getBytes());
                }

            }
        });

        edt_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //得到焦点
                    ToastTools.show(ForgetPwdActivity2.this, "请输入您的手机号");
                } else {
                    //失去焦点
                    if (edt_phone.getText().toString().trim().length() < 11 || !RegularMatchTools.isMobileNO(edt_phone.getText().toString().trim())) {
                        ToastTools.show(ForgetPwdActivity2.this, "输入的手机号不正确");
                    } else {
                        //输入的都正确
                        if (isCheckPhone) {
                            //校验过手机号就不用校验了
                        } else {
                            //校验
                            new HcNetWorkTask(ForgetPwdActivity2.this, ForgetPwdActivity2.this, 1).doPost(UrlConstant.REGISTER_QUERY_PHONE, null, postParams(1).getBytes());

                        }
                    }
                }
            }
        });

        timeCout = new TimeCout(60000, 1000);

        llayout_back.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forget_pwd_next:
                //下一步
                verify();
                break;
            case R.id.btn_forget_pwd_send_code:
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
        if (edt_phone.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, "手机号不能为空");
        } else if (!RegularMatchTools.isMobileNO(edt_phone.getText().toString().trim())) {
            ToastTools.show(this, "手机格式不正确");

        } else if (edt_code.getText().toString().trim().isEmpty()) {
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

    /**
     * post参数
     *
     * @param code
     * @return
     */
    private String postParams(int code) {
        Map<String, String> params = new HashMap<String, String>();
        if (code == 1) {
            // 注册之前查询手机
            params.put("phone", edt_phone.getText().toString().trim());
            if (LogTools.debug) {
                LogTools.i("注册之前查询手机参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        } else if (code == 2) {
            //发送短信验证码
            params.put("bool", "1");
            params.put("dest", edt_phone.getText().toString().trim());
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
                if (code == 1) {
                    if (LogTools.debug) {
                        LogTools.d("验证手机号返回数据->" + result);
                    }
                    //验证手机号
//                    state：0 - 新用户未注册
//
//                    state：1 - 参数异常或为空
//
//                    state：2 - 参数格式不正确
//
//                    state：3 - 该用户已存在
                    if (state == 0) {
                        ToastTools.show(this, "您输入的手机号还未注册");
                    } else if (state == 1) {
                        ToastTools.show(this, "参数异常或为空");
                    } else if (state == 2) {
                        ToastTools.show(this, "参数格式不正确");
                    } else if (state == 3) {
//                        ToastTools.show(this, "该用户已存在");
                        //改变“发送短信”按钮的状态
                        btn_send.setClickable(true);
                        btn_send.setBackgroundResource(R.drawable.shape_blue_small_corner);
                    }
                } else if (code == 2) {
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
