package com.ouzhongiot.ozapp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ouzhongiot.ozapp.Model.MachineBean;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.adapter.MachineTypeListAdapter;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.http.ConnectDataTask;
import com.ouzhongiot.ozapp.http.HcNetWorkTask;
import com.ouzhongiot.ozapp.tools.LogManager;
import com.ouzhongiot.ozapp.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author hxf
 * @date 创建时间: 2017/5/11
 * @Description 设备类型列表
 */

public class MachineTypeListActivity extends BaseHomeActivity implements ConnectDataTask.OnResultDataLintener, AdapterView.OnItemClickListener, View.OnClickListener {
    private LinearLayout llayout_back;
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题

    private ListView mListView;
    private MachineTypeListAdapter mAdapter;

    private List<MachineBean> machineLists;
    private JSONArray typeArray = new JSONArray();

    private ImageView img_guide2;
    public static final String PARAM_SWITCH_TYPE = "param_switch_type";//区分是“添加设备”还是“展示列表”
    private String switch_type;


    @Override
    public int addContentView() {
        return R.layout.activity_machine_type_list;
    }

    @Override
    public void initView() {
        mListView = (ListView) findViewById(R.id.list_machine_type);
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        img_guide2 = (ImageView) findViewById(R.id.img_guide_add_machine_2);
    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        if (getIntent().getExtras() != null) {
            switch_type = getIntent().getStringExtra(PARAM_SWITCH_TYPE);
            if (switch_type.equals("zhanshi")) {
                //展示列表
                tv_back_behind.setText(getString(R.string.txt_product_instructions));
                tv_title.setText(getString(R.string.txt_all_machine));

            } else if (switch_type.equals("add")) {
                //添加设备
                tv_back_behind.setText(getString(R.string.app_name));
                tv_title.setText(getString(R.string.txt_my_machine_nodata_add));
                if (MyMachineActivity.isGuide) {
                    MyMachineActivity.isGuide = false;
                    img_guide2.setVisibility(View.VISIBLE);
                    img_guide2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //消失
                            img_guide2.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }

        llayout_back.setOnClickListener(this);

        //查询设备类型列表
        new HcNetWorkTask(this, this, 2,false).doPost(UrlConstant.QUERY_ALL_TYPE, null, null);


    }


    @Override
    public void onResult(String result, int code) {
        if (!result.isEmpty()) {
            try {
                JSONObject object = new JSONObject(result);
                LogManager.d("MachineTypeListActivity onResult"+object.toString());
                int state = object.getInt("state");
                if (code == 2) {
                    //查询所有类型
                    if (state == 0) {
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
                            LogManager.d("jsonObject:" + jsonObject.toString());
                            String typeName = jsonObject.getString("typeName");
                            if (StringUtils.isEquals(typeName,"空气净化器")) {
                                typeArray.put(jsonObject);
                                break;
                            }
                        }
                        mAdapter = new MachineTypeListAdapter(this, typeArray);
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
        Intent intent = new Intent(this, MachineTypeUnderListActivity.class);
        try {
            intent.putExtra(MachineTypeUnderListActivity.PARAM_TYPE_SN, typeArray.getJSONObject(position).getString("typeSn"));
            intent.putExtra(MachineTypeUnderListActivity.PARAM_SWITCH_TYPE2, switch_type);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
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
