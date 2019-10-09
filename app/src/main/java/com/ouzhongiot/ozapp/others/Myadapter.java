package com.ouzhongiot.ozapp.others;

/**
 * Created by liu on 2016/5/12.
 */

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

        import com.ouzhongiot.ozapp.R;
        import com.ouzhongiot.ozapp.Model.machine;

        import java.util.ArrayList;
        import java.util.List;
//异步加载viewlist
public class Myadapter extends BaseAdapter {
    private AsyncImageLoader asyncImageLoader;
    List<machine> data;
    Context context;

    String addmachinetype;
    private ListView listView;

    public Myadapter(Context context, List<machine> list, ListView listView,String addmachinetype) {
        this.context = context;
        asyncImageLoader = new AsyncImageLoader();
        this.listView=listView;
        this.addmachinetype = addmachinetype;
        this.data = list;

//        Log.wtf("这个是机器大类",addmachinetype);
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
    public View getView(int position,  View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

      ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.vlist,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.img);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }



        int id = data.get(position).getId();
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
        viewHolder.tv.setText(data.get(position).getBrand()+typename);

//        if(!data.get(position).getTypeSn().substring(0,2).equals(addmachinetype))
//        {
//            iv.setVisibility(View.GONE);
//            tv.setVisibility(View.GONE);
//        }
        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}