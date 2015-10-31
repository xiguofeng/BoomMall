package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import android.annotation.SuppressLint;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Address;
import com.plmt.boommall.entity.PayMoney;
import com.plmt.boommall.entity.PreOrder;
import com.plmt.boommall.network.logic.AddressLogic;
import com.plmt.boommall.network.logic.OrderLogic;
import com.plmt.boommall.network.logic.PropertyLogic;
import com.plmt.boommall.ui.adapter.ShoppingCartAdapter.ischeck;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.MultiStateView;

public class CreateOrderActivity extends Activity implements OnClickListener {
	private Context mContext;

	private MultiStateView mMultiStateView;
	private ImageView mBackIv;

	private LinearLayout mAddressLl;
	private TextView mAddressUsernameTv;
	private TextView mAddressPhoneTv;
	private TextView mAddressDetailTv;
	private TextView mRealPayAmountTv;

	private TextView mDeliveryWayTv;
	private TextView mBmCardRemainingTv;

	private LinearLayout mInvoiceNotLl;
	private LinearLayout mInvoicePersonLl;
	private LinearLayout mInvoiceCompanyLl;

	private ImageView mInvoiceNotIv;
	private ImageView mInvoicePersonIv;
	private ImageView mInvoiceCompanyIv;

	private EditText mInvoiceTitleEt;
	private EditText mRemarkEt;

	private TextView mGoodsMoneyTv;
	private TextView mPostTaxTv;
	private TextView mFreightMoneyTv;
	private TextView mRemainingMoneyTv;
	private TextView mBmCardMoneyTv;

	private CheckBox mCheckBmCardCb;
	private CheckBox mCheckRemainingCb;

	private Button mConfirmBtn;

	private Address mAddress;
	private PreOrder mPreOrder;

	private String mOrderId;
	private boolean isCreateOrder = false;

	private CustomProgressDialog mProgressDialog;

	private Handler mOrderPreHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case OrderLogic.ORDER_PRE_INFO_GET_SUC: {
				if (null != msg.obj) {
					mPreOrder = (PreOrder) msg.obj;
					fillUpData(mPreOrder);
					if (isCreateOrder) {
						OrderLogic.createOrder(mContext, mOrderInfoHandler);
					}
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
					mProgressDialog.show();
					Intent intent = new Intent(CreateOrderActivity.this, PayActivity.class);
					intent.putExtra("orderId", mOrderId);
					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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

	private Handler mGiftCardHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PropertyLogic.BALANCE_PAY_SUC: {
				mProgressDialog.show();
				OrderLogic.getOrderPreInfo(mContext, mOrderPreHandler);
				break;
			}
			case PropertyLogic.BALANCE_PAY_FAIL: {

				break;
			}
			case PropertyLogic.BALANCE_PAY_EXCEPTION: {

				break;
			}
			case PropertyLogic.GIFTCARD_PAY_SUC: {
				mProgressDialog.show();
				OrderLogic.getOrderPreInfo(mContext, mOrderPreHandler);
				break;
			}
			case PropertyLogic.GIFTCARD_PAY_FAIL: {

				break;
			}
			case PropertyLogic.GIFTCARD_PAY_EXCEPTION: {

				break;
			}
			case AddressLogic.NET_ERROR: {

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
		setContentView(R.layout.create_order);
		mContext = CreateOrderActivity.this;
		mProgressDialog = new CustomProgressDialog(mContext);
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
		mAddressUsernameTv = (TextView) findViewById(R.id.create_order_address_username_tv);
		mAddressPhoneTv = (TextView) findViewById(R.id.create_order_address_phone_tv);
		mAddressDetailTv = (TextView) findViewById(R.id.create_order_address_detail_tv);

		mDeliveryWayTv = (TextView) findViewById(R.id.create_order_delivery_way_tv);
		mBmCardRemainingTv = (TextView) findViewById(R.id.create_order_bmcard_remaining_tv);

		mRealPayAmountTv = (TextView) findViewById(R.id.create_order_pay_amount_tv);

		mRemarkEt = (EditText) findViewById(R.id.create_order_remark_et);

		mGoodsMoneyTv = (TextView) findViewById(R.id.create_order_goods_money_tv);
		mPostTaxTv = (TextView) findViewById(R.id.create_order_post_tax_tv);
		mFreightMoneyTv = (TextView) findViewById(R.id.create_order_freight_money_tv);
		mRemainingMoneyTv = (TextView) findViewById(R.id.create_order_bmcard_remainin_money_tv);
		mBmCardMoneyTv= (TextView) findViewById(R.id.create_order_bmcard_money_tv);

		mCheckRemainingCb = (CheckBox) findViewById(R.id.create_order_bmcard_remaining_cb);
		mCheckRemainingCb.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mProgressDialog.show();
				if (isChecked) {
					PropertyLogic.balancePay(mContext, mGiftCardHandler, "1");
				} else {
					PropertyLogic.balancePay(mContext, mGiftCardHandler, "0");
				}
			}

		});

		mCheckBmCardCb = (CheckBox) findViewById(R.id.create_order_bmcard_cb);
		mCheckBmCardCb.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String giftCardPwd = mInvoiceTitleEt.getText().toString().trim();
				if (!TextUtils.isEmpty(giftCardPwd)) {
					mProgressDialog.show();
					if (isChecked) {
						PropertyLogic.giftCardPay(mContext, mGiftCardHandler, giftCardPwd, "0");
					} else {
						PropertyLogic.giftCardPay(mContext, mGiftCardHandler, giftCardPwd, "1");
					}
				}
			}

		});

		initInvoiceView();
	}

	private void initInvoiceView() {
		mInvoiceNotLl = (LinearLayout) findViewById(R.id.create_order_invoice_not_ll);
		mInvoiceNotLl.setOnClickListener(this);
		mInvoicePersonLl = (LinearLayout) findViewById(R.id.create_order_invoice_person_ll);
		mInvoicePersonLl.setOnClickListener(this);
		mInvoiceCompanyLl = (LinearLayout) findViewById(R.id.create_order_invoice_company_ll);
		mInvoiceCompanyLl.setOnClickListener(this);

		mInvoiceNotIv = (ImageView) findViewById(R.id.create_order_invoice_not_iv);
		mInvoicePersonIv = (ImageView) findViewById(R.id.create_order_invoice_person_iv);
		mInvoiceCompanyIv = (ImageView) findViewById(R.id.create_order_invoice_company_iv);

		mInvoiceTitleEt = (EditText) findViewById(R.id.create_order_bmcard_pwd_et);
		mInvoiceNotIv.setImageResource(R.drawable.radio_selected);
	}

	private void clearInvoiceBackground() {
		mInvoiceNotIv.setImageResource(R.drawable.radio_normal);
		mInvoicePersonIv.setImageResource(R.drawable.radio_normal);
		mInvoiceCompanyIv.setImageResource(R.drawable.radio_normal);
	}

	private void initData() {
		OrderLogic.getOrderPreInfo(mContext, mOrderPreHandler);
	}

	private void fillUpData(PreOrder preOrder) {
		mAddress = mPreOrder.getAddress();
		fillUpAddressData(mAddress);
		fillUpPayData();
		fillUpDeliveryData();
	}

	private void fillUpAddressData(Address address) {
		mAddressUsernameTv.setText(address.getUsername());
		mAddressPhoneTv.setText(address.getTelephone());
		mAddressDetailTv.setText(address.getContent());
	}

	private void fillUpDeliveryData() {
		mDeliveryWayTv.setText("标准快递");
	}

	private void fillUpPayData() {
		ArrayList<PayMoney> arrayList = mPreOrder.getPayMoneyList();
		PayMoney freightPayMoney = null;
		PayMoney balancePayMoney = null;
		PayMoney postPayMoney = null;
		PayMoney bmCardPayMoney = null;
		for (int i = 0; i < arrayList.size(); i++) {
			PayMoney tempPayMoney = arrayList.get(i);
			if ("运费".equals(tempPayMoney.getTitle())) {
				freightPayMoney = tempPayMoney;
			}
			if ("余额".equals(tempPayMoney.getTitle())) {
				balancePayMoney = tempPayMoney;
			}
			if ("行邮税".equals(tempPayMoney.getTitle())) {
				postPayMoney = tempPayMoney;
			}
			if ("旺卡".equals(tempPayMoney.getTitle())) {
				bmCardPayMoney = tempPayMoney;
			}
		}

		mFreightMoneyTv.setText("¥0");
		mRemainingMoneyTv.setText("¥0");
		mPostTaxTv.setText("¥0");
		mBmCardMoneyTv.setText("¥0");

		if (null != postPayMoney) {
			mPostTaxTv.setText("¥" + postPayMoney.getValue());
		}
		if (null != balancePayMoney) {
			mRemainingMoneyTv.setText("¥" + balancePayMoney.getValue());
		}
		if (null != freightPayMoney) {
			mFreightMoneyTv.setText("¥" + freightPayMoney.getValue());
		}
		if (null != freightPayMoney) {
			mBmCardMoneyTv.setText("¥" + bmCardPayMoney.getValue());
		}


		mBmCardRemainingTv.setText("¥0");
		if (!TextUtils.isEmpty(mPreOrder.getBalance())) {
			mBmCardRemainingTv.setText(mPreOrder.getBalance());
		}
		mRealPayAmountTv.setText("¥" + mPreOrder.getTotal());

		mGoodsMoneyTv.setText("¥" + mPreOrder.getBase_total());
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
			intent.setAction(AddressListActivity.ORIGIN_FROM_ORDER_KEY);
			startActivityForResult(intent, 500);
			break;
		}

		case R.id.create_order_confirm_btn: {
			mProgressDialog.show();
			isCreateOrder = true;
			OrderLogic.getOrderPreInfo(mContext, mOrderPreHandler);
			break;
		}

		case R.id.create_order_invoice_not_ll: {
			clearInvoiceBackground();
			mInvoiceNotIv.setImageResource(R.drawable.radio_selected);
			break;
		}
		case R.id.create_order_invoice_person_ll: {
			clearInvoiceBackground();
			mInvoicePersonIv.setImageResource(R.drawable.radio_selected);
			break;
		}
		case R.id.create_order_invoice_company_ll: {
			clearInvoiceBackground();
			mInvoiceCompanyIv.setImageResource(R.drawable.radio_selected);
			break;
		}

		default:
			break;
		}

	}

}
