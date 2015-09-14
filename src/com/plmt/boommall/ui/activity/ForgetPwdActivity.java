package com.plmt.boommall.ui.activity;

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
import com.plmt.boommall.network.logic.UserLogic;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.utils.UserInfoManager;

public class ForgetPwdActivity extends BaseActivity implements OnClickListener {
	public static final int TIME_UPDATE = 1;

	private LinearLayout mQueryLl;
	private LinearLayout mAuthCodeLl;
	private LinearLayout mReplaceLl;

	private RelativeLayout mAuthRl;

	private EditText mPhoneEt;
	private EditText mVerCodeEt;
	private EditText mAddressEt;
	private TextView mTimingTv;
	private TextView mPhoneTv;

	private Context mContext;

	private ImageView mBackIv;

	private String mPhone;
	private String mAuthCode;
	private String mAddress;

	private String mId;

	private MultiStateView mMultiStateView;

	private int mTiming = 60;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			}

		}

	};

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
					mReplaceLl.setVisibility(View.GONE);
				}
				mTimeHandler.sendEmptyMessage(TIME_UPDATE);
				break;
			}
			case UserLogic.SEND_AUTHCODE_FAIL: {
				// Toast.makeText(mContext, R.string.auth_get_fail,
				// Toast.LENGTH_SHORT).show();
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
		mAddressEt = (EditText) findViewById(R.id.forget_pwd_input_address_et);

		mTimingTv = (TextView) findViewById(R.id.forget_pwd_input_ver_code_btn_tv);

		mAuthRl = (RelativeLayout) findViewById(R.id.forget_pwd_input_ver_code_rl);

		// mBackIv = (ImageView) findViewById(R.id.forget_pwd_input_back_iv);

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
		mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
	}

	private void setUpListener() {
		mQueryLl.setOnClickListener(this);
		mAuthCodeLl.setOnClickListener(this);
		mReplaceLl.setOnClickListener(this);

		mBackIv.setOnClickListener(this);
	}

	private void initData() {
		mId = getIntent().getStringExtra("id");

		if (!UserInfoManager.getIsMustAuth(mContext)
				&& !TextUtils.isEmpty(UserInfoManager.getPhone(mContext))) {
			mPhone = UserInfoManager.getPhone(mContext);
			mPhoneTv.setVisibility(View.VISIBLE);
			mPhoneTv.setText(mPhone);
			mPhoneEt.setVisibility(View.GONE);
			mReplaceLl.setVisibility(View.VISIBLE);
		} else {
			mAuthRl.setVisibility(View.VISIBLE);
			mPhoneTv.setVisibility(View.GONE);
			mPhoneEt.setVisibility(View.VISIBLE);
			mReplaceLl.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.forget_pwd_input_submit_ll: {
			if (TextUtils.isEmpty(mPhone)) {
				mPhone = mPhoneEt.getText().toString().trim();
			}
			if (TextUtils.isEmpty(mPhone)) {
				// Toast.makeText(mContext,
				// getString(R.string.mobile_phone_and_code_hint),
				// Toast.LENGTH_SHORT).show();
			}
			mAuthCode = mVerCodeEt.getText().toString().trim();
			mAddress = mAddressEt.getText().toString().trim();
			if (TextUtils.isEmpty(mAddress)) {
				// Toast.makeText(mContext,
				// getString(R.string.detail_info_hint),
				// Toast.LENGTH_SHORT).show();
				return;
			}

			if (!TextUtils.isEmpty(mPhone) && !TextUtils.isEmpty(mAuthCode)
					&& !TextUtils.isEmpty(mAddress)
					&& mAuthCode.equals(mVerCodeEt.getText().toString().trim())) {

			} else {
				// Toast.makeText(mContext,
				// getString(R.string.mobile_phone_and_code_hint),
				// Toast.LENGTH_SHORT).show();
			}
			// User user = new User();
			// UserLogic.login(mContext, mLoginHandler, user);

			break;
		}
		case R.id.forget_pwd_input_ver_code_ll: {
			if (TextUtils.isEmpty(mPhone)) {
				mPhone = mPhoneEt.getText().toString().trim();
			}
			if (!TextUtils.isEmpty(mPhone)) {
				// UserLogic.sendAuthCode(mContext, mAuthHandler, mPhone);
			} else {
				Toast.makeText(mContext, getString(R.string.mobile_phone_hint),
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		default:
			break;
		}
	}

}
