package com.plmt.boommall.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.plmt.boommall.R;
import com.plmt.boommall.ui.view.webview.jsbridge.BridgeHandler;
import com.plmt.boommall.ui.view.webview.jsbridge.BridgeWebView;
import com.plmt.boommall.ui.view.webview.jsbridge.CallBackFunction;
import com.plmt.boommall.ui.view.webview.jsbridge.DefaultHandler;

public class Html5Activity extends Activity implements OnClickListener {

	private final String TAG = "xxx_Html5Activity";

	BridgeWebView webView;

	int RESULT_CODE = 0;

	ValueCallback<Uri> mUploadMessage;

	private String mUrl = "http://120.55.116.206:8060/webview_test";

	static class Location {
		String address;
	}

	static class User {
		String name;
		Location location;
		String testStr;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.h5_jsbridge);

		webView = (BridgeWebView) findViewById(R.id.webView);

		webView.setDefaultHandler(new DefaultHandler());

		webView.setWebChromeClient(new WebChromeClient() {

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
				this.openFileChooser(uploadMsg);
			}

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
				this.openFileChooser(uploadMsg);
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				mUploadMessage = uploadMsg;
				pickFile();
			}
		});
		
		initData();
	}

	private void initData() {
		String url = getIntent().getStringExtra("url");
		if (!TextUtils.isEmpty(url)) {
			mUrl = url;
		}
		// webView.loadUrl("http://120.55.116.206:8060/demo.html");
		webView.loadUrl(mUrl);
		// webView.loadUrl("file:///android_asset/demo.html");
		// webView.loadUrl("file:///android_asset/ExampleApp.html");

		webView.registerHandler("testObjcCallback", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				Log.e(TAG, "handler = submitFromWeb, data from web = " + data);
				function.onCallBack("submitFromWeb exe, response data from Java");
				try {
					JSONObject jsonObject = new JSONObject(data);
					String id = jsonObject.getString("id");
					Intent intent = new Intent(Html5Activity.this, GoodsDetailActivity.class);
					intent.setAction(GoodsDetailActivity.ORIGIN_FROM_CATE_ACTION);
					Bundle bundle = new Bundle();
					bundle.putSerializable(GoodsDetailActivity.GOODS_ID_KEY, id);
					intent.putExtras(bundle);
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

		webView.callHandler("testJavascriptHandler", "", new CallBackFunction() {
			@Override
			public void onCallBack(String data) {

			}
		});

		webView.send("hello");
	}

	public void pickFile() {
		Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
		chooserIntent.setType("image/*");
		startActivityForResult(chooserIntent, RESULT_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == RESULT_CODE) {
			if (null == mUploadMessage) {
				return;
			}
			Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}

	@Override
	public void onClick(View v) {

	}

}
