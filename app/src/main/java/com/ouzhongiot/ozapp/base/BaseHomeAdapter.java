package com.ouzhongiot.ozapp.base;

import java.util.List;
import org.json.JSONObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseHomeAdapter extends BaseAdapter {
	public LayoutInflater inflater;
	public List<JSONObject> list;
	public Context context;

	public BaseHomeAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	public BaseHomeAdapter(Context context, List<JSONObject> list) {
		this(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup arg2);


	public interface OnChildItemLinstener {
		public void onChildShop(JSONObject json);
	}
}
