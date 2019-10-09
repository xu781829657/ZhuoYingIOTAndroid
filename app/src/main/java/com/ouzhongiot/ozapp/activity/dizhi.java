package com.ouzhongiot.ozapp.activity;

import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.cascade.model.CityModel;
import com.ouzhongiot.ozapp.cascade.model.DistrictModel;
import com.ouzhongiot.ozapp.cascade.model.ProvinceModel;
import com.ouzhongiot.ozapp.cascade.service.XmlParserHandler;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.Post;
import com.ouzhongiot.ozapp.Model.Address;
import com.ouzhongiot.ozapp.widget.OnWheelChangedListener;
import com.ouzhongiot.ozapp.widget.WheelView;
import com.ouzhongiot.ozapp.widget.adapters.ArrayWheelAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class dizhi extends AppCompatActivity implements OnWheelChangedListener {
    private InputMethodManager manager;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private LinearLayout ll_dizhi_xuanzhelist;
    private TextView tv_dizhi_shengfen;
    private TextView tv_dizhi_shi;
    private TextView tv_dizhi_xiancheng;
    private EditText et_dizhi_jiedao, et_dizhi_shouji, et_dizhi_xingming;
    private String userSn, userId;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String addressdata;
    private int addressid;
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dizhi);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ll_dizhi_xuanzhelist = (LinearLayout) findViewById(R.id.ll_dizhi_xuanzhelist);
        tv_dizhi_shengfen = (TextView) findViewById(R.id.tv_dizhi_shengfen);
        tv_dizhi_shi = (TextView) findViewById(R.id.tv_dizhi_shi);
        tv_dizhi_xiancheng = (TextView) findViewById(R.id.tv_dizhi_xiancheng);
        et_dizhi_jiedao = (EditText) findViewById(R.id.et_dizhi_jiedao);
        et_dizhi_shouji = (EditText) findViewById(R.id.et_dizhi_shouji);
        et_dizhi_xingming = (EditText) findViewById(R.id.et_dizhi_xingming);
        preferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = preferences.edit();
//        userId = preferences.getString("userId", "");
        userSn = preferences.getString("userSn", "");
        addressdata = preferences.getString("address", "");

        if (!addressdata.equals("null")) {
            tv_dizhi_shengfen.setText(preferences.getString("address.shengfeng", "省份"));
            tv_dizhi_shi.setText(preferences.getString("address.shi", "市"));
            tv_dizhi_xiancheng.setText(preferences.getString("address.xiancheng", "县/区"));
            et_dizhi_jiedao.setText(preferences.getString("address.jiedao","请输入街道地址"));
            et_dizhi_xingming.setText(preferences.getString("address.receiverName","请输入收件人姓名"));
            et_dizhi_shouji.setText(preferences.getString("address.receiverPhone","请输入手机或电话号码"));

        }
        setUpViews();
        setUpListener();
        setUpData();
        this.findViewById(R.id.dizhi_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        this.findViewById(R.id.tv_dizhi_queding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_dizhi_shengfen.setText(mCurrentProviceName);
                tv_dizhi_shi.setText(mCurrentCityName);
                tv_dizhi_xiancheng.setText(mCurrentDistrictName);
                ll_dizhi_xuanzhelist.setVisibility(View.GONE);
                ll_dizhi_xuanzhelist.startAnimation(
                        AnimationUtils.loadAnimation(dizhi.this, R.anim.footer_disappear));

            }
        });

        this.findViewById(R.id.ll_dizhi_xuanzhe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_dizhi_xuanzhelist.setVisibility(View.VISIBLE);
                ll_dizhi_xuanzhelist.startAnimation(
                        AnimationUtils.loadAnimation(dizhi.this, R.anim.footer_appear));
            }
        });
        this.findViewById(R.id.tv_dizhi_xuanzhe_quxiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_dizhi_xuanzhelist.setVisibility(View.GONE);
                ll_dizhi_xuanzhelist.startAnimation(
                        AnimationUtils.loadAnimation(dizhi.this, R.anim.footer_disappear));
            }
        });

        this.findViewById(R.id.tv_dizhi_quxiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        this.findViewById(R.id.tv_dizhi_tijiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tv_dizhi_shengfen.getText().equals("省份")) {
                    Toast.makeText(dizhi.this, "请选择省份/市/县城", Toast.LENGTH_SHORT).show();
                } else if (et_dizhi_jiedao.getText().toString().equals("")) {
                    Toast.makeText(dizhi.this, "请输入街道地址", Toast.LENGTH_SHORT).show();
                } else if (et_dizhi_xingming.getText().toString().equals("")) {
                    Toast.makeText(dizhi.this, "请输入收件人姓名", Toast.LENGTH_SHORT).show();
                } else if (et_dizhi_shouji.getText().toString().equals("")) {
                    Toast.makeText(dizhi.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                } else if (addressdata.equals("null")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String uriAPI = MainActivity.ip + "smarthome/user/modifyUserAddress";
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
//                            params.add(new BasicNameValuePair("address.userId", userId));
                            params.add(new BasicNameValuePair("address.userSn", userSn));
                            params.add(new BasicNameValuePair("address.addrProvince", mCurrentProviceName));
                            params.add(new BasicNameValuePair("address.addrCity", mCurrentCityName));
                            params.add(new BasicNameValuePair("address.addrCounty", mCurrentDistrictName));
                            params.add(new BasicNameValuePair("address.addrStreet", et_dizhi_jiedao.getText().toString()));
                            params.add(new BasicNameValuePair("address.receiverName", et_dizhi_xingming.getText().toString()));
                            params.add(new BasicNameValuePair("address.receiverPhone", et_dizhi_shouji.getText().toString()));
                            String str = Post.dopost(uriAPI, params);
                            Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str, Map.class);
                            String state = JacksonUtil.serializeObjectToJson(map.get("state"));
                            if (state.equals("0")) {
                                editor.putString("address.shengfeng", mCurrentProviceName);
                                editor.putString("address.shi", mCurrentCityName);
                                editor.putString("address.xiancheng", mCurrentDistrictName);
                                editor.putString("address.jiedao", et_dizhi_jiedao.getText().toString());
                                editor.putString("address.receiverName", et_dizhi_xingming.getText().toString());
                                editor.putString("address.receiverPhone", et_dizhi_shouji.getText().toString());
                                editor.commit();
                                onBack();
                            } else if (state.equals("1")) {
                                Toast.makeText(dizhi.this, "地址更新失败，请检查网络后重试", Toast.LENGTH_SHORT);
                            }

                        }
                    }).start();
                } else {
                    addressid = Address.arrayAddressFromData(addressdata).get(0).getId();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String uriAPI = MainActivity.ip + "smarthome/user/modifyUserAddress";
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("address.id", addressid + ""));
//                            params.add(new BasicNameValuePair("address.userId", userId));
                            params.add(new BasicNameValuePair("address.userSn", userSn));
                            params.add(new BasicNameValuePair("address.addrProvince", mCurrentProviceName));
                            params.add(new BasicNameValuePair("address.addrCity", mCurrentCityName));
                            params.add(new BasicNameValuePair("address.addrCounty", mCurrentDistrictName));
                            params.add(new BasicNameValuePair("address.addrStreet", et_dizhi_jiedao.getText().toString()));
                            params.add(new BasicNameValuePair("address.receiverName", et_dizhi_xingming.getText().toString()));
                            params.add(new BasicNameValuePair("address.receiverPhone", et_dizhi_shouji.getText().toString()));
                            String str = Post.dopost(uriAPI, params);
                            Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str, Map.class);
                            String state = JacksonUtil.serializeObjectToJson(map.get("state"));
                            if (state.equals("0")) {
                                editor.putString("address.shengfeng", mCurrentProviceName);
                                editor.putString("address.shi", mCurrentCityName);
                                editor.putString("address.xiancheng", mCurrentDistrictName);
                                editor.putString("address.jiedao", et_dizhi_jiedao.getText().toString());
                                editor.putString("address.receiverName", et_dizhi_xingming.getText().toString());
                                editor.putString("address.receiverPhone", et_dizhi_shouji.getText().toString());
                                editor.commit();
                                onBack();
                            } else if (state.equals("1")) {
                                Toast.makeText(dizhi.this, "地址更新失败，请检查网络后重试", Toast.LENGTH_SHORT);
                            }

                        }
                    }).start();

                }

            }
        });


    }

    private void setUpViews() {
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
    }

    private void setUpListener() {
        // ���change�¼�
        mViewProvince.addChangingListener(this);
        // ���change�¼�
        mViewCity.addChangingListener(this);
        // ���change�¼�
        mViewDistrict.addChangingListener(this);
        // ���onclick�¼�
    }

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(dizhi.this, mProvinceDatas));
        // ���ÿɼ���Ŀ����
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }


    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * ��ݵ�ǰ���У�������WheelView����Ϣ
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * ��ݵ�ǰ��ʡ��������WheelView����Ϣ
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

//
//    private void showSelectedResult() {
//        Toast.makeText(dizhi.this, "��ǰѡ��:" + mCurrentProviceName + "," + mCurrentCityName + ","
//                + mCurrentDistrictName + "," + mCurrentZipCode, Toast.LENGTH_SHORT).show();
//    }


    //点击返回上个页面
    public void onBack() {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                } catch (Exception e) {
                    Log.e("Exception when onBack", e.toString());
                }
            }
        }.start();
    }

    //点击空白处回收键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
}
