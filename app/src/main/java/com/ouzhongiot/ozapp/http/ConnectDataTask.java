package com.ouzhongiot.ozapp.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ouzhongiot.ozapp.tools.SystemTools;


public abstract class ConnectDataTask extends AsyncTask<Object, Void, String> {
	public final static String GET = "GET";
	public final static String POST = "POST";
	public static int ConnectionCode = 10000;
	public final static int ShopCarCode = ConnectionCode++;
	/**
	 */
	public HttpConnection hc;
	/**
	 */
	public ProgressDialog pd;
	public Context context;
	public OnResultDataLintener onResultDataLintener;
	private int code = -1;
	private boolean progress = true;

	public ConnectDataTask(Context context) {
		hc = new AndConnectURL();
		this.context = context;
	}

	public ConnectDataTask(Context context,
			OnResultDataLintener onResultDataLintener) {
		this(context);
		this.onResultDataLintener = onResultDataLintener;
	}

	public ConnectDataTask(Context context,
			OnResultDataLintener onResultDataLintener, int code) {
		this(context, onResultDataLintener);
		this.code = code;
	}

	public ConnectDataTask(Context context,
			OnResultDataLintener onResultDataLintener, boolean progress) {
		this(context, onResultDataLintener);
		this.progress = progress;
	}

	public ConnectDataTask(Context context,
			OnResultDataLintener onResultDataLintener, int code,
			boolean progress) {
		this(context, onResultDataLintener, progress);
		this.code = code;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (progress) {
			pd = new ProgressDialog(context);
			pd.setMessage("数据加载中...");
			pd.setCanceledOnTouchOutside(false);
			pd.show();
			Window window = pd.getWindow();
			WindowManager.LayoutParams params = window.getAttributes();
			params.dimAmount = 0.5f;
			window.setAttributes(params);
		}
		if (!SystemTools.isNetWorkConnected(context)) {
			Toast.makeText(context, "网络检测失败，请检查网络是否正常...", Toast.LENGTH_SHORT)
					.show();
			if (pd != null) {
				pd.dismiss();
			}
		}
	}

	@Override
	protected abstract String doInBackground(Object... params);

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (pd != null) {
			pd.dismiss();
		}
		if (onResultDataLintener != null) {
			try {
				onResultDataLintener.onResult(result, code);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public interface OnResultDataLintener {
		public void onResult(String result, int code) throws Exception;
	}
}
