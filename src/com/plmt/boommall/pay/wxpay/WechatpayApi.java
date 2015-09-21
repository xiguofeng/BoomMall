package com.plmt.boommall.pay.wxpay;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.plmt.boommall.pay.WechatpayMerchant;
import com.plmt.boommall.pay.wxpay.simcpux.MD5;
import com.plmt.boommall.pay.wxpay.simcpux.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WechatpayApi {

	private static final String TAG = "wxpayApi";

	public static final int PREPAY_ID_GET_SUC = 0;
	public static final int PREPAY_ID_GET_FAIL = PREPAY_ID_GET_SUC + 1;
	public static final int PREPAY_ID_GET_EXCEPTION = PREPAY_ID_GET_FAIL + 1;

	Map<String, String> mResultunifiedorder;
	// StringBuffer mSb;

	private Activity mActivity;

	private Handler mHandler;

	public WechatpayApi() {

	}

	public WechatpayApi(Activity activity, Handler handler) {

	}

	/*************************************************
	 * 步骤1：生成预支付订单
	 ************************************************/
	/**
	 * 步骤1：生成预支付订单
	 * 
	 * @param activity
	 * @param handler
	 */
	public void getPrepayId(final Activity activity, final Handler handler,
			final WechatpayMerchant wechatpayMerchant, final String total_fee) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				String url = String
						.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
				String entity = genProductArgs(wechatpayMerchant, total_fee);

				Log.e("orion", entity);

				byte[] buf = Util.httpPost(url, entity);

				String content = new String(buf);
				Log.e("orion", content);
				Map<String, String> xml = decodeXml(content);

				Message message = new Message();
				message.what = PREPAY_ID_GET_SUC;
				message.obj = xml;
				handler.sendMessage(message);
			}
		}).start();

	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;

	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		Log.e("orion", sb.toString());
		return sb.toString();
	}

	@SuppressWarnings("deprecation")
	private String genProductArgs(WechatpayMerchant wechatpayMerchant,
			String total_fee) {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", wechatpayMerchant
					.getAppId()));
			packageParams.add(new BasicNameValuePair("body", "酒"));
			packageParams.add(new BasicNameValuePair("mch_id",
					wechatpayMerchant.getPartnerId()));
			packageParams.add(new BasicNameValuePair("nonce_str",
					wechatpayMerchant.getOut_trade_no()));
			packageParams.add(new BasicNameValuePair("notify_url",
					wechatpayMerchant.getNotifyUrl()));
			packageParams.add(new BasicNameValuePair("out_trade_no",
					wechatpayMerchant.getOut_trade_no()));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee", total_fee));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams, wechatpayMerchant);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);
			xmlstring = new String(xmlstring.getBytes("UTF-8"), "ISO-8859-1");

			return xmlstring;

		} catch (Exception e) {
			//Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
			return null;
		}

	}

	/**
	 * 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params,
			WechatpayMerchant wechatpayMerchant) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(wechatpayMerchant.getApiKey());

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", packageSign);
		return packageSign;
	}

	/*************************************************
	 * 步骤2：生成支付参数
	 ************************************************/
	public void genPayReq(Activity activity, Handler handler,
			Map<String, String> resultunifiedorder,
			WechatpayMerchant wechatpayMerchant) {
		PayReq req = new PayReq();
		req.appId = wechatpayMerchant.getAppId();
		req.partnerId = wechatpayMerchant.getPartnerId();
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams, wechatpayMerchant);

		// mSb.append("sign\n" + req.sign + "\n\n");
		sendPayReq(activity, req, wechatpayMerchant);
	}

	private String genAppSign(List<NameValuePair> params,
			WechatpayMerchant wechatpayMerchant) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(wechatpayMerchant.getApiKey());

		// this.mSb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", appSign);
		return appSign;
	}

	/*************************************************
	 * 步骤3：调起微信支付
	 ************************************************/
	private void sendPayReq(Context context, PayReq req,
			WechatpayMerchant wechatpayMerchant) {
		//Log.e("xxx_sendPayReq", "start");
		final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);

		msgApi.registerApp(wechatpayMerchant.getAppId());
		msgApi.sendReq(req);
		//Log.e("xxx_sendPayReq", "end");
	}

}
