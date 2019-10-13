package com.ouzhongiot.ozapp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ouzhongiot.ozapp.Model.MachineBean;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.adapter.TypeUnderListAdapter;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.http.ConnectDataTask;
import com.ouzhongiot.ozapp.http.HcNetWorkTask;
import com.ouzhongiot.ozapp.http.PostParamTools;
import com.ouzhongiot.ozapp.tools.LogManager;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.SpData;
import com.ouzhongiot.ozapp.tools.StringUtils;
import com.ouzhongiot.ozapp.web.MachineProductDesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hxf
 * @date 创建时间: 2017/5/11
 * @Description 机器类型下的设备列表
 */

public class MachineTypeUnderListActivity extends BaseHomeActivity implements AdapterView.OnItemClickListener, View.OnClickListener, ConnectDataTask.OnResultDataLintener {
    private LinearLayout llayout_back;
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题

    public static final String PARAM_ALL_MACHINE = "param_all_machine";
    public static final String PARAM_TYPE_SN = "param_type_sn";
    public static final String PARAM_SWITCH_TYPE2 = "param_switch_type2";

    private List<MachineBean> machineLists;//所有设备列表
    private String typeSn;//类型编号(4100)
    private List<MachineBean> typeUnderLists;//有效列表

    private ListView mListView;
    private TypeUnderListAdapter mAdapter;

    private JSONArray machineArray = new JSONArray();

    private String switch_type2;



    @Override
    public int addContentView() {
        return R.layout.activity_machine_type_under_list;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);

        mListView = (ListView) findViewById(R.id.list_type_under);

    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        if (getIntent().getExtras() != null) {
            //machineLists = (List<MachineBean>) getIntent().getSerializableExtra(PARAM_ALL_MACHINE);
            typeSn = getIntent().getStringExtra(PARAM_TYPE_SN);
            switch_type2 = getIntent().getStringExtra(PARAM_SWITCH_TYPE2);
            if (switch_type2.equals("zhanshi")) {
                //展示设备
                tv_back_behind.setText(getString(R.string.txt_back));
                tv_title.setText(getString(R.string.txt_all_machine));

            } else if (switch_type2.equals("add")) {
                //添加设备
                tv_back_behind.setText(getString(R.string.txt_back));
                tv_title.setText(getString(R.string.txt_my_machine_nodata_add));
            }
            //请求该类型下的所有设备
            new HcNetWorkTask(this, this, 1).doPost(UrlConstant.QUERY_ALL_MACHINE, null, postParams(1).getBytes());

        }
        llayout_back.setOnClickListener(this);


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
            params.put("typeSn", typeSn);
            if (LogTools.debug) {
                LogTools.i("列表参数->" + params.toString());
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
                LogManager.d("MachineTypeUnderListActivity onResult"+object.toString());
                int state = object.getInt("state");
                if (code == 1) {
                    if (state == 0) {
                        JSONArray data = object.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
                            LogManager.d("jsonObject:" + jsonObject.toString());
                            String typeName = jsonObject.getString("typeName");
                            if (StringUtils.isNotEmpty(typeName) && typeName.contains("新风机")) {
                                machineArray.put(jsonObject);
                                break;
                            }
                        }

                        mAdapter = new TypeUnderListAdapter(this, machineArray);
                        mListView.setAdapter(mAdapter);
                        mListView.setOnItemClickListener(this);


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (switch_type2.equals("add")) {
            //跳转到设备设置页
            Intent intent = new Intent(this, AddMachineSetActivity.class);
            try {
                if (machineArray.getJSONObject(position).getString("brand").equals("null")) {
                    intent.putExtra(AddMachineSetActivity.PARAM_MACHINE_NAME, machineArray.getJSONObject(position).getString("typeName"));
                } else {
                    intent.putExtra(AddMachineSetActivity.PARAM_MACHINE_NAME, machineArray.getJSONObject(position).getString("brand") + machineArray.getJSONObject(position).getString("typeName"));
                }
                intent.putExtra(AddMachineSetActivity.PARAM_SL_TYPE, machineArray.getJSONObject(position).getString("slType"));
                //下面的一些数据就不传来传去，直接存在本地
                SpData.getInstance(this).putData(SpConstant.ADD_MACHINE_SMARTLINK_PROTOCOL, machineArray.getJSONObject(position).getString("protocol"));
                SpData.getInstance(this).putData(SpConstant.ADD_MACHINE_TYPESN, machineArray.getJSONObject(position).getString("typeSn"));
                SpData.getInstance(this).putData(SpConstant.ADD_MACHINE_BINDURL, machineArray.getJSONObject(position).getString("bindUrl"));
                SpData.getInstance(this).putData(SpConstant.ADD_MACHINE_TYPENUMBER, machineArray.getJSONObject(position).getString("typeNumber"));

                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (switch_type2.equals("zhanshi")) {
            Intent intent = new Intent(this, MachineProductDesActivity.class);
            intent.putExtra(MachineProductDesActivity.PARAM_BIG_TYPE_SN, typeSn);
            try {
                intent.putExtra(MachineProductDesActivity.PARAM_TYPE_NUMBER, machineArray.getJSONObject(position).getString("typeNumber"));
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llayout_back) {
            //返回
            OZApplication.getInstance().finishActivity();
        }

    }
}
