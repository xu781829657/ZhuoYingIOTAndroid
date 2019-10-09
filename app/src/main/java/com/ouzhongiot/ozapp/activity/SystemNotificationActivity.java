package com.ouzhongiot.ozapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.adapter.SystemNotificationListAdapter;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.http.ConnectDataTask;
import com.ouzhongiot.ozapp.http.HcNetWorkTask;
import com.ouzhongiot.ozapp.http.PostParamTools;
import com.ouzhongiot.ozapp.tools.IconfontTools;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.SharedPreferencesTools;
import com.ouzhongiot.ozapp.tools.ToastTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * @author hxf
 * @date 创建时间: 2017/2/6
 * @Description 系统通知
 */

public class SystemNotificationActivity extends Activity implements View.OnClickListener, ConnectDataTask.OnResultDataLintener {
    private TextView font_back;
    private TextView txt_head_content;
    private TextView txt_head_right;

    private PtrClassicFrameLayout mPtrFrame;
    private ListView mListView;
    private SystemNotificationListAdapter mAdapter;

    //暂无数据
    private TextView font_nodata;
    private LinearLayout llayout_nodata;

    private int messageId;

    private int page = 1;
    private boolean isLoadMore = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_notification);
        initView();
        initValue();
        queryData();
    }

    public void initView() {
        font_back = (TextView) findViewById(R.id.font_head_back);
        txt_head_content = (TextView) findViewById(R.id.txt_head_content);
        txt_head_right = (TextView) findViewById(R.id.txt_head_right);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.sys_noti_header_list_view_frame);
        mListView = (ListView) findViewById(R.id.list_system_notification);

        font_nodata = (TextView) findViewById(R.id.font_nodata);
        llayout_nodata = (LinearLayout) findViewById(R.id.llayout_nodata);

    }

    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        Typeface typeface = IconfontTools.getTypeface(this);
        font_back.setTypeface(typeface);
        font_nodata.setTypeface(typeface);

        txt_head_content.setText(getString(R.string.txt_message_system));
//        txt_head_right.setText(getString(R.string.clear_txt));
        txt_head_right.setVisibility(View.GONE);
        font_back.setOnClickListener(this);

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            //加载
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                if (isLoadMore) {
                    queryData();
                }
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 1000);

            }

            //刷新
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                queryData();
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 1000);

            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return super.checkCanDoLoadMore(frame, mListView, footer);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, mListView, header);
            }
        });

        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(1000);
        //默认打开不自动刷新
        mPtrFrame.setPullToRefresh(false);
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 100);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.font_head_back) {
            //返回
            OZApplication.getInstance().finishActivity();
        }
    }

    //请求数据
    public void queryData() {
        new HcNetWorkTask(this, this, 1).doPost(UrlConstant.PUBLIC_MESSAGE, null, postParams(1).getBytes());
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
            //系统通知列表
            params.put("page", String.valueOf(page));
            params.put("rows",
                    "6");
            if (LogTools.debug) {
                LogTools.i("系统通知参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        } else if (code == 2) {
            //增加系统通知阅读数
            params.put("id", String.valueOf(messageId));
            if (LogTools.debug) {
                LogTools.i("通知增加阅读数参数->" + params.toString());
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
                if (code == 1) {
                    //系统通知列表
                    if (LogTools.debug) {
                        LogTools.i("系统通知返回数据->" + result);
                    }
                    if (object.getString("total").equals("null")) {
                        //没有数据
//                        ToastTools.show(this,"暂无数据");
                        if (page == 1) {
                            mPtrFrame.setVisibility(View.GONE);
                            llayout_nodata.setVisibility(View.VISIBLE);
                        } else if (page > 1) {
                            ToastTools.show(this, "没有更多数据");
                        }
                    } else if (object.getInt("total") > 0 && object.getJSONArray("rows").length() > 0) {
                        //有数据
                        if (page == 1) {
                            mPtrFrame.setVisibility(View.VISIBLE);
                            llayout_nodata.setVisibility(View.GONE);
                            mAdapter = new SystemNotificationListAdapter(this, this, object.getJSONArray("rows"));
                            mListView.setAdapter(mAdapter);
                            //把第一页的第一条数据的时间存下来
                            SharedPreferencesTools.getInstance(this).putData(SpConstant.SYSTEM_NOTI_NEWEST_TIME, object.getJSONArray("rows").getJSONObject(0).getString("addTime"));
                        } else {
                            //加载更多
                            mAdapter.addValArray(object.getJSONArray("rows"));
                        }
                        if (object.getJSONArray("rows").length() == 6) {
                            ++page;
                            isLoadMore = true;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    //系统通知增加阅读数
    public void sysNotiAddRead(int messageId, String url) {
        this.messageId = messageId;
        //这个接口没有任何返回值
        new HcNetWorkTask(this, this, 2).doPost(UrlConstant.ADD_PUBLIC_MESSAGE_READ, null, postParams(2).getBytes());
        Intent intent = new Intent(this, SystemNotificationDetailActivity.class);
        intent.putExtra(SystemNotificationDetailActivity.PARAM_URL, url);
        startActivity(intent);
    }
}
