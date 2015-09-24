package com.plmt.boommall.ui.view.gridview.paging;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.plmt.boommall.R;

public class LoadingView extends LinearLayout {

	public LoadingView(Context context) {
		super(context);
		init();
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.view_gv_paging_loading, this);
	}

}
