package com.plmt.boommall.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plmt.boommall.R;
import com.plmt.boommall.pay.wxpay.simcpux.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	private TextView mInfoTv;

	private LinearLayout mConfirmLl;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wechatpay_result);
		mInfoTv = (TextView) findViewById(R.id.wxpay_result_tv);
		mConfirmLl = (LinearLayout) findViewById(R.id.wxpay_dialog_confirm);
		mConfirmLl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				WXPayEntryActivity.this.finish();
			}
		});

		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.9
		// p.alpha = 1.0f; // 设置本身透明度
		p.dimAmount = 0.6f;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		getWindow().setAttributes(p);

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		// Log.e(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			//PayActivity.sWxRespCode = String.valueOf(resp.errCode);
			String toastStr = "";
			if (0 == resp.errCode) {
				toastStr = "支付成功！";
			} else if (-1 == resp.errCode) {
				toastStr = "支付发生错误！";
			} else if (-2 == resp.errCode) {
				toastStr = "您已取消支付！";
			}

//			mInfoTv.setText(getString(R.string.pay_result_callback_msg,
//					toastStr));
		}
	}
}