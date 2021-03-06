package com.plmt.boommall.ui.view.srollview.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class SlidingMenu extends ScrollView {
	private int mScreenHeight;
	// private int mOnePage;
	// private int mMenuPadding=220;

	private YsnowScrollViewPageOne wrapperMenu;
	private YsnowWebView wrapperContent;
	private boolean isSetted = false;
	private boolean ispageOne = true;

	private String mUrl;

	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 获得屏幕的宽度和计算设置的偏移量的像素值,并计算出menu的宽度
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);
		mScreenHeight = metrics.heightPixels;// 得到屏幕的宽度(像素)
	}

	public SlidingMenu(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!isSetted) {
			// 得到里面的控件
			final LinearLayout wrapper = (LinearLayout) getChildAt(0);
			wrapperMenu = (YsnowScrollViewPageOne) wrapper.getChildAt(0);
			wrapperContent = (YsnowWebView) wrapper.getChildAt(1);
			WebSettings settings = wrapperContent.getSettings();
			settings.setJavaScriptEnabled(true);
			settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			settings.setLoadWithOverviewMode(true);
			settings.setSupportZoom(true);
			settings.setBuiltInZoomControls(true);
	
			wrapperContent.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					wrapperContent.loadUrl(url);
					return true;
				}
			});
			// 设置两个子View的高度为手机的高度
			wrapperMenu.getLayoutParams().height = mScreenHeight;
			wrapperContent.getLayoutParams().height = mScreenHeight;
			isSetted = true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(0, 0);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			// 隐藏在左边的距离
			int scrollY = getScrollY();
			int creteria = mScreenHeight / 5;// 滑动多少距离
			if (ispageOne) {
				if (scrollY <= creteria) {
					// 显示菜单
					this.smoothScrollTo(0, 0);
				} else {
					// 隐藏菜单
					this.smoothScrollTo(0, mScreenHeight);
					wrapperContent.loadUrl(mUrl);
					this.setFocusable(false);
					ispageOne = false;
				}
			} else {
				int scrollpadding = mScreenHeight - scrollY;
				if (scrollpadding >= creteria) {
					this.smoothScrollTo(0, 0);
					ispageOne = true;
				} else {
					this.smoothScrollTo(0, mScreenHeight);
					wrapperContent.loadUrl(mUrl);
				}
			}

			return true;
		}
		return super.onTouchEvent(ev);
	}

	public void closeMenu() {
		if (ispageOne)
			return;
		this.smoothScrollTo(0, 0);
		ispageOne = true;
	}

	public void openMenu() {
		if (!ispageOne)
			return;
		this.smoothScrollTo(0, mScreenHeight);
		ispageOne = false;
	}

	/**
	 * 打开和关闭菜单
	 */
	public void toggleMenu() {
		if (ispageOne) {
			openMenu();
		} else {
			closeMenu();
		}
	}

	public String getmUrl() {
		return mUrl;
	}

	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}

}
