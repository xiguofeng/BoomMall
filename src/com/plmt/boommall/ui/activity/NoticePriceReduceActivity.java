package com.plmt.boommall.ui.activity;

import com.plmt.boommall.R;
import com.plmt.boommall.network.logic.NoticeLogic;
import com.plmt.boommall.network.logic.UserLogic;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.iosdialog.AlertDialog;
import com.plmt.boommall.utils.UserInfoManager;
import com.plmt.boommall.utils.VerifyUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NoticePriceReduceActivity extends Activity implements OnClickListener, TextWatcher {

	private Context mContext;

	private LinearLayout mPhoneLl;

	private TextView mVersionTv;

	private EditText mPhoneNoEt;
	private EditText mPriceEt;

	private CheckBox mUseSmsCb;

	private ImageView mBackIv;

	private Button mConfrimBtn;

	private String mFinalPrice;
	private String mSuk;

	private CustomProgressDialog mProgressDialog;

	private Handler mNoticeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NoticeLogic.PRICE_REDUCE_SET_SUC: {
				Toast.makeText(mContext, "添加通知成功！", Toast.LENGTH_SHORT).show();
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				break;
			}
			case NoticeLogic.PRICE_REDUCE_SET_FAIL: {
				if (null != msg.obj) {
					Toast.makeText(mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext, "添加通知失败！", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case NoticeLogic.PRICE_REDUCE_SET_EXCEPTION: {

				break;
			}
			case NoticeLogic.NET_ERROR: {

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
		setContentView(R.layout.price_reduce);
		mContext = NoticePriceReduceActivity.this;
		mProgressDialog = new CustomProgressDialog(mContext);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		mPhoneLl = (LinearLayout) findViewById(R.id.price_reduce_phone_ll);

		mPriceEt = (EditText) findViewById(R.id.price_reduce_price_expect_et);
		mPhoneNoEt = (EditText) findViewById(R.id.price_reduce_phone_et);

		mPriceEt.addTextChangedListener(this);
		mPhoneNoEt.addTextChangedListener(this);

		mUseSmsCb = (CheckBox) findViewById(R.id.price_reduce_sms_cb);
		mUseSmsCb.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mPhoneLl.setVisibility(View.VISIBLE);
				}
			}

		});
		mBackIv = (ImageView) findViewById(R.id.price_reduce_back_iv);
		mBackIv.setOnClickListener(this);

		mConfrimBtn = (Button) findViewById(R.id.price_reduce_submit_btn);
		mConfrimBtn.setOnClickListener(this);
	}

	private void initData() {
		mSuk = getIntent().getStringExtra("sku");
		mFinalPrice = getIntent().getStringExtra("price");
		if (TextUtils.isEmpty(UserInfoManager.userInfo.getAccount())) {
			UserInfoManager.setUserInfo(mContext);
		}
		mPhoneNoEt.setText(UserInfoManager.userInfo.getAccount());
	}

	private void checkInput() {
		String Mobile = mPhoneNoEt.getText().toString().trim();

		boolean isRight = false;
		if (!TextUtils.isEmpty(Mobile)) {
			if (VerifyUtils.isMobile(Mobile)) {
				isRight = true;
			} else {
				isRight = false;
				CharSequence html = Html.fromHtml("<font color='red'>格式不正确</font>");
				mPhoneNoEt.setError(html);
			}
		}

		if (!TextUtils.isEmpty(mPriceEt.getText().toString().trim()) && isRight) {
			mConfrimBtn.setClickable(true);
			mConfrimBtn.setBackgroundResource(R.drawable.corners_bg_red_all);
		} else {
			mConfrimBtn.setClickable(false);
			mConfrimBtn.setBackgroundResource(R.drawable.corners_bg_gray_all);
		}
	}

	private void setPriceReduceNotice() {
		mProgressDialog.show();
		NoticeLogic.setPriceReduce(mContext, mNoticeHandler, mSuk, mPriceEt.getText().toString().trim(),
				mPhoneNoEt.getText().toString().trim());
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		checkInput();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.price_reduce_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		}
		case R.id.price_reduce_submit_btn: {
			setPriceReduceNotice();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
		return super.onKeyDown(keyCode, event);
	}
}
