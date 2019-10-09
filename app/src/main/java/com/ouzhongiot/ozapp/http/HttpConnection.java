package com.ouzhongiot.ozapp.http;

import java.util.Map;
import android.graphics.Bitmap;

public abstract class HttpConnection {
	// 请求码
	public abstract int getStatus();

	// get请求
	public abstract String getRequestManager(String url,
			Map<String, String> headMap);

	// post请求
	public abstract String postRequestManager(String url,
			Map<String, String> headMap, byte[] postData);

	public abstract boolean downFile(String url, Map<String, String> headMap,
			String savePath);

	public abstract Bitmap downFile(String url, Map<String, String> headMap);
}
