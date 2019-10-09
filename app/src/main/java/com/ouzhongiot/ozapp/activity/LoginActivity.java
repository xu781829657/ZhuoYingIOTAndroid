package com.ouzhongiot.ozapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.http.ConnectDataTask;
import com.ouzhongiot.ozapp.http.HcNetWorkTask;
import com.ouzhongiot.ozapp.http.PostParamTools;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.RegularMatchTools;
import com.ouzhongiot.ozapp.tools.SpData;
import com.ouzhongiot.ozapp.tools.ToastTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hxf
 * @date 创建时间: 2017/4/18
 * @Description 新的登录页
 */

public class LoginActivity extends BaseHomeActivity implements View.OnClickListener, ConnectDataTask.OnResultDataLintener {
    private EditText edt_account;//账号
    private EditText edt_pwd;//密码
    private Button btn_login;//登录
    private Button btn_forget_pwd;//忘记密码
    private Button btn_register;//注册

    private Button btn_code_resend;
    EditText edt1, edt2, edt3, edt4, edt5, edt6;
    Dialog codeDialog;
    private TimeCout timeCout;// 计时器
    private String inputCode = "";//用户输入的验证码


    @Override
    public int addContentView() {
        return R.layout.activity_login3;
    }

    @Override
    public void initView() {
        edt_account = (EditText) findViewById(R.id.edt_login_account);
        edt_pwd = (EditText) findViewById(R.id.edt_login_pwd);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_forget_pwd = (Button) findViewById(R.id.btn_login_forget_pwd);
        btn_register = (Button) findViewById(R.id.btn_login_immediately_register);

    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        setClick();
        timeCout = new TimeCout(60000, 1000);


    }

    //设置点击事件
    public void setClick() {
        btn_login.setOnClickListener(this);
        btn_forget_pwd.setOnClickListener(this);
        btn_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //登录
//                verify();
                verify2();
                break;
            case R.id.btn_login_forget_pwd:
                //忘记密码
                startActivity(new Intent(this, ForgetPwdActivity2.class));
                break;
            case R.id.btn_login_immediately_register:
                //立即注册
                startActivity(new Intent(this, RegisterActivity2.class));
                break;

        }
    }

    //登录前的验证
    public void verify() {
        MainActivity.getuiClientid = PushManager.getInstance().getClientid(getApplication().getApplicationContext());
        if (LogTools.debug) {
            LogTools.i("个推信息->" + MainActivity.getuiClientid);
        }
        if (edt_account.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, getString(R.string.txt_login_account_hint));
        } else if (edt_pwd.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, getString(R.string.txt_login_pwd_hint));
        } else {
            //请求登录接口
            new HcNetWorkTask(this, this, 1).doPost(UrlConstant.LOGIN, null, postParams(1).getBytes());

        }


    }

    //获取短信验证码前的校验
    public void verify2() {
        MainActivity.getuiClientid = PushManager.getInstance().getClientid(getApplication().getApplicationContext());
        if (LogTools.debug) {
            LogTools.i("个推信息->" + MainActivity.getuiClientid);
        }

        if (edt_account.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, "请输入您的手机号");
        } else if (!RegularMatchTools.isMobileNO(edt_account.getText().toString().trim())) {
            ToastTools.show(this, "输入的手机格式不正确");
        } else {
            //获取短信验证码
            new HcNetWorkTask(this, this, 2).doPost(UrlConstant.REGISTER_SEND_CODE, null, postParams(2).getBytes());
            //弹出输入验证码的dialog
            showMessageCodeDialog();


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
            // 登录
            params.put("loginName", edt_account.getText().toString().trim());
            params.put("code", inputCode);
            if (!MainActivity.getuiClientid.isEmpty()) {
                params.put("ua.clientId", MainActivity.getuiClientid);
            }
            params.put("ua.phoneType", "1");
            params.put("ua.phoneBrand", android.os.Build.BRAND);
            params.put("ua.phoneModel", android.os.Build.MODEL);
            //获取手机系统版本(例如：4.4.2)
            params.put("ua.phoneSystem", android.os.Build.VERSION.RELEASE);

            if (LogTools.debug) {
                LogTools.i("登录参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        } else if (code == 2) {
            //发送短信验证码
            params.put("bool", "0");
            params.put("dest", edt_account.getText().toString().trim());
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
                        LogTools.d("登录返回数据->" + result);
                    }
                    if (state == 0) {
                        //登录成功，把返回的数据存在本地
                        codeDialog.dismiss();
                        JSONObject data = object.getJSONObject("data");
                        SpData.getInstance(this).putData(SpConstant.LOGIN_USERNAME, edt_account.getText().toString().trim());
//                        SpData.getInstance(this).putData(SpConstant.LOGIN_PWD, data.getString("password"));
                        SpData.getInstance(this).putData(SpConstant.LOGIN_USERSN, data.getString("sn"));
                        SpData.getInstance(this).putData(SpConstant.LOGIN_PHONE, data.getString("phone"));
                        SpData.getInstance(this).putData(SpConstant.LOGIN_ID, data.getString("id"));
                        SpData.getInstance(this).putData(SpConstant.LOGIN_EMAIL, data.getString("email"));
                        SpData.getInstance(this).putData(SpConstant.LOGIN_NICKNAME, data.getString("nickname"));
                        SpData.getInstance(this).putData(SpConstant.LOGIN_SEX, data.getString("sex"));
                        SpData.getInstance(this).putData(SpConstant.LOGIN_BIRTHDATE, data.getString("birthdate"));
                        SpData.getInstance(this).putData(SpConstant.LOGIN_HEADURL, data.getString("headImageUrl"));

                        if (SpData.getInstance(this).getData(SpConstant.LOCATION_CITY).equals("")) {
                            //显示定位失败的Dialog
                        }
                        //跳转到“我的设备列表”页
                        ((OZApplication) getApplication()).setLogin(true);
                        startActivity(new Intent(this, MyMachineActivity.class));
                        OZApplication.getInstance().finishActivity();


                    } else if (state == 1) {
                        ToastTools.show(this, getString(R.string.txt_login_fail1));
                    } else if (state == 2) {
                        //验证码不匹配
                        ToastTools.show(this, "输入的验证码错误");
                        edt1.setText("");
                        edt2.setText("");
                        edt3.setText("");
                        edt4.setText("");
                        edt5.setText("");
                        edt6.setText("");
                        inputCode = "";
                        edt1.setFocusable(true);

                        edt1.setFocusableInTouchMode(true);

                        edt1.requestFocus();

                        edt1.findFocus();

                        edt2.setFocusable(true);

                        edt2.setFocusableInTouchMode(true);


                        edt3.setFocusable(true);

                        edt3.setFocusableInTouchMode(true);


                        edt4.setFocusable(true);

                        edt4.setFocusableInTouchMode(true);

                        edt5.setFocusable(true);

                        edt5.setFocusableInTouchMode(true);

                        edt6.setFocusable(true);

                        edt6.setFocusableInTouchMode(true);
                    }

                } else if (code == 2) {
                    //发送短信验证码
                    if (LogTools.debug) {
                        LogTools.d("发送短信验证码返回数据->" + result);
                    }
                    if (state == 0) {
//                        JSONObject dataObject = object.getJSONObject("data");
//                        messageCode = dataObject.getString("code");

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

    public void showMessageCodeDialog() {
        codeDialog = new AlertDialog.Builder(this).create();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final RelativeLayout codelayout = (RelativeLayout) layoutInflater.inflate(
                R.layout.dialog_input_message_code, null);
        codeDialog.show();
        codeDialog.setCanceledOnTouchOutside(false);

        //加上下面这样一行代码：dialog中的edittext就可以弹出输入法了
        codeDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        codeDialog.getWindow().setContentView(codelayout);
        TextView tv_close = (TextView) codelayout.findViewById(R.id.tv_code_close);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭
                codeDialog.dismiss();
            }
        });
        TextView tv_code_phone = (TextView) codelayout.findViewById(R.id.tv_code_phone);
        tv_code_phone.setText("验证码已发送至 " + edt_account.getText().toString().trim());

        btn_code_resend = (Button) codelayout.findViewById(R.id.btn_code_resend);
        btn_code_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeCout.start();

            }
        });
        //开始倒计时
        timeCout.start();

        edt1 = (EditText) codelayout.findViewById(R.id.edt_code_num1);
        edt2 = (EditText) codelayout.findViewById(R.id.edt_code_num2);
        edt3 = (EditText) codelayout.findViewById(R.id.edt_code_num3);
        edt4 = (EditText) codelayout.findViewById(R.id.edt_code_num4);
        edt5 = (EditText) codelayout.findViewById(R.id.edt_code_num5);
        edt6 = (EditText) codelayout.findViewById(R.id.edt_code_num6);
        edt1.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            if (s.toString().length() == 1) {
                                                inputCode = inputCode + s.toString();
                                                edt1.setFocusable(false);
                                                if (inputCode.length() == 6) {
                                                    //我这边不用校验 用户输入的验证码是否正确 直接请求登录接口
                                                    new HcNetWorkTask(LoginActivity.this, LoginActivity.this, 1).doPost(UrlConstant.LOGIN_SIMPLE, null, postParams(1).getBytes());

                                                }
                                            }
                                        }


                                    }

        );
        edt2.addTextChangedListener(new

                                            TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    if (s.toString().length() == 1) {
                                                        inputCode = inputCode + s.toString();
                                                        edt2.setFocusable(false);
                                                        if (inputCode.length() == 6) {
                                                            new HcNetWorkTask(LoginActivity.this, LoginActivity.this, 1).doPost(UrlConstant.LOGIN_SIMPLE, null, postParams(1).getBytes());
                                                        }
                                                    }
                                                }

                                            }

        );
        edt3.addTextChangedListener(new

                                            TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    if (s.toString().length() == 1) {
                                                        inputCode = inputCode + s.toString();
                                                        edt3.setFocusable(false);
                                                        if (inputCode.length() == 6) {
                                                            new HcNetWorkTask(LoginActivity.this, LoginActivity.this, 1).doPost(UrlConstant.LOGIN_SIMPLE, null, postParams(1).getBytes());
                                                        }
                                                    }
                                                }
                                            }

        );

        edt4.addTextChangedListener(new

                                            TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    if (s.toString().length() == 1) {
                                                        inputCode = inputCode + s.toString();
                                                        edt4.setFocusable(false);
                                                        if (inputCode.length() == 6) {
                                                            new HcNetWorkTask(LoginActivity.this, LoginActivity.this, 1).doPost(UrlConstant.LOGIN_SIMPLE, null, postParams(1).getBytes());
                                                        }
                                                    }
                                                }

                                            }

        );
        edt5.addTextChangedListener(new

                                            TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    if (s.toString().length() == 1) {
                                                        inputCode = inputCode + s.toString();
                                                        edt5.setFocusable(false);
                                                        if (inputCode.length() == 6) {
                                                            new HcNetWorkTask(LoginActivity.this, LoginActivity.this, 1).doPost(UrlConstant.LOGIN_SIMPLE, null, postParams(1).getBytes());
                                                        }
                                                    }
                                                }

                                            }

        );
        edt6.addTextChangedListener(new

                                            TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    if (s.toString().length() == 1) {
                                                        inputCode = inputCode + s.toString();
                                                        edt6.setFocusable(false);
                                                        if (inputCode.length() == 6) {
                                                            new HcNetWorkTask(LoginActivity.this, LoginActivity.this, 1).doPost(UrlConstant.LOGIN_SIMPLE, null, postParams(1).getBytes());
                                                        }
                                                    }
                                                }

                                            }

        );


    }

    class TimeCout extends CountDownTimer {

        public TimeCout(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //倒计时
            btn_code_resend.setClickable(false);
            btn_code_resend.setBackgroundResource(R.drawable.shape_message_code_btn_countdown);
            btn_code_resend.setText(millisUntilFinished / 1000 + "s后重发");
            btn_code_resend.setTextColor(getResources().getColor(R.color.nine_txt_color));

        }

        @Override
        public void onFinish() {
            btn_code_resend.setClickable(true);
            btn_code_resend.setBackgroundResource(R.drawable.shape_message_code_btn_resend);
            btn_code_resend.setText("重新发送");
            btn_code_resend.setTextColor(getResources().getColor(R.color.six_txt_color));

        }
    }

}
