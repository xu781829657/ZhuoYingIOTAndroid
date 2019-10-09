package com.ouzhongiot.ozapp.http;

import java.util.HashMap;
import java.util.Map;
import com.ouzhongiot.ozapp.tools.LogTools;
import android.content.Context;

public class HcNetWorkTask extends ConnectDataTask {

	public HcNetWorkTask(Context context,
			OnResultDataLintener onResultDataLintener) {
		super(context, onResultDataLintener);
	}

	public HcNetWorkTask(Context context,
			OnResultDataLintener onResultDataLintener, int code) {
		super(context, onResultDataLintener, code);
	}

	public HcNetWorkTask(Context context,
			OnResultDataLintener onResultDataLintener, boolean progress) {
		super(context, onResultDataLintener, progress);

	}

	public HcNetWorkTask(Context context,
			OnResultDataLintener onResultDataLintener, int code,
			boolean progress) {
		super(context, onResultDataLintener, code, progress);

	}

	@SuppressWarnings("unchecked")
	@Override
	protected String doInBackground(Object... params) {
		String method = ((String) params[params.length - 1]);
		if (method.equals(GET)) {
			return hc.getRequestManager((String) params[0],
					(HashMap<String, String>) params[1]);
		} else if (method.equals(POST)) {
			return hc.postRequestManager((String) params[0],
					(HashMap<String, String>) params[1], (byte[]) params[2]);
		}
		return null;
	}

	/**
	 * Get请求
	 */
	public void doGet(String url, Map<String, String> headMap) {
		if (LogTools.debug)
			LogTools.d("Get：" + url);
		this.execute(url, headMap, GET);
	}

	/**
	 * Post请求
	 */
	public void doPost(String url, Map<String, String> headMap, byte[] postData) {
		if (LogTools.debug)
			LogTools.d("Post：" + url);
		this.execute(url, headMap, postData, POST);
	}
}
