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

/**
 * 登录界面
 */
public class RegisterActivity extends BaseActivity implements OnClickListener,
		TextWatcher {
	public static final int TIME_UPDATE = 1;

	private Context mContext;

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

	private String mAccount;
	private String mPassWord;
	private String mPhone;
	private String mAuthCode;

	private User mUser = new User();

	private CustomProgressDialog mProgressDialog;

	private int mTiming = 60;

	Handler mAuthHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.SEND_AUTHCODE_SUC: {
				if (null != msg.obj) {
					mAuthCode = (String) msg.obj;
					UserInfoManager.setPhone(mContext, mPhone);
					UserInfoManager.setIsMustAuth(mContext, false);
				}
				mTimeHandler.sendEmptyMessage(TIME_UPDATE);
				break;
			}
			case UserLogic.SEND_AUTHCODE_FAIL: {
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
					mAuthCodeLl.setBackgroundColor(getResources().getColor(
							R.color.gray_divide_line));
					mTimeHandler.sendEmptyMessageDelayed(TIME_UPDATE, 1000);
				} else {
					mAuthCodeLl.setClickable(true);
					mAuthCodeLl.setBackgroundColor(getResources().getColor(
							R.color.orange_bg));
					mTimingTv
							.setText(getString(R.string.get_verification_code));
					mTiming = 60;
				}
				break;
			}
			default:
				break;
			}
		};

	};

	// 登陆装填提示handler更新主线程，提示登陆状态情况
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.LOGIN_SUC: {
				if (null != msg.obj) {
					String session = (String) msg.obj;
					UserInfoManager.setSession(mContext, session);

					UserLogic.getInfo(mContext, mHandler);
				}

				break;
			}
			case UserLogic.LOGIN_FAIL: {
				Toast.makeText(mContext, R.string.login_fail,
						Toast.LENGTH_SHORT).show();
				break;
			}
			case UserLogic.LOGIN_EXCEPTION: {
				break;
			}
			case UserLogic.USER_INFO_GET_SUC: {
				if (null != msg.obj) {
					mUser = (User) msg.obj;
					mUser.setPassword(mPassWord);

					UserInfoManager.setRememberPwd(mContext, true);
					UserInfoManager.saveUserInfo(RegisterActivity.this, mUser);
					UserInfoManager.setUserInfo(RegisterActivity.this);
					UserInfoManager.setLoginIn(RegisterActivity.this, true);

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

		mPassWordEt.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
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
		// 获取用户的登录信息，连接服务器，获取登录状态
		mAccount = mPhoneEt.getText().toString().trim();
		mPassWord = mPassWordEt.getText().toString().trim();

		if ("".equals(mAccount) || "".equals(mPassWord)) {
			Toast.makeText(RegisterActivity.this,
					mContext.getString(R.string.login_emptyname_or_emptypwd),
					Toast.LENGTH_SHORT).show();
		} else {
			mProgressDialog = new CustomProgressDialog(mContext);
			mProgressDialog.show();

			mPassWord = "admin123";
			User user = new User();
			user.setUsername("13813003736");
			user.setPassword("admin123");
			UserLogic.login(mContext, mHandler, user);
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
		mAccount = mPhoneEt.getText().toString().trim();
		mPassWord = mPassWordEt.getText().toString().trim();
		mAuthCode = mAuthCodeEt.getText().toString().trim();

		if (!TextUtils.isEmpty(mAccount) && !TextUtils.isEmpty(mPassWord)
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
			register();
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
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
