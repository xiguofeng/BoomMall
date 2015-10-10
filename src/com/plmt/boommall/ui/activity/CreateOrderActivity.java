package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Address;
import com.plmt.boommall.entity.PreOrder;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateOrderActivity extends Activity implements OnClickListener {
	private Context mContext;

	private MultiStateView mMultiStateView;
	private ImageView mBackIv;

	private LinearLayout mAddressLl;
	private TextView mAddressUsernameTv;
	private TextView mAddressPhoneTv;
	private TextView mAddressDetailTv;
	private TextView mRealPayAmountTv;

	private Button mConfirmBtn;

	private Address mAddress;

	private PreOrder mPreOrder;

	private String mOrderId;

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

	private Handler mOrderPreHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case OrderLogic.ORDER_PRE_INFO_GET_SUC: {
				if (null != msg.obj) {
					mPreOrder = (PreOrder) msg.obj;
					fillUpData(mPreOrder);
				}
				break;
			}
			case OrderLogic.ORDER_PRE_INFO_GET_FAIL: {

				break;
			}
			case OrderLogic.ORDER_PRE_INFO_GET_EXCEPTION: {

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

	private Handler mOrderInfoHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case OrderLogic.ORDER_CREATE_SUC: {
				if (null != msg.obj) {
					mOrderId = (String) msg.obj;
				}
				break;
			}
			case OrderLogic.ORDER_CREATE_FAIL: {

				break;
			}
			case OrderLogic.ORDER_CREATE_EXCEPTION: {

				break;
			}
			case OrderLogic.ORDER_PAY_INFO_GET_SUC: {
				if (null != msg.obj) {
				}
				break;
			}
			case OrderLogic.ORDER_PAY_INFO_GET_FAIL: {

				break;
			}
			case OrderLogic.ORDER_PAY_INFO_GET_EXCEPTION: {

				break;
			}
			case OrderLogic.NET_ERROR: {

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

		mBackIv = (ImageView) findViewById(R.id.create_order_back_iv);
		mBackIv.setOnClickListener(this);

		mConfirmBtn = (Button) findViewById(R.id.create_order_confirm_btn);
		mConfirmBtn.setOnClickListener(this);

		mAddressLl = (LinearLayout) findViewById(R.id.create_order_address_ll);
		mAddressLl.setOnClickListener(this);
		mAddressUsernameTv = (TextView) findViewById(R.id.create_order_address_username_tv);
		mAddressPhoneTv = (TextView) findViewById(R.id.create_order_address_phone_tv);
		mAddressDetailTv = (TextView) findViewById(R.id.create_order_address_detail_tv);

		mRealPayAmountTv = (TextView) findViewById(R.id.create_order_pay_amount_tv);
	}

	private void initData() {
		// fetchAddressData();
		OrderLogic.createOrder(mContext, mOrderInfoHandler);
		OrderLogic.getOrderPreInfo(mContext, mOrderPreHandler);
	}

	private void fetchAddressData() {
		AddressLogic.getList(mContext, mAddressHandler);
	}

	private void fillUpAddressData(Address address) {
		mAddressUsernameTv.setText(address.getUsername());
		mAddressPhoneTv.setText(address.getTelephone());
		mAddressDetailTv.setText(address.getContent());
	}

	private void fillUpData(PreOrder preOrder) {
		mAddress = mPreOrder.getAddress();
		fillUpAddressData(mAddress);

		mRealPayAmountTv.setText(preOrder.getTotal());
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
			Intent intent = new Intent(CreateOrderActivity.this,
					AddressListActivity.class);
			startActivityForResult(intent, 500);
			break;
		}

		case R.id.create_order_confirm_btn: {
			OrderLogic.getOrderPayInfo(mContext, mOrderInfoHandler, mOrderId);
			break;
		}

		default:
			break;
		}

	}

}
