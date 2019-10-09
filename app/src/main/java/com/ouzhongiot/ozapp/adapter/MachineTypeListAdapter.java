package com.ouzhongiot.ozapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
 * @date 创建时间: 2017/5/11
 * @Description 设备类型列表adapter
 */

public class MachineTypeListAdapter extends ArrayAdapter {
    private DisplayImageOptions options;

    public MachineTypeListAdapter(Context context, JSONArray array) {
        super(context, array);
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.mipmap.img_machine_type_loadfail)
                .showImageOnFail(R.mipmap.img_machine_type_loadfail).build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ItemMachineTypeHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_machine_type, null);
            holder = new ItemMachineTypeHolder();
            holder.img_type = (ImageView) convertView.findViewById(R.id.img_item_machine_type);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_item_machine_type);
            convertView.setTag(holder);

        } else {
            holder = (ItemMachineTypeHolder) convertView.getTag();
        }
        JSONObject item = getValJson(position);
        try {
            holder.tv_type.setText(item.getString("typeName"));
            //显示图片
            ImageLoader.getInstance().displayImage(item.getString("imageUrl"), holder.img_type,options);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;

    }

    class ItemMachineTypeHolder {
        public ImageView img_type;//类型图
        public TextView tv_type;//类型名

    }
}
