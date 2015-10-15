package com.plmt.boommall.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plmt.boommall.R;

public class AddressAddActivity extends Activity implements OnClickListener {

	private Context mContext;

	private EditText mConsigneeEt;
	private EditText mAddressDetailEt;
	private EditText mContactWayEt;

	private TextView mAreaTv;
	private RelativeLayout mAreaRl;
	private Button mSaveBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_add);
		mContext = AddressAddActivity.this;
		initView();
		initData();

	}

	private void initView() {
		mConsigneeEt = (EditText) findViewById(R.id.address_add_input_consignee_et);
		mAddressDetailEt = (EditText) findViewById(R.id.address_add_input_address_detail_et);
		mContactWayEt = (EditText) findViewById(R.id.address_add_input_contact_way_et);

		mAreaTv = (EditText) findViewById(R.id.address_add_input_area_content_tv);
		mAreaRl = (RelativeLayout) findViewById(R.id.address_add_input_area_rl);
		mSaveBtn = (Button) findViewById(R.id.address_add_confirm_btn);
		mAreaRl.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);
	}

	private void initData() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 500: {
				mAreaTv.setText(data.getStringExtra("area"));
				break;
			}
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.address_add_input_area_rl: {
			Intent intent = new Intent(AddressAddActivity.this,
					AddressAddSelectActivity.class);
			startActivityForResult(intent, 500);

			break;
		}
		case R.id.address_add_confirm_btn: {

			break;
		}
		default:
			break;
		}

	}

}
