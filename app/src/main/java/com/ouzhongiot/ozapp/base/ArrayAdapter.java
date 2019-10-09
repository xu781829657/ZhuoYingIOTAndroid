package com.ouzhongiot.ozapp.base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ouzhongiot.ozapp.tools.ToastTools;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class ArrayAdapter extends BaseHomeAdapter {
	public JSONArray valArray;
	public int selection = 0;
	public ArrayAdapter(Context context, JSONArray array) {
		super(context);
		this.valArray = array;
	}

	@Override
	public int getCount() {
		return valArray.length();
	}

	@Override
	public Object getItem(int arg0) {
		try {
			return valArray.get(arg0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void setSelection(int selection) {
		this.selection = selection;
		notifyDataSetChanged();
	}

	public void setValArray(JSONArray valArray) {
		this.valArray = valArray;
		notifyDataSetChanged();
	}

	public JSONArray getValArray() {
		return valArray;
	}

	public void clearArray() {
		valArray = new JSONArray();
		notifyDataSetChanged();
	}

	public JSONObject getValJson(int index) {
		try {
			return valArray.getJSONObject(index);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void addValArray(JSONArray valArray){
		for (int i = 0,j = valArray.length(); i < j; i++) {
			try {
				this.valArray.put(valArray.getJSONObject(i));
			} catch (JSONException e) {
				e.printStackTrace();
				ToastTools.show(context, "增加数组异常");
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup arg2);
}
