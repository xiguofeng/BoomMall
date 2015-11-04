package com.plmt.boommall.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.plmt.boommall.R;

public class SplashActivity extends BaseActivity implements OnClickListener {

	public static final String TAG = SplashActivity.class.getSimpleName();

	private final int SPLISH_DISPLAY_LENGTH = 2000; // 延迟2秒启动登陆界面

	private ImageView mEnterIv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		initView();

	}

	protected void findViewById() {
		mEnterIv = (ImageView) findViewById(R.id.splash_jump_iv);
		mEnterIv.setOnClickListener(this);
	}

	protected void initView() {
		// 启动三秒后进度到登陆界面
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				openActivity(HomeActivity.class);
				SplashActivity.this.finish();
			}
		}, SPLISH_DISPLAY_LENGTH);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(SplashActivity.this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(SplashActivity.this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.splash_jump_iv: {
			openActivity(HomeActivity.class);
			SplashActivity.this.finish();
			break;
		}
		default:
			break;
		}
	}

}
