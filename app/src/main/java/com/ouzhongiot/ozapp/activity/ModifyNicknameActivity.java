package com.ouzhongiot.ozapp.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * @date 创建时间: 2017/5/5
 * @Description 修改昵称
 */

public class ModifyNicknameActivity extends BaseHomeActivity implements View.OnClickListener, ConnectDataTask.OnResultDataLintener {
    private LinearLayout llayout_back;
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private EditText edt_nickname;
    private Button btn_commit;
    private String old_nickname;

    public static final String PARAM_TAG = "param_tag";
    private String tag;


    @Override
    public int addContentView() {
        return R.layout.activity_modify_nickname;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        edt_nickname = (EditText) findViewById(R.id.edt_modify_nickname);
        btn_commit = (Button) findViewById(R.id.btn_modify_nickname_commit);


    }

    @Override
    public void initValue() {
        tv_back_behind.setText(getString(R.string.txt_personal_userinfo));
        if (getIntent().getExtras() != null) {
            tag = getIntent().getStringExtra(PARAM_TAG);
            if (tag.equals("email")) {
                //修改邮箱
                tv_title.setText(getString(R.string.txt_modify_email_title));
                if (!SpData.getInstance(this).getData(SpConstant.LOGIN_EMAIL).toString().equals("null")) {
                    edt_nickname.setText(SpData.getInstance(this).getData(SpConstant.LOGIN_EMAIL).toString());
                }
            } else if (tag.equals("nickname")) {
                //修改昵称
                tv_title.setText(getString(R.string.txt_modify_nickname_title));
                if (!SpData.getInstance(this).getData(SpConstant.LOGIN_NICKNAME).toString().equals("null")) {
                    edt_nickname.setText(SpData.getInstance(this).getData(SpConstant.LOGIN_NICKNAME).toString());
                }
            }

            old_nickname = edt_nickname.getText().toString().trim();

        }


        llayout_back.setOnClickListener(this);
        btn_commit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_back:
                //返回
                finish();
                break;
            case R.id.btn_modify_nickname_commit:
                if (tag.equals("nickname")) {
                    //修改昵称
                    if (edt_nickname.getText().toString().trim().equals("")) {
                        ToastTools.show(this, "请输入昵称");
                    } else if (old_nickname.equals(edt_nickname.getText().toString().trim())) {
                        ToastTools.show(this, "请输入不同的昵称");
                    } else {
                        //修改
                        new HcNetWorkTask(this, this, 1).doPost(UrlConstant.MODIFY_USERINFO, null, postParams(1).getBytes());
                    }
                } else if (tag.equals("email")) {
                    //修改邮箱
                    if (edt_nickname.getText().toString().trim().equals("")) {
                        ToastTools.show(this, "请输入要绑定的邮箱");
                    } else if (old_nickname.equals(edt_nickname.getText().toString().trim())) {
                        ToastTools.show(this, "请输入不同的邮箱");
                    } else if (!RegularMatchTools.isEmail(edt_nickname.getText().toString().trim())) {
                        ToastTools.show(this, "邮箱格式有误");
                    } else {
                        //修改
                        new HcNetWorkTask(this, this, 1).doPost(UrlConstant.MODIFY_USERINFO, null, postParams(1).getBytes());
                    }
                }
                break;
        }
    }

    private String postParams(int code) {
        Map<String, String> params = new HashMap<String, String>();
        if (code == 1) {

            params.put("user.sn", SpData.getInstance(this).getData(SpConstant.LOGIN_USERSN).toString());
            if (tag.equals("nickname")) {
                params.put("user.nickname", edt_nickname.getText().toString().trim());
            } else if (tag.equals("email")) {
                params.put("user.email", edt_nickname.getText().toString().trim());
            }
            if (LogTools.debug) {
                LogTools.i("修改参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        }
        return "";
    }

    @Override
    public void onResult(String result, int code) throws Exception {
        if (!result.isEmpty()) {
            try {
                JSONObject object = new JSONObject(result);
                int state = object.getInt("state");
                if (code == 1) {
                    if (LogTools.debug) {
                        LogTools.d("修改返回数据->" + result);
                    }
                    if (state == 0) {
                        //修改成功
                        ToastTools.show(this, "修改成功");
                        if (tag.equals("nickname")) {
                            SpData.getInstance(this).putData(SpConstant.LOGIN_NICKNAME, edt_nickname.getText().toString().trim());
                        } else if (tag.equals("email")) {
                            SpData.getInstance(this).putData(SpConstant.LOGIN_EMAIL, edt_nickname.getText().toString().trim());
                        }
                        setResult(RESULT_OK);
                        finish();

                    } else if (state == 1) {
                        ToastTools.show(this, "参数异常");
                    } else if (state == 2) {
                        ToastTools.show(this, "修改失败");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }
}
