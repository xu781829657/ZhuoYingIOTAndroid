package com.ouzhongiot.ozapp.http;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.ouzhongiot.ozapp.tools.LogTools;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AndConnectURL extends HttpConnection {
	private InputStream inputStream = null;
	private HttpURLConnection huc;
	private int code;

	@Override
	public String getRequestManager(String url, Map<String, String> headMap) {
		String result = "";
		try {
			URL u = new URL(url);
			huc = (HttpURLConnection) u.openConnection();
			huc.setConnectTimeout(10 * 1000);
			huc.setReadTimeout(10 * 1000);
			huc.setUseCaches(false);
			huc.setRequestMethod("GET");
			if (headMap != null) {
				Iterator<String> iterator = headMap.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String val = headMap.get(key);
					huc.setRequestProperty(key, val);
				}
			}
			code = huc.getResponseCode();
			if (LogTools.debug)
				LogTools.d("Get Code:" + code);
			inputStream = huc.getInputStream();
			byte[] buff = new byte[1024];
			int t = -1;
			while ((t = inputStream.read(buff)) != -1) {
				result += new String(buff, 0, t, "utf-8");
			}
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (huc != null) {
					huc.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public String postRequestManager(String url, Map<String, String> headMap,
			byte[] postByte) {
		String result = "";
		try {
			URL u = new URL(url);
			huc = (HttpURLConnection) u.openConnection();
			huc.setRequestMethod("POST");
			huc.setConnectTimeout(15 * 1000);
			huc.setReadTimeout(15 * 1000);
			if (headMap != null) {
				Iterator<String> iterator = headMap.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String val = headMap.get(key);
					huc.setRequestProperty(key, val);
				}
			}
			huc.setDoInput(true);
			huc.setDoOutput(true);
			if (postByte != null) {
				huc.getOutputStream().write(postByte);
				huc.getOutputStream().flush();
				huc.getOutputStream().close();
			}
			code = huc.getResponseCode();
			if (LogTools.debug)
				LogTools.d("Post Code:" + code);
			inputStream = huc.getInputStream();
			byte[] buff = new byte[1024];
			int t = -1;
			while ((t = inputStream.read(buff)) != -1) {
				result += new String(buff, 0, t, "utf-8");
			}
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (huc != null) {
					huc.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public synchronized String getRespContent(String encoding)
			throws IOException {
		if (encoding.toLowerCase().equals("gzip")) {
			return getRespContentGzip();
		}
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = inputStream.read()) != -1) {
			bytestream.write(ch);
		}
		byte[] byteContent = bytestream.toByteArray();
		String content = new String(byteContent, encoding);
		return content;
	}

	private String getRespContentGzip() throws IOException {
		GZIPInputStream gzipis = new GZIPInputStream(inputStream);
		InputStreamReader inreader = new InputStreamReader(gzipis, "utf-8");
		char[] bufArr = new char[128];

		StringBuilder strBuilder = new StringBuilder();
		int nBufLen = inreader.read(bufArr);

		String mlinestr;
		while (nBufLen != -1) {
			mlinestr = new String(bufArr, 0, nBufLen);
			strBuilder.append(mlinestr);
			nBufLen = inreader.read(bufArr);
		}
		inreader.close();
		gzipis.close();
		String content = strBuilder.toString();
		return content;
	}

	@Override
	public int getStatus() {
		return code;
	}

	@Override
	public boolean downFile(String url, Map<String, String> headMap,
			String savePath) {
		try {
			URL u = new URL(url);
			HttpURLConnection huc = (HttpURLConnection) u.openConnection();
			huc.setReadTimeout(30 * 1000);
			huc.setConnectTimeout(30 * 1000);
			huc.setRequestMethod("GET");
			if (headMap != null && headMap.size() > 0) {
				Iterator<String> iterator = headMap.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String val = headMap.get(key);
					huc.setRequestProperty(key, val);
				}
			}
			if (LogTools.debug)
				LogTools.d("Down Code:" + huc.getResponseCode());
			inputStream = huc.getInputStream();
			if (inputStream != null) {
				FileOutputStream out = new FileOutputStream(savePath);
				byte[] buff = new byte[1024 * 8];
				int temp = -1;
				while ((temp = inputStream.read(buff)) != -1) {
					out.write(buff, 0, temp);
				}
				out.flush();
				out.close();
				inputStream.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Bitmap downFile(String url, Map<String, String> headMap) {
		try {
			URL u = new URL(url);
			HttpURLConnection huc = (HttpURLConnection) u.openConnection();
			huc.setReadTimeout(30 * 1000);
			huc.setConnectTimeout(30 * 1000);
			huc.setRequestMethod("GET");
			if (headMap != null && headMap.size() > 0) {
				Iterator<String> iterator = headMap.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String val = headMap.get(key);
					huc.setRequestProperty(key, val);
				}
			}
			if (LogTools.debug)
				LogTools.d("Down Code:" + huc.getResponseCode());
			inputStream = huc.getInputStream();
			if (inputStream != null) {
				return BitmapFactory.decodeStream(inputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
