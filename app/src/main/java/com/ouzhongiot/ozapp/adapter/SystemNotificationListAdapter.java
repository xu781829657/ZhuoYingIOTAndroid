package com.ouzhongiot.ozapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.SystemNotificationActivity;
import com.ouzhongiot.ozapp.base.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author hxf
 * @date 创建时间: 2017/2/8
 * @Description 系统通知 Adapter
 */

public class SystemNotificationListAdapter extends ArrayAdapter {
    private SystemNotificationActivity activity;

    public SystemNotificationListAdapter(Context context, Activity mActivity, JSONArray array) {
        super(context, array);
        activity = (SystemNotificationActivity) mActivity;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemSysNotiViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_system_notification, null);
            holder = new ItemSysNotiViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_sys_noti_item_title);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_sys_noti_time);
            holder.tv_read = (TextView) convertView.findViewById(R.id.tv_sys_noti_readcount);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_sys_noti_item_content);
            holder.llayout_enter = (LinearLayout) convertView.findViewById(R.id.llayout_sys_noti_enter);
            convertView.setTag(holder);

        } else {
            holder = (ItemSysNotiViewHolder) convertView.getTag();
        }
        final JSONObject item = getValJson(position);
        try {
            holder.tv_title.setText(item.getString("title"));
            holder.tv_time.setText(item.getString("addTime"));
            holder.tv_read.setText("阅读量：" + String.valueOf(item.getInt("readCount")));
            holder.tv_content.setText(item.getString("content"));


            //点击进入详情
            holder.llayout_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        activity.sysNotiAddRead(item.getInt("id"), item.getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    class ItemSysNotiViewHolder {
        public TextView tv_title;//标题
        public TextView tv_time;//时间
        public TextView tv_read;//阅读量
        public TextView tv_content;//内容
        public LinearLayout llayout_enter;

    }
}
