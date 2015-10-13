package com.plmt.boommall.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.plmt.boommall.R;

@SuppressLint("JavascriptInterface")
public class HtmlSourceActivity extends Activity {
	private WebView webView;

	/** Called when the activity is first created. */
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.h5_common);
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
		webView.setWebViewClient(new MyWebViewClient());

		webView.getSettings().setDomStorageEnabled(true);
		webView.requestFocus();
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.loadUrl("http://www.baidu.com");
	}

	final class MyWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.e("WebView", "onPageStarted");
			super.onPageStarted(view, url, favicon);
		}

		public void onPageFinished(WebView view, String url) {
			Log.e("xxx_WebView", "onPageFinished ");
			view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
					+ "document.getElementsByTagName('html')[0].innerHTML+'</head>');");

			addImageClickListner();
			super.onPageFinished(view, url);
		}
	}

	// 注入js函数监听
	private void addImageClickListner() {
		// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
		webView.loadUrl("javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"img\"); "
				+ "for(var i=0;i<objs.length;i++)  " + "{"
				+ "    objs[i].onclick=function()  " + "    {  "
				+ "        window.android.toshow(this.src);  " + "    }  "
				+ "}" + "})()");
	}

	public void toshow(String str) {
		Log.e("xxx_WebView_pic", str);
		Intent intent = new Intent(this, GoodsDetailActivity.class);
		intent.putExtra("picPath", str);
		this.startActivity(intent);
	}

	final class InJavaScriptLocalObj {
		public void showSource(String html) {
			Log.e("xxx_HTML", html);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack();// 返回上一页面
				return true;
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
