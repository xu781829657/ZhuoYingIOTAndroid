package com.ouzhongiot.ozapp.tools;

import android.content.Context;
import android.widget.Toast;
/**
 * @date 创建时间: 2016/11/2
 * @author hxf
 * @Description Toast工具类
 */

public class ToastTools {

	public static void show(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}

	public static void show(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}
}
