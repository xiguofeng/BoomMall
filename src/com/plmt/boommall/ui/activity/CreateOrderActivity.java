package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Address;
import com.plmt.boommall.entity.Order;
import com.plmt.boommall.network.config.RequestUrl.address;
import com.plmt.boommall.network.logic.AddressLogic;
import com.plmt.boommall.network.logic.OrderLogic;
import com.plmt.boommall.ui.view.MultiStateView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateOrderActivity extends Activity implements OnClickListener {
	private Context mContext;

	private MultiStateView mMultiStateView;
	private ImageView mBackIv;

	private LinearLayout mAddressLl;
	private TextView mAddressUsername;
	private TextView mAddressPhone;
	private TextView mAddressDetail;

	private Button mConfirmBtn;

	private Address mAddress;

	private Handler mAddressHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AddressLogic.ANDRESS_LIST_GET_SUC: {
				if (null != msg.obj) {
					ArrayList<Address> addresslist = new ArrayList<>();
					addresslist.addAll((Collection<? extends Address>) msg.obj);
					mAddress = addresslist.get(0);
					fillUpAddressData(mAddress);
				}
				break;
			}
			case AddressLogic.ANDRESS_LIST_GET_FAIL: {

				break;
			}
			case AddressLogic.ANDRESS_LIST_GET_EXCEPTION: {

				break;
			}
			case AddressLogic.NET_ERROR: {

				break;
			}
			default:
				break;
			}
			mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_order);
		mContext = CreateOrderActivity.this;
		initView();
		initData();
	}

	private void initView() {
		mMultiStateView = (MultiStateView) findViewById(R.id.create_order_multiStateView);
		mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.retry)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
						Toast.makeText(getApplicationContext(), "Fetching Data", Toast.LENGTH_SHORT).show();
					}
				});
		mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

		mBackIv = (ImageView) findViewById(R.id.create_order_back_iv);
		mBackIv.setOnClickListener(this);

		mConfirmBtn = (Button) findViewById(R.id.create_order_confirm_btn);
		mConfirmBtn.setOnClickListener(this);

		mAddressLl = (LinearLayout) findViewById(R.id.create_order_address_ll);
		mAddressLl.setOnClickListener(this);
		mAddressUsername = (TextView) findViewById(R.id.create_order_address_username_tv);
		mAddressPhone = (TextView) findViewById(R.id.create_order_address_phone_tv);
		mAddressDetail = (TextView) findViewById(R.id.create_order_address_detail_tv);
	}

	private void initData() {
		fetchAddressData();
	}

	private void fetchAddressData() {
		AddressLogic.getList(mContext, mAddressHandler);
	}

	private void fillUpAddressData(Address address) {
		mAddressUsername.setText(address.getUsername());
		mAddressPhone.setText(address.getTelephone());
		mAddressDetail.setText(address.getContent());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 500: {
				Address address = new Address();
				address.setUsername(data.getStringExtra("username"));
				address.setTelephone(data.getStringExtra("telephone"));
				address.setContent(data.getStringExtra("content"));
				fillUpAddressData(address);
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
		case R.id.create_order_back_iv: {
			finish();
			break;
		}
		case R.id.create_order_address_ll: {
			Intent intent = new Intent(CreateOrderActivity.this, AddressListActivity.class);
			startActivityForResult(intent, 500);
			break;
		}

		case R.id.create_order_confirm_btn: {
		    OrderLogic.createOrder(mContext, mAddressHandler);
			break;
		}

		default:
			break;
		}

	}

}
