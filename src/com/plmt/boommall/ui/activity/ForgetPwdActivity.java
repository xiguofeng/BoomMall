package com.plmt.boommall.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.User;
import com.plmt.boommall.network.logic.UserLogic;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.utils.UserInfoManager;

public class ForgetPwdActivity extends BaseActivity implements OnClickListener {
	public static final int TIME_UPDATE = 1;

	private LinearLayout mQueryLl;
	private LinearLayout mAuthCodeLl;

	private RelativeLayout mPhoneRl;
	private RelativeLayout mVerCodeRl;
	private RelativeLayout mPwdRl;
	private RelativeLayout mPwdConfirmRl;

	private EditText mPhoneEt;
	private EditText mVerCodeEt;
	private EditText mPwdEt;
	private EditText mPwdConfirmEt;
	private TextView mTimingTv;

	private Context mContext;

	private ImageView mBackIv;

	private String mPhone;
	private String mAuthCode;
	private String mPwd;
	private String mConfirmPwd;

	private String mId;

	private MultiStateView mMultiStateView;
	private CustomProgressDialog mProgressDialog;

	private int mTiming = 30;

	Handler mAuthHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.SEND_AUTHCODE_SUC: {
				if (null != msg.obj) {
					mAuthCode = (String) msg.obj;
				}
				mTimeHandler.sendEmptyMessage(TIME_UPDATE);
				break;
			}
			case UserLogic.SEND_AUTHCODE_FAIL: {
				if (null != msg.obj) {
					Toast.makeText(mContext, (String) msg.obj,
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case UserLogic.SEND_AUTHCODE_EXCEPTION: {
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

	private Handler mTimeHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TIME_UPDATE: {
				if (mTiming > 0) {
					mTiming--;
					mTimingTv.setText(String.valueOf(mTiming) + "秒");
					mAuthCodeLl.setClickable(false);
					mAuthCodeLl
							.setBackgroundResource(R.drawable.corners_bg_gray_all);
					mTimeHandler.sendEmptyMessageDelayed(TIME_UPDATE, 1000);
				} else {
					mAuthCodeLl.setClickable(true);
					mAuthCodeLl
							.setBackgroundResource(R.drawable.corners_bg_red_all);
					mTimingTv
							.setText(getString(R.string.get_verification_code));
					mTiming = 30;
				}
				break;
			}
			default:
				break;
			}
		};

	};

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.MODIFY_PWD_SUC: {
				Toast.makeText(mContext, R.string.modify_pwd_suc,
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
			case UserLogic.MODIFY_PWD_FAIL: {
				Toast.makeText(mContext, R.string.modify_pwd_fail,
						Toast.LENGTH_SHORT).show();
				break;
			}
			case UserLogic.MODIFY_PWD_EXCEPTION: {
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
		setContentView(R.layout.forget_pwd);
		mContext = ForgetPwdActivity.this;
		initView();
		setUpListener();
		initData();
	}

	@Override
	protected void initView() {
		mQueryLl = (LinearLayout) findViewById(R.id.forget_pwd_input_submit_ll);
		mAuthCodeLl = (LinearLayout) findViewById(R.id.forget_pwd_input_ver_code_ll);

		mPhoneEt = (EditText) findViewById(R.id.forget_pwd_input_phone_et);
		mVerCodeEt = (EditText) findViewById(R.id.forget_pwd_input_ver_code_et);
		mPwdEt = (EditText) findViewById(R.id.forget_pwd_input_pwd_et);
		mPwdConfirmEt = (EditText) findViewById(R.id.forget_pwd_input_pwd_confirm_et);

		mTimingTv = (TextView) findViewById(R.id.forget_pwd_input_ver_code_btn_tv);

		mPhoneRl = (RelativeLayout) findViewById(R.id.forget_pwd_input_phone_rl);
		mVerCodeRl = (RelativeLayout) findViewById(R.id.forget_pwd_input_ver_code_rl);
		mPwdRl = (RelativeLayout) findViewById(R.id.forget_pwd_input_pwd_rl);
		mPwdConfirmRl = (RelativeLayout) findViewById(R.id.forget_pwd_input_pwd_confirm_rl);

		mBackIv = (ImageView) findViewById(R.id.forget_pwd_input_back_iv);

		mMultiStateView = (MultiStateView) findViewById(R.id.forget_multiStateView);
		mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR)
				.findViewById(R.id.retry)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMultiStateView
								.setViewState(MultiStateView.VIEW_STATE_LOADING);
						Toast.makeText(getApplicationContext(),
								"Fetching Data", Toast.LENGTH_SHORT).show();
					}
				});
	}

	@SuppressLint("NewApi")
	private void setUpListener() {
		mQueryLl.setOnClickListener(this);
		mAuthCodeLl.setOnClickListener(this);

		mBackIv.setOnClickListener(this);

		mPhoneEt.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					clearBackground();
					mPhoneRl.setBackgroundResource(R.drawable.edittext_red_bg);
					// 此处为得到焦点时的处理内容
				} else {
					// 此处为失去焦点时的处理内容
				}
			}
		});

		mVerCodeEt
				.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							clearBackground();
							mVerCodeRl
									.setBackgroundResource(R.drawable.edittext_red_bg);
							// 此处为得到焦点时的处理内容
						} else {
							// 此处为失去焦点时的处理内容
						}
					}
				});

		mPwdEt.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					clearBackground();
					mPwdRl.setBackgroundResource(R.drawable.edittext_red_bg);
					// 此处为得到焦点时的处理内容
				} else {
					// 此处为失去焦点时的处理内容
				}
			}
		});

		mPwdConfirmEt
				.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							clearBackground();
							mPwdConfirmRl
									.setBackgroundResource(R.drawable.edittext_red_bg);
							// 此处为得到焦点时的处理内容
						} else {
							// 此处为失去焦点时的处理内容
						}
					}
				});
	}

	private void clearBackground() {
		mPhoneRl.setBackgroundResource(R.drawable.edittext_gray_bg);
		mVerCodeRl.setBackgroundResource(R.drawable.edittext_gray_bg);
		mPwdRl.setBackgroundResource(R.drawable.edittext_gray_bg);
		mPwdConfirmRl.setBackgroundResource(R.drawable.edittext_gray_bg);
	}

	private void initData() {
		mId = getIntent().getStringExtra("id");

//		if (!UserInfoManager.getIsMustAuth(mContext)
//				&& !TextUtils.isEmpty(UserInfoManager.getPhone(mContext))) {
//			mPhone = UserInfoManager.getPhone(mContext);
//			mPhoneEt.setText(mPhone);
//		}
	}

	private void sendAuth() {
		mProgressDialog = new CustomProgressDialog(mContext);
		mProgressDialog.show();
		mPhone = mPhoneEt.getText().toString().trim();
		if (!TextUtils.isEmpty(mPhone)) {
			UserLogic.sendAuthCode(mContext, mAuthHandler, mPhone, "1");
		} else {
			Toast.makeText(mContext, R.string.mobile_phone_hint,
					Toast.LENGTH_SHORT).show();
		}
	}

	private void updatePwd() {
		mPhone = mPhoneEt.getText().toString().trim();
		if (TextUtils.isEmpty(mPhone)) {
			Toast.makeText(mContext, getString(R.string.mobile_phone_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}

		mAuthCode = mVerCodeEt.getText().toString().trim();
		if (TextUtils.isEmpty(mAuthCode)) {
			Toast.makeText(mContext,
					getString(R.string.verification_code_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}

		mPwd = mPwdEt.getText().toString().trim();
		mConfirmPwd = mPwdConfirmEt.getText().toString().trim();
		if (TextUtils.isEmpty(mPwd) || TextUtils.isEmpty(mConfirmPwd)) {
			Toast.makeText(mContext, getString(R.string.user_psw_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (!mPwd.equals(mConfirmPwd)) {
			Toast.makeText(mContext,
					getString(R.string.user_confirm_psw_no_same_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}

		mProgressDialog = new CustomProgressDialog(mContext);
		mProgressDialog.show();

		User user = new User();
		user.setPhone(mPhone);
		user.setPassword(mConfirmPwd);
		UserLogic.forgetPwd(mContext, mHandler, user, mAuthCode);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.forget_pwd_input_submit_ll: {
			updatePwd();
			break;
		}
		case R.id.forget_pwd_input_ver_code_ll: {
			mPhone = mPhoneEt.getText().toString().trim();
			if (!TextUtils.isEmpty(mPhone)) {
				sendAuth();
			} else {
				Toast.makeText(mContext, getString(R.string.mobile_phone_hint),
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		case R.id.forget_pwd_input_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			break;
		}

		default:
			break;
		}
	}

}
