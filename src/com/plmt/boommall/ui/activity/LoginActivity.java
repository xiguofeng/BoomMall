package com.plmt.boommall.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.User;
import com.plmt.boommall.network.logic.OrderLogic;
import com.plmt.boommall.network.logic.UserLogic;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.utils.UserInfoManager;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	public static final String ORIGIN_FROM_REG_KEY = "com.reg";

	public static final String ORIGIN_FROM_ORDER_KEY = "com.order";

	private EditText mAccountEt;
	private EditText mPassWordEt;
	private CheckBox mRemberpswCb;
	// private LinearLayout layoutProcess;
	private Button mLoginBtn;

	private String mAccount;
	private String mPassWord;

	private User mUser = new User();

	private MultiStateView mMultiStateView;

	private Context mContext;

	// 登陆装填提示handler更新主线程，提示登陆状态情况
	Handler mLoginHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case UserLogic.LOGIN_SUC: {
				if (null != msg.obj) {
					String session = (String) msg.obj;
					UserInfoManager.setSession(mContext, session);

					UserLogic.getInfo(mContext, mHandler);
					OrderLogic.getOrders(mContext, mHandler, session, "1", "5");
					// mUser = (User) msg.obj;
					// mUser.setPassword(mPassWord);
					// UserInfoManager.setRememberPwd(mContext, true);
					// UserInfoManager.saveUserInfo(LoginActivity.this, mUser);
					// UserInfoManager.setUserInfo(LoginActivity.this);
					// UserInfoManager.setLoginIn(LoginActivity.this, true);
					//
					// Intent intent = new Intent(LoginActivity.this,
					// HomeActivity.class);
					// startActivity(intent);
					// LoginActivity.this.finish();
					// overridePendingTransition(R.anim.push_left_in,
					// R.anim.push_left_out);
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
			case UserLogic.NET_ERROR: {
				break;
			}

			default:
				break;
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
		mAccountEt = (EditText) findViewById(R.id.login_username);
		mPassWordEt = (EditText) findViewById(R.id.login_password);

		mLoginBtn = (Button) findViewById(R.id.login_btn);
		mLoginBtn.setOnClickListener(this);

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

		if (UserInfoManager.getRememberPwd(mContext)) {
			UserInfoManager.setUserInfo(mContext);

			mAccountEt.setText(UserInfoManager.userInfo.getAccount());
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

			// Intent intent = new Intent(LoginActivity.this,
			// HomeActivity.class);
			// startActivity(intent);
			// LoginActivity.this.finish();
			// overridePendingTransition(R.anim.push_left_in,
			// R.anim.push_left_out);
			// mUser.setAccount(mAccount);
			// mUser.setPassword(mPassWord);
			// UserLogic.login(mContext, mLoginHandler, mUser);
			User user = new User();
			user.setUserName("13813003736");
			user.setPassword("admin123");
			UserLogic.login(mContext, mLoginHandler, user);
			// UserLogic.getInfo(mContext, mHandler);
			// UserLogic.getInfoByHttpUrl(mContext, mHandler);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn: {
			login();
			break;
		}

		default:
			break;
		}

	}

}
