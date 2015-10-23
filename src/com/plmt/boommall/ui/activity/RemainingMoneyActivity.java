package com.plmt.boommall.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.plmt.boommall.R;
import com.plmt.boommall.network.logic.PropertyLogic;
import com.plmt.boommall.ui.view.CustomProgressDialog;

public class RemainingMoneyActivity extends Activity implements OnClickListener {

	private Context mContext;

	private ImageView mBackIv;

	private TextView mBalanceTv;

	private CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case PropertyLogic.BALANCE_GET_SUC: {
				if (null != msg.obj) {
					mBalanceTv.setText(((String) msg.obj)+"元");
				}
				break;
			}
			case PropertyLogic.BALANCE_GET_FAIL: {
				break;
			}
			case PropertyLogic.BALANCE_GET_EXCEPTION: {
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
		setContentView(R.layout.remaining_money);
		mContext = RemainingMoneyActivity.this;
		initView();
		initData();

	}

	private void initView() {
		mBalanceTv = (TextView) findViewById(R.id.remaining_money_tv);

		mBackIv = (ImageView) findViewById(R.id.remaining_money_back_iv);
		mBackIv.setOnClickListener(this);
	}

	private void initData() {
		mProgressDialog = new CustomProgressDialog(mContext);
		mProgressDialog.show();
		PropertyLogic.queryBalance(mContext, mHandler);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.remaining_money_back_iv: {
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
