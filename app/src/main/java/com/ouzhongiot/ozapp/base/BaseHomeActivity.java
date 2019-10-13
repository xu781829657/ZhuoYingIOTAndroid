package com.ouzhongiot.ozapp.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseHomeActivity extends FragmentActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(addContentView());
		initView();
		initValue();
	}

	// 初始化布局
	public abstract int addContentView();

	// 初始化控件
	public abstract void initView();
	
	// 初始化值
	public abstract void initValue();

}
