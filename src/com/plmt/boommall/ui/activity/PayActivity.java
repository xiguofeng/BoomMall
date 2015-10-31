package com.plmt.boommall.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plmt.boommall.R;
import com.plmt.boommall.entity.Goods;
import com.plmt.boommall.network.logic.OrderLogic;
import com.plmt.boommall.pay.AlipayMerchant;
import com.plmt.boommall.pay.PayConstants;
import com.plmt.boommall.pay.UnionpayMerchant;
import com.plmt.boommall.pay.WechatpayMerchant;
import com.plmt.boommall.pay.alipay.AlipayApi;
import com.plmt.boommall.pay.alipay.PayResult;
import com.plmt.boommall.pay.unionpay.UnionpayApi;
import com.plmt.boommall.pay.wxpay.WechatpayApi;
import com.plmt.boommall.ui.view.CustomProgressDialog;
import com.plmt.boommall.utils.ActivitiyInfoManager;
import com.plmt.boommall.utils.OrderManager;

public class PayActivity extends Activity implements OnClickListener {

	private Context mContext;

	private RelativeLayout mAlipayRl;
	private RelativeLayout mWeChatRl;
	private RelativeLayout mUnionpayRl;

	private CheckBox mAlipayCb;
	private CheckBox mWeChatCb;
	private CheckBox mUnionpayCb;

	private Button mPayConfirmBtn;

	private ImageView mBackIv;

	private TextView mMoneyTv;

	private HashMap<String, Object> mMsgMap = new HashMap<String, Object>();

	private ArrayList<Goods> mGoodsList = new ArrayList<Goods>();

	private String mCurrentPayWay;

	private String mCurrentSelectPayWay;

	private UnionpayApi mUnionpayApi;

	private WechatpayApi mWechatpayApi;

	private String mOrderId;

	private String mMoney;

	public static String sWxRespCode = "";

	private CustomProgressDialog mProgressDialog;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case OrderLogic.ORDER_PAY_TYPE_SET_SUC: {
				if (null != msg.obj) {
					mMsgMap.clear();
					mMsgMap.putAll((Map<? extends String, ? extends Object>) msg.obj);
					mCurrentPayWay = mCurrentSelectPayWay;
					hanlder();
				}
				break;
			}
			case OrderLogic.ORDER_PAY_TYPE_SET_FAIL: {
				// Toast.makeText(mContext, R.string.login_fail,
				// Toast.LENGTH_SHORT).show();
				break;
			}
			case OrderLogic.ORDER_PAY_TYPE_SET_EXCEPTION: {
				break;
			}
			case OrderLogic.ORDER_PAY_RESULT_CHECK_SUC: {
				if (null != msg.obj) {
					Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(mContext, AccountActivity.class);
					intent.putExtra("order_state", "1");
					intent.putExtra("delivery_time", OrderManager.getsCurrentOrder().getDeliveryTime());
					startActivity(intent);
					PayActivity.this.finish();
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
				break;
			}
			case OrderLogic.ORDER_PAY_RESULT_CHECK_FAIL: {
				Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(mContext, AccountActivity.class);
				intent.putExtra("order_state", "1");
				intent.putExtra("delivery_time", OrderManager.getsCurrentOrder().getDeliveryTime());
				startActivity(intent);
				PayActivity.this.finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				break;
			}
			case OrderLogic.ORDER_PAY_RESULT_CHECK_EXCEPTION: {
				Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(mContext, AccountActivity.class);
				intent.putExtra("order_state", "1");
				intent.putExtra("delivery_time", OrderManager.getsCurrentOrder().getDeliveryTime());
				startActivity(intent);
				PayActivity.this.finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
					mProgressDialog.show();

				} else {

					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();

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
			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		};
	};

	private Handler mUnionpayHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case OrderLogic.ORDER_PAY_UNION_TN_GET_SUC: {
				if (null != msg.obj) {
					if (null == mUnionpayApi) {
						mUnionpayApi = new UnionpayApi(PayActivity.this, mUnionpayHandler);
					}
					String tn = (String) msg.obj;
					mUnionpayApi.pay(PayActivity.this, mUnionpayHandler, tn);
				}
			}
			case OrderLogic.ORDER_PAY_UNION_TN_GET_FAIL: {
				// Toast.makeText(mContext, "检查结果为：" + msg.obj,
				// Toast.LENGTH_SHORT)
				// .show();
				break;
			}
			case OrderLogic.ORDER_PAY_UNION_TN_GET_EXCEPTION: {
				// Toast.makeText(mContext, "检查结果为：" + msg.obj,
				// Toast.LENGTH_SHORT)
				// .show();
				break;
			}
			default:
				break;

			}
			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		};
	};

	private Handler mWechatpayHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WechatpayApi.PREPAY_ID_GET_SUC: {
				if (null != msg.obj) {
					// Toast.makeText(mContext, "生成预支付订单成功" + NotifyMsg.obj,
					// Toast.LENGTH_SHORT).show();
					Map<String, String> result = (Map<String, String>) msg.obj;
					if (null == mWechatpayApi) {
						mWechatpayApi = new WechatpayApi();
					}

					WechatpayMerchant wxpayMerchant = (WechatpayMerchant) mMsgMap.get(PayConstants.PAY_WAY_WXPAY);
					if (null != wxpayMerchant) {
						mWechatpayApi.genPayReq(PayActivity.this, mWechatpayHandler, result, wxpayMerchant);
					} else {
						Toast.makeText(mContext, "支付失败,请重试！", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			case WechatpayApi.PREPAY_ID_GET_FAIL: {
				// Toast.makeText(mContext, "检查结果为：" + msg.obj,
				// Toast.LENGTH_SHORT)
				// .show();
				break;
			}
			case WechatpayApi.PREPAY_ID_GET_EXCEPTION: {
				// Toast.makeText(mContext, "检查结果为：" + msg.obj,
				// Toast.LENGTH_SHORT)
				// .show();
				break;
			}

			default:
				break;

			}
			if (null != mProgressDialog && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		};
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

			case OrderLogic.ORDER_PAY_UNION_TN_GET_SUC: {
				if (null != msg.obj) {
					UnionpayMerchant unionPayMerchant;
					unionPayMerchant = (UnionpayMerchant) msg.obj;
					PayByUnion(unionPayMerchant);
				}
				break;
			}
			case OrderLogic.ORDER_PAY_UNION_TN_GET_FAIL: {

				break;
			}
			case OrderLogic.ORDER_PAY_UNION_TN_GET_EXCEPTION: {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		mContext = PayActivity.this;
		if (!ActivitiyInfoManager.activitityMap.containsKey(ActivitiyInfoManager.getCurrentActivityName(mContext))) {
			ActivitiyInfoManager.activitityMap.put(ActivitiyInfoManager.getCurrentActivityName(mContext), this);
		}
		mProgressDialog = new CustomProgressDialog(mContext);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!TextUtils.isEmpty(mCurrentPayWay) && PayConstants.PAY_WAY_WXPAY.equals(mCurrentPayWay)
				&& !TextUtils.isEmpty(sWxRespCode)) {
			if ("0".equals(sWxRespCode)) {
				mProgressDialog.show();
			}
		}
	}

	private void initView() {
		mAlipayRl = (RelativeLayout) findViewById(R.id.pay_alipay_rl);
		mWeChatRl = (RelativeLayout) findViewById(R.id.pay_wechat_rl);
		mUnionpayRl = (RelativeLayout) findViewById(R.id.pay_unionpay_rl);

		mAlipayCb = (CheckBox) findViewById(R.id.pay_alipay_cb);
		mWeChatCb = (CheckBox) findViewById(R.id.pay_wechat_cb);
		mUnionpayCb = (CheckBox) findViewById(R.id.pay_unionpay_cb);

		mPayConfirmBtn = (Button) findViewById(R.id.pay_confirm_btn);
		mBackIv = (ImageView) findViewById(R.id.pay_back_iv);

		mMoneyTv = (TextView) findViewById(R.id.pay_money_tv);

		initListener();
	}

	private void initListener() {

		mAlipayRl.setOnClickListener(this);
		mWeChatRl.setOnClickListener(this);
		mUnionpayRl.setOnClickListener(this);

		mAlipayCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mWeChatCb.setChecked(false);
					mUnionpayCb.setChecked(false);
					mCurrentSelectPayWay = PayConstants.PAY_WAY_ALIPAY;
				}
			}
		});
		mWeChatCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mAlipayCb.setChecked(false);
					mUnionpayCb.setChecked(false);
					mCurrentSelectPayWay = PayConstants.PAY_WAY_WXPAY;
				}
			}
		});
		mUnionpayCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mWeChatCb.setChecked(false);
					mAlipayCb.setChecked(false);
					mCurrentSelectPayWay = PayConstants.PAY_WAY_UNIONPAY;
				}
			}
		});

		mPayConfirmBtn.setOnClickListener(this);
		mBackIv.setOnClickListener(this);

	}

	private void initData() {
		mOrderId = getIntent().getExtras().getString("orderId");
		mMoney = getIntent().getStringExtra("price");
		if (!TextUtils.isEmpty(mMoney)) {
			mMoneyTv.setText(mMoney);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}

		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			msg = "支付成功！";
			mProgressDialog.show();
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
			Toast.makeText(mContext, "支付结果为：" + msg, Toast.LENGTH_SHORT).show();
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "支付取消！";
			Toast.makeText(mContext, "支付结果为：" + msg, Toast.LENGTH_SHORT).show();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void hanlder() {
		if (PayConstants.PAY_WAY_ALIPAY.equals(mCurrentPayWay)) {
			AlipayMerchant alipayMerchant = (AlipayMerchant) mMsgMap.get(PayConstants.PAY_WAY_ALIPAY);
			AlipayApi apAlipayApi = new AlipayApi();
			apAlipayApi.pay(PayActivity.this, mAlipayHandler, alipayMerchant);

		} else if (PayConstants.PAY_WAY_WXPAY.equals(mCurrentPayWay)) {
			if (null == mWechatpayApi) {
				mWechatpayApi = new WechatpayApi();
			}
			WechatpayMerchant wxpayMerchant = (WechatpayMerchant) mMsgMap.get(PayConstants.PAY_WAY_WXPAY);
			mWechatpayApi.getPrepayId(PayActivity.this, mWechatpayHandler, wxpayMerchant,
					String.valueOf(Integer.parseInt(OrderManager.getsCurrentOrder().getAmount()) * 100));

		} else if (PayConstants.PAY_WAY_UNIONPAY.equals(mCurrentPayWay)) {
			if (null == mUnionpayApi) {
				mUnionpayApi = new UnionpayApi(PayActivity.this, mUnionpayHandler);
			}
			UnionpayMerchant unionpayMerchant = (UnionpayMerchant) mMsgMap.get(PayConstants.PAY_WAY_UNIONPAY);
			mUnionpayApi.pay(PayActivity.this, mUnionpayHandler, unionpayMerchant.getTn());

		} else if (PayConstants.PAY_WAY_CASHPAY.equals(mCurrentPayWay)) {
			Intent intent = new Intent(mContext, AccountActivity.class);
			intent.putExtra("order_state", "1");
			intent.putExtra("delivery_time", OrderManager.getsCurrentOrder().getDeliveryTime());
			startActivity(intent);
			PayActivity.this.finish();
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

		} else if (PayConstants.PAY_WAY_POSPAY.equals(mCurrentPayWay)) {

			Intent intent = new Intent(mContext, AccountActivity.class);
			intent.putExtra("order_state", "1");
			intent.putExtra("delivery_time", OrderManager.getsCurrentOrder().getDeliveryTime());
			startActivity(intent);
			PayActivity.this.finish();
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

		}
	}

	private void PayByAli(AlipayMerchant alipayMerchant) {
		AlipayApi apAlipayApi = new AlipayApi();
		alipayMerchant.setAmount("0.01");
		alipayMerchant.setOrderId(mOrderId);
		if (TextUtils.isEmpty(mOrderId)) {
			alipayMerchant.setOrderId("id900033888499933sh");
		}
		apAlipayApi.pay(PayActivity.this, mAlipayHandler, alipayMerchant);
	}

	private void PayByUnion(UnionpayMerchant unionMerchant) {
		if (null == mUnionpayApi) {
			mUnionpayApi = new UnionpayApi(PayActivity.this, mUnionpayHandler);
		}
		mUnionpayApi.pay(PayActivity.this, mUnionpayHandler, unionMerchant.getTn());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay_confirm_btn: {
			if (!TextUtils.isEmpty(mCurrentSelectPayWay) && PayConstants.PAY_WAY_ALIPAY.equals(mCurrentSelectPayWay)) {
				if (null != mProgressDialog) {
					mProgressDialog.show();
				}
				OrderLogic.getOrderPayInfo(mContext, mOrderPayInfoHandler, mOrderId);
			} else if (!TextUtils.isEmpty(mCurrentSelectPayWay)
					&& PayConstants.PAY_WAY_UNIONPAY.equals(mCurrentSelectPayWay)) {
				if (null != mProgressDialog) {
					mProgressDialog.show();
				}
				OrderLogic.getOrderPayInfoByUnion(mContext, mOrderPayInfoHandler, mOrderId);
			}
			break;
		}

		case R.id.pay_back_iv: {
			PayActivity.this.finish();
			break;
		}

		case R.id.pay_alipay_rl: {
			if (!mAlipayCb.isChecked()) {
				mAlipayCb.setChecked(true);
			}
			break;
		}

		case R.id.pay_wechat_rl: {
			if (!mWeChatCb.isChecked()) {
				mWeChatCb.setChecked(true);
			}
			break;
		}

		case R.id.pay_unionpay_rl: {
			if (!mUnionpayCb.isChecked()) {
				mUnionpayCb.setChecked(true);
			}
			break;
		}

		default:
			break;
		}
	}

}
