package com.plmt.boommall.ui.view.srollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class BorderScrollView extends ScrollView {

	private OnBorderListener onBorderListener;
	private View contentView;
	private int headerHeight;

	public BorderScrollView(Context context) {
		super(context);

	}

	public BorderScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public BorderScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		doOnBorderListener();
	}

	public void setOnBorderListener(final OnBorderListener onBorderListener) {
		this.onBorderListener = onBorderListener;
		if (onBorderListener == null) {
			return;
		}

		if (contentView == null) {
			contentView = getChildAt(0);
		}
	}

	public static interface OnBorderListener {

		/**
		 * Called when scroll to bottom
		 */
		public void onBottom();

		/**
		 * Called when scroll to top
		 */
		public void onTop();

		/**
		 * Called when scroll onLeaveTop
		 */
		public void onLeaveTop();
	}

	private void doOnBorderListener() {
		if (contentView != null
				&& contentView.getMeasuredHeight() <= getScrollY()
						+ getHeight()) {
			if (onBorderListener != null) {
				onBorderListener.onLeaveTop();
				onBorderListener.onBottom();
			}
		} else if (getScrollY() <= headerHeight) {
			if (onBorderListener != null) {
				onBorderListener.onTop();
			}
		} else {
			if (onBorderListener != null) {
				onBorderListener.onLeaveTop();
			}
		}

		// if (contentView != null
		// && contentView.getMeasuredHeight() <= getScrollY()
		// + getHeight()) {
		// if (getScrollY() <= headerHeight) {
		// if (onBorderListener != null) {
		// Log.e("xxx_issv_onTop()", "");
		// onBorderListener.onTop();
		// }
		// }
		// } else {
		// if (onBorderListener != null) {
		// Log.e("xxx_issv_onLeaveTop()", "");
		// onBorderListener.onLeaveTop();
		// }
		// }
	}

	public void setHeadHeight(int height) {
		headerHeight = height;
	}

}
