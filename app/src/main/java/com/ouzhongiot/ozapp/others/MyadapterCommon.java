package com.ouzhongiot.ozapp.others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.ouzhongiot.ozapp.Model.MymachineData;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;

import java.util.List;

/**
 * Created by liu on 2016/5/13.
 */
//异步加载viewlist

public class MyadapterCommon extends BaseAdapter {
    private AsyncImageLoader asyncImageLoader;
    List<MymachineData.DataBean> data;
    Context context;
    private boolean ISFIRSTLOAD = true;
    OZApplication application;
    private ListView listView;

    public MyadapterCommon(Context context, List<MymachineData.DataBean> list, ListView listView, OZApplication application) {
        this.context = context;
        this.data = list;
        asyncImageLoader = new AsyncImageLoader();
        this.listView = listView;
        this.application = application;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.vlist,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.img);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.title);
            viewHolder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        String url = data.get(position).getImageUrl();
        String logourl = data.get(position).getLogoUrl();


        ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.iv,
                R.mipmap.touming1px, R.mipmap.touming1px);
        application.getImageLoader().get(url, listener);

        if (logourl.equals("null")) {
            //没有品牌
        } else {
            ImageLoader.ImageListener listener2 = ImageLoader.getImageListener(viewHolder.iv_logo,
                    R.mipmap.touming1px, R.mipmap.touming1px);
            application.getImageLoader().get(logourl, listener2);
        }


//            viewHolder.iv_logo.setTag(logourl);
//            Drawable cachedImage1 = asyncImageLoader.loadDrawable(logourl, new AsyncImageLoader.ImageCallback() {
//                public void imageLoaded(Drawable imageDrawable, String imageUrl) {
//                    ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
//                    if (imageViewByTag != null) {
//                        imageViewByTag.setImageDrawable(imageDrawable);
//                    }
//                }
//            });
//
//            viewHolder.iv_logo.setImageDrawable(cachedImage1);


//        String typename = data.get(position).getTypeName();
        if (data.get(position).getDefinedName() == null) {
            viewHolder.tv.setText(data.get(position).getTypeName());
        } else {
            viewHolder.tv.setText(data.get(position).getDefinedName());

        }



        return convertView;
    }
//    public void notifyDataSetChange(ListView listView, int position) {
//        ISFIRSTLOAD = false;
//        /**第一个可见的位置**/
//        int firstVisiblePosition = listView.getFirstVisiblePosition();
//        /**最后一个可见的位置**/
//        int lastVisiblePosition = listView.getLastVisiblePosition();
//
//        /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
//        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
//            /**获取指定位置view对象**/
//            View view = listView.getChildAt(position - firstVisiblePosition);
//            getView(position, view, listView);
//        }
//
//    }


    class ViewHolder {
        public ImageView iv;
        public TextView tv;
        public ImageView iv_logo;
    }

}