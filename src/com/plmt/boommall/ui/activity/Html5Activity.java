package com.plmt.boommall.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.Button;

import com.google.gson.Gson;
import com.plmt.boommall.R;
import com.plmt.boommall.ui.view.webview.jsbridge.BridgeHandler;
import com.plmt.boommall.ui.view.webview.jsbridge.BridgeWebView;
import com.plmt.boommall.ui.view.webview.jsbridge.CallBackFunction;
import com.plmt.boommall.ui.view.webview.jsbridge.DefaultHandler;

public class Html5Activity extends Activity implements OnClickListener {

	private final String TAG = "xxx_Html5Activity";

	BridgeWebView webView;

	Button button;

	int RESULT_CODE = 0;

	ValueCallback<Uri> mUploadMessage;

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

		button = (Button) findViewById(R.id.button);

		button.setOnClickListener(this);

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
				mUploadMessage = uploadMsg;
				pickFile();
			}
		});

		webView.loadUrl("http://120.55.116.206:8060/demo.html");
		// webView.loadUrl("http://120.55.116.206:8060/webview_test");
		// webView.loadUrl("file:///android_asset/demo.html");

		webView.registerHandler("submitFromWeb", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				Log.e(TAG, "handler = submitFromWeb, data from web = " + data);
				function.onCallBack("submitFromWeb exe, response data from Java");
			}

		});

		User user = new User();
		Location location = new Location();
		location.address = "SDU";
		user.location = location;
		user.name = "Bruce";

		webView.callHandler("functionInJs", new Gson().toJson(user),
				new CallBackFunction() {
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
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == RESULT_CODE) {
			if (null == mUploadMessage) {
				return;
			}
			Uri result = intent == null || resultCode != RESULT_OK ? null
					: intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}

	@Override
	public void onClick(View v) {
		if (button.equals(v)) {
			webView.callHandler("functionInJs", "data from Java",
					new CallBackFunction() {

						@Override
						public void onCallBack(String data) {
							// TODO Auto-generated method stub
							Log.e(TAG, "reponse data from js " + data);
						}

					});
		}

	}

}
