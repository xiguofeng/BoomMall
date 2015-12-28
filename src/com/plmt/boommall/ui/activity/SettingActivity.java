package com.plmt.boommall.ui.activity;

import com.plmt.boommall.R;
import com.plmt.boommall.network.logic.UserLogic;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.iosdialog.AlertDialog;
import com.plmt.boommall.utils.UserInfoManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener {

	private Context mContext;

	private RelativeLayout mAboutUsRl;
	private RelativeLayout mCheckVersionRl;

	private TextView mVersionTv;

	private ImageView mBackIv;

	private CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.LOGOUT_SUC: {
				UserInfoManager.clearUserInfo(mContext);
				UserInfoManager.setLoginIn(mContext, false);
				finish();
				break;
			}
			case UserLogic.LOGOUT_FAIL: {
				break;
			}
			case UserLogic.LOGOUT_EXCEPTION: {
				break;
			}
			case UserLogic.NET_ERROR: {
				break;
			}

			default:
				break;
			}

			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		mContext = SettingActivity.this;
		mProgressDialog = new CustomProgressDialog(mContext);
		initView();

	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		mAboutUsRl = (RelativeLayout) findViewById(R.id.setting_about_us_rl);
		mAboutUsRl.setOnClickListener(this);

		mCheckVersionRl = (RelativeLayout) findViewById(R.id.setting_check_version_rl);
		mCheckVersionRl.setOnClickListener(this);
		mVersionTv = (TextView) findViewById(R.id.setting_check_version_number_tv);

		mBackIv = (ImageView) findViewById(R.id.setting_back_iv);
		mBackIv.setOnClickListener(this);
	}

	private void initData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_about_us_rl: {
			Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.setting_check_version_rl: {
			break;
		}
		case R.id.setting_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		}
		case R.id.account_exit_btn: {
			new AlertDialog(SettingActivity.this).builder().setTitle(getString(R.string.prompt))
					.setMsg(getString(R.string.login_exit))
					.setPositiveButton(getString(R.string.confirm), new OnClickListener() {
						@Override
						public void onClick(View v) {
							mProgressDialog.show();
							UserLogic.logout(mContext, mHandler);
						}
					}).setNegativeButton(getString(R.string.cancal), new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();
			break;
		}
		default:
			break;
		}

	}

}
