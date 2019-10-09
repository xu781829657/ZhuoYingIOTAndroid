package com.ouzhongiot.ozapp.activity;

import android.content.Intent;
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
 * @date 创建时间: 2017/4/18
 * @author hxf
 * @Description 重新输入新密码
 */

public class ForgetSetNewPwdActivity extends BaseHomeActivity implements View.OnClickListener, ConnectDataTask.OnResultDataLintener {
    private TextView font_back;//返回
    private TextView tv_back_behind;//返回后面的字体
    private LinearLayout llayout_back;
    private TextView tv_title;//标题

    private EditText edt_new;//新密码
    private EditText edt_confirm;//确认密码
    private Button btn_commit;//提交
    public static final String PARAM_USER_SN = "userSn";
    private String userSn;

    @Override
    public int addContentView() {
        return R.layout.activity_forget_set_newpwd;
    }

    @Override
    public void initView() {
        font_back = (TextView) findViewById(R.id.font_head_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        tv_title = (TextView) findViewById(R.id.txt_head_content);

        edt_new = (EditText) findViewById(R.id.edt_forget_newpwd);
        edt_confirm = (EditText) findViewById(R.id.edt_forget_confirm_pwd);
        btn_commit = (Button) findViewById(R.id.btn_forget_pwd_commit);


    }

    @Override
    public void initValue() {
        font_back.setTypeface(IconfontTools.getTypeface(this));
        tv_back_behind.setText(getString(R.string.txt_back));
        tv_title.setVisibility(View.GONE);

        if (getIntent().getExtras() != null) {
            userSn = getIntent().getStringExtra(PARAM_USER_SN);
        }
        llayout_back.setOnClickListener(this);
        btn_commit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forget_pwd_commit:
                //提交
                verify();
                break;
            case R.id.llayout_back:
                //返回
                finish();
                break;

        }

    }

    public void verify() {
        if (edt_new.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, "新密码不能为空");
        } else if (edt_confirm.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, "确认密码不能为空");

        } else if (!RegularMatchTools.isNumerAndLetter(edt_new.getText().toString().trim())) {
            ToastTools.show(this, "请输入6位数字 字母或组合");

        } else if (!edt_new.getText().toString().trim().equals(edt_confirm.getText().toString().trim())) {
            ToastTools.show(this, "两次密码不一致");
        } else {
            //提交
            if (userSn != null) {
                new HcNetWorkTask(this, this, 1).doPost(UrlConstant.MODIFY_PWD, null, postParams(1).getBytes());
            } else {
                ToastTools.show(this, "userSn为空");
            }
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
            params.put("userSn", userSn);
            params.put("newPassword", edt_new.getText().toString().trim());
            if (LogTools.debug) {
                LogTools.i("忘记密码参数->" + params.toString());
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
                        LogTools.d("忘记密码返回数据->" + result);
                    }
               /*     state：0 - 修改成功

                    state：1 - 用户SN错误或为空

                    state：2 - 新密码为空

                    state：3 - 用户不存在或老密码错误

                    state：4 - 用户不存在*/
                    if (state == 0) {
                        ToastTools.show(this, "成功");
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (state == 1) {
                        ToastTools.show(this, "用户SN错误或为空");
                    } else if (state == 2) {
                        ToastTools.show(this, "新密码为空");
                    } else if (state == 4) {
                        ToastTools.show(this, "用户不存在");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }

    }
}
