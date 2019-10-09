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
 * @Description 某个类型列表adapter
 */

public class TypeUnderListAdapter extends ArrayAdapter {
    private DisplayImageOptions options;

    public TypeUnderListAdapter(Context context, JSONArray array) {
        super(context, array);

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.mipmap.default_machine)
                .showImageOnFail(R.mipmap.default_machine).build();

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemMachineHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_machine, null);
            holder = new ItemMachineHolder();
            holder.img_machine = (ImageView) convertView.findViewById(R.id.img_item_machine);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_machine_name);
            convertView.setTag(holder);

        } else {
            holder = (ItemMachineHolder) convertView.getTag();
        }
        JSONObject item = getValJson(position);
        try {
            if (item.getString("brand").equals("null")) {
                holder.tv_name.setText(item.getString("typeName"));
            } else {
                holder.tv_name.setText(item.getString("brand") + item.getString("typeName"));
            }
            ImageLoader.getInstance().displayImage(item.getString("imageUrl"), holder.img_machine, options);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;

    }

    class ItemMachineHolder {
        public ImageView img_machine;
        public TextView tv_name;

    }
}
