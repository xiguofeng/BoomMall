package com.plmt.boommall.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public abstract class BaseActivity extends Activity {

	public static final String TAG = BaseActivity.class.getSimpleName();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	/**
	 * 初始化控件
	 */
	protected abstract void initView();

	/**
	 * 通过类名启动Activity
	 * 
	 * @param pClass
	 */
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	/**
	 * 通过类名启动Activity，并且含有Bundle数据
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 通过Action启动Activity
	 * 
	 * @param pAction
	 */
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	/**
	 * 通过Action启动Activity，并且含有Bundle数据
	 * 
	 * @param pAction
	 * @param pBundle
	 */
	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 带有action
	 * 
	 * @param pAction
	 * @param pBundle
	 */
	protected void openActivity(Class<?> pClass, String action, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (!TextUtils.isEmpty(action)) {
			intent.setAction(action);
		}
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

}
