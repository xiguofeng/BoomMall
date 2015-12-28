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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountActivity extends Activity implements OnClickListener {

	private Context mContext;

	private RelativeLayout mAreaRl;
	private RelativeLayout mRealNameAuthRl;
	private RelativeLayout mCollectionRl;
	private RelativeLayout mModifyPwdRl;
	
	private TextView mRealNameAuthTv;

	private Button mExitBtn;

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
		setContentView(R.layout.account);
		mContext = AccountActivity.this;
		mProgressDialog = new CustomProgressDialog(mContext);
		initView();
		

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		mAreaRl = (RelativeLayout) findViewById(R.id.account_address_rl);
		mAreaRl.setOnClickListener(this);

		mRealNameAuthRl = (RelativeLayout) findViewById(R.id.account_real_name_auth_rl);
		mRealNameAuthRl.setOnClickListener(this);
		mRealNameAuthTv = (TextView) findViewById(R.id.account_real_name_auth_tag_tv);

		mCollectionRl = (RelativeLayout) findViewById(R.id.account_collection_rl);
		mCollectionRl.setOnClickListener(this);
		
		mModifyPwdRl= (RelativeLayout) findViewById(R.id.account_modify_pwd_rl);
		mModifyPwdRl.setOnClickListener(this);

		mBackIv = (ImageView) findViewById(R.id.account_back_iv);
		mBackIv.setOnClickListener(this);

		mExitBtn = (Button) findViewById(R.id.account_exit_btn);
		mExitBtn.setOnClickListener(this);
	}

	private void initData() {
		if("0".equals(UserInfoManager.userInfo.getIs_authentication())){
			mRealNameAuthTv.setText("实名认证(已认证)");
		}
		//mProgressDialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.account_address_rl: {
			Intent intent = new Intent(AccountActivity.this,
					AddressListActivity.class);
			intent.setAction(AddressListActivity.ORIGIN_FROM_ACCOUNT_KEY);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.account_real_name_auth_rl: {
			Intent intent = new Intent(AccountActivity.this,
					RealNameAuthActivity.class);
			intent.setAction(RealNameAuthActivity.ORIGIN_FROM_ACCOUNT_KEY);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.account_collection_rl: {
			Intent intent = new Intent(AccountActivity.this,
					CollectionListActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.account_modify_pwd_rl: {
			Intent intent = new Intent(AccountActivity.this,
					ForgetPwdActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.account_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			break;
		}
		case R.id.account_exit_btn: {
			new AlertDialog(AccountActivity.this)
					.builder()
					.setTitle(getString(R.string.prompt))
					.setMsg(getString(R.string.login_exit))
					.setPositiveButton(getString(R.string.confirm),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									mProgressDialog.show();
									UserLogic.logout(mContext, mHandler);
								}
							})
					.setNegativeButton(getString(R.string.cancal),
							new OnClickListener() {
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
