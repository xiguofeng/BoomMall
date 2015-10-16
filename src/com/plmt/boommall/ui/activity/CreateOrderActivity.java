package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.Collection;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Address;
import com.plmt.boommall.entity.PreOrder;
import com.plmt.boommall.network.logic.AddressLogic;
import com.plmt.boommall.network.logic.OrderLogic;
import com.plmt.boommall.pay.AlipayMerchant;
import com.plmt.boommall.pay.alipay.AlipayApi;
import com.plmt.boommall.pay.alipay.PayResult;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.MultiStateView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
	private TextView mAddressUsernameTv;
	private TextView mAddressPhoneTv;
	private TextView mAddressDetailTv;
	private TextView mRealPayAmountTv;

	private Button mConfirmBtn;

	private Address mAddress;

	private PreOrder mPreOrder;

	private String mOrderId;

	private CustomProgressDialog mProgressDialog;

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
					mProgressDialog = new CustomProgressDialog(mContext);
					mProgressDialog.show();
					OrderLogic.getOrderPayInfo(mContext, mOrderPayInfoHandler,
							mOrderId);
				}
				break;
			}
			case OrderLogic.ORDER_CREATE_FAIL: {

				break;
			}
			case OrderLogic.ORDER_CREATE_EXCEPTION: {

				break;
			}
			case OrderLogic.NET_ERROR: {

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

	private Handler mOrderPayInfoHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case OrderLogic.ORDER_PAY_INFO_GET_SUC: {
				if (null != msg.obj) {
					AlipayMerchant alipayMerchant;
					alipayMerchant = (AlipayMerchant) msg.obj;
					PayByAli(alipayMerchant);
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
			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}

	};

	private Handler mAlipayHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case com.plmt.boommall.pay.alipay.Constants.SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					// TODO
					// payResultCheck支付成功比对支付结果
					// mCustomProgressDialog.show();
					// OrderLogic.payResultCheck(mContext, mHandler,
					// OrderManager.getsCurrentOrderId(), "true");
				} else {

					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT)
								.show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT)
								.show();

					}
				}

				break;
			}
			case com.plmt.boommall.pay.alipay.Constants.SDK_CHECK_FLAG: {
				// Toast.makeText(mContext, "检查结果为：" + msg.obj,
				// Toast.LENGTH_SHORT)
				// .show();
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
		OrderLogic.getOrderPreInfo(mContext, mOrderPreHandler);
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

	private void PayByAli(AlipayMerchant alipayMerchant) {
		AlipayApi apAlipayApi = new AlipayApi();
		alipayMerchant.setAmount("0.01");
		alipayMerchant.setOrderId(mOrderId);
		if (TextUtils.isEmpty(mOrderId)) {
			alipayMerchant.setOrderId("id900033888499933sh");
		}
		apAlipayApi.pay(CreateOrderActivity.this, mAlipayHandler,
				alipayMerchant);
	}

	private void PayByAliInLoc(AlipayMerchant alipayMerchant) {
		AlipayApi apAlipayApi = new AlipayApi();
		alipayMerchant.setAmount("0.01");
		alipayMerchant.setProductName("201");
		alipayMerchant.setProductDescription("旺铺商城201");
		alipayMerchant.setOrderId("id900033888499933sh");
		alipayMerchant
				.setPartnerId(com.plmt.boommall.pay.alipay.Constants.PARTNER);
		alipayMerchant
				.setSellerAccount(com.plmt.boommall.pay.alipay.Constants.SELLER);
		apAlipayApi.pay(CreateOrderActivity.this, mAlipayHandler,
				alipayMerchant);
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
			intent.setAction(AddressListActivity.ORIGIN_FROM_ORDER_KEY);
			startActivityForResult(intent, 500);
			break;
		}

		case R.id.create_order_confirm_btn: {
			mProgressDialog = new CustomProgressDialog(mContext);
			mProgressDialog.show();
			OrderLogic.createOrder(mContext, mOrderInfoHandler);
			// AlipayMerchant alipayMerchant=new AlipayMerchant();
			// PayByAli(alipayMerchant);
			break;
		}

		default:
			break;
		}

	}

}
