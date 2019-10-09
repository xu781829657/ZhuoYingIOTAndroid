package com.ouzhongiot.ozapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.check.CheckUtil;
import com.ouzhongiot.ozapp.check.CheckView;
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
 * @date 创建时间: 2017/2/13
 * @Description 注册
 */


public class RegisterActivity2 extends Activity implements View.OnClickListener, ConnectDataTask.OnResultDataLintener {
    private TextView font_back;//返回
    private TextView tv_back_behind;//返回后面的字体
    private LinearLayout llayout_back;
    private TextView tv_title;//标题
    private EditText edt_phone;//手机号
    private EditText edt_pwd;//密码
    private EditText edt_sys_code;//验证码
    private CheckView view_sys_code;

    private Button btn_register;//注册
    private TextView tv_user_agree;//用户协议
    private TextView tv_policy;//隐私政策

    private int[] sysCode;
    private String messageCode;//短信验证码
    private String inputCode = "";//用户输入的验证码

    private TimeCout timeCout;// 计时器
    private Button btn_code_resend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        initView();
        initValue();
    }

    public void initView() {
        font_back = (TextView) findViewById(R.id.font_head_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        llayout_back = (LinearLayout)findViewById(R.id.llayout_back);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        edt_phone = (EditText) findViewById(R.id.edt_register_phone);
        edt_pwd = (EditText) findViewById(R.id.edt_register_pwd);
        edt_sys_code = (EditText) findViewById(R.id.edt_register_sys_code);
        view_sys_code = (CheckView) findViewById(R.id.view_register_sys_code);
        btn_register = (Button) findViewById(R.id.btn_register);
        tv_user_agree = (TextView) findViewById(R.id.tv_register_agreement);
        tv_policy = (TextView) findViewById(R.id.tv_register_policy);


    }

    public void initValue() {
        font_back.setTypeface(IconfontTools.getTypeface(this));
        tv_back_behind.setText(getString(R.string.txt_back));
        tv_title.setVisibility(View.GONE);
        timeCout = new TimeCout(60000, 1000);

        sysCode = CheckUtil.getCheckNum();
        view_sys_code.setCheckNum(sysCode);
        view_sys_code.invaliChenkNum();

        llayout_back.setOnClickListener(this);
        view_sys_code.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        tv_user_agree.setOnClickListener(this);
        tv_policy.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register) {
            //注册前的验证
            registerVerify();
        } else if (v.getId() == R.id.view_register_sys_code) {
            //更换系统验证码
            sysCode = CheckUtil.getCheckNum();
            view_sys_code.setCheckNum(sysCode);
            view_sys_code.invaliChenkNum();
        } else if (v.getId() == R.id.llayout_back) {
            //返回
            finish();
        } else if (v.getId() == R.id.tv_register_agreement) {
            //用户协议
//            Intent intent = new Intent(this, argument.class);
            Intent intent = new Intent(this, UserAgreementActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tv_register_policy) {
            //隐私政策
//            Intent intent = new Intent(this, privacy.class);
            Intent intent = new Intent(this, PolicyActivity.class);
            startActivity(intent);
        }

    }

    //注册前的验证
    public void registerVerify() {
        //验证手机号
        if (edt_phone.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, "手机号不能为空");
        } else if (!RegularMatchTools.isMobileNO(edt_phone.getText().toString().trim())) {
            ToastTools.show(this, "手机格式不正确");

        } else if (edt_pwd.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, "密码不能为空");

        } else if (!RegularMatchTools.isNumerAndLetter(edt_pwd.getText().toString().trim())) {
            ToastTools.show(this, "密码格式不正确");
        } else if (edt_sys_code.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, "验证码不能为空");

        } else if (!CheckUtil.checkNum(edt_sys_code.getText().toString().trim(), sysCode)) {
            ToastTools.show(this, "输入的验证码不正确");
        } else {
            //验证用户填入的手机号是否已经注册
            new HcNetWorkTask(this, this, 1).doPost(UrlConstant.REGISTER_QUERY_PHONE, null, postParams(1).getBytes());
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
            params.put("bool", "0");
            params.put("dest", edt_phone.getText().toString().trim());
            if (LogTools.debug) {
                LogTools.i("发送短信验证码参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        } else if (code == 3) {
            //注册
            params.put("user.phone", edt_phone.getText().toString().trim());
            params.put("user.password", edt_pwd.getText().toString().trim());
            if (!MainActivity.getuiClientid.isEmpty()) {
                params.put("ua.clientId", MainActivity.getuiClientid);
            }
            params.put("ua.phoneType", "1");
            params.put("ua.phoneBrand", android.os.Build.BRAND);
            params.put("ua.phoneModel", android.os.Build.MODEL);
            //获取手机系统版本(例如：4.4.2)
            params.put("ua.phoneSystem", android.os.Build.VERSION.RELEASE);

            if (LogTools.debug) {
                LogTools.i("注册参数->" + params.toString());
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
                        //发送短信验证码
                        new HcNetWorkTask(this, this, 2).doPost(UrlConstant.REGISTER_SEND_CODE, null, postParams(2).getBytes());
                        //弹出输入验证码的dialog
                        showMessageCodeDialog();


                    } else if (state == 1) {
                        ToastTools.show(this, "参数异常或为空");
                    } else if (state == 2) {
                        ToastTools.show(this, "参数格式不正确");
                    } else if (state == 3) {
                        ToastTools.show(this, "该用户已存在");
                    }
                } else if (code == 2) {
                    //发送短信验证码
                    if (LogTools.debug) {
                        LogTools.d("发送短信验证码返回数据->" + result);
                    }
                    if (state == 0) {
                        JSONObject dataObject = object.getJSONObject("data");
                        messageCode = dataObject.getString("code");

                    } else if (state == 1) {
                        ToastTools.show(this, "参数异常或为空");
                    } else if (state == 2) {
                        ToastTools.show(this, "参数格式不正确");
                    } else if (state == 3) {
                        ToastTools.show(this, "阿里大鱼返回异常");
                    } else if (state == 4) {
                        ToastTools.show(this, "阿里大鱼系统异常");
                    }

                } else if (code == 3) {
                    //注册
                    if (LogTools.debug) {
                        LogTools.d("注册返回数据->" + result);
                    }
                    if (state == 0) {
                        //注册成功
                        ToastTools.show(this, "注册成功");
                        Intent intent = new Intent(RegisterActivity2.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        ToastTools.show(this,"注册失败");
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void showMessageCodeDialog() {
        final Dialog codeDialog = new AlertDialog.Builder(this).create();
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
        tv_code_phone.setText("验证码已发送至 " + edt_phone.getText().toString().trim());
        btn_code_resend = (Button) codelayout.findViewById(R.id.btn_code_resend);
        //开始倒计时
        timeCout.start();

        final EditText edt1 = (EditText) codelayout.findViewById(R.id.edt_code_num1);
        final EditText edt2 = (EditText) codelayout.findViewById(R.id.edt_code_num2);
        final EditText edt3 = (EditText) codelayout.findViewById(R.id.edt_code_num3);
        final EditText edt4 = (EditText) codelayout.findViewById(R.id.edt_code_num4);
        final EditText edt5 = (EditText) codelayout.findViewById(R.id.edt_code_num5);
        final EditText edt6 = (EditText) codelayout.findViewById(R.id.edt_code_num6);
//        showKeyboard(edt1);
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
                        //校验用户输入的验证码是否正确
                        if (inputCode.equals(messageCode)) {
                            codeDialog.dismiss();
                            //开始请求注册接口
                            new HcNetWorkTask(RegisterActivity2.this, RegisterActivity2.this, 3).doPost(UrlConstant.REGISTER, null, postParams(3).getBytes());
                        } else {
                            //验证码输入有误
                            ToastTools.show(RegisterActivity2.this, "输入的验证码有误");
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
                    }
                }


            }
        });
        edt2.addTextChangedListener(new TextWatcher() {
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
                        //校验用户输入的验证码是否正确
                        if (inputCode.equals(messageCode)) {
                            codeDialog.dismiss();
                            //开始请求注册接口
                            new HcNetWorkTask(RegisterActivity2.this, RegisterActivity2.this, 3).doPost(UrlConstant.REGISTER, null, postParams(3).getBytes());
                        } else {
                            //验证码输入有误
                            ToastTools.show(RegisterActivity2.this, "输入的验证码有误");
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


                            edt1.setText("");
                            edt2.setText("");
                            edt3.setText("");
                            edt4.setText("");
                            edt5.setText("");
                            edt6.setText("");
                        }
                    }
                }

            }
        });
        edt3.addTextChangedListener(new TextWatcher() {
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
                        //校验用户输入的验证码是否正确
                        if (inputCode.equals(messageCode)) {
                            codeDialog.dismiss();
                            //开始请求注册接口
                            new HcNetWorkTask(RegisterActivity2.this, RegisterActivity2.this, 3).doPost(UrlConstant.REGISTER, null, postParams(3).getBytes());
                        } else {
                            //验证码输入有误
                            ToastTools.show(RegisterActivity2.this, "输入的验证码有误");
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


                            edt1.setText("");
                            edt2.setText("");
                            edt3.setText("");
                            edt4.setText("");
                            edt5.setText("");
                            edt6.setText("");
                        }
                    }
                }

            }
        });
        edt4.addTextChangedListener(new TextWatcher() {
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
                        //校验用户输入的验证码是否正确
                        if (inputCode.equals(messageCode)) {
                            codeDialog.dismiss();
                            //开始请求注册接口
                            new HcNetWorkTask(RegisterActivity2.this, RegisterActivity2.this, 3).doPost(UrlConstant.REGISTER, null, postParams(3).getBytes());
                        } else {
                            //验证码输入有误
                            ToastTools.show(RegisterActivity2.this, "输入的验证码有误");
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


                            edt1.setText("");
                            edt2.setText("");
                            edt3.setText("");
                            edt4.setText("");
                            edt5.setText("");
                            edt6.setText("");
                        }
                    }
                }

            }
        });
        edt5.addTextChangedListener(new TextWatcher() {
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
                        //校验用户输入的验证码是否正确
                        if (inputCode.equals(messageCode)) {
                            codeDialog.dismiss();
                            //开始请求注册接口
                            new HcNetWorkTask(RegisterActivity2.this, RegisterActivity2.this, 3).doPost(UrlConstant.REGISTER, null, postParams(3).getBytes());
                        } else {
                            //验证码输入有误
                            ToastTools.show(RegisterActivity2.this, "输入的验证码有误");
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


                            edt1.setText("");
                            edt2.setText("");
                            edt3.setText("");
                            edt4.setText("");
                            edt5.setText("");
                            edt6.setText("");
                        }
                    }
                }

            }
        });
        edt6.addTextChangedListener(new TextWatcher() {
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
                        //校验用户输入的验证码是否正确
                        if (inputCode.equals(messageCode)) {
                            codeDialog.dismiss();
                            //开始请求注册接口
                            new HcNetWorkTask(RegisterActivity2.this, RegisterActivity2.this, 3).doPost(UrlConstant.REGISTER, null, postParams(3).getBytes());
                        } else {
                            //验证码输入有误
                            ToastTools.show(RegisterActivity2.this, "输入的验证码有误");
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


                            edt1.setText("");
                            edt2.setText("");
                            edt3.setText("");
                            edt4.setText("");
                            edt5.setText("");
                            edt6.setText("");
                        }
                    }
                }

            }
        });


    }

    public void showKeyboard(EditText editText) {
        if (editText != null) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            //请求获得焦点
            editText.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(editText, 0);
        }
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


