package com.plmt.boommall.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plmt.boommall.R;

public class AccountActivity extends Activity implements OnClickListener {

	private Context mContext;

	private RelativeLayout mAreaRl;
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
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		case R.id.account_back_iv: {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		}
		default:
			break;
		}

	}

}
