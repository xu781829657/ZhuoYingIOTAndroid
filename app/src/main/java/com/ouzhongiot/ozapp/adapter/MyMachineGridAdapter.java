package com.ouzhongiot.ozapp.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author hxf
 * @date 创建时间: 2017/4/21
 * @Description 绑定的设备列表adapter
 */


public class MyMachineGridAdapter extends ArrayAdapter {
    private DisplayImageOptions options;

    public MyMachineGridAdapter(Context context, JSONArray array) {
        super(context, array);
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.mipmap.default_machine)
                .showImageOnFail(R.mipmap.default_machine).build();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        MyMachineItemHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grid_my_machine, null);
            holder = new MyMachineItemHolder();
            holder.img_machine = (ImageView) convertView
                    .findViewById(R.id.img_item_my_machine);
            holder.tv_machine_name = (TextView) convertView
                    .findViewById(R.id.tv_item_my_machine_name);
            holder.tv_machine_type_num = (TextView) convertView
                    .findViewById(R.id.tv_item_my_machine_type_num);
            holder.tv_isOnline = (TextView) convertView
                    .findViewById(R.id.tv_item_my_machine_isonline);
            convertView.setTag(holder);
        } else {
            holder = (MyMachineItemHolder) convertView.getTag();
        }
        JSONObject item = getValJson(position);
        try {
            //显示设备图片
            ImageLoader.getInstance().displayImage(item.optString("imageUrl"),
                    holder.img_machine, options);
            /*关于设备名称的显示问题
            1）先判断brand字段是否为空
                不为空：
                        判断definedName字段是否为空
                            不为空:brand+definedName
                            为空:brand+typeName
                为空：
                        判断definedName字段是否为空
                             不为空:definedName
                               为空:typeName
             */

            if (item.getString("brand").equals("null")) {
                if ((item.getString("definedName").equals("null"))) {
                    holder.tv_machine_name.setText(item.getString("typeName"));
                } else {
                    holder.tv_machine_name.setText(item.getString("definedName"));
                }
            } else {
                if ((item.getString("definedName").equals("null"))) {
                    holder.tv_machine_name.setText(item.getString("brand") + item.getString("typeName"));
                } else {
                    holder.tv_machine_name.setText(item.getString("brand") + item.getString("definedName"));
                }
            }
            holder.tv_machine_type_num.setText("No." + (position + 1));
            if (item.getBoolean("ifConn")) {
                holder.tv_isOnline.setText("在线");
                holder.tv_isOnline.setTextColor(context.getResources().getColor(R.color.main_theme_color));

            } else {
                holder.tv_isOnline.setText("离线");
                holder.tv_isOnline.setTextColor(context.getResources().getColor(R.color.content_black_color));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public class MyMachineItemHolder {
        public ImageView img_machine;// 设备图
        public TextView tv_machine_name;// 设备名
        public TextView tv_machine_type_num;// 同一设备类型下的产品数
        public TextView tv_isOnline;//是否在线
    }
}
