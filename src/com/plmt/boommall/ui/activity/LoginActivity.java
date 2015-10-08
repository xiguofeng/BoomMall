package com.plmt.boommall.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
public class LoginActivity extends BaseActivity implements OnClickListener,
		TextWatcher {
	public static final String ORIGIN_FROM_NULL = "com.null";

	public static final String ORIGIN_FROM_REG_KEY = "com.reg";

	public static final String ORIGIN_FROM_CART_KEY = "com.cart";

	public static final String ORIGIN_FROM_ORDER_KEY = "com.order";

	public static final String ORIGIN_FROM_USER_KEY = "com.user";

	private Context mContext;

	private EditText mAccountEt;
	private EditText mPassWordEt;
	private CheckBox mRemberpswCb;
	// private LinearLayout layoutProcess;
	private Button mLoginBtn;

	private LinearLayout mWechatLogin;
	private LinearLayout mWeiboLogin;
	private LinearLayout mQQLogin;

	private LinearLayout mRegisterLl;
	private LinearLayout mForgetPwdLl;

	private String mAccount;
	private String mPassWord;

	private User mUser = new User();

	private MultiStateView mMultiStateView;

	private CustomProgressDialog mProgressDialog;

	private String mNowAction = ORIGIN_FROM_NULL;

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
					UserInfoManager.saveUserInfo(LoginActivity.this, mUser);
					UserInfoManager.setUserInfo(LoginActivity.this);
					UserInfoManager.setLoginIn(LoginActivity.this, true);

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
		setContentView(R.layout.login);
		mContext = LoginActivity.this;
		initView();
		initData();

	}

	protected void initView() {
		mWechatLogin = (LinearLayout) findViewById(R.id.login_wechat_login_ll);
		mWeiboLogin = (LinearLayout) findViewById(R.id.login_weibo_login_ll);
		mQQLogin = (LinearLayout) findViewById(R.id.login_qq_login_ll);
		mWechatLogin.setOnClickListener(this);
		mWeiboLogin.setOnClickListener(this);
		mQQLogin.setOnClickListener(this);

		mRegisterLl = (LinearLayout) findViewById(R.id.login_reg_ll);
		mForgetPwdLl = (LinearLayout) findViewById(R.id.login_forget_pwd_ll);
		mRegisterLl.setOnClickListener(this);
		mForgetPwdLl.setOnClickListener(this);

		mAccountEt = (EditText) findViewById(R.id.login_username);
		mPassWordEt = (EditText) findViewById(R.id.login_password);
		mAccountEt.addTextChangedListener(this);
		mPassWordEt.addTextChangedListener(this);

		mLoginBtn = (Button) findViewById(R.id.login_btn);
		mLoginBtn.setOnClickListener(this);
		mLoginBtn.setClickable(false);

		mMultiStateView = (MultiStateView) findViewById(R.id.login_multiStateView);
		mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR)
				.findViewById(R.id.retry)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMultiStateView
								.setViewState(MultiStateView.VIEW_STATE_LOADING);
						Toast.makeText(getApplicationContext(), "正在",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void initData() {
		mNowAction = getIntent().getAction();

		if (UserInfoManager.getRememberPwd(mContext)) {
			UserInfoManager.setUserInfo(mContext);

			mAccountEt.setText(UserInfoManager.userInfo.getUsername());
			mPassWordEt.setText(UserInfoManager.userInfo.getPassword());
		}

	}

	private void login() {
		// 获取用户的登录信息，连接服务器，获取登录状态
		mAccount = mAccountEt.getText().toString().trim();
		mPassWord = mPassWordEt.getText().toString().trim();

		if ("".equals(mAccount) || "".equals(mPassWord)) {
			Toast.makeText(LoginActivity.this,
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
		if (mNowAction.equals(ORIGIN_FROM_NULL)) {
			// Intent intent = new Intent(LoginActivity.this,
			// HomeActivity.class);
			// startActivity(intent);
			// LoginActivity.this.finish();
			// overridePendingTransition(R.anim.push_left_in,
			// R.anim.push_left_out);
		} else if (mNowAction.equals(ORIGIN_FROM_REG_KEY)) {

		} else if (mNowAction.equals(ORIGIN_FROM_CART_KEY)) {
			LoginActivity.this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
		} else if (mNowAction.equals(ORIGIN_FROM_ORDER_KEY)) {

		} else if (mNowAction.equals(ORIGIN_FROM_USER_KEY)) {
			LoginActivity.this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
		}
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
		mAccount = mAccountEt.getText().toString().trim();
		mPassWord = mPassWordEt.getText().toString().trim();

		if (!TextUtils.isEmpty(mAccount) && !TextUtils.isEmpty(mPassWord)) {
			mLoginBtn.setClickable(true);
			mLoginBtn.setBackground(mContext.getResources().getDrawable(
					R.drawable.corners_bg_red_all));
		} else {
			mLoginBtn.setClickable(false);
			mLoginBtn.setBackground(mContext.getResources().getDrawable(
					R.drawable.corners_bg_gray_all));
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn: {
			login();
			break;
		}
		case R.id.login_wechat_login_ll: {
			login();
			break;
		}
		case R.id.login_weibo_login_ll: {
			login();
			break;
		}
		case R.id.login_qq_login_ll: {
			login();
			break;
		}

		case R.id.login_reg_ll: {
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.login_forget_pwd_ll: {
			Intent intent = new Intent(LoginActivity.this,
					ForgetPwdActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
			LoginActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
