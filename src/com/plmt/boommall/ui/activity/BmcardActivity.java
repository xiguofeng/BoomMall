package com.plmt.boommall.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.network.logic.PropertyLogic;
import com.plmt.boommall.ui.view.CustomProgressDialog;

public class BmcardActivity extends Activity implements OnClickListener {

	private Context mContext;

	private ImageView mBackIv;

	private EditText mGiftCardPwdEt;

	private Button mRechargeBtn;

	private Button mQueryBtn;

	private RelativeLayout mCardMoneyRl;

	private TextView mCardMoneyTv;

	private CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case PropertyLogic.GIFTCARD_GET_SUC: {
				if (null != msg.obj) {
					mCardMoneyRl.setVisibility(View.VISIBLE);
					mCardMoneyTv.setText((String) msg.obj);
				}
				break;
			}
			case PropertyLogic.GIFTCARD_GET_FAIL: {
				break;
			}
			case PropertyLogic.GIFTCARD_GET_EXCEPTION: {
				break;
			}
			
			case PropertyLogic.RECHARGE_SET_SUC: {
				Toast.makeText(mContext, "充值成功！", Toast.LENGTH_SHORT).show();
				break;
			}
			case PropertyLogic.RECHARGE_SET_FAIL: {
				if(null != msg.obj){
					Toast.makeText(mContext, "充值失败："+(String)msg.obj, Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case PropertyLogic.RECHARGE_SET_EXCEPTION: {
				break;
			}

			case PropertyLogic.NET_ERROR: {
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
		setContentView(R.layout.bmcard);
		mContext = BmcardActivity.this;
		initView();
		initData();

	}

	private void initView() {
		mGiftCardPwdEt = (EditText) findViewById(R.id.bmcard_content_pwd_et);

		mRechargeBtn = (Button) findViewById(R.id.bmcard_recharge_btn);
		mQueryBtn = (Button) findViewById(R.id.bmcard_query_btn);
		mRechargeBtn.setOnClickListener(this);
		mQueryBtn.setOnClickListener(this);

		mCardMoneyRl = (RelativeLayout) findViewById(R.id.bmcard_money_rl);
		mCardMoneyTv = (TextView) findViewById(R.id.bmcard_money_tv);
		mCardMoneyRl.setVisibility(View.GONE);

		mBackIv = (ImageView) findViewById(R.id.bmcard_back_iv);
		mBackIv.setOnClickListener(this);
	}

	private void initData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bmcard_recharge_btn: {
			if (!TextUtils.isEmpty(mGiftCardPwdEt.getText().toString())) {
				mProgressDialog = new CustomProgressDialog(mContext);
				mProgressDialog.show();
				PropertyLogic.rechargeBalance(mContext, mHandler, mGiftCardPwdEt
						.getText().toString());
			}
			break;
		}
		case R.id.bmcard_query_btn: {
			if (!TextUtils.isEmpty(mGiftCardPwdEt.getText().toString())) {
				mProgressDialog = new CustomProgressDialog(mContext);
				mProgressDialog.show();
				PropertyLogic.queryGiftCard(mContext, mHandler, mGiftCardPwdEt
						.getText().toString());
			} else {
				Toast.makeText(mContext, "旺卡密码不能为空！", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case R.id.bmcard_back_iv: {
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
