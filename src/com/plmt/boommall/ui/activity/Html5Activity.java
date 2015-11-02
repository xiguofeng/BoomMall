package com.plmt.boommall.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.plmt.boommall.R;
import com.plmt.boommall.ui.view.CustomProgressDialog;
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

	private ImageView mBackIv;

	private CustomProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.h5_jsbridge);

		mBackIv = (ImageView) findViewById(R.id.h5_back_iv);
		mBackIv.setOnClickListener(this);

		webView = (BridgeWebView) findViewById(R.id.webView);

		webView.setDefaultHandler(new DefaultHandler());

		webView.setWebChromeClient(new WebChromeClient() {

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String AcceptType, String capture) {
				this.openFileChooser(uploadMsg);
			}

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String AcceptType) {
				this.openFileChooser(uploadMsg);
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
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
					Intent intent = new Intent(Html5Activity.this,
							GoodsDetailActivity.class);
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

		webView.callHandler("testJavascriptHandler", "",
				new CallBackFunction() {
					@Override
					public void onCallBack(String data) {

					}
				});

		webView.send("hello");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.h5_back_iv: {
			Html5Activity.this.finish();
			break;
		}
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			Html5Activity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
