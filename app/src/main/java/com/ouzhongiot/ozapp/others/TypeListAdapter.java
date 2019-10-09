package com.ouzhongiot.ozapp.others;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ouzhongiot.ozapp.Model.MymachineData;
import com.ouzhongiot.ozapp.Model.TypeListData;
import com.ouzhongiot.ozapp.R;

import java.util.List;

/**
 * Created by liu on 2016/12/2.
 */
public class TypeListAdapter extends BaseAdapter {
    private AsyncImageLoader asyncImageLoader;
    List<TypeListData.DataBean> data;
    Context context;

    private ListView listView;

    public TypeListAdapter(Context context, List<TypeListData.DataBean> list, ListView listView) {
        this.context = context;
        this.data = list;
        asyncImageLoader = new AsyncImageLoader();
        this.listView=listView;
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }



    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.typelist_lv,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.img);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        }
       else{
            viewHolder = (ViewHolder)convertView.getTag();
        }







        String url=data.get(position).getImageUrl();
//        Log.wtf("图片地址",url);
        String typename = data.get(position).getTypeName();
//        Log.wtf("这个是机器名称",typename);
        viewHolder.iv.setTag(url);

        Drawable cachedImage = asyncImageLoader.loadDrawable(url, new AsyncImageLoader.ImageCallback() {
            public void imageLoaded(Drawable imageDrawable, String imageUrl) {
                ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
                if (imageViewByTag != null) {
                    imageViewByTag.setImageDrawable(imageDrawable);
                }
            }
        });
        viewHolder.iv.setImageDrawable(cachedImage);
        viewHolder.tv.setText(data.get(position).getTypeName());


        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}