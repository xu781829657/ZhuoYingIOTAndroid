package com.ouzhongiot.ozapp.tools;

import android.util.Log;

public class LogTools {

	public static void d(String msg) {
		Log.d(LogTools.class.getName(), msg);
	}

	public final static boolean debug = true;

	public static void i(String msg) {
		Log.i(LogTools.class.getName(), msg);
	}

	public static void e(String msg) {
		Log.e(LogTools.class.getName(), msg);
	}

	public static void v(String msg) {
		Log.v(LogTools.class.getName(), msg);
	}

	public static void w(String msg) {
		Log.w(LogTools.class.getName(), msg);
	}
}
