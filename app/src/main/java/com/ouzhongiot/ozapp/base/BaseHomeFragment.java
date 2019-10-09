package com.ouzhongiot.ozapp.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * @date 创建时间: 2016/11/3
 * @author hxf
 * @Description  Fragment基类
 */

public abstract class BaseHomeFragment extends Fragment {
	public View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(getLayoutResID(), container, false);
		initView();
		initValue();
		return mView;
	}

	// 得到布局
	public abstract int getLayoutResID();

	// 初始化布局
	public abstract void initView();

	// 初始化参数
	public abstract void initValue();
}
