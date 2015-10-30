package com.plmt.boommall.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.plmt.boommall.R;

public class AccountActivity extends Activity implements OnClickListener {

	private Context mContext;

	private RelativeLayout mAreaRl;
	private RelativeLayout mRealNameAuthRl;
	private RelativeLayout mCollectionRl;

	private ImageView mBackIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		mContext = AccountActivity.this;
		initView();
		initData();

	}

	private void initView() {
		mAreaRl = (RelativeLayout) findViewById(R.id.account_address_rl);
		mAreaRl.setOnClickListener(this);

		mRealNameAuthRl = (RelativeLayout) findViewById(R.id.account_real_name_auth_rl);
		mRealNameAuthRl.setOnClickListener(this);

		mCollectionRl = (RelativeLayout) findViewById(R.id.account_collection_rl);
		mCollectionRl.setOnClickListener(this);

		mBackIv = (ImageView) findViewById(R.id.account_back_iv);
		mBackIv.setOnClickListener(this);
	}

	private void initData() {

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
		case R.id.account_back_iv: {
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
