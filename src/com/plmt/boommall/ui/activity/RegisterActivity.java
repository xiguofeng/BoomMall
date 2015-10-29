package com.plmt.boommall.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.plmt.boommall.utils.UserInfoManager;

/**
 * 登录界面
 */
public class RegisterActivity extends BaseActivity implements OnClickListener,
		TextWatcher {
	public static final int TIME_UPDATE = 1;

	private Context mContext;

	private ImageView mBackIv;

	private EditText mPhoneEt;
	private EditText mPassWordEt;
	private EditText mAuthCodeEt;
	private EditText mPwdConfirmEt;

	private TextView mTimingTv;

	private Button mRegisterBtn;

	private RelativeLayout mPhoneRl;
	private RelativeLayout mVerCodeRl;
	private RelativeLayout mPwdRl;
	private RelativeLayout mPwdConfirmRl;

	private LinearLayout mAuthCodeLl;

	private String mPhone;
	private String mPassWord;
	private String mConfirmPwd;
	private String mAuthCode;

	private User mUser = new User();

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
			case UserLogic.REGIS_SUC: {
				Toast.makeText(mContext, R.string.register_suc,
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
			case UserLogic.REGIS_FAIL: {
				Toast.makeText(mContext, R.string.register_fail,
						Toast.LENGTH_SHORT).show();
				break;
			}
			case UserLogic.REGIS_EXCEPTION: {
				break;
			}
			case UserLogic.USER_INFO_GET_SUC: {
				if (null != msg.obj) {
					mUser = (User) msg.obj;
					mUser.setPassword(mPassWord);

					// UserInfoManager.setRememberPwd(mContext, true);
					// UserInfoManager.saveUserInfo(RegisterActivity.this,
					// mUser);
					// UserInfoManager.setUserInfo(RegisterActivity.this);
					// UserInfoManager.setLoginIn(RegisterActivity.this, true);

					handle();
				}

				break;

			}
			case UserLogic.USER_INFO_GET_FAIL: {
				break;
			}
			case UserLogic.USER_INFO_GET_EXCEPTION: {
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
		setContentView(R.layout.register);
		mContext = RegisterActivity.this;
		initView();
		initData();

	}

	protected void initView() {
		mBackIv = (ImageView) findViewById(R.id.register_input_back_iv);
		mBackIv.setOnClickListener(this);

		mPhoneRl = (RelativeLayout) findViewById(R.id.register_username_rl);
		mVerCodeRl = (RelativeLayout) findViewById(R.id.register_ver_code_rl);
		mPwdRl = (RelativeLayout) findViewById(R.id.register_pwd_rl);
		mPwdConfirmRl = (RelativeLayout) findViewById(R.id.register_pwd_confirm_rl);

		mAuthCodeLl = (LinearLayout) findViewById(R.id.registerg_ver_code_get_ll);
		mAuthCodeLl.setOnClickListener(this);

		mPhoneEt = (EditText) findViewById(R.id.register_username_et);
		mPassWordEt = (EditText) findViewById(R.id.register_pwd_et);
		mAuthCodeEt = (EditText) findViewById(R.id.register_ver_code_et);
		mPwdConfirmEt = (EditText) findViewById(R.id.register_pwd_confirm_et);
		mPhoneEt.addTextChangedListener(this);
		mPassWordEt.addTextChangedListener(this);
		mAuthCodeEt.addTextChangedListener(this);

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

		mAuthCodeEt
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

		mPassWordEt
				.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
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

		mTimingTv = (TextView) findViewById(R.id.register_input_ver_code_btn_tv);

		mRegisterBtn = (Button) findViewById(R.id.register_input_submit_btn);
		mRegisterBtn.setOnClickListener(this);
		mRegisterBtn.setClickable(false);

	}

	private void clearBackground() {
		mPhoneRl.setBackgroundResource(R.drawable.edittext_gray_bg);
		mVerCodeRl.setBackgroundResource(R.drawable.edittext_gray_bg);
		mPwdRl.setBackgroundResource(R.drawable.edittext_gray_bg);
		mPwdConfirmRl.setBackgroundResource(R.drawable.edittext_gray_bg);
	}

	private void initData() {
		if (UserInfoManager.getRememberPwd(mContext)) {
			UserInfoManager.setUserInfo(mContext);

			mPhoneEt.setText(UserInfoManager.userInfo.getUsername());
			mPassWordEt.setText(UserInfoManager.userInfo.getPassword());
		}

	}

	private void register() {
		mPhone = mPhoneEt.getText().toString().trim();
		if (TextUtils.isEmpty(mPhone)) {
			Toast.makeText(mContext, getString(R.string.mobile_phone_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}

		mAuthCode = mAuthCodeEt.getText().toString().trim();
		if (TextUtils.isEmpty(mAuthCode)) {
			Toast.makeText(mContext,
					getString(R.string.verification_code_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}

		mPassWord = mPassWordEt.getText().toString().trim();
		mConfirmPwd = mPwdConfirmEt.getText().toString().trim();
		if (TextUtils.isEmpty(mPassWord) || TextUtils.isEmpty(mConfirmPwd)) {
			Toast.makeText(mContext, getString(R.string.user_psw_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (!mPassWord.equals(mConfirmPwd)) {
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
		UserLogic.register(mContext, mHandler, user, mAuthCode);

	}

	private void sendAuth() {
		mProgressDialog = new CustomProgressDialog(mContext);
		mProgressDialog.show();
		mPhone = mPhoneEt.getText().toString().trim();
		if (!TextUtils.isEmpty(mPhone)) {
			UserLogic.sendAuthCode(mContext, mAuthHandler, mPhone, "0");
		} else {
			Toast.makeText(mContext, R.string.mobile_phone_hint,
					Toast.LENGTH_SHORT).show();
		}
	}

	private void handle() {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@SuppressLint("NewApi")
	@Override
	public void afterTextChanged(Editable s) {
		mPhone = mPhoneEt.getText().toString().trim();
		mPassWord = mPassWordEt.getText().toString().trim();
		mAuthCode = mAuthCodeEt.getText().toString().trim();

		if (!TextUtils.isEmpty(mPhone) && !TextUtils.isEmpty(mPassWord)
				&& !TextUtils.isEmpty(mAuthCode)) {
			mRegisterBtn.setClickable(true);
			mRegisterBtn.setBackground(mContext.getResources().getDrawable(
					R.drawable.corners_bg_red_all));
		} else {
			mRegisterBtn.setClickable(false);
			mRegisterBtn.setBackground(mContext.getResources().getDrawable(
					R.drawable.corners_bg_gray_all));
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_input_submit_btn: {
			register();
			break;
		}
		case R.id.registerg_ver_code_get_ll: {
			sendAuth();
			break;
		}
		case R.id.register_input_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			break;
		}

		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			RegisterActivity.this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
