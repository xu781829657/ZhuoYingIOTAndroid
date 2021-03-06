package com.ouzhongiot.ozapp.others;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ouzhongiot.ozapp.R;

public class AndroidShare extends Dialog implements AdapterView.OnItemClickListener {
	private LinearLayout mLayout;
	private GridView mGridView;
	private float mDensity;
	private String msgText = "感谢您的分享...";
	private String mImgPath;
	private int mScreenOrientation;
	private List<ShareItem> mListData;
	private Handler mHandler = new Handler();

	private Runnable work = new Runnable() {
		public void run() {
			int orient = getScreenOrientation();
			if (orient != mScreenOrientation) {
				if (orient == 0)
					mGridView.setNumColumns(4);
				else {
					mGridView.setNumColumns(6);
				}
				mScreenOrientation = orient;
				((MyAdapter) mGridView.getAdapter()).notifyDataSetChanged();
			}
			mHandler.postDelayed(this, 1000L);
		}
	};

	public AndroidShare(Context context) {
		super(context, R.style.shareDialogTheme);
	}

	public AndroidShare(Context context, int theme, String msgText, final String imgUri) {
		super(context, theme);
		this.msgText = msgText;

		if (Patterns.WEB_URL.matcher(imgUri).matches())
			new Thread(new Runnable() {
				public void run() {
					try {
						mImgPath = getImagePath(imgUri, getFileCache());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		else
			this.mImgPath = imgUri;
	}

	public AndroidShare(Context context, String msgText, final String imgUri) {
		super(context, R.style.shareDialogTheme);
		this.msgText = msgText;

		if (Patterns.WEB_URL.matcher(imgUri).matches())
			new Thread(new Runnable() {
				public void run() {
					try {
						mImgPath = getImagePath(imgUri,getFileCache());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		else
			this.mImgPath = imgUri;
	}

	void init(Context context) {

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		this.mDensity = dm.density;
		this.mListData = new ArrayList<ShareItem>();
		this.mListData.add(new ShareItem("微信", R.mipmap.fengxiang02,
				"com.tencent.mm.ui.tools.ShareImgUI", "com.tencent.mm"));
		this.mListData.add(new ShareItem("朋友圈", R.mipmap.fengxiang01,
				"com.tencent.mm.ui.tools.ShareToTimeLineUI", "com.tencent.mm"));
		this.mListData.add(new ShareItem("qq", R.mipmap.fengxiang04,
				"com.tencent.mobileqq.activity.JumpActivity","com.tencent.mobileqq"));
		this.mListData.add(new ShareItem("qq空间", R.mipmap.fengxiang03,
				"com.qzone.ui.operation.QZonePublishMoodActivity","com.qzone"));
		this.mListData.add(new ShareItem("新浪微博", R.mipmap.fengxiang05,
				"com.sina.weibo.EditActivity", "com.sina.weibo"));
		this.mListData.add(new ShareItem("腾讯微博", R.mipmap.fengxiang06,
				"com.tencent.WBlog.intentproxy.TencentWeiboIntent","com.tencent.WBlog"));
		this.mListData.add(new ShareItem("其他", R.mipmap.sharemore,
				"",""));

		this.mLayout = new LinearLayout(context);
		this.mLayout.setOrientation(LinearLayout.VERTICAL);
		this.mLayout.setLayoutParams(new LinearLayout.LayoutParams(500, ViewGroup.LayoutParams.MATCH_PARENT));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
		params.leftMargin = ((int) (10.0F * this.mDensity));
		params.rightMargin = ((int) (10.0F * this.mDensity));
		this.mLayout.setLayoutParams(params);
		this.mLayout.setBackgroundColor(Color.parseColor("#D9DEDF"));

		this.mGridView = new GridView(context);
		this.mGridView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
		this.mGridView.setGravity(17);
		this.mGridView.setHorizontalSpacing((int) (10.0F * this.mDensity));
		this.mGridView.setVerticalSpacing((int) (10.0F * this.mDensity));
		this.mGridView.setStretchMode(GridView.STRETCH_SPACING);
		this.mGridView.setColumnWidth((int) (90.0F * this.mDensity));

		this.mGridView.setHorizontalScrollBarEnabled(false);
		this.mGridView.setVerticalScrollBarEnabled(false);
		this.mLayout.addView(this.mGridView);
	}

	public List<ComponentName> queryPackage() {
		List<ComponentName> cns = new ArrayList<ComponentName>();
		Intent i = new Intent("android.intent.action.SEND");
		i.setType("image/*");
		List<ResolveInfo> resolveInfo = getContext().getPackageManager().queryIntentActivities(i, 0);
		for (ResolveInfo info : resolveInfo) {
			ActivityInfo ac = info.activityInfo;
			ComponentName cn = new ComponentName(ac.packageName, ac.name);
			cns.add(cn);
		}
		return cns;
	}

	public boolean isAvilible(Context context, String packageName) {
		PackageManager packageManager = context.getPackageManager();

		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		for (int i = 0; i < pinfo.size(); i++) {
			if (((PackageInfo) pinfo.get(i)).packageName.equalsIgnoreCase(packageName))
				return true;
		}
		return false;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Context context = getContext();
		init(context);
		setContentView(this.mLayout);

		getWindow().setGravity(80);

		if (getScreenOrientation() == 0) {
			this.mScreenOrientation = 0;
			this.mGridView.setNumColumns(4);
		} else {
			this.mGridView.setNumColumns(6);
			this.mScreenOrientation = 1;
		}
		this.mGridView.setAdapter(new MyAdapter());
		this.mGridView.setOnItemClickListener(this);

		this.mHandler.postDelayed(this.work, 1000L);

		setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				mHandler.removeCallbacks(work);
			}
		});
	}

	public void show() {
		super.show();
	}

	public int getScreenOrientation() {
		int landscape = 0;
		int portrait = 1;
		Point pt = new Point();
		getWindow().getWindowManager().getDefaultDisplay().getSize(pt);
		int width = pt.x;
		int height = pt.y;
		return width > height ? portrait : landscape;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ShareItem share = (ShareItem) this.mListData.get(position);
		shareMsg(getContext(), "分享到...", this.msgText, this.mImgPath, share);
	}

	private void shareMsg(Context context, String msgTitle, String msgText,
			String imgPath, ShareItem share) {
		if (!share.packageName.isEmpty() && !isAvilible(getContext(), share.packageName)) {
			Toast.makeText(getContext(), "请先安装" + share.title, Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent("android.intent.action.SEND");
		if ((imgPath == null) || (imgPath.equals(""))) {
			intent.setType("text/plain");
		} else {
			File f = new File(imgPath);
			if ((f != null) && (f.exists()) && (f.isFile())) {
				intent.setType("image/png");
				intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
			}
		}

		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if(!share.packageName.isEmpty()) {
			intent.setComponent(new ComponentName(share.packageName,share.activityName));
			context.startActivity(intent);
		}
		else {
			context.startActivity(Intent.createChooser(intent, msgTitle));
		}
	}

	private File getFileCache() {
		File cache = null;

		if (Environment.getExternalStorageState().equals("mounted"))
			cache = new File(Environment.getExternalStorageDirectory() + "/." + getContext().getPackageName());
		else {
			cache = new File(getContext().getCacheDir().getAbsolutePath() + "/." + getContext().getPackageName());
		}
		if ((cache != null) && (!cache.exists())) {
			cache.mkdirs();
		}
		return cache;
	}

	public String getImagePath(String imageUrl, File cache) throws Exception {
		String name = imageUrl.hashCode() + imageUrl.substring(imageUrl.lastIndexOf("."));
		File file = new File(cache, name);

		if (file.exists()) {
			return file.getAbsolutePath();
		}

		URL url = new URL(imageUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		if (conn.getResponseCode() == 200) {
			InputStream is = conn.getInputStream();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			is.close();
			fos.close();

			return file.getAbsolutePath();
		}

		return null;
	}

	private final class MyAdapter extends BaseAdapter {


		public MyAdapter() {
		}

		public int getCount() {
			return mListData.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0L;
		}

		private View getItemView() {
			LinearLayout item = new LinearLayout(getContext());
			item.setOrientation(LinearLayout.VERTICAL);
			int padding = (int) (5.0F * mDensity);
			item.setPadding(padding, padding, padding, padding);
			item.setGravity(17);

			ImageView iv = new ImageView(getContext());
			item.addView(iv);

			iv.setLayoutParams(new LinearLayout.LayoutParams((int)(60.0F*mDensity), (int)(60.0F*mDensity)));
			iv.setId(R.id.iv_share);


			TextView tv = new TextView(getContext());
			item.addView(tv);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
			layoutParams.topMargin = ((int) (5.0F * mDensity));
			tv.setLayoutParams(layoutParams);
			tv.setTextColor(Color.parseColor("#212121"));
			tv.setTextSize(16.0F);
			tv.setId(R.id.tv_share);

			return item;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getItemView();
			}
			ImageView iv = (ImageView) convertView.findViewById(R.id.iv_share);
			TextView tv = (TextView) convertView.findViewById(R.id.tv_share);
			ShareItem item = (ShareItem) mListData.get(position);
			iv.setImageResource(item.logo);
			tv.setText(item.title);
			return convertView;
		}
	}

	private class ShareItem {
		String title;
		int logo;
		String activityName;
		String packageName;

		public ShareItem(String title, int logo, String activityName, String packageName) {
			this.title = title;
			this.logo = logo;
			this.activityName = activityName;
			this.packageName = packageName;
		}
	}
}
