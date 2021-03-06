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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Address;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.entity.PayMoney;
import com.plmt.boommall.entity.PreOrder;
import com.plmt.boommall.entity.SubmitOrderResponse;
import com.plmt.boommall.network.logic.AddressLogic;
import com.plmt.boommall.network.logic.OrderLogic;
import com.plmt.boommall.network.logic.PropertyLogic;
import com.plmt.boommall.ui.adapter.ShoppingCartAdapter.ischeck;
import com.plmt.boommall.ui.view.CreateOrderGoodsView;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.ui.view.MultiStateView;
import com.plmt.boommall.ui.view.OrderGoodsView;
import com.plmt.boommall.utils.ActivitiyInfoManager;

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

	private RelativeLayout mRealNameRl;

	private LinearLayout mGoodsViewLl;

	private LinearLayout mInvoiceLl;
	private LinearLayout mInvoiceLineLl;
	private LinearLayout mInvoiceSelectLl;
	private LinearLayout mInvoiceNotLl;
	private LinearLayout mInvoicePersonLl;
	private LinearLayout mInvoiceCompanyLl;

	private ImageView mInvoiceNotIv;
	private ImageView mInvoicePersonIv;
	private ImageView mInvoiceCompanyIv;

	private EditText mInvoiceEt;

	private TextView mInvoiceTagTv;

	private EditText mBmCardEt;
	private EditText mRemarkEt;

	private TextView mGoodsMoneyTv;
	private TextView mPostTaxTv;
	private TextView mFreightMoneyTv;
	private TextView mRemainingMoneyTv;
	private TextView mBmCardMoneyTv;

	private CheckBox mCheckBmCardCb;
	private CheckBox mCheckRemainingCb;

	private boolean isHasBalance = false;
	private boolean isHasBmCard = false;

	private Button mConfirmBtn;

	private Address mAddress;
	private PreOrder mPreOrder;

	private String mOrderId;
	private boolean isCreateOrder = false;
	private String mInvoiceTitle = "";

	private PreOrder mSubmitpreOrder = new PreOrder();

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
						if (!TextUtils.isEmpty(mInvoiceEt.getText().toString().trim())) {
							mPreOrder.setInvoiceName(mInvoiceEt.getText().toString().trim());
						} else {
							mPreOrder.setInvoiceName("");
						}
						mPreOrder.setInvoiceTitle(mInvoiceTitle);
						mProgressDialog.show();
						OrderLogic.createOrder(mContext, mOrderInfoHandler, mPreOrder);
					} else {
						if (null != mProgressDialog && mProgressDialog.isShowing()) {
							mProgressDialog.dismiss();
						}
					}
				}
				break;
			}
			case OrderLogic.ORDER_PRE_INFO_GET_FAIL: {
				if (null != mProgressDialog && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
				break;
			}
			case OrderLogic.ORDER_PRE_INFO_GET_EXCEPTION: {
				if (null != mProgressDialog && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
				break;
			}
			case AddressLogic.NET_ERROR: {
				if (null != mProgressDialog && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
				break;
			}
			default:
				break;
			}
		}

	};

	private Handler mOrderInfoHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case OrderLogic.ORDER_CREATE_SUC: {
				if (null != msg.obj) {
					ShoppingCartActivity.isNeedUpdate = true;
					SubmitOrderResponse submitOrderResponse = (SubmitOrderResponse) msg.obj;
					mOrderId = submitOrderResponse.getOrder_id();
					if ("paid".equals(submitOrderResponse.getOrder_status())) {
						Toast.makeText(mContext, "下单成功！", Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Intent intent = new Intent(CreateOrderActivity.this, PayActivity.class);
						intent.putExtra("orderId", mOrderId);
						intent.putExtra("price", "¥" + mPreOrder.getTotal());
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					}
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
				if (null != msg.obj) {
					Toast.makeText(mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
				}
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
				if (null != msg.obj) {
					Toast.makeText(mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case PropertyLogic.GIFTCARD_PAY_EXCEPTION: {

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
		setContentView(R.layout.create_order);
		mContext = CreateOrderActivity.this;
		mProgressDialog = new CustomProgressDialog(mContext);
		if (!ActivitiyInfoManager.activitityMap.containsKey(ActivitiyInfoManager.getCurrentActivityName(mContext))) {
			ActivitiyInfoManager.activitityMap.put(ActivitiyInfoManager.getCurrentActivityName(mContext), this);
		}
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
		mBmCardMoneyTv = (TextView) findViewById(R.id.create_order_bmcard_money_tv);

		mCheckRemainingCb = (CheckBox) findViewById(R.id.create_order_bmcard_remaining_cb);
		mCheckRemainingCb.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isHasBalance) {
					mProgressDialog.show();
					if (isChecked) {
						PropertyLogic.balancePay(mContext, mGiftCardHandler, "1");
					} else {
						PropertyLogic.balancePay(mContext, mGiftCardHandler, "0");
					}
				}
				isHasBalance = false;
			}

		});

		mCheckBmCardCb = (CheckBox) findViewById(R.id.create_order_bmcard_cb);
		mCheckBmCardCb.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isHasBmCard) {
					String giftCardPwd = mBmCardEt.getText().toString().trim();
					if (!TextUtils.isEmpty(giftCardPwd)) {
						mProgressDialog.show();
						if (isChecked) {
							PropertyLogic.giftCardPay(mContext, mGiftCardHandler, giftCardPwd, "0");
						} else {
							PropertyLogic.giftCardPay(mContext, mGiftCardHandler, giftCardPwd, "1");
						}
					} else if (isChecked) {
						mCheckBmCardCb.setChecked(false);
					}
				}
				isHasBmCard = false;
			}

		});
		mGoodsViewLl = (LinearLayout) findViewById(R.id.create_order_goods_view_ll);
		initInvoiceView();

		mRealNameRl = (RelativeLayout) findViewById(R.id.create_order_real_name_auth_rl);
		mRealNameRl.setOnClickListener(this);
	}

	private void initInvoiceView() {
		mInvoiceLl = (LinearLayout) findViewById(R.id.create_order_invoice_ll);
		mInvoiceLineLl = (LinearLayout) findViewById(R.id.create_order_invoice_line_ll);
		mInvoiceSelectLl = (LinearLayout) findViewById(R.id.create_order_invoice_select_ll);

		mInvoiceNotLl = (LinearLayout) findViewById(R.id.create_order_invoice_not_ll);
		mInvoiceNotLl.setOnClickListener(this);
		mInvoicePersonLl = (LinearLayout) findViewById(R.id.create_order_invoice_person_ll);
		mInvoicePersonLl.setOnClickListener(this);
		mInvoiceCompanyLl = (LinearLayout) findViewById(R.id.create_order_invoice_company_ll);
		mInvoiceCompanyLl.setOnClickListener(this);

		mInvoiceNotIv = (ImageView) findViewById(R.id.create_order_invoice_not_iv);
		mInvoicePersonIv = (ImageView) findViewById(R.id.create_order_invoice_person_iv);
		mInvoiceCompanyIv = (ImageView) findViewById(R.id.create_order_invoice_company_iv);

		mInvoiceTagTv = (TextView) findViewById(R.id.create_order_address_invoice_tag_tv);
		mInvoiceEt = (EditText) findViewById(R.id.acreate_order_invoice_name_et);

		mBmCardEt = (EditText) findViewById(R.id.create_order_bmcard_pwd_et);
		mInvoiceNotIv.setImageResource(R.drawable.radio_selected);
	}

	private void clearInvoiceBackground() {
		mInvoiceNotIv.setImageResource(R.drawable.radio_normal);
		mInvoicePersonIv.setImageResource(R.drawable.radio_normal);
		mInvoiceCompanyIv.setImageResource(R.drawable.radio_normal);
	}

	private void initData() {
		mProgressDialog.show();
		OrderLogic.getOrderPreInfo(mContext, mOrderPreHandler);
	}

	private void fillUpData(PreOrder preOrder) {
		mAddress = mPreOrder.getAddress();
		fillUpAddressData(mAddress);
		fillUpPayData();
		fillUpDeliveryData();
		fillUpGoods();
		fillUpRealName();
		fillUpInvoice();
	}

	private void fillUpAddressData(Address address) {
		mAddressUsernameTv.setText(address.getUsername());
		mAddressPhoneTv.setText(address.getTelephone());
		mAddressDetailTv.setText(address.getContent());
	}

	private void fillUpDeliveryData() {
		mDeliveryWayTv.setText("标准快递");
	}

	private void fillUpGoods() {
		mGoodsViewLl.removeAllViews();
		ArrayList<Goods> goodsList = mPreOrder.getGoodsList();
		for (int i = 0; i < goodsList.size(); i++) {
			Goods goods = goodsList.get(i);
			CreateOrderGoodsView orderGoodsView = new CreateOrderGoodsView(mContext, goods);
			mGoodsViewLl.addView(orderGoodsView);
		}
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
			if (0.00 != Double.parseDouble(balancePayMoney.getValue())) {
				isHasBalance = true;
				mCheckRemainingCb.setChecked(true);
			}
		}
		if (null != freightPayMoney) {
			mFreightMoneyTv.setText("¥" + freightPayMoney.getValue());
		}
		if (null != bmCardPayMoney) {
			mBmCardMoneyTv.setText("¥" + bmCardPayMoney.getValue());
			if (0 != Double.parseDouble(bmCardPayMoney.getValue())) {
				isHasBmCard = true;
				mCheckBmCardCb.setChecked(true);
			}
		}

		mBmCardRemainingTv.setText("¥0");
		if (!TextUtils.isEmpty(mPreOrder.getBalance())) {
			mBmCardRemainingTv.setText(mPreOrder.getBalance());
		}
		mRealPayAmountTv.setText("¥" + mPreOrder.getTotal());

		mGoodsMoneyTv.setText("¥" + mPreOrder.getBase_total());
	}

	private void fillUpRealName() {
		mRealNameRl.setVisibility(View.GONE);
		if (!TextUtils.isEmpty(mPreOrder.getIs_out_country()) && "0".equals(mPreOrder.getIs_out_country())
				&& !TextUtils.isEmpty(mPreOrder.getIs_authentication())
				&& "-1".equals(mPreOrder.getIs_authentication())) {
			mRealNameRl.setVisibility(View.VISIBLE);
		}
	}

	private void fillUpInvoice() {
		mInvoiceTagTv.setText("发票信息(海外商品不支持提供发票服务)");
		mInvoiceTagTv.setTextColor(getResources().getColor(R.color.red_character));

		mInvoiceSelectLl.setVisibility(View.GONE);
		mInvoiceEt.setVisibility(View.GONE);

		if (!TextUtils.isEmpty(mPreOrder.getIs_out_country()) && "-1".equals(mPreOrder.getIs_out_country())) {

			mInvoiceTagTv.setText("发票信息");
			mInvoiceTagTv.setTextColor(getResources().getColor(R.color.black));

			mInvoiceSelectLl.setVisibility(View.VISIBLE);
			mInvoiceEt.setVisibility(View.VISIBLE);
		}
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
				mProgressDialog.show();
				OrderLogic.getOrderPreInfo(mContext, mOrderPreHandler);
				break;
			}

			case 501: {
				mProgressDialog.show();
				OrderLogic.getOrderPreInfo(mContext, mOrderPreHandler);
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
			if (!TextUtils.isEmpty(mPreOrder.getIs_out_country()) && "0".equals(mPreOrder.getIs_out_country())
					&& !TextUtils.isEmpty(mPreOrder.getIs_authentication())
					&& "-1".equals(mPreOrder.getIs_authentication())) {
				Toast.makeText(mContext, R.string.please_real_name_auth, Toast.LENGTH_SHORT).show();
				return;
			}

			if (!TextUtils.isEmpty(mPreOrder.getAddress().getId())
					&& !TextUtils.isEmpty(mPreOrder.getAddress().getTelephone())) {
				mProgressDialog.show();
				isCreateOrder = true;
				OrderLogic.getOrderPreInfo(mContext, mOrderPreHandler);
			} else {
				Toast.makeText(mContext, R.string.address_hint, Toast.LENGTH_SHORT).show();
			}
			break;
		}

		case R.id.create_order_invoice_not_ll: {
			clearInvoiceBackground();
			mInvoiceNotIv.setImageResource(R.drawable.radio_selected);
			mInvoiceTitle = "";
			break;
		}
		case R.id.create_order_invoice_person_ll: {
			clearInvoiceBackground();
			mInvoicePersonIv.setImageResource(R.drawable.radio_selected);
			mInvoiceTitle = "个人";
			break;
		}
		case R.id.create_order_invoice_company_ll: {
			clearInvoiceBackground();
			mInvoiceCompanyIv.setImageResource(R.drawable.radio_selected);
			mInvoiceTitle = "公司";
			break;
		}
		case R.id.create_order_real_name_auth_rl: {
			Intent intent = new Intent(CreateOrderActivity.this, RealNameAuthActivity.class);
			intent.setAction(RealNameAuthActivity.ORIGIN_FROM_ORDER_CREATE_KEY);
			startActivityForResult(intent, 501);
			break;
		}

		default:
			break;
		}

	}

}
