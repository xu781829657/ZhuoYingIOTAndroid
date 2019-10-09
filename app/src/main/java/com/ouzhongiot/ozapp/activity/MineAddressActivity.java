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
import com.ouzhongiot.ozapp.picker.AddressPickTask;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.RegularMatchTools;
import com.ouzhongiot.ozapp.tools.SpData;
import com.ouzhongiot.ozapp.tools.ToastTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

/**
 * @date 创建时间: 2017/4/13
 * @author hxf
 * @Description 我的地址
 */

public class MineAddressActivity extends BaseHomeActivity implements View.OnClickListener, ConnectDataTask.OnResultDataLintener {
    private LinearLayout llayout_back;
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题

    private EditText edt_name;
    private EditText edt_phone;
    private TextView tv_city;
    private LinearLayout llayout_select_city;
    private EditText edt_code;//邮政编码
    private EditText edt_detail;//详细地址
    private Button btn_save;//保存


    private boolean isHasAddress = false;
    private String address_id;



    @Override
    public int addContentView() {
        return R.layout.activity_mine_address;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        edt_name = (EditText) findViewById(R.id.edt_address_name);
        edt_phone = (EditText) findViewById(R.id.edt_address_phone);
        tv_city = (TextView) findViewById(R.id.tv_address_city);
        edt_code = (EditText) findViewById(R.id.edt_address_code);
        edt_detail = (EditText) findViewById(R.id.edt_address_detail);
        llayout_select_city = (LinearLayout) findViewById(R.id.llayout_address_select_city);
        btn_save = (Button) findViewById(R.id.btn_address_save);


    }

    @Override
    public void initValue() {
        tv_back_behind.setText(getString(R.string.txt_personal_userinfo));
        tv_title.setText(getString(R.string.txt_mine_address_title));
        //查询地址
        new HcNetWorkTask(this, this, 1).doPost(UrlConstant.QUERY_ADDRESS, null, postParams(1).getBytes());

        setClick();


    }

    public void setClick() {
        llayout_back.setOnClickListener(this);
        llayout_select_city.setOnClickListener(this);
        btn_save.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_back:
                //返回
                finish();
                break;
            case R.id.llayout_address_select_city:
                //选择地区
                onAddressPicker();

                break;
            case R.id.btn_address_save:
                //保存
                verify();
                break;
        }

    }

    public void onAddressPicker() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                ToastTools.show(MineAddressActivity.this, "数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                    tv_city.setText(province.getAreaName() + "-" + city.getAreaName());
                } else {
                    tv_city.setText(province.getAreaName() + "-" + city.getAreaName() + "-" + county.getAreaName());
                }
            }
        });
        task.execute("浙江", "杭州市");
    }

    public void verify() {
        if (edt_name.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, "收货人不能为空");
        } else if (edt_phone.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, "联系电话不能为空");
        } else if (!RegularMatchTools.isMobileNO(edt_phone.getText().toString().trim())) {
            ToastTools.show(this, "手机格式错误");
        } else if (tv_city.getText().toString().isEmpty()) {
            ToastTools.show(this, "请选择所在地区");
        } else if (edt_code.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, "邮政编码不能为空");
        } else if (edt_code.getText().toString().trim().length() < 6) {
            ToastTools.show(this, "邮编输入有误");
        } else if (edt_detail.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, "详细信息不能为空");
        } else {
            //请求接口
            new HcNetWorkTask(this, this, 3).doPost(UrlConstant.ADD_ADDRESS, null, postParams(3).getBytes());

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
            //查询地址
            params.put("userSn", SpData.getInstance(this).getData(SpConstant.LOGIN_USERSN).toString());
            if (LogTools.debug) {
                LogTools.i("查询地址参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        } else if (code == 3) {
            if (isHasAddress) {
                //修改地址
                params.put("address.id", address_id);
            }
            params.put("address.userSn", SpData.getInstance(this).getData(SpConstant.LOGIN_USERSN).toString());
            params.put("address.receiverName", edt_name.getText().toString().trim());
            params.put("address.receiverPhone", edt_phone.getText().toString().trim());
            params.put("address.addrProvince", tv_city.getText().toString().trim().split("-")[0]);
            params.put("address.addrCity", tv_city.getText().toString().trim().split("-")[1]);
            if (tv_city.getText().toString().trim().split("-")[2] != null) {
                params.put("address.addrCounty", tv_city.getText().toString().trim().split("-")[2]);
            }
            params.put("address.postcode", edt_code.getText().toString().trim());
            params.put("address.addrDetail", edt_detail.getText().toString().trim());

            if (LogTools.debug) {
                LogTools.i("地址参数->" + params.toString());
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
                    //查询地址
                    if (state == 2) {
                        //没有数据
                        isHasAddress = false;
                        ToastTools.show(this, "暂未添加地址");
                    } else if (state == 0) {
                        //有地址
                        isHasAddress = true;
                        JSONObject addressObject = object.getJSONArray("data").getJSONObject(0);
                        address_id = addressObject.getString("id");
                        edt_name.setText(addressObject.getString("receiverName"));
                        edt_name.setSelection(edt_name.getText().toString().length());
                        edt_phone.setText(addressObject.getString("receiverPhone"));
                        if (addressObject.getString("addrCounty").equals("null")) {
                            tv_city.setText(addressObject.getString("addrProvince") + "-" + addressObject.getString("addrCity"));
                        } else {
                            tv_city.setText(addressObject.getString("addrProvince") + "-" + addressObject.getString("addrCity") + "-" + addressObject.getString("addrCounty"));
                        }
                        edt_code.setText(addressObject.getString("postcode"));
                        edt_detail.setText(addressObject.getString("addrDetail"));

                    }

                } else if (code == 3) {
                    if (state == 0) {
                        ToastTools.show(this, "成功");
                        finish();

                    } else if (state == 1) {
                        ToastTools.show(this, "参数异常");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
