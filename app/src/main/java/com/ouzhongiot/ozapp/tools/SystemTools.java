package com.ouzhongiot.ozapp.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;

public final class SystemTools {
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
	
	/**
	 * 返回当前程序版本号，(系统识别)
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppVersion(Context context) {
		int version = 0;
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			version = pi.versionCode;
			if (version <= 0) {
				return 0;
			}
		} catch (Exception e) {
			Log.e("Version ", "Exception", e);
		}
		return version;
	}

	
	/**
	 * 返回当前程序版本名
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	public static String getSystemTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String time = sdf.format(new java.util.Date());
		return time;
	}

	public static String getYearMonDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new java.util.Date());
		return date;
	}

	/**
	 * 删除文件夹以及目录下的文件
	 * @param filePath
     * @return
     */
	public static boolean deleteDirectory(String filePath) {
		boolean flag = false;
		File dirFile = new File(filePath);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		File[] files = dirFile.listFiles();
		if (files == null) {
			return false;
		} else {
			//遍历删除文件夹下的所有文件(包括子目录)
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					//删除子文件
					flag = deleteFile(files[i].getAbsolutePath());
					if (!flag) break;
				} else {
					//删除子目录
					flag = deleteDirectory(files[i].getAbsolutePath());
					if (!flag)
						break;
				}
			}
		}
		if (!flag) return false;
		//删除当前空目录
		return dirFile.delete();
	}

	/**
	 * 删除单个文件
	 * @param filePath
	 * @return
     */

	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			return file.delete();
		}
		return false;

	}
	
	
}
